package com.capitalfloat.assignment.product.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.product.request.AddProductRequest;

import java.util.List;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface ProductService {

  /***
   * Add a product along with it's available quantity and price
   * @param request
   * @return
   */
  ResponseObj addProduct(AddProductRequest request);

  /***
   * Get the product details for a given productId
   * @param productId
   * @return
   */
  ResponseObj getProduct(String productId);

  /***
   * Update the quantity of a product
   * @param productId
   * @param availableQuantity
   * @return
   */
  ResponseObj updateQuantity(String productId, int availableQuantity);

  /***
   * Delete the product from the inventory
   * @param productId
   * @return
   */
  ResponseObj deleteProduct(String productId);

  /***
   * Get all the products based on the given list
   * @param productIds
   * @return
   */
  ResponseObj getProductList(List<String> productIds);

}
