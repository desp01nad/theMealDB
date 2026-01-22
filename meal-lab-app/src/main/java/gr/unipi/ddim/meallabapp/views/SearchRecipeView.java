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

public final class SearchRecipeView extends BorderPane {

	private final MealClient client;
	private final Navigation navigation;

	private final RadioButton byNameBtn = new RadioButton("By name");
	private final RadioButton byIngredientBtn = new RadioButton("By ingredient");
	private final TextField queryField = new TextField();
	private final Button searchBtn = new Button("Search");

	private final MealsGridView resultsView;

	public SearchRecipeView(MealClient client, Navigation navigation) {
		this.client = client;
		this.navigation = navigation;
		this.resultsView = new MealsGridView(this.client, this.navigation);

		setMaxWidth(1100);
		setPadding(new Insets(20));
		setTop(createSearchBar());
		setCenter(resultsView);

		byNameBtn.setSelected(true);
		queryField.setPromptText("Search a recipe");

		searchBtn.setOnAction(e -> search());
	}

	private HBox createSearchBar() {
		ToggleGroup group = new ToggleGroup();
		byNameBtn.setToggleGroup(group);
		byIngredientBtn.setToggleGroup(group);

		HBox bar = new HBox(12, byNameBtn, byIngredientBtn, queryField, searchBtn);
		bar.setAlignment(Pos.CENTER_LEFT);
		bar.setPadding(new Insets(10, 0, 20, 0));

		queryField.setPrefColumnCount(4);
		queryField.setPrefWidth(300);


		return bar;
	}

	private void search() {
		String query = queryField.getText();
		if (query == null || query.isBlank()) {
			resultsView.clearResults();
			return;
		}

		List<Meal> meals;

		if (byNameBtn.isSelected()) {
			meals = client.searchMealsByName(query);
		} else {
			meals = client.filterMealsByIngredient(query);
		}

		resultsView.setResults(query, meals);
	}
}
