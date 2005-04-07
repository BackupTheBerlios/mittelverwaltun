package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;

import applicationServer.ApplicationServerException;
import dbObjects.*;

/**
 * Frame zum Verwalten der Firmen. <br>
 * Es sind folgende Funktionen implementiert :<br>
 * 1. Anlegen einer neuen Firma.<br>
 * 2. Aktualisieren einer vorhandenen Firma.<br>
 * 3. Löschen einer vorhandenen Firma. <br>
 * @author w.flat
 **/
public class Firmenverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener {
	
	JScrollPane scrollList = new JScrollPane();
	DefaultListModel listModelFirmen = new DefaultListModel();
	JList listFirmen = new JList( listModelFirmen );
	JLabel labName = new JLabel();
	JTextField tfFirma = new JTextField();
	JLabel labStrasseNr = new JLabel();
	JTextField tfStrasseNr = new JTextField();
	JLabel labOrt = new JLabel();
	JTextField tfOrt = new JTextField();
	JLabel labPLZ = new JLabel();
	IntTextField tfPLZ = new IntTextField( 5 );
	JLabel labTelNr = new JLabel();
	JLabel labFaxNr = new JLabel();
	JLabel labEMail = new JLabel();
	JTextField tfTelNr = new JTextField();
	JTextField tfFaxNr = new JTextField();
	JTextField tfEMail = new JTextField();
	JLabel labWWW = new JLabel();
	JTextField tfWWW = new JTextField();
	JButton buAnlegen = new JButton();
	JButton buLoeschen = new JButton();
	JButton buAendern = new JButton();
	JButton buBeenden = new JButton();
	JButton buAktualisieren = new JButton();
	JLabel labKundenNr = new JLabel();
	JTextField tfKundenNr = new JTextField();
	
	MainFrame frame;
	
	/**
	 * Erzeugen vom Frame zum Verwalten der Firmen. 
	 * @param frame = MainFrame in dem das JInternalFrame liegt und welches den ApplicationServer besitzt.
	 * author w.flat
	 */
	public Firmenverwaltung( MainFrame frame ) {
		super( "Firmenverwaltung" );
		this.setClosable( true );
		this.setIconifiable( true);
		this.getContentPane().setLayout( null );
		this.frame = frame;

		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		loadFirmen();
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}
	
	/**
	 * Initialisierung der graphischen Oberfläche. 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
		labName.setText("Name *");
		labName.setBounds(new Rectangle(220, 10, 80, 15));
		labStrasseNr.setText("Straße Nr. *");
		labStrasseNr.setBounds(new Rectangle(220, 40, 80, 15));
		labOrt.setText("Ort *");
		labOrt.setBounds(new Rectangle(370, 70, 50, 15));
		labPLZ.setText("PLZ *");
		labPLZ.setBounds(new Rectangle(220, 70, 80, 15));
		labTelNr.setText("Tel. Nr.");
		labTelNr.setBounds(new Rectangle(220, 130, 80, 15));
		labFaxNr.setText("Fax. Nr.");
		labFaxNr.setBounds(new Rectangle(220, 160, 80, 15));
		labEMail.setText("E-Mail");
		labEMail.setBounds(new Rectangle(220, 190, 80, 15));
		labWWW.setVerifyInputWhenFocusTarget(true);
		labWWW.setText("www");
		labWWW.setBounds(new Rectangle(220, 220, 80, 15));
		tfFirma.setText("");
		tfFirma.setBounds(new Rectangle(300, 10, 300, 21));
		tfStrasseNr.setText("");
		tfStrasseNr.setBounds(new Rectangle(300, 40, 300, 21));
		tfOrt.setText("");
		tfOrt.setBounds(new Rectangle(420, 70, 180, 21));
		tfPLZ.setText("");
		tfPLZ.setBounds(new Rectangle(300, 70, 60, 21));
		tfTelNr.setText("");
		tfTelNr.setBounds(new Rectangle(300, 130, 140, 21));
		tfFaxNr.setText("");
		tfFaxNr.setBounds(new Rectangle(300, 160, 140, 21));
		tfEMail.setText("");
		tfEMail.setBounds(new Rectangle(300, 190, 140, 21));
		tfWWW.setText("");
		tfWWW.setBounds(new Rectangle(300, 220, 140, 21));
		this.setLocale(java.util.Locale.getDefault());
		buAnlegen.setBounds(new Rectangle(450, 130, 150, 25));
		buAnlegen.setText("Anlegen");
		buLoeschen.setBounds(new Rectangle(450, 190, 150, 25));
		buLoeschen.setText("Löschen");
		buAendern.setBounds(new Rectangle(450, 160, 150, 25));
		buAendern.setText("Ändern");
		buBeenden.setBounds(new Rectangle(450, 220, 150, 25));
		buBeenden.setText("Beenden");
		buAktualisieren.setBounds(new Rectangle(450, 100, 150, 25));
		buAktualisieren.setText("Aktualisieren");
		labKundenNr.setText("Kunden-Nr.");
		labKundenNr.setBounds(new Rectangle(218, 100, 80, 15));
		tfKundenNr.setText("");
		tfKundenNr.setBounds(new Rectangle(300, 100, 140, 21));
		scrollList.setBounds(new Rectangle(10, 10, 200, 235));
		this.getContentPane().add(scrollList, null);
		this.getContentPane().add(labName, null);
		this.getContentPane().add(tfFirma, null);
		this.getContentPane().add(labStrasseNr, null);
		this.getContentPane().add(tfStrasseNr, null);
		scrollList.getViewport().add(listFirmen, null);
		this.getContentPane().add(labPLZ, null);
		this.getContentPane().add(tfPLZ, null);
		this.getContentPane().add(labOrt, null);
		this.getContentPane().add(tfOrt, null);
		this.getContentPane().add(labEMail, null);
		this.getContentPane().add(labFaxNr, null);
		this.getContentPane().add(tfFaxNr, null);
		this.getContentPane().add(labTelNr, null);
		this.getContentPane().add(tfTelNr, null);
		this.getContentPane().add(labKundenNr, null);
		this.getContentPane().add(tfKundenNr, null);
		this.getContentPane().add(buAktualisieren, null);
		this.getContentPane().add(buAnlegen, null);
		this.getContentPane().add(buAendern, null);
		this.getContentPane().add(buLoeschen, null);
		this.getContentPane().add(buBeenden, null);
		this.getContentPane().add(tfEMail, null);
		this.getContentPane().add(labWWW, null);
		this.getContentPane().add(tfWWW, null);
		
		this.buAktualisieren.addActionListener( this );
		this.buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		this.buAnlegen.addActionListener( this );
		this.buAnlegen.setIcon(Functions.getAddIcon(getClass()));
		this.buLoeschen.addActionListener( this );
		this.buLoeschen.setIcon(Functions.getDelIcon(getClass()));
		this.buAendern.addActionListener( this );
		this.buAendern.setIcon(Functions.getEditIcon(getClass()));
		this.buBeenden.addActionListener( this );
		this.buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		
		listFirmen.addListSelectionListener( this );
		listFirmen.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		
		this.setSize( 618, 288 );
	}
	
	public static void main(String[] args) {
		JFrame test = new JFrame("FB-Kontenverwaltung");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setContentPane(desk);
		test.setBounds(100,100,650,350);
		JInternalFrame child = new Firmenverwaltung( null );
	  
		test.addWindowListener( new WindowAdapter() {
		  public void windowClosing(WindowEvent e) {
			  super.windowClosing(e);
			  e.getWindow().dispose();
			  System.exit(0);
		  }
		});
	  
		desk.add(child);
		test.show();
		child.show();
	}
	
	/**
	 * Anzeigen der ausgewählten Firma in den Textfeldern.
	 */
	void showFirma() {
		Firma firma = ((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() ));
		
		this.tfFirma.setText( firma.getName() );
		this.tfStrasseNr.setText( firma.getStrasseNr() );
		this.tfPLZ.setValue( firma.getPlz() );
		this.tfOrt.setText( firma.getOrt() );
		this.tfKundenNr.setText( firma.getKundenNr() );
		this.tfTelNr.setText( firma.getTelNr() );
		this.tfFaxNr.setText( firma.getFaxNr() );
		this.tfEMail.setText( firma.getEMail() );
		this.tfWWW.setText( firma.getWWW() );
		if(firma.getASK())
			this.buLoeschen.setEnabled(false);
		else
			this.buLoeschen.setEnabled(true);
	}
	
	/**
	 * Alle Textfelder löschen.
	 */
	void clearTextFields() {
		this.tfFirma.setText( "" );
		this.tfStrasseNr.setText( "" );
		this.tfPLZ.setValue( "" );
		this.tfOrt.setText( "" );
		this.tfKundenNr.setText( "" );
		this.tfTelNr.setText( "" );
		this.tfFaxNr.setText( "" );
		this.tfEMail.setText( "" );
		this.tfWWW.setText( "" );
	}

	
	/**
	 * Laden der Firmen aus der Datenbank in die Liste.
	 */
	void loadFirmen() {
		if( frame != null ) {
			try {
				listModelFirmen.clear();		// Die aktuelle Liste löschen
				ArrayList list = frame.getApplicationServer().getFirmen();	// Alle Firmen aus der DB holen
				// Alle Firmen an die Liste übertragen
				for( int i = 0; i < list.size(); i++ ) {
					listModelFirmen.addElement( list.get( i ) );
				}
				// Wenn es Firmen gibt, dann das erste Element in der Liste auswählen und anzeigen
				if( list.size() > 0 ) {
					listFirmen.setSelectedIndex( 0 );
					showFirma();
				} else {		// Sonst werden nur die Felder gelöscht
					clearTextFields();
				}
			} catch ( ApplicationServerException e ) {
				System.out.println( e.toString() );
			} catch(RemoteException re) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
														"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
			}
		}
	}
	
	/**
	 * Ermittlung der Fehler, die beim Erstellen oder Aktualisieren einer Firma lokal aufgetretten sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	String getError() {
		String error = "";
		
		if( tfFirma.getText().equalsIgnoreCase( "" ) ) {
			error = " - Der Firmenname darf nicht leer sein.\n";
		}
		if( tfStrasseNr.getText().equalsIgnoreCase( "" ) ) {
			error = " - Die Strassenangabe darf nicht leer sein.\n";
		}
		if( tfPLZ.getText().equalsIgnoreCase( "" ) ) {
			error = " - Die PLZ-Angabe darf nicht leer sein.\n";
		}
		if( tfOrt.getText().equalsIgnoreCase( "" ) ) {
			error = " - Der Ortsangabe darf nicht leer sein.\n";
		}
		
		return error;
	}
	
	/**
	 * Eine neue Firma an Hand der Eingaben erstellen.
	 * @return Die neu erstellte Firma.
	 */
	Firma getNewFirma() {
		return new Firma( 0, tfFirma.getText(), tfStrasseNr.getText(), tfPLZ.getText(), tfOrt.getText(),
						tfKundenNr.getText(), tfTelNr.getText(), tfFaxNr.getText(), 
						tfEMail.getText(), tfWWW.getText(), false, false );
	}
	
	/**
	 * Eine Firma erstellen.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	String addFirma() {
		String error = getError();
		
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				Firma firma = getNewFirma();
				firma.setId( frame.getApplicationServer().addFirma( firma ) );
				listModelFirmen.addElement( firma );
			} catch( ApplicationServerException e ) {
				error = " - " + e.toString() + ".\n";
			} catch(RemoteException re) {
				error = " - Fehler bei RMI-Kommunikation.\n";
			}
		}
		
		return error;
	}
	
	/**
	 * Eine Firma aktualisieren.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	String setFirma() {
		String error = getError();
		
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				Firma copy = (Firma)(((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() )).clone());
				copy.setFirma( getNewFirma() );
				if( frame.getApplicationServer().setFirma( copy ) == copy.getId() ) {
					((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() )).setFirma( copy );
					listModelFirmen.setElementAt( listModelFirmen.get( listFirmen.getSelectedIndex() ),
													listFirmen.getSelectedIndex() );
				} else {
					error = " - Ausnahmefehler aufgetretten.\n";
				}
			} catch( ApplicationServerException e ) {
				error = " - " + e.toString() + ".\n";
			} catch(RemoteException re) {
				error = " - Fehler bei RMI-Kommunikation.\n";
			}
		}
		
		return error;
	}
	
	/**
	 * Eine Firma löschen.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	String delFirma() {
		String error = getError();
		
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				Firma firma = ((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() ));
				if( frame.getApplicationServer().delFirma( firma ) > 0 ) {
					listModelFirmen.remove( listFirmen.getSelectedIndex() );
					if( listModelFirmen.size() > 0 )
						listFirmen.setSelectedIndex( 0 );
				} else {
					error = " - Ausnahmefehler aufgetretten.\n";
				}
			} catch( ApplicationServerException e ) {
				error = " - " + e.toString() + ".\n";
			} catch(RemoteException re) {
				error = " - Fehler bei RMI-Kommunikation.\n";
			}
		}
		
		return error;
	}
	
	/**
	 * Nachricht beim Löschen einer Firma.
	 * @return Nachricht die beim Löschen angezeigt wird. 
	 */
	String getDelMessage() {
		return "Wollen Sie die Firma\n" + ((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() )).toString()
				+ "\nwirklich löschen."; 
	}
	
	/**
	 * Nachricht beim Anlegen einer Firma.
	 * @return Nachricht die beim Anlegen angezeigt wird. 
	 */
	String getAddMessage() {
		return "Wollen Sie die Firma\n" + tfFirma.getText() + "\nwirklich anlegen.";
	}

	/**
	 * Nachricht beim Ändern einer Firma.
	 * @return Nachricht die beim Aktualisieren angezeigt wird. 
	 */
	String getEditMessage() {
		return "Wollen Sie die Firma\n" + ((Firma)listModelFirmen.get( listFirmen.getSelectedIndex() )).toString()
				+ "\nwirklich ändern.";
	}


	/**
	 * Reaktion auf Knopf-Ereignisse.
	 */
	public void actionPerformed( ActionEvent e ) {
		String error = "";
		
		if ( e.getSource() == buAktualisieren ) {
			loadFirmen();
		} else if ( e.getSource() == buAnlegen ) {
			if( frame == null )
				return;
			if( JOptionPane.showConfirmDialog( this, getAddMessage(), "Anlegen ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
				error = addFirma();
			}
		} else if ( e.getSource() == buLoeschen ) {
			if( frame == null || listFirmen.getSelectedIndex() < 0 )
				return;
			if( JOptionPane.showConfirmDialog( this, getDelMessage(), "Löschen ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
				error = delFirma();
			}
		} else if ( e.getSource() == buAendern ) {
			if( frame == null || listFirmen.getSelectedIndex() < 0 )
				return;
			if( JOptionPane.showConfirmDialog( this, getEditMessage(), "Ändern ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
				error = setFirma();
			}
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		
		if( !error.equalsIgnoreCase( "" ) ) {
			error = "Folgende Fehler sind aufgetreten:\n" + error;
			JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Reaktion auf das Klicken in der Liste.
	 */
	public void valueChanged( ListSelectionEvent e ) {
		if( listFirmen.getSelectedIndex() >= 0 )
			showFirma();
		else
			clearTextFields();
	}

}
