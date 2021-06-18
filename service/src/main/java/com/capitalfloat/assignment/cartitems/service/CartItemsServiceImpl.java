package com.capitalfloat.assignment.cartitems.service;

import com.capitalfloat.assignment.cartitems.dao.CartItemsDao;
import com.capitalfloat.assignment.cartitems.dto.CartItemsDTO;
import com.capitalfloat.assignment.cartitems.request.AddCartItemsRequest;
import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.offer.dto.OfferDTO;
import com.capitalfloat.assignment.offer.service.OfferService;
import com.capitalfloat.assignment.offer.type.OfferType;
import com.capitalfloat.assignment.product.dto.ProductDTO;
import com.capitalfloat.assignment.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.capitalfloat.assignment.cartitems.CartItemsConstants.*;
import static com.capitalfloat.assignment.cartitems.CartItemsUtils.*;
import static com.capitalfloat.assignment.offer.OfferUtils.isProductIdValid;
import static com.capitalfloat.assignment.product.ProductConstants.PRODUCT_ID_BLANK;
import static com.capitalfloat.assignment.user.UserConstants.USER_ID_BLANK;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForFailure;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForSuccess;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Service
@Slf4j
public class CartItemsServiceImpl implements CartItemsService {

  @Autowired
  private CartItemsDao cartItemsDao;

  @Autowired
  private ProductService productService;

  @Autowired
  private OfferService offerService;

  @Override
  public ResponseObj addToCart(AddCartItemsRequest request) {
    log.info("Received request to add to cart: {}", request);
    String validationMsg = validateAddCartItemsRequest(request);
    if(StringUtils.isNotBlank(validationMsg)){
      return getResponseObjForFailure(validationMsg);
    }
    ResponseObj productResponse = productService.getProduct(request.getProductId());
    if(StringUtils.isNotBlank(productResponse.getErrorMessage()) || Objects.isNull(productResponse.getData())){
      log.info("Failed to add to cart: {} because: {}", request, productResponse.getErrorMessage());
      return productResponse;
    }
    ProductDTO productDTO = (ProductDTO) productResponse.getData();
    if(productDTO.getAvailableQuantity() < request.getQuantity()){
      log.info("Failed to add to cart: {} because: {}", request, REQUESTED_QUANTITY_NOT_AVAILABLE);
      return getResponseObjForFailure(REQUESTED_QUANTITY_NOT_AVAILABLE);
    }
    try{
      CartItemsDTO cartItemsDTO = getCartItemsDTOFromRequest(request);
      cartItemsDao.addToCart(cartItemsDTO);
      log.info("Successfully processed request to add to cart: {}", request);
      return getResponseObjForSuccess(cartItemsDTO);
    } catch (Exception ex){
      log.error("Failed to add to cart: {} because: {}", request, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj removeFromCart(String userId, String productId) {
    log.info("Received request to remove from cart userID: {} productId: {}", userId, productId);
    if(!isUserIdValid(userId)){
      log.warn("Validation failed to remove from cart userID: {} productId: {} because: {}", userId, productId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    if(!isProductIdValid(productId)){
      log.warn("Validation failed to remove from cart userID: {} productId: {} because: {}", userId, productId, PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try{
      cartItemsDao.removeFromCart(userId, productId);
      log.info("Successfully removed from cart userID: {} productId: {}", userId, productId);
      return getResponseObjForSuccess(REMOVED_FROM_CART);
    } catch (Exception ex){
      log.error("Failed to remove from cart userID: {} productId: {} because: {}", userId, productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj clearCart(String userId) {
    log.info("Received request to clear from cart userID: {}", userId);
    if(!isUserIdValid(userId)){
      log.warn("Validation failed to clear from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    try{
      cartItemsDao.clearCart(userId);
      log.info("Successfully processed request to clear from cart userID: {}", userId);
      return getResponseObjForSuccess(CLEARED_CART);
    } catch (Exception ex){
      log.error("Failed to process request to clear from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj getCartItems(String userId) {
    log.info("Received request to get items from cart userID: {}", userId);
    if(!isUserIdValid(userId)){
      log.warn("Validation failed to get items from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    try{
      List<CartItemsDTO> cartItems = cartItemsDao.getCartItems(userId);
      log.info("Successfully processed request to get items from cart userID: {}", userId);
      return getResponseObjForSuccess(cartItems);
    } catch (Exception ex){
      log.error("Failed to process request to get items from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj checkoutCart(String userId) {
    log.info("Received request to checkout from cart userID: {}", userId);
    if(!isUserIdValid(userId)){
      log.warn("Validation failed to checkout from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    List<String> availableProducts = new ArrayList<>();
    List<String> unavailableProducts = new ArrayList<>();
    try {
      List<CartItemsDTO> cartItems = cartItemsDao.getCartItems(userId);
      if(CollectionUtils.isEmpty(cartItems)){
        log.info("Successfully processed request to checkout from cart userID: {}", userId);
        getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, 0, null));
      }

      //fetch for products to get their price and availability
      List<String> productsInCart = cartItems.stream().map(CartItemsDTO::getProductId).collect(Collectors.toList());
      ResponseObj productListResponse = productService.getProductList(productsInCart);
      if(!productListResponse.isResult() || Objects.isNull(productListResponse.getData())){
        return getResponseObjForFailure(productListResponse.getErrorMessage());
      }

      //segregate available and unavailable products
      Map<String, ProductDTO> prodIdToProdDTOMap = getProdIdToProdDTOMap((List<ProductDTO>) productListResponse.getData());
      cartItems.forEach(cartItemsDTO -> {
        if(cartItemsDTO.getQuantity() <= prodIdToProdDTOMap.get(cartItemsDTO.getProductId()).getAvailableQuantity()){
          availableProducts.add(cartItemsDTO.getProductId());
        } else{
          unavailableProducts.add(cartItemsDTO.getProductId());
        }
      });
      if(availableProducts.isEmpty()){
        log.info("Successfully processed request to checkout from cart userID: {}", userId);
        getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, 0, unavailableProducts));
      }

      //find total cost by fetching offers for each product and applying required discount, if applicable
      AtomicReference<Double> totalCost = new AtomicReference<>((double) 0);
      Map<String, CartItemsDTO> prodIdToCartItemsDTOMap = getProdIdToCartItemsDTOMap(cartItems);
      availableProducts.forEach(availableProduct -> {
        int quantityPurchased = prodIdToCartItemsDTOMap.get(availableProduct).getQuantity();
        ResponseObj offersResponse = offerService.getOffers(availableProduct);
        int price = prodIdToProdDTOMap.get(availableProduct).getPrice();
        if(offersResponse.isResult() && Objects.nonNull(offersResponse.getData())){
          List<OfferDTO> offerDTOList = (List<OfferDTO>) offersResponse.getData();
          double minPrice = calculateMinimumPrice(prodIdToCartItemsDTOMap, offerDTOList, quantityPurchased, price);
          totalCost.updateAndGet(v -> new Double((double) (v + minPrice)));
        } else{
          totalCost.updateAndGet(v -> new Double((double) (v + quantityPurchased*price)));
        }
        productService.updateQuantity(availableProduct,
            prodIdToProdDTOMap.get(availableProduct).getAvailableQuantity() - quantityPurchased);
      });
      //clear the cart for the user after checking out all products
      cartItemsDao.clearCart(userId);
      log.info("Successfully processed request to checkout from cart userID: {}", userId);
      return getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, totalCost.get(), unavailableProducts));
    } catch (Exception ex){
      log.error("Failed to process request to checkout from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  /**
   * Calculate minimum discounted price
   * @param prodIdToCartItemsDTOMap
   * @param offerDTOList
   * @param quantityPurchased
   * @param price
   * @return
   */
  private double calculateMinimumPrice(Map<String, CartItemsDTO> prodIdToCartItemsDTOMap, List<OfferDTO> offerDTOList, int quantityPurchased, int price) {
    AtomicReference<Double> minPrice = new AtomicReference<>(Double.MAX_VALUE);
    offerDTOList.forEach(offerDTO -> {
      if(OfferType.DISCOUNT.equals(offerDTO.getOfferType()) &&
          offerDTO.getMinimumQuantity() >= prodIdToCartItemsDTOMap.get(offerDTO.getProductId()).getQuantity()){
        minPrice.set(Math.min(minPrice.get(), calculatePriceAfterDiscount(quantityPurchased, offerDTO.getDiscount(), price)));
      } else if(OfferType.GIVEAWAY.equals(offerDTO.getOfferType()) &&
          offerDTO.getMinimumQuantity() >= prodIdToCartItemsDTOMap.get(offerDTO.getProductId()).getQuantity()){
        minPrice.set(Math.min(minPrice.get(), calculatePriceAfterGiveaway(quantityPurchased, offerDTO.getDiscount(), price)));
      } else if(OfferType.FLAT_DISCOUNT.equals(offerDTO.getOfferType()) &&
          offerDTO.getMinimumQuantity() >= prodIdToCartItemsDTOMap.get(offerDTO.getProductId()).getQuantity()){
        minPrice.set(Math.min(minPrice.get(), calculatePriceAfterFlatDiscount(quantityPurchased, offerDTO.getDiscount(), price)));
      }
    });
    return minPrice.get();
  }

}
