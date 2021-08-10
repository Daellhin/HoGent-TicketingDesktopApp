package domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import domain.enumerations.AanmeldpogingStatus;

@Entity
@NamedQueries({
		@NamedQuery(name = "Aanmeldpoging.geefLaatste5Pogingen", query = "SELECT a FROM Aanmeldpoging a WHERE a.gebruikersnaam = :gebruikersnaam ORDER BY a.timestamp DESC") })
public class Aanmeldpoging {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String gebruikersnaam;
	private AanmeldpogingStatus gelukt;
	private LocalDateTime timestamp;

	protected Aanmeldpoging() {
	}

	public Aanmeldpoging(String gebruikersnaam, AanmeldpogingStatus gelukt) {
		setGebruikersnaam(gebruikersnaam);
		setGelukt(gelukt);
		setTimestamp(LocalDateTime.now());
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	private void setGebruikersnaam(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.gebruikersnaam = value;
		} else {
			throw new IllegalArgumentException("Username cannot be empty.");
		}
	}

	public AanmeldpogingStatus isGelukt() {
		return this.gelukt;
	}

	private void setGelukt(AanmeldpogingStatus value) {
		this.gelukt = value;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	private void setTimestamp(LocalDateTime value) {
		if (value != null) {
			this.timestamp = value;
		} else {
			throw new IllegalArgumentException("Timestamp can not be null.");
		}
	}
}
