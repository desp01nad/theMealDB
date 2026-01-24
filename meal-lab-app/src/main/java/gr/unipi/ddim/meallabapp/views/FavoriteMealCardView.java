package gr.unipi.ddim.meallabapp.views;



import gr.unipi.ddim.meallabapi.api.MealClient;
import gr.unipi.ddim.meallabapi.models.Meal;
import javafx.scene.control.Button;

public class FavoriteMealCardView extends MealCardView {
	
	private final Button removeBtn = new Button("Remove");
		
	public FavoriteMealCardView(MealClient client, Meal meal, Navigation navigation) {
		super(client, meal, navigation);
		
		 
		removeBtn.setOnAction(event -> {
			navigation.favorites().remove(meal.getIdMeal());
		    navigation.showFavorites(); 
        });
		
		buttonsRow.getChildren().add(0, removeBtn);

	
	}

}
