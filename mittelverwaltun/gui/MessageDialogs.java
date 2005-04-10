/*
 * Created on 08.09.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Mario
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessageDialogs extends JDialog implements ActionListener, WindowListener{
	
	public static final int CLOSED_OPTION 	= -1;
	public static final int OK_OPTION 		=  0;

	public static final int ERROR_ICON 		= 0;
	public static final int WARNING_ICON 	= 1;
	public static final int INFO_ICON 		= 2;
		
	private int result = OK_OPTION;
	private JButton butDetails = null;
	private JScrollPane jsp = null;

	
	private MessageDialogs(Component parent, String title, String message, String details, int icon){
		super(JOptionPane.getFrameForComponent(parent),title,true);
		this.addWindowListener(this);
		Container cp = getContentPane(); 	
		cp.setLayout( null );
	
		Icon i = null;
		switch (icon){
			case ERROR_ICON:	i = Functions.getStopIcon(getClass());
								break;
			case WARNING_ICON:	i = Functions.getTipOfTheDay24Icon(getClass());
								break;
			case INFO_ICON:		i = Functions.getInformation24Icon(getClass());
								break;
		}
		JLabel labIcon = new JLabel(i);
		labIcon.setBounds(6,6,24,24);
		cp.add(labIcon);
		
		JTextArea labMsg = new JTextArea(message);
		labMsg.setEditable(false);
		labMsg.setBackground(this.getBackground());
		labMsg.setBounds(35,6,300,50);
		labMsg.setLineWrap(true);
		cp.add(labMsg);
			
		JButton butOk = new JButton("OK");
		butOk.addActionListener(this);
		butOk.setBounds(260,61,75,25);
		cp.add(butOk);
		
		butDetails = new JButton("Details anzeigen", Functions.getDown24Icon(getClass()));
		butDetails.setEnabled(!((details==null)||(details.equals(""))));
		butDetails.addActionListener(this);
		butDetails.setBounds(35,61,170,25);
		cp.add(butDetails);
	
		JTextArea jta = new JTextArea(details);
		jta.setEditable(false);
		jta.setLineWrap(true);
		
		jsp = new JScrollPane(jta);
		jsp.setVisible(false);
		jsp.setBounds(35,95,300,69);
		cp.add(jsp);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		d.width = (d.width - 355)/2;
		d.height =(d.height-125)/2;
		this.setBounds( d.width, d.height, 355,125);
		this.setResizable(false);
		
	}
	
	public static int showDetailMessageDialog(Component parent, String title, String message, String details, int icon){
		
		
		MessageDialogs md = new MessageDialogs(parent, title, message, details, icon);
		md.setVisible(true);
		return md.result;
	}

	/**
	 * ActionListener Methoden
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == "OK"){
			result = OK_OPTION;
			this.setVisible(false);
			this.dispose();
		}else if (cmd == "Details anzeigen"){
			butDetails.setText("Details ausblenden");
			butDetails.setIcon(Functions.getUp24Icon(getClass()));
			jsp.setVisible(true);
			this.setBounds( this.getX(), this.getY(), this.getWidth(), 205);
			this.validate();
		}else if (cmd == "Details ausblenden"){
			butDetails.setText("Details anzeigen");
			butDetails.setIcon(Functions.getDown24Icon(getClass()));
			jsp.setVisible(false);
			this.setBounds( this.getX(), this.getY(), this.getWidth(), 125);
			this.validate();
		}
	}

	/**
	 * WindowListener Methoden
	 */
	public void windowOpened(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		this.result = CLOSED_OPTION;
		this.setVisible(false);
		this.dispose();
	}
	
	public void windowClosed(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}

	/**
	 * MAIN***MAIN***MAIN***MAIN
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setVisible(true);
		System.out.println(""+showDetailMessageDialog(f,"Application Server: Fehler","TestTestTestTestTest" + "\n" + "TestTestTestTest\nTestTestTestTest","TestTestTestTestTestTestTest \n TestTestTestTest"+"\n"+"TestTest", MessageDialogs.INFO_ICON));
		f.setVisible(false);
		f.dispose();
	}
}
