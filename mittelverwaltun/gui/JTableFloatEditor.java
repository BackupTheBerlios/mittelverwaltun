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
 * Implements a cell editor that uses a formatted text field
 * to edit Float values. You can set a minimum size or/and
 * maximum size.
 */
public class JTableFloatEditor extends DefaultCellEditor {
    JFormattedTextField ftf;
    NumberFormat floatFormat;
    private Float minimum, maximum;
    private boolean DEBUG = false;

	public JTableFloatEditor() {
		super(new JFormattedTextField());
		init(Float.MIN_VALUE, Float.MAX_VALUE);
	}

	public JTableFloatEditor(float min) {
		super(new JFormattedTextField());
		init(min, Float.MAX_VALUE);
	}

    public JTableFloatEditor(float min, float max) {
        super(new JFormattedTextField());
        init(min, max);
    }

    private void init(float min, float max){
		ftf = (JFormattedTextField)getComponent();
		minimum = new Float(min);
		maximum = new Float(max);

		//Set up the editor for the integer cells.
		floatFormat = NumberFormat.getNumberInstance();
		NumberFormatter floatFormatter = new NumberFormatter(floatFormat);
		floatFormatter.setFormat(floatFormat);
		floatFormatter.setMinimum(minimum);
		floatFormatter.setMaximum(maximum);

		ftf.setFormatterFactory(
				new DefaultFormatterFactory(floatFormatter));
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
        if (o instanceof Float) {
            return o;
        } else if (o instanceof Number) {
            return o; //new Integer(((Number)o).intValue());
        } else {
            if (DEBUG) {
                System.out.println("getCellEditorValue: o isn't a Number");
            }
            try {
                return floatFormat.parseObject(o.toString());
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
