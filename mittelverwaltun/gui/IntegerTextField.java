package gui;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class IntegerTextField extends JFormattedTextField {
	NumberFormat integerFormat;
	NumberFormatter numberFormatter;

	public IntegerTextField(){
		super();
		Init(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public IntegerTextField(int min){
		super();
		Init(min, Integer.MAX_VALUE);
	}

	public IntegerTextField(int min, int max){
		super();
		Init(min, max);
	}

	private void Init(int min, int max){
		integerFormat = NumberFormat.getIntegerInstance();		
		numberFormatter = new NumberFormatter(NumberFormat.getNumberInstance());
		numberFormatter.setMinimum(new Integer(min));
		numberFormatter.setMaximum(new Integer(max));

		setFormatterFactory(new DefaultFormatterFactory(
								new NumberFormatter(integerFormat),
								new NumberFormatter(integerFormat),
								numberFormatter));
	}
}
