package com.capitalfloat.assignment.api.product;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.product.request.AddProductRequest;
import com.capitalfloat.assignment.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@RestController
@RequestMapping(value = "/product")
public class ProductRestAPI {

  @Autowired
  private ProductService productService;

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj getProduct(@RequestParam(value = "productId") String productId){
    return productService.getProduct(productId);
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj addProduct(@RequestBody AddProductRequest addProductRequest){
    return productService.addProduct(addProductRequest);
  }


  @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj deleteProduct(@RequestParam(value = "productId") String productId){
    return productService.deleteProduct(productId);
  }

  @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj updateQuantity(@RequestParam(value = "productId") String productId,
                                    @RequestParam(value = "quantity") int quantity){
    return productService.updateQuantity(productId, quantity);
  }

}
