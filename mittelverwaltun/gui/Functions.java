package gui;

import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * Klasse mit statischen Methoden, die zum Laden der Image-Dateien verwendet werden. 
 * @author robert
 */
public class Functions {
	
	/**
	 * Laden der Datei "connector.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getConnectorIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","connector.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "exit16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getExitIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","exit16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "account16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getAccountIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","account16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "user16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getUserIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","user16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "role16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getRoleIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","role16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "budget16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getBudgetIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","budget16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "money16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getMoneyIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","money16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "person16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getPersonIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","person16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "report16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getReportIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","report16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "redo.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getRedoIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","redo.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	/**
	 * Laden der Datei "export16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getExportIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","export16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	
	/**
	 * Laden der Datei "key.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getKeyIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","key.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "calendar.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getCalendarIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","calendar.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	
	
		
	/**
	 * Laden der Datei "Import.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getImportIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Import.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "login.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getLoginImage(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","login.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "fhlogo.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getFHLogo(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","fhlogo.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "find.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getFindIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","find.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "ZoomIn16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getZoomInIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","ZoomIn16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "cart.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getBestellIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","cart.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "expand.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getExpandIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","expand.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "PrintPreview16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getPrintIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","PrintPreview16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "Server16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getServerIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Server16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "WebComponent16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getWebIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","WebComponent16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "Open.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getOpenIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Open.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "refresh.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getRefreshIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","refresh.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	/**
	 * Laden der Datei "RowInsertAfter.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getRowInsertAfterIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","RowInsertAfter.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "RowDelete.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getRowDeleteIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","RowDelete.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	/**
	 * Laden der Datei "add.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getAddIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","add.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	  
	/**
	 * Laden der Datei "edit.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getEditIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","edit.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	  
	 
	/**
	 * Laden der Datei "delete.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getDelIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","delete.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	  
	  
	/**
	 * Laden der Datei "close.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getCloseIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","close.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 	

	/**
	 * Laden der Datei "back.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getBackIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","back.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 
	
	/**
	 * Laden der Datei "forward.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getForwardIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","forward.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 				
	
	/**
	 * Laden der Datei "Down24.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getDown24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Down24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "Up24.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getUp24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Up24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "Information24.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getInformation24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Information24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "TipOfTheDay24.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getTipOfTheDay24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","TipOfTheDay24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "New.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getNewIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","New.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "Paste.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getPasteIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Paste.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	
	/**
	 * Laden der Datei "Copy.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getCopyIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Copy.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "Zoom.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getZoomIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Zoom.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "Properties.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getPropertiesIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Properties.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "Stop.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getStopIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Stop.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	/**
	 * Laden der Datei "up.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getUpIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","up.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "down.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getDownIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","down.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "Save16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getSaveIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Save16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "institute16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getInstituteIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","institute16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Laden der Datei "fachbereich16.gif".
	 * @param clazz = Class von einer Componente.
	 * @return Das geladene ImageIcon.
	 */
	static public ImageIcon getFachbereichIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","fachbereich16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	/**
	 * Den InputStream einer Datei ermitteln. 
	 * @param pkgname = Der Name vom Package, in dme sich eine Datei befindet. 
	 * @param fname = Der Name der Datei.
	 * @param clazz = Class einer beliebigen Componente.
	 * @return InputStream das ermittelt wurde. 
	 */	
	static public InputStream getResourceStream (String pkgname, String fname, Class clazz){
		String resname = "/" + pkgname.replace('.','/')+ "/" + fname;
		InputStream is = clazz.getResourceAsStream(resname);
		return is;
	}

	/**
	 * Das angegebene Image laden. 
	 * @param pkgname = Der Name vom Package, in dme sich eine Datei befindet. 
	 * @param fname = Der Name der Datei.
	 * @param clazz = Class einer beliebigen Componente.
	 * @return Image das geladen wurde. 
	 * @throws IOException
	 */
	static public Image loadImageResource(String pkgname, String fname, Class clazz) throws IOException{
		Image ret = null;
		InputStream is = getResourceStream(pkgname, fname, clazz);

		if (is != null)	{
			byte[] buffer = new byte[0];
			byte[] tmpbuf = new byte[1024];
			
			while (true){
				int len = is.read(tmpbuf);
				if (len<=0){
					break;
				}
				byte[] newbuf = new byte[buffer.length + len];
				System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
				System.arraycopy(tmpbuf, 0, newbuf, buffer.length, len);
				buffer = newbuf;
			}
			// create image
			ret = Toolkit.getDefaultToolkit().createImage(buffer);
			is.close();
		}
		return ret;
	}
	
	/**
	 * Erstellen von allen nicht existierenden Verzeichnissen<br>
	 * vom aktuellen Verzeichnis bis zum angegebenem Verzeichnis.<br>
	 * author w.flat
	 * @param dir = Verzeichnis, bis einschließlich welchem man alle Verzeichnisse erstellen soll.
	 */
	static public void makeDirs(File dir) {
		if(dir.getAbsolutePath().equals((new File("")).getAbsolutePath()) || dir.getAbsolutePath().equals(""))
			return;
		makeDirs(dir.getParentFile());
		if(!dir.exists())
			dir.mkdir();
	}
	
	/**
	 * Methode zum Auslagern einer beliebigen Datei aus der Jar-Datei <br>
	 * in die gleiche Datei ausserhalb der Jar-Datei. <br>
	 * Wenn die Datei existiert wird sie nicht überschrieben.<br>
	 * author w.flat
	 * @param pkgname = Der Name vom Package, in dem sich eine Datei befindet. 
	 * @param fname = Der Name der Datei.
	 * @param clazz = Class einer beliebigen Componente.
	 * @throws IOException
	 */
	static public void restoreFile(String pkgname, String fname, Class clazz) throws IOException {
		File outFile = new File(pkgname.replace('.', File.separatorChar) + File.separator + fname);
		makeDirs(outFile.getAbsoluteFile().getParentFile());
		if(!outFile.exists()) {
			InputStream is = getResourceStream(pkgname, fname, clazz);
			FileOutputStream out = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
			  out.write(buf, 0, len);
			}
			out.close();
			is.close();        		
		}
	}
	
	final public static int CODE = 1;
	final public static int DECODE = 2;
	final private static int DIVISOR = 123456;
	final private static String KEY = "OO2-DBA-Projekt";

	/**
	 * XOR-Verschlüsselung oder -Entschlüsselung eines übergebenen Textes.
	 * @param text = Text der verschlüsselt oder entschlüsselt werden soll.
	 * @param cORd = Flag zum Entscheiden ob verschlüsselt oder entschlüsselt werden soll. <br>
	 *               Hat Auswirkungen auf ob man die Prüfsumme überprüfen oder hinzufügen muss.
	 * @return Der entschlüsselte oder verschlüsselte Text.
	 */
	static public String xorText(String text, int cORd) {
		String result = text;
		StringBuffer buf = new StringBuffer();
		int summe = 0;
		try {
			if(cORd == CODE) {			// Wenn der String mit XOR kodieren werden soll
			    for(int i = 0; i < text.length(); i++)
			        summe += (int)text.charAt(i);
			    result = "" + (summe % DIVISOR) + "\n" + result;	// Prüfsumme vorne anhängen
			}
			for(int i = 0; i < result.length(); i++)				// XOR-Verfahren durchführen
			    buf.append((char)(result.charAt(i) ^ KEY.charAt(i % KEY.length())));
			if(cORd == DECODE) {		// Wenn der String mit XOR dekodiert werden soll
			    result = buf.substring(buf.indexOf("\n") + 1);
			    summe = 0;
			    for(int i = 0; i < result.length(); i++)
			        summe += (int)result.charAt(i);
			    if((summe % DIVISOR) != Integer.parseInt(buf.substring(0, buf.indexOf("\n"))))
			        throw new Exception("Keine Übereinstimmung bei der Prüfsumme !");
			} else {
			    result = buf.toString();
			}
		} catch(Exception exc) {
		    result = "";
		}
		return result;
	}

	/**
	 * Einlesen einer Datei und Ausgabe als String.
	 * @param fileName = Datei, die eingelesen werden soll.
	 * @return Der Datei-Inhlat als String.
	 */
	static public String readFile(String fileName) {
		StringBuffer result = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
			int ch;
			while((ch = in.read()) != -1) {
				result.append((char)ch);
			}
			in.close();
		} catch(Exception e) {
		}
		return result.toString();
	}

	/**
	 * Einen Text in die Datei reinschreiben.
	 * @param fileName = Datei, die eingelesen werden soll.
	 * @param text = Der neue Inhalt der Datei.
	 */
	static public void writeFile(String fileName, String text) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileName)));
			out.write(text);
			out.close();
		} catch(Exception e) {
		}
	}
}
