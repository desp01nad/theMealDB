package gr.unipi.ddim.meallabapp.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gr.unipi.ddim.meallabapi.models.Meal;

class AbstractMealStoreManagerTest {

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
	void addRemoveToggle_persistsAndTracksState() throws Exception {
		TestStoreManager manager = new TestStoreManager();
		Meal meal = meal("1", "Meal One");

		manager.add(meal);
		assertTrue(manager.contains("1"));

		Path file = tempDir.resolve(".meallab").resolve("test-store.json");
		assertTrue(Files.exists(file));

		manager.remove("1");
		assertFalse(manager.contains("1"));

		assertTrue(manager.toggle(meal));
		assertTrue(manager.contains("1"));

		assertFalse(manager.toggle(meal));
		assertFalse(manager.contains("1"));
	}

	@Test
	void loadFromDisk_restoresMeals() {
		TestStoreManager manager = new TestStoreManager();
		Meal meal = meal("2", "Meal Two");
		manager.add(meal);

		TestStoreManager reloaded = new TestStoreManager();
		assertTrue(reloaded.contains("2"));
		assertEquals(1, reloaded.getAll().size());
	}

	@Test
	void invalidMealsAreIgnored() {
		TestStoreManager manager = new TestStoreManager();

		Meal nullId = meal(null, "No Id");
		Meal blankId = meal("  ", "Blank Id");

		manager.add(nullId);
		manager.add(blankId);
		assertTrue(manager.getAll().isEmpty());

		assertFalse(manager.toggle(null));
		assertFalse(manager.toggle(blankId));
		assertTrue(manager.getAll().isEmpty());
	}

	private static Meal meal(String id, String name) {
		Meal meal = new Meal();
		meal.setIdMeal(id);
		meal.setStrMeal(name);
		meal.setStrMealThumb("http://example.com/meal.jpg");
		return meal;
	}

	private static class TestStoreManager extends AbstractMealStoreManager {
		TestStoreManager() {
			super("test-store.json");
		}
	}
}
