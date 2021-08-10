package testen.beheerders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import domain.Aanmeldpoging;
import domain.Adres;
import domain.Contactpersoon;
import domain.Klant;
import domain.beheerders.KlantBeheerder;
import domain.dao.AanmeldpogingDao;
import domain.dao.KlantDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.GebruikersStatus;

@ExtendWith(MockitoExtension.class)
public class KlantBeheerderTest {
	@Mock
	private KlantDao klantDao;
	@Mock
	private AanmeldpogingDao aanmeldpogingDao;
	@InjectMocks
	private KlantBeheerder klantenBeheerder;

	@Captor
	private ArgumentCaptor<Aanmeldpoging> aanmeldpogingCaptor;

	private List<Klant> lijstKlanten;

	// -- Hulpmethoden --
	private void maakKlantenAan() {
		lijstKlanten = new ArrayList<>();

		Klant klant1 = new Klant("B-Temium", Arrays.asList("+32472643661"),
				new Adres("BE", "9000", "Gent", "Straattraat", 5), "B-Temium Inc.",
				Arrays.asList(new Contactpersoon("email@btemium.com", "Bee", "Temium")));

		Klant klant2 = new Klant("LotusKoekjes", Arrays.asList("+32472643661", "+32479858753"),
				new Adres("BE", "9000", "Gent", "Gentstraat", 5), "Lotus",
				Arrays.asList(new Contactpersoon("man@lotus.com", "Mr", "Lotus"),
						new Contactpersoon("vrouw@lotus.com", "Mrs", "Lotus")));
		klant2.setStatus(GebruikersStatus.Blocked);

		Klant klant3 = new Klant("Father Christmas", Arrays.asList("+32472643661", "+32472643669"),
				new Adres("NO", "H0H0H0", "Santa's Village", "ChristmasStreet", 1), "JingleBells Inc.",
				Arrays.asList(new Contactpersoon("santaswife@hotmail.com", "Ms", "Santa"),
						new Contactpersoon("rudolphtherednosedreindeer@hotmail.com", "Rudolph", "Rednose")));
		klant3.setStatus(GebruikersStatus.Inactive);

		lijstKlanten.add(klant1);
		lijstKlanten.add(klant2);
		lijstKlanten.add(klant3);
	}

	private static Stream<Arguments> nullEnLegeFilterParameters() {
		//@formatter:off
		return Stream.of(
				Arguments.of(0, null, null, null), 
				Arguments.of(0, null, null, new HashSet<>()),
				Arguments.of(-1, null, null, null),
				Arguments.of(0, "", null, null),
				Arguments.of(0, null, "", null)
				);
		//@formatter:on
	}

	private static Stream<Arguments> normaleFilters() {
		// filters meegeven en lijst van verwachte resultaten
		//@formatter:off
		return Stream.of(
				Arguments.of(0, "Koekje", null, null, 1),
				Arguments.of(0, "Temium", null, null, 1),
				Arguments.of(0, "s", null, null, 2),
				Arguments.of(0, null, "Inc.", null, 2),
				Arguments.of(0, null, "", new HashSet<>(Arrays.asList(GebruikersStatus.Active)), 1),
				Arguments.of(0, null, "", new HashSet<>(Arrays.asList(GebruikersStatus.Blocked)), 1)
				);
		//@formatter:on
	}
	// -- Hulpmethoden --

	@BeforeEach
	public void beforeEach() {
		maakKlantenAan();
		Mockito.lenient().when(klantDao.findAll()).thenReturn(lijstKlanten);
	}

	@Test
	public void newKlantBeheerder_GeeftActieveGebruikers() {
		// Act
		Collection<Klant> klanten = klantenBeheerder.getKlantList();

		// Assert
		Assertions.assertEquals(1, klanten.size());
		klanten.forEach(klant -> {
			Assertions.assertEquals(GebruikersStatus.Active, klant.getStatus());
		});

		Mockito.verify(klantDao).findAll();
	}

	@Test
	public void voegKlantToe_InsertsKlant() {
		// Act
		klantenBeheerder.getKlantList();
		Klant klant = new Klant("A-Temium", Arrays.asList("+32472643662"), new Adres("BE", "9000", "Gent", "Straat", 5),
				"A-Temium Inc.", Arrays.asList(new Contactpersoon("email@atemium.com", "Aah", "Temium")));
		klantenBeheerder.voegKlantToe(klant);

		// Assert
		Mockito.verify(klantDao).insert(klant);
		Mockito.verify(klantDao, Mockito.times(2)).findAll();
	}

	@Test
	public void pasKlantAan_UpdatesKlant() {
		// Act
		Klant klant = klantenBeheerder.getKlantList().get(0);
		klant.setBedrijfsNaam("Nieuwe bedrijfsnaam");
		klantenBeheerder.pasKlantAan(klant);

		// Assert
		Mockito.verify(klantDao).update(klant);
		Mockito.verify(klantDao, Mockito.times(2)).findAll();
	}

	@ParameterizedTest
	@MethodSource("nullEnLegeFilterParameters")
	public void pasFilterAan_LegeParameters_GeeftAlles(int klantnummer, String gebruikersnaam, String bedrijfsnaam,
			Set<GebruikersStatus> status) {
		// Act
		klantenBeheerder.getKlantList();
		klantenBeheerder.pasFilterAan(klantnummer, gebruikersnaam, bedrijfsnaam, status);

		// Assert
		Assertions.assertEquals(lijstKlanten.size(), klantenBeheerder.getKlantList().size());
		Mockito.verify(klantDao).findAll();
	}

	@Test
	public void pasFilterAan_AlleStatusenGeselecteerd_GeeftAlles() {
		// Act
		klantenBeheerder.getKlantList();
		klantenBeheerder.pasFilterAan(0, null, null, GebruikersStatus.valueSet());

		// Assert
		Assertions.assertEquals(lijstKlanten.size(), klantenBeheerder.getKlantList().size());
		Mockito.verify(klantDao).findAll();
	}

	@ParameterizedTest
	@MethodSource("normaleFilters")
	public void pasFilterAan_NormaleParameters_FiltertCorrect(int klantnummer, String gebruikersnaam,
			String bedrijfsnaam, Set<GebruikersStatus> status, int expectedSize) {
		// Act
		klantenBeheerder.getKlantList();
		klantenBeheerder.pasFilterAan(klantnummer, gebruikersnaam, bedrijfsnaam, status);

		// Assert
		Assertions.assertEquals(expectedSize, klantenBeheerder.getKlantList().size());
		Mockito.verify(klantDao).findAll();
	}

	@Test
	public void deblokkeerKlant_NewGedeblokeerdeAanmeldpoging() {
		String gebruikersnaam = "B-Temium";

		// Act
		klantenBeheerder.deblokkeerKlant(gebruikersnaam);

		// Assert
		Mockito.verify(aanmeldpogingDao).insert(aanmeldpogingCaptor.capture());
		Aanmeldpoging capturedAanmeldpoging = aanmeldpogingCaptor.getValue();
		Assertions.assertEquals(gebruikersnaam, capturedAanmeldpoging.getGebruikersnaam());
		Assertions.assertEquals(AanmeldpogingStatus.Gedeblokkeerd, capturedAanmeldpoging.isGelukt());
	}

}
