package main.java.destinder.model.business;

import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reaction {
	
	private final String hotel_id;
	private final AtomicLong likes;

	public Reaction(String hotel_id) {
		this.hotel_id=hotel_id;
		this.likes = new AtomicLong();
	}

	@JsonProperty("id")
	public String getHotel_id() {
		return hotel_id;
	}

	public AtomicLong getLikes() {
		return likes;
	}
	
	public void like(){
		likes.incrementAndGet();
	}
	
	public void dislike(){
		if(likes.intValue() > 0)
			likes.decrementAndGet();
	}

}
