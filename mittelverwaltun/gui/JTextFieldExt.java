/*
 * Created on 01.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JTextFieldExt extends JTextField {

		public JTextFieldExt(int cols) {
			super(cols);
	    }
	  	      	      
	    protected Document createDefaultModel() {
	     	return new LengthRistrictedDocument(this.getColumns());
	    }
	  
	    private class LengthRistrictedDocument extends PlainDocument {
	    	int maxCols;
	      	  
	      	public LengthRistrictedDocument (int cols){
	      		super();
	      	  	this.maxCols = cols;
	      	}
	      	  
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	        	
	  	        if (str == null) {
	  	        	return;
	  	        }
	  	          
	  	        super.insertString(offs, str, a);
	  	        if (getLength() > maxCols){
	  	        	super.remove(maxCols, getLength()- maxCols);
	  	        }
	  	    }
	    }
}
