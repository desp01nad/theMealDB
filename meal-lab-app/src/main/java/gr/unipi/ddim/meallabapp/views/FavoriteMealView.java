package gr.unipi.ddim.meallabapp.views;

import gr.unipi.ddim.meallabapi.api.MealClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/** Page that lists the user's favorite meals. */
public class FavoriteMealView extends BorderPane {
	private final MealClient client;
	private final Navigation navigation;

	private final MealsGridView favoritesView;

	public FavoriteMealView(MealClient client, Navigation navigation) {
		this.client = client;
		this.navigation = navigation;
		this.favoritesView = new MealsGridView(meal -> new FavoriteMealCardView(this.client, meal, this.navigation));

		Label pageTitle = new Label("Favorites");
		pageTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		HBox header = new HBox(12, pageTitle);
		header.setAlignment(Pos.CENTER_LEFT);
		header.setPadding(new Insets(10, 0, 20, 0));

		setMaxWidth(1100);
		setPadding(new Insets(20));
		setTop(header);
		setCenter(favoritesView);
	}

	/** Reloads favorites from the manager and updates the grid. */
	public void refresh() {
		favoritesView.setResultsWithTitle("", navigation.favorites().getAll());
	}

}
