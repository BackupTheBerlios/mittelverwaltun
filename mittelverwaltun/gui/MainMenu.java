package gui;

//Mario: Änderungen 01.09.2004

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import dbObjects.Rolle;

public class MainMenu extends JMenuBar implements ActionListener {

	// Das Menü "Sitzung"
	JMenu menuSitzung = new JMenu("Sitzung");
			JMenuItem miAccountAendern = new JActivityRelatedMenuItem(0, "Account ändern");
			JMenu menuSichtwechsel = new JMenu("Sichtwechsel");
				JMenuItem miDefaultRolle = new JActivityRelatedMenuItem(0,	"Standardrolle"); 
				JMenu menuTmpRolle = new JMenu("Temporäre Rollen"); 
					JActivityRelatedMenuItem[] miTmpRollen = null; 
			JMenuItem miLogoutBeenden = new JActivityRelatedMenuItem(	0,	"Logout/Beenden");
			
			
	// Das Menü "Bestellung"
	JMenu menuBestellung = new JMenu("Bestellung");
		JMenuItem miAnzeigen = new JActivityRelatedMenuItem(1, "Anzeigen");
		JMenu menuErstellen = new JMenu("Erstellen");
			JMenuItem miAuszahlungsanforderung = new JActivityRelatedMenuItem(1,	"Auszahlungsanforderung");
			JMenuItem miStandardbestellung = new JActivityRelatedMenuItem(	1,"Standardbestellung");
			JMenuItem miASKBestellung = new JActivityRelatedMenuItem(	1,	"ASK Bestellung");
		JMenuItem miAendernStornieren = new JActivityRelatedMenuItem(	1,"Ändern/Stornieren");
		JMenuItem miHUELNummer = new JActivityRelatedMenuItem(1,"HÜL-Nummer eintragen");
		JMenuItem miBegleichen = new JActivityRelatedMenuItem(1, "Begleichen");
		JMenuItem miDrucken = new JActivityRelatedMenuItem(1, "Drucken");
		
	// Das Menü "Mittelverwaltung"
	JMenu menuMittelverwaltung = new JMenu("Mittelverwaltung");
		JMenu menuZenralverwaltung = new JMenu("Zenralverwaltung");
			JMenuItem miKontenbudgetsFestlegen = new JActivityRelatedMenuItem(	1,"Kontenbudgets festlegen");
		JMenu menuFachbereichsintern = new JMenu("Fachbereich");
			JMenuItem miFBKontenUmbuchen = new JActivityRelatedMenuItem( 1, "FBKonten umbuchen" );
			JMenuItem miFBKontenZuweisen = new JActivityRelatedMenuItem( 1, "Budget zuweisen" );
			JMenuItem miFBKontenZuweisenProf = new JActivityRelatedMenuItem( 1, "Budget zuweisen (Prof.)" );
		
		
		
	// Das Menü "Verwaltung"
		JMenu menuVerwaltung = new JMenu("Verwaltung");
		JMenuItem miBenutzer = new JActivityRelatedMenuItem(1, "Benutzer");
		JMenuItem miFirmen = new JActivityRelatedMenuItem(1, "Firmen");
		JMenu menuKonten = new JMenu("Konten");
			JMenuItem miZVKonten = new JActivityRelatedMenuItem(1, "ZV-Konten");
			JMenuItem miFBKonten = new JActivityRelatedMenuItem(1, "FB-Konten");
		JMenuItem miZuordnung = new JActivityRelatedMenuItem(	1,	"Kontenzuordnung");
		JMenuItem miFachbereiche = new JActivityRelatedMenuItem(1,"Fachbereiche");
		JMenuItem miInstitute = new JActivityRelatedMenuItem(1, "Institute");
		JMenu menuHaushaltsjahr = new JMenu( "Haushaltsjahr" );
			JMenuItem miAbschliessenAnlegen = new JActivityRelatedMenuItem(	1,	"Abschließen/Anlegen");
			JMenuItem miAendern = new JActivityRelatedMenuItem(1, "Ändern");
			JMenuItem miMitteluebertrag = new JActivityRelatedMenuItem(	1,	"Mittelübertrag aus vergangenem Jahr");
			
	// Das Menü "Reporting"
	JMenu menuReporting = new JMenu("Reporting");

		JMenuItem miLoglisteAnzeigen = new JActivityRelatedMenuItem(
			1,
			"Logliste anzeigen");
		JMenu menuZVReport = new JMenu("Zentralverwaltung");
			JMenuItem miZVAusgabeNachK = new JActivityRelatedMenuItem(
				1,
				"Ausgabe nach Konten");
			JMenuItem miZVAusgabeNachKundI = new JActivityRelatedMenuItem(
				1,
				"Ausgbe nach Konten und Instituten");
			JMenuItem miZVVerteilung = new JActivityRelatedMenuItem(
				1,
				"Verteilung");
		JMenu menuFBReport = new JMenu("Fachbereichsintern");
			JMenuItem miFBAusgabeNachK = new JActivityRelatedMenuItem(
				1,
				"Ausgabe nach Konten");
			JMenuItem miFBAusgabeNachKundV = new JActivityRelatedMenuItem(
				1,
				"Ausgabe nach Konten und Verwaltungskonten");
		JMenu menuInstitutReport = new JMenu("Institutsintern");
			JMenuItem miIAusgabeNachVK = new JActivityRelatedMenuItem(
				1,
				"Ausgabe nach Verwaltungskonten");
			JMenuItem miBestellungen = new JActivityRelatedMenuItem(
				1,
				"Bestellungen");
			
	// Das Hauptfenster
	MainFrame frame;

	// Die Liste aller rollen-/aktivitätsgebundenen Menüeinträge	
	ArrayList activityRelItems;

	
	public MainMenu( MainFrame frame ) {
		super();
		this.frame = frame;
		activityRelItems = new ArrayList();
				
		// Das Menü "Sitzung"
		add( menuSitzung );	
			menuSitzung.add( miAccountAendern );
			activityRelItems.add( miAccountAendern );
			miAccountAendern.addActionListener( this );
			menuSitzung.add( menuSichtwechsel );
				menuSichtwechsel.add( miDefaultRolle );
				activityRelItems.add( miDefaultRolle );
				miDefaultRolle.addActionListener( this );
				if( frame.getBenutzer() != null ) {
					miTmpRollen = new JActivityRelatedMenuItem[frame.getBenutzer().getTmpRollen().length];						//Mario: Änderung 01.09.2004
																																//
					if (miTmpRollen.length > 0){																				//
						menuSichtwechsel.add( menuTmpRolle );																	//
						menuTmpRolle.addActionListener(this);																	//
						for (int i = 0; i < miTmpRollen.length ; i++){															//
							miTmpRollen[i] = new JActivityRelatedMenuItem(0, frame.getBenutzer().getTmpRollen()[i].toString());	//
							menuTmpRolle.add(miTmpRollen[i]);																	//
							activityRelItems.add(miTmpRollen[i]);																//
							miTmpRollen[i].addActionListener(this);																//
						}																										//
					}																											//
				}				
			menuSitzung.addSeparator();
			menuSitzung.add( miLogoutBeenden );
			activityRelItems.add( miLogoutBeenden );
			miLogoutBeenden.addActionListener( this );
					
		// Das Menü "Bestellung"
		add( menuBestellung );
			menuBestellung.add( miAnzeigen );
			activityRelItems.add( miAnzeigen );
			miAnzeigen.addActionListener( this );
			menuBestellung.add( menuErstellen );
				menuErstellen.add( miAuszahlungsanforderung );
				activityRelItems.add( miAuszahlungsanforderung );
				miAuszahlungsanforderung.addActionListener( this );
				menuErstellen.add( miStandardbestellung );
				activityRelItems.add( miStandardbestellung );
				miStandardbestellung.addActionListener( this );
				menuErstellen.add( miASKBestellung );
				activityRelItems.add( miASKBestellung );
				miASKBestellung.addActionListener( this );
			menuBestellung.add( miAendernStornieren );
			activityRelItems.add( miAendernStornieren );
			miAendernStornieren.addActionListener( this );
			menuBestellung.add( miHUELNummer );
			activityRelItems.add( miHUELNummer );
			miHUELNummer.addActionListener( this );
			menuBestellung.add( miBegleichen );
			activityRelItems.add( miBegleichen );
			miBegleichen.addActionListener( this );
			menuBestellung.add( miDrucken );
			activityRelItems.add( miDrucken );
			miDrucken.addActionListener( this );
		
		// Das Menü "Mittelverwaltung"
		add( menuMittelverwaltung );
			menuMittelverwaltung.add( menuZenralverwaltung );
				menuZenralverwaltung.add( miKontenbudgetsFestlegen );
				activityRelItems.add( miKontenbudgetsFestlegen );
				miKontenbudgetsFestlegen.addActionListener( this );
			menuMittelverwaltung.add( menuFachbereichsintern );
				menuFachbereichsintern.add( miFBKontenUmbuchen );
				activityRelItems.add( miFBKontenUmbuchen );
				miFBKontenUmbuchen.addActionListener( this );	
				menuFachbereichsintern.add( miFBKontenZuweisen );
				activityRelItems.add( miFBKontenZuweisen );
				miFBKontenZuweisen.addActionListener( this );	
				menuFachbereichsintern.add( miFBKontenZuweisenProf );
				activityRelItems.add( miFBKontenZuweisenProf );
				miFBKontenZuweisenProf.addActionListener( this );			
			
		// Das Menü "Verwaltung"
		add( menuVerwaltung );
			menuVerwaltung.add( miBenutzer );
			activityRelItems.add( miBenutzer );
			miBenutzer.addActionListener( this );
			menuVerwaltung.add( miFirmen );
			activityRelItems.add( miFirmen );
			miFirmen.addActionListener( this );
			menuVerwaltung.add( menuKonten );
				menuKonten.add( miZVKonten );
				activityRelItems.add( miZVKonten );
				miZVKonten.addActionListener( this );
				menuKonten.add( miFBKonten );
				activityRelItems.add( miFBKonten );
				miFBKonten.addActionListener( this );
				menuKonten.add( miZuordnung );
				activityRelItems.add( miZuordnung );
				miZuordnung.addActionListener( this );
			menuVerwaltung.add( miFachbereiche );
			activityRelItems.add( miFachbereiche );
			miFachbereiche.addActionListener( this );
			menuVerwaltung.add( miInstitute );
			activityRelItems.add( miInstitute );
			miInstitute.addActionListener( this );
			menuVerwaltung.add( menuHaushaltsjahr );
				menuHaushaltsjahr.add( miAbschliessenAnlegen );
				activityRelItems.add( miAbschliessenAnlegen );
				miAbschliessenAnlegen.addActionListener( this );
				menuHaushaltsjahr.add( miAendern );
				activityRelItems.add( miAendern );
				miAendern.addActionListener( this );
				menuHaushaltsjahr.add( miMitteluebertrag );
				activityRelItems.add( miMitteluebertrag );
				miMitteluebertrag.addActionListener( this );
			
			
		// Das Menü "Reporting"
		add( menuReporting );
			menuReporting.add( miLoglisteAnzeigen );
			activityRelItems.add( miLoglisteAnzeigen );
			miLoglisteAnzeigen.addActionListener( this );
			menuReporting.add( menuZVReport );
				menuZVReport.add( miZVAusgabeNachK );
				activityRelItems.add( miZVAusgabeNachK );
				miZVAusgabeNachK.addActionListener( this );
				menuZVReport.add( miZVAusgabeNachKundI );
				activityRelItems.add( miZVAusgabeNachKundI );
				miZVAusgabeNachKundI.addActionListener( this );
				menuZVReport.add( miZVVerteilung );
				activityRelItems.add( miZVVerteilung );
				miZVVerteilung.addActionListener( this );
			menuReporting.add( menuFBReport );
				menuFBReport.add( miFBAusgabeNachK );
				activityRelItems.add( miFBAusgabeNachK );
				miFBAusgabeNachK.addActionListener( this );
				menuFBReport.add( miFBAusgabeNachKundV );
				activityRelItems.add( miFBAusgabeNachKundV );
				miFBAusgabeNachKundV.addActionListener( this );
			menuReporting.add( menuInstitutReport );
				menuInstitutReport.add( miIAusgabeNachVK );
				activityRelItems.add( miIAusgabeNachVK );
				miIAusgabeNachVK.addActionListener( this );
				menuInstitutReport.add( miBestellungen );
				activityRelItems.add( miBestellungen );
				miBestellungen.addActionListener( this );
		
		// Aktivieren der Menüeinträge
		if( frame.getBenutzer() != null )		
			enableMenuItemsAccordingToRole(frame.getBenutzer().getRolle());
	}
	
	public void actionPerformed(ActionEvent e) {
				
		if ( e.getSource() == miAccountAendern ) {
			frame.addChild( new Benutzerverwaltung(frame) );
		} else if ( e.getSource() == miDefaultRolle ) {						
			enableMenuItemsAccordingToRole(frame.getBenutzer().getRolle());	//
		} else if ( e.getSource() ==  miLogoutBeenden ) {					//
			frame.onWindowClosing();
		} else if ( e.getSource() == miAnzeigen ) {
		} else if ( e.getSource() == miAuszahlungsanforderung ) {
			frame.addChild( new BestellungKlein( frame ) );
		} else if ( e.getSource() == miStandardbestellung ) {
			frame.addChild( new BestellungNormal( frame ) );
		} else if ( e.getSource() == miASKBestellung ) {
			frame.addChild( new BestellungASK( frame ) );
		} else if ( e.getSource() == miAendernStornieren ) {
			frame.addChild( new BestellungStorno( frame ) );
		} else if ( e.getSource() == miHUELNummer ) {
			frame.addChild( new HUELNummer( frame ) );
		} else if ( e.getSource() == miBegleichen ) {
			frame.addChild( new BestellungBegleichen( frame ) );
		} else if ( e.getSource() == miDrucken ) {
		} else if ( e.getSource() == miKontenbudgetsFestlegen ) {
			frame.addChild( new ZVMittelFestlegen( frame ) );
		} else if ( e.getSource() == miFBKontenUmbuchen ) {
			frame.addChild( new FBKontenUmbuchen( frame ) );
		}else if ( e.getSource() == miFBKontenZuweisen ) {
				frame.addChild( new RemmitanceToFBHauptkontoFrame( frame.getApplicationServer() ) );
		}else if ( e.getSource() == miFBKontenZuweisenProf ) {
			frame.addChild( new ProfBudgetFrame( frame.getApplicationServer() ) );
		} else if ( e.getSource() == miBenutzer ) {
			frame.addChild( new Benutzerverwaltung(frame.getApplicationServer()) );
		} else if ( e.getSource() == miFirmen ) {
			frame.addChild( new Firmenverwaltung(frame) );
		} else if ( e.getSource() == menuKonten ) {
		} else if ( e.getSource() == miZVKonten ) {
			frame.addChild( new ZVKontenverwaltung( frame ) );
		} else if ( e.getSource() == miFBKonten ) {
			frame.addChild( new FBKontenverwaltung( frame ) );
		} else if ( e.getSource() == miZuordnung ) {
			frame.addChild( new KontenZuordnungverwaltung( frame.getApplicationServer() ,frame) );
		} else if ( e.getSource() == miFachbereiche ) {
			frame.addChild( new Fachbereichverwaltung(frame.getApplicationServer()) );
		} else if ( e.getSource() == miInstitute ) {
			frame.addChild( new Institutverwaltung( frame.getApplicationServer() ) );
		} else if ( e.getSource() == miAbschliessenAnlegen ) {
			frame.addChild( new HaushaltsjahrAnlegen( frame ) );
		} else if ( e.getSource() == miAendern ) {
			frame.addChild( new HaushaltsjahrAendern(frame.getApplicationServer()) );
		} else if ( e.getSource() == miMitteluebertrag ) {
			frame.addChild( new MittelUebertrag( frame ) );
		} else if ( e.getSource() == miLoglisteAnzeigen ) {
		} else if ( e.getSource() == miZVAusgabeNachK ) {
		} else if ( e.getSource() == miZVAusgabeNachKundI ) {
		} else if ( e.getSource() == miZVVerteilung ) {
		} else if ( e.getSource() == miFBAusgabeNachK ) {
		} else if ( e.getSource() == miFBAusgabeNachKundV ) {
		} else if ( e.getSource() == miIAusgabeNachVK ) {
		} else if ( e.getSource() == miBestellungen ){
		} else {																			//Mario: Änderung 01.09.2004
			int i = 0;																		//
			while ( i < miTmpRollen.length ) {												//
				if ( miTmpRollen[i] == e.getSource() ){										//
					enableMenuItemsAccordingToRole(frame.getBenutzer().getTmpRollen()[i]);	//					
					break;																	//
				}																			//
				i++;																		//
			}																				//
		}																					//
	}
	
	public void enableMenuItemsAccordingToRole (Rolle r){
		Iterator it = activityRelItems.iterator();
		while (it.hasNext()){
			ActivityRelatedElement e = (ActivityRelatedElement) it.next();
			e.setActivityStatus(r);
		}
	}
	
}
