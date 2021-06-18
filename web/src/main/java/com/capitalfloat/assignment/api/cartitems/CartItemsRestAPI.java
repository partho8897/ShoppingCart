package com.capitalfloat.assignment.api.cartitems;

import com.capitalfloat.assignment.cartitems.request.AddCartItemsRequest;
import com.capitalfloat.assignment.cartitems.service.CartItemsService;
import com.capitalfloat.assignment.common.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@RestController
@RequestMapping(value = "/cart")
public class CartItemsRestAPI {

  @Autowired
  private CartItemsService cartItemsService;

  @RequestMapping(value = "/items",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj getCartItems(@RequestParam(value = "userId") String userId){
    return cartItemsService.getCartItems(userId);
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj addToCart(@RequestBody AddCartItemsRequest addCartItemsRequest){
    return cartItemsService.addToCart(addCartItemsRequest);
  }


  @RequestMapping(value = "/clear",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj clearCart(@RequestParam(value = "userId") String userId){
    return cartItemsService.clearCart(userId);
  }

  @RequestMapping(value = "/remove",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj removeFromCart(@RequestParam(value = "userId") String userId,
                                    @RequestParam(value = "productId") String productId){
    return cartItemsService.removeFromCart(userId, productId);
  }

  @RequestMapping(value = "/checkout", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj checkoutCart(@RequestParam(value = "userId") String userId){
    return cartItemsService.checkoutCart(userId);
  }

}
