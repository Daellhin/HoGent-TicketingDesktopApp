package domain.dao;

import domain.Klant;

public interface KlantDao extends GenericDao<Klant> {

	boolean bestaatUsername(String gebruikersnaam);

}
