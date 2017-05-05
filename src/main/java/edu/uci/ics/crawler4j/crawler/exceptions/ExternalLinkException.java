package edu.uci.ics.crawler4j.crawler.exceptions;

import uk.org.lidalia.slf4jext.Level;

public class ExternalLinkException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1263425829621015228L;
	public Level level;

	public ExternalLinkException(Level level, String msg) {
		super(msg);
		this.level = level;
	}

	public ExternalLinkException(String msg) {
		super(msg);
	}
}
