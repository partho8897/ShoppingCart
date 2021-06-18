package com.capitalfloat.assignment.offer.dao;

import com.capitalfloat.assignment.offer.dto.OfferDTO;
import com.capitalfloat.assignment.offer.type.OfferType;
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
public class OfferDaoImpl implements OfferDao{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS offer(" +
      "offerId VARCHAR(50) PRIMARY KEY," +
      "productId VARCHAR(50)," +
      "minimumQuantity INT NOT NULL," +
      "discount INT NOT NULL," +
      "offerType VARCHAR(50) NOT NULL," +
      "CONSTRAINT fk_productId FOREIGN KEY(productId) REFERENCES product(productId) ON DELETE CASCADE);";

  @Override
  public void createTable() {
    jdbcTemplate.execute(CREATE_TABLE_QUERY);
  }

  @Override
  public List<OfferDTO> getOffers(String productId) {
    StringBuilder query = new StringBuilder(SELECT).append(ALL).append(FROM).append(OFFER_TABLE).append(WHERE).append(PRODUCT_ID)
        .append(EQUALS).append(QUOTES).append(productId).append(QUOTES).append(SEMI_COLON);
    List<Map<String, Object>> results = jdbcTemplate.queryForList(query.toString());
    if(CollectionUtils.isEmpty(results) || MapUtils.isEmpty(results.get(0))){
      return new ArrayList<>();
    }
    List<OfferDTO> offerDTOList = new ArrayList<>();
    results.forEach(row -> {
      OfferDTO offerDTO = new OfferDTO();
      offerDTO.setOfferId((String) row.get(OFFER_ID));
      offerDTO.setProductId((String) row.get(PRODUCT_ID));
      offerDTO.setOfferType(OfferType.valueOf((String) row.get(OFFER_TYPE)));
      offerDTO.setDiscount((Integer) row.get(DISCOUNT));
      offerDTO.setMinimumQuantity((Integer) row.get(MINIMUM_QUANTITY));
      offerDTOList.add(offerDTO);
    });
    return offerDTOList;
  }

  @Override
  public void addOffer(OfferDTO offerDTO) {
    StringBuilder query = new StringBuilder(INSERT_INTO).append(OFFER_TABLE).append(PARENTHESIS_OPEN).append(OFFER_ID)
        .append(COMMA).append(PRODUCT_ID).append(COMMA).append(MINIMUM_QUANTITY).append(COMMA).append(DISCOUNT).append(COMMA)
        .append(OFFER_TYPE).append(PARENTHESIS_CLOSE).append(VALUES).append(PARENTHESIS_OPEN).append(QUOTES)
        .append(offerDTO.getOfferId()).append(QUOTES).append(COMMA).append(QUOTES).append(offerDTO.getProductId())
        .append(QUOTES).append(COMMA).append(offerDTO.getMinimumQuantity()).append(COMMA).append(offerDTO.getDiscount())
        .append(COMMA).append(QUOTES).append(offerDTO.getOfferType()).append(QUOTES).append(PARENTHESIS_CLOSE)
        .append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void removeOffer(String offerId) {
    StringBuilder query = new StringBuilder(DELETE).append(FROM).append(OFFER_TABLE).append(WHERE).append(OFFER_ID)
        .append(EQUALS).append(QUOTES).append(offerId).append(QUOTES).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }
}
