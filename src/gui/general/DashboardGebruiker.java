package gui.general;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DashboardGebruiker extends AnchorPane {
	@FXML
	private ImageView imvLogo;
	// private GebruikerScherm scherm;

	public DashboardGebruiker(GebruikerScherm scherm) {
		// this.scherm = scherm;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}
	}
}
