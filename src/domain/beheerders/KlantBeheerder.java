package domain.beheerders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import domain.Aanmeldpoging;
import domain.Klant;
import domain.dao.AanmeldpogingDao;
import domain.dao.KlantDao;
import domain.dao.WerknemerDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.GebruikersStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.AanmeldpogingDaoJpa;
import repository.KlantDaoJpa;
import repository.WerknemerDaoJpa;

public class KlantBeheerder {

	private KlantDao klantDao;
	private WerknemerDao werknemerDao;
	private AanmeldpogingDao aanmeldpogingDao;
	private FilteredList<Klant> filteredKlantList;

	private KlantBeheerder(KlantDao klantDao, AanmeldpogingDao aanmeldpogingDao, WerknemerDao werknemerDao) {
		this.klantDao = klantDao;
		this.werknemerDao = werknemerDao;
		this.aanmeldpogingDao = aanmeldpogingDao;
	}

	public KlantBeheerder() {
		this(new KlantDaoJpa(), new AanmeldpogingDaoJpa(), new WerknemerDaoJpa());
	}

	public boolean bestaatUsername(String username) {
		return werknemerDao.bestaatUsername(username) || klantDao.bestaatUsername(username);
	}

	public void voegKlantToe(Klant klant) {
		klantDao.startTransaction();
		klantDao.insert(klant);
		klantDao.commitTransaction();
		filteredKlantList = new FilteredList<>(FXCollections.observableArrayList(klantDao.findAll()),
				filteredKlantList.getPredicate());
	}

	public void pasKlantAan(Klant klant) {
		klantDao.startTransaction();
		klantDao.update(klant);
		klantDao.commitTransaction();
		filteredKlantList = new FilteredList<>(FXCollections.observableArrayList(klantDao.findAll()),
				filteredKlantList.getPredicate());
	}

	/**
	 * Filter de filteredKlantList, toont alles als er geen filters geslecteerd zijn
	 * <p>
	 * 
	 * Reduce uitleg:
	 * <a href="https://stackoverflow.com/a/24558850/8807613">Stackoverflow</a>
	 * 
	 * @param klantnummer    geef -1 mee als er niet gefilterd moet worden
	 * @param gebruikersnaam
	 * @param bedrijfsnaam
	 * @param status         geen meegegeven status == allemaal geslecteerd
	 */
	public void pasFilterAan(int klantnummer, String gebruikersnaam, String bedrijfsnaam,
			Set<GebruikersStatus> status) {
		List<Predicate<Klant>> filters = new ArrayList<>();

		if (klantnummer >= 0) {
			filters.add(klant -> Integer.toString(klant.getId()).startsWith(Integer.toString(klantnummer)));
		}

		if (gebruikersnaam != null && !gebruikersnaam.isBlank()) {
			filters.add(klant -> klant.getGebruikersnaam().toLowerCase().contains(gebruikersnaam.toLowerCase()));
		}

		if (bedrijfsnaam != null && !bedrijfsnaam.isBlank()) {
			filters.add(klant -> klant.getBedrijfsNaam().toLowerCase().contains(bedrijfsnaam.toLowerCase()));
		}

		if (status != null && (status.size() > 0 || status.size() >= GebruikersStatus.values().length)) {
			filters.add(klant -> status.contains(klant.getStatus()));
		}

		Predicate<Klant> filter = filters.stream().reduce(Predicate::and).orElse(x -> true);
		filteredKlantList.setPredicate(filter);
	}

	public ObservableList<Klant> getKlantList() {
		if (filteredKlantList == null) {
			filteredKlantList = new FilteredList<>(FXCollections.observableArrayList(klantDao.findAll()),
					e -> e.getStatus() == GebruikersStatus.Active);
		}
		return FXCollections.unmodifiableObservableList(filteredKlantList);
	}

	public void deblokkeerKlant(String gebruikersnaam) {
		aanmeldpogingDao.startTransaction();
		aanmeldpogingDao.insert(new Aanmeldpoging(gebruikersnaam, AanmeldpogingStatus.Gedeblokkeerd));
		aanmeldpogingDao.commitTransaction();
	}

}
