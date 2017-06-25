package main.java.destinder.model.business;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hotel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final String id;
    private final String name;
    
    @JsonIgnore
    private final String city_id;
    private final long stars;
    private final String main_picture;
    private Price_Detail price_detail;

	public Hotel(String id, String name, long stars,String city_id, String mainPicture) {
        this.id = id;
        this.name = name;
        this.city_id = city_id;
        this.stars=stars;
        this.main_picture=mainPicture;
        price_detail=null;
    }

	public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public String getCity_id() {
		return city_id;
	}

	public long getStars() {
		return stars;
	}

	@JsonProperty("main_picture")
	public String getMainPicture() {
		return main_picture;
	}

	public Price_Detail getPrice_detail() {
		return price_detail;
	}

	public void setPrice_detail(Price_Detail price_detail) {
		this.price_detail = price_detail;
	}
	
}
