package gui;

import javax.swing.JLabel;

import dbObjects.ZVUntertitel;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
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
