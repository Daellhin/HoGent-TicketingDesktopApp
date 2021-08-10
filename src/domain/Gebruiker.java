package domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import domain.enumerations.GebruikersStatus;
import util.Controles;

@MappedSuperclass
public abstract class Gebruiker implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String gebruikersnaam;
	@ElementCollection
	private List<String> telefoonnummers;
	@Embedded
	private Adres adres;
	@SuppressWarnings("unused")
	private String wachtwoord;
	private GebruikersStatus status;

	protected Gebruiker() {
	}

	/**
	 * Maakt een nieuwe gebruiker aan met standaard wachtwoord: "Wachtwoord" en
	 * status: Actief
	 * 
	 * @param gebruikersnaam
	 * @param telefoonnummers
	 * @param adres
	 */
	public Gebruiker(String gebruikersnaam, List<String> telefoonnummers, Adres adres) {
		setGebruikersnaam(gebruikersnaam);
		setTelefoonnummers(telefoonnummers);
		setAdres(adres);

		this.wachtwoord = "Wachtwoord";
		setStatus(GebruikersStatus.Active);
	}

	public int getId() {
		return id;
	}

	public String getGebruikersnaam() {
		return this.gebruikersnaam;

	}

	public void setGebruikersnaam(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			Controles.stringMinimum(value, Constanten.MIN_LENGTE_GEBRUIKERSNAAM,
					"Username must be at least 6 characters long");
			this.gebruikersnaam = value;
		} else {
			throw new IllegalArgumentException("Username can not be empty");
		}
	}

	public List<String> getTelefoonnummers() {
		return this.telefoonnummers;
	}

	public void setTelefoonnummers(List<String> value) {
		// internationaal toegangsnummer : 00 of 011
		// landnummer: 1 tot 5 cijfers
		// Uniek nummer: 7 tot 15 cijfers
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("Phone numbers can not be empty.");
		} else {
			value.forEach(e -> {
				Controles.stringTelephoneNumber(e,
						"Telephone number must conform to the international standard. Must start with + eg: +32472643661");
			});
			this.telefoonnummers = value;
		}
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres value) {
		if (value != null) {
			this.adres = value;
		} else {
			throw new IllegalArgumentException("An address needs to be provided");
		}
	}

	public GebruikersStatus getStatus() {
		return this.status;
	}

	public void setStatus(GebruikersStatus value) {
		this.status = value;
	}
}
