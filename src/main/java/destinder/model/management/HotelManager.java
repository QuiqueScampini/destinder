package main.java.destinder.model.management;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.destinder.model.apiCLient.ApiClient;
import main.java.destinder.model.business.Hotel;
import main.java.destinder.model.business.Price_Detail;


public class HotelManager {
	
	final static Logger logger = LoggerFactory.getLogger(HotelManager.class);
	
	private static String hotelsFile="./snapshot/Snapshot.bin";
	
	private static HotelManager instance;
	private HashMap<String,Hotel> hotels;
	
	private HotelManager() {
		hotels=new HashMap<String,Hotel>();
	}
	
	public static HotelManager getInstance( ) {
		if(instance == null)
			instance=new HotelManager();	
		return instance;
	}
	
	public void loadHotelsFromApi() {
		try {			
			new ApiClient().loadHotels();
			logger.info("Hotels Loaded Successfully");
		} catch (IOException e) {
			logger.error("There was a problem loading the hotels :" + e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("There was an error parsing the hotels :" + e.getMessage());
			e.printStackTrace();
		}
		
		try {
			this.saveHotelsSnapshot();
			logger.info("Hotels Snapshot saved Successfully");
		} catch (IOException e) {
			logger.error("There was an error saving the hotels Snapshot :" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void saveHotelsSnapshot() throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(hotelsFile));
		outputStream.writeObject(hotels);
		outputStream.close();
	} 

	public void loadHotelsSnapshot() throws ClassNotFoundException, FileNotFoundException, IOException {
		Object hotelsBin = new ObjectInputStream(new FileInputStream(hotelsFile)).readObject();
		hotels = (HashMap<String,Hotel>) hotelsBin;
	}

	public void parseHotels(InputStreamReader inputReader) throws IOException, ParseException {
	
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(inputReader);

		JSONArray hotels = (JSONArray) jsonObject.get("items");
		
		JSONObject jsonHotel,jsonCity,jsonMainPicture;
		String idHotel,idCity,mainPicTureUrl,hotelName;
		long hotelStars;
		
		Iterator<JSONObject> iterator = hotels.iterator();
		while (iterator.hasNext()) {
			jsonHotel = iterator.next();
			
			jsonCity = (JSONObject) jsonHotel.get("location");
			jsonCity = (JSONObject) jsonCity.get("city");
			jsonMainPicture = (JSONObject) jsonHotel.get("main_picture");
			
			idHotel = (String) jsonHotel.get("id"); 
			hotelName =(String) jsonHotel.get("name");
			hotelStars =  (long) jsonHotel.get("stars");
			idCity = (String) jsonCity.get("id");
			mainPicTureUrl= (String) jsonMainPicture.get("url");
			
			this.addHotel(idHotel,hotelName,hotelStars,idCity,mainPicTureUrl);
		}
	}
	
	private void addHotel(String idHotel, String hotelName, long hotelStars, String idCity, String mainPicTureUrl) {
		hotels.put(idHotel, new Hotel(idHotel,hotelName,hotelStars,idCity,mainPicTureUrl));
	}
	
	public void setPricesToHotels(InputStreamReader inputReader) throws IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(inputReader);		
		JSONArray items = (JSONArray) jsonObject.get("items");

		JSONObject jsonItem,jsonPriceDetail;
		String hotel_ID,currency;
		Double total;
		
		Iterator<JSONObject> iterator = items.iterator();
		while (iterator.hasNext()) {
			jsonItem = iterator.next();
			hotel_ID = (String) jsonItem.get("hotel_id");
			jsonPriceDetail = (JSONObject) jsonItem.get("price_detail");
			
			if (hotel_ID != null & jsonPriceDetail != null){
				currency=(String) jsonPriceDetail.get("currency");
				total= (Double) jsonPriceDetail.get("total");
				
				if (currency != null && total != null)
					this.setPriceToHotel(hotel_ID,currency,total);
			}

			
		}
	}
	
	public void setPriceToHotel(String hotel_ID, String currency, Double total) {
		this.getHotelWithID(hotel_ID).setPrice_detail(new Price_Detail(currency,total));		
	}
	
	public Hotel getHotelWithID(String hotelID){		
		return hotels.get(hotelID);
	}
	
	public List<Hotel> getHotelsFromCity(String cityID){
		return hotels.values()
			.stream()
			.filter( hotel -> hotel.getCity_id().equals(cityID))
			.collect(Collectors.toList());	
	}

}
