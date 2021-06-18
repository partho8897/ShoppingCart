package com.capitalfloat.assignment.offer.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.offer.request.AddOfferRequest;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface OfferService {

  /***
   * Get all offers for a given productId
   * @param productId
   * @return
   */
  ResponseObj getOffers(String productId);

  /***
   * Add an offer for a given productId
   * @param offerDTO
   * @return
   */
  ResponseObj addOffer(AddOfferRequest offerDTO);

  /***
   * Remove an offer based on the given param
   * @param offerId
   * @return
   */
  ResponseObj removeOffer(String offerId);

}
