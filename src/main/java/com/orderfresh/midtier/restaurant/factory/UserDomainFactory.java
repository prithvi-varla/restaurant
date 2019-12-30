package com.orderfresh.midtier.restaurant.factory;

import com.orderfresh.midtier.restaurant.model.DailySummay;
import com.orderfresh.midtier.restaurant.model.LastOrder;
import com.orderfresh.midtier.restaurant.model.MonthlySummary;
import com.orderfresh.midtier.restaurant.model.RestaurantAdminInfo;
import com.orderfresh.midtier.restaurant.repository.dao.UserRepository;
import com.orderfresh.midtier.restaurant.repository.model.ItemOptionDTO;
import com.orderfresh.midtier.restaurant.repository.model.ItemOptionSectionDTO;
import com.orderfresh.midtier.restaurant.repository.model.MenuItemDTO;
import com.orderfresh.midtier.restaurant.repository.model.OrderDTO;
import com.orderfresh.midtier.restaurant.repository.model.OrderItemDTO;
import com.orderfresh.midtier.restaurant.repository.model.OrderItemOptionDTO;
import com.orderfresh.midtier.restaurant.repository.model.UserDTO;
import com.orderfresh.midtier.restaurant.security.PBKDF2Encoder;
import com.orderfresh.midtier.restaurant.security.model.ApiPrincipal;
import com.orderfresh.midtier.restaurant.web.model.AdminInfo;
import com.orderfresh.midtier.restaurant.web.model.ItemOption1;
import com.orderfresh.midtier.restaurant.web.model.ItemOptionSection1;
import com.orderfresh.midtier.restaurant.web.model.MenuItem1;
import com.orderfresh.midtier.restaurant.web.model.Order1;
import com.orderfresh.midtier.restaurant.web.model.OrderItem1;
import com.orderfresh.midtier.restaurant.web.model.OrderItemOption1;
import com.orderfresh.midtier.restaurant.web.model.OrderStatus;
import com.orderfresh.midtier.restaurant.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserDomainFactory {


    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public UserDTO getUserDTOBuild(User user) {
        return UserDTO.builder()
                      .userId(UUID.randomUUID())
                      .companyId(user.getCompanyId())
                      .userId(user.getUserId())
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .emailAddress(user.getEmailAddress())
                      .username(user.getUsername() == null ? user.getEmailAddress() : user.getUsername())
                      .password(passwordEncoder.encode(user.getPassword()))
                      .level(user.getLevel())
                      .roles(user.getRoles())
                      .active(user.getActive() == null ? Boolean.FALSE : user.getActive())
                      .createdDate(LocalDateTime.now())
                      .updatedDate(LocalDateTime.now())
                      .build();
    }

    public User getUserBuild(UserDTO userDTO) {
        return User.builder()
                   .userId(userDTO.getUserId())
                   .companyId(userDTO.getCompanyId())
                   .userId(userDTO.getUserId())
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
    public Order1 getOrderBuild(OrderDTO orderDTO) {
        return Order1.builder()
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

    public List<OrderItem1> getOrderItemBuild(List<OrderItemDTO> orderItemsDTO) {

        List<OrderItem1> orderItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO: orderItemsDTO){
            orderItems.add(OrderItem1.builder()
                                     .orderItemId(orderItemDTO.getOrderItemId())
                                     .orderId(orderItemDTO.getOrderId())
                                     .menuItemId(orderItemDTO.getMenuItemId())
                                     .quantity(orderItemDTO.getQuantity())
                                     .itemInstructions(orderItemDTO.getItemInstructions())
                                     .orderItemOptions(getOrderItemOptionBuild(orderItemDTO.getOrderItemOptions()))
                                     .build());
        }
        return orderItems;
    }


    public List<OrderItemOption1> getOrderItemOptionBuild(List<OrderItemOptionDTO> orderItemOptionsDTO) {

        List<OrderItemOption1> orderItemOptions = new ArrayList<>();
        for (OrderItemOptionDTO orderItemOptionDTO: orderItemOptionsDTO){
            orderItemOptions.add(OrderItemOption1
                                         .builder()
                                         .orderItemOptionId(orderItemOptionDTO.getOrderItemOptionId())
                                         .orderItemId(orderItemOptionDTO.getOrderItemId())
                                         .itemOptionId(orderItemOptionDTO.getItemOptionId())
                                         .build());
        }
        return orderItemOptions;
    }






    public OrderDTO getOrderDTOBuild(Order1 order, ApiPrincipal apiPrincipal) {

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

    public List<OrderItemDTO> getOrderItemDTOBuild(List<OrderItem1> orderItems, UUID newOrderId) {


        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem1 orderItem: orderItems){
            UUID newOrderItemId = UUID.randomUUID();
            orderItemDTOs.add(OrderItemDTO.builder()
                                          .orderItemId(newOrderItemId)
                                          .orderId(newOrderId)
                                          .menuItemId(orderItem.getMenuItemId())
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


    public List<OrderItemOptionDTO> getOrderItemOptionDTOBuild(List<OrderItemOption1> orderItemOptions,
                                                               UUID newOrderItemId) {

        List<OrderItemOptionDTO> orderItemOptionDTOs = new ArrayList<>();
        for (OrderItemOption1 orderItemOption: orderItemOptions){
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
            Menu Domain changes
     */
    public MenuItem1 getMenuItemBuild(MenuItemDTO menuItemDTO) {
        return MenuItem1.builder()
                     .menuItemId(menuItemDTO.getMenuItemId())
                     .userId(menuItemDTO.getUserId())
                     .companyId(menuItemDTO.getCompanyId())
                     .name(menuItemDTO.getName())
                     .price(menuItemDTO.getPrice())
                     .description(menuItemDTO.getDescription())
                     .itemOptionSection(getItemOptionSectionBuild(menuItemDTO.getItemOptionSection()))
                     .build();
    }

    public List<ItemOptionSection1> getItemOptionSectionBuild(List<ItemOptionSectionDTO> itemOptionSectionDTOS) {

        List<ItemOptionSection1> itemOptionSections = new ArrayList<>();
        for (ItemOptionSectionDTO itemOptionSectionDTO: itemOptionSectionDTOS){
            itemOptionSections.add(ItemOptionSection1.builder()
                                     .itemOptionSectionId(itemOptionSectionDTO.getItemOptionSectionId())
                                     .menuItemId(itemOptionSectionDTO.getMenuItemId())
                                     .name(itemOptionSectionDTO.getName())
                                     .description(itemOptionSectionDTO.getDescription())
                                     .required(itemOptionSectionDTO.isRequired())
                                     .minAllowed(itemOptionSectionDTO.getMinAllowed())
                                     .maxAllowed(itemOptionSectionDTO.getMaxAllowed())
                                     .itemOption(getItemOptionBuild(itemOptionSectionDTO.getItemOption()))
                                     .build());
        }
        return itemOptionSections;
    }


    public List<ItemOption1> getItemOptionBuild(List<ItemOptionDTO> itemOptionDTOS) {

        List<ItemOption1> itemOptions = new ArrayList<>();
        for (ItemOptionDTO itemOptionDTO: itemOptionDTOS){
            itemOptions.add(ItemOption1.builder()
                                       .itemOptionId(itemOptionDTO.getItemOptionId())
                                       .itemOptionSectionId(itemOptionDTO.getItemOptionSectionId())
                                       .name(itemOptionDTO.getName())
                                       .description(itemOptionDTO.getDescription())
                                       .price(itemOptionDTO.getPrice())
                                       .build());
        }
        return itemOptions;
    }







    public MenuItemDTO getMenuItemDTOBuild(MenuItem1 menuItem,  ApiPrincipal apiPrincipal) {

        UUID newMenuItemId = UUID.randomUUID();
        return MenuItemDTO.builder()
                          .menuItemId(newMenuItemId)
                          .userId(apiPrincipal.getUserId())
                          .companyId(apiPrincipal.getCompanyId())
                          .name(menuItem.getName())
                          .price(menuItem.getPrice())
                          .description(menuItem.getDescription())
                          .itemOptionSection(getItemOptionSectionDTOBuild(menuItem.getItemOptionSection(),
                                                                          newMenuItemId))
                          .createdDate(LocalDateTime.now())
                          .updatedDate(LocalDateTime.now())
                          .build();
    }

    public List<ItemOptionSectionDTO> getItemOptionSectionDTOBuild(List<ItemOptionSection1>  itemOptionSections,
                                                                   UUID newMenuItemId) {

        List<ItemOptionSectionDTO> itemOptionSectionDTOs = new ArrayList<>();
        for (ItemOptionSection1 itemOptionSection: itemOptionSections){
            UUID itemOptionSectionId = UUID.randomUUID();
            itemOptionSectionDTOs.add(ItemOptionSectionDTO
                                         .builder()
                                         .itemOptionSectionId(itemOptionSectionId)
                                         .menuItemId(newMenuItemId)
                                         .name(itemOptionSection.getName())
                                         .description(itemOptionSection.getDescription())
                                         .required(itemOptionSection.isRequired())
                                         .minAllowed(itemOptionSection.getMinAllowed())
                                         .maxAllowed(itemOptionSection.getMaxAllowed())
                                         .itemOption(getItemOptionDTOBuild(itemOptionSection.getItemOption(),
                                                                           itemOptionSectionId))
                                         .createdDate(LocalDateTime.now())
                                         .updatedDate(LocalDateTime.now())
                                         .build());
        }
        return itemOptionSectionDTOs;
    }


    public List<ItemOptionDTO> getItemOptionDTOBuild(List<ItemOption1> itemOptions, UUID itemOptionSectionId) {

        List<ItemOptionDTO> itemOptionDTOs = new ArrayList<>();
        for (ItemOption1 itemOption: itemOptions){
            itemOptionDTOs.add(ItemOptionDTO.builder()
                                       .itemOptionId(UUID.randomUUID())
                                       .itemOptionSectionId(itemOptionSectionId)
                                       .name(itemOption.getName())
                                       .description(itemOption.getDescription())
                                       .price(itemOption.getPrice())
                                       .createdDate(LocalDateTime.now())
                                       .updatedDate(LocalDateTime.now())
                                       .build());
        }
        return itemOptionDTOs;
    }






   /* public AdminInfo getAdminInfo(List<Order1> topFiveOrders,
                                  Long count,
                                  String sumOfAllOrdersTotal,
                                  List<Order1> lastSevenDaysOrder,
                                  List<Order1> beforeLastSevenDaysOrder,
                                  String sumOfAllUsersTotal) {

        return new AdminInfo();
    }*/

    public AdminInfo getAllOrdersAdminInfo(List<OrderDTO> allOrderDTOs) {

        List<LastOrder> allOrders = new ArrayList<>();

        allOrderDTOs.stream()
                     .map(orderDTO -> LastOrder
                             .builder()
                             .date(orderDTO.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                             .name(userRepository.findByUserId(orderDTO.getUserId())
                                                 .map(userDTO -> userDTO.getFirstName())
                                                 .block())
                             .status(orderDTO.getOrderStatus() == null ? "" : orderDTO.getOrderStatus().toString())
                             .price(orderDTO.getTotal())
                             .build())
                     .forEachOrdered(allOrders::add);


        return AdminInfo.builder()
                        .ordersSummary(allOrders)
                        .build();

    }

    public AdminInfo getAdminInfo(Object[] results) {

        int counter = 0;

        List<OrderDTO> topFiveOrders = (List<OrderDTO>) results[counter++];

        Long allOrdersCount = (Long) results[counter++];

        String sumOfAllOrdersTotal = (String) results[counter++];

        List<OrderDTO> lastSevenDaysOrders = (List<OrderDTO>) results[counter++];

        List<OrderDTO> beforeLastSevenDaysOrders = (List<OrderDTO>) results[counter++];

        Long allUsersCount = (Long) results[counter++];

        List<UserDTO> lastSevenDaysUsers = (List<UserDTO>) results[counter++];

        List<UserDTO> beforeLastSevenDaysUsers = (List<UserDTO>) results[counter++];

        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        List<LastOrder> lastOrders = new ArrayList<>();

        DailySummay dailySummay = DailySummay.builder()
                                             .orders(String.valueOf(allOrdersCount))
                                             .users(String.valueOf(allUsersCount))
                                             .visits(String.valueOf(sumOfAllOrdersTotal))
                                             .build();

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


        topFiveOrders.stream()
                     .map(orderDTO -> LastOrder
                             .builder()
                             .date(orderDTO.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                             .name(userRepository.findByUserId(orderDTO.getUserId())
                                                 .map(userDTO -> userDTO.getFirstName())
                                                 .block())
                             .status(orderDTO.getOrderStatus() == null ? "" : orderDTO.getOrderStatus().toString())
                             .price(orderDTO.getTotal())
                             .build())
                     .forEachOrdered(lastOrders::add);


        return AdminInfo.builder()
                        .dailySummary(dailySummay)
                        .monthlySummary(monthlySummaries)
                        .ordersSummary(lastOrders)
                        .build();
    }


}
