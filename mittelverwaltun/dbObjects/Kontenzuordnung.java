package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Kontenzuordnung implements Serializable {

	private short status;

	private ZVKonto zvKonto;

	
	public Kontenzuordnung(short status, ZVKonto zvKonto) {
		this.status = status;
		this.zvKonto = zvKonto;
	}
	
	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public ZVKonto getZvKonto() {
		return zvKonto;
	}

	public void setZvKonto(ZVKonto zvKonto) {
		this.zvKonto = zvKonto;
	}

	public String toString(){
		return zvKonto.toString();
	}
}
