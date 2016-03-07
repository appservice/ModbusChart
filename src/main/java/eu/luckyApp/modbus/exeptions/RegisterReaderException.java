package eu.luckyApp.modbus.exeptions;

public class RegisterReaderException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2078127863399119776L;

	public RegisterReaderException() {
		super();
	}

	public RegisterReaderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public RegisterReaderException(String message, Throwable cause) {
		super(message, cause);

	}

	public RegisterReaderException(String message) {
		super(message);

	}

	public RegisterReaderException(Throwable cause) {
		super(cause);

	}
	

}
