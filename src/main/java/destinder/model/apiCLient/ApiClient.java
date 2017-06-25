package main.java.destinder.model.apiCLient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.json.simple.parser.ParseException;

import main.java.destinder.model.business.Hotel;
import main.java.destinder.model.management.HotelManager;

public class ApiClient {
	
	private static String hotelsURL="https://api.despegar.com/v3/hotels?cities=982,4544,5296,2820,52606,31651";
	private static String pricesURL="https://api.despegar.com/v3/hotels/prices?hotels=HOTELS_LIST&country=ar&distribution=2";
	private static String apiKey="aa58b8708cf44b5893a0de0b2e410343";

	public ApiClient() {
	}
	
	public void loadHotels() throws MalformedURLException, IOException, ParseException {
		HttpURLConnection conn = getConnection(hotelsURL);
		HotelManager.getInstance().parseHotels(getInputReaderFromConnection(conn));
		conn.disconnect();
	}
	
	public void setPricesFromHotels(List<Hotel> listOfHotels)  throws MalformedURLException, IOException, ParseException {
		HttpURLConnection conn = getConnection(this.getUrlOf(listOfHotels));
		InputStreamReader inputReader = getInputReaderFromConnection(conn);
		HotelManager.getInstance().setPricesToHotels(inputReader);
		conn.disconnect();
	}

	private InputStreamReader getInputReaderFromConnection(HttpURLConnection conn) throws IOException {
		InputStreamReader inputReader = null;
		if ("gzip".equals(conn.getContentEncoding())) 
			inputReader = new InputStreamReader(new GZIPInputStream(conn.getInputStream()));
		else 
			inputReader = new InputStreamReader(conn.getInputStream());
		return inputReader;
	}

	private HttpURLConnection getConnection(String urlString) throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept-Encoding", "gzip");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("X-ApiKey", apiKey);
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		return conn;
	}
	

	
	public String getUrlOf(List<Hotel> listOfHotels) {
		List<String> hotelsIds = listOfHotels
				.stream()
				.map(hotel -> hotel.getId())
				.collect(Collectors.toList());	
	
		return pricesURL.replaceAll("HOTELS_LIST",String.join(",", hotelsIds));
	}
	
}
