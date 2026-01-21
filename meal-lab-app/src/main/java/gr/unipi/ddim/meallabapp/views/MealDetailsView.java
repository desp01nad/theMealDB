package gr.unipi.ddim.meallabapp.views;

import java.io.ByteArrayInputStream;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.ImageSize;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MealDetailsView extends BorderPane {
	
	protected final MealClient client;
	
	// Header components
	protected final ImageView mealImageView;
	protected final Label mealTitleLabel;
	protected final Label mealCategoryLabel;
	protected final Label mealOriginLabel;
	
	protected final Button favoriteBtn;
	protected final Button cookedBtn;
	
	protected final HBox buttonsRow;
	
	// Bottom components
	private final ListView<String> ingredientsList;
	private final TextArea instructionsArea;
	
		
	public MealDetailsView(MealClient client) {
		this.client = client;
		this.setPadding(new Insets(15));	
		
	// Header
		mealImageView = new ImageView();
		mealImageView.setFitWidth(260);
		mealImageView.setPreserveRatio(true);
		
		mealTitleLabel = new Label("");
		mealTitleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
		mealCategoryLabel = new Label("");
		mealOriginLabel = new Label("");
		
		favoriteBtn = new Button("Add to Favorites");
		cookedBtn = new Button("Add to Cooked");		
		favoriteBtn.setFocusTraversable(false);
		cookedBtn.setFocusTraversable(false);
				
		
		buttonsRow = new HBox(10, favoriteBtn, cookedBtn);
		buttonsRow.setAlignment(Pos.CENTER_LEFT);
		
		VBox rightColumn = new VBox(8, mealTitleLabel, mealCategoryLabel, mealOriginLabel, buttonsRow);
		rightColumn.setAlignment(Pos.TOP_LEFT);
        rightColumn.setFillWidth(true);
		
        HBox header = new HBox(20, mealImageView, rightColumn);
        header.setAlignment(Pos.TOP_LEFT);
        header.setPadding(new Insets(10, 0, 10, 0));

        this.setTop(header);
        
    // Bottom Area
        // Ingredients Box
        Label ingredients = new Label("Ingredients");
        ingredientsList = new ListView<>();
        VBox ingredientsListBox = new VBox(8, ingredients, ingredientsList);
        VBox.setVgrow(ingredientsList, Priority.ALWAYS);
        ingredientsListBox.setPadding(new Insets(10, 0, 0, 10));
        
        // Instructions Box
        Label instructions = new Label("Instructions");
        instructionsArea = new TextArea();
        instructionsArea.setWrapText(true);
        instructionsArea.setEditable(false);
        
        VBox instructionsBox = new VBox(8, instructions, instructionsArea);
        VBox.setVgrow(instructionsArea, Priority.ALWAYS);
        instructionsBox.setPadding(new Insets(10, 0, 0, 10));
        
        SplitPane splitPane = new SplitPane(ingredientsListBox, instructionsBox);
        splitPane.setDividerPositions(0.35);
        
        this.setCenter(splitPane);
	}  
        
     // Fill the UI with data
        public void showMeal(Meal meal) {
        	
        	if (meal == null ) {
        		mealImageView.setImage(null);
        		mealTitleLabel.setText("No meal found.");
        		mealCategoryLabel.setText("");
        		mealOriginLabel.setText("");
        		ingredientsList.getItems().clear();
        		instructionsArea.clear();
        		return;	
        	}
        	
    		mealTitleLabel.setText(safe(meal.getStrMeal()));
    		mealCategoryLabel.setText("Category " + (safe(meal.getStrCategory())));
    		mealOriginLabel.setText("Origin: " + (safe(meal.getStrArea())));
    		ingredientsList.getItems().setAll(meal.getIngredientsList());
    		instructionsArea.setText(safe(meal.getStrInstructions()));

    		
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
        
        // Helper method - if API gives me null, convert it into an empty string so the UI stays stable
        private String safe(String value) {
            return value == null ? "" : value;
        }

        
        
}
