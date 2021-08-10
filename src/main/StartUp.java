package main;

import domain.controllers.AanmeldController;
import gui.general.AanmeldScherm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.DataInitializer;

public class StartUp extends Application {

	public static void main(String[] args) {
		new DataInitializer(true).run();

		launch(args);
	}

	@Override
	public void start(Stage stage) {
		AanmeldController dc = new AanmeldController();
		AanmeldScherm aanmeldScherm = new AanmeldScherm(dc);
		Scene scene = new Scene(aanmeldScherm);

		stage.setResizable(true);
		stage.setTitle("Ticketing Systeem");
		stage.setScene(scene);
		stage.setWidth(1280);
		stage.setHeight(830);
		stage.setMinHeight(830);

		aanmeldScherm.setAsScene(stage, scene);

		stage.show();
	}
}