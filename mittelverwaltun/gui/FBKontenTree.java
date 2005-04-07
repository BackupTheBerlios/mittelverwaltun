package gui;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import dbObjects.*;


/**
 * Klasse zum Verwalten der Struktur der FBKonten. <br>
 * In dem Baum wird ein Array mit Instituten geladen und dieses Institute haben die FBHauptkonten. 
 * @author w.flat
 */
public class FBKontenTree extends JTree {

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
	 * @param rootName = Namee für den root-Knoten.
	 */ 
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
	
	
	/**
	 * Das Hauptkonto des ausgewählten Knotens ermitteln. <br>
	 * Wenn ein FBUnterkonto ausgewählt ist, dann wird das FBHauptkonto zurückgeliefert, <br>
	 * welchem dieses FBUnterkonto zugeordnet ist. <br>
	 * Ist der ausgewählte Knoten ein FBHauptkonto, dann wird das ausgewählte FBHauptkonto zurückgeliefert. <br>
	 * Sonst wird null zurückgeliefert. 
	 * @return FBHauptkonto des ausgewählten Knotens. 
	 */
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
	
	/**
	 * Das FBUnterkonto des ausgewählten Knotens ermitteln. <br>
	 * Ist der ausgewählte Knoten ein FBUnterkonto, dann wird das ausgewählte FBUnterkonto zurückgeliefert. <br>
	 * Sonst wird null zurückgeliefert. 
	 * @return FBUnterkonto des ausgewählten Knotens. 
	 */
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
	 * @return ArrayList mit Instituten.
	 */
	public ArrayList getInstituts() {
		ArrayList instituts = new ArrayList();
		for( int i = 0; i < root.getChildCount(); i++ ) {
			instituts.add( ((DefaultMutableTreeNode)root.getChildAt( i )).getUserObject() );
		}
		
		return instituts;
	}
	
	/**
	 * Den Institut des ausgewählten Knotens ermitteln. <br>
	 * Ist ein FBKonto ausgewählt, dann wird das Institut ausgegeben, dem dieses FBKonto zugeordnet ist. <br>
	 * Ist der ausgewählte Knoten ein Institut, dann wird das ausgewählte Institut zurückgeliefert. <br>
	 * Sonst wird null zurückgeliefert. 
	 * @return Institut des ausgewählten Knotens. 
	 */
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
	
	/**
	 * Abfrage ob der ausgewählte Knoten der root-Knoten ist.
	 * @return true = Wenn der ausgewählte Knoten ein root-Knoten ist, sonst = false.
	 */
	public boolean rootIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof String )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgewählte Knoten eine Instanz vom Instituts-Objekt ist. 
	 * @return true = Wenn der ausgewählte Knoten ein Institut ist, sonst = false.
	 */
	public boolean institutIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof Institut )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgewählte Knoten eine Instanz vom FBHauptkonto-Objekt ist. 
	 * @return true = Wenn der ausgewählte Knoten ein FBHauptkonto ist, sonst = false.
	 */
	public boolean fbHauptkontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBHauptkonto )
			return true;
		else
			return false;
	}
	
	/**
	 * Abfrage ob der ausgewählte Knoten eine Instanz vom FBUnterkonto-Objekt ist. 
	 * @return true = Wenn der ausgewählte Knoten ein FBUnterkonto ist, sonst = false.
	 */
	public boolean fbUnterkontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBHauptkonto )
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FBUnterkonto )
			return true;
		return false;
	}

	/**
	 * Den root-Knoten auswählen.
	 */
	public void selectRoot(){
		if( root != null )	// Wenn der root-Knoten existiert
			this.setSelectionPath( new TreePath( root ) );
	}
	
	/**
	 * Löschen des gesammten Baums.
	 */
	public void delTree() {
		this.setModel( treeModel = new DefaultTreeModel( root = new DefaultMutableTreeNode( root.getUserObject().toString() ) ) );
		selectRoot();
	}
	
	/**
	 * Den aktuellen Knoten aktualisieren. 
	 */
	public void refreshCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgewählt ist dann wird der null-Zeiger zurückgegeben
			return;

		treeModel.reload( (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent() );
	}
	
	/**
	 * Rückgabe des ausgewählten Knotens. 
	 * @return Der ausgewählte Knoten. 
	 */
	public DefaultMutableTreeNode getCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgewählt ist dann wird der null-Zeiger zurückgegeben
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
	 * Prozedur zum Laden der Institute.
	 * @param institute = Institute, die in den Baum geladen werden sollen.
	 */
	public void loadInstituts( Institut[] institute ) {
		if( institute == null )		// wenn keine Institute angegeben wurden
			return;

		DefaultMutableTreeNode node;

		// Schleife in der alle Institute in den Baum übernommen wurden
		for( int i = 0; i < institute.length; i++ ) {
			if( institute[i] == null )		// wenn ein Institut leer ist dann weitermachen beim nächstem
				continue;

			// einen neuen Institut in den Baum anhängen, in dem an den root-Knoten an letzter Stelle
			// einen neuen Knoten mit dem Institut einfügt 
			treeModel.insertNodeInto( node = new DefaultMutableTreeNode( institute[i] ), root, root.getChildCount() );
			// an den Institus-Knoten die Hauptkonten des Instituts anfügen
			addHauptkonten( institute[i].getHauptkonten(), node );
		}

		this.selectRoot();		// am Ende den root-Knoten selektieren
		this.expandPath( new TreePath( root ) );	// und den Baum öffnen
	}
	
	/**
	 * Die Hauptknoten an den angegebenen Knoten anhängen.
	 * @param hauptKonten = FBHauptkonten, die an den angegeben Knoten angehängt werden sollen.
	 * @param instNode = Der Knoten, an dem die FBKonten angehängt werden sollen. 
	 */
	public void addHauptkonten( ArrayList hauptKonten, DefaultMutableTreeNode instNode ) {
		if( hauptKonten == null || instNode == null )	// Sind keine Hauptknoten angegeben  
			return;
			
		DefaultMutableTreeNode newNode;

		// Schleife in der alle Hauptkonten an den angegebenen Knoten angehängt werden
		for( int i = 0; i < hauptKonten.size(); i++ ) {
			if( hauptKonten.get(i) == null )		// wenn das konto leer, dann weiter machen
				continue;

			// An den Instituts-Knoten einen Hauptkonten-Knoten einfügen 
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( (FBHauptkonto)hauptKonten.get(i) ),
																			instNode, instNode.getChildCount() );
			// an den Hauptkonten-Knoten die dazugehörigen Unterkonten einfügen
			addUnterkonten( ((FBHauptkonto)hauptKonten.get(i)).getUnterkonten(), newNode );
		}
	}
	
	/**
	 * Die Unterknoten an den angegebenen Knoten anhängen.
	 * @param hauptKonten = FBUnterknoten, die an den angegeben Knoten angehängt werden sollen.
	 * @param instNode = Der Knoten, an dem die FBKonten angehängt werden sollen. 
	 */
	public void addUnterkonten( ArrayList unterKonten, DefaultMutableTreeNode node ) {
		if( unterKonten == null )	// Sind keine Unterknoten angegeben  
			return;

		// Schleife in der alle Unterkonten an den angegebenen Knoten angehängt werden
		for( int i = 0; i < unterKonten.size(); i++ ) {
			if( unterKonten.get(i) == null )		// wenn das konto leer, dann weiter machen
				continue;

			// An den Hauptkonten-Knoten einen Unterkonten-Knoten einfügen 
			treeModel.insertNodeInto( new DefaultMutableTreeNode( (FBUnterkonto)unterKonten.get(i) ), node, node.getChildCount() );
		}
	}

	/**
	 * Das markierte Konto aktualisieren.
	 * @param konto = FBKonto, welches statt dem ausgewählten Konto gesetzt wird.
	 */
	public void updateNode( Object konto ){
		if( konto == null )		// wenn das konto leer ist
			return;
			
		DefaultMutableTreeNode node;
		if( (node = getCurrentNode()) == null )	// kein Knoten ausgewählt 
			return;
			
		node.setUserObject( konto );	// Das Konto aktualisieren
		selectThisNode( node );
	}
	
	/**
	 * An den aktuellen Knoten einen neuen Knoten anhängen.
	 * @param konto = FBKonto, das an den aktuellen Knoten angehängt wird.
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
	 * Den aktuellen Knoten löschen.
	 */
	public void delNode() {
		DefaultMutableTreeNode node = getCurrentNode();
		if( node == null )		// wenn kein aktueller Knoten
			return;
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if( parent == null )	// wenn kein vater-knoten
			return;
		
		treeModel.removeNodeFromParent( node );		// den aktuellen knoten löschen
		selectThisNode( parent );		// den vater-knoten selektieren
	}
	
	/**
	 * Überprüfung ob ein Node selektiert ist, wenn nicht dann wird der Eleternknoten selektiert..
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
