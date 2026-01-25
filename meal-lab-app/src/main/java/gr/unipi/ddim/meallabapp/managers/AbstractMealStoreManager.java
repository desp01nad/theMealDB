package gr.unipi.ddim.meallabapp.managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.unipi.ddim.meallabapi.models.Meal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Shared persistence + in-memory storage for Meal "collections" (favorites,
 * cooked, etc.). Stores "light" Meal objects (id, name, thumb) and persists
 * them as JSON.
 */
public abstract class AbstractMealStoreManager {

	private final Map<String, Meal> byId = new LinkedHashMap<>();
	private final ObjectMapper mapper = new ObjectMapper();
	private final Path filePath;
	private static final Logger logger = LogManager.getLogger(AbstractMealStoreManager.class);

	protected AbstractMealStoreManager(String fileName) {
		this.filePath = defaultFilePath(fileName);
		load();
	}

	/** Returns whether the collection contains the given meal id. */
	public final boolean contains(String mealId) {
		if (mealId == null || mealId.isBlank())
			return false;
		return byId.containsKey(mealId);
	}

	/** Adds a meal to the collection and persists it. */
	public final void add(Meal meal) {
		if (!isValid(meal))
			return;

		Meal light = toLightMeal(meal);
		byId.put(light.getIdMeal(), light);
		save();
	}

	/** Removes a meal by id (no-op if missing) and persists. */
	public final void remove(String mealId) {
		if (mealId == null || mealId.isBlank())
			return;
		if (byId.remove(mealId) != null) {
			save();
		}
	}

	/** Add if missing, otherwise remove. Returns new state (true = now present). */
	public final boolean toggle(Meal meal) {
		if (!isValid(meal))
			return false;

		String id = meal.getIdMeal();
		if (byId.containsKey(id)) {
			byId.remove(id);
			save();
			return false;
		}

		add(meal);
		return true;
	}

	/** Returns a snapshot list of all stored meals. */
	public final List<Meal> getAll() {
		return new ArrayList<>(byId.values());
	}

	// ---------- Helpers ----------

	private static boolean isValid(Meal meal) {
		return meal != null && meal.getIdMeal() != null && !meal.getIdMeal().isBlank();
	}

	private static Meal toLightMeal(Meal meal) {
		Meal light = new Meal();
		light.setIdMeal(meal.getIdMeal());
		light.setStrMeal(meal.getStrMeal());
		light.setStrMealThumb(meal.getStrMealThumb());
		return light;
	}

	private static Path defaultFilePath(String fileName) {
		Path dir = Paths.get(System.getProperty("user.home"), ".meallab");
		return dir.resolve(fileName);
	}

	private void load() {
		try {
			if (!Files.exists(filePath))
				return;

			byte[] bytes = Files.readAllBytes(filePath);
			if (bytes.length == 0)
				return;

			List<Meal> list = mapper.readValue(bytes, new TypeReference<List<Meal>>() {
			});
			byId.clear();

			if (list != null) {
				for (Meal m : list) {
					if (m != null && m.getIdMeal() != null && !m.getIdMeal().isBlank()) {
						byId.put(m.getIdMeal(), m);
					}
				}
			}
		} catch (Exception e) {
			// don't crash the app because of a broken file
			logger.error("Could not load {}", filePath.getFileName(), e);
		}
	}

	private void save() {
		try {
			Files.createDirectories(filePath.getParent());
			mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), getAll());
		} catch (IOException e) {
			logger.error("Could not save {}", filePath.getFileName(), e);
		}
	}
}
