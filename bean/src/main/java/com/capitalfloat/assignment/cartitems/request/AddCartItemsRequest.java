package com.capitalfloat.assignment.cartitems.request;

import lombok.Data;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class AddCartItemsRequest {

  private String userId;
  private String productId;
  private int quantity;

}
