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

    private final MealClient client;
    private final Navigation navigation;

    private final Label mealTitleLabel = new Label();
    private final Label mealCategoryLabel = new Label();
    private final Label mealOriginLabel = new Label();

    private final ImageView mealImageView = new ImageView();
    private final ListView<String> ingredientsList = new ListView<>();
    private final TextArea instructionsArea = new TextArea();
    
    private final HBox headerActions = new HBox(8);

    private Meal currentMeal;

    public MealDetailsView(MealClient client, Navigation navigation) {
        this.client = client;
        this.navigation = navigation;

        setMaxWidth(1100);
        setPadding(new Insets(20));
        setTop(createHeader());
        setCenter(createContent());

        instructionsArea.setWrapText(true);
        instructionsArea.setEditable(false);

        mealImageView.setFitWidth(340);
        mealImageView.setPreserveRatio(true);
        mealImageView.setSmooth(true);
    }
    
    protected final MealClient client() {
        return client;
    }


    public void showMeal(Meal meal) {
        this.currentMeal = meal;

        if (meal == null) {
            clear();
            return;
        }

        mealTitleLabel.setText(safe(this.currentMeal.getStrMeal()));
        mealCategoryLabel.setText("Category: " + safe(this.currentMeal.getStrCategory()));
        mealOriginLabel.setText("Origin: " + safe(this.currentMeal.getStrArea()));

        ingredientsList.getItems().setAll(this.currentMeal.getIngredientsList());
        instructionsArea.setText(safe(this.currentMeal.getStrInstructions()));

        loadImage(this.currentMeal);
        
     // update favorites button label based on current meal state
        for (var node : headerActions.getChildren()) {
            if (node instanceof Button b && b.getText().contains("Favorites")) {
                boolean fav = navigation.favorites().isFavorite(meal.getIdMeal());
                b.setText(fav ? "❤︎ Remove from Favorites" : "❤︎ Add to Favorites");
            }
        }

    }
    

    protected final HBox getHeaderActions() {
        return headerActions;
    }


    private HBox createHeader() {
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> navigation.back());
        
        Button favoritesBtn = new Button("❤︎ Add to Favorites");
        Button cookedBtn = new Button("✔ Add To Cooked");
        
        favoritesBtn.setFocusTraversable(false);
        favoritesBtn.setOnAction(e -> {
            if (currentMeal == null) return;
            
            boolean nowFav = navigation.favorites().toggle(currentMeal);
            favoritesBtn.setText(nowFav ? "❤︎ Remove from Favorites" : "❤︎ Add to Favorites");
        });
        
        headerActions.getChildren().addAll(favoritesBtn, cookedBtn, backBtn);
        headerActions.setPadding(new Insets(35, 0, 0, 0));

        mealTitleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        mealCategoryLabel.setStyle("-fx-text-fill: #444444;");
        mealOriginLabel.setStyle("-fx-text-fill: #444444;");

        VBox meta = new VBox(8, mealTitleLabel, mealCategoryLabel, mealOriginLabel, headerActions);
        meta.setAlignment(Pos.TOP_LEFT);
        meta.setFillWidth(true);


        HBox header = new HBox(20, mealImageView, meta);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 10, 0));

        return header;
        
    }
    
    

    private SplitPane createContent() {

        Label ingredientsLabel = new Label("Ingredients");
        ingredientsLabel.setPadding(new Insets(0, 0, 2, 10)); 

        VBox left = new VBox(2, ingredientsLabel, ingredientsList);
        VBox.setVgrow(ingredientsList, Priority.ALWAYS);
        left.setPadding(new Insets(5, 0, 5, 0));
        left.setPrefWidth(460);

        Label instructionsLabel = new Label("Instructions");
        instructionsLabel.setPadding(new Insets(0, 0, 2, 10));
        
        VBox right = new VBox(2, instructionsLabel, instructionsArea);
        VBox.setVgrow(instructionsArea, Priority.ALWAYS);
        right.setPadding(new Insets(5, 0, 5, 0));

        SplitPane split = new SplitPane(left, right);
        left.setPrefWidth(320);         
        split.setDividerPositions(0.3); 


        return split;
    }


    private void loadImage(Meal meal) {
        try {
            byte[] imageBytes = client.fetchMealImage(meal, ImageSize.MEDIUM);
            if (imageBytes == null || imageBytes.length == 0) {
                mealImageView.setImage(null);
                return;
            }
            mealImageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
        } catch (Exception e) {
            System.err.println("Could not load image: " + e.getMessage());
            mealImageView.setImage(null);
        }
    }

    private void clear() {
        mealTitleLabel.setText("");
        mealCategoryLabel.setText("");
        mealOriginLabel.setText("");
        ingredientsList.getItems().clear();
        instructionsArea.clear();
        mealImageView.setImage(null);
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
