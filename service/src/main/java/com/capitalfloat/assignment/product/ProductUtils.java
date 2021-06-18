package com.capitalfloat.assignment.product;

import com.capitalfloat.assignment.product.category.Category;
import com.capitalfloat.assignment.product.request.AddProductRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.capitalfloat.assignment.product.ProductConstants.*;

/**
 * Created by @author Partho Paul on 18/06/21
 */
public interface ProductUtils {

  static String validateAddProductRequest(AddProductRequest request){
    if(!isProductNameValid(request.getProductName())){
      return PRODUCT_NAME_BLANK;
    }
    if(!isProductCategoryValid(request.getCategory())){
      return INVALID_CATEGORY;
    }
    if(!isProductPriceValid(request.getPrice())){
      return INVALID_PRICE;
    }
    if(!isProductQuantityValid(request.getAvailableQuantity())){
      return INVALID_QUANTITY;
    }
    return StringUtils.EMPTY;
  }

  static boolean isProductNameValid(String productName){
    return StringUtils.isNotBlank(productName);
  }

  static boolean isProductCategoryValid(Category category){
    return Objects.nonNull(category);
  }

  static boolean isProductQuantityValid(int quantity){
    return quantity >= 0;
  }

  static boolean isProductPriceValid(int price){
    return price > 0;
  }
}
