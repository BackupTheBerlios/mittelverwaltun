package gui;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
import java.security.*;

public class PasswordEncrypt {

  /**
   * Constructor for the PasswordEncrypt object
   */
  public PasswordEncrypt() { }


  /**
   * This is the method which converts the any string value to MD5
format.
   *
   *@param str password
   *@return     encrypted password in MD5
   */
  public String encrypt(String str) {
	StringBuffer retString = new StringBuffer();
	try {

	  MessageDigest alg = MessageDigest.getInstance("MD5", "SUN");

	  String myVar = str;

	  byte bs[] = myVar.getBytes();

	  byte digest[] = alg.digest(bs);
	  for (int i = 0; i < digest.length; ++i) {
		retString.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
	  }
	} catch (Exception e) {
	  System.out.println("there appears to have been an error " + e);
	}
	return retString.toString();
  }

}
