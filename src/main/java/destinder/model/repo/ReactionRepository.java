package main.java.destinder.model.repo;

import java.util.Set;

public interface ReactionRepository {
	void saveReaction(String key, String idHotel);
	void deleteReaction(String key, String idHotel);
	Set<String> findAllReactionsOfKind(String reactionKind);
}
