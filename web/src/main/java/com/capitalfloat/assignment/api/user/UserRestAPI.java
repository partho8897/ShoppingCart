package com.capitalfloat.assignment.api.user;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.user.request.CreateUserRequest;
import com.capitalfloat.assignment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@RestController
@RequestMapping(value = "/user")
public class UserRestAPI {

  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj findUserByEmail(@RequestParam(value = "emailId") String emailId){
    return userService.findByEmail(emailId);
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj createUser(@RequestBody CreateUserRequest createUserRequest){
    return userService.createUser(createUserRequest);
  }


  @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj deleteUser(@RequestParam(value = "userId") String userId){
    return userService.deleteUser(userId);
  }

}
