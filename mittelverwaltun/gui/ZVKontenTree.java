package gui;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import dbObjects.*;



public class ZVKontenTree extends JTree {

	/**
	 * 
	 */
	DefaultMutableTreeNode root;

	/**
	 * 
	 */
	DefaultTreeModel treeModel;

	
	// Konstuktor erhält im ersten Parameter den TreeSelectionListener, im dem die Aktionen im Baum behandelt werden
	// und anschliessend den Namen für den root-Knoten
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
	
	// Abfrage des aktuellen ZVKontos unabhängig welchen Knoten man ausgewählt hat
	public ZVKonto getZVKonto() {
		if( getCurrentNode() == null )	// Kein Knoten ausgewählt
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
	
	// Abfrage des aktuellen ZVTitels unabhängig welchen Knoten man ausgewählt hat
	public ZVTitel getZVTitel() {
		if( getCurrentNode() == null )	// Kein Knoten ausgewählt
			return null;
		
		if( zvTitelIsSelected() )		// Der markierte Knoten ist es  
			return (ZVTitel)getCurrentNode().getUserObject();
		else if( zvUntertitelIsSelected() )		// Der Vaterknoten vom markiertem Knoten ist es
			return (ZVTitel)((DefaultMutableTreeNode)getCurrentNode().getParent()).getUserObject();
		else				// Sonst kann man kein ZVTitel ermitteln
			return null;
	}
	
	// Abfrage des aktuellen ZVTitels unabhängig welchen Knoten man ausgewählt hat
	public ZVUntertitel getZVUntertitel() {
		if( getCurrentNode() == null )	// Kein Knoten ausgewählt
			return null;
		
		if( zvUntertitelIsSelected() )		// Der markierte Knoten ist es 
			return (ZVUntertitel)getCurrentNode().getUserObject();
		else				// Sonst kann man kein ZVUntertitel ermitteln
			return null;
	}
	
	// Abfrage ob der ausgewählte Knoten der root-Knoten ist
	public boolean rootIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof String )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgewählte Knoten ein ZVKonto ist
	public boolean zvKontoIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVKonto )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgewählte Knoten ein ZVTitel ist
	public boolean zvTitelIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVTitel )
			return true;
		else
			return false;
	}
	
	// Abfrage ob der ausgewählte Knoten ein ZVUntertitel ist
	public boolean zvUntertitelIsSelected() {
		if( this.getSelectionPath() == null )	// Wenn nichts ausgewählt
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVTitel )
			return false;
		if( ((DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ZVUntertitel )
			return true;
		return false;
	}
	
	// Den root-Knoten auswählen
	public void selectRoot(){
		if( root != null )	// Wenn der root-Knoten existiert
			this.setSelectionPath( new TreePath( root ) );
	}
	
	/**
	 * Löschen des gesammten Baums ausser dem root-Knoten
	 */
	public void delTree() {
		this.setModel( treeModel = new DefaultTreeModel( root = new DefaultMutableTreeNode( root.getUserObject().toString() ) ) );
		selectRoot();
	}
	
	/**
	 * Den aktuellen Knoten aktualisieren
	 */
	public void refreshCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgewählt ist dann wird der null-Zeiger zurückgegeben
			return;
		
		treeModel.nodeChanged( (DefaultMutableTreeNode)this.getSelectionPath().getLastPathComponent() );
	}
	
	// Rückgabe des ausgewählten Knotens
	public DefaultMutableTreeNode getCurrentNode() {
		if( this.getSelectionPath() == null )	// Wenn kein Knoten ausgewählt ist dann wird der null-Zeiger zurückgegeben
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
	
	// ZVKonten in den Baum laden
	public void loadZVKonten( ArrayList zvKonten ) {
		if( zvKonten == null )		// Keine konten angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neu erzeugte Knoten
		this.selectRoot();

		// Schleife um alle ZVKonten in den Baum zu übernehmen
		for( int i = 0; i < zvKonten.size(); i++ ) {
			if( zvKonten.get(i) == null )		// Kein ZVKonto
				continue;
			
			// Das ZVKonten an den root-Knoten anhängen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( zvKonten.get(i) ), root, root.getChildCount() );
			// Alle zugehörigen ZVTitel an das ZVKonto dranhängen
			addZVTitel( ((ZVKonto)zvKonten.get(i)).getSubTitel(), newNode );
		}

		this.selectRoot();		// am Ende den root-Knoten selektieren
		this.expandPath( new TreePath( root ) );	// und den Baum öffnen
	}
	
	// ZVTitel an einen Knoten anhängen
	public void addZVTitel( ArrayList titel, DefaultMutableTreeNode node ) {
		if( titel == null )		// Keine Titel angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neuerzeugte Knoten	
		
		// Alle ZVTitel an den Knoten mit dem ZVKonto anhängen
		for( int i = 0; i < titel.size(); i++ ) {
			if( titel.get(i) == null )		// Kein Titel
				continue;
			// Neuen Knoten erzeugen und an letzter Position anhängen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( titel.get(i) ), node, node.getChildCount() );
			addZVUntertitel( ((ZVTitel)titel.get(i)).getSubUntertitel(), newNode );
		}
	}
	
	// ZVUntertitel an einen ZVTitel-Knoten anhängen
	public void addZVUntertitel( ArrayList untertitel, DefaultMutableTreeNode node ) {
		if( untertitel == null )		// Keine Untertitel angegeben
			return;
		
		DefaultMutableTreeNode newNode;	// Der neuerzeugte Knoten	
		
		// Alle ZVUntertitel an den Knoten mit dem ZVTitel anhängen
		for( int i = 0; i < untertitel.size(); i++ ) {
			if( untertitel.get(i) == null )		// Kein Untertitel
				continue;
			// Neuen Knoten erzeugen und an letzter Position anhängen
			treeModel.insertNodeInto( newNode = new DefaultMutableTreeNode( untertitel.get(i) ), node, node.getChildCount() );
		}
	}


	// Das markierte Konto aktualisieren 
	public void updateNode( Object konto ){
		if( konto == null )		// wenn das konto leer ist
			return;
			
		DefaultMutableTreeNode node;
		if( (node = getCurrentNode()) == null )	// kein Knoten ausgewählt 
			return;
			
		node.setUserObject( konto );	// Das Konto aktualisieren
		selectThisNode( node );
	}

	
	// An den aktuellen Knoten einen neuen Knoten einfügen
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

	// Den aktuellen Knoten löschen
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
	
	// Überprüfung ob ein Node selektiert ist, wenn nicht dann wird der Eleternknoten selektiert
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
