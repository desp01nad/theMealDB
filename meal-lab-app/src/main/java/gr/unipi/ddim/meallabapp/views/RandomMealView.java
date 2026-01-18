package gr.unipi.ddim.meallabapp.views;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RandomMealView extends VBox {
	private final MealClient client;
	private final Label mealTitle;

	public RandomMealView(MealClient client) {
		this.client = client;

		Meal randomMeal = client.getRandomMeal();

		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);

		this.mealTitle = new Label("Meal title: " + randomMeal.getStrMeal());
		Button randomMealBtn = new Button("Refresh");

		randomMealBtn.setOnAction(event -> fetchRandomMeal());
		
		this.getChildren().addAll(mealTitle, randomMealBtn);
	}

	private void fetchRandomMeal() {
		try {
			Meal randomMeal = client.getRandomMeal();
			if (randomMeal != null) {
                mealTitle.setText("Meal title: " + randomMeal.getStrMeal());
            }
		} catch (Exception e) {
			this.mealTitle.setText("Error fetching meal");
		}
	}
}
