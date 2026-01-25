package gr.unipi.ddim.meallabapi.exceptions;

/** Runtime exception used for API and parsing failures in the Meal client. */
public class MealApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MealApiException(String message) {
		super(message);
	}

	public MealApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
