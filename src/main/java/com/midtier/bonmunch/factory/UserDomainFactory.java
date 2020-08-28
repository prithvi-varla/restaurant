package com.midtier.bonmunch.factory;

import com.google.common.collect.Lists;
import com.midtier.bonmunch.model.DailySummay;
import com.midtier.bonmunch.model.LastOrder;
import com.midtier.bonmunch.model.MonthlySummary;
import com.midtier.bonmunch.model.TotalSummary;
import com.midtier.bonmunch.web.model.AdminSummary;
import com.midtier.bonmunch.web.model.Order;
import com.midtier.bonmunch.web.model.OrderItem;
import com.midtier.bonmunch.web.model.OrderItemOption;
import com.midtier.bonmunch.web.model.OrderStatus;
import com.midtier.bonmunch.web.model.Role;
import com.midtier.bonmunch.web.model.User;
import com.midtier.bonmunch.repository.dao.UserRepository;
import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.model.OrderItemDTO;
import com.midtier.bonmunch.repository.model.OrderItemOptionDTO;
import com.midtier.bonmunch.repository.model.UserDTO;
import com.midtier.bonmunch.security.PBKDF2Encoder;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UserDomainFactory {

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public UserDTO getUserDTOBuild(User user, boolean isPasswordChanged, UUID companyId) {
        UserDTO sdf =  UserDTO.builder()
                      .userId(user.getUserId() == null ? UUID.randomUUID() : user.getUserId())
                      .companyId(companyId)
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .emailAddress(user.getEmailAddress() == null ? user.getUsername() : user.getEmailAddress())
                      .username(user.getUsername() == null ? user.getEmailAddress() : user.getUsername())
                      .password(isPasswordChanged ? passwordEncoder.encode(user.getPassword()) : user.getPassword() )
                      .level(user.getLevel())
                      .roles(user.getRoles() == null ? Role.ROLE_CUSTOMER.toString() : user.getRoles())
                      .active(user.getActive() == null ? Boolean.FALSE : user.getActive())
                      .createdDate(LocalDateTime.now())
                      .updatedDate(LocalDateTime.now())
                      .build();

        return sdf;
    }

    public User getUserBuild(UserDTO userDTO) {
        return User.builder()
                   .userId(userDTO.getUserId())
                   .companyId(userDTO.getCompanyId())
                   .userId(userDTO.getUserId())
                   .username(userDTO.getUsername())
                   .firstName(userDTO.getFirstName())
                   .lastName(userDTO.getLastName())
                   .emailAddress(userDTO.getEmailAddress())
                   .password(userDTO.getPassword())
                   .level(userDTO.getLevel())
                   .roles(userDTO.getRoles())
                   .active(userDTO.getActive())
                   .build();
    }


    /*
            Order Domain changes
     */
    public Order getOrderBuild(OrderDTO orderDTO) {
        return Order.builder()
                    .orderId(orderDTO.getOrderId())
                    .userId(orderDTO.getUserId())
                    .companyId(orderDTO.getCompanyId())
                    .orderNumber(orderDTO.getOrderNumber())
                    .subTotal(orderDTO.getSubTotal())
                    .tax(orderDTO.getTax())
                    .tip(orderDTO.getTip())
                    .deliveryFee(orderDTO.getDeliveryFee())
                    .total(orderDTO.getTotal())
                    .orderStatus(orderDTO.getOrderStatus())
                    .deliveryInstructions(orderDTO.getDeliveryInstructions())
                    .orderItems(getOrderItemBuild(orderDTO.getOrderItems()))
                    .build();
    }

    public List<OrderItem> getOrderItemBuild(List<OrderItemDTO> orderItemsDTO) {

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO: orderItemsDTO){
            orderItems.add(OrderItem.builder()
                                    .orderItemId(orderItemDTO.getOrderItemId())
                                    .orderId(orderItemDTO.getOrderId())
                                    .productId(orderItemDTO.getProductId())
                                    .quantity(orderItemDTO.getQuantity())
                                    .itemInstructions(orderItemDTO.getItemInstructions())
                                    .orderItemOptions(getOrderItemOptionBuild(orderItemDTO.getOrderItemOptions()))
                                    .build());
        }
        return orderItems;
    }


    public List<OrderItemOption> getOrderItemOptionBuild(List<OrderItemOptionDTO> orderItemOptionsDTO) {

        List<OrderItemOption> orderItemOptions = new ArrayList<>();
        for (OrderItemOptionDTO orderItemOptionDTO: orderItemOptionsDTO){
            orderItemOptions.add(OrderItemOption
                                         .builder()
                                         .orderItemOptionId(orderItemOptionDTO.getOrderItemOptionId())
                                         .orderItemId(orderItemOptionDTO.getOrderItemId())
                                         .itemOptionId(orderItemOptionDTO.getItemOptionId())
                                         .build());
        }
        return orderItemOptions;
    }


    public OrderDTO getOrderDTOBuild(Order order, ApiPrincipal apiPrincipal) {

        UUID newOrderId = UUID.randomUUID();
        return OrderDTO.builder()
                       .orderId(newOrderId)
                       .userId(apiPrincipal.getUserId())
                       .companyId(apiPrincipal.getCompanyId())
                       .orderNumber(order.getOrderNumber())
                       .subTotal(order.getSubTotal())
                       .tax(order.getTax())
                       .tip(order.getTip())
                       .deliveryFee(order.getDeliveryFee())
                       .total(order.getTotal())
                       .orderStatus(OrderStatus.COMPLETED)
                       .deliveryInstructions(order.getDeliveryInstructions())
                       .createdDate(LocalDateTime.now())
                       .updatedDate(LocalDateTime.now())
                       .orderItems(getOrderItemDTOBuild(order.getOrderItems(), newOrderId))
                       .build();
    }

    public List<OrderItemDTO> getOrderItemDTOBuild(List<OrderItem> orderItems, UUID newOrderId) {


        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem: orderItems){
            UUID newOrderItemId = UUID.randomUUID();
            orderItemDTOs.add(OrderItemDTO.builder()
                                          .orderItemId(newOrderItemId)
                                          .orderId(newOrderId)
                                          .productId(orderItem.getProductId())
                                          .quantity(orderItem.getQuantity())
                                          .itemInstructions(orderItem.getItemInstructions())
                                          .orderItemOptions(getOrderItemOptionDTOBuild(orderItem.getOrderItemOptions(),
                                                                                       newOrderItemId))
                                          .createdDate(LocalDateTime.now())
                                          .updatedDate(LocalDateTime.now())
                                          .build());
        }
        return orderItemDTOs;
    }


    public List<OrderItemOptionDTO> getOrderItemOptionDTOBuild(List<OrderItemOption> orderItemOptions,
                                                               UUID newOrderItemId) {

        List<OrderItemOptionDTO> orderItemOptionDTOs = new ArrayList<>();
        for (OrderItemOption orderItemOption: orderItemOptions){
            orderItemOptionDTOs.add(OrderItemOptionDTO.builder()
                                                      .orderItemOptionId(UUID.randomUUID())
                                                      .orderItemId(newOrderItemId)
                                                      .itemOptionId(orderItemOption.getItemOptionId())
                                                      .createdDate(LocalDateTime.now())
                                                      .updatedDate(LocalDateTime.now())
                                                      .build());
        }
        return orderItemOptionDTOs;
    }



    /*
        GetAllOrderAdminInfo
     */
    public AdminSummary getAllOrdersAdminInfo(Object[] results) {

        int counter = 0;

        List<LastOrder> lastOrders = new ArrayList<>();

        List<OrderDTO> topFiveOrders = (List<OrderDTO>) results[counter++];
        Map<UUID, String> nameMap = (Map<UUID, String>) results[counter++];

        topFiveOrders.stream()
                     .map(orderDTO -> LastOrder
                             .builder()
                             .date(orderDTO.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                             .name(nameMap.get(orderDTO.getUserId()))
                             .status(orderDTO.getOrderStatus() == null ? "" : orderDTO.getOrderStatus().toString())
                             .price(orderDTO.getTotal())
                             .build())
                     .forEachOrdered(lastOrders::add);


        return AdminSummary.builder()
                           .ordersSummary(lastOrders)
                           .build();

    }

    public AdminSummary getAdminInfo(Object[] results) {

        int counter = 0;

        List<OrderDTO> topFiveOrders = (List<OrderDTO>) results[counter++];

        Long allOrdersCount = (Long) results[counter++];

        String sumOfAllOrdersTotal = (String) results[counter++];

        List<OrderDTO> lastSevenDaysOrders = (List<OrderDTO>) results[counter++];

        List<OrderDTO> beforeLastSevenDaysOrders = (List<OrderDTO>) results[counter++];

        Long allUsersCount = (Long) results[counter++];

        List<UserDTO> lastSevenDaysUsers = (List<UserDTO>) results[counter++];

        List<UserDTO> beforeLastSevenDaysUsers = (List<UserDTO>) results[counter++];

        Map<UUID, String> nameMap = (Map<UUID, String>) results[counter++];

        String sumOfAllOrdersTotalForLast1Month = (String) results[counter++];
        String sumOfAllOrdersTotalForLast2Month = (String) results[counter++];
        String sumOfAllOrdersTotalForLast3Month = (String) results[counter++];

        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        List<LastOrder> lastOrders = new ArrayList<>();

        DailySummay dailySummay = DailySummay.builder()
                                             .monthlyUsers(String.valueOf(lastSevenDaysUsers.size()))
                                             .totalUsers(String.valueOf(allUsersCount))
                                             .usersProgress(String.valueOf(
                                                     (lastSevenDaysUsers.size() * 100)/
                                                     (beforeLastSevenDaysUsers.size() == 0 ? 1 :  beforeLastSevenDaysUsers.size()))
                                             )
                                             .monthlyOrders(String.valueOf(lastSevenDaysOrders.size()))
                                             .totalOrders(String.valueOf(allOrdersCount))
                                             .ordersProgress(String.valueOf(
                                                     (lastSevenDaysOrders.size() * 100)/
                                                     (beforeLastSevenDaysOrders.size() == 0 ? 1 :  beforeLastSevenDaysOrders.size()))
                                             )
                                             .build();


        TotalSummary totalSummary = TotalSummary.builder()
                                                .totalUsers(String.valueOf(allUsersCount))
                                                .totalOrders(String.valueOf(allOrdersCount))
                                                .totalRevenue(sumOfAllOrdersTotal)
                                                .monthlyOrdersList(Lists.newArrayList(sumOfAllOrdersTotalForLast1Month, sumOfAllOrdersTotalForLast2Month, sumOfAllOrdersTotalForLast3Month))
                                                .build();
        /*

        monthlySummaries
                .add(MonthlySummary
                             .builder()
                             .value(String.valueOf(lastSevenDaysOrders.size()))
                             .progress(String.valueOf(
                                     (lastSevenDaysOrders.size() * 100)/
                                     (beforeLastSevenDaysOrders.size() == 0 ? 1 :  beforeLastSevenDaysOrders.size()))
                             )
                             .difference(String.valueOf(
                                     (lastSevenDaysOrders.size() * 100)/
                                     (beforeLastSevenDaysOrders.size() == 0 ? 1 :  beforeLastSevenDaysOrders.size())) + "%"
                             )
                             .build());

        monthlySummaries
                .add(MonthlySummary
                             .builder()
                             .value(String.valueOf(lastSevenDaysUsers.size()))
                             .progress(String.valueOf(
                                     (lastSevenDaysUsers.size() * 100)/
                                     (beforeLastSevenDaysUsers.size() == 0 ? 1 :  beforeLastSevenDaysUsers.size()))
                             )
                             .difference(String.valueOf(
                                     (lastSevenDaysOrders.size() * 100)/
                                     (beforeLastSevenDaysUsers.size() == 0 ? 1 :  beforeLastSevenDaysUsers.size())) + "%"
                             )
                             .build());

        */

        topFiveOrders.stream()
                     .map(orderDTO -> LastOrder
                             .builder()
                             .date(orderDTO.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                             .name(nameMap.get(orderDTO.getUserId()))
                             .status(orderDTO.getOrderStatus() == null ? "" : orderDTO.getOrderStatus().toString())
                             .price(orderDTO.getTotal())
                             .build())
                     .forEachOrdered(lastOrders::add);


        return AdminSummary.builder()
                           .dailySummary(dailySummay)
                           .totalSummary(totalSummary)
                           .monthlySummary(monthlySummaries)
                           .ordersSummary(lastOrders)
                           .build();
    }


}
