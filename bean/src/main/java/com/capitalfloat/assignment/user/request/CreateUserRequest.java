package com.capitalfloat.assignment.user.request;

import lombok.Data;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class CreateUserRequest {

  private String emailId;
  private String name;
  private String address;

}
