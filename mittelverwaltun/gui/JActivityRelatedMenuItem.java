package gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

import dbObjects.Rolle;

/**
 * @author Mario
 */
public class JActivityRelatedMenuItem extends JMenuItem implements ActivityRelatedElement, Comparable{
	
	private int activity = 0;
	private boolean activityEnabled = true;
	
	//	Konstruktoren 
	public JActivityRelatedMenuItem() {
		super();
	}
	public JActivityRelatedMenuItem(int activity) {
		super();
		this.activity = activity;
	}

	public JActivityRelatedMenuItem(Icon icon) {
		super(icon);
	}
	public JActivityRelatedMenuItem(int activity, Icon icon) {
		super(icon);
		this.activity = activity;
	}

	public JActivityRelatedMenuItem(String text) {
		super(text);
	}
	public JActivityRelatedMenuItem(int activity, String text) {
		super(text);
		this.activity = activity;
	}

	public JActivityRelatedMenuItem(Action a) {
		super(a);
	}
	public JActivityRelatedMenuItem(int activity, Action a) {
		super(a);
		this.activity = activity;
	}

	public JActivityRelatedMenuItem(String text, Icon icon) {
		super(text, icon);
	}
	public JActivityRelatedMenuItem(int activity, String text, Icon icon) {
		super(text, icon);
		this.activity = activity;
	}

	public JActivityRelatedMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
	}
	public JActivityRelatedMenuItem(int activity, String text, int mnemonic) {
		super(text, mnemonic);
		this.activity = activity;
	}

	//	Methoden
	public void setEnabled (boolean b){
		if (activityEnabled){
			super.setEnabled(b);
		}
	}
	
	public void setActivityStatus(int[] validActivities){
		
		boolean b = true;
		
		if (this.activity != 0){
			int i=0;
			b = false;
			while ((i < validActivities.length) && !(b = (validActivities[i]==activity))){
				i++;
			}
		}
		
		this.activityEnabled = b;
		super.setEnabled(b);	
	}
	
	public void setActivityStatus(Rolle r){
		setActivityStatus(r.getAktivitaeten());
	}
	
	
	/* Mario: Änderung 31.08.2004 => vgl. Head */	
	public int compareTo(Object o) {
		if (this == o ){
			return 0;
		} else {
			return -1;
		}
	}
}
