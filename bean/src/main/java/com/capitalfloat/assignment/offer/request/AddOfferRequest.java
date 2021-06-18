package com.capitalfloat.assignment.offer.request;

import com.capitalfloat.assignment.offer.type.OfferType;
import lombok.Data;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class AddOfferRequest {

  private String productId;
  private int minimumQuantity;
  private int discount;
  private OfferType offerType;

}
