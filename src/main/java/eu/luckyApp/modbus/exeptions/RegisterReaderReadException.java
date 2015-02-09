package eu.luckyApp.modbus.exeptions;

public class RegisterReaderReadException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2078127863399119776L;

	public RegisterReaderReadException() {
		super();
	}

	public RegisterReaderReadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public RegisterReaderReadException(String message, Throwable cause) {
		super(message, cause);

	}

	public RegisterReaderReadException(String message) {
		super(message);

	}

	public RegisterReaderReadException(Throwable cause) {
		super(cause);

	}
	

}
