package domain.statistieken;

import java.util.List;
import java.util.Map;

import domain.enumerations.StatistiekFilter;

public interface Verwerkingsmanier<T> {

	Map<String, Double> verwerkData(Map<StatistiekFilter, Object> filters, List<T> data);
}
