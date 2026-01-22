package gr.unipi.ddim.meallabapp.views;

import java.io.ByteArrayInputStream;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.ImageSize;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public final class MealCardView extends VBox {

	private final MealClient client;
	private final Meal meal;
	private final Navigation navigation;

	private final ImageView thumbView = new ImageView();
	private final Label titleLabel = new Label();
	private final Label idLabel = new Label();
	private final Button detailsBtn = new Button("See Details");

	public MealCardView(MealClient client, Meal meal, Navigation navigation) {
		this.client = client;
		this.meal = meal;
		this.navigation = navigation;

		setSpacing(8);
		setPadding(new Insets(10));
		setPrefWidth(190);
		setMaxWidth(190);
		setStyle("-fx-border-color: #D0D0D0; -fx-border-radius: 8; -fx-background-radius: 8;");

		thumbView.setFitWidth(190);
		thumbView.setFitHeight(170);
		thumbView.setPreserveRatio(true);
		thumbView.setSmooth(true);

		titleLabel.setText(safe(meal.getStrMeal()));
		titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		idLabel.setText("#" + safe(meal.getIdMeal()));
		idLabel.setStyle("-fx-text-fill: #666666;");

		detailsBtn.setOnAction(e -> this.navigation.showMealDetails(meal.getIdMeal()));

		HBox footer = new HBox(10, idLabel, spacer(), detailsBtn);
		footer.setAlignment(Pos.CENTER_LEFT);

		getChildren().addAll(thumbView, titleLabel, footer);

		loadThumbnail();
	}

	private void loadThumbnail() {
		try {
			byte[] imageBytes = client.fetchMealImage(meal, ImageSize.SMALL);
			if (imageBytes == null || imageBytes.length == 0) {
				thumbView.setImage(null);
				return;
			}
			thumbView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
		} catch (Exception e) {
			System.err.println("Could not load image: " + e.getMessage());
			thumbView.setImage(null);
		}
	}

	private static HBox spacer() {
		HBox spacer = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	private static String safe(String value) {
		return value == null ? "" : value;
	}
}
