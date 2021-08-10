package gui.general.statistiek;

import java.io.IOException;
import java.util.List;

import domain.controllers.StatistiekController;
import domain.statistieken.Statistiek;
import gui.general.GebruikerScherm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class StatistiekenBekijken extends AnchorPane {
	@FXML
	private VBox vbox;

	private GebruikerScherm scherm;
	private StatistiekController dc;
	private List<Statistiek<?>> statistieken;

	public StatistiekenBekijken(GebruikerScherm scherm) {
		this.scherm = scherm;
		this.dc = new StatistiekController();

		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		addGrowHorizontal();
		loadStatistieken();
		buildGui();
	}

	private void loadStatistieken() {
		statistieken = dc.getTrackedStatistieken();
	}

	private void buildGui() {
		statistieken.forEach(e -> {
			vbox.getChildren().add(new StatistiekPaneel(this, e));
		});
	}

	private void addGrowHorizontal() {
		this.setPrefWidth(scherm.getWidth() - 300);
		scherm.widthProperty().addListener(event -> {
			this.setPrefWidth(scherm.getWidth() - 300);
		});
	}

}
