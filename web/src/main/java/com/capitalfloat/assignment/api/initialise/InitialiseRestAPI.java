package com.capitalfloat.assignment.api.initialise;

import com.capitalfloat.assignment.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@RestController
@RequestMapping(value = "/init")
public class InitialiseRestAPI {

  @Autowired
  private ConfigService configService;

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public boolean getCartItems(){
    configService.createTables();
    return true;
  }

}
