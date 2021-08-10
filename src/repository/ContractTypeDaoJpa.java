package repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import domain.ContractType;
import domain.Ticket;
import domain.dao.ContractTypeDao;
import domain.enumerations.TicketStatus;

public class ContractTypeDaoJpa extends GenericDaoJpa<ContractType> implements ContractTypeDao {

	public ContractTypeDaoJpa() {
		super(ContractType.class);
	}

	@Override
	public boolean bestaatContractTypeNaam(String naam) {
		try {
			em.createNamedQuery("ContractType.bestaatContractTypeNaam", ContractType.class).setParameter("naam", naam)
					.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	@Override
	public int geefAantalBehandeldeTicketsVanContractType(String naam) {
		List<Ticket> tickets = em.createNamedQuery("Ticket.geefAfgehandeldeTickets", Ticket.class)
				.setParameter("status", TicketStatus.Finished).getResultList();
		long aantal = tickets.stream().filter(e -> e.getContract().getContractType().getNaam().equals(naam)).count();
		return (int) aantal;
	}

	@Override
	public double geefPercentageOpTijdBehandeldeTicketsVanContractType(String naam) {
		List<Ticket> tickets = em.createNamedQuery("Ticket.geefAfgehandeldeTickets", Ticket.class)
				.setParameter("status", TicketStatus.Finished).getResultList();
		tickets = tickets.stream().filter(e -> e.getContract().getContractType().getNaam().equals(naam))
				.collect(Collectors.toList());
		double aantalAfgehandeld = tickets.stream().filter(Ticket::isOptijdAfgehandeld).count();
		// afronden tot 2 cijfers na de komma: Math.round(VALUE * 100) / 100;
		return Math.round((aantalAfgehandeld == 0 ? 0 : (aantalAfgehandeld / tickets.size()) * 100) * 100) / 100;
	}

}
