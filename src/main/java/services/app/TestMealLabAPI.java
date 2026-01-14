package services.app;

import java.util.List;

import services.api.MealClient;
import services.exception.MealApiException;
import services.model.Meal;

public class TestMealLabAPI {
	
	public static void main(String[] args) {
		try {
			System.out.println("============RANDOM MEAL=============");
			Meal randomMeal = MealClient.getRandomMeal();
			if (randomMeal != null) {
				System.out.println(randomMeal.getStrMeal());
			} else {
				System.out.println("No random meal found.");
			}
		
			System.out.println("============MEAL BY ID==============");
			Meal mealById = MealClient.getMealById("52772");
			if (mealById != null) { 
				System.out.println(mealById.getStrMeal());
			} else { 
				System.out.println("There is no meal found with id: 52772");
			}
			
			System.out.println("============MEALS BY NAME===========");
			List<Meal> mealsByName = MealClient.searchMealsByName("Air");
			if (mealsByName != null) {
				for (Meal meal: mealsByName) {
					System.out.println(meal.getStrMeal());
				}
			} else {
				System.out.println("There is no meal with name containing the word: Air");
			}
		
			System.out.println("============MEALS BY INGR===========");
			List<Meal> mealsByIngredient = MealClient.filterMealsByIngredient("chicken_breast");
			if (mealsByIngredient != null ) {
				for (Meal meal: mealsByIngredient) {
					System.out.println(meal.getStrMeal());
				}
			} else {
				System.out.println("There are no meals with the ingredient: chicken_breast");
			}
			
		
		} catch (MealApiException e) {
			System.err.println("API Error: " + e.getMessage());
		} 
	}
}
