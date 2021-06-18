package com.capitalfloat.assignment.cartitems.dao;

import com.capitalfloat.assignment.cartitems.dto.CartItemDTO;

import java.util.List;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface CartItemsDao {

  /***
   * Initialise the required table
   */
  void createTable();

  /***
   * Add required items to cart
   * @param cartItemDTO
   * @return
   */
  void addToCart(CartItemDTO cartItemDTO);

  /***
   * Remove items from cart based on given params
   * @param userId
   * @param productId
   * @return
   */
  void removeFromCart(String userId, String productId);

  /***
   * Clear the cart for a user
   * @param userId
   * @return
   */
  void clearCart(String userId);

  /***
   * Fetch the cart items for a given user
   * @param userId
   * @return
   */
  List<CartItemDTO> getCartItems(String userId);

}
