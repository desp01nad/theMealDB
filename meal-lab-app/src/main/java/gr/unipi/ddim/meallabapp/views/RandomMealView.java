package gr.unipi.ddim.meallabapp.views;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.scene.control.Button;


public class RandomMealView extends MealDetailsView {
	private String lastMealId;
	private final Button newRandomMealBtn;
	
	public RandomMealView(MealClient client) {
		super(client);
		
		newRandomMealBtn = new Button("New Random Recipe");
		newRandomMealBtn.setFocusTraversable(false);
		newRandomMealBtn.setOnAction(event -> fetchAndShowNewRandomMeal());
		
		buttonsRow.getChildren().add(0, newRandomMealBtn);
		
		fetchAndShowNewRandomMeal();
	}
	
	public void fetchAndShowNewRandomMeal() {
		
		newRandomMealBtn.setDisable(true);
        mealTitleLabel.setText("Loading...");
        mealImageView.setImage(null);
        
        new Thread(() -> {
            try {
                Meal meal;
                do {
                    meal = client.getRandomMeal();
                } while (meal != null
                        && meal.getIdMeal() != null
                        && meal.getIdMeal().equals(lastMealId));

                Meal finalMeal = meal;

                javafx.application.Platform.runLater(() -> {
                    if (finalMeal != null) {
                        showMeal(finalMeal);
                        lastMealId = finalMeal.getIdMeal();
                    } else {
                        mealTitleLabel.setText("No meal found.");
                    }
                    newRandomMealBtn.setDisable(false);
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    mealTitleLabel.setText("Error loading meal");
                    newRandomMealBtn.setDisable(false);
                });
            }
        }).start();
    }
}
	

    