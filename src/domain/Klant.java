package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({ @NamedQuery(name = "Klant.findAll", query = "SELECT k FROM Klant k"),
		@NamedQuery(name = "Klant.geefKlant", query = "SELECT k FROM Klant k WHERE k.gebruikersnaam = :gebruikersnaam"),
		@NamedQuery(name = "Klant.bestaatUsername", query = "SELECT COUNT(k) FROM Klant k WHERE k.gebruikersnaam = :gebruikersnaam") })
public class Klant extends Gebruiker implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bedrijfsNaam;
	@OneToMany(mappedBy = "klant", cascade = CascadeType.PERSIST)
	private Collection<Contactpersoon> contactpersonen;
	@OneToMany(mappedBy = "klant", cascade = CascadeType.PERSIST)
	private Collection<Contract> contracten;
	private LocalDate datumKlantGeworden;

	protected Klant() {
	}

	public Klant(String gebruikersnaam, List<String> telefoonnummers, Adres bedrijfsAdres, String bedrijfsNaam,
			Collection<Contactpersoon> contactpersonen) {
		super(gebruikersnaam, telefoonnummers, bedrijfsAdres);
		setBedrijfsNaam(bedrijfsNaam);
		setContactpersonen(contactpersonen);
		setContracten(new ArrayList<Contract>());
		setDatumKlantGeworden(LocalDate.now());

		contactpersonen.forEach(e -> {
			e.setKlant(this);
		});
	}

	public void addContract(Contract contract) {
		contracten.add(contract);
	}

	public String getBedrijfsNaam() {
		return this.bedrijfsNaam;
	}

	public void setBedrijfsNaam(String value) {
		if (value != null && !value.isBlank() && !value.isEmpty()) {
			this.bedrijfsNaam = value;
		} else {
			throw new IllegalArgumentException("Company Name can not be empty.");
		}
	}

	public Collection<Contactpersoon> getContactpersonen() {
		return contactpersonen;
	}

	public void setContactpersonen(Collection<Contactpersoon> contactpersonen) {
		if (contactpersonen != null) {
			this.contactpersonen = contactpersonen;
		} else {
			throw new IllegalArgumentException("a contact person needs to be provided");
		}
	}

	public Collection<Contract> getContracten() {
		return contracten;
	}

	private void setContracten(Collection<Contract> contracten) {
		this.contracten = contracten;
	}

	public LocalDate getDatumKlantGeworden() {
		return this.datumKlantGeworden;
	}

	private void setDatumKlantGeworden(LocalDate datumKlantGeworden) {
		this.datumKlantGeworden = datumKlantGeworden;
	}

}
