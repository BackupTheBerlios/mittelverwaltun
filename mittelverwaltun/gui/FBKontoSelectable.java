package gui;

import dbObjects.FBUnterkonto;

/**
 * @author robert
 *
 * Schnittstelle zum Auswählen eines FBKontos
 */
public interface FBKontoSelectable {

	/**
	 * setzt das FBKonto
	 * @param fbKonto
	 */
	public void setFBKonto(FBUnterkonto fbKonto);
}
