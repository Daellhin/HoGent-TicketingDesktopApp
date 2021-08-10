package gui.general;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import domain.controllers.AanmeldController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AanmeldScherm extends BorderPane {

	private AanmeldController dc;

	@FXML
	private TextField txfGebruikersnaam;
	@FXML
	private PasswordField pwfWachtwoord;
	@FXML
	private Label lblErrorGebruikersnaam;
	@FXML
	private Label lblErrorWachtwoord;
	@FXML
	private Label lblErrorDomein;
	@FXML
	private Button btnLogin;

	public AanmeldScherm(AanmeldController dc) {
		this.dc = dc;

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

	public void setAsScene(Stage stage, Scene scene) {
		scene.setRoot(this);
		stage.setMinWidth(700);
		stage.setMinHeight(700);
	}

	private void aanmelden(String werknemersnaam, String wachtwoord) {
		try {
			dc.aanmelden(werknemersnaam, wachtwoord);
			Stage stage = (Stage) this.getScene().getWindow();
			GebruikerScherm gebruikersScherm = new GebruikerScherm(dc);
			gebruikersScherm.setAsScene(stage, this.getScene());
		} catch (IllegalArgumentException | EntityNotFoundException e) {
			lblErrorDomein.setText(e.getMessage());
		}
	}

	@FXML
	public void loginOnAction() {
		lblErrorGebruikersnaam.setText("");
		lblErrorWachtwoord.setText("");
		lblErrorDomein.setText("");

		if (txfGebruikersnaam.getText().isEmpty()) {
			lblErrorGebruikersnaam.setText("Username cannot be empty");
		}
		if (pwfWachtwoord.getText().isEmpty()) {
			lblErrorWachtwoord.setText("Password cannot be empty");
		}

		if (!(txfGebruikersnaam.getText().isEmpty() || pwfWachtwoord.getText().isEmpty())) {
			aanmelden(txfGebruikersnaam.getText(), pwfWachtwoord.getText());
		}
	}

	@FXML
	public void loginOnKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
		case ENTER:
			loginOnAction();
			e.consume();
			break;
		default:
			break;
		}
	}

}
