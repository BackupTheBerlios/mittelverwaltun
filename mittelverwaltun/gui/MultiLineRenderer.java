package gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class MultiLineRenderer extends JTextPane implements TableCellRenderer  {  
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setFont(new Font("Arial", Font.PLAIN, 10));
		setText((String)value);
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//setCaretPosition(getDocument().getLength());
		
		return this;   
	}  
}
