package repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import domain.Aanmeldpoging;
import domain.Adres;
import domain.Comment;
import domain.Contactpersoon;
import domain.Contract;
import domain.ContractType;
import domain.Klant;
import domain.Ticket;
import domain.Werknemer;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.ContractTypeStatus;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.StatistiekType;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import domain.enumerations.TicketStatus;
import domain.enumerations.TicketUrgency;
import domain.enumerations.WerknemersType;
import domain.statistieken.Statistiek;
import util.Generator;

public class DataInitializer {

	private WerknemerDaoJpa werknemerDao = new WerknemerDaoJpa();
	private GenericDaoJpa<Klant> klantDao = new GenericDaoJpa<>(Klant.class);
	private TicketDaoJpa ticketDao = new TicketDaoJpa();
	private AanmeldpogingDaoJpa aanmeldpogingDao = new AanmeldpogingDaoJpa();
	private ContractTypeDaoJpa contractTypeDao = new ContractTypeDaoJpa();
	private StatistiekDaoJpa statistiekDao = new StatistiekDaoJpa();

	private boolean fast;
	private int aantalTickets;
	private int aantalTechniekers;
	private int aantalContractTypes;
	private int aantalContracten;
	private Generator g;

	public DataInitializer(boolean fast) {
		this.fast = fast;
		if (fast) {
			aantalTickets = 10;
			aantalTechniekers = 3;
			aantalContractTypes = 2;
			aantalContracten = 4;
		} else {
			aantalTickets = 500;
			aantalTechniekers = 10;
			aantalContractTypes = 6;
			aantalContracten = 7;
		}

		g = new Generator();
	}

	public void run() {
		Instant start = Instant.now();
		System.out.printf("DataInitializer (Fast: %b): startTransaction%n", fast);
		werknemerDao.startTransaction();

		// CONTRACTTYPES
		ContractType contractTypeInactive = new ContractType("ContractType4",
				new HashSet<>(Arrays.asList(TicketAanmaakManier.Application)), TicketAanmaakTijd.WorkingHours, 2,
				ContractDoorlooptijd.OneYear, new BigDecimal(8));
		contractTypeInactive.setStatus(ContractTypeStatus.Inactive);
		contractTypeDao.insert(contractTypeInactive);

		List<ContractType> contractTypes = maakContractTypesAan(aantalContractTypes);

		// WERKNEMERS
		// gui testers
		Werknemer admin = new Werknemer("Administrator", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "Molenstraat", 34), "Clauws", "Robin",
				"robin.clauws@student.hogent.be", WerknemersType.Administrator, Dienst.Admin);
		werknemerDao.insert(admin);

		Werknemer supportmanager = new Werknemer("SupportManager", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9000", "Gent", "Oostveldstraat", 5, "AW"), "Jonckheere", "Jolien",
				"jolien.jonckheere@student.hogent.be", WerknemersType.SupportManager, Dienst.Finance);
		werknemerDao.insert(supportmanager);

		Werknemer technieker = new Werknemer("Technieker", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "1030", "Schaarbeek", "Raamstraat", 58, "2"), "Speybrouck", "Lorin",
				"lorin.speybrouck@student.hogent.be", WerknemersType.Technician, Dienst.IT);
		werknemerDao.insert(technieker);

		// gebruikersStatus testers
		Werknemer adminActief = new Werknemer("Actief", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "Winkelstraat", 34), "Winkelman", "Geoffrey", "geoffrey@hotmail.com",
				WerknemersType.Administrator, Dienst.Admin);
		werknemerDao.insert(adminActief);

		Werknemer supportmanagerGeblokeerd = new Werknemer("Geblokkeerd", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "Stationstraat", 49), "De Coeyer", "Anja", "anja@hotmail.com",
				WerknemersType.SupportManager, Dienst.HumanResource);
		supportmanagerGeblokeerd.setStatus(GebruikersStatus.Blocked);
		werknemerDao.insert(supportmanagerGeblokeerd);

		Werknemer techniekerNietActief = new Werknemer("NietActief", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "KaaiStraat", 21, "2"), "Dhondt", "Dominique", "dominique@hotmail.com",
				WerknemersType.Technician, Dienst.IT);
		techniekerNietActief.setStatus(GebruikersStatus.Inactive);
		werknemerDao.insert(techniekerNietActief);

		// techniekers
		List<Werknemer> techniekers = maakWerknemersAan(WerknemersType.Technician, aantalTechniekers);
		techniekers.add(technieker);

		// KLANTEN
		List<Klant> klanten = new ArrayList<>();
		Klant klant1 = new Klant("B-Temium", Arrays.asList("+32472643661"),
				new Adres("BE", "9000", "Gent", "Straattraat", 5, "A"), "B-Temium Inc.",
				Arrays.asList(new Contactpersoon("bee.temium@btemium.com", "Bee", "Temium")));
		klantDao.insert(klant1);
		klanten.add(klant1);

		Klant klant2 = new Klant("LotusKoekjes", Arrays.asList("+32472643661", "+32479858753"),
				new Adres("BE", "9971", "Lembeke", "Gentstraat", 1), "Lotus",
				Arrays.asList(new Contactpersoon("jan.boone@lotus.com", "Jan", "Boone"),
						new Contactpersoon("emiel.boon@lotus.com", "Emiel", "Boone"),
						new Contactpersoon("henri.boone@lotus.com", "Henri ", "Boone")));
		klantDao.insert(klant2);
		klanten.add(klant2);

		Klant klant3 = new Klant("Father Christmas", Arrays.asList("+32472643661", "+32472643669"),
				new Adres("US", "99705", "Alaska", "Santa Claus Lane", 1, "4"), "JingleBells Inc.",
				Arrays.asList(new Contactpersoon("santaswife@hotmail.com", "Ms", "Santa"),
						new Contactpersoon("rudolphtherednosedreindeer@hotmail.com", "Rudolph", "Rednose")));
		klant3.setStatus(GebruikersStatus.Inactive);
		klantDao.insert(klant3);
		klanten.add(klant3);

		Klant klant4 = new Klant("Belfius", Arrays.asList("+32213548246"),
				new Adres("BE", "1210", "Sint-Joost-ten-Node", "Karel Rogierplein", 11), "Belfius",
				Arrays.asList(new Contactpersoon("marc.raisiere@belfius.com", "Marc", "Raisiere")));
		klantDao.insert(klant4);
		klanten.add(klant4);

		Klant klant5 = new Klant("JanDeNul", Arrays.asList("+32249217319", "+32964381272"),
				new Adres("BE", "9308", "Aalst", "Tragel", 60), "Jan De Nul Group",
				Arrays.asList(new Contactpersoon("dirk.denul@hotmail.com", "Dirk", "De Nul"),
						new Contactpersoon("jan.denul@hotmail.com", "Jan", "De Nul")));
		klantDao.insert(klant5);
		klanten.add(klant5);

		Klant klant6 = new Klant("Krëfel", Arrays.asList("+32485137546"),
				new Adres("BE", "1851", "Grimbergen", "Steenstraat", 44), "Krëfel",
				Arrays.asList(new Contactpersoon("auguste.marcel.poulet@krefel.com", "Auguste Marcel", "Poulet")));
		klantDao.insert(klant6);
		klanten.add(klant6);

		Klant klant7 = new Klant("OmegaPharma", Arrays.asList("+32947251683", "+32248135761"),
				new Adres("BE", "9810", "Nazareth", "Venecoweg", 26), "Omega Pharma",
				Arrays.asList(new Contactpersoon("marc.coucke@omega.com", " Marc", "Coucke"),
						new Contactpersoon("yvan.vindevogel@omega.com", "Yvan", "Vindevogel")));
		klantDao.insert(klant7);
		klanten.add(klant7);

		// CONTRACTEN
		new Contract(contractTypes.get(0), ContractStatus.Cancelled, LocalDate.now(), LocalDate.now().plusDays(400),
				klant1);

		new Contract(contractTypes.get(1), ContractStatus.Pending, LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(380), klant2);

		new Contract(contractTypes.get(1), ContractStatus.Finished, LocalDate.now().plusDays(6),
				LocalDate.now().plusDays(450), klant3);

		List<Contract> contracten = maakActieveContractenAan(contractTypes, aantalContracten, klanten);

		// TICKETS
		maakTicketsAan(aantalTickets, techniekers, contracten);

		// AANMELDPOGINGEN
		for (int i = 0; i < 3; i++) {
			aanmeldpogingDao.insert(new Aanmeldpoging("GeryEelbode", AanmeldpogingStatus.Gelukt));
		}
		for (int i = 0; i < 3; i++) {
			aanmeldpogingDao.insert(new Aanmeldpoging("AnnMechelink", AanmeldpogingStatus.Mislukt));
		}

		// TRACKED STATISTIEKEN
		statistiekDao.insert(new Statistiek<Ticket>(StatistiekType.AverageDurationTicketsPerContractType));
		statistiekDao.insert(new Statistiek<Ticket>(StatistiekType.AmountOfFinishedTicketsPerCustomer));

		System.out.printf("DataInitializer (Fast: %b): commitTransaction%n", fast);
		werknemerDao.commitTransaction();

		Instant end = Instant.now();
		System.out.printf("DataInitializer (Fast: %b): finished, took %d sec%n", fast,
				Duration.between(start, end).toSeconds());
	}

	private List<ContractType> maakContractTypesAan(int aantal) {
		List<ContractType> contractTypes = new ArrayList<>();

		for (int i = 0; i < aantal; i++) {
			ContractType c = new ContractType("ContractType" + i,
					new HashSet<>(g.randomElements(TicketAanmaakManier.valueSet())),
					g.randomElement(TicketAanmaakTijd.valueSet()), 1, g.randomElement(ContractDoorlooptijd.valueSet()),
					new BigDecimal(g.randomInt(1, 7)));
			contractTypeDao.insert(c);
			contractTypes.add(c);
		}
		return contractTypes;
	}

	private List<Werknemer> maakWerknemersAan(WerknemersType type, int aantal) {
		List<Werknemer> werknemers = new ArrayList<>();

		for (int i = 0; i < aantal; i++) {
			String firstName = g.firstName();
			String lastName = g.lastName();
			Werknemer w = new Werknemer(firstName + lastName, Arrays.asList(g.telephone(), g.telephone()),
					new Adres("BE", "9900", "Eeklo", "Veldstraat", 68), firstName, lastName, firstName + "@hotmail.com",
					type, g.randomElement(Dienst.valueSet()));
			werknemerDao.insert(w);
			werknemers.add(w);
		}
		return werknemers;
	}

	private List<Contract> maakActieveContractenAan(List<ContractType> contractTypes, int aantal, List<Klant> klanten) {
		List<Contract> contracten = new ArrayList<>();

		for (int i = 0; i < aantal; i++) {
			contracten.add(new Contract(g.randomElement(contractTypes), ContractStatus.Active,
					LocalDate.now().plusDays(g.randomInt(1, 100)), LocalDate.now().plusDays(470 + g.randomInt(0, 300)),
					g.randomElement(klanten)));
		}
		return contracten;
	}

	private List<Ticket> maakTicketsAan(int aantal, List<Werknemer> techiekers, List<Contract> contracten) {
		List<Ticket> tickets = new ArrayList<>();

		for (int i = 0; i < aantal; i++) {
			Contract contract = g.randomElement(contracten);
			Ticket t = new Ticket("Title " + i, g.randomElement(TicketUrgency.valueSet()), "omschrijving " + i,
					Arrays.asList("www.bijlageO.com", "www.bijlageP.com"), contract,
					g.randomElement(Dienst.valueSet()));

			Werknemer technieker = g.randomElement(techiekers);
			t.setToegewezenTechnieker(technieker);
			t.setStatus(g.randomElement(TicketStatus.valueSet(), 0.5, TicketStatus.Finished));
			if (t.isAfgehandeld()) {
				t.setDatumAfgehandeld(LocalDateTime.now().plusHours(g.randomInt(12, 70)));
			}

			t.setOpmerkingen(Arrays.asList(new Comment("opmerkingx", technieker.getGebruikersnaam(), t),
					new Comment("opmerkingy", contract.getKlant().getGebruikersnaam(), t),
					new Comment("opmerkingy", technieker.getGebruikersnaam(), t)));
			ticketDao.insert(t);
			tickets.add(t);
		}
		return tickets;
	}

}
