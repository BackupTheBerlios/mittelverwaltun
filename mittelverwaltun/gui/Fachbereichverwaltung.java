package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import dbObjects.Fachbereich;
import dbObjects.Institut;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.text.NumberFormat;
import java.text.ParseException;

public class Fachbereichverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener {

	JTextField tfBezeichnung = new JTextField();
	CurrencyTextField tfProfBudget = new CurrencyTextField(0);
	
	JPanel panel = new JPanel();
	
	JScrollPane scrollPanel = new JScrollPane();
	DefaultListModel listModel = new DefaultListModel();
	JList listFachbereiche = new JList(listModel);
	JComboBox cbInstitut = new JComboBox();
  
	JButton buAnlegen = new JButton( "Anlegen" );
	JButton buAendern = new JButton( "Ändern" );
	JButton buLoeschen = new JButton( "Löschen" );
	JButton buBeenden = new JButton( "Beenden" );
	JButton buRefresh = new JButton( "" );
	
	ApplicationServer applicationServer;
	Container contentPane;
	
	public Fachbereichverwaltung(ApplicationServer applicationServer) {
		super( "Fachbereichverwaltung" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.getContentPane().setLayout( null );
		this.applicationServer = applicationServer;
		
		Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 430, 195 );
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout( null );		
		
		Setting.setPosAndLoc( panel, new JLabel( "Fachbereiche" ), 10, 10, 100, 16 );
		
		Setting.setPosAndLoc( panel, new JLabel( "Bezeichnung" ), 170, 30, 100, 16 );
		Setting.setPosAndLoc( panel, tfBezeichnung, 280, 30, 140, 22 );
		
		Setting.setPosAndLoc( panel, new JLabel( "Proffesor Budget" ), 170, 60, 100, 16 );
		Setting.setPosAndLoc( panel, tfProfBudget, 280, 60, 140, 22 );

		Setting.setPosAndLoc( panel, new JLabel( "Institut" ), 170, 90, 100, 16 );
		Setting.setPosAndLoc( panel, cbInstitut, 280, 90, 140, 22 );

		Setting.setPosAndLoc( panel, scrollPanel, 10, 30, 150, 150 );
		scrollPanel.getViewport().add( listFachbereiche, null );
		
		Setting.setPosAndLoc( panel, buRefresh, 100, 5, 20, 20 );
	  buRefresh.setBorder(null);
		buRefresh.setIcon(Functions.getRefreshIcon(getClass()));
		buRefresh.setToolTipText("Aktualisieren");
		buRefresh.addActionListener( this );
		Setting.setPosAndLoc( panel, buAnlegen, 170, 120, 110, 25 );
		buAnlegen.setIcon(Functions.getAddIcon(getClass()));
    //buAnlegen.addActionListener( this );
    buAnlegen.setEnabled(false);
		Setting.setPosAndLoc( panel, buAendern, 310, 120, 110, 25 );
		buAendern.setIcon(Functions.getEditIcon(getClass()));
    buAendern.addActionListener( this );
		Setting.setPosAndLoc( panel, buLoeschen, 170, 155, 110, 25 );
		buLoeschen.setIcon(Functions.getDelIcon(getClass()));
    //buLoeschen.addActionListener( this );
    buLoeschen.setEnabled(false);
		Setting.setPosAndLoc( panel, buBeenden, 310, 155, 110, 25 );
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
    buBeenden.addActionListener( this );
		listFachbereiche.addListSelectionListener(this);
	  listFachbereiche.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		loadInstituts();
		loadFachbereiche();
		this.setSize( 450, 240 );
	}
	
	private void loadFachbereiche(){
		try{
		  Fachbereich[] fachbereiche = applicationServer.getFachbereiche();
			listModel.clear();
		  for(int i = 0; i < fachbereiche.length; i++){
			  listModel.addElement(fachbereiche[i]);
		  }
		  if(fachbereiche.length > 0)
			  listFachbereiche.setSelectedIndex(0);
	  }catch(Exception e){
		  System.out.println(e);
	  }
	}

	private void loadInstituts(){
	  try{
		  Institut[] institute = applicationServer.getInstitutes();

			if(institute != null){
				cbInstitut.removeAllItems();
				  for(int i = 0; i < institute.length; i++){
					  cbInstitut.addItem(institute[i]);
				  }
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }
  
  protected String changeFachbereich(){
		Fachbereich currFachbereich = (Fachbereich)listModel.getElementAt(listFachbereiche.getSelectedIndex());
		
		Number budget = null;
		try {
			budget = NumberFormat.getCurrencyInstance().parse(tfProfBudget.getText());
		} catch (ParseException e) {
			return e.getMessage();
		}
		Fachbereich fachbereich = new Fachbereich(currFachbereich.getId(), tfBezeichnung.getText(), budget.floatValue());
		try{
			int institutsId = ((Institut)cbInstitut.getSelectedItem()).getId();
			applicationServer.setFachbereich(fachbereich, institutsId);	
			fachbereich.setId(institutsId);
			listModel.setElementAt(fachbereich, listFachbereiche.getSelectedIndex());
			return "";
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public void actionPerformed( ActionEvent e ) {
		if(!listFachbereiche.isSelectionEmpty()){
			String error = "";
			if ( e.getSource() == buAnlegen ) {
			} else if ( e.getSource() == buAendern ) {
				error = changeFachbereich();
			} else if ( e.getSource() == buLoeschen ) {
				JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich löschen ?", "Löschen",
													JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			} else if ( e.getSource() == buRefresh ) {
				loadInstituts();
				loadFachbereiche();
			} else if ( e.getSource() == buBeenden ) {
				this.dispose();
			}
			if(!error.equals("")){
				JOptionPane.showMessageDialog(
					 this,
					 error,
					 "Warnung",
					 JOptionPane.ERROR_MESSAGE);
				loadFachbereiche();
			}
		}
	}

	public static void main(String[] args) {
		JFrame test = new JFrame("Fachbereichverwaltung Test");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setContentPane(desk);
		test.setBounds(100,100,800,700);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
			ApplicationServer applicationServer = server.getMyApplicationServer();
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("r.driesner").toString());
			applicationServer.login("r.driesner", psw);
			Fachbereichverwaltung fachbereichverwaltung = new Fachbereichverwaltung(applicationServer);
			desk.add(fachbereichverwaltung);
			test.show();
			fachbereichverwaltung.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }

	public void valueChanged(ListSelectionEvent e) {
		if(!listFachbereiche.isSelectionEmpty()){
			Fachbereich fachbereich = (Fachbereich)listModel.getElementAt(listFachbereiche.getSelectedIndex());
	
			tfBezeichnung.setText(fachbereich.getHochschule());
			tfProfBudget.setText(NumberFormat.getCurrencyInstance().format(fachbereich.getProfPauschale()));
			Institut institut = new Institut(fachbereich.getId(), fachbereich.getBezeichnung(), fachbereich.getKostenstelle());
			cbInstitut.setSelectedItem(institut);
		}
	}
}
