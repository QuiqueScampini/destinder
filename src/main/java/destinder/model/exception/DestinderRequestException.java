package main.java.destinder.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinderRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(DestinderRequestException.class);

	public DestinderRequestException() {
	}

	public DestinderRequestException(String message) {
		super(message);
		logger.error(message);
	}

}
