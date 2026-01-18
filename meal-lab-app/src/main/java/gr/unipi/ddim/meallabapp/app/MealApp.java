package gr.unipi.ddim.meallabapp.app;

import gr.unipi.ddim.meallabapp.views.MainLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MealApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainLayout mainLayout = new MainLayout();

		Scene scene = new Scene(mainLayout, 800, 600);
		primaryStage.setTitle("Meal Lab App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(MealApp.class, args);
	}

}
