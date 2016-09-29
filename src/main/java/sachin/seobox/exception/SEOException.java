package sachin.seobox.exception;

import uk.org.lidalia.slf4jext.Level;

public class SEOException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	public Level level;

	public SEOException(String msg) {
		super(msg);
	}

	public SEOException(Level level, String msg) {
		super(msg);
		this.level = level;
	}

	public SEOException() {

	}
}
