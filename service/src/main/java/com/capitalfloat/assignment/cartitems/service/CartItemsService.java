package com.capitalfloat.assignment.cartitems.service;

import com.capitalfloat.assignment.cartitems.request.AddCartItemsRequest;
import com.capitalfloat.assignment.common.ResponseObj;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface CartItemsService {

  /***
   * Add required items to cart
   * @param request
   * @return
   */
  ResponseObj addToCart(AddCartItemsRequest request);

  /***
   * Remove items from cart based on given params
   * @param userId
   * @param productId
   * @return
   */
  ResponseObj removeFromCart(String userId, String productId);

  /***
   * Clear the cart for a user
   * @param userId
   * @return
   */
  ResponseObj clearCart(String userId);

  /***
   * Fetch the cart items for a given user
   * @param userId
   * @return
   */
  ResponseObj getCartItems(String userId);

  /***
   * Checkout the items present in the cart for a user, calculate total cost based on offers and clear the cart
   * @param userId
   * @return
   */
  ResponseObj checkoutCart(String userId);

}
