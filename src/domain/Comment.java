package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Ticket ticket;
	private LocalDateTime tijdstip;
	private String opmerking;
	private String persoonDieOpmerkingToevoegt;

	protected Comment() {
	}

	public Comment(String opmerking, String persoon, Ticket ticket) {
		this.tijdstip = LocalDateTime.now();
		this.ticket = ticket;
		setOpmerking(opmerking);
		setPersoonDieOpmerkingToevoegt(persoon);
	}

	public LocalDateTime getTijdstip() {
		return tijdstip;
	}

	public String getOpmerking() {
		return opmerking;
	}

	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	public String getPersoonDieOpmerkingToevoegt() {
		return persoonDieOpmerkingToevoegt;
	}

	public void setPersoonDieOpmerkingToevoegt(String persoonDieOpmerkingToevoegt) {
		this.persoonDieOpmerkingToevoegt = persoonDieOpmerkingToevoegt;
	}

}
