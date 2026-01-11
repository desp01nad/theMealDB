package services;

import java.io.IOException;
import java.util.List;

public class TestMealLabAPI {
	
	public static void main(String[] args) throws IOException {
		System.out.println("============RANDOM MEAL=============");
		Meal randomMeal = MealClient.getRandomMeal();
		System.out.println(randomMeal.getStrMeal());
		
		System.out.println("============MEAL BY ID==============");
		Meal mealById = MealClient.getMealById("52772");
		System.out.println(mealById.getStrMeal());
		
		System.out.println("============MEALS BY NAME===========");
		List<Meal> mealsByName = MealClient.searchMealsByName("Air");
		for (Meal meal: mealsByName) {
			System.out.println(meal.getStrMeal());
		}

		System.out.println("============MEALS BY INGR===========");
		List<Meal> mealsByIngredient = MealClient.filterMealsByIngredient("chicken_breast");
		for (Meal meal: mealsByIngredient) {
			System.out.println(meal.getStrMeal());
		}
		
	}

}
