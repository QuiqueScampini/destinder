package main.java.destinder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.destinder.model.management.HotelManager;
import main.java.destinder.model.management.ReactionManager;

@SpringBootApplication
public class Application {

	final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public CommandLineRunner init(){
        return (args) -> {
            HotelManager.getInstance().loadHotelsFromApi();
            ReactionManager.getInstance().setRepositoryWith(redisTemplate());
            logger.info("Destinder Application started successfully");
        };
    }
    
    @Bean
    JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("localhost");
        factory.setPort(6379);
        factory.setUsePool(true);
        return factory;
    }

    @Bean
    RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        return template;
    }
    
}
