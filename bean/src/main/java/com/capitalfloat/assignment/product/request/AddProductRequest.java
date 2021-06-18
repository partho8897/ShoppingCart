package com.capitalfloat.assignment.product.request;

import com.capitalfloat.assignment.product.category.Category;
import lombok.Data;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class AddProductRequest {

  private String productName;
  private Category category;
  private int availableQuantity;
  private int price;

}
