package com.capitalfloat.assignment.cartitems.response;

import lombok.Data;

import java.util.List;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class CheckoutCartResponse {

  private List<String> productsCheckedOut;
  private String userId;
  private double totalCost;
  private List<String> productsUnavailable;

}
