package com.capitalfloat.assignment.user.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.user.request.AddUserRequest;

/**
 * Created by @author Partho Paul on 17/06/21
 */
public interface UserService {

  /***
   * Fetch a user by it's emailId
   * @param emailId
   * @return
   */
  ResponseObj findByEmail(String emailId);

  /***
   * Add a user to the database
   * @param request
   * @return
   */
  ResponseObj addUser(AddUserRequest request);

  /***
   * Delete a user from the database based on it's userId
   * @param userId
   * @return
   */
  ResponseObj deleteUser(String userId);

}
