package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum TicketStatus {
	Created, Pending, Finished, Cancelled, AwaitingCustomerInformation, ReceivedCustomerInformation, InDevelopment;

	public static HashSet<TicketStatus> afgehandeldeTickets() {
		return new HashSet<>(Arrays.asList(Finished, Cancelled));
	}

	public static HashSet<TicketStatus> openstaandeTickets() {
		return new HashSet<>(Arrays.asList(Created, Pending, AwaitingCustomerInformation, ReceivedCustomerInformation,
				InDevelopment));
	}

	public static HashSet<TicketStatus> valueSet() {
		return new HashSet<>(Arrays.asList(TicketStatus.values()));
	}
}
