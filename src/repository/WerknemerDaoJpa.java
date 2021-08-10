package repository;

import java.util.List;

import javax.persistence.NoResultException;

import domain.Werknemer;
import domain.dao.WerknemerDao;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;

public class WerknemerDaoJpa extends GenericDaoJpa<Werknemer> implements WerknemerDao {

	public WerknemerDaoJpa() {
		super(Werknemer.class);
	}

	@Override
	public void blokkeerWerknemer(String gebruikersnaam) {
		try {
			em.createNamedQuery("Werknemer.updateStatus", Werknemer.class)
					.setParameter("gebruikersnaam", gebruikersnaam).setParameter("status", GebruikersStatus.Blocked)
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Werknemer geefWerknemer(String gebruikersnaam, String wachtwoord) throws NoResultException {
		return em.createNamedQuery("Werknemer.geefWerknemer", Werknemer.class)
				.setParameter("gebruikersnaam", gebruikersnaam).setParameter("wachtwoord", wachtwoord)
				.getSingleResult();
	}

	@Override
	public GebruikersStatus bestaatWerkemer(String gebruikersnaam) throws NoResultException {
		return em.createNamedQuery("Werknemer.geefStatus", GebruikersStatus.class)
				.setParameter("gebruikersnaam", gebruikersnaam).getSingleResult();
	}

	@Override
	public List<Werknemer> geefTechniekersVanDienst(Dienst dienst) {
		return em.createNamedQuery("Werknemer.geefAlleTechniekersVanDienst", Werknemer.class)
				.setParameter("dienst", dienst).setParameter("type", WerknemersType.Technician).getResultList();
	}

	@Override
	public boolean bestaatUsername(String username) {
		return em.createNamedQuery("Werknemer.bestaatUsername", Long.class).setParameter("gebruikersnaam", username)
				.getSingleResult() == 1;
	}
}
