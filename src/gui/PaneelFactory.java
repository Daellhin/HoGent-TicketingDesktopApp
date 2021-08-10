package gui;

import domain.enumerations.ApplicatieFunctie;
import gui.admin.BeheerKlanten;
import gui.admin.BeheerWerknemers;
import gui.enumerations.Link;
import gui.general.BeheerTickets;
import gui.general.DashboardGebruiker;
import gui.general.GebruikerScherm;
import gui.general.statistiek.StatistiekenBekijken;
import gui.supportManager.BeheerContractTypes;
import gui.supportManager.KPIBeheer;
import javafx.scene.layout.Pane;

public class PaneelFactory {

	public static Pane create(ApplicatieFunctie functie, GebruikerScherm scherm) {
		return switch (functie) {
		case Dashboard -> new DashboardGebruiker(scherm);
		case ManageCustomers -> new BeheerKlanten(scherm);
		case ManageEmployees -> new BeheerWerknemers(scherm);
		case ManageContractTypes -> new BeheerContractTypes(scherm);
		case NewTicket -> new DashboardGebruiker(scherm);
		// case NewTicket -> throw new UnsupportedOperationException();
		case TicketsInProgress -> new BeheerTickets(scherm, Link.OpenTickets);
		case FinishedTickets -> new BeheerTickets(scherm, Link.GeslotenTickets);
		case KnowledgeBase -> new DashboardGebruiker(scherm);
		// case KnowledgeBase -> new BeheerTickets(scherm, Link.KnowledgeDatabase);
		case Statistics -> new StatistiekenBekijken(scherm);
		case KPI -> new KPIBeheer(scherm);
		default -> throw new IllegalArgumentException("Unexpected value: " + functie);
		};
	}

}
