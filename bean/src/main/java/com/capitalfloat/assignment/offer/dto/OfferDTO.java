package com.capitalfloat.assignment.offer.dto;

import com.capitalfloat.assignment.offer.type.OfferType;
import lombok.Data;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Data
public class OfferDTO {

  private String offerId;
  private String productId;
  private int minimumQuantity;
  private int discount;
  private OfferType offerType;

}
