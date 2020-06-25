package com.midtier.bonmunch.dummy.web.model;

import com.midtier.bonmunch.web.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemsInfo {

    List<Product> menuItems;

}
