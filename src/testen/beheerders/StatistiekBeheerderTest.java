package testen.beheerders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import domain.beheerders.StatistiekBeheerder;
import domain.dao.StatistiekDao;
import domain.dao.TicketDao;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.ContractTypeStatus;
import domain.enumerations.Dienst;
import domain.enumerations.StatistiekType;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import domain.enumerations.TicketUrgency;
import domain.statistieken.AantalOpgelosteTicketsPerKlant;
import domain.statistieken.GemiddeldeDoorlooptijdPerContractType;
import domain.statistieken.Statistiek;

@ExtendWith(MockitoExtension.class)
public class StatistiekBeheerderTest {

	@Mock
	private StatistiekDao statistiekDao;

	@Mock
	private TicketDao ticketDao;

	@InjectMocks
	private StatistiekBeheerder statistiekBeheerder;

	List<StatistiekType> statistiekTypes;

	private List<StatistiekType> maakStatistiekTypesLijst() {
		List<StatistiekType> statistiekTypes = new ArrayList<>();
		statistiekTypes.add(StatistiekType.AmountOfFinishedTicketsPerCustomer);
		statistiekTypes.add(StatistiekType.AverageDurationTicketsPerContractType);
		return statistiekTypes;
	}

	private List<Ticket> maakTicketsAan() {
		List<ContractType> lijstContractTypes;
		// contractTypes aanmaken
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

		// klanten + contracten aanmaken
		Klant klant1 = new Klant("B-Temium", Arrays.asList("+32472643661"),
				new Adres("BE", "9000", "Gent", "Straattraat", 5), "B-Temium Inc.",
				Arrays.asList(new Contactpersoon("bee.temium@btemium.com", "Bee", "Temium")));

		Contract contract1 = new Contract(contractType1, ContractStatus.Active, LocalDate.now(),
				LocalDate.now().plusDays(400), klant1);

		Contract contract2 = new Contract(contractType1, ContractStatus.Active, LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(380), klant1);

		Contract contract3 = new Contract(contractType2, ContractStatus.Active, LocalDate.now().plusDays(6),
				LocalDate.now().plusDays(450), klant1);
		// ticketList aanmaken
		Ticket ticket1 = new Ticket("Title1", TicketUrgency.ProductionImpacted, "omschrijving1", new ArrayList<>(),
				contract1, Dienst.Finance);
		Ticket ticket2 = new Ticket("Title2", TicketUrgency.NoProductionImpact, "omschrijving2", new ArrayList<>(),
				contract1, Dienst.Admin);
		Ticket ticket3 = new Ticket("Title3", TicketUrgency.ProductionWillBeImpacted, "omschrijving3",
				new ArrayList<>(), contract3, Dienst.IT);
		List<Ticket> ticketList = new ArrayList<>();
		ticketList.add(ticket1);
		ticketList.add(ticket2);
		ticketList.add(ticket3);
		return ticketList;
	}

	private List<Statistiek<?>> maakStatistiekObjectenAan(List<Ticket> ticketList) {
		List<Statistiek<?>> statistiekLijst = new ArrayList<>();

		Statistiek<?> statistiek1 = new Statistiek<>(StatistiekType.AmountOfFinishedTicketsPerCustomer, ticketList,
				new AantalOpgelosteTicketsPerKlant());

		Statistiek<?> statistiek2 = new Statistiek<>(StatistiekType.AverageDurationTicketsPerContractType, ticketList,
				new GemiddeldeDoorlooptijdPerContractType());

		statistiekLijst.add(statistiek1);
		statistiekLijst.add(statistiek2);
		return statistiekLijst;
	}

	@Test
	public void getTickets_geeftLijstTickets() {
		// Arange
		List<Ticket> ticketList = maakTicketsAan();
		Mockito.when(ticketDao.findAll()).thenReturn(ticketList);
		// Act
		statistiekBeheerder.getTickets();
		// Assert
		Mockito.verify(ticketDao).findAll();

	}

	@Test
	public void getTrackedStatistiekTypes_roeptJuisteMethodeAan() {
		statistiekTypes = maakStatistiekTypesLijst();
		Mockito.when(statistiekDao.geefTrackedStatistiekTypes()).thenReturn(statistiekTypes);
		List<StatistiekType> statistiekTypesTest = statistiekBeheerder.getTrackedStatistiekTypes();
		Assertions.assertEquals(statistiekTypesTest, statistiekTypes);
	}

	@Test
	public void newStatistiekBeheerder_geeftTrackedStatistieken() {
		// Arange
		statistiekTypes = maakStatistiekTypesLijst();
		Mockito.when(statistiekDao.geefTrackedStatistiekTypes()).thenReturn(statistiekTypes);
		List<Statistiek<?>> statistiekLijst = maakStatistiekObjectenAan(maakTicketsAan());
		// Act
		List<Statistiek<?>> teTestenStatistiekLijst = statistiekBeheerder.getTrackedStatistieken();

		// Assert
		Assertions.assertEquals(statistiekLijst.get(0).getType(), teTestenStatistiekLijst.get(0).getType());
		Assertions.assertEquals(statistiekLijst.get(1).getType(), teTestenStatistiekLijst.get(1).getType());

		// Logica voor de verwerkte map aan te maken zit in een andere klasse.
		// Moet dit dan getest worden?
		// Assertions.assertEquals(statistiekLijst.get(0).getVerwerkteData(),
		// teTestenStatistiekLijst.get(0).getVerwerkteData());
		// Assertions.assertEquals(statistiekLijst.get(1).getVerwerkteData(),
		// teTestenStatistiekLijst.get(1).getVerwerkteData());
	}

}
