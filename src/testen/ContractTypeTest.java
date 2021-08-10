package testen;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ContractType;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;

public class ContractTypeTest {

	private static Set<TicketAanmaakManier> maakTicketAanmaakManierSet() {
		Set<TicketAanmaakManier> ticketAanmaakManier = new HashSet<>();
		ticketAanmaakManier.add(TicketAanmaakManier.Application);
		ticketAanmaakManier.add(TicketAanmaakManier.Email);
		return ticketAanmaakManier;
	}

	private static Stream<Arguments> correcteParameters() {
		return Stream.of(
				Arguments.of("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, new BigDecimal(1000)),
				Arguments.of("Robin", maakTicketAanmaakManierSet(), TicketAanmaakTijd.WorkingHours, 750,
						ContractDoorlooptijd.TwoYear, new BigDecimal(5000)));
	}

	private static Stream<Arguments> foutieveParameters() {
		return Stream.of(
				// foute naam
				Arguments.of("", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, new BigDecimal(1000)),
				Arguments.of(null, maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, new BigDecimal(1000)),
				Arguments.of("      ", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, new BigDecimal(1000)),
				// foute TicketAanmaakManier
				Arguments.of("Joery", null, TicketAanmaakTijd.AllTime, 400, ContractDoorlooptijd.OneYear,
						new BigDecimal(1000)),
				// foute ticketAanmaakTijd
				Arguments.of("Joery", maakTicketAanmaakManierSet(), null, 400, ContractDoorlooptijd.OneYear,
						new BigDecimal(1000)),
				// foute ticketAfhandeltijd
				Arguments.of("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, -500,
						ContractDoorlooptijd.OneYear, new BigDecimal(1000)),
				// foute contractDoorlooptijd
				Arguments.of("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400, null,
						new BigDecimal(1000)),
				// foute prijs
				Arguments.of("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, null),
				Arguments.of("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
						ContractDoorlooptijd.OneYear, new BigDecimal(-1000)));
	}

	@ParameterizedTest
	@MethodSource("correcteParameters")
	public void correctContractAanmaken_vultVeldenCorrectIn(String naam, Set<TicketAanmaakManier> ticketAanmaakManier,
			TicketAanmaakTijd ticketAanmaakTijd, int ticketAfhandeltijd, ContractDoorlooptijd minimaleDoorlooptijd,
			BigDecimal prijs) {
		ContractType cType = new ContractType(naam, ticketAanmaakManier, ticketAanmaakTijd, ticketAfhandeltijd,
				minimaleDoorlooptijd, prijs);
		Assertions.assertEquals(naam, cType.getNaam());
		Assertions.assertEquals(ticketAanmaakManier, cType.getTicketAanmaakManier());
		Assertions.assertEquals(ticketAanmaakTijd, cType.getTicketAanmaakTijd());
		Assertions.assertEquals(ticketAfhandeltijd, cType.getTicketAfhandeltijd());
		Assertions.assertEquals(minimaleDoorlooptijd, cType.getMinimaleDoorlooptijd());
		Assertions.assertEquals(prijs, cType.getPrijs());
	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutiefContractAanmaken_werptIllegalArgumentException(String naam,
			Set<TicketAanmaakManier> ticketAanmaakManier, TicketAanmaakTijd ticketAanmaakTijd, int ticketAfhandeltijd,
			ContractDoorlooptijd minimaleDoorlooptijd, BigDecimal prijs) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractType(naam, ticketAanmaakManier,
				ticketAanmaakTijd, ticketAfhandeltijd, minimaleDoorlooptijd, prijs));

	}

	@Test
	public void setStatus_foutieveParameter() {
		ContractType cType = new ContractType("Joery", maakTicketAanmaakManierSet(), TicketAanmaakTijd.AllTime, 400,
				ContractDoorlooptijd.OneYear, new BigDecimal(1000));
		Assertions.assertThrows(IllegalArgumentException.class, () -> cType.setStatus(null));
	}
}
