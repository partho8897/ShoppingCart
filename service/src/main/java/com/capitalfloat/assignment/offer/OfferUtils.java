package com.capitalfloat.assignment.offer;

import com.capitalfloat.assignment.offer.request.AddOfferRequest;
import com.capitalfloat.assignment.offer.type.OfferType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.capitalfloat.assignment.offer.OfferConstants.*;
import static com.capitalfloat.assignment.product.ProductConstants.PRODUCT_ID_BLANK;

/**
 * Created by @author Partho Paul on 18/06/21
 */
public interface OfferUtils {

  static String validateAddOfferRequest(AddOfferRequest request){
    if(!isProductIdValid(request.getProductId())){
      return PRODUCT_ID_BLANK;
    }
    if(!isOfferTypeValid(request.getOfferType())){
      return INVALID_OFFER_TYPE;
    }
    if(!isDiscountValid(request.getDiscount())){
      return INVALID_DISCOUNT;
    }
    if(!isQuantityValid(request.getMinimumQuantity())){
      return INVALID_MINIMUM_QUANTITY;
    }
    return StringUtils.EMPTY;
  }

  static boolean isProductIdValid(String productId){
    return StringUtils.isNotBlank(productId);
  }

  static boolean isOfferTypeValid(OfferType offerType){
    return Objects.nonNull(offerType);
  }

  static boolean isQuantityValid(int quantity){
    return quantity >= 0;
  }

  static boolean isDiscountValid(int discount){
    return discount > 0;
  }

}
