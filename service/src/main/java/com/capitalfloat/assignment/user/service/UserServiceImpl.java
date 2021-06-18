package com.capitalfloat.assignment.user.service;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.user.dao.UserDao;
import com.capitalfloat.assignment.user.dto.UserDTO;
import com.capitalfloat.assignment.user.request.AddUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.capitalfloat.assignment.user.UserConstants.*;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForFailure;
import static com.capitalfloat.assignment.utils.CommonUtils.getResponseObjForSuccess;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{

  @Autowired
  private UserDao userDao;

  @Override
  public ResponseObj findByEmail(String emailId) {
    log.info("Received request to search user by emailId: {}", emailId);
    if(StringUtils.isBlank(emailId)){
      log.warn("Validation failed request to search user by emailId: {} with message: {}", emailId, EMAIL_ID_BLANK);
      return getResponseObjForFailure(EMAIL_ID_BLANK);
    }
    try {
      UserDTO user = userDao.findUserByEmail(emailId);
      if(Objects.isNull(user)){
        return getResponseObjForFailure(USER_NOT_FOUND);
      }
      log.info("Successfully fetched user by emailId: {}", emailId);
      return getResponseObjForSuccess(user);
    } catch (Exception ex){
      log.error("Search for email: {} failed with exception: {}", emailId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj addUser(AddUserRequest request) {
    log.info("Received request to add user : {}", request);
    if(StringUtils.isBlank(request.getEmaiId())){
      log.warn("Validation failed request to add user {} with message: {}", request, EMAIL_ID_BLANK);
      return getResponseObjForFailure(EMAIL_ID_BLANK);
    }
    UserDTO userDTO = getUserDTOFromRequest(request);
    try{
      userDao.addUser(userDTO);
      log.info("Successfully added user with userID : {}", userDTO.getUserId());
      return getResponseObjForSuccess(userDTO);
    } catch (Exception ex){
      log.error("Failed to add user : {} with exception: {}", request, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  @Override
  public ResponseObj deleteUser(String userId) {
    log.info("Received request to delete user with userId: {}", userId);
    if(StringUtils.isBlank(userId)){
      return getResponseObjForFailure(USER_ID_BLANK);
    }
    try{
      userDao.deleteUser(userId);
      log.info("Successfully deleted user with userId: {}", userId);
      return getResponseObjForSuccess(USER_DELETED_SUCCESSFULLY);
    } catch (Exception ex){
      log.error("Failed to delete user with userId: {} with exception: {}", userId, ex.getMessage());
      return getResponseObjForFailure(ex.getMessage());
    }
  }

  private static UserDTO getUserDTOFromRequest(AddUserRequest request){
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(UUID.randomUUID().toString());
    userDTO.setAddress(request.getAddress());
    userDTO.setName(request.getName());
    userDTO.setEmailId(request.getEmaiId());
    return userDTO;
  }
}
