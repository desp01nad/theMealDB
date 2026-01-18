package gr.unipi.ddim.meallabapp.views;
import gr.unipi.ddim.meallabapi.api.MealClient;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class MainLayout extends BorderPane{
	
	private final MealClient client;
	
	public MainLayout() {
		this.client = new MealClient();
		
		Button searchBtn = new Button("Search recipes");
		Button randomBtn = new Button("Get a random recipe");
		Button favoritesBtn = new Button("My recipes");
		ToolBar navBar = new ToolBar(searchBtn, randomBtn, favoritesBtn);
		this.setTop(navBar);
		
		randomBtn.setOnAction(event -> this.setCenter(new RandomMealView(client)));
	}
}
