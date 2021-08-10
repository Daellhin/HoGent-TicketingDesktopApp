package domain.dao;

import java.util.List;

import domain.Werknemer;
import domain.enumerations.Dienst;
import domain.enumerations.GebruikersStatus;

public interface WerknemerDao extends GenericDao<Werknemer> {

	Werknemer geefWerknemer(String gebruikersnaam, String wachtwoord);

	void blokkeerWerknemer(String gebruikersnaam);

	GebruikersStatus bestaatWerkemer(String gebruikersnaam);

	List<Werknemer> geefTechniekersVanDienst(Dienst dienst);

	boolean bestaatUsername(String username);
}
