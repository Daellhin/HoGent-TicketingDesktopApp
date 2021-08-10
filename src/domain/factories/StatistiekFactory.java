package domain.factories;

import domain.beheerders.StatistiekBeheerder;
import domain.enumerations.StatistiekType;
import domain.statistieken.AantalOpgelosteTicketsPerKlant;
import domain.statistieken.GemiddeldeDoorlooptijdPerContractType;
import domain.statistieken.Statistiek;

public class StatistiekFactory {

	public static Statistiek<?> createStatistiek(StatistiekType statistiekType,
			StatistiekBeheerder statistiekBeheerder) {
		return switch (statistiekType) {
		case AverageDurationTicketsPerContractType -> new Statistiek<>(statistiekType, statistiekBeheerder.getTickets(),
				new GemiddeldeDoorlooptijdPerContractType());

		case AmountOfFinishedTicketsPerCustomer -> new Statistiek<>(statistiekType, statistiekBeheerder.getTickets(),
				new AantalOpgelosteTicketsPerKlant());

		default -> throw new IllegalArgumentException("Unexpected value" + statistiekType);
		};
	}
}
