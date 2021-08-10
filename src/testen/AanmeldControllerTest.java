package testen;

import java.util.Arrays;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Aanmeldpoging;
import domain.Adres;
import domain.Constanten;
import domain.Werknemer;
import domain.controllers.AanmeldController;
import domain.dao.AanmeldpogingDao;
import domain.dao.WerknemerDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;

@ExtendWith(MockitoExtension.class)
public class AanmeldControllerTest {
	@Mock
	private WerknemerDao werknemerDao;
	@Mock
	private AanmeldpogingDao aanmeldpogingDao;
	@InjectMocks
	private AanmeldController controller;
	@Captor
	private ArgumentCaptor<Aanmeldpoging> aanmeldpogingCaptor;

	private Werknemer werknemer;

	// -- Hulpmethoden --
	private void aanmeldenJuistWachtwoordTrainen(String gebruikersnaam, String wachtwoord, GebruikersStatus status,
			int aantalGefaaldeAanmeldPogingen) {
		werknemer = new Werknemer("Administrator", Arrays.asList("+32472643661", "+32472643661"),
				new Adres("BE", "9900", "Eeklo", "Molenstraat", 34), "Clauws", "Robin",
				"robin.clauws@student.hogent.be", WerknemersType.Administrator, Dienst.Admin);

		Mockito.when(werknemerDao.bestaatWerkemer(gebruikersnaam)).thenReturn(status);
		Mockito.lenient().when(werknemerDao.geefWerknemer(gebruikersnaam, wachtwoord)).thenReturn(werknemer);
		Mockito.lenient().when(aanmeldpogingDao.geefAantalGefaaldeAanmeldpogingen(gebruikersnaam))
				.thenReturn(aantalGefaaldeAanmeldPogingen);
	}

	private void aanmeldenFoutWachtwoordTrainen(String gebruikersnaam, String wachtwoord, GebruikersStatus status,
			int aantalGefaaldeAanmeldPogingen) {
		Mockito.when(werknemerDao.bestaatWerkemer(gebruikersnaam)).thenReturn(status);
		Mockito.lenient().when(werknemerDao.geefWerknemer(gebruikersnaam, wachtwoord))
				.thenThrow(NoResultException.class);
		Mockito.lenient().when(aanmeldpogingDao.geefAantalGefaaldeAanmeldpogingen(gebruikersnaam))
				.thenReturn(aantalGefaaldeAanmeldPogingen);

	}
	// -- Hulpmethoden --

	@Test
	public void afmelden_GeenAangemeldeGebruiker_ThrowsException() {
		// Act
		controller.afmelden();

		// Assert
		Assertions.assertNull(controller.getAangemeldeWerknemer());
	}

	@Test
	public void aanmelden_WerknemerBestaatNiet_ThrowsException() {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		Mockito.lenient().when(werknemerDao.bestaatWerkemer(gebruikersnaam)).thenThrow(NoResultException.class);

		// Act + Assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
	}

	@Test
	public void aanmelden_FoutWachtwoord_ThrowsException() {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		int aantalGefaaldeAanmeldPogingen = 0;
		GebruikersStatus status = GebruikersStatus.Active;

		aanmeldenFoutWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act + Assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(werknemerDao).geefWerknemer(gebruikersnaam, wachtwoord);
	}

	@Test
	public void aanmelden_WerknemerActief_MeldAan() {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		int aantalGefaaldeAanmeldPogingen = 0;
		GebruikersStatus status = GebruikersStatus.Active;
		aanmeldenJuistWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act
		controller.aanmelden(gebruikersnaam, wachtwoord);

		// Assert
		Assertions.assertEquals(werknemer, controller.getAangemeldeWerknemer());

		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(werknemerDao).geefWerknemer(gebruikersnaam, wachtwoord);
	}

	@ParameterizedTest
	@EnumSource(value = GebruikersStatus.class, names = { "Inactive", "Blocked" })
	public void aanmelden_WerknemerNietActief_ThrowsException(GebruikersStatus status) {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		int aantalGefaaldeAanmeldPogingen = 0;

		aanmeldenJuistWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act + Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
	}

	@Test()
	public void aanmelden_JuistWachtwoord_MaaktGelukteAanmeldPogingAan() {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		int aantalGefaaldeAanmeldPogingen = 0;
		GebruikersStatus status = GebruikersStatus.Active;

		aanmeldenJuistWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act
		controller.aanmelden(gebruikersnaam, wachtwoord);

		// Assert
		Mockito.verify(aanmeldpogingDao).insert(aanmeldpogingCaptor.capture());
		Aanmeldpoging capturedAanmeldpoging = aanmeldpogingCaptor.getValue();
		Assertions.assertEquals(gebruikersnaam, capturedAanmeldpoging.getGebruikersnaam());
		Assertions.assertEquals(AanmeldpogingStatus.Gelukt, capturedAanmeldpoging.isGelukt());

		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(werknemerDao).geefWerknemer(gebruikersnaam, wachtwoord);
	}

	@Test()
	public void aanmelden_FoutWachtwoord_MaaktGefaaldeAanmeldPogingAan() {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		int aantalGefaaldeAanmeldPogingen = 0;
		GebruikersStatus status = GebruikersStatus.Active;

		aanmeldenFoutWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act + Assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(aanmeldpogingDao).insert(aanmeldpogingCaptor.capture());
		Aanmeldpoging capturedAanmeldpoging = aanmeldpogingCaptor.getValue();
		Assertions.assertEquals(gebruikersnaam, capturedAanmeldpoging.getGebruikersnaam());
		Assertions.assertEquals(AanmeldpogingStatus.Mislukt, capturedAanmeldpoging.isGelukt());

		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(werknemerDao).geefWerknemer(gebruikersnaam, wachtwoord);
		Mockito.verify(aanmeldpogingDao).geefAantalGefaaldeAanmeldpogingen(gebruikersnaam);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, Constanten.MAX_AANTAL_GEFAALDE_AANMELDPOGINGEN - 1 })
	public void aanmelden_FoutWachtwoord_GefaaldeAanmeldpogingenBinnenGrenzen_BlokeertWerknemerNiet(
			int aantalGefaaldeAanmeldPogingen) {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		GebruikersStatus status = GebruikersStatus.Active;

		aanmeldenFoutWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act + Assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(werknemerDao, Mockito.times(0)).blokkeerWerknemer(gebruikersnaam);

		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(aanmeldpogingDao).geefAantalGefaaldeAanmeldpogingen(gebruikersnaam);
	}

	@ParameterizedTest
	@ValueSource(ints = { Constanten.MAX_AANTAL_GEFAALDE_AANMELDPOGINGEN,
			Constanten.MAX_AANTAL_GEFAALDE_AANMELDPOGINGEN + 1 })
	public void aanmelden_FoutWachtwoord_GefaaldeAanmeldpogingenBuitenGrenzen_BlokeertWerknemer(
			int aantalGefaaldeAanmeldPogingen) {
		String gebruikersnaam = "Gebruiker";
		String wachtwoord = "Wachtwoord";
		GebruikersStatus status = GebruikersStatus.Active;

		aanmeldenFoutWachtwoordTrainen(gebruikersnaam, wachtwoord, status, aantalGefaaldeAanmeldPogingen);

		// Act + Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.aanmelden(gebruikersnaam, wachtwoord));

		// Assert
		Mockito.verify(werknemerDao).blokkeerWerknemer(gebruikersnaam);

		Mockito.verify(werknemerDao).bestaatWerkemer(gebruikersnaam);
		Mockito.verify(aanmeldpogingDao).geefAantalGefaaldeAanmeldpogingen(gebruikersnaam);
	}

}
