package gui;

import applicationServer.*;

import java.rmi.Naming;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import dbObjects.FBHauptkonto;
import dbObjects.Kontenzuordnung;
import dbObjects.ZVKonto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KontenZuordnungverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener, TreeSelectionListener{

  ApplicationServer applicationServer;
  DefaultListModel liMoZuordnungen = new DefaultListModel();
  JList listZuordnungen = new JList(liMoZuordnungen);
  JScrollPane jScrollPane1 = new JScrollPane();
  JLabel jLabel3 = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  DefaultListModel liMoZVKonten = new DefaultListModel();
  JList listZVKonten = new JList(liMoZVKonten);
  JLabel jLabel4 = new JLabel();
  JButton buAdd = new JButton();
  JButton buDel = new JButton();
  JButton buSetZuordnung = new JButton();
  JScrollPane scrollTree = new JScrollPane();
  FBKontenTree treeKonten;
  JCheckBox cbGesperrt = new JCheckBox();
  JFrame frame;


  public KontenZuordnungverwaltung(ApplicationServer applicationServer, JFrame frame) {
		super( "Kontenzuordnungen" );
		this.frame = frame;
		this.applicationServer = applicationServer;
		this.setClosable(true);
		this.setIconifiable(true);

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		buAdd.setIcon(Functions.getUpIcon(getClass()));
		buAdd.addActionListener( this );
		buDel.setIcon(Functions.getDownIcon(getClass()));
    buDel.addActionListener( this );
		buSetZuordnung.addActionListener( this );
		buSetZuordnung.setIcon(Functions.getEditIcon(getClass()));

		listZuordnungen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listZuordnungen.addListSelectionListener(this);
		listZVKonten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		try {
			treeKonten.loadInstituts( applicationServer.getInstitutZuordnungen() );
		} catch (ApplicationServerException e1) {
		} 

		setZuordnungEnabled(false);
		this.setBounds(0,0,436, 606);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
  }


	private void loadZuordnungen(Kontenzuordnung[] zuordnung){
	  try{
			liMoZuordnungen.clear();
			if(zuordnung != null){
				for(int i = 0; i < zuordnung.length; i++){
					liMoZuordnungen.addElement(zuordnung[i]);
			  }
			  listZuordnungen.setSelectedIndex(0);
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }

  private void loadZVKonten(){
		try{
			ArrayList zvKonten = applicationServer.getZVKontenOnly();

			if(zvKonten != null){
				  liMoZVKonten.clear();
				for(int i = 0; i < zvKonten.size(); i++){
					if(!containsZVKonto((ZVKonto)zvKonten.get(i)))
						liMoZVKonten.addElement((ZVKonto)zvKonten.get(i));
				}
				listZVKonten.setSelectedIndex(0);
			  }
		}catch(Exception e){
			System.out.println(e);
		}
	}

	private void setZuordnung() throws ApplicationServerException{
		if(!listZuordnungen.isSelectionEmpty()){
			Kontenzuordnung zuordnung = (Kontenzuordnung)liMoZuordnungen.getElementAt(listZuordnungen.getSelectedIndex());
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );

			short status = (short)((cbGesperrt.isSelected()) ? 1 : 0);

			zuordnung.setStatus(status);

			applicationServer.setKontenZuordnung(fbKonto, zuordnung);
		}
	}


	private void addZuordnung() throws ApplicationServerException{
		if(treeKonten.fbHauptkontoIsSelected()){
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );
			ZVKonto zvKonto = (ZVKonto)liMoZVKonten.getElementAt(listZVKonten.getSelectedIndex());

			if(fbKonto != null && zvKonto != null){
				applicationServer.addKontenZuordnung(fbKonto,zvKonto);

				Kontenzuordnung zuordnung = new Kontenzuordnung((short)0, zvKonto);

				liMoZuordnungen.addElement(zuordnung);
				listZuordnungen.setSelectedIndex(liMoZuordnungen.getSize() - 1);
				listZuordnungen.ensureIndexIsVisible(liMoZuordnungen.getSize() - 1);
				int index = listZVKonten.getSelectedIndex();
				liMoZVKonten.remove(index);
				if(liMoZVKonten.getSize() > 0){
					if(index == 0)
						listZVKonten.setSelectedIndex(index);
					else if(index >= liMoZVKonten.getSize()-1)
						listZVKonten.setSelectedIndex(liMoZVKonten.getSize()-1);
					else
						listZVKonten.setSelectedIndex(index);
				}

				fbKonto.setZuordnung(getZuordnung());
				treeKonten.updateNode(fbKonto);
			}
		}
	}

	private void delZuordnung() throws ApplicationServerException{
		if(treeKonten.fbHauptkontoIsSelected()){
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );
			ZVKonto zvKonto = ((Kontenzuordnung)liMoZuordnungen.getElementAt(listZuordnungen.getSelectedIndex())).getZvKonto();

			if(fbKonto != null && zvKonto != null){
				applicationServer.delKontenZuordnung(fbKonto, zvKonto);

				liMoZVKonten.addElement(zvKonto);
				listZVKonten.setSelectedIndex(liMoZVKonten.getSize() - 1);
				listZVKonten.ensureIndexIsVisible(liMoZVKonten.getSize() - 1);
				int index = listZuordnungen.getSelectedIndex();
				liMoZuordnungen.remove(listZuordnungen.getSelectedIndex());
				if(liMoZuordnungen.getSize() > 0){
					if(index == 0)
						listZuordnungen.setSelectedIndex(index);
					else if(index >= liMoZuordnungen.getSize()-1)
						listZuordnungen.setSelectedIndex(liMoZuordnungen.getSize()-1);
					else
						listZuordnungen.setSelectedIndex(index);
				}

				fbKonto.setZuordnung(getZuordnung());
				treeKonten.updateNode(fbKonto);
			}
		}
	}

	private boolean containsZVKonto(ZVKonto zvKonto){
		for(int i = 0; i < liMoZuordnungen.getSize(); i++){
			ZVKonto listZVKonto = ((Kontenzuordnung)liMoZuordnungen.getElementAt(i)).getZvKonto();
			if(listZVKonto.getId() == zvKonto.getId())
				return true;
		}
		return false;
	}

	private void reload(){
		//loadRollen();
	}

	private Kontenzuordnung[] getZuordnung(){
		Kontenzuordnung[] zuordnung = new Kontenzuordnung[liMoZuordnungen.getSize()];

		for(int i = 0; i < liMoZuordnungen.getSize(); i++){
			zuordnung[i] = (Kontenzuordnung)liMoZuordnungen.getElementAt(i);
		}
		return zuordnung;
	}

  private void jbInit() throws Exception {
    scrollTree.setBounds(new Rectangle(9, 8, 411, 191));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().setLayout(null);
    jScrollPane1.setBounds(new Rectangle(8, 226, 293, 151));
    jLabel3.setText("ZVKonten");
    jLabel3.setBounds(new Rectangle(7, 396, 78, 15));
    jScrollPane2.setBounds(new Rectangle(8, 419, 293, 151));
    jLabel4.setText("Zurordnungen");
    jLabel4.setBounds(new Rectangle(6, 204, 106, 15));
    buAdd.setBounds(new Rectangle(107, 388, 20, 20));
    buAdd.setToolTipText("ZVKonto hinzufügen");
    buAdd.setText("");
    buDel.setText("");
    buDel.setToolTipText("ZVKonto entfernen");
    buDel.setBounds(new Rectangle(174, 388, 20, 20));
    buSetZuordnung.setBounds(new Rectangle(312, 277, 107, 25));
    buSetZuordnung.setText("Ändern");
    cbGesperrt.setText("gesperrt");
    cbGesperrt.setBounds(new Rectangle(309, 237, 83, 23));
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(jScrollPane1, null);
    this.getContentPane().add(cbGesperrt, null);
    this.getContentPane().add(buDel, null);
    this.getContentPane().add(buAdd, null);
    this.getContentPane().add(buSetZuordnung, null);
    this.getContentPane().add(scrollTree, null);
    this.getContentPane().add(jScrollPane2, null);
    this.getContentPane().add(jLabel3, null);
    jScrollPane2.getViewport().add(listZVKonten, null);
    jScrollPane1.getViewport().add(listZuordnungen, null);
    scrollTree.getViewport().add(treeKonten = new FBKontenTree( this, "FBKonten" ) , null);
  }

  public static void main(String[] args) {
	  JFrame test = new JFrame("Kontenzuordnungen Test");
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
				KontenZuordnungverwaltung kontenVerwaltung = new KontenZuordnungverwaltung(applicationServer, test);
			  desk.add(kontenVerwaltung);
			  test.show();
				kontenVerwaltung.show();
	  }catch(Exception e){
					  System.out.println(e);
	  }
	}

	public void actionPerformed(ActionEvent e){
		try {
		if( treeKonten.fbHauptkontoIsSelected() ){
			if ( e.getSource() == buAdd ) {
					if(!liMoZVKonten.isEmpty() && !listZVKonten.isSelectionEmpty()){
						addZuordnung();
					}else{
						JOptionPane.showMessageDialog(
							  this,
							  "Ein ZVKonto muss selektiert sein !",
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
					}
				}else if( e.getSource() == buDel ) {
					if(!liMoZuordnungen.isEmpty() && !listZuordnungen.isSelectionEmpty()){
						delZuordnung();
					}else{
						JOptionPane.showMessageDialog(
							  this,
							  "Eine Kontenzuordnung muss selektiert sein !",
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
					}
				}else if( e.getSource() == buSetZuordnung ) {
					setZuordnung();
				}

				//rolle.setAktivitaetenFull(getRollenAktivitaeten());

			}
		} catch (ApplicationServerException e1) {
			MessageDialogs.showDetailMessageDialog(this, "Fehler", e1.getMessage(), e1.getNestedMessage(), MessageDialogs.ERROR_ICON);
//			JOptionPane.showMessageDialog(
//							  this,
//							  e1.getMessage(),
//							  "Warnung",
//							  JOptionPane.ERROR_MESSAGE);
		} 
	}

	void setZuordnungEnabled(boolean b){
		listZuordnungen.setEnabled(b);
		listZVKonten.setEnabled(b);
		buAdd.setEnabled(b);
		buDel.setEnabled(b);
		buSetZuordnung.setEnabled(b);
		cbGesperrt.setEnabled(b);
	}

	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		if( treeKonten.fbHauptkontoIsSelected() ){
			setZuordnungEnabled(true);
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );
			loadZuordnungen(fbKonto.getZuordnung());
			loadZVKonten();
		}else
			setZuordnungEnabled(false);
	}


	public void valueChanged(ListSelectionEvent e) {
		if(!listZuordnungen.isSelectionEmpty()){

			Kontenzuordnung zuordnung = (Kontenzuordnung)liMoZuordnungen.getElementAt(listZuordnungen.getSelectedIndex());

			cbGesperrt.setSelected((zuordnung.getStatus() == 0) ? false : true);
		}
	}




}