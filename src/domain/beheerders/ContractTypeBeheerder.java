package domain.beheerders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import domain.ContractType;
import domain.dao.ContractTypeDao;
import domain.enumerations.ContractTypeStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.ContractTypeDaoJpa;

public class ContractTypeBeheerder {

	private ContractTypeDao contractTypeDao;
	private FilteredList<ContractType> filteredContractTypeList;

	private ContractTypeBeheerder(ContractTypeDao contractTypeDao) {
		this.contractTypeDao = contractTypeDao;
	}

	public ContractTypeBeheerder() {
		this(new ContractTypeDaoJpa());
	}

	public boolean bestaatContractTypeNaam(String naam) {
		return contractTypeDao.bestaatContractTypeNaam(naam);
	}

	public int geefAantalBehandeldeTicketsVanContractType(String naam) {
		return contractTypeDao.geefAantalBehandeldeTicketsVanContractType(naam);
	}

	public double geefPercentageOpTijdBehandeldeTicketsVanContractType(String naam) {
		return contractTypeDao.geefPercentageOpTijdBehandeldeTicketsVanContractType(naam);
	}

	public void voegContractTypeToe(ContractType contractType) {
		contractTypeDao.startTransaction();
		contractTypeDao.insert(contractType);
		contractTypeDao.commitTransaction();
		filteredContractTypeList = new FilteredList<>(FXCollections.observableArrayList(contractTypeDao.findAll()),
				filteredContractTypeList.getPredicate());
	}

	public void pastContractTypeAan(ContractType contractType) {
		contractTypeDao.startTransaction();
		contractTypeDao.update(contractType);
		contractTypeDao.commitTransaction();
		filteredContractTypeList = new FilteredList<>(FXCollections.observableArrayList(contractTypeDao.findAll()),
				filteredContractTypeList.getPredicate());
	}

	public void pasFilterAan(String naam, Set<ContractTypeStatus> status, int aantalContracten) {
		List<Predicate<ContractType>> filters = new ArrayList<>();

		if (naam != null && !naam.isBlank()) {
			filters.add(contractType -> contractType.getNaam().toLowerCase().contains(naam.toLowerCase()));
		}

		if (status != null && (status.size() > 0 || status.size() >= ContractTypeStatus.values().length)) {
			filters.add(contractType -> status.contains(contractType.getStatus()));
		}
		if (aantalContracten >= 0) {
			filters.add(contractType -> contractType.getAantalLopendeContracten() >= aantalContracten);
		}

		Predicate<ContractType> filter = filters.stream().reduce(Predicate::and).orElse(x -> true);
		filteredContractTypeList.setPredicate(filter);
	}

	public ObservableList<ContractType> getContractTypesList() {
		if (filteredContractTypeList == null) {
			filteredContractTypeList = new FilteredList<>(FXCollections.observableArrayList(contractTypeDao.findAll()),
					e -> e.getStatus() == ContractTypeStatus.Active);
		}
		return FXCollections.unmodifiableObservableList(filteredContractTypeList);
	}
}
