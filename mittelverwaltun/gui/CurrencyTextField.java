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
public class CurrencyTextField extends JFormattedTextField {
	NumberFormat currencyFormat;
	NumberFormatter numberFormatter;
	
	public CurrencyTextField(){
		super();
		Init(Float.MIN_VALUE, Float.MAX_VALUE);
	}
	
	public CurrencyTextField(float min){
		super();
		Init(min, Float.MAX_VALUE);
	}
	
	public CurrencyTextField(float min, float max){
		super();
		Init(min, max);
	}
	
	private void Init(float min, float max){
		currencyFormat = NumberFormat.getCurrencyInstance();		
		numberFormatter = new NumberFormatter(NumberFormat.getNumberInstance());
		numberFormatter.setMinimum(new Float(min));
		numberFormatter.setMaximum(new Float(max));

		setFormatterFactory(new DefaultFormatterFactory(
								new NumberFormatter(currencyFormat),
								new NumberFormatter(currencyFormat),
								numberFormatter));
	}

	public void setInterval(float min, float max){
		currencyFormat = NumberFormat.getCurrencyInstance();
		numberFormatter = new NumberFormatter(NumberFormat.getNumberInstance());
		numberFormatter.setMinimum(new Float(min));
		numberFormatter.setMaximum(new Float(max));

		setFormatterFactory(new DefaultFormatterFactory(
								new NumberFormatter(currencyFormat),
								new NumberFormatter(currencyFormat),
								numberFormatter));
	}
}
