package gr.unipi.ddim.meallabapp.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/** Landing page view with the app's welcome copy. */
public class HomeView extends VBox {

	public HomeView() {
		setMaxWidth(1100);
		setPadding(new Insets(20, 40, 200, 40));
		setAlignment(Pos.CENTER);

		// Top text group
		VBox headerBox = new VBox(12);
		headerBox.setAlignment(Pos.CENTER);

		Label upertitle = new Label("Welcome to");
		upertitle.setStyle("-fx-font-size: 22px;");
		upertitle.setAlignment(Pos.CENTER);

		Label title = new Label("Meal Lab");
		title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #B53324;");
		title.setAlignment(Pos.CENTER);

		Label subtitle = new Label("What’s cooking?");
		subtitle.setStyle("-fx-font-size: 22px;");
		subtitle.setAlignment(Pos.CENTER);

		headerBox.getChildren().addAll(upertitle, title, subtitle);

		// Bottom text
		Label description = new Label("Search our recipe database using the navigation bar above.\n\n"
				+ "Discover new recipes, get a random meal,\n"
				+ "save your favorites, and track the recipes you’ve cooked.");
		description.setStyle("-fx-font-size: 16px;");
		description.setWrapText(true);
		description.setMaxWidth(500);
		description.setAlignment(Pos.CENTER);
		description.setTextAlignment(TextAlignment.CENTER);

		// Main Layout
		setSpacing(40);
		getChildren().addAll(headerBox, description);

	}
}
