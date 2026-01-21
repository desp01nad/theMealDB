package gr.unipi.ddim.meallabapp.views;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.ImageSize;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

//MealCardView is a reusable UI component that displays a single meal and 
//reports user actions (via Consumer<String>) without knowing application logic.

public class MealCardView extends VBox {
	private final ImageView thumbView;
	private final Label mealTitleLabel;
	private final Label mealIdLabel;
	

	public MealCardView(MealClient client, Meal meal, Consumer<String> onDetails) {
		
		thumbView = new ImageView();
		
		// Card Styling
		setSpacing(8);
	    setPadding(new Insets(10));
	    setPrefWidth(190);
	    setMaxWidth(190);
	    
	    setStyle("-fx-border-color: #cfcfcf; -fx-border-radius: 8; -fx-background-radius: 8;");

	    
	    // thumbView Styling
	    thumbView.setFitHeight(170);
	    thumbView.setFitWidth(130);
	    thumbView.setPreserveRatio(true);
	    
	    
		// Meal Title
	    mealTitleLabel = new Label(meal.getStrMeal());
	    mealTitleLabel.setWrapText(true);
	    mealTitleLabel.setStyle("-fx-font-weight: bold;");
	    
	    // Meal Id
	    mealIdLabel = new Label();
	    if (meal.getIdMeal() != null) {
	    	mealIdLabel.setText("#" + meal.getIdMeal());
        }
	    
	    // Details Button
	    Button detailsBtn = new Button("See Details");
	    detailsBtn.setOnAction(event -> onDetails.accept(meal.getIdMeal()));
	    
	    
	    // Footer layout
	    Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox footer = new HBox(8, mealIdLabel, spacer, detailsBtn);
        footer.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(thumbView, mealTitleLabel, footer);

        loadImage(client, meal);
	}
	
	private void loadImage(MealClient client, Meal meal) {
		try {
            byte[] imageBytes = client.fetchMealImage(meal, ImageSize.SMALL);
            if (imageBytes != null && imageBytes.length > 0) {
                ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(stream);
                thumbView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + e.getMessage());
            thumbView.setImage(null);
        }
		
	}
}
