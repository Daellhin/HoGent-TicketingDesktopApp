package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum Dienst {
	Marketing, Finance, OperationsManagement, HumanResource, IT, Admin;

	public static HashSet<Dienst> valueSet() {
		return new HashSet<>(Arrays.asList(Dienst.values()));
	}
}
