package gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class HaushaltsjahrAnlegen extends JInternalFrame implements ActionListener {
	
	JPanel panelWindow = new JPanel();
	JScrollPane scrollZVKonten = new JScrollPane();
	JLabel labZVKonten = new JLabel();
	JList listZVKonten = new JList();
	JRadioButton rbUebernehmen = new JRadioButton();
	JRadioButton rbBeantragtGenehmigt = new JRadioButton();
	JRadioButton rbBeantragt = new JRadioButton();
	JRadioButton rbVerwerfen = new JRadioButton();
	JButton buUebernehmen = new JButton();
	JButton buAnlegen = new JButton();
	JButton buAbbrechen = new JButton();
	JButton buBeenden = new JButton();  
	ButtonGroup buGroup = new ButtonGroup();
	MainFrame frame;

	public HaushaltsjahrAnlegen( MainFrame frame ) {
		super( "Haushaltsjahr abschlieﬂen/anlegen" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.frame = frame;
	  
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		buGroup.add( rbUebernehmen );
		rbUebernehmen.setSelected( true );
		buGroup.add( rbBeantragtGenehmigt );
		buGroup.add( rbBeantragt );
		buGroup.add( rbVerwerfen );
		buUebernehmen.addActionListener( this );
		buBeenden.addActionListener( this );
		buAbbrechen.addActionListener( this );
		buBeenden.addActionListener( this );
		
		this.setSize( 455, 385 );
	}
	
	private void jbInit() throws Exception {
		panelWindow.setBorder(BorderFactory.createEtchedBorder());
		panelWindow.setMaximumSize(new Dimension(32767, 32767));
		panelWindow.setLayout(null);
		labZVKonten.setText("ZV-Konten");
		labZVKonten.setBounds(new Rectangle(12, 12, 150, 15));
		rbUebernehmen.setText("‹bernehmen");
		rbUebernehmen.setBounds(new Rectangle(252, 32, 150, 23));
		rbBeantragtGenehmigt.setText("Beantragt/Genehmigt");
		rbBeantragtGenehmigt.setBounds(new Rectangle(252, 97, 150, 23));
		rbBeantragt.setText("Beantragt");
		rbBeantragt.setBounds(new Rectangle(252, 152, 150, 23));
		rbVerwerfen.setText("Verwerfen");
		rbVerwerfen.setBounds(new Rectangle(252, 207, 150, 23));
		buUebernehmen.setBounds(new Rectangle(12, 262, 200, 25));
		buUebernehmen.setText("‹bernehmen");
		buAnlegen.setBounds(new Rectangle(222, 262, 200, 25));
		buAnlegen.setText("Anlegen");
		buAbbrechen.setBounds(new Rectangle(12, 302, 200, 25));
		buAbbrechen.setText("Abbrechen");
		buBeenden.setBounds(new Rectangle(222, 302, 200, 25));
		buBeenden.setText("Beenden");
		scrollZVKonten.setBounds(new Rectangle(12, 32, 200, 200));
		this.getContentPane().add(panelWindow, BorderLayout.CENTER);
		panelWindow.add(scrollZVKonten, null);
		panelWindow.add(labZVKonten, null);
		scrollZVKonten.getViewport().add(listZVKonten, null);
		panelWindow.add(rbUebernehmen, null);
		panelWindow.add(rbBeantragtGenehmigt, null);
		panelWindow.add(rbBeantragt, null);
		panelWindow.add(rbVerwerfen, null);
		panelWindow.add(buUebernehmen, null);
		panelWindow.add(buAnlegen, null);
		panelWindow.add(buAbbrechen, null);
		panelWindow.add(buBeenden, null);
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buAnlegen ) {
		} else if ( e.getSource() == buUebernehmen ) {
		} else if ( e.getSource() == buAbbrechen ) {
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
	}
}
