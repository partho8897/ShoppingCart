package com.capitalfloat.assignment.cartitems.service;

import com.capitalfloat.assignment.cartitems.dao.CartItemsDao;
import com.capitalfloat.assignment.cartitems.dto.CartItemDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    if (StringUtils.isNotBlank(validationMsg)) {
      return getResponseObjForFailure(validationMsg);
    }
    ResponseObj productResponse = productService.getProduct(request.getProductId());
    if (StringUtils.isNotBlank(productResponse.getErrorMessage()) || Objects.isNull(productResponse.getData())) {
      log.info("Failed to add to cart: {} because: {}", request, productResponse.getErrorMessage());
      return productResponse;
    }
    ProductDTO productDTO = (ProductDTO) productResponse.getData();
    if (productDTO.getAvailableQuantity() < request.getQuantity()) {
      log.info("Failed to add to cart: {} because: {}", request, REQUESTED_QUANTITY_NOT_AVAILABLE);
      return getResponseObjForFailure(REQUESTED_QUANTITY_NOT_AVAILABLE);
    }
    try {
      CartItemDTO cartItemDTO = getCartItemsDTOFromRequest(request);
      cartItemsDao.addToCart(cartItemDTO);
      log.info("Successfully processed request to add to cart: {}", request);
      return getResponseObjForSuccess(cartItemDTO);
    } catch (Exception ex) {
      log.error("Failed to add to cart: {} because: {}", request, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj removeFromCart(String userId, String productId) {
    log.info("Received request to remove from cart userID: {} productId: {}", userId, productId);
    if (!isUserIdValid(userId)) {
      log.warn("Validation failed to remove from cart userID: {} productId: {} because: {}", userId, productId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    if (!isProductIdValid(productId)) {
      log.warn("Validation failed to remove from cart userID: {} productId: {} because: {}", userId, productId, PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try {
      cartItemsDao.removeFromCart(userId, productId);
      log.info("Successfully removed from cart userID: {} productId: {}", userId, productId);
      return getResponseObjForSuccess(REMOVED_FROM_CART);
    } catch (Exception ex) {
      log.error("Failed to remove from cart userID: {} productId: {} because: {}", userId, productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj clearCart(String userId) {
    log.info("Received request to clear from cart userID: {}", userId);
    if (!isUserIdValid(userId)) {
      log.warn("Validation failed to clear from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    try {
      cartItemsDao.clearCart(userId);
      log.info("Successfully processed request to clear from cart userID: {}", userId);
      return getResponseObjForSuccess(CLEARED_CART);
    } catch (Exception ex) {
      log.error("Failed to process request to clear from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj getCartItems(String userId) {
    log.info("Received request to get items from cart userID: {}", userId);
    if (!isUserIdValid(userId)) {
      log.warn("Validation failed to get items from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    try {
      List<CartItemDTO> cartItems = cartItemsDao.getCartItems(userId);
      log.info("Successfully processed request to get items from cart userID: {}", userId);
      return getResponseObjForSuccess(cartItems);
    } catch (Exception ex) {
      log.error("Failed to process request to get items from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj checkoutCart(String userId) {
    log.info("Received request to checkout from cart userID: {}", userId);
    if (!isUserIdValid(userId)) {
      log.warn("Validation failed to checkout from cart userID: {} because: {}", userId, USER_ID_BLANK);
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    List<String> availableProducts = new ArrayList<>();
    List<String> unavailableProducts = new ArrayList<>();
    try {
      List<CartItemDTO> cartItems = cartItemsDao.getCartItems(userId);
      if (CollectionUtils.isEmpty(cartItems)) {
        log.info("Successfully processed request to checkout from cart userID: {}", userId);
        getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, 0, null));
      }

      //fetch for products to get their price and availability
      List<String> productsInCart = cartItems.stream().map(CartItemDTO::getProductId).collect(Collectors.toList());
      ResponseObj productListResponse = productService.getProductList(productsInCart);
      if (!productListResponse.isResult() || Objects.isNull(productListResponse.getData())) {
        return getResponseObjForFailure(productListResponse.getErrorMessage());
      }

      //segregate available and unavailable products
      Map<String, ProductDTO> prodIdToProdDTOMap = getProdIdToProdDTOMap((List<ProductDTO>) productListResponse.getData());
      cartItems.forEach(cartItemDTO -> {
        if (cartItemDTO.getQuantity() <= prodIdToProdDTOMap.get(cartItemDTO.getProductId()).getAvailableQuantity()) {
          availableProducts.add(cartItemDTO.getProductId());
        } else {
          unavailableProducts.add(cartItemDTO.getProductId());
        }
      });
      if (availableProducts.isEmpty()) {
        log.info("Successfully processed request to checkout from cart userID: {}", userId);
        getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, 0, unavailableProducts));
      }

      Map<String, CartItemDTO> prodIdToCartItemsDTOMap = getProdIdToCartItemsDTOMap(cartItems);
      double totalCost = getTotalCost(availableProducts, prodIdToProdDTOMap, prodIdToCartItemsDTOMap);

      //clear the cart for the user after checking out all products
      cartItemsDao.clearCart(userId);
      log.info("Successfully processed request to checkout from cart userID: {}", userId);
      return getResponseObjForSuccess(getCheckoutCartResponse(availableProducts, userId, totalCost, unavailableProducts));
    } catch (Exception ex) {
      log.error("Failed to process request to checkout from cart userID: {} because: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  /***
   * Find total cost by iterating over the available ProductIds
   * @param availableProducts
   * @param prodIdToProdDTOMap
   * @param prodIdToCartItemsDTOMap
   * @return
   */
  private double getTotalCost(List<String> availableProducts,
                              Map<String, ProductDTO> prodIdToProdDTOMap,
                              Map<String, CartItemDTO> prodIdToCartItemsDTOMap) {
    AtomicReference<Double> totalCost = new AtomicReference<>((double) 0);
    availableProducts.forEach(availableProduct -> {
      int quantityPurchased = prodIdToCartItemsDTOMap.get(availableProduct).getQuantity();
      ResponseObj offersResponse = offerService.getOffers(availableProduct);
      int price = prodIdToProdDTOMap.get(availableProduct).getPrice();
      if (offersResponse.isResult() && Objects.nonNull(offersResponse.getData())) {
        List<OfferDTO> offerDTOList = (List<OfferDTO>) offersResponse.getData();
        double minPrice = calculateMinimumPrice(prodIdToCartItemsDTOMap, offerDTOList, quantityPurchased, price);
        totalCost.updateAndGet(v -> new Double(v + minPrice));
      } else {
        totalCost.updateAndGet(v -> new Double(v + quantityPurchased * price));
      }
      productService.updateQuantity(availableProduct,
          prodIdToProdDTOMap.get(availableProduct).getAvailableQuantity() - quantityPurchased);
    });
    return totalCost.get();
  }

  /**
   * Calculate minimum discounted price
   *
   * @param prodIdToCartItemsDTOMap
   * @param offerDTOList
   * @param quantityPurchased
   * @param price
   * @return
   */
  private double calculateMinimumPrice(Map<String, CartItemDTO> prodIdToCartItemsDTOMap,
                                       List<OfferDTO> offerDTOList,
                                       int quantityPurchased, int price) {
    AtomicReference<Double> minPrice = new AtomicReference<>((double) (quantityPurchased * price));
    offerDTOList.forEach(offerDTO -> {
      double discountedPrice = Double.MAX_VALUE;
      boolean isEligibleForDiscount = offerDTO.getMinimumQuantity() <= prodIdToCartItemsDTOMap.get(offerDTO.getProductId()).getQuantity();

      if (isEligibleForDiscount) {
        if (OfferType.DISCOUNT.equals(offerDTO.getOfferType())) {
          discountedPrice = calculatePriceAfterDiscount(quantityPurchased, offerDTO.getDiscount(), price);
        } else if (OfferType.GIVEAWAY.equals(offerDTO.getOfferType())) {
          discountedPrice = calculatePriceAfterGiveaway(quantityPurchased, offerDTO.getDiscount(), price);
        } else if (OfferType.FLAT_DISCOUNT.equals(offerDTO.getOfferType())) {
          discountedPrice = calculatePriceAfterFlatDiscount(quantityPurchased, offerDTO.getDiscount(), price);
        }
        minPrice.set(Math.min(minPrice.get(), discountedPrice));
      }
    });

    return minPrice.get().equals(Double.MAX_VALUE) ? 0 : minPrice.get();
  }

}
