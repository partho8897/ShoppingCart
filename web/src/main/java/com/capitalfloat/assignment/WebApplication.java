package com.capitalfloat.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by @author Partho Paul on 17/06/21
 */
@SpringBootApplication
//@Import({
//    WebConfig.class
//})
public class WebApplication {

  public static void main(String[] args){
    SpringApplication.run(WebApplication.class,args);
  }

}
