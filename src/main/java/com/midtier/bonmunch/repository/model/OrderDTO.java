package com.midtier.bonmunch.repository.model;

import com.midtier.bonmunch.web.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDTO {

    @Id
    private UUID orderId;
    private UUID userId;
    private UUID companyId;

    private String orderNumber;
    private String subTotal;
    private String tax;
    private String tip;
    private String deliveryFee;
    private String total;
    private String deliveryInstructions;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
}
