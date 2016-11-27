package play.ground.exception;

public class DAORuntimeException extends RuntimeException {

	private static final long serialVersionUID = -866339932632143878L;

	public DAORuntimeException() {
	}

	public DAORuntimeException(final String message) {
		super(message);
	}

	public DAORuntimeException(final Throwable cause) {
		super(cause);
	}

	public DAORuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
