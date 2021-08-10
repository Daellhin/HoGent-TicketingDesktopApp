package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum ApplicatieFunctie {
	Dashboard, ManageCustomers, ManageEmployees, ManageContractTypes, NewTicket, TicketsInProgress, FinishedTickets,
	KnowledgeBase, Statistics, KPI;

	public static HashSet<ApplicatieFunctie> valueSet() {
		return new HashSet<>(Arrays.asList(ApplicatieFunctie.values()));
	}

}
