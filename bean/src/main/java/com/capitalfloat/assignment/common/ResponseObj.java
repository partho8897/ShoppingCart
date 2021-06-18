package com.capitalfloat.assignment.common;

import lombok.Data;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@Data
public class ResponseObj {

  boolean result;
  Object data;
  String errorMessage;

}
