package domain.enumerations;

import static domain.enumerations.GrafiekType.*;
import static domain.enumerations.StatistiekFilter.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum StatistiekType {
	//@formatter:off
	AverageDurationTicketsPerContractType(HorizontalBarChart, new HashSet<>(Arrays.asList(StartDatum, EindDatum, Naam)), "Average Duration Tickets Per Contract Type", "Average Duration (Hours)", "Contact Type"), 
	AmountOfFinishedTicketsPerCustomer(VerticalBarChart, new HashSet<>(Arrays.asList(StartDatum, EindDatum, Naam)), "Amount Of Finished Tickets Per Customer", "Amount Of Tickets", "Customer");
	//@formatter:on

	private GrafiekType grafiekType;
	private Set<StatistiekFilter> statistiekFilters;
	private String xAsNaam;
	private String yAsNaam;
	private String titel;

	StatistiekType(GrafiekType grafiekType, Set<StatistiekFilter> statistiekFilters, String titel, String xAsNaam,
			String yAsNaam) {
		this.grafiekType = grafiekType;
		this.statistiekFilters = statistiekFilters;
		this.titel = titel;
		this.xAsNaam = xAsNaam;
		this.yAsNaam = yAsNaam;
	}

	public static HashSet<StatistiekType> valueSet() {
		return new HashSet<>(Arrays.asList(StatistiekType.values()));
	}

	public GrafiekType getGrafiekType() {
		return this.grafiekType;
	}

	public Set<StatistiekFilter> getStatistiekFilters() {
		return this.statistiekFilters;
	}

	public String getTitel() {
		return titel;
	}

	public String getXAsNaam() {
		return this.xAsNaam;
	}

	public String getYAsNaam() {
		return this.yAsNaam;
	}

}
