package com.capitalfloat.assignment.cartitems.dto;

import lombok.Data;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Data
public class CartItemDTO {

  private String cartId;
  private String userId;
  private String productId;
  private int quantity;

}
