package gr.unipi.ddim.meallabapi.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** API response wrapper for a list of meals. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealResponse {
	private List<Meal> meals;

	/** Returns the list of meals (may be null). */
	public List<Meal> getMeals() {
		return meals;
	}

	/** Sets the list of meals parsed from the API. */
	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
}
