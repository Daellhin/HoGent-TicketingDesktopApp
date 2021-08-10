package testen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Aanmeldpoging;
import domain.enumerations.AanmeldpogingStatus;

public class AanmeldpogingTest {

	private Aanmeldpoging aanmeldpoging;

	private static Stream<Arguments> foutieveParametersAanmeldpoging() {
		return Stream.of(Arguments.of(null, AanmeldpogingStatus.Gelukt), Arguments.of("", AanmeldpogingStatus.Gelukt),
				Arguments.of("       ", AanmeldpogingStatus.Gelukt));
	}

	private static Stream<Arguments> correcteParametersAanmeldpoging() {
		return Stream.of(Arguments.of("Gebruikersnaam", AanmeldpogingStatus.Gelukt),
				Arguments.of("Gebruikersnaam", AanmeldpogingStatus.Mislukt),
				Arguments.of("Gebruikersnaam", AanmeldpogingStatus.Gedeblokkeerd));
	}

	@ParameterizedTest
	@MethodSource("foutieveParametersAanmeldpoging")
	public void incorrecteAanmeldpogingAanmaken(String gebruikersnaam, AanmeldpogingStatus gelukt) {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Aanmeldpoging(gebruikersnaam, AanmeldpogingStatus.Mislukt));
	}

	@ParameterizedTest
	@MethodSource("correcteParametersAanmeldpoging")
	public void correcteAanmeldpogingAanmaken(String gebruikersnaam, AanmeldpogingStatus gelukt) {
		aanmeldpoging = new Aanmeldpoging(gebruikersnaam, gelukt);
		Assertions.assertEquals(gebruikersnaam, aanmeldpoging.getGebruikersnaam());
		Assertions.assertEquals(gelukt, aanmeldpoging.isGelukt());
	}
}
