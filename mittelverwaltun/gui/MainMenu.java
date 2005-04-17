package gui;

//Mario: Änderungen 01.09.2004

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;


import applicationServer.ApplicationServerException;
import dbObjects.Benutzer;
import dbObjects.Rolle;

/**
 * Das HauptMenü im Client-Fenster.
 * @author w.flat
 */
public class MainMenu extends JMenuBar implements ActionListener {

	// Das Menü "Sitzung"
	JMenu menuSitzung = new JMenu("Sitzung");
			JMenuItem miAccountAendern = new JActivityRelatedMenuItem(1, "Account ändern");
			JMenuItem miTmpRollenVerwalten = new JActivityRelatedMenuItem(2, "Temporäre Rollen verwalten");
			JMenu menuSichtwechsel = new JMenu("Sichtwechsel");
				JMenuItem miDefaultRolle = new JActivityRelatedMenuItem(0,	"Standardrolle"); 
				JMenu menuTmpRolle = new JMenu("Temporäre Rollen"); 
					JActivityRelatedMenuItem[] miTmpRollen = null; 
			JMenuItem miLogoutBeenden = new JActivityRelatedMenuItem(	0,	"Logout/Beenden");
		
	// Das Menü "Bestellung"
	JMenu menuBestellung = new JMenu("Bestellung");
		JMenu menuBestellungErstellen = new JMenu("Neu...");
			JMenuItem miAAErstellen = new JActivityRelatedMenuItem(3, "Auszahlungsanforderung");
			JMenuItem miSBErstellen = new JActivityRelatedMenuItem(4, "Standardbestellung");
			JMenuItem miASKErstellen = new JActivityRelatedMenuItem(5, "ASK-Bestellung");
		JMenuItem miBestellungenAnzeigen = new JActivityRelatedMenuItem(0, "Anzeigen");
		
	// Das Menü "Mittelverwaltung"
	JMenu menuMittelverwaltung = new JMenu("Mittelverwaltung");
		JMenu menuZenralverwaltung = new JMenu("Zenralverwaltung");
			JMenuItem miKontenbudgetsFestlegen = new JActivityRelatedMenuItem(9, "Kontenbudgets festlegen");
		JMenu menuFachbereichsintern = new JMenu("Fachbereich");
			JMenuItem miFBKontenUmbuchen = new JActivityRelatedMenuItem( 11, "Umbuchungen zw. Fachbereichskonten" );
			JMenuItem miFBKontenZuweisen = new JActivityRelatedMenuItem( 10, "Mittelverteilung" );
			JMenuItem miFBKontenZuweisenProf = new JActivityRelatedMenuItem( 10, "Institutsweise Mittelverteilung nach Professoren" );

	// Das Menü "Verwaltung"
		JMenu menuVerwaltung = new JMenu("Verwaltung");
		JMenuItem miBenutzer = new JActivityRelatedMenuItem(12, "Benutzer");
		JMenuItem miRollen = new JActivityRelatedMenuItem(13, "Rollen");
		JMenuItem miFirmen = new JActivityRelatedMenuItem(14, "Firmen");
		JMenu menuKonten = new JMenu("Konten");
			JMenuItem miZVKonten = new JActivityRelatedMenuItem(15, "Zentralverwaltungskonten");
			JMenuItem miFBKonten = new JActivityRelatedMenuItem(15, "Fachbereichskonten");
		JMenuItem miZuordnung = new JActivityRelatedMenuItem(	15,	"Kontenzuordnungen");
		JMenuItem miFachbereiche = new JActivityRelatedMenuItem(16,"Fachbereiche");
		JMenuItem miInstitute = new JActivityRelatedMenuItem(17, "Institute");
		JMenu menuHaushaltsjahr = new JMenu( "Haushaltsjahre" );
			JMenuItem miAbschliessen = new JActivityRelatedMenuItem(	18,	"Aktuelles Haushaltsjahr abschließen");
			JMenuItem miAnzeigen = new JActivityRelatedMenuItem(18, "Haushaltsjahre anzeigen");
		
		
	// Das Menü "Reporting"
	JMenu menuReporting = new JMenu("Reports");

		JMenuItem miReportsLoglisteAnzeigen = new JActivityRelatedMenuItem(19, "Anzeigen");
//		JMenu menuZVReport = new JMenu("Zentralverwaltung");
//			JMenuItem miZVAusgabeNachK = new JActivityRelatedMenuItem(1, "Ausgabe nach Konten");
//			JMenuItem miZVAusgabeNachKundI = new JActivityRelatedMenuItem(1, "Ausgbe nach Konten und Instituten");
//			JMenuItem miZVVerteilung = new JActivityRelatedMenuItem(1, "Verteilung");
//		JMenu menuFBReport = new JMenu("Fachbereichsintern");
//			JMenuItem miFBAusgabeNachK = new JActivityRelatedMenuItem(1, "Ausgabe nach Konten");
//			JMenuItem miFBAusgabeNachKundV = new JActivityRelatedMenuItem(1, "Ausgabe nach Konten und Verwaltungskonten");
//		JMenu menuInstitutReport = new JMenu("Institutsintern");
//			JMenuItem miIAusgabeNachVK = new JActivityRelatedMenuItem(1, "Ausgabe nach Verwaltungskonten");
//			JMenuItem miBestellungen = new JActivityRelatedMenuItem(1, "Bestellungen");
			
	// Das Hauptfenster
	MainFrame frame;

	// Die Liste aller rollen-/aktivitätsgebundenen Menüeinträge	
	private ArrayList activityRelItems;

	// Aktive Rolle
	private Rolle activeRole = null;
	
	public MainMenu( MainFrame frame ) {
		super();
		this.frame = frame;
		activityRelItems = new ArrayList();
				
		// Das Menü "Sitzung"
		add( menuSitzung );	
			menuSitzung.add( miAccountAendern );
			activityRelItems.add( miAccountAendern );
			miAccountAendern.addActionListener( this );
			miAccountAendern.setIcon(Functions.getPersonIcon(this.getClass()));
			menuSitzung.add( miTmpRollenVerwalten );
			activityRelItems.add( miTmpRollenVerwalten );
			miTmpRollenVerwalten.addActionListener( this );
			miTmpRollenVerwalten.setIcon(Functions.getRoleIcon(this.getClass()));
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
							miTmpRollen[i] = new JActivityRelatedMenuItem(0, frame.getBenutzer().getTmpRollen()[i].getTmpRolle());	//
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
			miLogoutBeenden.setIcon(Functions.getExitIcon(this.getClass()));		
		// Das Menü "Bestellung"
		add( menuBestellung );
			menuBestellung.add( menuBestellungErstellen );
				menuBestellungErstellen.add(miAAErstellen);
				menuBestellungErstellen.setIcon(Functions.getNewIcon(this.getClass()));
				activityRelItems.add( miAAErstellen );
				miAAErstellen.addActionListener(this);
				menuBestellungErstellen.add(miSBErstellen);
				activityRelItems.add( miSBErstellen );
				miSBErstellen.addActionListener(this);
				menuBestellungErstellen.add(miASKErstellen);
				activityRelItems.add( miASKErstellen );
				miASKErstellen.addActionListener(this);
			menuBestellung.add ( miBestellungenAnzeigen);
			activityRelItems.add( miBestellungenAnzeigen );
			miBestellungenAnzeigen.setIcon(Functions.getPasteIcon(this.getClass()));
			miBestellungenAnzeigen.addActionListener(this);
						
		// Das Menü "Mittelverwaltung"
		add( menuMittelverwaltung );
			menuMittelverwaltung.add( menuZenralverwaltung );
				menuZenralverwaltung.add( miKontenbudgetsFestlegen );
				activityRelItems.add( miKontenbudgetsFestlegen );
				miKontenbudgetsFestlegen.addActionListener( this );
				miKontenbudgetsFestlegen.setIcon(Functions.getBudgetIcon(this.getClass()));
			menuMittelverwaltung.add( menuFachbereichsintern );
				menuFachbereichsintern.add( miFBKontenZuweisen );
				activityRelItems.add( miFBKontenZuweisen );
				miFBKontenZuweisen.addActionListener( this );	
				miFBKontenZuweisen.setIcon(Functions.getMoneyIcon(this.getClass()));
				menuFachbereichsintern.add( miFBKontenZuweisenProf );
				activityRelItems.add( miFBKontenZuweisenProf );
				miFBKontenZuweisenProf.addActionListener( this );
				miFBKontenZuweisenProf.setIcon(Functions.getMoneyIcon(this.getClass()));
				menuFachbereichsintern.add( miFBKontenUmbuchen );
				activityRelItems.add( miFBKontenUmbuchen );
				miFBKontenUmbuchen.addActionListener( this );	
						
			
		// Das Menü "Verwaltung"
		add( menuVerwaltung );
			menuVerwaltung.add( miBenutzer );
			activityRelItems.add( miBenutzer );
			miBenutzer.addActionListener( this );
			miBenutzer.setIcon(Functions.getUserIcon(this.getClass()));
			menuVerwaltung.add( miRollen );
			activityRelItems.add( miRollen );
			miRollen.addActionListener( this );
			miRollen.setIcon(Functions.getRoleIcon(this.getClass()));
			menuVerwaltung.add( miFirmen );
			activityRelItems.add( miFirmen );
			miFirmen.addActionListener( this );
			menuVerwaltung.add( menuKonten );
				menuKonten.setIcon(Functions.getAccountIcon(this.getClass()));
			    menuKonten.add( miZVKonten );
				activityRelItems.add( miZVKonten );
				miZVKonten.addActionListener( this );
				miZVKonten.setIcon(Functions.getAccountIcon(this.getClass()));
				menuKonten.add( miFBKonten );
				activityRelItems.add( miFBKonten );
				miFBKonten.addActionListener( this );
				miFBKonten.setIcon(Functions.getAccountIcon(this.getClass()));
				menuKonten.add( miZuordnung );
				activityRelItems.add( miZuordnung );
				miZuordnung.addActionListener( this );
			menuVerwaltung.add( miFachbereiche );
			activityRelItems.add( miFachbereiche );
			miFachbereiche.addActionListener( this );
			miFachbereiche.setIcon(Functions.getFachbereichIcon(this.getClass()));
			menuVerwaltung.add( miInstitute );
			activityRelItems.add( miInstitute );
			miInstitute.addActionListener( this );
			miInstitute.setIcon(Functions.getInstituteIcon(this.getClass()));
			menuVerwaltung.add( menuHaushaltsjahr );
				menuHaushaltsjahr.setIcon(Functions.getCalendarIcon(this.getClass()));
				miAnzeigen.setIcon(Functions.getCalendarIcon(this.getClass()));
				menuHaushaltsjahr.add( miAnzeigen );
				activityRelItems.add( miAnzeigen );
				miAnzeigen.addActionListener( this );
				miAbschliessen.setIcon(Functions.getKeyIcon(this.getClass()));
				menuHaushaltsjahr.add( miAbschliessen );
				activityRelItems.add( miAbschliessen );
				miAbschliessen.addActionListener( this );
			
		// Das Menü "Reporting"
		add( menuReporting );
			menuReporting.add( miReportsLoglisteAnzeigen );
			activityRelItems.add( miReportsLoglisteAnzeigen );
			miReportsLoglisteAnzeigen.addActionListener( this );
			miReportsLoglisteAnzeigen.setIcon(Functions.getReportIcon(this.getClass()));

		
		// Aktivieren der Menüeinträge
		if( frame.getBenutzer() != null )		
			enableMenuItemsAccordingToRole(frame.getBenutzer().getRolle());
	}
	
	/**
	 * Die Aktionen wenn eines der Menü-Felder angeclickt werden. 
	 */
	public void actionPerformed(ActionEvent e) {

		if ( e.getSource() == miAccountAendern ) {
			frame.addChild( new Benutzerverwaltung(frame, Benutzer.VIEW_PRIVAT) );
		} else if ( e.getSource() == miTmpRollenVerwalten ) {						
			frame.addChild( new TempRollenFrame(frame) );
		} else if ( e.getSource() == miDefaultRolle ) {
			if (frame.desk.getComponentCount() > 0)
				MessageDialogs.showInfoMessageDialog(frame, "Hinweis", "Um eine andere Rolle anzunehmen, müssen zuerst\nalle offenen Dialogfenster ("+ frame.desk.getComponentCount()  +") geschlossen werden.");
			else enableMenuItemsAccordingToRole(frame.getBenutzer().getRolle());	//
		} else if ( e.getSource() ==  miLogoutBeenden ) {					//
			frame.onWindowClosing();
		} else if ( e.getSource() == miAAErstellen ) {
			frame.addChild( new BestellungKlein( frame ) );
		} else if ( e.getSource() == miSBErstellen ) {
			frame.addChild( new BestellungNormal( frame ) );
		} else if ( e.getSource() == miASKErstellen ) {
			frame.addChild( new BestellungASK( frame ) );
		} else if ( e.getSource() == miBestellungenAnzeigen ) {
			frame.addChild( new AuswahlBestellung( frame ) );
		} else if ( e.getSource() == miKontenbudgetsFestlegen ) {
			frame.addChild( new ZVMittelFestlegen( frame ) );
		} else if ( e.getSource() == miFBKontenUmbuchen ) {
			frame.addChild( new FBKontenUmbuchen( frame ) );
		}else if ( e.getSource() == miFBKontenZuweisen ) {
				frame.addChild( new RemmitanceToFBHauptkontoFrame( frame ) );
		}else if ( e.getSource() == miFBKontenZuweisenProf ) {
			frame.addChild( new ProfBudgetFrame( frame ) );
		} else if ( e.getSource() == miBenutzer ) {
			frame.addChild( new Benutzerverwaltung(frame, Benutzer.VIEW_FACHBEREICH) );
		} else if ( e.getSource() == miRollen ) {
			frame.addChild( new RollenAktivitaetenverwaltung(frame) );
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
			frame.addChild( new Fachbereichverwaltung(frame) );
		} else if ( e.getSource() == miInstitute ) {
			frame.addChild( new Institutverwaltung( frame ) );
		} else if ( e.getSource() == miAbschliessen ) {
			try {
				frame.addChild( new AbschlussHaushaltsjahr( frame, frame.getApplicationServer().getCurrentHaushaltsjahrId(), true ) );
			} catch (ApplicationServerException e1) {
				MessageDialogs.showErrorMessageDialog(this, "Fehler", "Aktuelles Haushaltsjahr konnte nicht ermittelt werden.");
				//e1.printStackTrace();
			}
		} else if ( e.getSource() == miAnzeigen ) {
			frame.addChild( new AuswahlHaushaltsjahr(frame) );
		} else if ( e.getSource() == null/*miMitteluebertrag*/ ) {
		} else if ( e.getSource() == miReportsLoglisteAnzeigen ) {
			frame.addChild( new Reports(frame) );

		} else {																			//Mario: Änderung 01.09.2004
			int i = 0;																		//
			while ( i < miTmpRollen.length ) {												//
				if ( miTmpRollen[i] == e.getSource() ){
					if (frame.desk.getComponentCount() > 0)
						MessageDialogs.showInfoMessageDialog(frame, "Hinweis", "Um eine andere Rolle anzunehmen, müssen zuerst\nalle offenen Dialogfenster geschlossen werden.");
					else enableMenuItemsAccordingToRole(frame.getBenutzer().getTmpRollen()[i]);	//					
					break;																	//
				}																			//
				i++;																		//
			}																				//
		}																					//
	}
	
	public void enableMenuItemsAccordingToRole (Rolle r){
		activeRole = r;
		Iterator it = activityRelItems.iterator();
		while (it.hasNext()){
			ActivityRelatedElement e = (ActivityRelatedElement) it.next();
			if (e==miBestellungenAnzeigen)
				miBestellungenAnzeigen.setEnabled(r.hasAktivitaet(6)||r.hasAktivitaet(7)|r.hasAktivitaet(8));
			else e.setActivityStatus(r);
		}
	}
	
	public Rolle getActiveRole(){
		return activeRole;
	}
}
