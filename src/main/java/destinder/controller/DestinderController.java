package main.java.destinder.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import main.java.destinder.model.business.Hotel;
import main.java.destinder.model.business.Reaction;
import main.java.destinder.model.exception.DestinderInternalException;
import main.java.destinder.model.exception.DestinderRequestException;
import main.java.destinder.model.management.PricesManager;
import main.java.destinder.model.management.ReactionManager;

@RestController
public class DestinderController {
	
    @RequestMapping("/")
    public String home() {
    	return "Destinder Root";
    }
	
	@RequestMapping(value = "/destinder/hotels/prices", method = RequestMethod.GET)
    public List<Hotel> hotelPrices(@RequestParam(value = "city_id", required = true) String city_id) {
        return new PricesManager().getHotelPricesFromCity(city_id);
    }

	@RequestMapping(
		value = "/destinder/hotels/likes", 
		method = RequestMethod.POST,
		consumes = "application/json")
    public Reaction likeHotel(@RequestBody String hotelIdJson) {
		return ReactionManager.getInstance().likeHotel(hotelIdJson);
    }
	
	@RequestMapping(
		value = "/destinder/hotels/likes", 
		method = RequestMethod.DELETE,
		consumes = "application/json")
    public Reaction dislikeHotel(@RequestBody String hotelIdJson) {
        return ReactionManager.getInstance().dislikeHotel(hotelIdJson);
    }	

	@RequestMapping(value = "/destinder/myLovedAndHatedHotels", method = RequestMethod.GET)
    public ReactionManager lovedAndHatedHotels() {
        return ReactionManager.getInstance();
    }
	
    @ExceptionHandler({DestinderRequestException.class})
    public void handleException(DestinderRequestException e, HttpServletResponse response) throws IOException {
    	response.sendError(HttpStatus.BAD_REQUEST.value());
    }
    
    @ExceptionHandler({DestinderInternalException.class})
    public void handleException(DestinderInternalException e, HttpServletResponse response) throws IOException {
    	response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
	
}
