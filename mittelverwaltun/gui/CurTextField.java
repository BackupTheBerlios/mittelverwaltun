package gui;


import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;



public class CurTextField extends JTextField implements FocusListener {
	double amount = 0;

	public CurTextField(){
		super();
		this.addFocusListener( this );
	}

	public float getAmount() {
		return (float)amount;
	}

	public void setAmount( float amount ) {
		long tmp = Math.round( amount * 100 );
		this.amount = tmp / 100.0;
		setText( "" + this.amount + " €" );
	}

	public void clearText() {
		amount = 0;
		setText( "" );
	}

	float getCurrentValue() {
		float res;

		try {
			res = Float.parseFloat( this.getText() );
		} catch (Exception e) {
			res = 0;
		}

		return res;
	}

	public void focusGained(FocusEvent e) {
		setText( "" + amount );
	}

	public void focusLost(FocusEvent e) {
		long tmp = Math.round( getCurrentValue() * 100 );
		this.amount = tmp / 100.0;
		setText( "" + this.amount + " €" );
	}
}