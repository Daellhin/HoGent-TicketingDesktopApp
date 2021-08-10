package domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import domain.enumerations.Dienst;
import domain.enumerations.TicketStatus;
import domain.enumerations.TicketUrgency;

@Entity
@NamedQueries({
		@NamedQuery(name = "Ticket.geefAfgehandeldeTickets", query = "SELECT t FROM Ticket t WHERE t.status = :status"),
		@NamedQuery(name = "Ticket.geefTicketsVanTechnieker", query = "SELECT t FROM Ticket t WHERE t.toegewezenTechnieker = :personeelsnummer"),
		@NamedQuery(name = "Ticket.geefGewijzigdeTicketsVanTechnieker", query = "SELECT t FROM Ticket t WHERE t.toegewezenTechnieker = :werknemer AND t.bekekenDoorTechnieker = false "),
		@NamedQuery(name = "Ticket.geefAlleGewijzigdeTickets", query = "SELECT t FROM Ticket t WHERE t.bekekenDoorTechnieker = false ") })
public class Ticket {

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Werknemer toegewezenTechnieker;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ElementCollection
	private Collection<String> bijlages;
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.PERSIST)
	private Collection<Comment> opmerkingen;
	@ManyToOne
	private Contract contract;
	private String titel;
	private String omschrijving;
	private Boolean bekekenDoorTechnieker;
	private Dienst dienst;
	private TicketUrgency type;
	private TicketStatus status;
	private LocalDateTime datumAanmaak;
	private LocalDateTime datumAfgehandeld;
	private Boolean bekekenDoorKlant;

	protected Ticket() {
	}

	public Ticket(String title, TicketUrgency type, String omschrijving, Collection<String> bijlages, Contract contract,
			Dienst dienst) {
		setType(type);
		setTitel(title);
		setContract(contract);
		setDienst(dienst);
		setOmschrijving(omschrijving);
		setBijlages(bijlages);
		setBekekenDoorTechnieker(false);
		setBekekenDoorKlant(false);
		setStatus(TicketStatus.Created);
		datumAanmaak = LocalDateTime.now();
		contract.addTicket(this);
	}

	public boolean isAfgehandeld() {
		return status == TicketStatus.Finished;
	}

	public boolean isOptijdAfgehandeld() {
		if (!isAfgehandeld()) {
			return false;
		} else {
			int afhandeltijd = contract.getContractType().getTicketAfhandeltijd();
			return Duration.between(datumAanmaak, datumAfgehandeld).toDays() < afhandeltijd;
		}
	}

	/**
	 * Returnt de doorlooptijd in uren
	 * 
	 * @return
	 */
	public int berekenDoorlooptijd() {
		if (!isAfgehandeld()) {
			throw new IllegalStateException("A ticket must be finished before checking its duration");
		}
		return (int) Duration.between(datumAanmaak, datumAfgehandeld).toHours();
	}

	private void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public void setBijlages(Collection<String> bijlages) {
		this.bijlages = bijlages;
	}

	public void setOpmerkingen(Collection<Comment> opmerkingen) {
		this.opmerkingen = opmerkingen;
	}

	public void setDatumAfgehandeld(LocalDateTime datumAfgehandeld) {
		this.datumAfgehandeld = datumAfgehandeld;
	}

	public void setToegewezenTechnieker(Werknemer toegewezenTechnieker) {
		this.toegewezenTechnieker = toegewezenTechnieker;
	}

	public void setBekekenDoorTechnieker(Boolean bekekenDoorTechnieker) {
		this.bekekenDoorTechnieker = bekekenDoorTechnieker;
	}

	public void setBekekenDoorKlant(Boolean bekekenDoorKlant) {
		this.bekekenDoorKlant = bekekenDoorKlant;
	}

	public void setDienst(Dienst dienst) {
		if (dienst != null) {
			this.dienst = dienst;
		} else {
			throw new IllegalArgumentException("a division can not be empty");
		}
	}

	public void setTitel(String titel) {
		if (titel != null && !titel.isBlank() && !titel.isEmpty()) {
			this.titel = titel;
		} else {
			throw new IllegalArgumentException("a Ticket titel can not be empty");
		}
	}

	public void setType(TicketUrgency type) {
		if (type != null) {
			this.type = type;
		} else {
			throw new IllegalArgumentException("a Ticket type can not be empty");
		}
	}

	public void setOmschrijving(String omschrijving) {
		if (omschrijving != null && !omschrijving.isBlank() && !omschrijving.isEmpty()) {
			this.omschrijving = omschrijving;
		} else {
			throw new IllegalArgumentException("A ticket description can not be empty");
		}
	}

	public boolean isWijzigbaar() {
		return (this.status == TicketStatus.Cancelled || this.status == TicketStatus.Finished);
	}

	public int getId() {
		return this.id;
	}

	public Contract getContract() {
		return this.contract;
	}

	public String getTitel() {
		return this.titel;
	}

	public Dienst getDienst() {
		return this.dienst;
	}

	public TicketUrgency getType() {
		return this.type;
	}

	public TicketStatus getStatus() {
		return this.status;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public LocalDateTime getDatumAanmaak() {
		return this.datumAanmaak;
	}

	public Collection<String> getBijlages() {
		return this.bijlages;
	}

	public Boolean getBekekenDoorTechnieker() {
		return bekekenDoorTechnieker;
	}

	public Collection<Comment> getOpmerkingen() {
		Collection<Comment> opmerkingen = this.opmerkingen;
		opmerkingen.stream().sorted(new Comparator<Comment>() {
			@Override
			public int compare(Comment c1, Comment c2) {
				return c1.getTijdstip().compareTo(c2.getTijdstip());
			}
		}).collect(Collectors.toList());
		return opmerkingen;
	}

	public LocalDateTime getDatumAfgehandeld() {
		return this.datumAfgehandeld;
	}

	public Werknemer getToegewezenTechnieker() {
		return this.toegewezenTechnieker;
	}
}