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

    public SEOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);

    }

    public SEOException(String message, Throwable cause) {
	super(message, cause);

    }

    public SEOException(Throwable cause) {
	super(cause);

    }

    public SEOException(Level level, String msg) {
	super(msg);
	this.level = level;
    }

    public SEOException() {

    }
}
