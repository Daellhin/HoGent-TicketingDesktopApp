package testen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Adres;
import domain.Contactpersoon;
import domain.Contract;
import domain.ContractType;
import domain.Klant;
import domain.Ticket;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.Dienst;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import domain.enumerations.TicketUrgency;

public class TicketTest {

	private static Stream<Arguments> foutieveParameters() {
		Klant klant = new Klant("gebruikersnaam", Arrays.asList("+32472359285", "+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
				Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam")));

		ContractType contractType = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(7));

		Contract contract = new Contract(contractType, ContractStatus.Cancelled, LocalDate.now(),
				LocalDate.now().plusDays(400), klant);

		return Stream.of(
				// foutieve gebruikersnaam
				Arguments.of(null, TicketUrgency.ProductionImpacted, "Dit is een omschrijving!", Arrays.asList("test"),
						contract, Dienst.Admin),
				Arguments.of("", TicketUrgency.ProductionImpacted, "Dit is een omschrijving!", Arrays.asList("test"),
						contract, Dienst.Admin),
				Arguments.of("          ", TicketUrgency.ProductionImpacted, "Dit is een omschrijving!",
						Arrays.asList("test"), contract, Dienst.Admin),
				// omschrijving
				Arguments.of("Gebruikersnaam", TicketUrgency.NoProductionImpact, null, Arrays.asList("test"), contract,
						Dienst.Admin),
				Arguments.of("Gebruikersnaam", TicketUrgency.NoProductionImpact, "", Arrays.asList("test"), contract,
						Dienst.Admin),
				Arguments.of("Gebruikersnaam", TicketUrgency.NoProductionImpact, "            ", Arrays.asList("test"),
						contract, Dienst.Admin));
	}

	private static Stream<Arguments> correcteParameters() {
		Klant klant = new Klant("gebruikersnaam", Arrays.asList("+32472359285", "+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
				Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam")));

		ContractType contractType = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(7));

		Contract contract = new Contract(contractType, ContractStatus.Cancelled, LocalDate.now(),
				LocalDate.now().plusDays(400), klant);

		return Stream.of(
				Arguments.of("Gebruikersnaam", TicketUrgency.NoProductionImpact, "Dit is een omschrijving!",
						Arrays.asList("test"), contract, Dienst.Admin),
				Arguments.of("Gebruikersnaam", TicketUrgency.ProductionImpacted, "Dit is een omschrijving!",
						Arrays.asList("test"), contract, Dienst.Admin),
				Arguments.of("Gebruikersnaam", TicketUrgency.ProductionWillBeImpacted, "Dit is een omschrijving!",
						Arrays.asList("test"), contract, Dienst.Admin));
	}

	@ParameterizedTest
	@MethodSource("correcteParameters")
	public void correctTicketAanmaken_steltAtributenCorrectIn(String titel, TicketUrgency ticketType,
			String omschrijving, Collection<String> bijlages, Contract contract, Dienst dienst) {
		Ticket ticket = new Ticket(titel, ticketType, omschrijving, bijlages, contract, dienst);
		Assertions.assertEquals(titel, ticket.getTitel());
		Assertions.assertEquals(ticketType, ticket.getType());
		Assertions.assertEquals(omschrijving, ticket.getOmschrijving());
		Assertions.assertEquals(bijlages, ticket.getBijlages());
		Assertions.assertEquals(contract, ticket.getContract());
		Assertions.assertEquals(dienst, ticket.getDienst());

	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutiefTicketAanmaken_werptException(String titel, TicketUrgency ticketType, String omschrijving,
			Collection<String> bijlages, Contract contract, Dienst dienst) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Ticket(titel, ticketType, omschrijving, bijlages, contract, dienst));
	}

}
