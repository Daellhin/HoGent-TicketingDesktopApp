package domain.dao;

import domain.ContractType;

public interface ContractTypeDao extends GenericDao<ContractType> {

	boolean bestaatContractTypeNaam(String naam);

	int geefAantalBehandeldeTicketsVanContractType(String naam);

	double geefPercentageOpTijdBehandeldeTicketsVanContractType(String naam);
}
