package play.ground.exception;

public class DAOException extends Exception {

	private static final long serialVersionUID = 9211757807165015938L;

	public DAOException() {
	}

	public DAOException(final String message) {
		super(message);
	}

	public DAOException(final Throwable cause) {
		super(cause);
	}

	public DAOException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
