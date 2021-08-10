package domain.enumerations;

import static domain.enumerations.ApplicatieFunctie.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum WerknemersType {

	Administrator(new HashSet<>(Arrays.asList(Dashboard, ManageCustomers, ManageEmployees))),
	SupportManager(new HashSet<>(Arrays.asList(Dashboard, ManageContractTypes, NewTicket, TicketsInProgress,
			FinishedTickets, KnowledgeBase, Statistics, KPI))),
	Technician(new HashSet<>(Arrays.asList(Dashboard, TicketsInProgress, FinishedTickets, KnowledgeBase, Statistics)));

	private Set<ApplicatieFunctie> functies;

	private WerknemersType(Set<ApplicatieFunctie> functies) {
		this.functies = functies;
	}

	public Set<ApplicatieFunctie> getFuncties() {
		return functies;
	}

	public static HashSet<WerknemersType> valueSet() {
		return new HashSet<>(Arrays.asList(WerknemersType.values()));
	}

}
