package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import domain.enumerations.ContractDoorlooptijd;
import domain.enumerations.ContractStatus;
import domain.enumerations.ContractTypeStatus;
import domain.enumerations.TicketAanmaakManier;
import domain.enumerations.TicketAanmaakTijd;
import util.Controles;

@Entity
@NamedQueries({
		@NamedQuery(name = "ContractType.bestaatContractTypeNaam", query = "SELECT c FROM ContractType c where c.naam = :naam") })
public class ContractType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String naam;
	@ElementCollection
	private Set<TicketAanmaakManier> ticketAanmaakManier;
	private TicketAanmaakTijd ticketAanmaakTijd;
	private int ticketAfhandeltijd; // in aantal dagen
	private ContractDoorlooptijd minimaleDoorlooptijd;
	private BigDecimal prijs;
	private ContractTypeStatus status;
	@OneToMany(mappedBy = "contractType", cascade = CascadeType.PERSIST)
	private Collection<Contract> contracten;

	protected ContractType() {
	}

	public ContractType(String naam, Set<TicketAanmaakManier> ticketAanmaakManier, TicketAanmaakTijd ticketAanmaakTijd,
			int ticketAfhandeltijd, ContractDoorlooptijd minimaleDoorlooptijd, BigDecimal prijs) {
		setNaam(naam);
		setTicketAanmaakManier(ticketAanmaakManier);
		setTicketAanmaakTijd(ticketAanmaakTijd);
		setTicketAfhandeltijd(ticketAfhandeltijd);
		setMinimaleDoorlooptijd(minimaleDoorlooptijd);
		setPrijs(prijs);
		setStatus(ContractTypeStatus.Active);
		contracten = new ArrayList<>();
	}

	public int getAantalLopendeContracten() {
		return (int) contracten.stream().filter(e -> e.getStatus() == ContractStatus.Active).count();
	}

	public void addContract(Contract contract) {
		contracten.add(contract);
	}

	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String value) {
		Controles.stringNotEmpty(value, "The contractType name may not be empty");
		this.naam = value;
	}

	public Set<TicketAanmaakManier> getTicketAanmaakManier() {
		return this.ticketAanmaakManier;
	}

	public void setTicketAanmaakManier(Set<TicketAanmaakManier> value) {
		if (value == null)
			throw new IllegalArgumentException("The ticket must have a creation type");
		this.ticketAanmaakManier = value;
	}

	public TicketAanmaakTijd getTicketAanmaakTijd() {
		return this.ticketAanmaakTijd;
	}

	public void setTicketAanmaakTijd(TicketAanmaakTijd value) {
		if (value == null)
			throw new IllegalArgumentException("The ticket must have a creation time");
		this.ticketAanmaakTijd = value;
	}

	public int getTicketAfhandeltijd() {
		return this.ticketAfhandeltijd;
	}

	public void setTicketAfhandeltijd(int value) {
		if (value < 0)
			throw new IllegalArgumentException("The Ticket runtime must be positive");
		this.ticketAfhandeltijd = value;
	}

	public ContractDoorlooptijd getMinimaleDoorlooptijd() {
		return this.minimaleDoorlooptijd;
	}

	public void setMinimaleDoorlooptijd(ContractDoorlooptijd value) {
		if (value == null)
			throw new IllegalArgumentException("The ticket must have a minimum runtime");
		this.minimaleDoorlooptijd = value;
	}

	public java.math.BigDecimal getPrijs() {
		return this.prijs.setScale(2);
	}

	public void setPrijs(java.math.BigDecimal value) {
		if (value == null)
			throw new IllegalArgumentException("The ticket price may not be empty");
		if (value.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("The ticket must have a positive price");
		this.prijs = value;
	}

	public ContractTypeStatus getStatus() {
		return this.status;
	}

	public void setStatus(ContractTypeStatus value) {
		if (value == null)
			throw new IllegalArgumentException("The ticket must have a status");
		this.status = value;
	}

	public Collection<Contract> getContracten() {
		return contracten;
	}

	public void setContracten(Collection<Contract> contracten) {
		this.contracten = contracten;
	}

}
