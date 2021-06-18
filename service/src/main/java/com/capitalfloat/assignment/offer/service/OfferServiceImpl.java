package com.capitalfloat.assignment.offer.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.offer.dao.OfferDao;
import com.capitalfloat.assignment.offer.dto.OfferDTO;
import com.capitalfloat.assignment.offer.request.AddOfferRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.capitalfloat.assignment.offer.OfferConstants.*;
import static com.capitalfloat.assignment.offer.OfferUtils.validateAddOfferRequest;
import static com.capitalfloat.assignment.product.ProductConstants.PRODUCT_ID_BLANK;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForFailure;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForSuccess;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Service
@Slf4j
public class OfferServiceImpl implements OfferService{

  @Autowired
  private OfferDao offerDao;

  @Override
  public ResponseObj getOffers(String productId) {
    log.info("Received request to fetch offers for productId : {}", productId);
    if(StringUtils.isBlank(productId)){
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try{
      List<OfferDTO> offers = offerDao.getOffers(productId);
      if(CollectionUtils.isEmpty(offers)){
        return getResponseObjForFailure(NO_OFFERS_FOUND);
      }
      log.info("Successfully fetched offers for productId : {}", productId);
      return getResponseObjForSuccess(offers);
    } catch (Exception ex){
      log.error("Failed to fetch offers for productId : {} and exception: {}", productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj addOffer(AddOfferRequest request) {
    log.info("Received request to add offers for request : {}", request);
    String validationMessage = validateAddOfferRequest(request);
    if(StringUtils.isNotBlank(validationMessage)){
      log.warn("Validation failed request {} to add offers", request);
      return getResponseObjForFailure(validationMessage);
    }
    try{
      OfferDTO offerDTO = getOfferDTOFromRequest(request);
      offerDao.addOffer(offerDTO);
      log.info("Successfully added offers with offerId: {}", offerDTO.getOfferId());
      return getResponseObjForSuccess(offerDTO);
    } catch (Exception ex){
      log.error("Failed to add offer for productId : {} and exception: {}", request.getProductId(), ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj removeOffer(String offerId) {
    log.info("Received request to delete offerId : {}", offerId);
    if(StringUtils.isBlank(offerId)){
      log.info("Validation failed for request to delete offerId : {} with message: {}", offerId, OFFER_ID_BLANK);
      return getResponseObjForFailure(OFFER_ID_BLANK);
    }
    try{
      offerDao.removeOffer(offerId);
      log.info("Successfully deleted offerId : {}", offerId);
      return getResponseObjForSuccess(OFFER_REMOVED);
    } catch (Exception ex){
      log.error("Failed to delete offerId : {} and exception: {}", offerId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  private static OfferDTO getOfferDTOFromRequest(AddOfferRequest request){
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setOfferId(UUID.randomUUID().toString());
    offerDTO.setOfferType(request.getOfferType());
    offerDTO.setMinimumQuantity(request.getMinimumQuantity());
    offerDTO.setDiscount(request.getDiscount());
    offerDTO.setProductId(request.getProductId());
    return offerDTO;
  }

}
