package gui;

import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class DateTextField extends JFormattedTextField {
	SimpleDateFormat dateFormat;
	DateFormatter dateFormatter;

	public DateTextField(){
		super();
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		dateFormatter = new DateFormatter(dateFormat);
		setFormatterFactory(new DefaultFormatterFactory(
							new DateFormatter(dateFormat),
							new DateFormatter(dateFormat),
							dateFormatter));
		
	}
}
