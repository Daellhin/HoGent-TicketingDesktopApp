package domain.controllers;

import java.util.List;

import domain.beheerders.StatistiekBeheerder;
import domain.enumerations.StatistiekType;
import domain.statistieken.Statistiek;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StatistiekController {

	private StatistiekBeheerder statistiekBeheerder;

	public StatistiekController() {
		this.statistiekBeheerder = new StatistiekBeheerder();
	}

	// enkel voor KPI beheren scherm
	public ObservableList<StatistiekType> getTrackedStatistiekTypes() {
		return FXCollections.observableArrayList(statistiekBeheerder.getTrackedStatistiekTypes());
	}

	// enkel voor KPI beheren scherm
	public ObservableList<StatistiekType> getUntrackedStatistiekTypes() {
		return FXCollections.observableArrayList(statistiekBeheerder.getUntrackedStatistiekTypes());
	}

	// enkel voor KPI beheren scherm
	public void trackStatistiekTypes(List<StatistiekType> statistiek) {
		statistiekBeheerder.trackStatistiekTypes(statistiek);
	}

	// enkel voor statistieken scherm
	public List<Statistiek<?>> getTrackedStatistieken() {
		return statistiekBeheerder.getTrackedStatistieken();
	}
}
