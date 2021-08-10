package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum TicketAanmaakManier {
	Email, Telephone, Application;

	public static HashSet<TicketAanmaakManier> valueSet() {
		return new HashSet<>(Arrays.asList(TicketAanmaakManier.values()));
	}

}
