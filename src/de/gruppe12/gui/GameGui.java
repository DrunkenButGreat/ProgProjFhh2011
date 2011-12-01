package de.gruppe12.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import javax.imageio.ImageIO;

/** 
 * GameGui
 * 
 * @author Simon Karyo, Laura Oetter
 * @Version 1.0
 * 
 */
public class GameGui extends JFrame {
	private static final long serialVersionUID = 2L;
	private static final String fonttype= "Arial";
	
	/*static Strings zur Bezeichnung der Panels/Cards im CardLayout*/
	private static String cardNameStartMenu= "Start Menu";
	private static String cardNameHvH= "Human vs Human";
	private static String cardNameHvA= "Human vs AI";
	private static String cardNameAvA= "AI vs AI";
	private static String cardNameGamePanel= "Game Panel";
	
	private JMenuBar jmbMenuBar;

	private JPanel jpnlStartMenu;
	private JPanel jpnlHumanVsHuman;
	private JPanel jpnlHumanVsAi;
	private JPanel jpnlAiVsAi;
	private JPanel jpnlGamePanel;
	
	private JPanel jpnlBoardDisplay;
	private JPanel jpnlGameInfo;
	
	private final CardLayout cardLO;
	private final GuiController controller;
	private final Container cardLOContainer;
	
	/* ActionListener, der Buttons mit {ENTER} zu aktivieren ermoeglicht */ 
	private KeyListener enterListener;
	/* FocusListener, der den Inhalt eines JTextFields bei Fokus-Gewinn markiert */
	private FocusListener markOnFocus;
	
	/** 
	 * GameGui Konstruktor
	 * 
	 * erzeugt GuiController und setzt die final Attribute cardLO und cardLOContainer 
	 * 
	 */
	public GameGui () {
		super();
		
		controller= new GuiController();
		controller.setGameGui(this);
		cardLO= new CardLayout();
		cardLOContainer= getContentPane();
		
		initGUI();
		
	}
	/** 
	 * initGUI
	 * 
	 * setzt Fensterstartgroesse, ruft Komponenten bauende Methoden auf 
	 * und fügt diese anschließend im CardLayout zusammen
	 * 
	 */
	
	private void initGUI() {
		
		try {
			setIconImage(ImageIO.read(new File("images/viking icon.gif")));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(ERROR);
		}
		setTitle("Hnefatafl");
		
		enterListener= new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar()=='\n') {
					((JButton)e.getSource()).doClick();
				}
			}
		};
		
		markOnFocus = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {}
			
			@Override
			public void focusGained(FocusEvent fe) {
				JTextField field= (JTextField)fe.getSource();
				field.setSelectionStart(0);
				field.setSelectionEnd(field.getText().length());
			}
		};
		
		buildMenuBar();
		buildStartMenu();
		buildHumanVsHuman();
		buildHumanVsAi();
		buildAiVsAi();
		buildGamePanel();
		
		setJMenuBar(jmbMenuBar);
		
		setLayout(cardLO);
		add(jpnlStartMenu, cardNameStartMenu);
		add(jpnlHumanVsHuman, cardNameHvH);
		add(jpnlHumanVsAi, cardNameHvA);
		add(jpnlAiVsAi, cardNameAvA);
		add(jpnlGamePanel, cardNameGamePanel);
		
		getContentPane().setPreferredSize(new Dimension(1000, 600));
		
		pack();
	}
	/**
	 * buildStartMenu
	 * 
	 * baut das StartMenu aus vonwoaus zwischen den 3 Spielmodi gewaehlt werden kann.
	 *
	 */

	private void buildStartMenu() {
		jpnlStartMenu= new JPanel();
		jpnlStartMenu.setLayout(new GridBagLayout());
		final JButton jbtnHumanVsHuman = new JButton("Mensch vs Mensch");
		final JButton jbtnHumanVsAi = new JButton("Mensch vs KI");
		final JButton jbtnAiVsAi = new JButton("KI vs KI");
		
		addToGBPanel(0, 0, 1, 1, 1, 1, Box.createGlue(), jpnlStartMenu);
		addToGBPanel(1, 1, 1, 1, 1, 1, jbtnHumanVsHuman, jpnlStartMenu);
		addToGBPanel(1, 2, 1, 1, 1, 1, jbtnHumanVsAi, jpnlStartMenu);
		addToGBPanel(1, 3, 1, 1, 1, 1, jbtnAiVsAi, jpnlStartMenu);
		addToGBPanel(2, 4, 1, 1, 1, 1, Box.createGlue(), jpnlStartMenu);
		
		jbtnHumanVsHuman.addKeyListener(enterListener);
		jbtnHumanVsAi.addKeyListener(enterListener);
		jbtnAiVsAi.addKeyListener(enterListener);
		
		/* sorgt bei Groessenaenderung dafür, dass Schritfgroessen angepasst werden */
		jpnlStartMenu.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jbtnHumanVsHuman.getHeight()/3;
				if (fontSize==0) return;
				jbtnHumanVsHuman.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jbtnHumanVsAi.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jbtnAiVsAi.setFont(new Font(fonttype, Font.BOLD, fontSize));
			}
		});
		
		jbtnHumanVsHuman.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, cardNameHvH);
				jpnlHumanVsHuman.requestFocus();
			}
		});
		
		jbtnHumanVsAi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, cardNameHvA);
				jpnlHumanVsAi.requestFocus();
			}
		});
		
		jbtnAiVsAi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, cardNameAvA);
				jpnlAiVsAi.requestFocus();
			}
		});
		
		jpnlStartMenu.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				jbtnHumanVsHuman.requestFocus();
			}
		});
		
	}
	
	/**
	 * buildHumanVsHuman
	 * 
	 * baut das Mensch-gegen-Mensch Auswahlmenue aus, wo die beiden Spieler 
	 * zur Identifikation einen Namen eingeben koennen
	 */

	private void buildHumanVsHuman() {
		jpnlHumanVsHuman= new JPanel();
		
		final JLabel jlbPlayer1= new JLabel("Angreifer:");
		final JLabel jlbPlayer2= new JLabel("Verteidiger:   ");
		final JTextField jtfPlayer1= new JTextField();
		final JTextField jtfPlayer2= new JTextField();
		final JButton jbtnStart= new JButton("Spiel starten");
		jpnlHumanVsHuman.setLayout(new GridBagLayout());
		addToGBPanel(0, 0, 1, 1, 0.2, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 1, 1, 1, 0, 1, jlbPlayer1, jpnlHumanVsHuman);
		addToGBPanel(2, 1, 1, 1, 1, 1, jtfPlayer1, jpnlHumanVsHuman);
		addToGBPanel(0, 2, 1, 1, 0, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 3, 1, 1, 0, 1, jlbPlayer2, jpnlHumanVsHuman);
		addToGBPanel(2, 3, 1, 1, 1, 1, jtfPlayer2, jpnlHumanVsHuman);
		addToGBPanel(0, 4, 1, 1, 0, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 5, 2, 1, 1, 1, jbtnStart, jpnlHumanVsHuman);
		addToGBPanel(3, 6, 1, 1, 0.2, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		
		jpnlHumanVsHuman.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				jtfPlayer1.setText("Spieler 1");
				jtfPlayer2.setText("Spieler 2");
				jtfPlayer1.requestFocus();
				
			}
		});
		
		
		
		jtfPlayer1.addFocusListener(markOnFocus);
		jtfPlayer2.addFocusListener(markOnFocus);
		
		KeyAdapter enterToTabListener= new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar()== '\n') {
					((JTextField)e.getSource()).transferFocus();
				}
			}
		};
		
		jtfPlayer1.addKeyListener(enterToTabListener);
		jtfPlayer2.addKeyListener(enterToTabListener);
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//controller.initHvHGame();
				cardLO.show(cardLOContainer, cardNameGamePanel);
			}
		});

		jpnlHumanVsHuman.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jtfPlayer1.getHeight()/3;
				if (fontSize==0) return;
				jtfPlayer1.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jtfPlayer2.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jlbPlayer1.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jlbPlayer2.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jbtnStart.setFont(new Font(fonttype, Font.BOLD, fontSize));
			}
		});
		
	}
	
	/**
	 * buildHumanVsAi
	 * 
	 * baut das Mensch-gegen-KI Auswahlmenue, wo der Spieler einen Namen eingeben
	 * und zwischen den vorhandenen Gegner KIs waehlen kann.
	 * 
	 * 2 verknuepfte JRadioButtons dienen zur Auswahl, 
	 * ob Spieler oder KI die Rolle des Angreifers uebernehmen
	 */
	
	private void buildHumanVsAi() {
		jpnlHumanVsAi= new JPanel();
		
		final JLabel jlbPlayer= new JLabel("Spieler:   ");
		final JLabel jlbAi= new JLabel("KI:");
		final JLabel jlbAngreifer= new JLabel("Angreifer:   ");
		final JTextField jtfPlayer= new JTextField("   Spieler 1   ");
		final JComboBox jcbAi= new JComboBox();
		final JRadioButton jrbPlayer= new JRadioButton("Spieler  ");
		final JRadioButton jrbAi= new JRadioButton("KI");
		ActionListener buttonToggle= new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getSource().equals(jrbPlayer)) {
					jrbAi.setSelected(!jrbPlayer.isSelected());
				} else {
					jrbPlayer.setSelected(!jrbAi.isSelected());
				}
			}
		};
		
		jrbPlayer.addActionListener(buttonToggle);
		jrbAi.addActionListener(buttonToggle);
		jrbPlayer.setSelected(true);
		
		final JButton jbtnStart= new JButton("Spiel starten");
		jpnlHumanVsAi.setLayout(new GridBagLayout());
		addToGBPanel(0, 0, 1, 1, 0.2, 1, Box.createGlue(), jpnlHumanVsAi);
		addToGBPanel(1, 1, 1, 1, 0, 1, jlbPlayer, jpnlHumanVsAi);
		addToGBPanel(2, 1, 2, 1, 1, 1, jtfPlayer, jpnlHumanVsAi);
		addToGBPanel(0, 2, 1, 1, 0, 0.5, Box.createGlue(), jpnlHumanVsAi);
		addToGBPanel(1, 3, 1, 1, 0, 1, jlbAi, jpnlHumanVsAi);
		addToGBPanel(2, 3, 2, 1, 1, 1, jcbAi, jpnlHumanVsAi);
		//addToGBPanel(0, 4, 1, 1, 0, 0.1, Box.createGlue(), jpnlHumanVsAi);
		addToGBPanel(1, 5, 1, 1, 0, 1, jlbAngreifer, jpnlHumanVsAi);
		addToGBPanel(2, 5, 1, 1, 0, 1, jrbPlayer , jpnlHumanVsAi);
		addToGBPanel(3, 5, 1, 1, 0.5, 1, jrbAi, jpnlHumanVsAi);
		addToGBPanel(1, 6, 3, 1, 1, 1, jbtnStart, jpnlHumanVsAi);
		addToGBPanel(4, 7, 1, 1, 0.2, 1, Box.createGlue(), jpnlHumanVsAi);
		
		jpnlHumanVsAi.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				jtfPlayer.setText("Spieler");
				jtfPlayer.requestFocus();
			}
		});
		
		jtfPlayer.addFocusListener(markOnFocus);

		jpnlHumanVsAi.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jtfPlayer.getHeight()/3;
				if (fontSize==0) return;
				jtfPlayer.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jcbAi.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jlbPlayer.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jlbAi.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jlbAngreifer.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jbtnStart.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jrbPlayer.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jrbAi.setFont(new Font(fonttype, Font.PLAIN, fontSize));
			}
		});
		
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//controller.initHvAGame(jrbPlayer.isSelected(), jcbAi.getSelectedItem());
				cardLO.show(cardLOContainer, cardNameGamePanel);
			}
		});
		
	}
	
	/**
	 * buildAiVsAi
	 * 
	 * baut das KI-gegen-KI Auswahlmenue auf, wo die vorhandenen Strategien fuer 
	 * Angreifer und Verteidiger eingestellt werden koennen
	 */
	
	private void buildAiVsAi() {
		jpnlAiVsAi= new JPanel();
		
		final JLabel jlbOffenderAi= new JLabel("Angreifer KI:");
		final JLabel jlbDefenderAi= new JLabel("Verteidiger KI:   ");
		final JComboBox jcbOffenderAi= new JComboBox();
		final JComboBox jcbDefenderAi= new JComboBox();


		
		final JButton jbtnStart= new JButton("Spiel starten");
		jpnlAiVsAi.setLayout(new GridBagLayout());
		addToGBPanel(0, 0, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 1, 1, 1, 0, 1, jlbOffenderAi, jpnlAiVsAi);
		addToGBPanel(2, 1, 2, 1, 1, 1, jcbOffenderAi, jpnlAiVsAi);
		addToGBPanel(0, 2, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 3, 1, 1, 0, 1, jlbDefenderAi, jpnlAiVsAi);
		addToGBPanel(2, 3, 2, 1, 1, 1, jcbDefenderAi, jpnlAiVsAi);
		addToGBPanel(0, 4, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 6, 3, 1, 1, 1, jbtnStart, jpnlAiVsAi);
		addToGBPanel(4, 7, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		
		jpnlHumanVsAi.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jcbOffenderAi.getHeight()/3;
				if (fontSize==0) return;
				jcbOffenderAi.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jcbDefenderAi.setFont(new Font(fonttype, Font.BOLD, fontSize));
				jlbOffenderAi.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jlbDefenderAi.setFont(new Font(fonttype, Font.PLAIN, fontSize));
				jbtnStart.setFont(new Font(fonttype, Font.BOLD, fontSize));
			}
		});
		
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//controller.initAvAGame(jcbOffenderAi.getSelectedItem(), jcbDefenderAi.getSelectedItem());
				cardLO.show(cardLOContainer, cardNameGamePanel);
			}
		});
	}

	private void buildGamePanel() {
		jpnlGamePanel= new JPanel();
		jpnlGameInfo= new JPanel();
		
		buildBoardDisplay();
		buildGameInfo();
		

		
		
		jpnlGamePanel.setLayout(new BorderLayout());
		jpnlGamePanel.add(jpnlBoardDisplay, BorderLayout.CENTER);
		
	}

	/**
	 * buildBoardDisplay
	 * 
	 * legt mit Uebergabe des GuiController Objekts ein JPanelBoardDisplay an
	 */
	private void buildBoardDisplay() {
		jpnlBoardDisplay= new JPanelBoardDisplay(controller);
	}
	
	/**
	 * buildGameInfo
	 * 
	 * baut die Anzeige des Logs und des aktuellen Spielzustands auf
	 * 
	 */
	private void buildGameInfo() {
		
	}
	
	/**
	 * buildMenuBar
	 * 
	 * baut die Menueleiste auf, die das Beenden des Spiels und 
	 * das Starten eines neuen Spiels ermoeglicht
	 * 
	 */

	private void buildMenuBar() {
		jmbMenuBar= new JMenuBar();
		
		JMenu jmFile= new JMenu("Datei");
		JMenuItem jmiEndGame= new JMenuItem("Spiel Beenden");
		jmiEndGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		jmFile.add(jmiEndGame);
		
		
		JMenu jmGame= new JMenu("Spiel");
		JMenuItem jmiNewGame= new JMenuItem("Neues Spiel");
		
		jmiNewGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cardLO.show(cardLOContainer, cardNameStartMenu);
				jpnlStartMenu.requestFocus();
			}
		});
		
		jmGame.add(jmiNewGame);
		
		
		JMenu jmSettings= new JMenu("Optionen");
		JMenu jmHelp= new JMenu("Hilfe");
		
		jmbMenuBar.add(jmFile);
		jmbMenuBar.add(jmGame);
		jmbMenuBar.add(jmSettings);
		jmbMenuBar.add(jmHelp);			
		
		/*### Dev Buttons ####*/
		ActionListener devListener= new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, ((JMenuItem)e.getSource()).getActionCommand());
			}
		};
		
		JMenu jmDevelopment= new JMenu("DevButton");
		JMenuItem jmiStartMenu= new JMenuItem(cardNameStartMenu);
		JMenuItem jmiHvH= new JMenuItem(cardNameHvH);
		JMenuItem jmiHvA= new JMenuItem(cardNameHvA);
		JMenuItem jmiAvA= new JMenuItem(cardNameAvA);
		JMenuItem jmiGamePanel= new JMenuItem(cardNameGamePanel);
		
		jmiStartMenu.addActionListener(devListener);
		jmiHvH.addActionListener(devListener);
		jmiHvA.addActionListener(devListener);
		jmiAvA.addActionListener(devListener);
		jmiGamePanel.addActionListener(devListener);
		
		jmDevelopment.add(jmiStartMenu);
		jmDevelopment.add(jmiHvH);
		jmDevelopment.add(jmiHvA);
		jmDevelopment.add(jmiAvA);
		jmDevelopment.add(jmiGamePanel);
		
		jmbMenuBar.add(jmDevelopment);
		/*######### ######### #########*/
	}






	/**
	 * dient dem Testen der GUI
	 * 
	 * @param args
	 */


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameGui inst= null;
				inst = new GameGui();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	/**
	 * redraw
	 * 
	 * wird von GuiController durch Observer-Methode update() aufgerufen
	 * und fuehrt zum Neuzeichnen des Spielbretts
	 */

	protected void redraw() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				jpnlBoardDisplay.repaint();
			}
		});
		
	}
	
	/**
	 * addToGBPanel
	 * 
	 * Hilfsmethode die ein geordnetes Hinzufuegen von Komponenten zu JPanels ermoeglicht
	 * 
	 * @param x			: x-Position der Zelle
	 * @param y			: y-Position der Zelle
	 * @param width		: Anzahl an einzunehmenden Zellen (rechts von x-Position)
	 * @param height	: Anzahl an einzunehmenden Zellen (unterhalb von y-Position)
	 * @param weightx	: bestimmt, wie stark die Komponente bei Groessenaenderung des 
	 * 					  Zielpanels in x-Richtung groessenangepasst werden soll
	 * @param weighty	: wie weightx fuer y-Richtung
	 * @param comp		: die hinzuzufuegende Komponente
	 * @param targetPnl : das JPanel, in das die Komponente hinzugefuegt werden sollt, sollte ein GridBagLayout benutzen
	 *  
	 */
	
	private static void addToGBPanel(int x, int y, int width, int height, double weightx, double weighty, Component comp, JPanel targetPnl) {
		GridBagConstraints gbc= new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=width;
		gbc.gridheight=height;
		gbc.weightx=weightx;
		gbc.weighty=weighty;
		
		targetPnl.add(comp, gbc);
	}
}

	


	
