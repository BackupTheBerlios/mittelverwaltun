package gui;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class JTableIntegerEditor extends DefaultCellEditor {
	JFormattedTextField ftf;
	NumberFormat integerFormat;
	private Integer minimum, maximum;
	private boolean DEBUG = false;

	public JTableIntegerEditor() {
		super(new JFormattedTextField());
		init(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public JTableIntegerEditor(int min) {
		super(new JFormattedTextField());
		init(min, Integer.MAX_VALUE);
	}

	public JTableIntegerEditor(int min, int max) {
		super(new JFormattedTextField());
		init(min, max);
	}

	private void init(int min, int max){
		ftf = (JFormattedTextField)getComponent();
		minimum = new Integer(min);
		maximum = new Integer(max);

		//Set up the editor for the integer cells.
		integerFormat = NumberFormat.getNumberInstance();
		NumberFormatter integerFormatter = new NumberFormatter(integerFormat);
		integerFormatter.setFormat(integerFormat);
		integerFormatter.setMinimum(minimum);
		integerFormatter.setMaximum(maximum);

		ftf.setFormatterFactory(
				new DefaultFormatterFactory(integerFormatter));
		ftf.setValue(minimum);
		ftf.setHorizontalAlignment(JTextField.TRAILING);
		ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

		//React when the user presses Enter while the editor is
		//active.  (Tab is handled as specified by
		//JFormattedTextField's focusLostBehavior property.)
		ftf.getInputMap().put(KeyStroke.getKeyStroke(
										KeyEvent.VK_ENTER, 0),
										"check");
		ftf.getActionMap().put("check", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
		if (!ftf.isEditValid()) { //The text is invalid.

				ftf.postActionEvent(); //inform the editor

				} else try {              //The text is valid,
					ftf.commitEdit();     //so use it.
					ftf.postActionEvent(); //stop editing
				} catch (java.text.ParseException exc) { }
			}
		});
	}

	//Override to invoke setValue on the formatted text field.
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected,
			int row, int column) {
		JFormattedTextField ftf =
			(JFormattedTextField)super.getTableCellEditorComponent(
				table, value, isSelected, row, column);
		ftf.setValue(value);
		return ftf;
	}

	//Override to ensure that the value remains an Integer.
	public Object getCellEditorValue() {
		JFormattedTextField ftf = (JFormattedTextField)getComponent();
		Object o = ftf.getValue();
		if (o instanceof Integer) {
			return o;
		} else if (o instanceof Number) {
			return o; //new Integer(((Number)o).intValue());
		} else {
			if (DEBUG) {
				System.out.println("getCellEditorValue: o isn't a Number");
			}
			try {
				return integerFormat.parseObject(o.toString());
			} catch (ParseException exc) {
				System.err.println("getCellEditorValue: can't parse o: " + o);
				return null;
			}
		}
	}

	//Override to check whether the edit is valid,
	//setting the value if it is and complaining if
	//it isn't.  If it's OK for the editor to go
	//away, we need to invoke the superclass's version
	//of this method so that everything gets cleaned up.
	public boolean stopCellEditing() {
		JFormattedTextField ftf = (JFormattedTextField)getComponent();
		if (ftf.isEditValid()) {
			try {
				ftf.commitEdit();
			} catch (java.text.ParseException exc) { }

		} else { //text is invalid
			ftf.setValue(ftf.getValue());
		}
		return super.stopCellEditing();
	}
}
