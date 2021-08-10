package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import domain.enumerations.Dienst;
import domain.enumerations.WerknemersType;
import util.Controles;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
		@NamedQuery(name = "Werknemer.geefStatus", query = "SELECT w.status FROM Werknemer w WHERE w.gebruikersnaam = :gebruikersnaam"),
		@NamedQuery(name = "Werknemer.geefWerknemer", query = "SELECT w FROM Werknemer w WHERE w.gebruikersnaam = :gebruikersnaam AND w.wachtwoord = :wachtwoord"),
		@NamedQuery(name = "Werknemer.updateStatus", query = "UPDATE Werknemer SET status = :status WHERE gebruikersnaam = :gebruikersnaam"),
		@NamedQuery(name = "Werknemer.geefAlleTechniekersVanDienst", query = "SELECT w FROM Werknemer w WHERE w.dienst = :dienst AND w.werknemersType = :type"),
		@NamedQuery(name = "Werknemer.bestaatUsername", query = "SELECT COUNT(w) FROM Werknemer w WHERE w.gebruikersnaam = :gebruikersnaam") })

public class Werknemer extends Gebruiker implements Serializable {

	private static final long serialVersionUID = 1L;

	private String naam;
	private String voornaam;
	private String email;
	private Dienst dienst;
	private WerknemersType werknemersType;
	private LocalDate datumInDienstTreding;

	protected Werknemer() {
	}

	public Werknemer(String gebruikersnaam, List<String> telefoonnummers, Adres adres, String naam, String voornaam,
			String email, WerknemersType werknemersType, Dienst dienst) {
		super(gebruikersnaam, telefoonnummers, adres);
		setNaam(naam);
		setVoornaam(voornaam);
		setEmail(email);
		setWerknemersType(werknemersType);
		setDienst(dienst);
		setDatumInDienstTreding(LocalDate.now());
	}

	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.naam = value;
		} else {
			throw new IllegalArgumentException("Name may not be empty.");
		}
	}

	public String getVoornaam() {
		return this.voornaam;
	}

	public void setVoornaam(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.voornaam = value;
		} else {
			throw new IllegalArgumentException("First name can not be empty.");
		}
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String value) {
		if (value == null || value.isBlank() || value.isEmpty()) {
			throw new IllegalArgumentException("Email can not be empty.");
		} else {
			Controles.stringEmail(value, "Invalid email format");
			this.email = value;
		}
	}

	public WerknemersType getWerknemersType() {
		return this.werknemersType;
	}

	public void setWerknemersType(WerknemersType value) {
		if (value == WerknemersType.Administrator || value == WerknemersType.SupportManager
				|| value == WerknemersType.Technician) {
			this.werknemersType = value;
		} else {
			throw new IllegalArgumentException("Invalid employee type.");
		}
	}

	public LocalDate getDatumInDienstTreding() {
		return this.datumInDienstTreding;
	}

	private void setDatumInDienstTreding(LocalDate datumInDienstTreding) {
		this.datumInDienstTreding = datumInDienstTreding;
	}

	public Dienst getDienst() {
		return dienst;
	}

	public void setDienst(Dienst dienst) {
		this.dienst = dienst;
	}

}
