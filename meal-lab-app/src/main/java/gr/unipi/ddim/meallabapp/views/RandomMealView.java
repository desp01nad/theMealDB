package gr.unipi.ddim.meallabapp.views;

import java.io.ByteArrayInputStream;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.ImageSize;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RandomMealView extends VBox {
    private final MealClient client;
    private final Label mealTitle;
    private final ImageView mealImageView;

    public RandomMealView(MealClient client) {
        this.client = client;
        
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        
        this.mealTitle = new Label("");
        
        this.mealImageView = new ImageView();
        this.mealImageView.setFitWidth(300);
        this.mealImageView.setPreserveRatio(true);

        Button randomMealBtn = new Button("Next Random Meal");
        randomMealBtn.setOnAction(event -> fetchRandomMeal());

        this.getChildren().addAll(mealImageView, mealTitle, randomMealBtn);

        fetchRandomMeal();
    }

    private void fetchRandomMeal() {
        try {
            Meal randomMeal = client.getRandomMeal();
            
            if (randomMeal != null) {
                updateUI(randomMeal);
            } else {
                mealTitle.setText("No meal found.");
            }
        } catch (Exception e) {
            mealTitle.setText("Error: " + e.getMessage());
        }
    }

    private void updateUI(Meal meal) {
        mealTitle.setText("Meal title: " + meal.getStrMeal());

        try {
            byte[] imageBytes = client.fetchMealImage(meal, ImageSize.LARGE);
            if (imageBytes != null && imageBytes.length > 0) {
                ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(stream);
                mealImageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + e.getMessage());
            mealImageView.setImage(null);
        }
    }
}