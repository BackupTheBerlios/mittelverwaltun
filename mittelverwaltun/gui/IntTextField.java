package gui;


import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.JTextField;

/**
 * 
 */
public class IntTextField extends JTextField implements FocusListener {
	int size;

	public IntTextField( int size){
		super();
		this.size = size;
		this.addFocusListener( this );
		this.setText( checkPattern( "" ) );
	}

	String getPattern(){
		return "([0-9])";
	}


	public void setValue( String str ) {
		this.setText( checkPattern( str ) );
	}

	String checkPattern( String str ){
		String res = "";

		for( int i = 0; i < str.length(); i++ ){
			if( Pattern.matches( getPattern(), str.substring(i, i + 1) ) )
				res += str.substring( i, i + 1 );
		}

		if( res.length() >= size ) {
			return res.substring( 0, size );
		} else {
			for( int j = res.length(); j < size; j++)
				res = "0" + res;

			return res;
		}
	}

	public void focusGained(FocusEvent e) {
		this.setText( checkPattern( this.getText() ) );
	}

	public void focusLost(FocusEvent e) {
		this.setText( checkPattern( this.getText() ) );
	}

}