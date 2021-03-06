package com.capitalfloat.assignment.cartitems.dao;

import com.capitalfloat.assignment.cartitems.dto.CartItemDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.capitalfloat.assignment.utils.SQLStatements.*;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Repository
public class CartItemsDaoImpl implements CartItemsDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS cart_items(" +
      "cartId VARCHAR(50) PRIMARY KEY," +
      "userId VARCHAR(50)," +
      "productId VARCHAR(50)," +
      "quantity INT NOT NULL," +
      "CONSTRAINT fk_userId FOREIGN KEY(userId) REFERENCES user_table(userId) ON DELETE CASCADE," +
      "CONSTRAINT fk_productId FOREIGN KEY(productId) REFERENCES product(productId) ON DELETE CASCADE);";

  @Override
  public void createTable() {
    jdbcTemplate.execute(CREATE_TABLE_QUERY);
  }

  @Override
  public void addToCart(CartItemDTO cartItemDTO) {
    StringBuilder query = new StringBuilder(INSERT_INTO)
        .append(CART_ITEMS_TABLE)
        .append(PARENTHESIS_OPEN)
        .append(CART_ID).append(COMMA).append(USER_ID).append(COMMA).append(PRODUCT_ID).append(COMMA).append(QUANTITY)
        .append(PARENTHESIS_CLOSE)
        .append(VALUES)
        .append(PARENTHESIS_OPEN)
        .append(QUOTES).append(cartItemDTO.getCartId()).append(QUOTES).append(COMMA)
        .append(QUOTES).append(cartItemDTO.getUserId()).append(QUOTES).append(COMMA)
        .append(QUOTES).append(cartItemDTO.getProductId()).append(QUOTES).append(COMMA)
        .append(cartItemDTO.getQuantity())
        .append(PARENTHESIS_CLOSE).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void removeFromCart(String userId, String productId) {
    StringBuilder query = new StringBuilder(DELETE).append(FROM)
        .append(CART_ITEMS_TABLE)
        .append(WHERE).append(USER_ID).append(EQUALS).append(QUOTES).append(userId).append(QUOTES)
        .append(AND).append(PRODUCT_ID).append(EQUALS).append(QUOTES).append(productId).append(QUOTES)
        .append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void clearCart(String userId) {
    StringBuilder query = new StringBuilder(DELETE).append(FROM)
        .append(CART_ITEMS_TABLE)
        .append(WHERE).append(USER_ID).append(EQUALS).append(QUOTES).append(userId).append(QUOTES)
        .append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public List<CartItemDTO> getCartItems(String userId) {
    StringBuilder query = new StringBuilder(SELECT).append(ALL).append(FROM)
        .append(CART_ITEMS_TABLE)
        .append(WHERE).append(USER_ID).append(EQUALS).append(QUOTES).append(userId).append(QUOTES)
        .append(SEMI_COLON);
    List<Map<String, Object>> results = jdbcTemplate.queryForList(query.toString());
    if(CollectionUtils.isEmpty(results) || MapUtils.isEmpty(results.get(0))){
      return new ArrayList<>();
    }
    List<CartItemDTO> cartItemDTOList = new ArrayList<>();
    results.forEach(row -> {
      CartItemDTO cartItemDTO = new CartItemDTO();
      cartItemDTO.setCartId((String) row.get(CART_ID));
      cartItemDTO.setUserId((String) row.get(USER_ID));
      cartItemDTO.setProductId((String) row.get(PRODUCT_ID));
      cartItemDTO.setQuantity((Integer) row.get(QUANTITY));
      cartItemDTOList.add(cartItemDTO);
    });
    return cartItemDTOList;
  }
}
