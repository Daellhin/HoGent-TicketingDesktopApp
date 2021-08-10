package repository;

import domain.Klant;
import domain.dao.KlantDao;

public class KlantDaoJpa extends GenericDaoJpa<Klant> implements KlantDao {

	public KlantDaoJpa() {
		super(Klant.class);
	}

	@Override
	public boolean bestaatUsername(String username) {
		System.out.println(em.createNamedQuery("Klant.bestaatUsername", Long.class)
				.setParameter("gebruikersnaam", username).getSingleResult());
		return em.createNamedQuery("Klant.bestaatUsername", Long.class).setParameter("gebruikersnaam", username)
				.getSingleResult() == 1;
	}

}
