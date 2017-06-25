package main.java.destinder.model.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.java.destinder.model.business.Reaction;
import main.java.destinder.model.exception.DestinderInternalException;
import main.java.destinder.model.exception.DestinderRequestException;
import main.java.destinder.model.repo.ReactionRepositoryImpl;

@Component
public class ReactionManager {
	
	final static Logger logger = LoggerFactory.getLogger(ReactionManager.class);

	private static ReactionManager instance;
	private static final String likedKEY = "likedHotels";
	private static final String notLikedKEY = "Hotel";

	@JsonIgnore
	private ReactionRepositoryImpl reactionRepo;
	@JsonIgnore
	private final List<Reaction> reactions;
	
	public static ReactionManager getInstance( ) {
		if(instance == null)
			instance=new ReactionManager();	
		return instance;
	}
	
	public void setRepositoryWith(RedisTemplate<String, String> redisTemplate) {
		reactionRepo= new ReactionRepositoryImpl(redisTemplate);
	}
	
	public Set<String> getLikedHotels() {
		return reactionRepo.findAllReactionsOfKind(likedKEY);
	}

	public Set<String> getNotlikedHotels() {
		return reactionRepo.findAllReactionsOfKind(notLikedKEY);
	}
	
	private ReactionManager() {
		reactions= new ArrayList<Reaction>();
	}

	public Reaction dislikeHotel(String hotelIdJson) {
		
		String hotelID = this.parseJson(hotelIdJson);
		this.validateHotel(hotelID);
		Reaction hotelReaction = this.getHotelReaction(hotelID);
		
		reactionRepo.saveReaction(notLikedKEY, hotelID);
		reactionRepo.deleteReaction(likedKEY, hotelID);
		
		hotelReaction.dislike();
		return hotelReaction;
	}

	public Reaction likeHotel(String hotelIdJson) {
		
		String hotelID = this.parseJson(hotelIdJson);
		this.validateHotel(hotelID);
		Reaction hotelReaction = this.getHotelReaction(hotelID);
		
		reactionRepo.saveReaction(likedKEY, hotelID);
		reactionRepo.deleteReaction(notLikedKEY, hotelID);
		hotelReaction.like();
		return hotelReaction;
	}
	
	private void validateHotel(String hotelID) {
		if(HotelManager.getInstance().getHotelWithID(hotelID) == null)
			throw new DestinderInternalException("There hotel with id "+ hotelID + " does not exist" );
	}

	private Reaction getHotelReaction(String hotelID) {
		for (Reaction reaction : reactions)
		   if(reaction.getHotel_id().equals(hotelID))
			   return reaction;
		
		Reaction reaction = new Reaction(hotelID);
		reactions.add(reaction);
		return reaction;
	}

	private String parseJson(String hotelIdJson) {

		JSONParser parser = new JSONParser();
		String hotelID=null;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(hotelIdJson);
			hotelID = (String) jsonObject.get("hotel_id");
		} catch (ParseException e) {
			throw new DestinderRequestException("There was a problem parsing the hotel id from the request");
		}
		return hotelID;
	}
}
