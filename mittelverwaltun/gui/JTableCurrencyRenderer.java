package gui;

import java.text.NumberFormat;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;


/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class JTableCurrencyRenderer
	extends DefaultTableCellRenderer
	 {

		public JTableCurrencyRenderer() {
			super();
			setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		  }

		  public void setValue(Object value) {
			if ( (value != null) && (value instanceof Number)) {
			  Number numberValue = (Number) value;
			 // if(numberValue.doubleValue() > 0){
				NumberFormat formatter = NumberFormat.getCurrencyInstance();

				NumberFormatter nf = new NumberFormatter(formatter);
				nf.setMinimum(new Float(0));
				try{
				  value = nf.valueToString(numberValue);

				}catch(Exception e){
			   // }
				//value = formatter.format(numberValue.doubleValue());
			  }
			}
			super.setValue(value);
		  }
}
