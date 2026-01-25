package gr.unipi.ddim.meallabapp.views;

import java.util.List;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** View that searches recipes by name or ingredient and displays results. */
public final class SearchRecipeView extends BorderPane {

	private final MealClient client;
	private final Navigation navigation;
	private static final Logger logger = LogManager.getLogger(SearchRecipeView.class);

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
			String required = byNameBtn.isSelected() ? "name" : "ingredient";
			navigation.showNotification("Please enter a " + required + ".");
			return;
		}

		searchBtn.setDisable(true);

		Task<List<Meal>> task = new Task<>() {
			@Override
			protected List<Meal> call() {
				logger.info("Search request started for query='{}' (byName={})", query, byNameBtn.isSelected());
				if (byNameBtn.isSelected()) {
					return client.searchMealsByName(query);
				} else {
					return client.filterMealsByIngredient(query);
				}
			}
		};

		task.setOnSucceeded(e -> {
			resultsView.setResults(query, task.getValue());
			searchBtn.setDisable(false);
			int count = task.getValue() == null ? 0 : task.getValue().size();
			logger.info("Search request completed for query='{}' (byName={}) with {} results", query,
					byNameBtn.isSelected(), count);
			if (count == 0) {
				navigation.showNotification("No results found.");
			}
		});

		task.setOnFailed(e -> {
			resultsView.clearResults();
			searchBtn.setDisable(false);

			Throwable error = task.getException();
			logger.error("Search failed for query='{}' (byName={})", query, byNameBtn.isSelected(), error);
			navigation.showNotification("✕ Search failed");
		});

		navigation.backgroundThread().execute(task);
	}
}
