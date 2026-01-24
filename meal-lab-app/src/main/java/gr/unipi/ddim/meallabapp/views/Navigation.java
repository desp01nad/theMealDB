package gr.unipi.ddim.meallabapp.views;

import java.util.ArrayDeque;
import java.util.Deque;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import gr.unipi.ddim.meallabapp.managers.CookedManager;
import gr.unipi.ddim.meallabapp.managers.FavoritesManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public final class Navigation extends BorderPane {

	private final MealClient client;
	private final Deque<Node> backStack = new ArrayDeque<>();

	private final HomeView homeView;
	private final SearchRecipeView searchRecipeView;
	private final RandomMealView randomMealView;
	private final FavoriteMealView favoriteView;
	private final CookedMealView cookedView;

	private final FavoritesManager favoritesManager = new FavoritesManager();
	private final CookedManager cookedManager = new CookedManager();

	private final Label notificationLabel = new Label();
	private final HBox notificationBox = new HBox();
	private PauseTransition notificationTimer;

	public Navigation() {
		this(new MealClient());
	}

	public Navigation(MealClient client) {
		this.client = client;
		homeView = new HomeView();
		searchRecipeView = new SearchRecipeView(client, this);
		randomMealView = new RandomMealView(client, this);
		favoriteView = new FavoriteMealView(client, this);
		cookedView = new CookedMealView(client, this);

		setTop(createToolbar());

		showHome();

		notificationLabel.setStyle(
				"-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 6 14; -fx-background-radius: 8; -fx-font-size: 13px;");

		notificationBox.getChildren().add(notificationLabel);
		notificationBox.setAlignment(Pos.CENTER);
		notificationBox.setPadding(Insets.EMPTY);
		notificationBox.setMinHeight(40);
		notificationBox.setPrefHeight(40);
		notificationBox.setMaxHeight(40);
		notificationBox.setVisible(false);
		notificationBox.setManaged(true);
		setBottom(notificationBox);
		BorderPane.setMargin(notificationBox, Insets.EMPTY);

	}

	public MealClient client() {
		return client;
	}

	public void showHome() {
		replaceCenter(homeView, false);
	}

	public void showSearch() {
		replaceCenter(searchRecipeView, true);
	}

	public void showRandom() {
		randomMealView.updateFavoriteCookedButtons();
		replaceCenter(randomMealView, true);
	}

	public FavoritesManager favorites() {
		return favoritesManager;
	}

	public CookedManager cooked() {
		return cookedManager;
	}

	public void showMealDetails(String mealId) {
		if (mealId == null || mealId.isBlank()) {
			return;
		}

		Meal meal = client.getMealById(mealId);

		MealDetailsView details = new MealDetailsView(client, this);
		details.showMeal(meal);

		replaceCenter(details, true);
	}

	public void showFavorites() {
		favoriteView.refresh();
		replaceCenter(favoriteView, true);
	}

	public void showCooked() {
		cookedView.refresh();
		replaceCenter(cookedView, true);
	}

	public void showNotification(String text) {
		notificationLabel.setText(text);

		notificationBox.setVisible(true);

		if (notificationTimer != null)
			notificationTimer.stop();
		notificationTimer = new PauseTransition(Duration.seconds(2));
		notificationTimer.setOnFinished(e -> {
			notificationBox.setVisible(false);
		});
		notificationTimer.playFromStart();
	}

	public void back() {
		if (backStack.isEmpty()) {
			showHome();
			return;
		}

		Node prevPage = backStack.pop();
		if (prevPage instanceof MealDetailsView) {
			((MealDetailsView) prevPage).updateFavoriteCookedButtons();
		} else if (prevPage instanceof FavoriteMealView) {
			((FavoriteMealView) prevPage).refresh();
		} else if (prevPage instanceof CookedMealView) {
			((CookedMealView) prevPage).refresh();
		}

		replaceCenter(prevPage, false);
	}

	private ToolBar createToolbar() {
		Button homeBtn = new Button("Home");
		Button searchBtn = new Button("Search recipes");
		Button randomBtn = new Button("Get a random recipe");
		Button favoritesBtn = new Button("Favorites");
		Button cookedBtn = new Button("Cooked");

		homeBtn.setOnAction(e -> showHome());
		searchBtn.setOnAction(e -> showSearch());
		randomBtn.setOnAction(e -> showRandom());
		favoritesBtn.setOnAction(e -> showFavorites());
		cookedBtn.setOnAction(e -> showCooked());

		return new ToolBar(homeBtn, searchBtn, randomBtn, favoritesBtn, cookedBtn);
	}

	private void replaceCenter(Node next, boolean pushCurrent) {
		Node current = getCenter();

		if (pushCurrent && current != null && current != next) {
			backStack.push(current);
		}

		setCenter(next);
	}

}
