package gui.general;

import java.io.IOException;
import java.util.Set;

import domain.Werknemer;
import domain.controllers.AanmeldController;
import domain.controllers.BeheerTicketsController;
import domain.enumerations.ApplicatieFunctie;
import gui.Opslaanbaar;
import gui.PaneelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GebruikerScherm extends HBox {

	private AanmeldController ac;
	private BeheerTicketsController btc;
	private DynamischeZijbalk dz;
	private Hyperlink selectedHyperlink;

	public GebruikerScherm(AanmeldController dc) {
		this.ac = dc;
		this.btc = new BeheerTicketsController(ac.getAangemeldeWerknemer());
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		buildGui();
	}

	public void setAsScene(Stage stage, Scene scene) {
		scene.setRoot(this);
		stage.setMinWidth(1280);
		stage.setMinHeight(830);
	}

	private void buildGui() {
		dz = new DynamischeZijbalk(this);
		this.getChildren().add(dz);
		this.getChildren().add(new DashboardGebruiker(this));
	}

	public void afmelden() {
		ac.afmelden();
		Stage stage = (Stage) this.getScene().getWindow();
		AanmeldScherm aanmeldScherm = new AanmeldScherm(ac);
		aanmeldScherm.setAsScene(stage, this.getScene());
		stage.show();
	}

	public Set<ApplicatieFunctie> getFuncties() {
		return ac.getAangemeldeWerknemer().getWerknemersType().getFuncties();
	}

	public String getGebruikersnaam() {
		return ac.getAangemeldeWerknemer().getGebruikersnaam();
	}

	public Werknemer getAangemeldeWerknemer() {
		return ac.getAangemeldeWerknemer();
	}

	public void toonPaneel(ApplicatieFunctie permission, Hyperlink link) {
		Pane pane = (Pane) this.getChildren().get(1);
		if (pane instanceof Opslaanbaar) {
			if (!((Opslaanbaar) pane).veranderScherm()) {
				return;
			}
		}

		// persistente zijbalk style
		selectedHyperlink.setStyle("");
		selectedHyperlink = link;
		selectedHyperlink.setStyle("-fx-border-style: solid;\r\n" + "-fx-border-color: dodgerblue;\r\n"
				+ "-fx-background-color: #d3d3d3;");

		Pane newPane = PaneelFactory.create(permission, this);
		this.getChildren().remove(1);
		this.getChildren().add(newPane);
	}

	public void setSelectedHyperlinkPaneel(Hyperlink link) {
		selectedHyperlink = link;
		selectedHyperlink.setStyle("-fx-border-style: solid;\r\n" + "-fx-border-color: dodgerblue;\r\n"
				+ "-fx-background-color: #d3d3d3;");
	}

	public int geefAantalGewijzigdeTickets() {
		return btc.getGewijzigdeTicketten().size();
	}

	public void updateAantalGewijzigdeTickets() {
		dz.updateAantalGewijzigdeTicketten();
	}

}
