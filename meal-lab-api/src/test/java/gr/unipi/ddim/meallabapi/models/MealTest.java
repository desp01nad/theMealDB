package gr.unipi.ddim.meallabapi.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gr.unipi.ddim.meallabapi.models.Meal;

public class MealTest {
	private Meal meal;

	@BeforeEach
	public void setUp() {
		this.meal = new Meal();
	}

	@Test
	public void getIngredientsList_WithValidIngredients() {
		meal.setStrIngredient1("Chicken");
		meal.setStrMeasure1("1kg");
		meal.setStrIngredient2("Onion");
		meal.setStrMeasure2("2 slices");

		List<String> result = meal.getIngredientsList();

		assertEquals(2, result.size());
		assertEquals("Chicken - 1kg", result.get(0));
		assertEquals("Onion - 2 slices", result.get(1));
	}

	@Test
	public void getIngredientsList_WithIngredientOnly_NoMeasure() {
		meal.setStrIngredient1("Salt");
		meal.setStrMeasure1("");
		meal.setStrIngredient2("Pepper");
		meal.setStrMeasure2(null);

		List<String> result = meal.getIngredientsList();

		assertEquals(2, result.size());
		assertEquals("Salt", result.get(0));
		assertEquals("Pepper", result.get(1));
	}

	@Test
	public void getIngredientsList_WithNullIngredients() {
		meal.setStrIngredient1("");
		meal.setStrMeasure1("");

		List<String> result = meal.getIngredientsList();

		assertEquals(0, result.size());
	}

	@Test
	public void getIngredientsList_WithMeasureOnly_ShouldIgnore() {
		meal.setStrIngredient1("");
		meal.setStrMeasure1("1 tsp");

		List<String> result = meal.getIngredientsList();

		assertTrue(result.isEmpty());
	}

	@Test
	public void getIngredientsList_WithEmptyIngredients() {

		List<String> result = meal.getIngredientsList();

		assertTrue(result.isEmpty());
	}

}
