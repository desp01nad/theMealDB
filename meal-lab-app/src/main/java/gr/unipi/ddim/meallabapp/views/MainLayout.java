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
		Button favoritesBtn = new Button("Favorites");
		Button cookedBtn = new Button("Cooked");
		Button homeBtn = new Button("Home");
		ToolBar navBar = new ToolBar(homeBtn, searchBtn, randomBtn, favoritesBtn, cookedBtn);
		this.setTop(navBar);
		
		setCenter(new HomeView());
		
		homeBtn.setOnAction(event -> this.setCenter(new HomeView()));
		
		randomBtn.setOnAction(event -> this.setCenter(new RandomMealView(client)));
		
		
		
	}
}
