package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum TicketUrgency {
	ProductionImpacted, ProductionWillBeImpacted, NoProductionImpact;

	public static HashSet<TicketUrgency> valueSet() {
		return new HashSet<>(Arrays.asList(TicketUrgency.values()));
	}

}
