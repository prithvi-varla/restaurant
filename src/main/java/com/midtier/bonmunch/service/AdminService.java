package com.midtier.bonmunch.service;

import com.midtier.bonmunch.factory.UserDomainFactory;
import com.midtier.bonmunch.repository.dao.OrderRepository;
import com.midtier.bonmunch.repository.dao.UserRepository;
import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.model.UserDTO;
import com.midtier.bonmunch.security.JWTUtil;
import com.midtier.bonmunch.security.PBKDF2Encoder;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.AdminSummary;
import com.midtier.bonmunch.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository1;


    @Autowired
    UserDomainFactory userDomainFactory;


    public Mono<AdminSummary> getAllOrdersAdminInfo(ApiPrincipal apiPrincipal) {

        List<Mono<?>> list = new ArrayList<>();

        Mono<List<OrderDTO>> getAllOrdersMono =
                getAllOrders(apiPrincipal);
        list.add(getAllOrdersMono);

        // get Map of UUID and Name for top 5 orders
        Mono<Map<UUID, String>> NameMap =
                getAllOrdersMono
                        .flatMapIterable(orderDTOList -> orderDTOList)
                        .flatMap(orderDTO ->  getNameFromUserId(orderDTO))
                        .collectMap( s -> s.getKey(), e -> e.getValue());
        list.add(NameMap);

        return
                Mono.zip(
                        list,
                        results -> userDomainFactory.getAllOrdersAdminInfo(results)
                );
    }

    public Mono<AdminSummary> getAdminSummaryInfo(ApiPrincipal apiPrincipal) {

        List<Mono<?>> list = new ArrayList<>();

        // Top 5 Orders
        Mono<List<OrderDTO>> topFiveOrders =
                getTopFiveOrder(apiPrincipal);
        list.add(topFiveOrders);

        // Total orders count
        Mono<Long> allOrdersCount = getAllOrdersCount(apiPrincipal);
        list.add(allOrdersCount);

        // Sum of all orders Total
        Mono<String> sumOfAllOrdersTotal =
                getSumOfAllOrdersTotal(apiPrincipal);
        list.add(sumOfAllOrdersTotal);

        // last seven days orders
        Mono<List<OrderDTO>> lastSevenDaysOrders = getLastSevenDaysOrders(apiPrincipal);
        list.add(lastSevenDaysOrders);

        // before last seven days orders
        Mono<List<OrderDTO>> beforeLastSevenDaysOrders = getBeforeLastSevenDaysOrders(apiPrincipal);
        list.add(beforeLastSevenDaysOrders);

        //  Total Users count
        Mono<Long> allUsersCount = getAllUsersCount(apiPrincipal);
        list.add(allUsersCount);

        // last seven days users
        Mono<List<UserDTO>> lastSevenDaysUsers = getLastSevenDaysUsers(apiPrincipal);
        list.add(lastSevenDaysUsers);

        // before last seven days users
        Mono<List<User>> beforeLastSevenDaysUsers = getBeforeLastSevenDaysUsers(apiPrincipal);
        list.add(beforeLastSevenDaysUsers);

        // get Map of UUID and Name for top 5 orders
        Mono<Map<UUID, String>> NameMap =
                topFiveOrders
                        .flatMapIterable(orderDTOList -> orderDTOList)
                        .flatMap(orderDTO ->  getNameFromUserId(orderDTO))
                        .collectMap( s -> s.getKey(), e -> e.getValue());
        list.add(NameMap);


        // last 1 month orders Total
        Mono<String> ordersLast1Month = getBeforeAndAfterCreatedDateRange(apiPrincipal, 1, 0);
        list.add(ordersLast1Month);

        // last 2 month orders Total
        Mono<String> ordersLast2Month = getBeforeAndAfterCreatedDateRange(apiPrincipal, 2, 1);
        list.add(ordersLast2Month);

        // last 3 month orders Total
        Mono<String> ordersLast3Month = getBeforeAndAfterCreatedDateRange(apiPrincipal, 3, 2);
        list.add(ordersLast3Month);

        return
                Mono.zip(
                        list,
                        results -> userDomainFactory.getAdminInfo(results)
                );

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

    public Mono<String> getBeforeAndAfterCreatedDateRange(ApiPrincipal apiPrincipal, int from, int to) {

        return
                orderRepository1.findByCompanyIdAndCreatedDateBetween(
                        apiPrincipal.getCompanyId(), LocalDateTime.now().minusMonths(from), LocalDateTime.now().minusMonths(to))
                                .subscribeOn(Schedulers.elastic())
                                .publishOn(Schedulers.parallel())
                                .map(orderDTO -> Float.parseFloat(orderDTO.getTotal()))
                                .reduce(new Float(0.0), (a, b) -> a + b)
                                .map(result -> String.valueOf(result));
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


    private Mono<Map.Entry<UUID, String>> getNameFromUserId(OrderDTO dit) {
        UUID tt = dit.getUserId();
        return userRepository.findByUserId(dit.getUserId())
                             .map(t -> new AbstractMap.SimpleEntry<>(
                                     tt,
                                     t.getFirstName()));
    }
}
