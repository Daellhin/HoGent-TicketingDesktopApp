package repository;

import java.util.List;

import domain.Ticket;
import domain.Werknemer;
import domain.dao.TicketDao;

public class TicketDaoJpa extends GenericDaoJpa<Ticket> implements TicketDao {

	public TicketDaoJpa() {
		super(Ticket.class);
	}

	@Override
	public List<Ticket> geefTicketsVanTechnieker(Werknemer werknemer) {
		return em.createNamedQuery("Ticket.geefTicketsVanTechnieker", Ticket.class)
				.setParameter("personeelsnummer", werknemer).getResultList();
	}

	@Override
	public List<Ticket> geefGewijzigdeTicketsVanTechnieker(Werknemer aangemeldeWerknemer) {
		return em.createNamedQuery("Ticket.geefGewijzigdeTicketsVanTechnieker", Ticket.class)
				.setParameter("werknemer", aangemeldeWerknemer).getResultList();
	}

	@Override
	public List<Ticket> geefAlleGewijzigdeTickets() {
		return em.createNamedQuery("Ticket.geefAlleGewijzigdeTickets", Ticket.class).getResultList();
	}

}
