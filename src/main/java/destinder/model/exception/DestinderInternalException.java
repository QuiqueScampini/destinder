package main.java.destinder.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinderInternalException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(DestinderInternalException.class);
	
	public DestinderInternalException() {
	}

	public DestinderInternalException(String message) {
		super(message);
		logger.error(message);
	}
}
