package gr.unipi.ddim.meallabapp.views;

import java.util.List;
import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

public final class MealsGridView extends BorderPane {

    private final MealClient client;
    private final Navigation navigation;

    private final Label titleLabel = new Label();
    private final TilePane grid = new TilePane();
    private final ScrollPane scroll = new ScrollPane();

	public MealsGridView(MealClient client, Navigation navigation) {
        this.client = client;
        this.navigation = navigation;

        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

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

    public void clearResults() {
        titleLabel.setText("Results");
        grid.getChildren().clear();
    }

    public void setResults(String queryText, List<Meal> meals) {
        titleLabel.setText("Results for: " + safe(queryText));

        grid.getChildren().clear();
        if (meals == null || meals.isEmpty()) {
            return;
        }

        for (Meal meal : meals) {
            grid.getChildren().add(new MealCardView(client, meal, navigation));
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
