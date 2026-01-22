package gr.unipi.ddim.meallabapp.views;

import java.util.ArrayDeque;
import java.util.Deque;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public final class Navigation extends BorderPane {

	private final MealClient client;
	private final Deque<Node> backStack = new ArrayDeque<>();

	private final HomeView homeView;
	private final SearchRecipeView searchRecipeView;
	private final RandomMealView randomMealView;

	public Navigation() {
		this(new MealClient());
	}

	public Navigation(MealClient client) {
		this.client = client;
		homeView = new HomeView();
		searchRecipeView = new SearchRecipeView(client, this);
		randomMealView = new RandomMealView(client, this);

		setTop(createToolbar());

		showHome();

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
		replaceCenter(randomMealView, true);
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

	public void back() {
		if (backStack.isEmpty()) {
			showHome();
			return;
		}
		replaceCenter(backStack.pop(), false);
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
