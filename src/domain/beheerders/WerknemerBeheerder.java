package domain.beheerders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import domain.Aanmeldpoging;
import domain.Werknemer;
import domain.dao.AanmeldpogingDao;
import domain.dao.KlantDao;
import domain.dao.WerknemerDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.AanmeldpogingDaoJpa;
import repository.KlantDaoJpa;
import repository.WerknemerDaoJpa;

public class WerknemerBeheerder {

	private WerknemerDao werknemerDao;
	private AanmeldpogingDao aanmeldpogingDao;
	private FilteredList<Werknemer> filteredWerknemerList;
	private KlantDao klantDao;

	private WerknemerBeheerder(WerknemerDao werknemerDao, AanmeldpogingDao aanmeldpogingDao, KlantDao klantDao) {
		this.werknemerDao = werknemerDao;
		this.aanmeldpogingDao = aanmeldpogingDao;
		this.klantDao = klantDao;
	}

	public WerknemerBeheerder() {
		this(new WerknemerDaoJpa(), new AanmeldpogingDaoJpa(), new KlantDaoJpa());
	}

	public boolean bestaatUsername(String username) {
		return werknemerDao.bestaatUsername(username) || klantDao.bestaatUsername(username);
	}

	public void voegWerknemerToe(Werknemer werknemer) {
		werknemerDao.startTransaction();
		werknemerDao.insert(werknemer);
		werknemerDao.commitTransaction();
		filteredWerknemerList = new FilteredList<>(FXCollections.observableArrayList(werknemerDao.findAll()),
				filteredWerknemerList.getPredicate());
	}

	public void pasWerknemerAan(Werknemer werknemer) {
		werknemerDao.startTransaction();
		werknemerDao.update(werknemer);
		werknemerDao.commitTransaction();
		filteredWerknemerList = new FilteredList<>(FXCollections.observableArrayList(werknemerDao.findAll()),
				filteredWerknemerList.getPredicate());
	}

	/**
	 * Filter de filteredKlantList, toont alles als er geen filters geslecteerd zijn
	 * <p>
	 * 
	 * Reduce uitleg:
	 * <a href="https://stackoverflow.com/a/24558850/8807613">Stackoverflow</a>
	 * 
	 * @param gebruikersnaam
	 * @param naam
	 * @param voornaam
	 * @param status         geen meegegeven status == allemaal geslecteerd
	 * @param rol            geen meegegeven status == allemaal geslecteerd
	 */
	public void pasFilterAan(int werknemernummer, String gebruikersnaam, String naam, String voornaam,
			Set<GebruikersStatus> status, Set<WerknemersType> rol) {
		List<Predicate<Werknemer>> filters = new ArrayList<>();

		if (werknemernummer >= 0) {
			filters.add(klant -> Integer.toString(klant.getId()).startsWith(Integer.toString(werknemernummer)));
		}

		if (gebruikersnaam != null && !gebruikersnaam.isBlank()) {
			filters.add(
					werknemer -> werknemer.getGebruikersnaam().toLowerCase().contains(gebruikersnaam.toLowerCase()));
		}

		if (naam != null && !naam.isBlank()) {
			filters.add(werknemer -> werknemer.getNaam().toLowerCase().contains(naam.toLowerCase()));
		}

		if (voornaam != null && !voornaam.isBlank()) {
			filters.add(werknemer -> werknemer.getVoornaam().toLowerCase().contains(voornaam.toLowerCase()));
		}

		if (status != null && (status.size() > 0 || status.size() >= GebruikersStatus.values().length)) {
			filters.add(werknemer -> status.contains(werknemer.getStatus()));
		}

		if (rol != null && (rol.size() > 0 || rol.size() >= WerknemersType.values().length)) {
			filters.add(werknemer -> rol.contains(werknemer.getWerknemersType()));
		}

		Predicate<Werknemer> filter = filters.stream().reduce(Predicate::and).orElse(x -> true);
		filteredWerknemerList.setPredicate(filter);
	}

	public ObservableList<Werknemer> getWerknemerList() {
		if (filteredWerknemerList == null) {
			filteredWerknemerList = new FilteredList<>(FXCollections.observableArrayList(werknemerDao.findAll()),
					e -> e.getStatus() == GebruikersStatus.Active);
		}
		return filteredWerknemerList;
	}

	public void deblokkeerWerknemer(String gebruikersnaam) {
		aanmeldpogingDao.startTransaction();
		aanmeldpogingDao.insert(new Aanmeldpoging(gebruikersnaam, AanmeldpogingStatus.Gedeblokkeerd));
		aanmeldpogingDao.commitTransaction();
	}

}
