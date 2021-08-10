package testen;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Adres;
import domain.Contactpersoon;
import domain.Klant;

public class KlantTest {

	private static Stream<Arguments> foutieveParameters() {
		return Stream.of(
				// foutieve gebruikersnaam
				Arguments.of(null, Arrays.asList("+32472359285"), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"bedrijfsNaam", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("", Arrays.asList("+32472359285"), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"bedrijfsNaam", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("       ", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				// foutief telefoonnummer
				Arguments.of("gebruikersnaam", null, new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"bedrijfsNaam", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList(""), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"bedrijfsNaam", Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285", "sdfsd"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList("      "),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				// foutief adres
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285"), null, "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				// foutieve bedrijfsnaam
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "         ",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), null,
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				// foutief contactpersoon
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285", "+32472359287"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsnaam", null));
	}

	private static Stream<Arguments> correcteParameters() {
		return Stream.of(
				// foutieve gebruikersnaam
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))),
				Arguments.of("gebruikersnaam", Arrays.asList("+32472359285", "+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "bedrijfsNaam",
						Arrays.asList(new Contactpersoon("test@hotmail.com", "test", "naam"))));
	}

	@ParameterizedTest
	@MethodSource("correcteParameters")
	public void correcteKlantAanmaken_steltAtributenCorrectIn(String gebruikersnaam, List<String> telefoonnummers,
			Adres bedrijfsAdres, String bedrijfsNaam, Collection<Contactpersoon> contactpersonen) {
		Klant klant = new Klant(gebruikersnaam, telefoonnummers, bedrijfsAdres, bedrijfsNaam, contactpersonen);
		Assertions.assertEquals(gebruikersnaam, klant.getGebruikersnaam());
		Assertions.assertEquals(telefoonnummers, klant.getTelefoonnummers());
		Assertions.assertEquals(bedrijfsAdres, klant.getAdres());
		Assertions.assertEquals(bedrijfsNaam, klant.getBedrijfsNaam());
		Assertions.assertEquals(contactpersonen, klant.getContactpersonen());

	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutieveKlantAanmaken_werptException(String gebruikersnaam, List<String> telefoonnummers,
			Adres bedrijfsAdres, String bedrijfsNaam, Collection<Contactpersoon> contactpersonen) {

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Klant(gebruikersnaam, telefoonnummers, bedrijfsAdres, bedrijfsNaam, contactpersonen));
	}

}
