package com.capitalfloat.assignment.product.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.product.dao.ProductDao;
import com.capitalfloat.assignment.product.dto.ProductDTO;
import com.capitalfloat.assignment.product.request.AddProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.capitalfloat.assignment.product.ProductConstants.*;
import static com.capitalfloat.assignment.product.ProductUtils.isProductQuantityValid;
import static com.capitalfloat.assignment.product.ProductUtils.validateAddProductRequest;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForFailure;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForSuccess;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

  @Autowired
  private ProductDao productDao;

  @Override
  public ResponseObj addProduct(AddProductRequest request) {
    log.info("Received request to add product : {}", request);
    String validationMessage = validateAddProductRequest(request);
    if(StringUtils.isNotBlank(validationMessage)){
      log.warn("Request validation failed for: {} with message: {}", request, validationMessage);
      return getResponseObjForFailure(validationMessage);
    }
    try{
      ProductDTO productDTO = getProductDTOFromRequest(request);
      productDao.addProduct(productDTO);
      log.info("Product added successfully with productId: {}", productDTO.getProductId());
      return getResponseObjForSuccess(productDTO);
    } catch (Exception ex){
      log.error("Failed to add product for request: {} and exception: {}", request, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj getProduct(String productId) {
    log.info("Received request to fetch productId : {}", productId);
    if(StringUtils.isBlank(productId)){
      log.warn("Request validation failed with message: {}", PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try{
      ProductDTO productDTO = productDao.getProduct(productId);
      if(Objects.isNull(productDTO)){
        return getResponseObjForFailure(PRODUCT_NOT_FOUND);
      }
      log.info("Product fetched successfully with productId: {}", productDTO.getProductId());
      return getResponseObjForSuccess(productDTO);
    } catch (Exception ex){
      log.error("Failed to fetch product for productId: {} and exception: {}", productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj updateQuantity(String productId, int availableQuantity) {
    log.info("Received request to update productId : {}", productId);
    if(StringUtils.isBlank(productId)){
      log.warn("Request validation failed with message: {}", PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    if(!isProductQuantityValid(availableQuantity)){
      log.warn("Request validation failed with message: {}", INVALID_QUANTITY);
      return getResponseObjForFailure(INVALID_QUANTITY);
    }
    try{
      productDao.updateQuantity(productId, availableQuantity);
      log.info("Product updated successfully with productId: {}", productId);
      return getResponseObjForSuccess(PRODUCT_QUANTITY_UPDATED);
    } catch (Exception ex){
      log.error("Failed to update product for productId: {} and exception: {}", productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj deleteProduct(String productId) {
    log.info("Received request to delete productId : {}", productId);
    if(StringUtils.isBlank(productId)){
      log.warn("Request validation failed with message: {}", PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try{
      productDao.deleteProduct(productId);
      log.info("Product deleted successfully with productId: {}", productId);
      return getResponseObjForSuccess(PRODUCT_DELETED);
    } catch (Exception ex){
      log.error("Failed to delete product for productId: {} and exception: {}", productId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj getProductList(List<String> productIds) {
    log.info("Received request to fetch productIds : {}", productIds);
    if(CollectionUtils.isEmpty(productIds)){
      log.warn("Request validation failed with message: {}", PRODUCT_ID_BLANK);
      return getResponseObjForFailure(PRODUCT_ID_BLANK);
    }
    try{
      List<ProductDTO> products = productDao.getProducts(productIds);
      log.info("Products fetched successfully with productIds: {}", productIds);
      return getResponseObjForSuccess(products);
    } catch (Exception ex){
      log.error("Failed to fetch product for productIds: {} and exception: {}", productIds, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  private ProductDTO getProductDTOFromRequest(AddProductRequest request){
    ProductDTO productDTO = new ProductDTO();
    productDTO.setProductId(UUID.randomUUID().toString());
    productDTO.setProductName(request.getProductName());
    productDTO.setCategory(request.getCategory());
    productDTO.setAvailableQuantity(request.getAvailableQuantity());
    productDTO.setPrice(request.getPrice());
    return productDTO;
  }

}
