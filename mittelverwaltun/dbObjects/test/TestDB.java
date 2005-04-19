/* Created on 13.04.2005 */

package dbObjects.test;
import dbObjects.*;
import junit.framework.*;

/**
 * Klasse TestDB. 
 * @author w.flat
 */
public class TestDB extends TestCase {
    
    public TestDB(String name) {
        super(name);
	}
    
    /**
     * Testen von Benutzern. Es werden die Funktionen Gleichheit, Kopieren und Aktualisieren überprüft.
     */
    public void testBenutzer() {
    	// Erstellen von zwei Benutzern
    	Benutzer ben1 = new Benutzer(1, "w.flat", "Flat", "Waldemar");
    	Benutzer ben2 = new Benutzer(2, "r.driesner", "Driesner", "Robert");
    	// Prüfen auf ungleichheit
    	assertFalse("Benutzer aind ungleich", ben1.equals(ben2));
    	// Kopieren eines Benutzers
    	Benutzer ben3 = (Benutzer)ben1.clone();
    	// Prüfen auf Gleichheit
    	assertTrue("Benutzer sind gleich", ben1.equals(ben3));
    }
    
    /**
     * Testen von Firmen. Es werden die Funktionen Gleichheit, Kopieren und Aktualisieren überprüft.
     */
    public void testFirma() {
    	// Zwei Firmen erzeugen
    	Firma firm1 = new Firma(1, "Test", "Teststrasse 1", "68167", "Mannheim", "", "", "", "", "", false, false);
    	Firma firm2 = new Firma(1, "Test1", "Teststrasse 1", "68167", "Mannheim", "", "", "", "", "", false, false);
    	// Firmen sind ungleich
    	assertFalse("Firmen sind ungleich", firm1.equals(firm2));
    	// Firmen sind gleich
    	firm2.setName("Test");
    	assertTrue("Firmen sind gleich", firm1.equals(firm2));
    	firm2.setName("Test1");
    	// Firma kopieren
    	Firma firm3 = (Firma)firm1.clone();
    	assertFalse("Kopierte Firma ist ungleich", firm3.equals(firm2));
    }
    
    /**
     * Testen von ZVKonten, ZVTitel und ZVUntertitel. Es werden die Funktionen Gleichheit, Kopieren und Aktualisieren überprüft.
     */
    public void testZV() {
        // Erzeugen von zwei ZVKonten, die nicht gleich sind
        ZVKonto zv1 = new ZVKonto(1, 1, "Testkonto 1", "24356", "23", 1000.0f, 0.0f, true, '1', (short)0, false, false);
        ZVKonto zv2 = new ZVKonto(2, 1, "Testkonto 2", "24344", "24", 1000.0f, 0.0f, true, '1', (short)0, false, false);
        // Den ZVKonten, ZVTitel zuweisen
        zv1.getSubTitel().add(new ZVTitel(1, zv1, "ZVTitel von zv1", "12323", "", 0.0f, "keine", ""));
        zv2.getSubTitel().add(new ZVTitel(1, zv1, "ZVTitel von zv2", "12324", "", 0.0f, "keine", ""));
       // Behaupten, dass sie ungleich sind
        assertFalse("ZVKonten sind nicht gleich", zv1.equals(zv2));
        // Das eine ZVKonto mit dem anderem aktualisieren
        zv1.setZVKonto(zv2);
        // Behaupten, dass sie gleich sind
        assertTrue("ZVKonten sind gleich", zv1.equals(zv2));
        // Der ZVTitel müsste auch geändert sein
        assertTrue("ZVTitel wurde nicht geändert", ((ZVTitel)zv1.getSubTitel().get(0)).equals((ZVTitel)zv2.getSubTitel().get(0)));
        // Ausgangszustand herstellen
        zv1.setKapitel("24356");
        zv1.setTitelgruppe("23");
        // Behaupten, dass ungleich sind
        assertFalse("ZVKonten sind nicht gleich", zv1.equals(zv2));
        // Eine Kopie vom zv1 erstellen, ZVTitel werden nicht kopiert
        ZVKonto zv3 = (ZVKonto)zv1.clone();
        assertTrue("ZVKonten sind gleich", zv1.equals(zv3));
        assertTrue("ZVTitel-Anzahl ist gleich null", zv3.getSubTitel().size() == 0);
        // Eine Kopie vom zv1 erstellen, ZVTitel werden auch kopiert
        zv3 = (ZVKonto)zv1.cloneWhole();
        assertTrue("ZVKonten sind gleich", zv1.equals(zv3));
        assertTrue("ZVTitel-Anzahl ist gleich null", zv3.getSubTitel().size() > 0);
        assertTrue("Kopierter-ZVTitel = Origanal-ZVTitel", ((ZVTitel)zv1.getSubTitel().get(0)).equals((ZVTitel)zv3.getSubTitel().get(0)));
        // Die Prüfbedingung-String überprüfen
        ((ZVTitel)zv1.getSubTitel().get(0)).setPruefung(true, false, new Float(20.5));
        assertTrue("Prüfbedingung aktiv", ((ZVTitel)zv1.getSubTitel().get(0)).isPruefungActive());
        assertTrue("Prüfbedingung geht ab einer Summe", ((ZVTitel)zv1.getSubTitel().get(0)).isPruefungAb());
        assertTrue("Prüfbedingung bis 20.5", ((ZVTitel)zv1.getSubTitel().get(0)).getPruefsumme().equalsIgnoreCase("20.5"));
        assertTrue("Prüfstring ist in Ordnung", ZVTitel.getPruefung(true, true, new Float("215.0")).equalsIgnoreCase("<215.0"));
    }
    
    /**
     * Testen vom Institut, FBHauptonto und FBUnterkonto. Es werden die Funktionen Gleichheit, Kopieren und Aktualisieren überprüft.
     */
    public void testFB() {
        // Zwei Institute erstellen
        Institut inst1 = new Institut(1, "Institut 1", "123456");
        Institut inst2 = new Institut(2, "Institut 2", "123457");
        // Behauptung, dass keine Hauptkonten vorhanden sind
        assertTrue("Keine FBhauptkonten vorhanden [inst1]", inst1.getHauptkonten().size() == 0);
        assertTrue("Keine FBhauptkonten vorhanden [inst2]", inst2.getHauptkonten().size() == 0);
        // Hauptkonten hinzufügen 
        inst1.getHauptkonten().add(new FBHauptkonto(1, "FBKonto von Inst1", inst1, "01", "0000", 0.0f, 0.0f));
        inst2.getHauptkonten().add(new FBHauptkonto(1, "FBKonto von Inst2", inst2, "01", "0000", 0.0f, 0.0f));
        // Behauptung, dass die Institute ungleich sind. Beide Hauptkonten sind auch ungleich
        assertFalse("Institute sind ungleich", inst1.equals(inst2));
        assertFalse("Institute sind ungleich", inst1.equals(null));
        assertFalse("Die Hauptkonten sind ungleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst2.getHauptkonten().get(0)));
        inst2.setKostenstelle("123456");
        assertTrue("Institute sind gleich", inst1.equals(inst2));
        assertTrue("Die Hauptkonten sind gleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst2.getHauptkonten().get(0)));
        // Ändern des Hauptkontos
        ((FBHauptkonto)inst1.getHauptkonten().get(0)).setHauptkonto("02");
        assertFalse("Die Hauptkonten sind ungleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst2.getHauptkonten().get(0)));
        // Ändern mit falschem Parameter
        ((FBHauptkonto)inst1.getHauptkonten().get(0)).setHauptkonto("0");
        assertFalse("Die Hauptkonten sind ungleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst2.getHauptkonten().get(0)));
        // Ändern des Hauptkontos mit richtigem Parameter
        ((FBHauptkonto)inst1.getHauptkonten().get(0)).setHauptkonto("01");
        assertTrue("Die Hauptkonten sind gleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst2.getHauptkonten().get(0)));
        // Kostenstelle wieder ändern
        inst2.setKostenstelle("123457");
        // Kopieren des Instituts und FBKontos
        Institut inst3 =  (Institut)inst1.clone();
        assertTrue("Keine FBhauptkonten vorhanden [inst3]", inst3.getHauptkonten().size() == 0);
        assertTrue("Kopierter Institut ist gleich", inst3.equals(inst1));
        assertFalse("Kopierter Institut ist ungleich", inst3.equals(inst2));       
        // Hauptkonto kopieren
        inst3.getHauptkonten().add(((FBHauptkonto)inst1.getHauptkonten().get(0)).clone());
        assertTrue("Die Hauptkonten sind gleich", ((FBHauptkonto)inst1.getHauptkonten().get(0)).equals((FBHauptkonto)inst3.getHauptkonten().get(0)));
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestDB.class);
	}
}












