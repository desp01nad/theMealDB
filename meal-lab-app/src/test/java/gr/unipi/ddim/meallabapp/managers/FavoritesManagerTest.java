package gr.unipi.ddim.meallabapp.managers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gr.unipi.ddim.meallabapi.models.Meal;

class FavoritesManagerTest {

	@TempDir
	Path tempDir;

	private String originalUserHome;

	@BeforeEach
	void setUp() {
		originalUserHome = System.getProperty("user.home");
		System.setProperty("user.home", tempDir.toString());
	}

	@AfterEach
	void tearDown() {
		if (originalUserHome != null) {
			System.setProperty("user.home", originalUserHome);
		}
	}

	@Test
	void toggleFavs_addsAndRemoves() {
		FavoritesManager manager = new FavoritesManager();
		Meal meal = meal("10", "Test Meal");

		assertTrue(manager.toggleFavs(meal));
		assertTrue(manager.isFavorite("10"));

		assertFalse(manager.toggleFavs(meal));
		assertFalse(manager.isFavorite("10"));
	}

	private static Meal meal(String id, String name) {
		Meal meal = new Meal();
		meal.setIdMeal(id);
		meal.setStrMeal(name);
		meal.setStrMealThumb("http://example.com/meal.jpg");
		return meal;
	}
}
