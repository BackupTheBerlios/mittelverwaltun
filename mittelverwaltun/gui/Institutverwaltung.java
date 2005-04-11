package gui;

import applicationServer.*;
import dbObjects.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.rmi.*;
import java.awt.*;

public class Institutverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener {
	
	ApplicationServer applicationServer;
	DefaultListModel listModel = new DefaultListModel();
	JPanel panelWindow = new JPanel();
	JScrollPane scrollInstitute = new JScrollPane();
	JList listInstitute = new JList(listModel);
	JLabel labInstitute = new JLabel();
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	JLabel labKostenstelle = new JLabel();
	JTextField tfKostenstelle = new JTextField();
	JLabel labInstitutsleiter = new JLabel();
	JComboBox cbInstitutsleiter = new JComboBox();
	JButton buAnlegen = new JButton();
	JButton buAendern = new JButton();
	JButton buLoeschen = new JButton();
	JButton buBeenden = new JButton();
	JButton buRefresh = new JButton();
	
	
	public Institutverwaltung(MainFrame frame) {
		super( "Institutsverwaltung" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.applicationServer = frame.getApplicationServer();
		
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		buRefresh.setBorder(null);
		buRefresh.setIcon(Functions.getRefreshIcon(getClass()));
		buRefresh.setToolTipText("Aktualisieren");
		buRefresh.addActionListener( this );
		buAnlegen.setIcon(Functions.getAddIcon(getClass()));
		buAnlegen.addActionListener( this );
		buAendern.setIcon(Functions.getEditIcon(getClass()));
    	buAendern.addActionListener( this );
		buLoeschen.setIcon(Functions.getDelIcon(getClass()));
		buLoeschen.addActionListener( this );
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );
		listInstitute.addListSelectionListener(this);
		listInstitute.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		loadInstitutes();
		loadInstitutsleiter();
		this.setSize( 442, 225 );
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}
	
	private void jbInit() throws Exception {
		buAnlegen.setBounds(new Rectangle(172, 122, 110, 25));
		buAnlegen.setText("Anlegen");
		buAendern.setBounds(new Rectangle(312, 122, 110, 25));
		buAendern.setText("Ändern");
		buLoeschen.setBounds(new Rectangle(172, 157, 110, 25));
		buLoeschen.setText("Löschen");
		buBeenden.setBounds(new Rectangle(312, 157, 110, 25));
		buBeenden.setText("Beenden");
		buRefresh.setBounds(new Rectangle(102, 7, 20, 20));
		buRefresh.setText("");
		panelWindow.setOpaque(true);
		scrollInstitute.setBounds(new Rectangle(12, 32, 150, 150));
		cbInstitutsleiter.setBounds(new Rectangle(282, 92, 140, 21));
		labInstitutsleiter.setBounds(new Rectangle(172, 92, 100, 15));
		tfKostenstelle.setBounds(new Rectangle(282, 62, 140, 21));
		labKostenstelle.setBounds(new Rectangle(172, 62, 100, 15));
		tfBezeichnung.setBounds(new Rectangle(282, 32, 140, 21));
		labBezeichnung.setBounds(new Rectangle(172, 32, 100, 15));
		labInstitute.setBounds(new Rectangle(12, 12, 100, 15));
		panelWindow.add(scrollInstitute, null);
		panelWindow.add(labInstitute, null);
		panelWindow.add(labBezeichnung, null);
		panelWindow.add(tfBezeichnung, null);
		panelWindow.add(labKostenstelle, null);
		panelWindow.add(tfKostenstelle, null);
		panelWindow.add(labInstitutsleiter, null);
		panelWindow.add(cbInstitutsleiter, null);
		panelWindow.add(buAnlegen, null);
		panelWindow.add(buLoeschen, null);
		panelWindow.add(buRefresh, null);
		panelWindow.add(buAendern, null);
		scrollInstitute.getViewport().add(listInstitute, null);
		panelWindow.setBorder(BorderFactory.createEtchedBorder());
		panelWindow.setLayout(null);
		labInstitute.setText("Institute");
		labBezeichnung.setText("Bezeichnung");
		tfBezeichnung.setText("");
		labKostenstelle.setText("Kostenstelle");
		tfKostenstelle.setText("");
		labInstitutsleiter.setText("Institutsleiter");
		this.getContentPane().add(panelWindow, BorderLayout.CENTER);
		panelWindow.add(buBeenden, null);
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
  
  private void loadInstitutsleiter(){
		 try{
			 Benutzer[] users = applicationServer.getUsers();

			  if(users != null){
				  cbInstitutsleiter.removeAllItems();
					 for(int i = 0; i < users.length; i++){
						 cbInstitutsleiter.addItem(users[i]);
					 }
			  }
		 }catch(Exception e){
			 System.out.println(e);
		 }

	 }
  
  protected String changeInstitut(){
		if(!listInstitute.isSelectionEmpty()){
			Institut currInstitut = (Institut)listModel.getElementAt(listInstitute.getSelectedIndex());
			Benutzer institutsleiter = (Benutzer)cbInstitutsleiter.getSelectedItem();
			
			Institut institut = new Institut(currInstitut.getId(), tfBezeichnung.getText(), tfKostenstelle.getText(),institutsleiter);
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
			Benutzer institutsleiter = (Benutzer)cbInstitutsleiter.getSelectedItem();
			Institut institut = new Institut(0, tfBezeichnung.getText(), tfKostenstelle.getText(), institutsleiter);
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
		}catch(ApplicationServerException e){
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
		MainFrame test = new MainFrame("Institutsverwaltung Test");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setContentPane(desk);
		test.setBounds(100,100,800,700);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			applicationServer.login("r.driesner", psw);
//			Institutverwaltung institutverwaltung = new Institutverwaltung(applicationServer);
//			desk.add(institutverwaltung);
//			test.show();
//			institutverwaltung.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }

	public void valueChanged(ListSelectionEvent e) {
		if(!listInstitute.isSelectionEmpty()){
			Institut institut = (Institut)listModel.getElementAt(listInstitute.getSelectedIndex());
			
			tfBezeichnung.setText(institut.getBezeichnung());
			tfKostenstelle.setText(institut.getKostenstelle());
			
			for(int i = 0; i < cbInstitutsleiter.getItemCount(); i++){
				Benutzer benutzer = (Benutzer)cbInstitutsleiter.getItemAt(i);
				
				if(institut.getInstitutsleiter().getId() == benutzer.getId()){
					cbInstitutsleiter.setSelectedIndex(i);
					break;
				}
			}
		}
	}
}
