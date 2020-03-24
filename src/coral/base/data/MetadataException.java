package coral.base.data;

public class MetadataException extends RuntimeException {

	private static final long serialVersionUID = -9077735962523998932L;

	public MetadataException() {
	}

	public MetadataException(String message) {
		super(message);
	}

	public MetadataException(Throwable cause) {
		super(cause);
	}

	public MetadataException(String message, Throwable cause) {
		super(message, cause);
	}

}
