package gui;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import dbObjects.*;


/**
 * Klasse zum Verwalten der Struktur der ZVKonten. <br>
 * In dem Baum wird ein Array mit ZVKonten geladen. 
 * @author w.flat
 */
public class ZVKontenTree extends JTree {

	/**
	 * Der Root-Knoten des Baums. 
	 */
	DefaultMutableTreeNode root;

	/**
	 * Das TreeModel des Baums. 
	 */
	DefaultTreeModel treeModel;

	
	/**
	 * Konstruktor zum Erzeugen des FBKontenTrees.
	 * @param tsl = TreeSelectionListener, im dem die Aktionen im Baum behandelt werden. 
	 * @param rootName = Namee f�r den root-Knoten.
	 */ 
	public ZVKontenTree( TreeSelectionListener tsl, String rootName ){
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
	
	/**
	 * Abfrage des aktuellen ZVKontos unabh�ngig welchen Knoten man ausgew�hlt hat. <br>
	 * Wenn ein ZVTitel/ZVUntertitel ausgew�hlt ist, dann wird das ZVKonto zur�ckgeliefert, <br>
	 * welchem dieser ZVTitel/ZVUntertitel zugeordnet ist. <br>
	 * Ist der ausgew�hlte Knoten ein ZVKonto, dann wird das ausgew�hlte ZVKonto zur�ckgeliefert. <br>
	 * Sonst wird null zur�ckgeliefert. 
	 * @return ZVKonto des ausgew�hlten Knotens. 
	 */
	public ZVKonto getZVKonto() {
		if( getCurrentNode() == null )	// Kein Knoten ausgew�hlt
			return null;
		
		if( zvKontoIsSelected() )	// Der markierte Knoten ist es  
			return (ZVKonto)getCurrentNode().getUserObject();
		else if( zvTitelIsSelected() )		// Der Vaterknoten vom markiertem Knoten ist es
			return (ZVKonto)((DefaultMutableTreeNode)getCurrentNode().getParent()).getUserObject();
		else if( zvUntertitelIsSelected() )		// Der Vaterknoten vom Vaterknoten vom markierten Knoten ist es 
			return (ZVKonto)((DefaultMutableTreeNode)getCurrentNode().getParent().getParent()).getUserObject();
		else				// Sonst kann man kein ZVKonto ermitteln
			return null;
	}
	
	/**
	 * Abfrage des aktuellen ZVTitels unabh�ngig welchen Knoten man ausgew�hlt hat. <br>
	 * Wenn ein ZVUntertitel ausgew�hlt ist, dann wird der ZVTitel zur�ckgeliefert, <br>
	 * welchem dieser ZVUntertitel zugeordnet ist. <br>
	 * Ist der ausgew�hlte Knoten ein ZVTitel, dann wird das ausgew�hlte ZVTitel zur�ckgeliefert. <br>
	 * Sonst wird null zur�ckgeliefert. 
	 * @return ZVTitel des ausgew�hlten Knotens. 
	 */
	public ZVTitel getZVTitel() {
		if( getCurrentNode() == null )	// Kein Knoten ausgew�hlt
			return null;
		
		if( zvTitelIsSelected() )		// Der markierte Knoten ist es  
			return (ZVTitel)getCurrentNode().getUserObject();
		else if( zvUntertitelIsSelected() )		// Der Vaterknoten vom markiertem Knoten ist es
			return (ZVTitel)((DefaultMutableTreeNode)getCurrentNode().getParent()).getUserObject();
		else				// Sonst kann man kein ZVTitel ermitteln
			return null;
	}
	
	/**
	 * Abfrage des aktuellen ZVUntertitels unabh�ngig welchen Knoten man ausgew�hlt hat. <br>
	 * Ist der ausgew�hlte Knoten ein ZVUntertitel, dann wird das ausgew�hlte ZVUntertitel zur�ckgeliefert. <br>
	 * Sonst wird null zur�ckgeliefert. 
	 * @return ZVTitel des ausgew�hlten Knotens. 
	 */
	public ZVUntertitel getZVUntertitel() {
		if( getCurrentNode() == null )	// Kein Knoten ausgew�hlt
			return null;
		
		if( zvUntertitelIsSelected() )		// Der markierte Knoten ist es 
			return (ZVUntertitel)getCurrentNode().getUserObject();
		else				// Sonst kann man kein ZVUntertitel ermitteln
			return null;
	}
	
	/**
	 * Abfrage ob der ausgew�hlte Knoten der root-Knoten ist.
	 * @return true = Wenn der ausgew�hlte Knoten ein root-Knoten ist, sonst = false.
	 */
	public boolean rootIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof String )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgew�hlte Knoten ein ZVKonto ist.
	 * @return true = Wenn der ausgew�hlte Knoten ein ZVKonto ist, sonst = false.
	 */
	public boolean zvKontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVKonto )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgew�hlte Knoten ein ZVTitel ist.
	 * @return true = Wenn der ausgew�hlte Knoten ein ZVTitel ist, sonst = false.
	 */
	public boolean zvTitelIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVTitel )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgew�hlte Knoten ein ZVUntertitel ist.
	 * @return true = Wenn der ausgew�hlte Knoten ein ZVUntertitel ist, sonst = false.
	 */
	public boolean zvUntertitelIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgew�hlt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVTitel )
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVUntertitel )
			return true;
		return false;
	}
	
	/**
	 * Den root-Knoten ausw�hlen.
	 */
	public void selectRoot(){
		if( root != null )	// Wenn der root-Knoten existiert
			this.setSelectionPath( new TreePath( root ) );
	}
	
	/**
	 * L�schen des gesammten Baums ausser dem root-Knoten.
	 */
	public void delTree() {
		this.setModel( treeModel = new DefaultTreeModel( root = new DefaultMutableTreeNode( root.getUserObject().toString() ) ) );
		selectRoot();
	}
	
	/**
	 * Den aktuellen Knoten aktualisieren. 
	 */
	public void refreshCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgew�hlt ist dann wird der null-Zeiger zur�ckgegeben
			return;
		
		treeModel.nodeChanged( (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent() );
	}
	
	/**
	 * R�ckgabe des ausgew�hlten Knotens. 
	 * @return Der ausgew�hlte Knoten. 
	 */
	public DefaultMutableTreeNode getCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgew�hlt ist dann wird der null-Zeiger zur�ckgegeben
			return null;
		
		return (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent();
	}
	
	/**
	 * Den angegeben Knoten markieren.
	 * @param node = Knoten der markiert werden soll.
	 */
	public void selectThisNode( DefaultMutableTreeNode node ) {
		if( node == null )		// Wenn der Knoten angegebene Knoten nicht existiert dann wird nichts gemacht
			return;

		TreeNode[] path;		

		if( ( path = treeModel.getPathToRoot( node ) ) == null )	// Dann wird der Weg zum root-Knoten ermittelt 
			return;			// wenn der ermittelte Weg nicht vorhanden dann wird nichts gemacht 
		else
			setSelectionPath( new TreePath( path ) );		// andernfalls wird angegebene Knoten markiert
	}
	
	/**
	 * ZVKonten in den Baum laden.
	 * @param zvKonten = Liste mit Konten, die in den Baum geladen werden sollen. 
	 */
	public void loadZVKonten( ArrayList zvKonten ) {
		if( zvKonten == null )		// Keine konten angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neu erzeugte Knoten
		this.selectRoot();

		// Schleife um alle ZVKonten in den Baum zu �bernehmen
		for( int i = 0; i < zvKonten.size(); i++ ) {
			if( zvKonten.get(i) == null )		// Kein ZVKonto
				continue;
			
			// Das ZVKonten an den root-Knoten anh�ngen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( zvKonten.get(i) ), root, root.getChildCount() );
			// Alle zugeh�rigen ZVTitel an das ZVKonto dranh�ngen
			addZVTitel( ((ZVKonto)zvKonten.get(i)).getSubTitel(), newNode );
		}

		this.selectRoot();		// am Ende den root-Knoten selektieren
		this.expandPath( new TreePath( root ) );	// und den Baum �ffnen
	}
	
	/**
	 * ZVTitel an einen Knoten anh�ngen.
	 * @param titel = ZVTitel, die an den angegeben Knoten angeh�ngt werden sollen.
	 * @param node = Der Knoten, an dem die ZVTitel angeh�ngt werden sollen. 
	 */
	public void addZVTitel( ArrayList titel, DefaultMutableTreeNode node ) {
		if( titel == null )		// Keine Titel angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neuerzeugte Knoten	
		
		// Alle ZVTitel an den Knoten mit dem ZVKonto anh�ngen
		for( int i = 0; i < titel.size(); i++ ) {
			if( titel.get(i) == null )		// Kein Titel
				continue;
			// Neuen Knoten erzeugen und an letzter Position anh�ngen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( titel.get(i) ), node, node.getChildCount() );
			addZVUntertitel( ((ZVTitel)titel.get(i)).getSubUntertitel(), newNode );
		}
	}
	
	/**
	 * ZVUntertitel an einen Knoten anh�ngen.
	 * @param untertitel = ZVUntertitel, die an den angegeben Knoten angeh�ngt werden sollen.
	 * @param node = Der Knoten, an dem die ZVUntertitel angeh�ngt werden sollen. 
	 */
	public void addZVUntertitel( ArrayList untertitel, DefaultMutableTreeNode node ) {
		if( untertitel == null )		// Keine Untertitel angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neuerzeugte Knoten	
		
		// Alle ZVUntertitel an den Knoten mit dem ZVTitel anh�ngen
		for( int i = 0; i < untertitel.size(); i++ ) {
			if( untertitel.get(i) == null )		// Kein Untertitel
				continue;
			// Neuen Knoten erzeugen und an letzter Position anh�ngen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( untertitel.get(i) ), node, node.getChildCount() );
		}
	}


	/**
	 * Das markierte Konto aktualisieren.
	 * @param konto = ZVKonto/ZVTitel/ZVUntertitel, welches statt dem ausgew�hlten Konto gesetzt wird.
	 */
	public void updateNode( Object konto ){
		if( konto == null )		// wenn das konto leer ist
			return;
			
		DefaultMutableTreeNode node;
		if( (node = getCurrentNode()) == null )	// kein Knoten ausgew�hlt 
			return;
			
		node.setUserObject( konto );	// Das Konto aktualisieren
		selectThisNode( node );
	}

	
	/**
	 * An den aktuellen Knoten einen neuen Knoten anh�ngen.
	 * @param konto = ZVKonto/ZVTitel/ZVUntertitel, das an den aktuellen Knoten angeh�ngt wird.
	 */
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

	/**
	 * Den aktuellen Knoten l�schen.
	 */
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
	
	/**
	 * �berpr�fung ob ein Node selektiert ist, wenn nicht dann wird der Eleternknoten selektiert..
	 */
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
