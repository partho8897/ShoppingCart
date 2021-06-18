package com.capitalfloat.assignment.user.dao;

import com.capitalfloat.assignment.user.dto.UserDTO;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.capitalfloat.assignment.utils.Constants.*;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Repository
public class UserDaoImpl implements UserDao{

  private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS user_table(" +
      "userId VARCHAR(50) PRIMARY KEY," +
      "name VARCHAR(50)," +
      "address VARCHAR(100)," +
      "emailId VARCHAR(30) UNIQUE NOT NULL);";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void createTable() {
    jdbcTemplate.execute(CREATE_TABLE_QUERY);
  }

  @Override
  public UserDTO findUserByEmail(String emailId) {
    StringBuilder query = new StringBuilder(SELECT).append(ALL).append(FROM).append(USER_TABLE).append(WHERE).
        append(EMAIL_ID).append(EQUALS).append(QUOTES).append(emailId).append(QUOTES).append(SEMI_COLON);
    List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString());
    if(CollectionUtils.isEmpty(result) || MapUtils.isEmpty(result.get(0))){
      return null;
    }
    Map<String, Object> row = result.get(0);
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId((String) row.get(USER_ID));
    userDTO.setEmailId((String) row.get(EMAIL_ID));
    userDTO.setName((String) row.get(NAME));
    userDTO.setAddress((String) row.get(ADDRESS));
    return userDTO;
  }

  @Override
  public void addUser(UserDTO userDTO) {
    StringBuilder query = new StringBuilder(INSERT_INTO).append(USER_TABLE).append(PARENTHESIS_OPEN).append(USER_ID)
        .append(COMMA).append(EMAIL_ID).append(COMMA).append(NAME).append(COMMA).append(ADDRESS).append(PARENTHESIS_CLOSE)
        .append(VALUES).append(PARENTHESIS_OPEN).append(QUOTES).append(userDTO.getUserId()).append(QUOTES).append(COMMA)
        .append(QUOTES).append(userDTO.getEmailId()).append(QUOTES).append(COMMA).append(QUOTES).append(userDTO.getName())
        .append(QUOTES).append(COMMA).append(QUOTES).append(userDTO.getAddress()).append(QUOTES).append(PARENTHESIS_CLOSE)
        .append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }

  @Override
  public void deleteUser(String userId) {
    StringBuilder query = new StringBuilder(DELETE).append(FROM).append(USER_TABLE).append(WHERE).append(USER_ID)
        .append(EQUALS).append(QUOTES).append(userId).append(QUOTES).append(SEMI_COLON);
    jdbcTemplate.execute(query.toString());
  }
}
