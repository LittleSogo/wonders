package coral.base.data;

public class DataException extends RuntimeException {

	private static final long serialVersionUID = 9037744401287856633L;

	public DataException() {
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

}
