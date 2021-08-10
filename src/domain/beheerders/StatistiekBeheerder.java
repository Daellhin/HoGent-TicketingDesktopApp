package domain.beheerders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.Ticket;
import domain.dao.StatistiekDao;
import domain.dao.TicketDao;
import domain.enumerations.StatistiekType;
import domain.factories.StatistiekFactory;
import domain.statistieken.Statistiek;
import repository.StatistiekDaoJpa;
import repository.TicketDaoJpa;

public class StatistiekBeheerder {

	private StatistiekDao statistiekDao;
	private TicketDao ticketDao;
	private List<Ticket> tickets;
	private List<StatistiekType> trackedStatistiekTypes;

	private StatistiekBeheerder(StatistiekDao statistiekDao, TicketDao ticketDao) {
		this.statistiekDao = statistiekDao;
		this.ticketDao = ticketDao;
	}

	public StatistiekBeheerder() {
		this(new StatistiekDaoJpa(), new TicketDaoJpa());
	}

	public List<StatistiekType> getTrackedStatistiekTypes() {
		if (trackedStatistiekTypes == null) {
			trackedStatistiekTypes = statistiekDao.geefTrackedStatistiekTypes();
		}
		return trackedStatistiekTypes;
	}

	public List<StatistiekType> getUntrackedStatistiekTypes() {
		return StatistiekType.valueSet().stream().filter(e -> !getTrackedStatistiekTypes().contains(e))
				.collect(Collectors.toList());
	}

	public void trackStatistiekTypes(List<StatistiekType> statistiek) {
		List<Statistiek<?>> statistiekTypes = new ArrayList<>();
		statistiek.forEach(type -> {
			statistiekTypes.add(new Statistiek<>(type));
		});

		statistiekDao.startTransaction();
		statistiekDao.deleteAll();

		for (Statistiek<?> stat : statistiekTypes) {
			statistiekDao.insert(stat);
		}
		statistiekDao.commitTransaction();

	}

	public List<Statistiek<?>> getTrackedStatistieken() {
		List<StatistiekType> types = statistiekDao.geefTrackedStatistiekTypes();

		List<Statistiek<?>> statistieken = new ArrayList<>();
		types.forEach(naam -> {
			statistieken.add(StatistiekFactory.createStatistiek(naam, this));
		});
		return statistieken;
	}

	// deze methode word enkel opgeroepen door de factory
	public List<Ticket> getTickets() {
		if (tickets == null) {
			tickets = ticketDao.findAll();
		}
		return tickets;
	}
}
