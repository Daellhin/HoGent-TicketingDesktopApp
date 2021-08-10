package testen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Contactpersoon;

public class ContactpersoonTest {

	private static Stream<Arguments> foutieveParameters() {
		return Stream.of(
				// verkeerd emailadres
				Arguments.of(".username@yahoo.com", "Joery", "Labie"),
				Arguments.of("username@yahoo.com.", "Joery", "Labie"),
				Arguments.of("username@yahoo..com", "Joery", "Labie"),
				Arguments.of("username@yahoo.c", "Joery", "Labie"),
				Arguments.of("username@yahoo.corporate", "Joery", "Labie"),
				// verkeerde naam
				Arguments.of("user@domain.com", null, "Labie"), Arguments.of("user@domain.com", "", "Labie"),
				Arguments.of("user@domain.com", "      ", "Labie"),
				// verkeerde voornaam
				Arguments.of("user@domain.com", "Joery", null), Arguments.of("user@domain.com", "Joery", ""),
				Arguments.of("user@domain.com", "Joery", "      "));
	}

	@ParameterizedTest
	@CsvSource(value = { "user@domain.com,gebruiker,test", "user@domain.co.in,joery,labie",
			"user.name@domain.com,Lorin,Speybrouck", "user_name@domain.com,Jolien,Jonckheere",
			"username@yahoo.corporate.in,Bryan,Decabooter" }, delimiter = ',')
	public void correcteContactpersoonAanmaken(String email, String naam, String voornaam) {
		Contactpersoon cPersoon = new Contactpersoon(email, naam, voornaam);
		Assertions.assertEquals(email, cPersoon.getEmail());
		Assertions.assertEquals(naam, cPersoon.getNaam());
		Assertions.assertEquals(voornaam, cPersoon.getVoornaam());
	}

	@ParameterizedTest
	@MethodSource("foutieveParameters")
	public void foutieveContactPersoonAanmaken(String email, String naam, String voornaam) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Contactpersoon(email, naam, voornaam));
	}

}
