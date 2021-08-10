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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Adres;
import domain.Comment;
import domain.Contactpersoon;
import domain.Contract;
import domain.ContractType;
import domain.Klant;
import domain.Ticket;
import domain.Werknemer;
import domain.beheerders.TicketBeheerder;
import domain.dao.TicketDao;
import domain.dao.WerknemerDao;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.Dienst;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import domain.enumerations.TicketStatus;
import domain.enumerations.TicketUrgency;
import domain.enumerations.WerknemersType;

@ExtendWith(MockitoExtension.class)
public class TicketBeheerderTest {
	@Mock
	private TicketDao ticketDao;
	@Mock
	private WerknemerDao werknemerDao;
	@InjectMocks
	private TicketBeheerder ticketBeheerder;

	@Captor
	private ArgumentCaptor<Ticket> klantCaptor;

	private List<Ticket> lijstTickets;
	private Klant klant;
	private Contract contract;
	private Werknemer technieker;
	private Werknemer supportmanager;

	// -- Hulpmethoden --
	private void maakTicketsAan() {
		lijstTickets = new ArrayList<>();

		ContractType contractType1 = new ContractType("ContractType1",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.AllTime, 1,
				ContractDoorlooptijd.OneYear, new BigDecimal(1));

		klant = new Klant("B-Temium", Arrays.asList("+32472643661"), new Adres("BE", "9000", "Gent", "Straattraat", 5),
				"B-Temium Inc.", Arrays.asList(new Contactpersoon("email@btemium.com", "Bee", "Temium")));

		technieker = new Werknemer("GeryEelbode", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "Veldstraat", 68), "Eelbode", "Gery", "gery@hotmail.com",
				WerknemersType.Technician, Dienst.OperationsManagement);

		supportmanager = new Werknemer("SupportManager", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9000", "Gent", "Oostveldstraat", 5), "Jonckheere", "Jolien",
				"jolien.jonckheere@student.hogent.be", WerknemersType.SupportManager, Dienst.Finance);

		contract = new Contract(contractType1, ContractStatus.Active, LocalDate.now(), LocalDate.now().plusDays(400),
				klant);

		// status = pending
		Ticket ticket1 = new Ticket("title 1", TicketUrgency.NoProductionImpact, "omschrijving 1",
				Arrays.asList("www.bijlageA.com", "www.bijlageB.com"), contract, Dienst.Finance);

		// status = pending
		Ticket ticket2 = new Ticket("title 2", TicketUrgency.ProductionImpacted, "omschrijving 2",
				Arrays.asList("www.bijlageC.com", "www.bijlageD.com"), contract, Dienst.HumanResource);
		ticket2.setOpmerkingen(Arrays.asList(new Comment("opmerkingx", technieker.getGebruikersnaam(), ticket2),
				new Comment("opmerkingy", contract.getKlant().getGebruikersnaam(), ticket2),
				new Comment("opmerkingy", technieker.getGebruikersnaam(), ticket2)));

		Ticket ticket3 = new Ticket("title 3", TicketUrgency.ProductionWillBeImpacted, "omschrijving 3",
				Arrays.asList("www.bijlageE.com", "www.bijlageF.com"), contract, Dienst.Marketing);
		ticket3.setStatus(TicketStatus.Created);

		Ticket ticket4 = new Ticket("probleem", TicketUrgency.NoProductionImpact, "omschrijving 4",
				Arrays.asList("www.bijlageG.com", "www.bijlageH.com"), contract, Dienst.IT);
		ticket4.setStatus(TicketStatus.InDevelopment);

		lijstTickets.add(ticket1);
		lijstTickets.add(ticket2);
		lijstTickets.add(ticket3);
		lijstTickets.add(ticket4);
	}

	private static Stream<Arguments> nullEnLegeFilterParameters() {
		//@formatter:off
		return Stream.of(
				Arguments.of(0, null, null, null), 
				Arguments.of(-1, null, null, null),
				Arguments.of(0, "", null, null),
				Arguments.of(0, null, new HashSet<>(), null),
				Arguments.of(0, null, null, new HashSet<>())
				);
		//@formatter:on
	}

	private static Stream<Arguments> normaleFilters() {
		// filters meegeven en lijst van verwachte resultaten
		// @formatter:off
		return Stream.of(
				// filter input voor title
				Arguments.of(0, "title", null, null, 3),
				Arguments.of(0, "probl", null, null, 1),
				// filter input voor ticketstatus
				Arguments.of(0, null, new HashSet<>(Arrays.asList(TicketStatus.Created)), null, 3),
				Arguments.of(0, "", TicketStatus.afgehandeldeTickets(), null, 0),
				Arguments.of(0, "", TicketStatus.openstaandeTickets(), null, 4),
				// filter input voor tickettype
				Arguments.of(0, "", null, new HashSet<>(Arrays.asList(TicketUrgency.NoProductionImpact)), 2),
				Arguments.of(0, "", null, new HashSet<>(Arrays.asList(TicketUrgency.ProductionImpacted)), 1)
				);
		// @formatter:on
	}
	// -- Hulpmethoden --

	@BeforeEach
	public void beforeEach() {
		maakTicketsAan();
		Mockito.lenient().when(ticketDao.findAll()).thenReturn(lijstTickets);
		ticketBeheerder.setAangemeldeWerknemer(supportmanager);
	}

	@Test
	public void newTicketBeheerder_WerknemerTechnieker_GeeftTicketsVanTechnieker() {
		// Act
		ticketBeheerder.setAangemeldeWerknemer(technieker);
		ticketBeheerder.getTicketList();

		// Assert
		Mockito.verify(ticketDao).geefTicketsVanTechnieker(technieker);
	}

	@Test
	public void newTicketBeheerder_WerknemerGeenTechnieker_GeeftAlleTickets() {
		// Act
		ticketBeheerder.getTicketList();

		// Assert
		Mockito.verify(ticketDao).findAll();
	}

	@Test
	public void voegTicketToe_InsertsTicket() {
		// Act
		ticketBeheerder.getTicketList();
		Ticket ticket = new Ticket("title 1", TicketUrgency.NoProductionImpact, "omschrijving 1",
				Arrays.asList("www.bijlageA.com", "www.bijlageB.com"), contract, Dienst.Finance);
		ticketBeheerder.voegTicketToe(ticket);

		// Assert
		Mockito.verify(ticketDao).insert(ticket);
		Mockito.verify(ticketDao, Mockito.times(2)).findAll();
	}

	@Test
	public void pasTicketAan_UpdatesTicket() {
		// Act
		Ticket ticket = ticketBeheerder.getTicketList().get(0);
		ticket.setToegewezenTechnieker(technieker);
		ticketBeheerder.pasTicketAan(ticket);

		// Assert
		Mockito.verify(ticketDao).update(ticket);
		Mockito.verify(ticketDao, Mockito.times(2)).findAll();
	}

	@ParameterizedTest
	@MethodSource("nullEnLegeFilterParameters")
	public void pasFilterAan_LegeParameters_GeeftAlles(int ticketnummer, String titel, Set<TicketStatus> status,
			Set<TicketUrgency> type) {
		// Act
		ticketBeheerder.getTicketList();
		ticketBeheerder.pasFilterAan(ticketnummer, titel, status, type);

		// Assert
		Assertions.assertEquals(lijstTickets.size(), ticketBeheerder.getTicketList().size());
		Mockito.verify(ticketDao).findAll();
	}

	@Test
	public void pasFilterAan_AlleStatusenGeselecteerd_GeeftAlles() {
		// Act
		ticketBeheerder.getTicketList();
		ticketBeheerder.pasFilterAan(-1, null, TicketStatus.valueSet(), TicketUrgency.valueSet());

		// Assert
		Assertions.assertEquals(lijstTickets.size(), ticketBeheerder.getTicketList().size());
		Mockito.verify(ticketDao).findAll();
	}

	@ParameterizedTest
	@MethodSource("normaleFilters")
	public void pasFilterAan_NormaleParameters_FiltertCorrect(int ticketnummer, String titel, Set<TicketStatus> status,
			Set<TicketUrgency> type, int expectedSize) {
		// Act
		ticketBeheerder.getTicketList();
		ticketBeheerder.pasFilterAan(ticketnummer, titel, status, type);

		// Assert
		Assertions.assertEquals(expectedSize, ticketBeheerder.getTicketList().size());
		Mockito.verify(ticketDao).findAll();
	}

}
