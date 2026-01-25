package gr.unipi.ddim.meallabapp.managers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gr.unipi.ddim.meallabapi.models.Meal;

class CookedManagerTest {

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
	void toggleCooked_addsAndRemoves() {
		CookedManager manager = new CookedManager();
		Meal meal = meal("20", "Test Meal");

		assertTrue(manager.toggleCooked(meal));
		assertTrue(manager.isCooked("20"));

		assertFalse(manager.toggleCooked(meal));
		assertFalse(manager.isCooked("20"));
	}

	private static Meal meal(String id, String name) {
		Meal meal = new Meal();
		meal.setIdMeal(id);
		meal.setStrMeal(name);
		meal.setStrMealThumb("http://example.com/meal.jpg");
		return meal;
	}
}
