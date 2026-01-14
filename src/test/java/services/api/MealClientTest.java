package services.api;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


import services.model.Meal;


public class MealClientTest {
	
	@Test
	public void testGetRandomMeal_Success() {
		Meal meal = MealClient.getRandomMeal();
		
		assertNotNull("Should return a meal object", meal);
		assertNotNull("Meal should have a name", meal.getStrMeal());
		
		System.out.println("Random Meal Found: " + meal.getStrMeal());
	}
	
	
	@Test
	public void testGetMealById_ValidId() {
		Meal meal = MealClient.getMealById("52772");
		
		assertNotNull("Meal should be found", meal);
		assertNotNull("ID should match request 52772", meal.getIdMeal());
		assertNotNull("Meal name should not be null", meal.getStrMeal());
		
		System.out.println("Meal By ID Found: ID:" + meal.getIdMeal() +  " - Name:" + meal.getStrMeal() );
	}
	
	@Test
	public void testGetMealById_InvalidId() {
		Meal meal = MealClient.getMealById("0000");
		
		assertNull("Meal should be null for invalid ID", meal);
		
		System.out.println("Meal By ID (Invalid): Correctly returned null for ID 0000");
	}
	
	
	
	
	@Test
	public void testSearchMealsByName_ValidName_Found() {
		List<Meal> meals = MealClient.searchMealsByName("Arrabiata");
		
		assertNotNull("Meals list should not be null", meals);
		assertFalse("Meals list should not be empty", meals.isEmpty());	
		
		System.out.println("Search By Name ('Arrabiata'): Found " + meals.size() + " meal(s).");
	}
	
	@Test
	public void testSearchMealsByName_ValidName_NotFound() {
		List<Meal> meals = MealClient.searchMealsByName("UnlikelyFood123");
		
		assertNotNull("Meals list should not be null", meals);
		assertTrue("Meals list should be empty", meals.isEmpty());	
		
		System.out.println("Search By Name ('UnlikelyFood123'): Correctly found 0 meals.");
	}
	
	
	
	@Test
    public void testFilterMealsByIngredient_Found() { 
		List<Meal> meals = MealClient.filterMealsByIngredient("chicken_breast");
		
		assertNotNull("Meals list should not be null", meals);
		assertFalse("Meals list should not be empty", meals.isEmpty());	
		
		System.out.println("Filter By Ingredient ('chicken_breast'): Found " + meals.size() + " meals.");
	}
	
	


}
