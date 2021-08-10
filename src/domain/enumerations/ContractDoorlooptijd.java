package domain.enumerations;

import java.util.Arrays;
import java.util.HashSet;

public enum ContractDoorlooptijd {
	OneYear, TwoYear, ThreeYear;

	public static HashSet<ContractDoorlooptijd> valueSet() {
		return new HashSet<>(Arrays.asList(ContractDoorlooptijd.values()));
	}
}
