package domain.dao;

import domain.Aanmeldpoging;

public interface AanmeldpogingDao extends GenericDao<Aanmeldpoging> {

	int geefAantalGefaaldeAanmeldpogingen(String gebruikersnaam);

}
