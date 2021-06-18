package com.capitalfloat.assignment.config;

import com.capitalfloat.assignment.cartitems.dao.CartItemsDao;
import com.capitalfloat.assignment.offer.dao.OfferDao;
import com.capitalfloat.assignment.product.dao.ProductDao;
import com.capitalfloat.assignment.user.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService{

  @Autowired
  private UserDao userDao;

  @Autowired
  private ProductDao productDao;

  @Autowired
  private CartItemsDao cartItemsDao;

  @Autowired
  private OfferDao offerDao;

  @Override
  public void createTables() {
    try{
      userDao.createTable();
      productDao.createTable();
      offerDao.createTable();
      cartItemsDao.createTable();
    } catch (Exception ex){
      log.error("Failed to create tables: {}", ex.getMessage());
    }
  }
}
