package gui;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import dbObjects.*;



public class FBKontenTree extends JTree {

	/**
	 * 
	 */
	DefaultMutableTreeNode root;

	/**
	 * 
	 */
	DefaultTreeModel treeModel;

	
	// Konstuktor erh�lt im ersten Parameter den TreeSelectionListener, im dem die Aktionen im Baum behandelt werden
	// und anschliessend den Namen f�r den root-Knoten
	public FBKontenTree( TreeSelectionListener tsl, String rootName ){
		super();
		this.setModel( treeModel = new DefaultTreeModel( root = new DefaultMutableTreeNode( rootName ) ) );
		// Das Selektieren von Knoten wird durch das TreeSelectionModel gesteuert
		TreeSelectionModel tsm = new DefaultTreeSelectionModel();
		tsm.setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );	// Nur ein Knoten kann selektiert werden
		this.setSelectionModel( tsm );
		this.setRootVisible( true );		// Der root-Knoten wird angezeigt
		this.selectRoot();
		this.addTreeSelectionListener( tsl );
	}
	
	
	// Das Hauptkonto des ausgew�hlten Knotens ermitteln
	public FBHauptkonto getFBHauptkonto() {
		if( getCurrentNode() == null )
			return null;
		if( fbHauptkontoIsSelected() )
			return (FBHauptkonto)getCurrentNode().getUserObject();
		else if( fbUnterkontoIsSelected() )
			return (FBHauptkonto)((DefaultMutableTreeNode)getCurrentNode().getParent()).getUserObject();
		else
			return null;
	}
	
	// Das Unterkonto des ausgew�hlten Knotens ermitteln
	public FBUnterkonto getFBUnterkonto() {
		if( getCurrentNode() == null )
			return null;
		if( fbUnterkontoIsSelected() )
			return (FBUnterkonto)getCurrentNode().getUserObject();
		else
			return null;
	}
	
	/**
	 * Alle Institute erstellen die im Baum enthalten sind.
	 * @return ArrayList mit Instituten
	 */
	public ArrayList getInstituts() {
		ArrayList instituts = new ArrayList();
		for( int i = 0; i < root.getChildCount(); i++ ) {
			instituts.add( ((DefaultMutableTreeNode)root.getChildAt( i )).getUserObject() );
		}
		
		return instituts;
	}
	
	// Den Institut des ausgew�hlten Knotens ermitteln
	public Institut getInstitut() {
		if( getCurrentNode() == null )
			return null;
		if( institutIsSelected() )
			return (Institut)getCurrentNode().getUserObject();
		else if( fbHauptkontoIsSelected() )
			return (Institut)((DefaultMutableTreeNode)getCurrentNode().getParent()).getUserObject();
		else if( fbUnterkontoIsSelected() )
			return (Institut)((DefaultMutableTreeNode)getCurrentNode().getParent().getParent()).getUserObject();
		else
			return null;
	}
	
	// Abfrage ob der ausgew�hlte Knoten der root-Knoten ist
	public boolean rootIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof String )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgew�hlte Knoten eine Instanz vom Institus-Objekt ist
	public boolean institutIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof Institut )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgew�hlte Knoten eine Instanz vom FBHauptkonto-Objekt ist
	public boolean fbHauptkontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBHauptkonto )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgew�hlte Knoten eine Instanz vom FBUnterkonto-Objekt ist
	public boolean fbUnterkontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBHauptkonto )
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBUnterkonto )
			return true;
		return false;
	}

	// Den root-Knoten ausw�hlen
	public void selectRoot(){
		if( root != null )	// Wenn der root-Knoten existiert
			this.setSelectionPath( new TreePath( root ) );
	}
	
	/**
	 * L�schen des gesammten Baums
	 */
	public void delTree() {
		this.setModel( treeModel = new DefaultTreeModel( root = new DefaultMutableTreeNode( root.getUserObject().toString() ) ) );
		selectRoot();
	}
	
	/**
	 * Den aktuellen Knoten aktualisieren
	 */
	public void refreshCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgew�hlt ist dann wird der null-Zeiger zur�ckgegeben
			return;

		treeModel.reload( (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent() );
	}
	
	// R�ckgabe des ausgew�hlten Knotens
	public DefaultMutableTreeNode getCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgew�hlt ist dann wird der null-Zeiger zur�ckgegeben
			return null;
		
		return (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent();
	}
	
	// Den angegeben Knoten markieren
	public void selectThisNode( DefaultMutableTreeNode node ) {
		if( node == null )		// Wenn der Knoten angegebene Knoten nicht existiert dann wird nichts gemacht
			return;

		TreeNode[] path;		

		if( ( path = treeModel.getPathToRoot( node ) ) == null )	// Dann wird der Weg zum root-Knoten ermittelt 
			return;			// wenn der ermittelte Weg nicht vorhanden dann wird nichts gemacht 
		else
			setSelectionPath( new TreePath( path ) );		// andernfalls wird angegebene Knoten markiert
	}
	
	// Prozedur zum Laden der Institute
	public void loadInstituts( Institut[] institute ) {
		if( institute == null )		// wenn keine Institute angegeben wurden
			return;

		DefaultMutableTreeNode node;

		// Schleife in der alle Institute in den Baum �bernommen wurden
		for( int i = 0; i < institute.length; i++ ) {
			if( institute[i] == null )		// wenn ein Institut leer ist dann weitermachen beim n�chstem
				continue;

			// einen neuen Institut in den Baum anh�ngen, in dem an den root-Knoten an letzter Stelle
			// einen neuen Knoten mit dem Institut einf�gt 
			treeModel.insertNodeInto( node = new DefaultMutableTreeNode( institute[i] ), root, root.getChildCount() );
			// an den Institus-Knoten die Hauptkonten des Instituts anf�gen
			addHauptkonten( institute[i].getHauptkonten(), node );
		}

		this.selectRoot();		// am Ende den root-Knoten selektieren
		this.expandPath( new TreePath( root ) );	// und den Baum �ffnen
	}
	
	// Die Hauptknoten an den angegebenen Knoten anh�ngen
	public void addHauptkonten( ArrayList hauptKonten, DefaultMutableTreeNode instNode ) {
		if( hauptKonten == null || instNode == null )	// Sind keine Hauptknoten angegeben  
			return;
			
		DefaultMutableTreeNode newNode;

		// Schleife in der alle Hauptkonten an den angegebenen Knoten angeh�ngt werden
		for( int i = 0; i < hauptKonten.size(); i++ ) {
			if( hauptKonten.get(i) == null )		// wenn das konto leer, dann weiter machen
				continue;

			// An den Instituts-Knoten einen Hauptkonten-Knoten einf�gen 
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( (FBHauptkonto)hauptKonten.get(i) ),
																			instNode, instNode.getChildCount() );
			// an den Hauptkonten-Knoten die dazugeh�rigen Unterkonten einf�gen
			addUnterkonten( ((FBHauptkonto)hauptKonten.get(i)).getUnterkonten(), newNode );
		}
	}
	
	// Die Unterknoten an den angegebenen Knoten anh�ngen
	public void addUnterkonten( ArrayList unterKonten, DefaultMutableTreeNode node ) {
		if( unterKonten == null )	// Sind keine Unterknoten angegeben  
			return;

		// Schleife in der alle Unterkonten an den angegebenen Knoten angeh�ngt werden
		for( int i = 0; i < unterKonten.size(); i++ ) {
			if( unterKonten.get(i) == null )		// wenn das konto leer, dann weiter machen
				continue;

			// An den Hauptkonten-Knoten einen Unterkonten-Knoten einf�gen 
			treeModel.insertNodeInto( new DefaultMutableTreeNode( (FBUnterkonto)unterKonten.get(i) ), node, node.getChildCount() );
		}
	}

	// Das markierte Konto aktualisieren 
	public void updateNode( Object konto ){
		if( konto == null )		// wenn das konto leer ist
			return;
			
		DefaultMutableTreeNode node;
		if( (node = getCurrentNode()) == null )	// kein Knoten ausgew�hlt 
			return;
			
		node.setUserObject( konto );	// Das Konto aktualisieren
		selectThisNode( node );
	}
	
	// An den aktuellen Knoten einen neuen Knoten einf�gen
	public void addNewNode( Object konto ) {
		if( konto == null )		// wenn kein konto angegeben
			return;
		
		DefaultMutableTreeNode presNode;		// aktueller Knoten
		if( (presNode = getCurrentNode()) == null )
			return;

		DefaultMutableTreeNode newNode;			// der neue Knoten
		treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( konto ), presNode, presNode.getChildCount() );
		if( newNode != null )
			selectThisNode( newNode );
	}
	
	// Den aktuellen Knoten l�schen
	public void delNode() {
		DefaultMutableTreeNode node = getCurrentNode();
		if( node == null )		// wenn kein aktueller Knoten
			return;
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if( parent == null )	// wenn kein vater-knoten
			return;
		
		treeModel.removeNodeFromParent( node );		// den aktuellen knoten l�schen
		selectThisNode( parent );		// den vater-knoten selektieren
	}
	
	// �berpr�fung ob ein Node selektiert ist, wenn nicht dann wird der Eleternknoten selektiert
	public void checkSelection( TreeSelectionEvent event ) {
		if( event == null )
			return;
		if( event.getNewLeadSelectionPath() == null ){		// Kein neuer Pfad
			// Den alten Pfad ermitteln
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)event.getOldLeadSelectionPath().getLastPathComponent();
			// Und den Vater-Knoten davon selektieren
			this.selectThisNode( (DefaultMutableTreeNode)node.getParent() );
		} 
	}
	
}
