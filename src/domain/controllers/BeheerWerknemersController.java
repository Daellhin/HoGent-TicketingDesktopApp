package domain.controllers;

import java.util.Set;

import domain.Werknemer;
import domain.beheerders.WerknemerBeheerder;
import domain.enumerations.GebruikersStatus;
import domain.enumerations.WerknemersType;
import javafx.collections.ObservableList;

public class BeheerWerknemersController {

	private WerknemerBeheerder werknemerBeheerder;

	public BeheerWerknemersController() {
		this.werknemerBeheerder = new WerknemerBeheerder();
	}

	public boolean bestaatUsername(String username) {
		return werknemerBeheerder.bestaatUsername(username);
	}

	public void voegWerknemerToe(Werknemer werknemer) {
		werknemerBeheerder.voegWerknemerToe(werknemer);
	}

	public void pasWerknemerAan(Werknemer werknemer) {
		werknemerBeheerder.pasWerknemerAan(werknemer);
	}

	public void pasFilterAan(int werknemernummer, String gebruikersnaam, String naam, String voornaam,
			Set<GebruikersStatus> status, Set<WerknemersType> rol) {
		werknemerBeheerder.pasFilterAan(werknemernummer, gebruikersnaam, naam, voornaam, status, rol);
	}

	public ObservableList<Werknemer> getWerknemerList() {
		return werknemerBeheerder.getWerknemerList();
	}

	public void deblokkeerWerknemer(String gebruikersnaam) {
		werknemerBeheerder.deblokkeerWerknemer(gebruikersnaam);
	}

}
