package gui;

import java.awt.*;

public class Setting {
	
	static void setGBC( Container cont, GridBagLayout gbl, Component comp, int posx, int posy, int gridw, int gridh ){//, int sizex, int sizey) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = posx;
		gbc.gridy = posy;
		gbc.gridwidth = gridw;
		gbc.gridheight = gridh;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets( 5, 5, 5, 5 );
//		gbc.ipadx = sizex;
//		gbc.ipady = sizey;

		gbl.setConstraints( comp, gbc );
		cont.add( comp );
	}
	
	
	static void setPosAndLoc(Container pane, Component comp, int posX, int posY, int sizeX, int sizeY) {
		comp.setBounds( posX, posY, sizeX, sizeY );		
		pane.add( comp );
	}

}
