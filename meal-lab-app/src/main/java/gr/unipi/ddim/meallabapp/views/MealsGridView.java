package gr.unipi.ddim.meallabapp.views;

import java.util.List;
import java.util.function.Consumer;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;



public class MealsGridView extends BorderPane {
	
	private final MealClient client; 
	
	private final Label titleLabel; 
	private final ScrollPane scroll;
	private final TilePane grid;
	
	public MealsGridView(MealClient client) {
		
		this.client = client;
		this.setPadding(new Insets(15));
		
		// Top components
		titleLabel = new Label();
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		
        setTop(titleLabel);
		
        
        // Grid
        grid = new TilePane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPrefColumns(4);
        grid.setAlignment(Pos.TOP_LEFT);
        
        
        // Scroll
        scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        
        setCenter(scroll);
        		
	}
	
	public void setResults(String queryText, List<Meal> meals, Consumer<String> onDetails) {
        titleLabel.setText("Results for: " + queryText);

        grid.getChildren().clear();
        for (Meal meal : meals) {
            grid.getChildren().add(new MealCardView(client, meal, onDetails));
        }
	}
	

}
