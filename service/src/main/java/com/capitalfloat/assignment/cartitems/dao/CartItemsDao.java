package com.capitalfloat.assignment.cartitems.dao;

import com.capitalfloat.assignment.cartitems.dto.CartItemsDTO;

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
   * @param cartItemsDTO
   * @return
   */
  void addToCart(CartItemsDTO cartItemsDTO);

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
  List<CartItemsDTO> getCartItems(String userId);

}
