package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Klasse für die Zuordnung eines ZVKontos zu einem FBKonto
 */
public class Kontenzuordnung implements Serializable {

	/**
	 * Status für die Kontenzuordnung: 0 - nicht gesperrt, 1-gesperrt
	 */
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
