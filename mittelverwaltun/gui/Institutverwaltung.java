package gui;

import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import dbObjects.Institut;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.rmi.Naming;

public class Institutverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener {
	
	ApplicationServer applicationServer;
	JPanel panel = new JPanel();
	JScrollPane scrollInstitute = new JScrollPane();
	DefaultListModel listModel = new DefaultListModel();
	JList listInstitute = new JList(listModel);

	JTextField tfBezeichnung = new JTextField();
	JTextField tfKostenstelle = new JTextField();

	JButton buAnlegen = new JButton( "Anlegen" );
	JButton buAendern = new JButton( "Ändern" );
	JButton buLoeschen = new JButton( "Löschen" );
	JButton buBeenden = new JButton( "Beenden" );
	JButton buRefresh = new JButton("");
	
	
	public Institutverwaltung(ApplicationServer applicationServer) {
	  super( "Haushaltsjahr abschließen/anlegen" );
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.getContentPane().setLayout( null );
	  this.applicationServer = applicationServer;
		
		
	  Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 430, 190 );
	  panel.setBorder(BorderFactory.createEtchedBorder());
	  panel.setLayout( null );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Institute" ), 10, 10, 100, 16 );
	  Setting.setPosAndLoc( panel, scrollInstitute, 10, 30, 150, 150 );
	  scrollInstitute.getViewport().add( listInstitute, null );
		
	  Setting.setPosAndLoc( panel, new JLabel( "Bezeichnung" ), 170, 30, 100, 16 );
	  Setting.setPosAndLoc( panel, tfBezeichnung, 280, 30, 140, 22 );
		
	  Setting.setPosAndLoc( panel, new JLabel( "Kostenstelle" ), 170, 70, 100, 16 );
	  Setting.setPosAndLoc( panel, tfKostenstelle, 280, 70, 140, 22 );
		
	  Setting.setPosAndLoc( panel, buRefresh, 100, 5, 20, 20 );
	  buRefresh.setBorder(null);
		buRefresh.setIcon(Functions.getRefreshIcon(getClass()));
		buRefresh.setToolTipText("Aktualisieren");
		buRefresh.addActionListener( this );
		Setting.setPosAndLoc( panel, buAnlegen, 170, 110, 110, 25 );
	  buAnlegen.setIcon(Functions.getAddIcon(getClass()));
    buAnlegen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAendern, 310, 110, 110, 25 );
	  buAendern.setIcon(Functions.getEditIcon(getClass()));
    buAendern.addActionListener( this );
	  Setting.setPosAndLoc( panel, buLoeschen, 170, 155, 110, 25 );
	  buLoeschen.setIcon(Functions.getDelIcon(getClass()));
    buLoeschen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buBeenden, 310, 155, 110, 25 );
	  buBeenden.setIcon(Functions.getCloseIcon(getClass()));
    buBeenden.addActionListener( this );
	  listInstitute.addListSelectionListener(this);
		listInstitute.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		

		loadInstitutes();
	  this.setSize( 450, 235 );
	
	}
	
	protected void loadInstitutes(){
	  try{
		  Institut[] institut = applicationServer.getInstitutes();
			listModel.clear();
		  for(int i = 0; i < institut.length; i++){
			  listModel.addElement(institut[i]);
		  }
		  if(institut.length > 0)
			  listInstitute.setSelectedIndex(0);
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }
  
  protected String changeInstitut(){
		if(!listInstitute.isSelectionEmpty()){
			Institut currInstitut = (Institut)listModel.getElementAt(listInstitute.getSelectedIndex());
			Institut institut = new Institut(currInstitut.getId(), tfBezeichnung.getText(), tfKostenstelle.getText());
			try{
				applicationServer.setInstitute(institut, currInstitut);	
				listModel.setElementAt(institut, listInstitute.getSelectedIndex());
				return "";
			}catch(Exception e){
				return e.getMessage();
			}
		}else
			return "Es ist kein Institut selektiert !";
	}
	
	protected String addInstitut(){
		if(tfBezeichnung.getText().equals("") || tfKostenstelle.getText().equals("")){
			return "Bezeichnung und Kostenstelle müssen ausgefüllt sein.";
		}else{
			Institut institut = new Institut(0, tfBezeichnung.getText(), tfKostenstelle.getText());
			try{
				int key = applicationServer.addInstitute(institut);
				institut.setId(key);	
				listModel.addElement(institut);
				listInstitute.setSelectedIndex(listModel.getSize() - 1);
				return "";
			}catch(Exception e){
				return e.getMessage();
			}
		}
	}
	
	protected String delInstitut(){
		Institut currInstitut = (Institut)listModel.getElementAt(listInstitute.getSelectedIndex());
		try{
			applicationServer.delInstitute(currInstitut);
			listModel.remove(listInstitute.getSelectedIndex());
			return "";
		}catch(Exception e){
			return e.getMessage();
		}
	}	
	  
	public void actionPerformed(ActionEvent e) {
		if(!listInstitute.isSelectionEmpty()){
			String error = "";
		  if ( e.getSource() == buAnlegen ) {
		  	error = addInstitut();
		  } else if ( e.getSource() == buAendern ) {
		  	error = changeInstitut();
		  } else if ( e.getSource() == buLoeschen ) {
				if(listInstitute.isSelectionEmpty()){
					error = "Es ist kein Institut selektiert !";
				}else{
					if(JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich löschen ?", "Löschen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
						error = delInstitut();
						loadInstitutes();
				}
			} else if ( e.getSource() == buRefresh ) {
				loadInstitutes();
			} else if ( e.getSource() == buBeenden ) {
			  this.dispose();
		  }
		  if(!error.equals("")){
				JOptionPane.showMessageDialog(
					 this,
					 error,
					 "Warnung",
					 JOptionPane.ERROR_MESSAGE);
				loadInstitutes();
		  }
		}
	}

	public static void main(String[] args) {
		JFrame test = new JFrame("Institutsverwaltung Test");
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
			Institutverwaltung institutverwaltung = new Institutverwaltung(applicationServer);
			desk.add(institutverwaltung);
			test.show();
			institutverwaltung.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }

	public void valueChanged(ListSelectionEvent e) {
		if(!listInstitute.isSelectionEmpty()){
			Institut institut = (Institut)listModel.getElementAt(listInstitute.getSelectedIndex());
			
			tfBezeichnung.setText(institut.getBezeichnung());
			tfKostenstelle.setText(institut.getKostenstelle());
		}
	}
}
