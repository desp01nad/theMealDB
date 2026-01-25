package gr.unipi.ddim.meallabapp.views;

import gr.unipi.ddim.meallabapi.api.MealClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/** Page that lists the meals marked as cooked. */
public class CookedMealView extends BorderPane {
	private final MealClient client;
	private final Navigation navigation;

	private final MealsGridView cookedView;

	public CookedMealView(MealClient client, Navigation navigation) {
		this.client = client;
		this.navigation = navigation;
		this.cookedView = new MealsGridView(meal -> new CookedMealCardView(this.client, meal, this.navigation));

		Label pageTitle = new Label("Cooked Meals");
		pageTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		HBox header = new HBox(12, pageTitle);
		header.setAlignment(Pos.CENTER_LEFT);
		header.setPadding(new Insets(10, 0, 20, 0));

		setMaxWidth(1100);
		setPadding(new Insets(20));
		setTop(header);
		setCenter(cookedView);
	}

	/** Reloads cooked meals from the manager and updates the grid. */
	public void refresh() {
		cookedView.setResultsWithTitle("", navigation.cooked().getAll());
	}

}
