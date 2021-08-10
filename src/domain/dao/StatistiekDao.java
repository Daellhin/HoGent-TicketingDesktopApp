package domain.dao;

import java.util.List;

import domain.enumerations.StatistiekType;
import domain.statistieken.Statistiek;

public interface StatistiekDao extends GenericDao<Statistiek> {

	List<StatistiekType> geefTrackedStatistiekTypes();

	void deleteAll();
}
