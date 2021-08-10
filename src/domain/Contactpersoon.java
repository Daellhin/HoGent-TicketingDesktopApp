package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import util.Controles;

@Entity
public class Contactpersoon implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	private String naam;
	private String voornaam;
	@ManyToOne
	private Klant klant;

	protected Contactpersoon() {
	}

	public Contactpersoon(String email, String naam, String voornaam) {
		setEmail(email);
		setNaam(naam);
		setVoornaam(voornaam);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String value) {
		Controles.stringEmail(value, "Email must be in a valid format");
		this.email = value;
	}

	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String value) {
		if (value == null || value.isBlank() || value.isEmpty()) {
			throw new IllegalArgumentException("Name can not be empty.");
		}
		this.naam = value;
	}

	public String getVoornaam() {
		return this.voornaam;
	}

	public void setVoornaam(String value) {
		if (value == null || value.isBlank() || value.isEmpty()) {
			throw new IllegalArgumentException("First name can not be empty.");
		}
		this.voornaam = value;
	}

	public Klant getKlant() {
		return klant;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}

}
