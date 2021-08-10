package domain.statistieken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domain.Ticket;
import domain.enumerations.StatistiekFilter;

public class AantalOpgelosteTicketsPerKlant implements Verwerkingsmanier<Ticket> {

	@Override
	public Map<String, Double> verwerkData(Map<StatistiekFilter, Object> filters, List<Ticket> data) {
		List<Predicate<Ticket>> predicaten = new ArrayList<>();

		LocalDateTime startDatum = (LocalDateTime) filters.get(StatistiekFilter.StartDatum);
		if (startDatum != null) {
			predicaten.add(ticket -> ticket.getDatumAanmaak().isAfter(startDatum));
		}

		LocalDateTime eindDatum = (LocalDateTime) filters.get(StatistiekFilter.EindDatum);
		if (eindDatum != null) {
			predicaten.add(ticket -> ticket.isAfgehandeld() && ticket.getDatumAfgehandeld().isBefore(eindDatum));
		}

		Set<String> klantNamen = (Set<String>) filters.get(StatistiekFilter.Naam);
		if (klantNamen != null) {
			predicaten.add(ticket -> !klantNamen.contains(ticket.getContract().getKlant().getBedrijfsNaam()));
		}

		Predicate<Ticket> filter = predicaten.stream().reduce(Predicate::and).orElse(x -> true);

		return data.stream().filter(filter)
				.collect(Collectors.groupingBy(e -> e.getContract().getKlant().getBedrijfsNaam(),
						Collectors.summingDouble(e -> e.isOptijdAfgehandeld() ? 1 : 0)));
	}
}
