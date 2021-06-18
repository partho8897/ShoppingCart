package com.capitalfloat.assignment.product.dto;

import com.capitalfloat.assignment.product.category.Category;
import lombok.Data;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Data
public class ProductDTO {

  private String productId;
  private String productName;
  private Category category;
  private int availableQuantity;
  private int price;

}
