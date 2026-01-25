package gr.unipi.ddim.meallabapp.views;

import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.scene.control.Button;

/** Meal card variant with a remove-from-cooked action. */
public class CookedMealCardView extends MealCardView {

	private final Button removeBtn = new Button("Remove");

	public CookedMealCardView(MealClient client, Meal meal, Navigation navigation) {
		super(client, meal, navigation);

		removeBtn.setOnAction(event -> {
			navigation.cooked().remove(meal.getIdMeal());

			String name = meal.getStrMeal();
			navigation.showNotification("✕ " + name + " removed from Cooked");

			navigation.showCooked();
		});

		buttonsRow.getChildren().add(0, removeBtn);
	}
}
