package gui;

import javax.swing.JLabel;

import dbObjects.ZVUntertitel;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public abstract class ZVKonten {
	ZVUntertitel zvTitel;
	JLabel labKapitel;
	JLabel labTitel;
	JLabel labUT;
	
	public void setZVTitel(ZVUntertitel zvTitel){
		this.zvTitel = zvTitel;
		labKapitel.setText(zvTitel.getZVTitel().getZVKonto().getKapitel());
		labTitel.setText(zvTitel.getTitel());
		labUT.setText(zvTitel.getUntertitel());
	}
}
