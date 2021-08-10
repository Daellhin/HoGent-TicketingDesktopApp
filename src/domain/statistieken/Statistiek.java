package domain.statistieken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import domain.enumerations.StatistiekFilter;
import domain.enumerations.StatistiekType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

@Entity
@NamedQueries({
		@NamedQuery(name = "Statistiek.geefTrackedStatistiekTypes", query = "SELECT s.type FROM Statistiek s") })
public class Statistiek<T> {
	@Id
	private StatistiekType type;
	@Transient
	private Verwerkingsmanier<T> verwerkingsmanier;
	@Transient
	private List<T> data;
	@Transient
	private Map<StatistiekFilter, Object> filters;
	@Transient
	private ObservableMap<String, Double> verwerkteData;

	protected Statistiek() {
	}

	// word opgeroepen om geselecteerde KPI's op te slagen in de databank
	public Statistiek(StatistiekType type) {
		this.type = type;
	}

	// word opgroepen door de statistiekfactory, om data aan grafieken te geven
	public Statistiek(StatistiekType type, List<T> data, Verwerkingsmanier<T> verwerkingsmanier) {
		this(type);
		this.data = data;
		this.verwerkingsmanier = verwerkingsmanier;
		this.verwerkteData = FXCollections.observableHashMap();
		maakFilterMapAan();
		verwerkData();
	}

	private void maakFilterMapAan() {
		this.filters = new HashMap<>();
		List<StatistiekFilter> statistiekFilters = Arrays.asList(StatistiekFilter.values());
		statistiekFilters.forEach(filter -> {
			filters.put(filter, null);
		});
	}

	private void verwerkData() {
		verwerkteData.clear();
		verwerkteData.putAll(verwerkingsmanier.verwerkData(filters, data));
	}

	public void filterData(StatistiekFilter key, Object value) {
		if (!filters.containsKey(key)) {
			throw new IllegalArgumentException("Unexpected value" + key);
		} else {
			filters.replace(key, value);
		}
		verwerkData();
	}

	public ObservableMap<String, Double> getVerwerkteData() {
		return this.verwerkteData;
	}

	public StatistiekType getType() {
		return this.type;
	}

}
