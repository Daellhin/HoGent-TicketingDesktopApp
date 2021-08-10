package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum ContractTypeStatus {
	Active, Inactive;

	public static HashSet<ContractTypeStatus> valueSet() {
		return new HashSet<>(Arrays.asList(ContractTypeStatus.values()));
	}
}
