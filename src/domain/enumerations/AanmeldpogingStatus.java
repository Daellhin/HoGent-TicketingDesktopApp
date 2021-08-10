package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum AanmeldpogingStatus {
	Gelukt, Mislukt, Gedeblokkeerd;

	public static HashSet<AanmeldpogingStatus> valueSet() {
		return new HashSet<>(Arrays.asList(AanmeldpogingStatus.values()));
	}
}
