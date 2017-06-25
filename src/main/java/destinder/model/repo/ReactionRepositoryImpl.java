package main.java.destinder.model.repo;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

public class ReactionRepositoryImpl implements ReactionRepository {

    private SetOperations<String, String> setOperations;
  
	public ReactionRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
		setOperations = redisTemplate.opsForSet();
	}
	
	@Override
	public void saveReaction(String reactionKind, String idHotel) {
		setOperations.add(reactionKind, idHotel);
	}

	@Override
	public void deleteReaction(String reactionKind, String idHotel) {
		setOperations.remove(reactionKind, 0, idHotel);
	}

	@Override
	public Set<String> findAllReactionsOfKind(String reactionKind) {
		return setOperations.members(reactionKind);
	}

}
