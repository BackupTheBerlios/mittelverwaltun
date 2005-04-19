package gui;

import java.awt.*;

import javax.swing.*;

import dbObjects.Firma;

/**
 * <p>Title: Mittelverwaltung - GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Mario Schmitt
 * @version 1.0
 */

public class FirmenDetails extends JDialog {
  
  JTextField tfName = new JTextField();
  JLabel lbStrasse = new JLabel();
  JLabel lbOrt = new JLabel();
  JLabel lbTel = new JLabel();
  JLabel lbFax = new JLabel();
  JLabel lbMail = new JLabel();
  JLabel lbInternet = new JLabel();
  JLabel lbKundennr = new JLabel();
  JTextField tfStrasse = new JTextField();
  JTextField tfOrt = new JTextField();
  JTextField tfTel = new JTextField();
  JTextField tfFax = new JTextField();
  JTextField tfMail = new JTextField();
  JTextField tfInternet = new JTextField();
  JTextField tfKundennr = new JTextField();

  public FirmenDetails(Component parent, String title, boolean modal, Firma firma) {
    super(JOptionPane.getFrameForComponent(parent), title, modal);
    this.setSize(405, 295);
    this.setResizable(false);
    this.setLocation((JOptionPane.getFrameForComponent(parent).getWidth()/2) - (getWidth()/2), (JOptionPane.getFrameForComponent(parent).getHeight()/2) - (getHeight()/2));
    try {
        lbStrasse.setHorizontalAlignment(SwingConstants.RIGHT);
        lbOrt.setHorizontalAlignment(SwingConstants.RIGHT);
        lbTel.setHorizontalAlignment(SwingConstants.RIGHT);
        lbFax.setHorizontalAlignment(SwingConstants.RIGHT);
        lbMail.setHorizontalAlignment(SwingConstants.RIGHT);
        lbInternet.setHorizontalAlignment(SwingConstants.RIGHT);
        lbKundennr.setHorizontalAlignment(SwingConstants.RIGHT);

        tfName.setBackground(UIManager.getColor("Viewport.background"));
        tfName.setEnabled(false);
        tfName.setFont(new java.awt.Font("Dialog", 1, 14));
        tfName.setBorder(BorderFactory.createEtchedBorder());
        tfName.setDisabledTextColor(Color.black);
        tfName.setText(" " + firma.getName());
        tfName.setBounds(new Rectangle(25, 20, 350, 25));

        lbStrasse.setFont(new java.awt.Font("Dialog", 1, 11));
        lbStrasse.setText("Strasse, Hausnummer >");
        lbStrasse.setBounds(new Rectangle(25, 55, 140, 24));

        tfStrasse.setBounds(new Rectangle(170, 55, 205, 24));
        tfStrasse.setText(firma.getStrasseNr());
        tfStrasse.setDisabledTextColor(Color.black);
        tfStrasse.setBorder(null);
        tfStrasse.setFont(new java.awt.Font("Dialog", 0, 12));
        tfStrasse.setEnabled(false);
        tfStrasse.setBackground(UIManager.getColor("Viewport.background"));

        lbOrt.setBounds(new Rectangle(25, 84, 140, 24));
        lbOrt.setText("Postleitzahl, Ort >");
        lbOrt.setFont(new java.awt.Font("Dialog", 1, 11));

        tfOrt.setBackground(UIManager.getColor("Viewport.background"));
        tfOrt.setEnabled(false);
        tfOrt.setFont(new java.awt.Font("Dialog", 0, 12));
        tfOrt.setBorder(null);
        tfOrt.setDisabledTextColor(Color.black);
        tfOrt.setText(firma.getPlz() + " " + firma.getOrt());
        tfOrt.setBounds(new Rectangle(170, 84, 205, 24));

        lbTel.setFont(new java.awt.Font("Dialog", 1, 11));
        lbTel.setText("Telefon >");
        lbTel.setBounds(new Rectangle(25, 113, 140, 24));

        tfTel.setBackground(UIManager.getColor("Viewport.background"));
        tfTel.setEnabled(false);
        tfTel.setFont(new java.awt.Font("Dialog", 0, 12));
        tfTel.setBorder(null);
        tfTel.setDisabledTextColor(Color.black);
        tfTel.setText(firma.getTelNr());
        tfTel.setBounds(new Rectangle(170, 113, 205, 24));

        lbFax.setFont(new java.awt.Font("Dialog", 1, 11));
        lbFax.setText("Telefaxt >");
        lbFax.setBounds(new Rectangle(25, 142, 140, 24));

        tfFax.setBackground(UIManager.getColor("Viewport.background"));
        tfFax.setEnabled(false);
        tfFax.setFont(new java.awt.Font("Dialog", 0, 12));
        tfFax.setBorder(null);
        tfFax.setDisabledTextColor(Color.black);
        tfFax.setText(firma.getFaxNr());
        tfFax.setBounds(new Rectangle(170, 142, 205, 24));

        lbMail.setFont(new java.awt.Font("Dialog", 1, 11));
        lbMail.setText("E-Mail >");
        lbMail.setBounds(new Rectangle(25, 171, 140, 24));

        tfMail.setBackground(UIManager.getColor("Viewport.background"));
        tfMail.setEnabled(false);
        tfMail.setFont(new java.awt.Font("Dialog", 0, 12));
        tfMail.setBorder(null);
        tfMail.setDisabledTextColor(Color.black);
        tfMail.setText(firma.getEMail());
        tfMail.setBounds(new Rectangle(170, 171, 205, 24));

        lbInternet.setFont(new java.awt.Font("Dialog", 1, 11));
        lbInternet.setText("Internet >");
        lbInternet.setBounds(new Rectangle(25, 200, 140, 24));

        tfInternet.setBackground(UIManager.getColor("Viewport.background"));
        tfInternet.setEnabled(false);
        tfInternet.setFont(new java.awt.Font("Dialog", 0, 12));
        tfInternet.setBorder(null);
        tfInternet.setDisabledTextColor(Color.black);
        tfInternet.setText(firma.getWWW());
        tfInternet.setBounds(new Rectangle(170, 200, 205, 24));

        lbKundennr.setFont(new java.awt.Font("Dialog", 1, 11));
        lbKundennr.setText("Kundennummer >");
        lbKundennr.setBounds(new Rectangle(25, 229, 140, 24));

        tfKundennr.setBackground(UIManager.getColor("Viewport.background"));
        tfKundennr.setEnabled(false);
        tfKundennr.setFont(new java.awt.Font("Dialog", 0, 12));
        tfKundennr.setBorder(null);
        tfKundennr.setDisabledTextColor(Color.black);
        tfKundennr.setText(firma.getKundenNr());
        tfKundennr.setBounds(new Rectangle(170, 229, 205, 24));

       
        this.getContentPane().setLayout(null);
        this.getContentPane().add(tfName, null);
        this.getContentPane().add(lbStrasse, null);
        this.getContentPane().add(lbOrt, null);
        this.getContentPane().add(lbTel, null);
        this.getContentPane().add(lbFax, null);
        this.getContentPane().add(lbMail, null);
        this.getContentPane().add(lbInternet, null);
        this.getContentPane().add(lbKundennr, null);
        this.getContentPane().add(tfStrasse, null);
        this.getContentPane().add(tfOrt, null);
        this.getContentPane().add(tfTel, null);
        this.getContentPane().add(tfFax, null);
        this.getContentPane().add(tfMail, null);
        this.getContentPane().add(tfInternet, null);
        this.getContentPane().add(tfKundennr, null);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
