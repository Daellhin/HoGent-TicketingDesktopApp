package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum GebruikersStatus {
	Active, Inactive, Blocked;

	public static HashSet<GebruikersStatus> valueSet() {
		return new HashSet<>(Arrays.asList(GebruikersStatus.values()));
	}
}
