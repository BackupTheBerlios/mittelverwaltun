package gui;

import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

import applicationServer.ApplicationServerException;
import applicationServer.ConnectionException;

public class LoginDialog extends JDialog implements ActionListener{
	GridBagLayout gbl;
	JButton buAnmelden;
	JButton buAbbrechen;
	JTextField tfBenutzername;
	JPasswordField tfPasswort;
	Container contentPane;	
	MainFrame frame;
	

	public LoginDialog(MainFrame frame, String title, boolean modal) {
	  super(frame, title, modal);
	  
	  this.addWindowListener( new WindowAdapter() {
	  	public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			e.getWindow().dispose();
		}
	  });
	  
	  this.frame = frame;
	  contentPane = this.getContentPane();	  
	  contentPane.setLayout( null );

	  Setting.setPosAndLoc( contentPane, new JLabel( "Benutzername" ), 10, 10, 100, 16 );
	  Setting.setPosAndLoc( contentPane, new JLabel( "Passwort" ), 10, 50, 100, 16 );
	  Setting.setPosAndLoc( contentPane, tfBenutzername = new JTextField(), 120, 10, 100, 22 );
	  Setting.setPosAndLoc( contentPane, tfPasswort = new JPasswordField(), 120, 50, 100, 22 );
	  tfPasswort.addKeyListener(new PasswordTF_keyAdapter(this));
	  Setting.setPosAndLoc( contentPane, buAbbrechen = new JButton( "Abbrechen" ), 10, 100, 100, 25 );
	  Setting.setPosAndLoc( contentPane, buAnmelden = new JButton( "Anmelden" ), 120, 100, 100, 25 );
	  
	  buAnmelden.addActionListener( this );
	  buAbbrechen.addActionListener( this );
	  
	  this.setBounds( 300, 300, 235, 160 );
	  this.setResizable( false );
	}

	public void actionPerformed(ActionEvent e) {
		
		if( e.getSource() == buAnmelden ) {
			if(tfBenutzername.getText().equals("") || tfPasswort.getPassword().length == 0){
				JOptionPane.showMessageDialog(
						this,
						"Benutzername oder/und Passwort ist/sind leer !!!",
						"Warnung",
						JOptionPane.ERROR_MESSAGE);
			}else{
				
				String psw = "";
				
				try{
					PasswordEncrypt pe = new PasswordEncrypt();
					psw = pe.encrypt(new String(tfPasswort.getPassword()).toString());
				}catch(Exception ex){
					System.out.println("MessageDigest Fehler");
				}	
				
				try{
					frame.setBenutzer(frame.getApplicationServer().login(tfBenutzername.getText(), psw));
					frame.sendBenutzer();
					this.dispose();
				}catch (ApplicationServerException e1) {
					JOptionPane.showMessageDialog(
								this,
								e1.getMessage(),
								"Warnung",
								JOptionPane.ERROR_MESSAGE);
				}catch(ConnectionException exc){
					JOptionPane.showMessageDialog(
							this,
							exc.getMessage(),
							"Warnung",
							JOptionPane.ERROR_MESSAGE);
				}	
			}
		}else if( e.getSource() == buAbbrechen ) {
			this.dispose();
			frame.onWindowClosing();
		}
	}
	
	void tfPasswort_keyPressed(KeyEvent e) {
   		if(KeyEvent.VK_ENTER == e.getKeyCode())
   			buAnmelden.doClick();
	}
}

class PasswordTF_keyAdapter extends java.awt.event.KeyAdapter {
  LoginDialog adaptee;

  PasswordTF_keyAdapter(LoginDialog adaptee) {
	this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
	adaptee.tfPasswort_keyPressed(e);
  }
}
