package domain.controllers;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domain.Aanmeldpoging;
import domain.Constanten;
import domain.Werknemer;
import domain.dao.AanmeldpogingDao;
import domain.dao.WerknemerDao;
import domain.enumerations.AanmeldpogingStatus;
import domain.enumerations.GebruikersStatus;
import repository.AanmeldpogingDaoJpa;
import repository.WerknemerDaoJpa;

public class AanmeldController {

	private Werknemer aangemeldeWerknemer;
	private WerknemerDao werknemerDao;
	private AanmeldpogingDao aanmeldpogingDao;

	private AanmeldController(WerknemerDao werknemerDao, AanmeldpogingDao aanmeldpogingDao) {
		this.werknemerDao = werknemerDao;
		this.aanmeldpogingDao = aanmeldpogingDao;
	}

	public AanmeldController() {
		this(new WerknemerDaoJpa(), new AanmeldpogingDaoJpa());
	}

	public void aanmelden(String werknemersnaam, String wachtwoord) {
		GebruikersStatus status;
		try {
			status = werknemerDao.bestaatWerkemer(werknemersnaam);
		} catch (NoResultException e) {
			throw new EntityNotFoundException("Username and/or password is incorrect.");
		}

		if (status == GebruikersStatus.Blocked) {
			throw new IllegalArgumentException("Your account is blocked. Please contact an admin.");
		}
		if (status == GebruikersStatus.Inactive) {
			throw new IllegalArgumentException("Your account is inactive. Please contact an admin.");
		}

		Werknemer werknemer;
		try {
			werknemer = werknemerDao.geefWerknemer(werknemersnaam, wachtwoord);
		} catch (NoResultException e) {
			aanmeldpogingDao.startTransaction();
			aanmeldpogingDao.insert(new Aanmeldpoging(werknemersnaam, AanmeldpogingStatus.Mislukt));
			aanmeldpogingDao.commitTransaction();

			if (aanmeldpogingDao.geefAantalGefaaldeAanmeldpogingen(
					werknemersnaam) >= Constanten.MAX_AANTAL_GEFAALDE_AANMELDPOGINGEN) {
				werknemerDao.startTransaction();
				werknemerDao.blokkeerWerknemer(werknemersnaam);
				werknemerDao.commitTransaction();
				throw new IllegalArgumentException(
						"Due to too many failed login attempts, this account has been blocked. Please contact an admin.");
			}

			throw new EntityNotFoundException("Username and/or password is incorrect.");
		}

		Aanmeldpoging poging = new Aanmeldpoging(werknemersnaam, AanmeldpogingStatus.Gelukt);
		aanmeldpogingDao.startTransaction();
		aanmeldpogingDao.insert(poging);
		aanmeldpogingDao.commitTransaction();

		this.aangemeldeWerknemer = werknemer;
	}

	public void afmelden() {
		this.aangemeldeWerknemer = null;
	}

	public Werknemer getAangemeldeWerknemer() {
		return aangemeldeWerknemer;
	}
}
