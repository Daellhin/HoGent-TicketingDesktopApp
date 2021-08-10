package domain.dao;

import java.util.List;

import domain.Ticket;
import domain.Werknemer;

public interface TicketDao extends GenericDao<Ticket> {

	List<Ticket> geefTicketsVanTechnieker(Werknemer werknemer);

	List<Ticket> geefGewijzigdeTicketsVanTechnieker(Werknemer aangemeldeWerknemer);

	List<Ticket> geefAlleGewijzigdeTickets();
}
