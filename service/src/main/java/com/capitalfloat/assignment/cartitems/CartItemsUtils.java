package com.capitalfloat.assignment.cartitems;

import com.capitalfloat.assignment.cartitems.dto.CartItemsDTO;
import com.capitalfloat.assignment.cartitems.request.AddCartItemsRequest;
import com.capitalfloat.assignment.cartitems.response.CheckoutCartResponse;
import com.capitalfloat.assignment.product.dto.ProductDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.capitalfloat.assignment.offer.OfferUtils.isProductIdValid;
import static com.capitalfloat.assignment.offer.OfferUtils.isQuantityValid;
import static com.capitalfloat.assignment.product.ProductConstants.INVALID_QUANTITY;
import static com.capitalfloat.assignment.product.ProductConstants.PRODUCT_ID_BLANK;
import static com.capitalfloat.assignment.user.UserConstants.USER_ID_BLANK;

/**
 * Created by @author Partho Paul on 18/06/21
 */
public interface CartItemsUtils {

  static String validateAddCartItemsRequest(AddCartItemsRequest request){
    if(!isProductIdValid(request.getProductId())){
      return PRODUCT_ID_BLANK;
    }
    if(!isUserIdValid(request.getUserId())){
      return USER_ID_BLANK;

    }
    if(!isQuantityValid(request.getQuantity())){
      return INVALID_QUANTITY;
    }
    return StringUtils.EMPTY;
  }

  static boolean isUserIdValid(String userId){
    return StringUtils.isNotBlank(userId);
  }

  static CartItemsDTO getCartItemsDTOFromRequest(AddCartItemsRequest request){
    CartItemsDTO cartItemsDTO = new CartItemsDTO();
    cartItemsDTO.setCartId(UUID.randomUUID().toString());
    cartItemsDTO.setQuantity(request.getQuantity());
    cartItemsDTO.setUserId(request.getUserId());
    cartItemsDTO.setProductId(request.getProductId());
    return cartItemsDTO;
  }

  static CheckoutCartResponse getCheckoutCartResponse(List<String> productsCheckedOut, String userId,
                                                      double totalCost, List<String> productsUnavailable){
    CheckoutCartResponse checkoutCartResponse = new CheckoutCartResponse();
    checkoutCartResponse.setProductsCheckedOut(productsCheckedOut);
    checkoutCartResponse.setUserId(userId);
    checkoutCartResponse.setTotalCost(totalCost);
    checkoutCartResponse.setProductsUnavailable(productsUnavailable);
    return checkoutCartResponse;
  }

  static Map<String, ProductDTO> getProdIdToProdDTOMap(List<ProductDTO> productDTOList){
    Map<String, ProductDTO> prodIdToProdDTOMap = new HashMap<>();
    productDTOList.forEach(productDTO -> {
      prodIdToProdDTOMap.put(productDTO.getProductId(), productDTO);
    });
    return prodIdToProdDTOMap;
  }

  static Map<String, CartItemsDTO> getProdIdToCartItemsDTOMap(List<CartItemsDTO> cartItemsDTOList){
    Map<String, CartItemsDTO> prodIdToCartItemsDTOMap = new HashMap<>();
    cartItemsDTOList.forEach(itemsDTO -> {
      prodIdToCartItemsDTOMap.put(itemsDTO.getProductId(), itemsDTO);
    });
    return prodIdToCartItemsDTOMap;
  }

  static double calculatePriceAfterDiscount(int quantity, int discount, int price){
    return quantity*price*(1 - 0.01*discount);
  }

  static double calculatePriceAfterGiveaway(int quantity, int discount, int price){
    return (quantity-discount)*price;
  }

  static double calculatePriceAfterFlatDiscount(int quantity, int discount, int price){
    return quantity*price-discount;
  }

}
