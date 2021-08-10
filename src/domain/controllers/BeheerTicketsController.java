package domain.controllers;

import java.util.Set;

import domain.Ticket;
import domain.Werknemer;
import domain.beheerders.TicketBeheerder;
import javafx.collections.ObservableList;

public class BeheerTicketsController {

	private TicketBeheerder ticketBeheerder;

	public BeheerTicketsController(Werknemer aangemeldeWerknemer) {
		this.ticketBeheerder = new TicketBeheerder(aangemeldeWerknemer);
	}

	public void voegTicketToe(Ticket ticket) {
		ticketBeheerder.voegTicketToe(ticket);
	}

	public void pasTicketAan(Ticket ticket) {
		ticketBeheerder.pasTicketAan(ticket);
	}

	public void pasFilterAan(int ticketnummer, String titel, Set<domain.enumerations.TicketStatus> status,
			Set<domain.enumerations.TicketUrgency> type) {
		ticketBeheerder.pasFilterAan(ticketnummer, titel, status, type);
	}

	public ObservableList<Ticket> getTicketsList() {
		return ticketBeheerder.getTicketList();
	}

	public ObservableList<Ticket> getGewijzigdeTicketten() {
		return ticketBeheerder.getGewijzigdeTicketten();
	}

}
