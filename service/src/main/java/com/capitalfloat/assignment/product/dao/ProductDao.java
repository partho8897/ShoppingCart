package com.capitalfloat.assignment.product.dao;

import com.capitalfloat.assignment.product.dto.ProductDTO;

import java.util.List;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface ProductDao {

  /***
   * Initialise the required table
   */
  void createTable();

  /***
   * Get the product details for a given productId
   * @param productId
   * @return
   */
  ProductDTO getProduct(String productId);

  /***
   * Add a product along with it's available quantity and price
   * @param productDTO
   * @return
   */
  void addProduct(ProductDTO productDTO);

  /***
   * Update the quantity of a product
   * @param productId
   * @param availableQuantity
   * @return
   */
  void updateQuantity(String productId, int availableQuantity);

  /***
   * Delete the product from the inventory
   * @param productId
   * @return
   */
  void deleteProduct(String productId);

  /***
   * Get all the products based on the given list
   * @param productIds
   * @return
   */
  List<ProductDTO> getProducts(List<String> productIds);

}
