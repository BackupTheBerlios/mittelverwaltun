package gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Functions {

	static public ImageIcon getFindIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","find.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getZoomInIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","zoomIn.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getBestellIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","cart.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getExpandIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","expand.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getPrintIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","print.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getServerIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Server16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getWebIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","WebComponent16.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getOpenIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Open.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getSaveIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Save.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getRefreshIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","refresh.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}

	static public ImageIcon getRowInsertAfterIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","RowInsertAfter.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getRowDeleteIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","RowDelete.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	
	static public ImageIcon getAddIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","add.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}
	  
	static public ImageIcon getEditIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","edit.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	  
	 
	static public ImageIcon getDelIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","delete.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
	  return null;
	}	  
	  
	static public ImageIcon getCloseIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","close.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 	

	static public ImageIcon getBackIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","back.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 
	
	static public ImageIcon getForwardIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","forward.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}	 				
	
	static public ImageIcon getDown24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Down24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getUp24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Up24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getInformation24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Information24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getTipOfTheDay24Icon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","TipOfTheDay24.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	static public ImageIcon getNewIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","New.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getPasteIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Paste.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	
	static public ImageIcon getCopyIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Copy.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getZoomIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Zoom.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	static public ImageIcon getPropertiesIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Properties.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	static public ImageIcon getStopIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","Stop.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}

	static public ImageIcon getUpIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","up.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	static public ImageIcon getDownIcon(Class clazz){
		try{
		  Image img = Functions.loadImageResource("image","down.gif", clazz);
		  if (img != null)
			  return  new ImageIcon(img);
	  }catch (IOException e){};
		return null;
	}
	
	
	static public InputStream getResourceStream (String pkgname, String fname, Class clazz){
		  String resname = "/" + pkgname.replace('.','/')+ "/" + fname;
		  //Class clazz = getClass();
		  InputStream is = clazz.getResourceAsStream(resname);
		  return is;
  }

  static public Image loadImageResource(String pkgname, String fname, Class clazz) throws IOException{
	  Image ret = null;

	  InputStream is = getResourceStream(pkgname, fname, clazz);

	  if (is != null){

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
}
