package gui;

import applicationServer.*;

import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbObjects.Aktivitaet;
import dbObjects.Rolle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RollenAktivitaetenverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener{

  ApplicationServer applicationServer;
  JLabel jLabel2 = new JLabel();
  DefaultListModel liMoRollenAktivitaeten = new DefaultListModel();
  JList listRollenAktivitaeten = new JList(liMoRollenAktivitaeten);
  JScrollPane jScrollPane1 = new JScrollPane();
  JLabel jLabel3 = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  DefaultListModel liMoAktivitaeten = new DefaultListModel();
  JList listAktivitaeten = new JList(liMoAktivitaeten);
  JLabel jLabel4 = new JLabel();
  JButton buAdd = new JButton();
  JButton buDel = new JButton();
  JScrollPane jScrollPane3 = new JScrollPane();
  DefaultListModel liMoRollen = new DefaultListModel();
  JList listRollen = new JList(liMoRollen);
  JLabel jLabel1 = new JLabel();
  JTextField tfRollenName = new JTextField();
  JButton buDelRolle = new JButton();
  JButton buSaveRolle = new JButton();
  JButton buAddRolle = new JButton();


  public RollenAktivitaetenverwaltung(ApplicationServer applicationServer) {
		super( "Benutzerverwaltung" );
		this.applicationServer = applicationServer;
		this.setClosable(true);
		this.setIconifiable(true);

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		buAdd.setIcon(Functions.getBackIcon(getClass()));
		buAdd.addActionListener( this );
		buAddRolle.addActionListener( this );
		buAddRolle.setIcon(Functions.getAddIcon(getClass()));
    buDel.setIcon(Functions.getForwardIcon(getClass()));
    buDel.addActionListener( this );
		buDelRolle.setIcon(Functions.getDelIcon(getClass()));
    buDelRolle.addActionListener( this );
		buSaveRolle.addActionListener( this );
		buSaveRolle.setIcon(Functions.getEditIcon(getClass()));

		listRollen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listRollen.addListSelectionListener(this);
		listRollenAktivitaeten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAktivitaeten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loadRollen();

		this.setBounds(0,0,403, 440);
  }

  private void loadRollen(){
		try{
			Rolle[] rollen = applicationServer.getRollenFull();

			liMoRollen.clear();
			for(int i = 0; i < rollen.length; i++){
				liMoRollen.addElement(rollen[i]);
		  }
		  listRollen.setSelectedIndex(0);
		}catch(Exception e){
			System.out.println(e);
		}
	}

	private void loadRollenAktivitaeten(Aktivitaet[] aktivitaeten){
	  try{
			liMoRollenAktivitaeten.clear();
			if(aktivitaeten != null){
				for(int i = 0; i < aktivitaeten.length; i++){
					liMoRollenAktivitaeten.addElement(aktivitaeten[i]);
			  }
			  listRollenAktivitaeten.setSelectedIndex(0);
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }

  private void loadAktivitaeten(){
		try{
			Aktivitaet[] aktivitaeten = applicationServer.getAktivitaeten();

			if(aktivitaeten != null){
				  liMoAktivitaeten.clear();
				for(int i = 0; i < aktivitaeten.length; i++){
					if(!liMoRollenAktivitaeten.contains(aktivitaeten[i]))
						liMoAktivitaeten.addElement(aktivitaeten[i]);
				}
				listAktivitaeten.setSelectedIndex(0);
			  }
		}catch(Exception e){
			System.out.println(e);
		}
	}

	private void addRolle()  throws ApplicationServerException, RemoteException{
		if(tfRollenName.getText().equals("")){
			throw new ApplicationServerException(1, "Rollenname muss ausgefüllt sein.");
		}else{
			Rolle rolle = new Rolle(0, tfRollenName.getText());
			int key = applicationServer.addRolle(rolle);
			rolle.setId(key);
			liMoRollen.addElement(rolle);
			listRollen.setSelectedIndex(liMoRollen.getSize() - 1);
			listRollen.ensureIndexIsVisible(liMoRollen.getSize() - 1);
		}
	}

	private void setRolle() throws ApplicationServerException, RemoteException{
		if(!listRollen.isSelectionEmpty()){
			
			Rolle currRolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());
			Rolle rolle = new Rolle(currRolle.getId(), tfRollenName.getText());

			applicationServer.setRolle(rolle, currRolle);
			liMoRollen.setElementAt(rolle, listRollen.getSelectedIndex());
		
		}else
			throw new ApplicationServerException(0, "Es ist keine Rolle selektiert !");
	}

	private void delRolle() throws ApplicationServerException, RemoteException{
		Rolle currRolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());
		applicationServer.delRolle(currRolle);
		
		int index = listRollen.getSelectedIndex();
		liMoRollen.remove(listRollen.getSelectedIndex());
		
		if(index <= liMoRollen.getSize()){
			if(index == 0)
				listRollen.setSelectedIndex(index);
			else
				listRollen.setSelectedIndex(index-1);
		}
	}

	private void addRollenAktivitaet() throws ApplicationServerException, RemoteException{
		Rolle rolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());
		Aktivitaet aktivitaet = (Aktivitaet)liMoAktivitaeten.getElementAt(listAktivitaeten.getSelectedIndex());

		if(rolle != null && aktivitaet != null){
			applicationServer.addRollenAktivitaet(rolle.getId(), aktivitaet.getId());
			liMoRollenAktivitaeten.addElement(aktivitaet);
			listRollenAktivitaeten.setSelectedIndex(liMoRollenAktivitaeten.getSize() - 1);
			listRollenAktivitaeten.ensureIndexIsVisible(liMoRollenAktivitaeten.getSize() - 1);
			int index = listAktivitaeten.getSelectedIndex();
			liMoAktivitaeten.remove(index);
			if(liMoAktivitaeten.getSize() > 0){
				if(index == 0)
					listAktivitaeten.setSelectedIndex(index);
				else if(index >= liMoAktivitaeten.getSize()-1)
					listAktivitaeten.setSelectedIndex(liMoAktivitaeten.getSize()-1);
				else
					listAktivitaeten.setSelectedIndex(index);
			}
		}
	}

	private void delRollenAktivitaet() throws ApplicationServerException, RemoteException{
		Rolle rolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());
		Aktivitaet aktivitaet = (Aktivitaet)liMoRollenAktivitaeten.getElementAt(listRollenAktivitaeten.getSelectedIndex());

		if(rolle != null && aktivitaet != null){
			applicationServer.delRollenAktivitaet(rolle.getId(), aktivitaet.getId());
			liMoAktivitaeten.addElement(aktivitaet);
			listAktivitaeten.setSelectedIndex(liMoAktivitaeten.getSize() - 1);
			listAktivitaeten.ensureIndexIsVisible(liMoAktivitaeten.getSize() - 1);
			int index = listRollenAktivitaeten.getSelectedIndex();
			liMoRollenAktivitaeten.remove(listRollenAktivitaeten.getSelectedIndex());
			if(liMoRollenAktivitaeten.getSize() > 0){
				if(index == 0)
					listRollenAktivitaeten.setSelectedIndex(index);
				else if(index >= liMoRollenAktivitaeten.getSize()-1)
					listRollenAktivitaeten.setSelectedIndex(liMoRollenAktivitaeten.getSize()-1);
				else
					listRollenAktivitaeten.setSelectedIndex(index);
			}
		}
	}

	private void reload(){
		loadRollen();
	}

	private Aktivitaet[] getRollenAktivitaeten(){
		Aktivitaet[] aktivitaeten = new Aktivitaet[liMoRollenAktivitaeten.getSize()];

		for(int i = 0; i < liMoRollenAktivitaeten.getSize(); i++){
			aktivitaeten[i] = (Aktivitaet)liMoRollenAktivitaeten.getElementAt(i);
		}
		return aktivitaeten;
	}

  private void jbInit() throws Exception {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().setLayout(null);
    jLabel2.setText("Rollen");
    jLabel2.setBounds(new Rectangle(5, 5, 50, 15));
    jScrollPane1.setBounds(new Rectangle(4, 162, 166, 247));
    jLabel3.setText("Aktivitäten");
    jLabel3.setBounds(new Rectangle(220, 138, 78, 15));
    jScrollPane2.setBounds(new Rectangle(220, 161, 166, 247));
    jLabel4.setText("Rollenaktivitäten");
    jLabel4.setBounds(new Rectangle(4, 141, 106, 15));
    buAdd.setBounds(new Rectangle(185, 233, 20, 20));
    buAdd.setToolTipText("Aktivität hinzufügen");
    buAdd.setText("");
    buDel.setText("");
    buDel.setToolTipText("Aktivität entfernen");
    buDel.setBounds(new Rectangle(184, 284, 20, 20));
    this.setTitle("Rollenverwaltung");
    jScrollPane3.setBounds(new Rectangle(4, 26, 138, 106));
    jLabel1.setText("Name:");
    jLabel1.setBounds(new Rectangle(148, 24, 50, 15));
    tfRollenName.setText("");
    tfRollenName.setBounds(new Rectangle(146, 46, 101, 21));
    buDelRolle.setBounds(new Rectangle(272, 80, 111, 25));
    buDelRolle.setText("Löschen");
    buSaveRolle.setBounds(new Rectangle(146, 80, 122, 25));
    buSaveRolle.setText("Speichern");
    buAddRolle.setBounds(new Rectangle(272, 43, 111, 25));
    buAddRolle.setText("Einfügen");
    this.getContentPane().add(jScrollPane3, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(jScrollPane3, null);
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(tfRollenName, null);
    this.getContentPane().add(buAddRolle, null);
    this.getContentPane().add(buDelRolle, null);
    this.getContentPane().add(buSaveRolle, null);
    this.getContentPane().add(jScrollPane1, null);
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(jLabel3, null);
    this.getContentPane().add(buAdd, null);
    this.getContentPane().add(buDel, null);
    this.getContentPane().add(jScrollPane2, null);
    jScrollPane2.getViewport().add(listAktivitaeten, null);
    jScrollPane1.getViewport().add(listRollenAktivitaeten, null);
    jScrollPane3.getViewport().add(listRollen, null);
  }

  public static void main(String[] args) {
	  JFrame test = new JFrame("Rollenverwaltung Test");
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
				RollenAktivitaetenverwaltung rollenVerwaltung = new RollenAktivitaetenverwaltung(applicationServer);
			  desk.add(rollenVerwaltung);
			  test.show();
				rollenVerwaltung.show();
	  }catch(Exception e){
					  System.out.println(e);
	  }
	}

	public void actionPerformed(ActionEvent e){
		try {
			Rolle rolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());

			if(rolle != null){
				if ( e.getSource() == buAdd ) {
					if(!liMoAktivitaeten.isEmpty() && !listAktivitaeten.isSelectionEmpty()){
						addRollenAktivitaet();
					}else{
						JOptionPane.showMessageDialog(
							  this,
							  "Eine Aktivität muss selektiert sein !",
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
					}
				}else if( e.getSource() == buDel ) {
					if(!liMoRollenAktivitaeten.isEmpty() && !listRollenAktivitaeten.isSelectionEmpty()){
						delRollenAktivitaet();
					}else{
						JOptionPane.showMessageDialog(
							  this,
							  "Eine Rollenaktivität muss selektiert sein !",
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
					}
				}else if( e.getSource() == buAddRolle ) {
					addRolle();
				}else if( e.getSource() == buSaveRolle ) {
					setRolle();
				}else if( e.getSource() == buDelRolle ) {
						if(!listRollen.isSelectionEmpty()){
							if(JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich die Rolle " + rolle.getBezeichnung() + " löschen ?", "Löschen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
								delRolle();
						}else{
							JOptionPane.showMessageDialog(
								  this,
								  "Eine Rolle muss selektiert sein !",
								  "Warnung",
								  JOptionPane.ERROR_MESSAGE);
						}
				}
				rolle.setAktivitaetenFull(getRollenAktivitaeten());
			}
		} catch (ApplicationServerException e1) {
			JOptionPane.showMessageDialog(
							  this,
							  e1.getMessage(),
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
		} catch (RemoteException exc){
		JOptionPane.showMessageDialog( this, exc.getMessage(),	"RemoteException !", JOptionPane.ERROR_MESSAGE );
	}
	}

	public void valueChanged(ListSelectionEvent e) {
		if(!listRollen.isSelectionEmpty()){
			Rolle rolle = (Rolle)liMoRollen.getElementAt(listRollen.getSelectedIndex());
			tfRollenName.setText(rolle.getBezeichnung());
			loadRollenAktivitaeten(rolle.getAktivitaetenFull());
			loadAktivitaeten();
		}
	}




}