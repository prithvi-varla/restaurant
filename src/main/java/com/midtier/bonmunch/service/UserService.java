package com.midtier.bonmunch.service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

import com.midtier.bonmunch.factory.UserDomainFactory;
import com.midtier.bonmunch.security.JWTUtil;
import com.midtier.bonmunch.security.PBKDF2Encoder;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.security.model.AuthRequest;
import com.midtier.bonmunch.web.model.Order;
import com.midtier.bonmunch.web.model.User;
import com.midtier.bonmunch.exception.DuplicateRecordException;
import com.midtier.bonmunch.repository.dao.ProductRepository;
import com.midtier.bonmunch.repository.dao.OrderRepository;
import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.dao.UserRepository;
import com.midtier.bonmunch.repository.model.UserDTO;
import com.midtier.bonmunch.security.model.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author prithvi
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
    OrderRepository orderRepository;

    @Autowired
    UserDomainFactory userDomainFactory;

    /*
        Create new user
     */
    public Mono<User> createNewUser(Mono<User> userMono, UUID companyId) {

        return userMono.flatMap(user -> userRepository.findByEmailAddress(user.getEmailAddress())
                                                      .publishOn(Schedulers.elastic())
                                                      .subscribeOn(Schedulers.parallel())
                                                      .map(userDTO -> userDomainFactory.getUserBuild(userDTO))
                                                      .flatMap (userObject -> getError())
                                                      .switchIfEmpty(Mono.defer(() -> createNewUser(user, companyId))));
    }

    // Can be used later to get all users of an entity
    public Mono<User> getUserForEntity(ApiPrincipal apiPrincipal, String searchField) {

        return
                userRepository.findByUserId(apiPrincipal.getUserId())
                              .subscribeOn(Schedulers.elastic())
                              .publishOn(Schedulers.parallel())
                              .map(userDTO -> userDomainFactory.getUserBuild(userDTO));
    }

    /*
        update th existing user
     */
    public Mono<User> updateUser(Mono<User> userMono, boolean isPasswordChanged, ApiPrincipal apiPrincipal) {

        return userMono
                    .map(user -> userDomainFactory.getUserDTOBuild(user, isPasswordChanged, apiPrincipal.getCompanyId()))
                   .flatMap(userRepository::save)
                   .map(userDTOObject -> userDomainFactory.getUserBuild(userDTOObject));
    }

    /*
        Create new user for the company/customer
     */
    private Mono<User> createNewUser(User user, UUID companyId) {
        UserDTO userDTO = userDomainFactory.getUserDTOBuild(user, false, companyId);
        return Mono.just(userDTO)
                   .flatMap(userRepository::save)
                   .map(userDTOObject -> userDomainFactory.getUserBuild(userDTOObject));
    }


    public  Mono<AuthResponse> getUserLoginInfo(AuthRequest authRequest){

       return userRepository.findByEmailAddress(authRequest.getUsername())
                      .switchIfEmpty(Mono.defer(() -> Mono.empty()))
                      .filter(userDetails ->
                                      passwordEncoder.encode(
                                              authRequest.getPassword()).equals(userDetails.getPassword()))
                      .map(result -> new AuthResponse(jwtUtil.generateToken(result)));
    }

    private Mono<User> getError() {
        return Mono.error(new DuplicateRecordException(
                "error.user.duplicate"));
    }


    public Mono<Order> createOrder(Mono<Order> order, ApiPrincipal apiPrincipal) {

        return
                order.map(orderObject -> userDomainFactory.getOrderDTOBuild(orderObject, apiPrincipal))
                     .publishOn(Schedulers.elastic())
                     .flatMap(orderRepository::save)
                     .publishOn(Schedulers.elastic())
                     .subscribeOn(Schedulers.parallel())
                     .map(orderDTO -> userDomainFactory.getOrderBuild(orderDTO));
    }

}