package com.orderfresh.midtier.restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.orderfresh.midtier.restaurant.exception.DuplicateRecordException;
import com.orderfresh.midtier.restaurant.factory.UserDomainFactory;
import com.orderfresh.midtier.restaurant.repository.dao.MenuItemRepository1;
import com.orderfresh.midtier.restaurant.repository.dao.OrderRepository1;
import com.orderfresh.midtier.restaurant.repository.model.OrderDTO;
import com.orderfresh.midtier.restaurant.security.model.ApiPrincipal;
import com.orderfresh.midtier.restaurant.web.model.AdminInfo;
import com.orderfresh.midtier.restaurant.web.model.MenuItem1;
import com.orderfresh.midtier.restaurant.web.model.Order1;
import com.orderfresh.midtier.restaurant.web.model.User;
import com.orderfresh.midtier.restaurant.repository.dao.UserRepository;
import com.orderfresh.midtier.restaurant.repository.model.UserDTO;
import com.orderfresh.midtier.restaurant.security.JWTUtil;
import com.orderfresh.midtier.restaurant.security.PBKDF2Encoder;
import com.orderfresh.midtier.restaurant.security.model.AuthRequest;
import com.orderfresh.midtier.restaurant.security.model.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author ard333
 */
@Service
public class UserService {

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository1 orderRepository1;

    @Autowired
    MenuItemRepository1 menuItemRepository;

    @Autowired
    UserDomainFactory userDomainFactory;

    public Mono<User> createNewUser(Mono<User> companyMono) {

        return companyMono.flatMap(company -> getUserMono(company));
    }

    private Mono<User> getUserMono(User user) {
        return userRepository.findByEmailAddress(userDomainFactory
                                                         .getUserDTOBuild(user)
                                                         .getEmailAddress())
                                .publishOn(Schedulers.elastic())
                                .subscribeOn(Schedulers.parallel())
                                .map(userDTO -> userDomainFactory.getUserBuild(userDTO))
                                .flatMap (userObject -> getError())
                                .switchIfEmpty(Mono.defer(() -> createNewUser(user)));
    }

    private Mono<User> getError() {
        return Mono.error(new DuplicateRecordException(
                "error.restaurant.user.duplicate"));
    }

    private Mono<User> createNewUser(User user) {
        UserDTO userDTO = userDomainFactory.getUserDTOBuild(user);
        return Mono.just(userDTO).flatMap(userRepository::save)
                   .map(userDTOObject -> userDomainFactory.getUserBuild(userDTOObject));
    }


    public  Mono<AuthResponse> getUserLoginInfo(AuthRequest ar){

       return userRepository.findByEmailAddress(ar.getUsername())
                      .switchIfEmpty(Mono.defer(() -> Mono.empty()))
                      .filter(userDetails ->
                                      passwordEncoder.encode(
                                              ar.getPassword()).equals(userDetails.getPassword()))
                      .map(result -> new AuthResponse(jwtUtil.generateToken(result)));
    }


    public Mono<Order1> createOrder(Mono<Order1> order, ApiPrincipal apiPrincipal) {

        return
                order.map(orderObject -> userDomainFactory.getOrderDTOBuild(orderObject, apiPrincipal))
                     .publishOn(Schedulers.elastic())
                     .flatMap(orderRepository1::save)
                     .publishOn(Schedulers.elastic())
                     .subscribeOn(Schedulers.parallel())
                     .map(orderDTO -> userDomainFactory.getOrderBuild(orderDTO));


    }


    public Mono<MenuItem1> createMenuItem(Mono<MenuItem1> menuItem, ApiPrincipal apiPrincipal) {

        return
                menuItem.map(menuItemObject -> userDomainFactory.getMenuItemDTOBuild(menuItemObject, apiPrincipal))
                     .publishOn(Schedulers.elastic())
                     .flatMap(menuItemRepository::save)
                     .publishOn(Schedulers.elastic())
                     .subscribeOn(Schedulers.parallel())
                     .map(menuItemDTO1 -> userDomainFactory.getMenuItemBuild(menuItemDTO1));


    }

    public Mono<AdminInfo> getAllOrdersAdminInfo(ApiPrincipal apiPrincipal) {

        Mono<List<OrderDTO>> getAllOrdersMono =
                getAllOrders(apiPrincipal);

        return
                getAllOrdersMono.map(orderDTOS -> userDomainFactory.getAllOrdersAdminInfo(orderDTOS));
    }


    public Mono<AdminInfo> getAdminInfo(ApiPrincipal apiPrincipal) {

        List<Mono<?>> list = new ArrayList<>();

        Mono<List<OrderDTO>> topFiveOrders =
                getTopFiveOrder(apiPrincipal);
        list.add(topFiveOrders);

        Mono<Long> allOrdersCount = getAllOrdersCount(apiPrincipal);
        list.add(allOrdersCount);

        Mono<String> sumOfAllOrdersTotal =
                getSumOfAllOrdersTotal(apiPrincipal);
        list.add(sumOfAllOrdersTotal);

        Mono<List<OrderDTO>> lastSevenDaysOrders = getLastSevenDaysOrders(apiPrincipal);
        list.add(lastSevenDaysOrders);

        Mono<List<OrderDTO>> beforeLastSevenDaysOrders = getBeforeLastSevenDaysOrders(apiPrincipal);
        list.add(beforeLastSevenDaysOrders);




        Mono<Long> allUsersCount = getAllUsersCount(apiPrincipal);
        list.add(allUsersCount);

        Mono<List<UserDTO>> lastSevenDaysUsers = getLastSevenDaysUsers(apiPrincipal);
        list.add(lastSevenDaysUsers);

        Mono<List<User>> beforeLastSevenDaysUsers = getBeforeLastSevenDaysUsers(apiPrincipal);
        list.add(beforeLastSevenDaysUsers);

        return
                Mono.zip(
                        list,
                        results -> userDomainFactory.getAdminInfo(results)
                );

       /* return
                Mono.zip(topFiveOrders, allOrdersCount, sumOfAllOrdersTotal, lastSevenDaysOrders, beforeLastSevenDaysOrders,
                         sumOfAllUsersTotal, allUsersCount)
                    .map(tuple -> userDomainFactory.getAdminInfo(
                            tuple.getT1(), // topFiveOrders
                            tuple.getT2(), // allOrdersCount
                            tuple.getT3(), // sumOfAllOrdersTotal
                            tuple.getT4(), // lastSevenDaysOrders
                            tuple.getT5(), // beforeLastSevenDaysOrders

                            tuple.getT6()
                    ));*/

    }

    public Mono<List<OrderDTO>> getTopFiveOrder(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findTop5ByCompanyIdOrderByCreatedDateDesc(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }

    public Mono<Long> getAllOrdersCount(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .count();
    }


    // Can be used later to get all orders of an entity
    public Mono<List<OrderDTO>> getAllOrders(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }

    public Mono<String> getSumOfAllOrdersTotal(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .map(orderDTO -> Float.parseFloat(orderDTO.getTotal()))
                                .reduce(new Float(0.0), (a, b) -> a + b)
                                .map(result -> String.valueOf(result));
    }

    public Mono<List<OrderDTO>> getLastSevenDaysOrders(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findByCompanyIdAndCreatedDateGreaterThan(apiPrincipal.getCompanyId(), LocalDateTime.now().minusDays(7))
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }


    public Mono<List<OrderDTO>> getBeforeLastSevenDaysOrders(ApiPrincipal apiPrincipal) {

        return
                orderRepository1.findByCompanyIdAndCreatedDateLessThan(apiPrincipal.getCompanyId(), LocalDateTime.now().minusDays(7))
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }










    public Mono<List<UserDTO>> getTopFiveUsers(ApiPrincipal apiPrincipal) {

        return
                userRepository.findTop5ByCompanyIdOrderByCreatedDateDesc(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }

    public Mono<Long> getAllUsersCount(ApiPrincipal apiPrincipal) {

        return
                userRepository.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .count();
    }

    // Can be used later to get all users of an entity
    public Mono<List<UserDTO>> getAllUsers(ApiPrincipal apiPrincipal) {

        return
                userRepository.findByCompanyId(apiPrincipal.getCompanyId())
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }

    public Mono<List<UserDTO>> getLastSevenDaysUsers(ApiPrincipal apiPrincipal) {

        return
                userRepository.findByCompanyIdAndCreatedDateGreaterThan(apiPrincipal.getCompanyId(), LocalDateTime.now().minusDays(7))
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .collectList();
    }


    public Mono<List<User>> getBeforeLastSevenDaysUsers(ApiPrincipal apiPrincipal) {

        return
                userRepository.findByCompanyIdAndCreatedDateLessThan(apiPrincipal.getCompanyId(), LocalDateTime.now().minusDays(7))
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .map(userDTO -> userDomainFactory.getUserBuild(userDTO))
                                .collectList();
    }




}