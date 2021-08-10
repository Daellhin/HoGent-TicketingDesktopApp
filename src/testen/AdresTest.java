package testen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Adres;

public class AdresTest {

	private Adres adres;

	private static Stream<Arguments> incorrecteAdressenOpsommingZonderBusnummer() {
		return Stream.of(
				// Verkeerd land
				Arguments.of("AC", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of("ADE", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of("A", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of("", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of("    ", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of(null, "9900", "Eeklo", "Kaaistraat", 12),
				// Verkeerde postcode
				Arguments.of("AD", "", "Eeklo", "Kaaistraat", 12), Arguments.of("AD", "   ", "Eeklo", "Kaaistraat", 12),
				Arguments.of("AD", null, "Eeklo", "Kaaistraat", 12),
				// Verkeerde woonplaats
				Arguments.of("AD", "9900", "", "Kaaistraat", 12), Arguments.of("AD", "9900", "   ", "Kaaistraat", 12),
				Arguments.of("AD", "9900", null, "Kaaistraat", 12),
				Arguments.of("AD", "9900", "Oost--Eeklo", "Kaaistraat", 12),
				Arguments.of("AD", "9900", "123", "Kaaistraat", 12),
				Arguments.of("AD", "9900", "&dsfd&", "Kaaistraat", 12),
				// Verkeerde straatnaam
				Arguments.of("AD", "9900", "Eeklo", "", 12), Arguments.of("AD", "9900", "Eeklo", "    ", 12),
				Arguments.of("AD", "9900", "Eeklo", null, 12),
				// Verkeerd huisnummer
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 0),
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 10000));
	}

	private static Stream<Arguments> incorrecteAdressenOpsommingMetBusnummer() {
		return Stream.of(
				// Verkeerd land
				Arguments.of("AC", "9900", "Eeklo", "Kaaistraat", 12, ""),
				Arguments.of("ADE", "9900", "Eeklo", "Kaaistraat", 12, "   "),
				Arguments.of("A", "9900", "Eeklo", "Kaaistraat", 12, null));
	}

	private static Stream<Arguments> correcteAdressenZonderBusnummerOpsomming() {
		return Stream.of(Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 12),
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 1),
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 9999));
	}

	private static Stream<Arguments> correcteAdressenMetBusnummerOpsomming() {
		return Stream.of(Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 12, "3"),
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 12, "C"),
				Arguments.of("AD", "9900", "Eeklo", "Kaaistraat", 12, "3A"));
	}

	@ParameterizedTest
	@MethodSource("correcteAdressenZonderBusnummerOpsomming")
	public void correctAdresAanmakenZonderBusNr(String land, String postcode, String woonplaats, String straat,
			int huisnummer) {
		adres = new Adres(land, postcode, woonplaats, straat, huisnummer);
		Assertions.assertEquals(land, adres.getLand());
		Assertions.assertEquals(postcode, adres.getPostcode());
		Assertions.assertEquals(woonplaats, adres.getWoonplaats());
		Assertions.assertEquals(straat, adres.getStraat());
		Assertions.assertEquals(huisnummer, adres.getHuisnummer());
	}

	@ParameterizedTest
	@MethodSource("correcteAdressenMetBusnummerOpsomming")
	public void correctAdresAanmakenMetBusNr(String land, String postcode, String woonplaats, String straat,
			int huisnummer, String busnr) {
		adres = new Adres(land, postcode, woonplaats, straat, huisnummer, busnr);
		Assertions.assertEquals(busnr, adres.getBusnr());
	}

	@ParameterizedTest
	@MethodSource("incorrecteAdressenOpsommingZonderBusnummer")
	public void incorrectAdresAanmakenZonderBusNr(String land, String postcode, String woonplaats, String straat,
			int huisnummer) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Adres(land, postcode, woonplaats, straat, huisnummer));
	}

	@ParameterizedTest
	@MethodSource("incorrecteAdressenOpsommingMetBusnummer")
	public void incorrectAdresAanmakenMetBusNr(String land, String postcode, String woonplaats, String straat,
			int huisnummer, String busnr) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Adres(land, postcode, woonplaats, straat, huisnummer, busnr));
	}
}
