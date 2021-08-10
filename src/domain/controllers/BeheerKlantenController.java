package domain.controllers;

import java.util.Set;

import domain.Klant;
import domain.beheerders.KlantBeheerder;
import domain.enumerations.GebruikersStatus;
import javafx.collections.ObservableList;

public class BeheerKlantenController {

	private KlantBeheerder klantBeheerder;

	public BeheerKlantenController() {
		this.klantBeheerder = new KlantBeheerder();
	}

	public boolean bestaatUsername(String username) {
		return klantBeheerder.bestaatUsername(username);
	}

	public void voegKlantToe(Klant klant) {
		klantBeheerder.voegKlantToe(klant);
	}

	public void pasKlantAan(Klant klant) {
		klantBeheerder.pasKlantAan(klant);
	}

	public void pasFilterAan(int klantnummer, String gebruikersnaam, String bedrijfsnaam,
			Set<GebruikersStatus> status) {
		klantBeheerder.pasFilterAan(klantnummer, gebruikersnaam, bedrijfsnaam, status);
	}

	public ObservableList<Klant> getKlantList() {
		return klantBeheerder.getKlantList();
	}

	public void deblokkeerKlant(String gebruikersnaam) {
		klantBeheerder.deblokkeerKlant(gebruikersnaam);
	}

}
