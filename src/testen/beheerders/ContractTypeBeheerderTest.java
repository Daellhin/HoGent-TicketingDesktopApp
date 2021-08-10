package testen.beheerders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Adres;
import domain.Contactpersoon;
import domain.Contract;
import domain.ContractType;
import domain.Klant;
import domain.Ticket;
import domain.beheerders.ContractTypeBeheerder;
import domain.dao.ContractTypeDao;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.ContractTypeStatus;
import domain.enumerations.Dienst;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import domain.enumerations.TicketUrgency;

@ExtendWith(MockitoExtension.class)
public class ContractTypeBeheerderTest {

	@Mock
	private ContractTypeDao contractTypeDao;
	@InjectMocks
	private ContractTypeBeheerder contractTypeBeheerder;

	private List<ContractType> lijstContractTypes;

	// -- Hulpmethoden --
	private void maakContractTypesAan() {
		lijstContractTypes = new ArrayList<>();

		ContractType contractType1 = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(7));

		ContractType contractType2 = new ContractType("ContractType2",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Email)), TicketAanmaakTijd.WorkingHours, 5,
				ContractDoorlooptijd.TwoYear, new BigDecimal(3));

		ContractType contractType3 = new ContractType("ContractType3",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Telephone, TicketAanmaakManier.Email)),
				TicketAanmaakTijd.AllTime, 3, ContractDoorlooptijd.ThreeYear, new BigDecimal(1));
		contractType3.setStatus(ContractTypeStatus.Inactive);

		lijstContractTypes.add(contractType1);
		lijstContractTypes.add(contractType2);
		lijstContractTypes.add(contractType3);

		Klant klant1 = new Klant("B-Temium", Arrays.asList("+32472643661"),
				new Adres("BE", "9000", "Gent", "Straattraat", 5), "B-Temium Inc.",
				Arrays.asList(new Contactpersoon("bee.temium@btemium.com", "Bee", "Temium")));

		Contract contract1 = new Contract(contractType1, ContractStatus.Active, LocalDate.now(),
				LocalDate.now().plusDays(400), klant1);

		Contract contract2 = new Contract(contractType1, ContractStatus.Active, LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(380), klant1);

		Contract contract3 = new Contract(contractType2, ContractStatus.Active, LocalDate.now().plusDays(6),
				LocalDate.now().plusDays(450), klant1);

		Ticket ticket1 = new Ticket("Title1", TicketUrgency.ProductionImpacted, "omschrijving1", new ArrayList<>(),
				contract1, Dienst.Finance);
		Ticket ticket2 = new Ticket("Title2", TicketUrgency.NoProductionImpact, "omschrijving2", new ArrayList<>(),
				contract2, Dienst.Admin);
		Ticket ticket3 = new Ticket("Title3", TicketUrgency.ProductionWillBeImpacted, "omschrijving3",
				new ArrayList<>(), contract3, Dienst.IT);

	}

	private static Stream<Arguments> nullEnLegeFilterParameters() {
		//@formatter:off
			return Stream.of(
					Arguments.of("", null, 0), 
					Arguments.of(null, new HashSet<>(), 0),
					Arguments.of(null, null, -1)
					);
			//@formatter:on
	}

	private static Stream<Arguments> normaleFilters() {
		// filters meegeven en lijst van verwachte resultaten
		//@formatter:off
			return Stream.of(
					// als inactief niet meertelt dan is het 2 ipv 3
					Arguments.of("ContractType", null, 0, 3), 
					Arguments.of("1", null, 0, 1), 
					Arguments.of("abcdef", null, 0, 0), 
					Arguments.of("", new HashSet<>(Arrays.asList(ContractTypeStatus.Active)), 0, 2),
					Arguments.of("", new HashSet<>(Arrays.asList(ContractTypeStatus.Inactive)), 0, 1),
					Arguments.of("", null, 1, 2),
					Arguments.of("", null, 2, 1),
					Arguments.of("", null, 5, 0)
					);
			//@formatter:on
	}

	// -- Hulpmethoden --

	@BeforeEach
	public void beforeEach() {
		maakContractTypesAan();
		Mockito.lenient().when(contractTypeDao.findAll()).thenReturn(lijstContractTypes);
	}

	@Test
	public void newContractTypeBeheerder_GeeftActieveContractTypes() {
		// Act
		contractTypeBeheerder.getContractTypesList();

		// Assert
		Mockito.verify(contractTypeDao).findAll();
	}

	@Test
	public void voegContractTypeToe_InsertContractType() {
		// Act
		contractTypeBeheerder.getContractTypesList();
		ContractType contractType = new ContractType("ContractType4",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.WorkingHours, 2,
				ContractDoorlooptijd.OneYear, new BigDecimal(8));
		contractTypeBeheerder.voegContractTypeToe(contractType);

		// Assert
		Mockito.verify(contractTypeDao).insert(contractType);
		Mockito.verify(contractTypeDao, Mockito.times(2)).findAll();
	}

	@Test
	public void pasContractTypeAan_UpdateContractType() {
		// Act
		ContractType contractType = contractTypeBeheerder.getContractTypesList().get(0);
		contractType.setNaam("abc");
		contractTypeBeheerder.pastContractTypeAan(contractType);

		// Assert
		Mockito.verify(contractTypeDao).update(contractType);
		Mockito.verify(contractTypeDao, Mockito.times(2)).findAll();
	}

	@ParameterizedTest
	@MethodSource("nullEnLegeFilterParameters")
	public void pasFilterAan_LegeParameters_GeeftAlles(String naam, Set<ContractTypeStatus> status,
			int aantalContracten) {
		// Act
		contractTypeBeheerder.getContractTypesList();
		contractTypeBeheerder.pasFilterAan(naam, status, aantalContracten);

		// Assert
		Assertions.assertEquals(lijstContractTypes.size(), contractTypeBeheerder.getContractTypesList().size());
		Mockito.verify(contractTypeDao).findAll();

	}

	@Test
	public void pasFilterAan_AlleStatusenGeselecteerd_GeeftAlles() {
		// Act
		contractTypeBeheerder.getContractTypesList();
		contractTypeBeheerder.pasFilterAan(null, new HashSet<>(Arrays.asList(ContractTypeStatus.values())), 0);

		// Assert
		Assertions.assertEquals(lijstContractTypes.size(), contractTypeBeheerder.getContractTypesList().size());
		Mockito.verify(contractTypeDao).findAll();
	}

	@ParameterizedTest
	@MethodSource("normaleFilters")
	public void pasFilterAan_NormaleParameters_FiltertCorrect(String naam, Set<ContractTypeStatus> status,
			int aantalContracten, int expectedSize) {
		// Act
		contractTypeBeheerder.getContractTypesList();
		contractTypeBeheerder.pasFilterAan(naam, status, aantalContracten);

		// Assert
		Assertions.assertEquals(expectedSize, contractTypeBeheerder.getContractTypesList().size());
		Mockito.verify(contractTypeDao).findAll();
	}
}
