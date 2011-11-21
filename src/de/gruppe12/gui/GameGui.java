package de.gruppe12.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class GameGui extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JMenuBar jmbMenuBar;
	private CardLayout cardLO;

	private JPanel jpnlStartMenu;
	private JPanel jpnlPlayerKISelection;
	private JPanel jpnlGamePanel;
	
	private JPanel jpnlBoardDisplay;
	private JPanel jpnlGameInfo;
	
	public final GuiController controller;
	
	public GameGui () throws IOException {
		super();
		
		controller= new GuiController();
		cardLO= new CardLayout();
		
		initGUI();
		
	}
	
	private void initGUI() throws IOException {
		setPreferredSize(new Dimension(800, 600));
		setIconImage(ImageIO.read(new File("images/viking icon.gif")));
		setTitle("Hnefatafl");
		
		buildMenuBar();
		
		buildStartMenu();
		buildPlayerKISelection();
		buildGamePanel();
		
		
		
		
		
		
		// -------------------------------
		
		setJMenuBar(jmbMenuBar);
		
		setLayout(cardLO);
		
		add(jpnlStartMenu, "Start Menu");
		add(jpnlPlayerKISelection, "Selection Menu");
		add(jpnlGamePanel, "Game Panel");
		
		pack();
	}

	private void buildStartMenu() {
		jpnlStartMenu= new JPanel();
		
	}

	private void buildPlayerKISelection() {
		jpnlPlayerKISelection= new JPanel();
		
	}

	private void buildGamePanel() {
		jpnlGamePanel= new JPanel();
		jpnlGameInfo= new JPanel();
		
		buildBoardDisplay();
		buildGameInfo();
		
		
		JSplitPane splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				jpnlGameInfo, jpnlBoardDisplay);
		
		
		jpnlGamePanel.setLayout(new BorderLayout());
		jpnlGamePanel.add(splitPane, BorderLayout.CENTER);
		
	}

	private void buildBoardDisplay() {
		jpnlBoardDisplay= new JPanelBoardDisplay(controller);
	}
	
	private void buildGameInfo() {
		
	}




	private void buildMenuBar() {
		jmbMenuBar= new JMenuBar();
		
		JMenu jmDatei= new JMenu("Datei");
		JMenuItem jmiSpielBeenden= new JMenuItem("Spiel Beenden");
		jmiSpielBeenden.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Programm beenden
				
			}
		});
		
		jmDatei.add(jmiSpielBeenden);
		
		
		JMenu jmSpiel= new JMenu("Spiel");
		JMenu jmOptionen= new JMenu("Option");
		JMenu jmHilfe= new JMenu("Hilfe");
		
		jmbMenuBar.add(jmDatei);
		jmbMenuBar.add(jmSpiel);
		jmbMenuBar.add(jmOptionen);
		jmbMenuBar.add(jmHilfe);			
		
		/*######### ######### #########*/
		JMenu jmDevelopment= new JMenu("DevButton");
		JMenuItem jmiStartMenu= new JMenuItem("Start Menu");
		JMenuItem jmiSelectionMenu= new JMenuItem("Selection Menu");
		JMenuItem jmiGamePanel= new JMenuItem("Game Panel");
		
		jmiStartMenu.addActionListener(new DevActionListener(getContentPane(), cardLO, "Start Menu"));
		jmiSelectionMenu.addActionListener(new DevActionListener(getContentPane(), cardLO, "Selection Menu"));
		jmiGamePanel.addActionListener(new DevActionListener(getContentPane(), cardLO, "Game Panel"));
		
		jmDevelopment.add(jmiStartMenu);
		jmDevelopment.add(jmiSelectionMenu);
		jmDevelopment.add(jmiGamePanel);
		
		jmbMenuBar.add(jmDevelopment);
		/*######### ######### #########*/
	}









	public static void main(String[] args) throws FileNotFoundException{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameGui inst= null;
				try {
					inst = new GameGui();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
}

class DevActionListener implements ActionListener {
	private Container ct;
	private CardLayout cl;
	private String name;
	
	public DevActionListener(Container ct, CardLayout cl, String name) {
		this.ct= ct;
		this.cl= cl;
		this.name= name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cl.show(ct, name);
	}
}
