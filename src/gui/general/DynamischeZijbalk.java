package gui.general;

import static domain.enumerations.ApplicatieFunctie.Dashboard;
import static domain.enumerations.ApplicatieFunctie.FinishedTickets;
import static domain.enumerations.ApplicatieFunctie.KPI;
import static domain.enumerations.ApplicatieFunctie.KnowledgeBase;
import static domain.enumerations.ApplicatieFunctie.ManageContractTypes;
import static domain.enumerations.ApplicatieFunctie.ManageCustomers;
import static domain.enumerations.ApplicatieFunctie.ManageEmployees;
import static domain.enumerations.ApplicatieFunctie.NewTicket;
import static domain.enumerations.ApplicatieFunctie.Statistics;
import static domain.enumerations.ApplicatieFunctie.TicketsInProgress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.enumerations.ApplicatieFunctie;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.Util;

public class DynamischeZijbalk extends AnchorPane {
	@FXML
	private Hyperlink hypDashboard;
	@FXML
	private Hyperlink hypManageCustomers;
	@FXML
	private Hyperlink hypManageEmployees;
	@FXML
	private Hyperlink hypManageContracts;
	@FXML
	private Hyperlink hypNewTicket;
	@FXML
	private Hyperlink hypTicketsInProgess;
	@FXML
	private Hyperlink hypFinishedTickets;
	@FXML
	private Hyperlink hypKnowledgeBase;
	@FXML
	private Hyperlink hypStatistics;
	@FXML
	private Label lblGebruikersnaam;
	@FXML
	private Hyperlink hypKPI;

	@FXML
	private Label lblAantalTicketsInProgress;
	@FXML
	private AnchorPane anchorTicketsInProgess;

	private Map<ApplicatieFunctie, Hyperlink> links;
	private GebruikerScherm scherm;

	public DynamischeZijbalk(GebruikerScherm scherm) {
		this.scherm = scherm;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			System.out.println("Probleem met tonen " + this.getClass().getSimpleName());
			ex.printStackTrace();
		}

		// anchorpane laten werken
		anchorTicketsInProgess.managedProperty().bind(anchorTicketsInProgess.visibleProperty());
		anchorTicketsInProgess.visibleProperty().bind(hypTicketsInProgess.visibleProperty());

		createHyperlinkMap();
		bindManagedToVisible();
		buildGui();
		scherm.setSelectedHyperlinkPaneel(hypDashboard);
	}

	private void buildGui() {
		changeVisibilityLinks(ApplicatieFunctie.valueSet(), false);
		changeVisibilityLinks(scherm.getFuncties(), true);
		lblGebruikersnaam.setText(scherm.getGebruikersnaam());
		updateAantalGewijzigdeTicketten();
	}

	@FXML
	public void hypChangePanelOnAction(Event event) {
		Hyperlink selectedHyperlink = (Hyperlink) event.getSource();
		scherm.toonPaneel(Util.firstKeyOfValue(links, selectedHyperlink), selectedHyperlink);
	}

	@FXML
	public void hypAfmeldenOnAction(ActionEvent event) {
		scherm.afmelden();
	}

	// -- Hulpmethodes
	// zorgen dat de hyperlinks ook verschuiven als ze verborgen worden
	private void createHyperlinkMap() {
		links = new HashMap<>();
		links.put(Dashboard, hypDashboard);
		links.put(ManageCustomers, hypManageCustomers);
		links.put(ManageEmployees, hypManageEmployees);
		links.put(ManageContractTypes, hypManageContracts);
		links.put(NewTicket, hypNewTicket);
		links.put(TicketsInProgress, hypTicketsInProgess);
		links.put(FinishedTickets, hypFinishedTickets);
		links.put(KnowledgeBase, hypKnowledgeBase);
		links.put(KPI, hypKPI);
		links.put(Statistics, hypStatistics);
	}

	public void bindManagedToVisible() {
		links.forEach((applicatieFunctie, hyperlink) -> {
			hyperlink.managedProperty().bind(hyperlink.visibleProperty());
		});
	}

	private void changeVisibilityLinks(Set<ApplicatieFunctie> functies, boolean visible) {
		for (ApplicatieFunctie functie : functies) {
			links.get(functie).setVisible(visible);
		}
	}

	public void updateAantalGewijzigdeTicketten() {
		lblAantalTicketsInProgress.setText("(" + Integer.toString(scherm.geefAantalGewijzigdeTickets()) + ")");
	}
	// -- Hulpmethodes
}
