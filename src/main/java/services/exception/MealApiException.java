package services.exception;

public class MealApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public MealApiException(String message) {
		super(message);
	}
	
	public MealApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
