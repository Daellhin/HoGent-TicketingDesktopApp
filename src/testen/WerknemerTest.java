package testen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Adres;
import domain.Werknemer;
import domain.enumerations.Dienst;
import domain.enumerations.WerknemersType;

public class WerknemerTest {
	private static Stream<Arguments> foutieveParameters() {
		return Stream.of(
				// gebruikersnaam, telefoonnummers, adres, naam, voornaam, email, type
				// foutieve gebruikersnaam (null, leeg of korter dan 5 karakters)
				Arguments.of(null, Arrays.asList("+32472359285"), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"Robin", "Clauws", "robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("", Arrays.asList("+32472359285"), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"Robin", "Clauws", "robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("   ", Arrays.asList("+32472359285"), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"Robin", "Clauws", "robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("Robin", Arrays.asList("+32472359285"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				// foutief telefoonnummer (null lijst, lijst met null, leeg, te kort nummer,
				// geen +, te lang nummer
				Arguments.of("RobinClauws", null, new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin",
						"Clauws", "robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList(""), new Adres("BE", "9750", "Zingem", "Beekstraat", 99),
						"Robin", "Clauws", "robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+hello"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+32472"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("32472359825"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+324723u9825"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+324723598256951820"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				// foutief adres (null)
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"), null, "Robin", "Clauws",
						"robin@hogent.be", WerknemersType.Administrator, Dienst.Admin),
				// foutieve voornaam (null of leeg)
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), null, "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "   ", "Clauws", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				// foutieve achternaam(null of leeg)
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", null, "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "   ", "robin@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				// foutief email(Meerdere @, geen @, geen punten, niets voor de @, niets na de
				// @)
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robinhogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogentbe",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "@hogent.be",
						WerknemersType.Administrator, Dienst.Admin),
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@",
						WerknemersType.Administrator, Dienst.Admin),
				// Foutief type
				Arguments.of("RobinClauws", Arrays.asList("+3247235982569518"),
						new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be", null,
						Dienst.Admin));
	}

	private static Stream<Arguments> correcteParameters() {
		return Stream.of(Arguments.of("RobinClauws", Arrays.asList("+32472359285"),
				new Adres("BE", "9750", "Zingem", "Beekstraat", 99), "Robin", "Clauws", "robin@hogent.be",
				WerknemersType.Administrator, Dienst.Admin));
	}

	@ParameterizedTest
	@MethodSource("correcteParameters")
	public void correcteWerknemerAanmaken_steltAtributenCorrectIn(String gebruikersnaam, List<String> telefoonnummers,
			Adres adres, String naam, String voornaam, String email, WerknemersType type, Dienst dienst) {
		Werknemer werknemer = new Werknemer(gebruikersnaam, telefoonnummers, adres, naam, voornaam, email, type,
				dienst);
		Assertions.assertEquals(gebruikersnaam, werknemer.getGebruikersnaam());
		Assertions.assertEquals(telefoonnummers, werknemer.getTelefoonnummers());
		Assertions.assertEquals(adres, werknemer.getAdres());
		Assertions.assertEquals(naam, werknemer.getNaam());
		Assertions.assertEquals(voornaam, werknemer.getVoornaam());
		Assertions.assertEquals(email, werknemer.getEmail());
		Assertions.assertEquals(type, werknemer.getWerknemersType());

	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutieveWerknemerAanmaken_werptException(String gebruikersnaam, List<String> telefoonnummers,
			Adres adres, String naam, String voornaam, String email, WerknemersType type, Dienst dienst) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Werknemer(gebruikersnaam, telefoonnummers, adres, naam, voornaam, email, type, dienst));
	}
}
