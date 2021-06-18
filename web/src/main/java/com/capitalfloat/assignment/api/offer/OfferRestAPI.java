package com.capitalfloat.assignment.api.offer;

import com.capitalfloat.assignment.common.ResponseObj;
import com.capitalfloat.assignment.offer.request.AddOfferRequest;
import com.capitalfloat.assignment.offer.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author Partho Paul on 18/06/21
 */
@RestController
@RequestMapping(value = "/offer")
public class OfferRestAPI {

  @Autowired
  private OfferService offerService;

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj getOffers(@RequestParam(value = "productId") String productId){
    return offerService.getOffers(productId);
  }

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj addOffer(@RequestBody AddOfferRequest addOfferRequest){
    return offerService.addOffer(addOfferRequest);
  }


  @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObj deleteOffer(@RequestParam(value = "offerId") String offerId){
    return offerService.removeOffer(offerId);
  }

}
