package repository;

import java.util.List;

import javax.persistence.NoResultException;

import domain.Aanmeldpoging;
import domain.Constanten;
import domain.dao.AanmeldpogingDao;
import domain.enumerations.AanmeldpogingStatus;

public class AanmeldpogingDaoJpa extends GenericDaoJpa<Aanmeldpoging> implements AanmeldpogingDao {

	public AanmeldpogingDaoJpa() {
		super(Aanmeldpoging.class);
	}

	/**
	 * Haalt id de databank de
	 * {@value Constanten#MAX_AANTAL_GEFAALDE_AANMELDPOGINEN} laatste
	 * aanmeldpogingen op. Telt hoeveel pogingen er gefaald zijn. Vangt de
	 * NoResultException op als er voor de gebruiker geen aanmeldpogingen worden
	 * gegeven (bv als de gebruiker nog nooit heeft ingelogd) en retourneerd 0.
	 */
	@Override
	public int geefAantalGefaaldeAanmeldpogingen(String gebruikersnaam) throws NoResultException {
		int aantalGefaaldePogingen = 0;

		try {
			List<Aanmeldpoging> aanmeldpogingenGebruiker = em
					.createNamedQuery("Aanmeldpoging.geefLaatste5Pogingen", Aanmeldpoging.class)
					.setParameter("gebruikersnaam", gebruikersnaam)
					.setMaxResults(Constanten.MAX_AANTAL_GEFAALDE_AANMELDPOGINGEN).getResultList();

			for (Aanmeldpoging aanmeldpoging : aanmeldpogingenGebruiker) {
				if (aanmeldpoging.isGelukt() == AanmeldpogingStatus.Mislukt) {
					aantalGefaaldePogingen++;
				}
			}

			return aantalGefaaldePogingen;
		} catch (NoResultException ex) {
			return 0;
		}
	}

}
