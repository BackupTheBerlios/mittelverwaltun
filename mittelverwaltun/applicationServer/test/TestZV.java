package applicationServer.test;

import junit.framework.TestCase;

/**
 * Mit dieser Klasse werden die ZVKonten, ZVtitel und ZVUntertitel gestestet. 
 * @author w.flat
 * 03.04.2005
 */
public class TestZV extends TestCase {
	
	public TestZV(String name) {
		super(name);
	}
	
	/**
	 * Verbindung aufbauen.
	 */
	protected void setUp() {
	}

	/**
	 * Ressourcen freigeben. 
	 */
	protected void tearDown() {
	}
	
	/**
	 * Testen vom Erstellen eines ZVKontos. 
	 */
	public void testZVKontoAdd() {
	}
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestZV.class);
	}
}
