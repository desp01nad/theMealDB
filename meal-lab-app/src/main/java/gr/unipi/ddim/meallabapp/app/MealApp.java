package gr.unipi.ddim.meallabapp.app;

import gr.unipi.ddim.meallabapp.views.Navigation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** JavaFX entry point for the Meal Lab desktop app. */
public class MealApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Navigation nav = new Navigation();

		Scene scene = new Scene(nav, 1200, 800);
		primaryStage.setTitle("Meal Lab App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/** Launches the JavaFX application. */
	public static void main(String[] args) {
		Application.launch(MealApp.class, args);
	}

}
