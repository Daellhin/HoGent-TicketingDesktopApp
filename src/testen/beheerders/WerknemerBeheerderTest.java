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
import domain.Werknemer;
import domain.beheerders.WerknemerBeheerder;
import domain.dao.AanmeldpogingDao;
import domain.dao.WerknemerDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;

@ExtendWith(MockitoExtension.class)
public class WerknemerBeheerderTest {
	@Mock
	private WerknemerDao werknemerDao;
	@Mock
	private AanmeldpogingDao aanmeldpogingDao;
	@InjectMocks
	private WerknemerBeheerder werknemerBeheerder;

	@Captor
	private ArgumentCaptor<Aanmeldpoging> aanmeldpogingCaptor;

	private List<Werknemer> lijstWerknemers;

	// -- Hulpmethoden --
	private void maakWerknemersAan() {
		lijstWerknemers = new ArrayList<Werknemer>();

		Werknemer werknemer1 = new Werknemer("RobinClauws", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Clauws", "Robin", "robin@hogent.be",
				WerknemersType.Administrator, Dienst.Admin);
		Werknemer werknemer2 = new Werknemer("LorinSpeybrouck", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Speybrouck", "Lorin", "lorin@hogent.be",
				WerknemersType.Technician, Dienst.Admin);
		Werknemer werknemer3 = new Werknemer("JolienJonckheere", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Jonckheere", "Jolien", "jolien@hogent.be",
				WerknemersType.SupportManager, Dienst.Admin);
		werknemer2.setStatus(GebruikersStatus.Inactive);

		lijstWerknemers.add(werknemer1);
		lijstWerknemers.add(werknemer2);
		lijstWerknemers.add(werknemer3);
	}

	private static Stream<Arguments> nullEnLegeFilterParameters() {
		//@formatter:off
		return Stream.of(
				Arguments.of(0, "", "", "", null, null), 
				Arguments.of(0, null, null, null, null, new HashSet<>()),
				Arguments.of(-1, null, null, null, null, null),
				Arguments.of(0, "", "", "", new HashSet<>(), new HashSet<>())
				);
		//@formatter:on
	}

	private static Stream<Arguments> normaleFilters() {
		// filters meegeven en lijst van verwachte resultaten
		//@formatter:off
			return Stream.of(
					Arguments.of(0, "Robin", null, null, null, null, 1),
					Arguments.of(0, "Lorin", null, null, null, null, 1),
					Arguments.of(0, "s", null, null, null, null, 2),
					Arguments.of(0, null, "ck", null, null, null,2),
					Arguments.of(0, null, null, null, null, null,3),
					Arguments.of(0, null, null, "in", null, null,2),
					Arguments.of(0, "", "", "",  new HashSet<>(Arrays.asList(GebruikersStatus.Active)), null, 2),
					Arguments.of(0, "", "", "", new HashSet<>(Arrays.asList(GebruikersStatus.Inactive)), null,1),
					Arguments.of(0, "", "", "", null, new HashSet<>(Arrays.asList(WerknemersType.Technician)), 1)
					);
		//@formatter:on
	}
	// -- Hulpmethoden --

	@BeforeEach
	public void beforeEach() {
		maakWerknemersAan();
		Mockito.lenient().when(werknemerDao.findAll()).thenReturn(lijstWerknemers);
	}

	@Test
	public void newWerknemerBeheerder_GeeftActieveGebruikers() {
		// Act
		Collection<Werknemer> werknemers = werknemerBeheerder.getWerknemerList();

		// Assert
		Assertions.assertEquals(2, werknemers.size());
		werknemers.forEach(werknemer -> Assertions.assertEquals(GebruikersStatus.Active, werknemer.getStatus()));
		Mockito.verify(werknemerDao).findAll();
	}

	@Test
	public void voegWerknemerToe_InsertsWerknemer() {
		// Act
		werknemerBeheerder.getWerknemerList();
		Werknemer werknemer = new Werknemer("JolienJonckheere", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Jolien", "Jonckheere", "jolien@hogent.be",
				WerknemersType.SupportManager, Dienst.Admin);
		werknemerBeheerder.voegWerknemerToe(werknemer);

		// Assert
		Mockito.verify(werknemerDao).insert(werknemer);
		Mockito.verify(werknemerDao, Mockito.times(2)).findAll();
	}

	@Test
	public void pasWerknemerAan_UpdatesWerknemer() {
		// Act
		Werknemer werknemer = werknemerBeheerder.getWerknemerList().get(0);
		werknemer.setStatus(GebruikersStatus.Blocked);
		werknemerBeheerder.pasWerknemerAan(werknemer);

		// Assert
		Mockito.verify(werknemerDao).update(werknemer);
		Mockito.verify(werknemerDao, Mockito.times(2)).findAll();
	}

	@ParameterizedTest
	@MethodSource("nullEnLegeFilterParameters")
	// een keer nulls en een keer lege objecten
	public void pasFilterAan_LegeParameters_GeeftAlles(int werknemernummer, String gebruikersnaam, String naam,
			String voornaam, Set<GebruikersStatus> status, Set<WerknemersType> rol) {
		// Act
		werknemerBeheerder.getWerknemerList();
		werknemerBeheerder.pasFilterAan(werknemernummer, gebruikersnaam, naam, voornaam, status, rol);

		// Assert
		Assertions.assertEquals(werknemerBeheerder.getWerknemerList().size(), lijstWerknemers.size());
		Mockito.verify(werknemerDao).findAll();
	}

	@Test
	public void pasFilterAan_AlleStatussenGeselecteerd_AndereParametersNull_GeeftAlles() {
		// Act
		werknemerBeheerder.getWerknemerList();
		werknemerBeheerder.pasFilterAan(0, null, null, null, GebruikersStatus.valueSet(), null);

		// Assert
		Assertions.assertEquals(lijstWerknemers.size(), werknemerBeheerder.getWerknemerList().size());
		Mockito.verify(werknemerDao).findAll();
	}

	@Test
	public void pasFilterAan_AlleRollenGeselecteerd_AndereParametersNull_GeeftAlles() {
		// Act
		werknemerBeheerder.getWerknemerList();
		werknemerBeheerder.pasFilterAan(0, null, null, null, null,
				new HashSet<>(Arrays.asList(WerknemersType.values())));

		// Assert
		Assertions.assertEquals(lijstWerknemers.size(), werknemerBeheerder.getWerknemerList().size());
		Mockito.verify(werknemerDao).findAll();
	}

	@ParameterizedTest
	@MethodSource("normaleFilters")
	public void pasFilterAan_NormaleParameters_FiltertCorrect(int werknemernummer, String gebruikersnaam, String naam,
			String voornaam, Set<GebruikersStatus> status, Set<WerknemersType> rol, int expectedSize) {
		// Act
		werknemerBeheerder.getWerknemerList();
		werknemerBeheerder.pasFilterAan(werknemernummer, gebruikersnaam, naam, voornaam, status, rol);

		// Assert
		Assertions.assertEquals(expectedSize, werknemerBeheerder.getWerknemerList().size());
		Mockito.verify(werknemerDao).findAll();
	}

	@Test
	public void deblokkeerWerknemer_NewGedeblokeerdeAanmeldpoging() {
		String gebruikersnaam = "Koekje";

		// Act
		werknemerBeheerder.deblokkeerWerknemer(gebruikersnaam);

		// Assert
		Mockito.verify(aanmeldpogingDao).insert(aanmeldpogingCaptor.capture());
		Aanmeldpoging capturedAanmeldpoging = aanmeldpogingCaptor.getValue();
		Assertions.assertEquals(gebruikersnaam, capturedAanmeldpoging.getGebruikersnaam());
		Assertions.assertEquals(AanmeldpogingStatus.Gedeblokkeerd, capturedAanmeldpoging.isGelukt());
	}

}
