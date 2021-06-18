package com.capitalfloat.assignment.offer.dao;

import com.capitalfloat.assignment.offer.dto.OfferDTO;

import java.util.List;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface OfferDao {

  /***
   * Initialise the required table
   */
  void createTable();

  /***
   * Get all offers for a given productId
   * @param productId
   * @return
   */
  List<OfferDTO> getOffers(String productId);

  /***
   * Add an offer for a given productId
   * @param offerDTO
   * @return
   */
  void addOffer(OfferDTO offerDTO);

  /***
   * Remove an offer based on the given param
   * @param offerId
   * @return
   */
  void removeOffer(String offerId);

}
