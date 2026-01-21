package gr.unipi.ddim.meallabapp.views;

import java.util.List;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

public class SearchRecipeView extends BorderPane {
	private final MealClient client;
	private final MealsGridView resultsView;
	private final Consumer<String> onDetails;

	private final RadioButton byNameBtn;
	private final RadioButton byIngredientBtn;
	private final TextField searchField;
	private final Button searchBtn;

	public SearchRecipeView(MealClient client, Consumer<String> onDetails) {
		this.client = client;
		this.onDetails = onDetails;
		setPadding(new Insets(15));

		// Search mode selection
		byNameBtn = new RadioButton("By name");
		byIngredientBtn = new RadioButton("By ingredient");

		ToggleGroup modeGroup = new ToggleGroup();
		byNameBtn.setToggleGroup(modeGroup);
		byIngredientBtn.setToggleGroup(modeGroup);
		byNameBtn.setSelected(true);

		// Search field
		searchField = new TextField();
		searchField.setPromptText("Search recipe");

		// Search Button
		searchBtn = new Button("Search");

		// Search Bar
		HBox searchBar = new HBox(10, byNameBtn, byIngredientBtn, searchField, searchBtn);
		searchBar.setAlignment(Pos.CENTER_LEFT);
		searchBar.setPadding(new Insets(10));
		setTop(searchBar);

		// Search Results
		resultsView = new MealsGridView(client);
		setCenter(resultsView);

		searchBtn.setOnAction(event -> doSearch());

	}

	// Helper method
	private void doSearch() {
		String query = searchField.getText();

		if (query == null || query.isBlank()) {
			return;
		}

		List<Meal> meals;

		if (byNameBtn.isSelected()) {
			meals = client.searchMealsByName(query);
		} else {
			meals = client.filterMealsByIngredient(query);
		}

		resultsView.setResults(query, meals, onDetails);
	}

}
