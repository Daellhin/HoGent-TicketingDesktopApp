package testen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;

public class ContractTest {

	private static Stream<Arguments> correcteParameters() {
		Klant klant = new Klant("Gebruikersnaam", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Actemium",
				Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam")));

		ContractType contractType = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(7));

		return Stream.of(
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(2), klant,
						"Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(2), klant,
						"Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Finished, LocalDate.now(), LocalDate.now().plusYears(2),
						klant, "Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Pending, LocalDate.now(), LocalDate.now().plusYears(2), klant,
						"Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Cancelled, LocalDate.now(), LocalDate.now().plusYears(2),
						klant, "Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(1), klant,
						"Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(3), klant,
						"Actemium", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(2), klant,
						"LangeBedrijfsNaamTestMoetNogWerken",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))));
	}

	private static Stream<Arguments> foutieveParameters() {
		Klant klant = new Klant("Gebruikersnaam", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Actemium",
				Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam")));

		ContractType contractType = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(7));

		return Stream.of(
				// ongeldig ContractType
				Arguments.of(null, ContractStatus.Finished, LocalDate.now(), LocalDate.now().plusYears(2), klant),
				// ongeldige ContractStatus
				Arguments.of(contractType, null, LocalDate.now(), LocalDate.now().plusYears(2), klant),
				// eindDatum kleiner dan beginDatum
				Arguments.of(contractType, ContractStatus.Cancelled, LocalDate.now(), LocalDate.now().minusYears(1),
						klant),
				Arguments.of(contractType, ContractStatus.Cancelled, LocalDate.now(),
						LocalDate.now().plusYears(1).minusDays(1), klant),
				// klant null
				Arguments.of(contractType, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusYears(1), null));
	}

	@ParameterizedTest
	@MethodSource("correcteParameters")
	public void correctContractAanmaken_vultVeldenCorrectIn(ContractType contractType, ContractStatus status,
			LocalDate startDatum, LocalDate eindDatum, Klant klant) {
		Contract contract = new Contract(contractType, status, startDatum, eindDatum, klant);
		Assertions.assertEquals(contractType, contract.getContractType());
		Assertions.assertEquals(status, contract.getStatus());
		Assertions.assertEquals(startDatum, contract.getStartDatum());
		Assertions.assertEquals(eindDatum, contract.getEindDatum());
		Assertions.assertEquals(klant, contract.getKlant());
	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutiefContractAanmaken_werptIllegalArgumentException(ContractType contractType, ContractStatus status,
			LocalDate startDatum, LocalDate eindDatum, Klant klant) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Contract(contractType, status, startDatum, eindDatum, klant));
	}

}
