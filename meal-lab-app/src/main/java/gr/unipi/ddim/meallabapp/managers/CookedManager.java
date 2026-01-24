package gr.unipi.ddim.meallabapp.managers;

import gr.unipi.ddim.meallabapi.models.Meal;

public final class CookedManager extends AbstractMealStoreManager {

	public CookedManager() {
		super("cooked.json");
	}

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