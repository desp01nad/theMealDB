package gr.unipi.ddim.meallabapp.managers;

import gr.unipi.ddim.meallabapi.models.Meal;

/** Manages the user's cooked meals with local persistence. */
public final class CookedManager extends AbstractMealStoreManager {

	public CookedManager() {
		super("cooked.json");
	}

	/** Checks whether the meal id is currently marked as cooked. */
	public boolean isCooked(String mealId) {
		return contains(mealId);
	}

	/**
	 * Convenience: add if missing, otherwise remove. Returns new state (true = now
	 * cooked).
	 */
	public boolean toggleCooked(Meal meal) {
		return toggle(meal);
	}
}
