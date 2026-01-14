package services.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MealTest {
	private Meal meal;
	
		
	@Before
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
		
		assertEquals("Should have exactly 2 valid ingredients", 2, result.size());
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
	    
	    assertEquals("Should have exactly 2 valid ingredients", 2, result.size());
		assertEquals("Salt", result.get(0));
		assertEquals("Pepper", result.get(1));
	}
	
	
	@Test
	public void getIngredientsList_WithNullIngredients() {
		meal.setStrIngredient1("");
		meal.setStrMeasure1("");
		
		List<String> result = meal.getIngredientsList();
		
		assertEquals("List should be empty when ingredients are blank strings", 0, result.size());
	}
	
	@Test
	public void getIngredientsList_WithMeasureOnly_ShouldIgnore() {
	    meal.setStrIngredient1("");
	    meal.setStrMeasure1("1 tsp");

	    List<String> result = meal.getIngredientsList();

	    assertTrue("Ingredient without name should be ignored", result.isEmpty());
	}

		
	
	@Test
	public void getIngredientsList_WithEmptyIngredients() {
		
		List<String> result = meal.getIngredientsList();
		
		
		assertTrue("List should be empty for new Meal",result.isEmpty());
	}
	

}
