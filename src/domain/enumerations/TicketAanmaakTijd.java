package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum TicketAanmaakTijd {
	AllTime, WorkingHours;

	public static HashSet<TicketAanmaakTijd> valueSet() {
		return new HashSet<>(Arrays.asList(TicketAanmaakTijd.values()));
	}
}
