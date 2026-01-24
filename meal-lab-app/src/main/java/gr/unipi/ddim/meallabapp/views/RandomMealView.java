package gr.unipi.ddim.meallabapp.views;

import java.util.Objects;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.concurrent.Task;
import javafx.scene.control.Button;

public final class RandomMealView extends MealDetailsView {

	private final Button newRandomMealBtn = new Button("⟳ New Random Recipe");
	private String lastMealId;

	public RandomMealView(MealClient client, Navigation navigation) {
		super(client, navigation);

		newRandomMealBtn.setFocusTraversable(false);
		newRandomMealBtn.setOnAction(e -> fetchAndShowNewRandomMeal());

		getHeaderActions().getChildren().add(0, newRandomMealBtn);

		fetchAndShowNewRandomMeal();
	}

	private void fetchAndShowNewRandomMeal() {
		newRandomMealBtn.setDisable(true);
		showMeal(null);

		Task<Meal> task = new Task<>() {
			@Override
			protected Meal call() {
				Meal meal;
				do {
					meal = client().getRandomMeal();
				} while (sameAsLast(meal));
				return meal;
			}
		};

		task.setOnSucceeded(e -> {
			Meal meal = task.getValue();
			if (meal == null) {
				showMeal(null);
				newRandomMealBtn.setDisable(false);
				return;
			}

			showMeal(meal);
			lastMealId = meal.getIdMeal();
			newRandomMealBtn.setDisable(false);
		});

		task.setOnFailed(e -> {
			showMeal(null);
			newRandomMealBtn.setDisable(false);
		});

		this.getNavitation().backgroundThread().execute(task);
	}

	private boolean sameAsLast(Meal meal) {
		if (meal == null) {
			return false;
		}

		String id = meal.getIdMeal();
		if (id == null || id.isBlank()) {
			return false;
		}

		return Objects.equals(id, lastMealId);
	}
}
