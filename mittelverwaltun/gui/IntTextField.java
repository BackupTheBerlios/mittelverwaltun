package gui;


import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.JTextField;

/**
 * TextFeld, welches nur eine bestimmte L�nge einer Integer-Zahl akzeptiert. <br>
 * Ist die eingegebene Zahl l�nger als die angegebene L�nge, dann wird diese abgeschnitten. <br>
 * Ist diese k�rzer, dann werden vorne 0 eingef�gt. <br>
 * Enth�lt das Feld andere Zeichen au�er Ziffern, dann werden diese entfernt. 
 */
public class IntTextField extends JTextField implements FocusListener {
	int size;

	/**
	 * Erzeugt ein <code>IntTextField</code>, dass nur eine bestimmte L�nge von Ziffern akzeptiert. 
	 * @param size = Die gew�nschte L�nge der Zahl. 
	 */
	public IntTextField( int size){
		super();
		this.size = size;
		this.addFocusListener( this );
		this.setText( checkPattern( "" ) );
	}

	/**
	 * Das Pattern f�r die eingegebene Zahl. 
	 * @return Pattern nach die �berpr�fung der eingegeben Zahl vorgenommen wird. 
	 */
	String getPattern(){
		return "([0-9])";
	}

	/**
	 * Neue Zahl dem TextFeld zuweisen. 
	 * @param str
	 */
	public void setValue( String str ) {
		this.setText( checkPattern( str ) );
	}

	/**
	 * �berpr�fung der eingegebenen Zahl. 
	 * @param str = Die eingegebene Zahl im TextFeld. 
	 * @return Korigierte Zahl als String.
	 */
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

	/**
	 * Wenn das TextFeld den Focus erh�lt, dann findet eine �berpr�fung der Zahl statt. 
	 */
	public void focusGained(FocusEvent e) {
		this.setText( checkPattern( this.getText() ) );
	}

	/**
	 * Wenn das TextFeld den Focus verliert, dann findet eine �berpr�fung der Zahl statt. 
	 */
	public void focusLost(FocusEvent e) {
		this.setText( checkPattern( this.getText() ) );
	}

}