package com.capitalfloat.assignment.utils;

import com.capitalfloat.assignment.common.ResponseObj;

/**
 * Created by @author Partho Paul on 18/06/21
 */
public interface CommonUtils {

  static ResponseObj getResponseObjForFailure(String message) {
    ResponseObj responseObj = new ResponseObj();
    responseObj.setResult(false);
    responseObj.setErrorMessage(message);
    return responseObj;
  }

  static ResponseObj getResponseObjForSuccess(Object data) {
    ResponseObj responseObj = new ResponseObj();
    responseObj.setResult(true);
    responseObj.setData(data);
    return responseObj;
  }

}
