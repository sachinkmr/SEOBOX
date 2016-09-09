package edu.uci.ics.crawler4j.crawler.exceptions;

import uk.org.lidalia.slf4jext.Level;

public class InitializationException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -3642590055518638924L;
	public Level level;

	public InitializationException(String msg) {
		super(msg);
	}

	public InitializationException(Level level, String msg) {
		super(msg);
		this.level = level;
	}

	public InitializationException() {

	}
}
