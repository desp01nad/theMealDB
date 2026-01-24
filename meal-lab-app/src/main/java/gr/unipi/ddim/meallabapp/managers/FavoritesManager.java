package gr.unipi.ddim.meallabapp.managers;

import gr.unipi.ddim.meallabapi.models.Meal;

public final class FavoritesManager extends AbstractMealStoreManager {

	public FavoritesManager() {
		super("favorites.json");
	}

	public boolean isFavorite(String mealId) {
		return contains(mealId);
	}

	/**
	 * Convenience: add if missing, otherwise remove. Returns new state (true = now
	 * favorite).
	 */
	public boolean toggleFavs(Meal meal) {
		return toggle(meal);
	}
}