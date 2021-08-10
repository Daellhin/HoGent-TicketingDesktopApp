package domain.controllers;

import java.util.Set;

import domain.ContractType;
import domain.beheerders.ContractTypeBeheerder;
import domain.enumerations.ContractTypeStatus;
import javafx.collections.ObservableList;

public class BeheerContractTypesController {

	private ContractTypeBeheerder contractTypeBeheerder;

	public BeheerContractTypesController() {
		this.contractTypeBeheerder = new ContractTypeBeheerder();
	}

	public int geefAantalBehandeldeTicketsVanContractType(String naam) {
		return contractTypeBeheerder.geefAantalBehandeldeTicketsVanContractType(naam);
	}

	public double geefPercentageOpTijdBehandeldeTicketsVanContractType(String naam) {
		return contractTypeBeheerder.geefPercentageOpTijdBehandeldeTicketsVanContractType(naam);
	}

	public boolean bestaatContractTypeNaam(String naam) {
		return contractTypeBeheerder.bestaatContractTypeNaam(naam);
	}

	public void voegContractTypeToe(ContractType contractType) {
		contractTypeBeheerder.voegContractTypeToe(contractType);
	}

	public void pastContractTypeAan(ContractType contractType) {
		contractTypeBeheerder.pastContractTypeAan(contractType);
	}

	public void pasFilterAan(String naam, Set<ContractTypeStatus> status, int aantalContracten) {
		contractTypeBeheerder.pasFilterAan(naam, status, aantalContracten);
	}

	public ObservableList<ContractType> getContractTypesList() {
		return contractTypeBeheerder.getContractTypesList();
	}
}
