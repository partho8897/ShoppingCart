package com.capitalfloat.assignment.product.dao;

import com.capitalfloat.assignment.product.category.Category;
import com.capitalfloat.assignment.product.dto.ProductDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.capitalfloat.assignment.utils.Constants.*;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Repository
public class ProductDaoImpl implements ProductDao{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS product(" +
      "productId VARCHAR(50) PRIMARY KEY," +
      "productName VARCHAR(30) NOT NULL," +
      "category VARCHAR(20) NOT NULL," +
      "availableQuantity INT NOT NULL," +
      "price INT NOT NULL);";

  @Override
  public void createTable() {
    jdbcTemplate.execute(CREATE_TABLE_QUERY);
  }

  @Override
  public ProductDTO getProduct(String productId) {
    StringBuilder query = new StringBuilder(SELECT).append(ALL).append(FROM).append(PRODUCT_TABLE).append(WHERE).append(PRODUCT_ID)
        .append(EQUALS).append(QUOTES).append(productId).append(QUOTES).append(SEMI_COLON);
    List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString());
    if(CollectionUtils.isEmpty(result) || MapUtils.isEmpty(result.get(0))){
      return null;
    }
    ProductDTO productDTO = new ProductDTO();
    Map<String, Object> row = result.get(0);
    productDTO.setProductId((String) row.get(PRODUCT_ID));
    productDTO.setProductName((String) row.get(PRODUCT_NAME));
    productDTO.setAvailableQuantity((Integer) row.get(AVAILABLE_QUANTITY));
    productDTO.setPrice((Integer) row.get(PRICE));
    productDTO.setCategory(Category.valueOf((String) row.get(CATEGORY)));
    return productDTO;
  }

  @Override
  public void addProduct(ProductDTO productDTO) {
    StringBuilder query = new StringBuilder(INSERT_INTO).append(PRODUCT_TABLE).append(PARENTHESIS_OPEN).append(PRODUCT_ID)
        .append(COMMA).append(PRODUCT_NAME).append(COMMA).append(AVAILABLE_QUANTITY).append(COMMA).append(PRICE)
        .append(COMMA).append(CATEGORY).append(PARENTHESIS_CLOSE)
        .append(VALUES).append(PARENTHESIS_OPEN).append(QUOTES).append(productDTO.getProductId()).append(QUOTES).append(COMMA)
        .append(QUOTES).append(productDTO.getProductName()).append(QUOTES).append(COMMA).append(productDTO.getAvailableQuantity())
        .append(COMMA).append(productDTO.getPrice()).append(COMMA).append(QUOTES).append(productDTO.getCategory()).append(QUOTES)
        .append(PARENTHESIS_CLOSE).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void updateQuantity(String productId, int availableQuantity) {
    StringBuilder query = new StringBuilder(UPDATE).append(PRODUCT_TABLE).append(SET).append(AVAILABLE_QUANTITY).append(EQUALS)
        .append(availableQuantity).append(WHERE).append(PRODUCT_ID).append(EQUALS).append(QUOTES).append(productId)
        .append(QUOTES).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void deleteProduct(String productId) {
    StringBuilder query = new StringBuilder(DELETE).append(FROM).append(PRODUCT_TABLE).append(WHERE).append(PRODUCT_ID)
        .append(EQUALS).append(QUOTES).append(productId).append(QUOTES).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public List<ProductDTO> getProducts(List<String> productIds) {
    StringBuilder query = new StringBuilder(SELECT).append(ALL).append(FROM).append(PRODUCT_TABLE).append(WHERE).append(PRODUCT_ID)
        .append(IN).append(PARENTHESIS_OPEN);
    for (int i = 0; i < productIds.size(); i++) {
      query.append(QUOTES).append(productIds.get(i)).append(QUOTES);
      if(productIds.size() -1 != i){
        query.append(COMMA);
      }
    }
    query.append(PARENTHESIS_CLOSE).append(SEMI_COLON);
    List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString());
    if(CollectionUtils.isEmpty(result) || MapUtils.isEmpty(result.get(0))){
      return new ArrayList<>();
    }
    List<ProductDTO> productDTOList = new ArrayList<>();
    result.forEach(row -> {
      ProductDTO productDTO = new ProductDTO();
      productDTO.setProductId((String) row.get(PRODUCT_ID));
      productDTO.setProductName((String) row.get(PRODUCT_NAME));
      productDTO.setAvailableQuantity((Integer) row.get(AVAILABLE_QUANTITY));
      productDTO.setPrice((Integer) row.get(PRICE));
      productDTO.setCategory(Category.valueOf((String) row.get(CATEGORY)));
      productDTOList.add(productDTO);
    });
    return productDTOList;
  }
}
