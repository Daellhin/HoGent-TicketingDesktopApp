package domain.beheerders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import domain.Ticket;
import domain.Werknemer;
import domain.dao.TicketDao;
import domain.dao.WerknemerDao;
import domain.enumerations.TicketStatus;
import domain.enumerations.TicketUrgency;
import domain.enumerations.WerknemersType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.TicketDaoJpa;
import repository.WerknemerDaoJpa;

public class TicketBeheerder {

	private TicketDao ticketDao;
	private FilteredList<Ticket> filteredTicketList;
	private Werknemer aangemeldeWerknemer;
	private WerknemerDao werknemerDao;

	private TicketBeheerder(TicketDao ticketDao, WerknemerDao werknemerDao) {
		this.ticketDao = ticketDao;
		this.werknemerDao = werknemerDao;
	}

	public TicketBeheerder(Werknemer aangemeldeWerknemer) {
		this(new TicketDaoJpa(), new WerknemerDaoJpa());
		this.aangemeldeWerknemer = aangemeldeWerknemer;
	}

	public void voegTicketToe(Ticket ticket) {
		ticketDao.startTransaction();

		List<Werknemer> techniekers = werknemerDao.geefTechniekersVanDienst(ticket.getDienst());
		if (!techniekers.isEmpty()) {
			ticket.setToegewezenTechnieker(techniekers.get(new Random().nextInt(techniekers.size())));
			ticket.setStatus(TicketStatus.Pending);
		}
		ticketDao.insert(ticket);
		ticketDao.commitTransaction();
		haalTicketsOp();
	}

	public void pasTicketAan(Ticket ticket) {
		ticketDao.startTransaction();
		if (ticket.getStatus() == TicketStatus.AwaitingCustomerInformation
				|| ticket.getStatus() == TicketStatus.Finished)
			ticket.setBekekenDoorKlant(false);
		if (ticket.getDatumAfgehandeld() == null
				&& (ticket.getStatus() == TicketStatus.Cancelled || ticket.getStatus() == TicketStatus.Finished))
			ticket.setDatumAfgehandeld(LocalDateTime.now());
		ticketDao.update(ticket);
		ticketDao.commitTransaction();
		haalTicketsOp();
	}

	public void pasFilterAan(int ticketnummer, String titel, Set<TicketStatus> status, Set<TicketUrgency> type) {
		List<Predicate<Ticket>> filters = new ArrayList<>();

		if (ticketnummer >= 0) {
			filters.add(ticket -> Integer.toString(ticket.getId()).startsWith(Integer.toString(ticketnummer)));
		}

		if (titel != null && !titel.isBlank()) {
			filters.add(ticket -> ticket.getTitel().toLowerCase().contains(titel.toLowerCase()));
		}

		if (status != null && (status.size() > 0 || status.size() >= TicketStatus.values().length)) {
			filters.add(ticket -> status.contains(ticket.getStatus()));
		}
		if (type != null && (type.size() > 0 || type.size() >= TicketStatus.values().length)) {
			filters.add(ticket -> type.contains(ticket.getType()));
		}

		Predicate<Ticket> filter = filters.stream().reduce(Predicate::and).orElse(x -> true);
		filteredTicketList.setPredicate(filter);
	}

	public ObservableList<Ticket> getTicketList() {
		if (filteredTicketList == null) {
			haalTicketsOp();
		}
		return FXCollections.unmodifiableObservableList(filteredTicketList);
	}

	// nodig om klasse testbaar te maken
	public void setAangemeldeWerknemer(Werknemer aangemeldeWerknemer) {
		this.aangemeldeWerknemer = aangemeldeWerknemer;
	}

	@SuppressWarnings("unchecked")
	private void haalTicketsOp() {
		Predicate<Ticket> filter;
		if (filteredTicketList == null) {
			filter = e -> true;
		} else {
			filter = (Predicate<Ticket>) filteredTicketList.getPredicate();
		}

		if (aangemeldeWerknemer.getWerknemersType().equals(WerknemersType.Technician)) {
			filteredTicketList = new FilteredList<>(
					FXCollections.observableArrayList(ticketDao.geefTicketsVanTechnieker(aangemeldeWerknemer)), filter);
		} else {
			filteredTicketList = new FilteredList<>(FXCollections.observableArrayList(ticketDao.findAll()), filter);
		}
	}

	public ObservableList<Ticket> getGewijzigdeTicketten() {
		if (aangemeldeWerknemer.getWerknemersType().equals(WerknemersType.Technician)) {
			return FXCollections.observableArrayList(ticketDao.geefGewijzigdeTicketsVanTechnieker(aangemeldeWerknemer));
		} else {
			return FXCollections.observableArrayList(ticketDao.geefAlleGewijzigdeTickets());
		}

	}

}
