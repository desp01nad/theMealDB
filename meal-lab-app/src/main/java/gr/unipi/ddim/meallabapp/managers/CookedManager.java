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

/*
 * Keeps cooked meals in-memory + persists them to a small JSON file.
 * We store only the fields we need for the cards (id, name, thumb).
 */
public class CookedManager {
	
	private final Map<String, Meal> cookedById = new LinkedHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Path filePath;

    public CookedManager() {
        this.filePath = defaultFilePath();
        load();
    }

    public boolean isCooked(String mealId) {
        if (mealId == null || mealId.isBlank()) return false;
        return cookedById.containsKey(mealId);
    }

    public void add(Meal meal) {
        if (meal == null || meal.getIdMeal() == null || meal.getIdMeal().isBlank()) return;

        // store a "light" Meal with only what we need in favorites page cards
        Meal light = new Meal();
        light.setIdMeal(meal.getIdMeal());
        light.setStrMeal(meal.getStrMeal());
        light.setStrMealThumb(meal.getStrMealThumb());

        cookedById.put(light.getIdMeal(), light);
        save();
    }

    public void remove(String mealId) {
        if (mealId == null || mealId.isBlank()) return;
        if (cookedById.remove(mealId) != null) {
            save();
        }
    }

    /** Convenience: add if missing, otherwise remove. Returns new state (true = now cooked). */
    public boolean toggleCooked(Meal meal) {
        if (meal == null || meal.getIdMeal() == null || meal.getIdMeal().isBlank()) return false;

        String id = meal.getIdMeal();
        if (cookedById.containsKey(id)) {
        	cookedById.remove(id);
            save();
            return false;
        }

        add(meal);
        return true;
    }

    public List<Meal> getAll() {
        return new ArrayList<>(cookedById.values());
    }

    
    // ---------- Persistence ----------

    private static Path defaultFilePath() {
        Path dir = Paths.get(System.getProperty("user.home"), ".meallab");
        return dir.resolve("cooked.json");
    }

    private void load() {
        try {
            if (!Files.exists(filePath)) return;

            byte[] bytes = Files.readAllBytes(filePath);
            if (bytes.length == 0) return;

            List<Meal> list = mapper.readValue(bytes, new TypeReference<List<Meal>>() {});
            cookedById.clear();
            if (list != null) {
                for (Meal m : list) {
                    if (m != null && m.getIdMeal() != null && !m.getIdMeal().isBlank()) {
                    	cookedById.put(m.getIdMeal(), m);
                    }
                }
            }
        } catch (Exception e) {
            // don't crash the app because of a broken file
            System.err.println("Could not load cooked meals: " + e.getMessage());
        }
    }

    private void save() {
        try {
            Files.createDirectories(filePath.getParent());
            List<Meal> list = getAll();
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), list);
        } catch (IOException e) {
            System.err.println("Could not save cooked meals: " + e.getMessage());
        }
    }
}



