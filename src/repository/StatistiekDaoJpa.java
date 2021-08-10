package repository;

import java.util.List;

import domain.dao.StatistiekDao;
import domain.enumerations.StatistiekType;
import domain.statistieken.Statistiek;

public class StatistiekDaoJpa extends GenericDaoJpa<Statistiek> implements StatistiekDao {

	public StatistiekDaoJpa() {
		super(Statistiek.class);
	}

	@Override
	public List<StatistiekType> geefTrackedStatistiekTypes() {
		return em.createNamedQuery("Statistiek.geefTrackedStatistiekTypes", StatistiekType.class).getResultList();
	}

	@Override
	public void deleteAll() {
		em.createQuery("DELETE FROM Statistiek").executeUpdate();
	}

}
