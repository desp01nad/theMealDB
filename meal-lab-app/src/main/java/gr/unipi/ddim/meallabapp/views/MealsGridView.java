package gr.unipi.ddim.meallabapp.views;

import java.util.List;
import java.util.function.Function;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

/**
 * Grid container that renders meal cards with a title and scrollable layout.
 */
public final class MealsGridView extends BorderPane {

	private final Label titleLabel = new Label();
	private final TilePane grid = new TilePane();
	private final ScrollPane scroll = new ScrollPane();

	// a function that takes a Meal and returns a JavaFX Node.
	// in this case the returned Node will be a card (MealCardView,
	// FavoriteMealCardView or CookedMealCardView).
	private final Function<Meal, Node> cardCreator;

	// This constructor is used when you want the grid to behave like Search
	// results.
	// This constructor creates a grid where each meal becomes a MealCardView
	// This constructor delegates all setup to the other constructor
	public MealsGridView(MealClient client, Navigation navigation) {
		this(meal -> new MealCardView(client, meal, navigation));
	}

	// Main constructor (used by favorites, cooked, etc)
	public MealsGridView(Function<Meal, Node> cardCreator) {
		this.cardCreator = cardCreator;

		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPrefColumns(4);
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setPadding(new Insets(10));

		scroll.setFitToWidth(true);
		scroll.setContent(grid);

		setMaxWidth(1100);
		setPadding(new Insets(10, 0, 0, 0));
		setTop(titleLabel);
		setCenter(scroll);

		clearResults();
	}

	/** Clears the grid and resets the title to the default state. */
	public void clearResults() {
		titleLabel.setText("Results");
		grid.getChildren().clear();
	}

	/**
	 * Displays a list of meals in the grid.
	 *
	 * - setResults(...) preserves the original Search behavior by automatically
	 * prefixing the title with "Results for:". - setResultsWithTitle(...) allows
	 * other views (e.g. Favorites, Cooked) to provide a custom title.
	 *
	 * Both methods delegate the actual rendering logic to setResultsInternal(...)
	 * to avoid code duplication and keep the API backward-compatible.
	 */
	public void setResults(String queryText, List<Meal> meals) {
		setResultsInternal("Results for: " + safe(queryText), meals);
	}

	/** Displays a list of meals using the provided title. */
	public void setResultsWithTitle(String title, List<Meal> meals) {
		setResultsInternal(title == null ? "" : title, meals);
	}

	private void setResultsInternal(String title, List<Meal> meals) {
		titleLabel.setText(title);

		grid.getChildren().clear();
		if (meals == null || meals.isEmpty()) {
			return;
		}

		for (Meal meal : meals) {
			grid.getChildren().add(cardCreator.apply(meal));
		}
	}

	private static String safe(String value) {
		return value == null ? "" : value;
	}
}
