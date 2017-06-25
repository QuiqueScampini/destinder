package main.java.destinder.model.management;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.destinder.model.apiCLient.ApiClient;
import main.java.destinder.model.business.Hotel;
import main.java.destinder.model.exception.DestinderInternalException;
import main.java.destinder.model.exception.DestinderRequestException;


public class PricesManager {
	
	final static Logger logger = LoggerFactory.getLogger(PricesManager.class);
	
	public PricesManager() {}

	public List<Hotel> getHotelPricesFromCity(String city_id) {
		
		this.validateCityID(city_id);
		
		List<Hotel> cityHotels = HotelManager.getInstance().getHotelsFromCity(city_id);
		
		if(cityHotels.isEmpty())
			throw new DestinderInternalException("There are no hotels in the requested city " + city_id );
		
		//Being just a test app we are just asquing only for 10 hotels
		while(cityHotels.size()>9)
			cityHotels.remove(cityHotels.size()-1);
				
		try {
			new ApiClient().setPricesFromHotels(cityHotels);
		} catch (IOException e) {
			logger.error("There was a problem loading hotels prices :" + e.getMessage());
		} catch (ParseException e) {
			logger.error("There was a problem parsing hotels prices :" + e.getMessage());
		}

		//We only return city hotels that have price detail
		cityHotels = cityHotels
				.stream()
				.filter( hotel -> hotel.getPrice_detail() != null)
				.collect(Collectors.toList());
		
		if(cityHotels.isEmpty())
			throw new DestinderInternalException("There are no hotels in the city " + city_id + " that contains price detail");
		
		return cityHotels;
	}

	private void validateCityID(String city_id) {
		if(city_id == null || city_id.isEmpty())
			throw new DestinderRequestException("The 'city_id' parameter must not be null or empty");
	}

}
