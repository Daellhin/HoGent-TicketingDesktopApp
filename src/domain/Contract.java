package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import domain.enumerations.ContractStatus;

@Entity
public class Contract implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int nummer;
	@ManyToOne
	private ContractType contractType;
	private ContractStatus status;
	private LocalDate startDatum;
	private LocalDate eindDatum;
	@ManyToOne
	private Klant klant;
	@OneToMany(mappedBy = "contract", cascade = CascadeType.PERSIST)
	private Collection<Ticket> tickets;

	public Collection<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Collection<Ticket> tickets) {
		this.tickets = tickets;
	}

	protected Contract() {
	}

	public Contract(ContractType contractType, ContractStatus status, LocalDate startDatum, LocalDate eindDatum,
			Klant klant) {
		setContractType(contractType);
		setStatus(status);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setKlant(klant);

		tickets = new ArrayList<>();
		contractType.addContract(this);
		klant.addContract(this);
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType value) {
		if (value != null) {
			this.contractType = value;
		} else {
			throw new IllegalArgumentException("A contract needs to have a type");
		}
	}

	public ContractStatus getStatus() {
		return this.status;
	}

	public void setStatus(ContractStatus value) {
		if (value != null) {
			this.status = value;
		} else {
			throw new IllegalArgumentException("Status can not be empty.");
		}
	}

	public java.time.LocalDate getStartDatum() {
		return this.startDatum;
	}

	public void setStartDatum(java.time.LocalDate value) {
		if (value != null) {
			this.startDatum = value;
		} else {
			throw new IllegalArgumentException("Start date can not be empty.");
		}
	}

	public java.time.LocalDate getEindDatum() {
		return this.eindDatum;
	}

	public void setEindDatum(java.time.LocalDate value) {
		// De looptijd moet minimum 1 jaar zijn en de eindDatum mag ook niet kleiner
		// zijn dan de beginDatum
		if (value != null) {
			if (value.isBefore(startDatum.plusYears(1))) {
				throw new IllegalArgumentException("The end date needs to be 1 year greater than the start date");
			}
			this.eindDatum = value;
		} else {
			throw new IllegalArgumentException("The end date can not be empty");
		}
	}

	public Klant getKlant() {
		return klant;
	}

	public void setKlant(Klant klant) {
		if (klant != null) {
			this.klant = klant;
		} else {
			throw new IllegalArgumentException("Customer can not be empty.");
		}
	}

	public int getNummer() {
		return nummer;
	}

}
