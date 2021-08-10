package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import util.Controles;

@Embeddable
public class Adres implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String land;
	@Column(nullable = false)
	private String postcode;
	@Column(nullable = false)
	private String woonplaats;
	@Column(nullable = false)
	private String straat;
	@Column(nullable = false)
	private int huisnummer;
	private String busnr;

	protected Adres() {
	}

	public Adres(String land, String postcode, String woonplaats, String straat, int huisnummer) {
		setLand(land);
		setPostcode(postcode);
		setWoonplaats(woonplaats);
		setStraat(straat);
		setHuisnummer(huisnummer);
	}

	public Adres(String land, String postcode, String woonplaats, String straat, int huisnummer, String busnr) {
		this(land, postcode, woonplaats, straat, huisnummer);
		setBusnr(busnr);
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		String[] countryCodes = Locale.getISOCountries();
		if (land != null && Arrays.stream(countryCodes).anyMatch(land::equals)) {
			this.land = land;
		} else {
			throw new IllegalArgumentException("Invalid country.");
		}
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.postcode = value;
		} else {
			throw new IllegalArgumentException("Invalid postcode.");
		}
	}

	public String getWoonplaats() {
		return woonplaats;
	}

	public void setWoonplaats(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			Controles.stringCity(value, "Please enter a valid city name");
			this.woonplaats = value;
		} else {
			throw new IllegalArgumentException("City can not be empty.");
		}
	}

	public String getStraat() {
		return straat;
	}

	public void setStraat(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.straat = value;
		} else {
			throw new IllegalArgumentException("Street can not be empty.");
		}
	}

	public int getHuisnummer() {
		return huisnummer;
	}

	public void setHuisnummer(int value) {
		if (value >= 1 && value <= 9999) {
			this.huisnummer = value;
		} else {
			throw new IllegalArgumentException("Invalid house number.");
		}
	}

	public String getBusnr() {
		return busnr;
	}

	public void setBusnr(String value) {
		if (value == null) {
			this.busnr = null;
		} else {
			Controles.stringAlphaNumeric(value, "Boxnr must be alphanumerical");
			this.busnr = value;
		}
	}

}
