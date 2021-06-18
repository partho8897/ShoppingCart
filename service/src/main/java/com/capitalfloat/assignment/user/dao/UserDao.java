package com.capitalfloat.assignment.user.dao;

import com.capitalfloat.assignment.user.dto.UserDTO;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface UserDao {

  /***
   * Initialise the required table
   */
  void createTable();

  /***
   * Fetch a user by it's emailId
   * @param emailId
   * @return
   */
  UserDTO findUserByEmail(String emailId);

  /***
   * Add a user to the database
   * @param userDTO
   * @return
   */
  void addUser(UserDTO userDTO);

  /***
   * Delete a user from the database based on it's userId
   * @param userId
   * @return
   */
  void deleteUser(String userId);

}
