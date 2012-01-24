package de.gruppe12.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

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
	
	private JPanelBoardDisplay jpnlBoardDisplay;
	private JPanel jpnlGameInfo;
	
	private final Font font;
	
	private final CardLayout cardLO;
	private final GuiController controller;
	private final Container cardLOContainer;
	
	private final DefaultListModel logListModel;
	
	private String playerAttackerName;
	private String playerDefenderName;
	
	
	/* ActionListener, der Buttons mit {ENTER} zu aktivieren ermoeglicht */ 
	private KeyListener enterListener;
	/* FocusListener, der den Inhalt eines JTextFields bei Fokus-Gewinn markiert */
	private FocusListener markOnFocus;

	private JComboBox jcbAi;
	private JComboBox jcbOffenderAi;
	private JComboBox jcbDefenderAi;

	private JTextField jtfCurrentPlayer;

	private JComboBox jcbAiPath;

	private JComboBox jcbOffenderAiPath;

	private JComboBox jcbDefenderAiPath;
	
	/** 
	 * GameGui Konstruktor
	 * 
	 * erzeugt GuiController und setzt die final Attribute cardLO und cardLOContainer 
	 * 
	 */
	public GameGui () {
		super();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Font tempfont;
		try {
			tempfont= Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("images/Viking_n.ttf"));
		} catch (Exception e) {
			tempfont= Font.getFont("Arial");
		}
		font= tempfont;
		
		
		controller= new GuiController();
		controller.setGameGui(this);
		cardLO= new CardLayout();
		cardLOContainer= getContentPane();
		logListModel= new DefaultListModel();
		
		//moveStrategies= controller.getStrats();
		
		initGUI();
		
	}
	/** 
	 * initGUI
	 * 
	 * setzt Fensterstartgroesse, ruft Komponenten bauende Methoden auf 
	 * und f�gt diese anschlie�end im CardLayout zusammen
	 * 
	 */
	
	private void initGUI() {
		
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/viking icon.gif")));
		} catch (IOException ioe) {
			ioe.printStackTrace();
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
		
		setPreferredSize(new Dimension(1000, 600));
		setMinimumSize(new Dimension(600, 400));
		
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
		
		//jbtnHumanVsHuman.setContentAreaFilled(false);
		//jbtnHumanVsHuman.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		
		addToGBPanel(0, 0, 1, 1, 1, 1, Box.createGlue(), jpnlStartMenu);
		addToGBPanel(1, 1, 1, 1, 1, 1, jbtnHumanVsHuman, jpnlStartMenu);
		addToGBPanel(1, 2, 1, 1, 1, 1, jbtnHumanVsAi, jpnlStartMenu);
		addToGBPanel(1, 3, 1, 1, 1, 1, jbtnAiVsAi, jpnlStartMenu);
		addToGBPanel(2, 4, 1, 1, 1, 1, Box.createGlue(), jpnlStartMenu);
		
		jbtnHumanVsHuman.addKeyListener(enterListener);
		jbtnHumanVsAi.addKeyListener(enterListener);
		jbtnAiVsAi.addKeyListener(enterListener);
		
		/* sorgt bei Groessenaenderung daf�r, dass Schritfgroessen angepasst werden */
		jpnlStartMenu.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jbtnHumanVsHuman.getHeight()/3;
				if (fontSize==0) return;
				jbtnHumanVsHuman.setFont(font.deriveFont(Font.BOLD, fontSize));
				jbtnHumanVsAi.setFont(font.deriveFont(Font.BOLD, fontSize));
				jbtnAiVsAi.setFont(font.deriveFont(Font.BOLD, fontSize));
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
				setAttackerName(jtfPlayer1.getText());
				setDefenderName(jtfPlayer2.getText());
				controller.initHvHGame();
				cardLO.show(cardLOContainer, cardNameGamePanel);
				jpnlBoardDisplay.resetSelectedCell();
				update();
			}
		});

		jpnlHumanVsHuman.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jtfPlayer1.getHeight()/3;
				int fontSizeSmall= (int) Math.round(fontSize*0.7);
				if (fontSize==0) return;
				jtfPlayer1.setFont(font.deriveFont(Font.BOLD, fontSize));
				jtfPlayer2.setFont(font.deriveFont(Font.BOLD, fontSize));
				jlbPlayer1.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
				jlbPlayer2.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
				jbtnStart.setFont(font.deriveFont(Font.BOLD, fontSize));
			}
		});
		
	}
	
	private void setAttackerName(String name) {
		playerAttackerName= name;
	}
	
	private void setDefenderName(String name) {
		playerDefenderName= name;
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
		final JLabel jlbAiPath= new JLabel("KI Path:");
		jcbAiPath = new JComboBox();
		jcbAi = new JComboBox();
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
		addToGBPanel(0, 2, 1, 1, 0, 1, Box.createGlue(), jpnlHumanVsAi);
		addToGBPanel(1, 3, 1, 1, 0, 0.6, jlbAiPath, jpnlHumanVsAi);
		addToGBPanel(2, 3, 2, 1, 1, 0.6, jcbAiPath, jpnlHumanVsAi);
		addToGBPanel(1, 4, 1, 1, 0, 1, jlbAi, jpnlHumanVsAi);
		addToGBPanel(2, 4, 2, 1, 1, 1, jcbAi, jpnlHumanVsAi);
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
				int fontSize= jtfPlayer.getHeight()/5;
				int fontSizeSmall= (int) Math.round(fontSize*0.7);
				int fontSizeBig= (int) Math.round(fontSize*1.3);
				int fontSizeExtraBig= (int) Math.round(fontSize*2.0);
				if (fontSize==0) return;
				jtfPlayer.setFont(font.deriveFont(Font.BOLD, fontSizeExtraBig));
				jcbAi.setFont(font.deriveFont(Font.BOLD, fontSizeBig));
				jlbPlayer.setFont(font.deriveFont(Font.BOLD, fontSizeBig));
				jlbAi.setFont(font.deriveFont(Font.BOLD, fontSizeBig));
				jlbAngreifer.setFont(font.deriveFont(Font.BOLD, fontSizeBig));
				jbtnStart.setFont(font.deriveFont(Font.BOLD, fontSize));
				jrbPlayer.setFont(font.deriveFont(Font.BOLD, fontSize));
				jrbAi.setFont(font.deriveFont(Font.BOLD, fontSize));
				jcbAiPath.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
				jlbAiPath.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
			}
		});
		
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean humanIsAttacker= jrbPlayer.isSelected();
				String kiShortName= (String)jcbAi.getSelectedItem();
				if (humanIsAttacker) {
					setAttackerName(jtfPlayer.getText());
					setDefenderName(kiShortName);
				} else {
					setAttackerName(kiShortName);
					setDefenderName(jtfPlayer.getText());
				}
				
				String stratShortPath= (String)jcbAiPath.getSelectedItem();
				String stratPath= controller.getKiFolderList().get(stratShortPath);
				Map<String, String> moveStrategies= controller.getStrats(stratPath);
				String strat= moveStrategies.get(kiShortName);
				
				controller.initHvAGame(humanIsAttacker, stratPath, strat);
				cardLO.show(cardLOContainer, cardNameGamePanel);
				jpnlBoardDisplay.resetSelectedCell();
				update();
			}
		});
		
		jcbAiPath.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				jcbAi.removeAllItems();
				String aiPath= controller.getKiFolderList().get((String)jcbAiPath.getSelectedItem());
				for (String s: controller.getStrats(aiPath).keySet()) {
					jcbAi.addItem(s);
				}
				
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
		final JLabel jlbAiPath= new JLabel("KI Path:");
		final JLabel jlbAiPath2= new JLabel("KI Path:");
		jcbOffenderAiPath = new JComboBox();
		jcbDefenderAiPath = new JComboBox();
		jcbOffenderAi = new JComboBox();
		jcbDefenderAi = new JComboBox();


		
		final JButton jbtnStart= new JButton("Spiel starten");
		jpnlAiVsAi.setLayout(new GridBagLayout());
		addToGBPanel(0, 0, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 1, 1, 1, 0, 0.6, jlbAiPath, jpnlAiVsAi);
		addToGBPanel(2, 1, 2, 1, 1, 0.6, jcbOffenderAiPath, jpnlAiVsAi);
		addToGBPanel(1, 2, 1, 1, 0, 1, jlbOffenderAi, jpnlAiVsAi);
		addToGBPanel(2, 2, 2, 1, 1, 1, jcbOffenderAi, jpnlAiVsAi);
		addToGBPanel(0, 3, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 4, 1, 1, 0, 0.6, jlbAiPath2, jpnlAiVsAi);
		addToGBPanel(2, 4, 2, 1, 1, 0.6, jcbDefenderAiPath, jpnlAiVsAi);
		addToGBPanel(1, 5, 1, 1, 0, 1, jlbDefenderAi, jpnlAiVsAi);
		addToGBPanel(2, 5, 2, 1, 1, 1, jcbDefenderAi, jpnlAiVsAi);
		addToGBPanel(0, 6, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		addToGBPanel(1, 8, 3, 1, 1, 1, jbtnStart, jpnlAiVsAi);
		addToGBPanel(4, 9, 1, 1, 0.2, 1, Box.createGlue(), jpnlAiVsAi);
		
		jpnlHumanVsAi.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jcbOffenderAi.getHeight()/3;
				int fontSizeSmall= (int) Math.round(fontSize*0.7);
				int fontSizeExtraSmall= (int) Math.round(fontSize*0.5);
				if (fontSize==0) return;
				jcbOffenderAi.setFont(font.deriveFont(Font.BOLD, fontSize));
				jcbDefenderAi.setFont(font.deriveFont(Font.BOLD, fontSize));
				jlbOffenderAi.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
				jlbDefenderAi.setFont(font.deriveFont(Font.BOLD, fontSizeSmall));
				jbtnStart.setFont(font.deriveFont(Font.BOLD, fontSize));
				jlbAiPath.setFont(font.deriveFont(Font.BOLD, fontSizeExtraSmall));
				jlbAiPath2.setFont(font.deriveFont(Font.BOLD, fontSizeExtraSmall));
				jcbDefenderAiPath.setFont(font.deriveFont(Font.BOLD, fontSizeExtraSmall));
				jcbOffenderAiPath.setFont(font.deriveFont(Font.BOLD, fontSizeExtraSmall));
			}
		});
		
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String offStratShort=(String)jcbOffenderAi.getSelectedItem();
				String defStratShort=(String)jcbDefenderAi.getSelectedItem();
				
				setAttackerName(offStratShort);
				setDefenderName(defStratShort);
				
				String shortOffStratPath= (String)jcbOffenderAiPath.getSelectedItem();
				String offStratPath= controller.getKiFolderList().get(shortOffStratPath);
				Map<String, String> offMoveStrategies= controller.getStrats(offStratPath);
				String offStrat= offMoveStrategies.get(offStratShort);
				
				String shortDefStratPath= (String)jcbDefenderAiPath.getSelectedItem();
				String defStratPath= controller.getKiFolderList().get(shortDefStratPath);
				Map<String, String> defMoveStrategies= controller.getStrats(defStratPath);
				String defStrat= defMoveStrategies.get(defStratShort);
				
				cardLO.show(cardLOContainer, cardNameGamePanel);
				controller.initAvAGame(offStratPath, offStrat, defStratPath, defStrat);
				jpnlBoardDisplay.resetSelectedCell();
				update();
			}
		});
		
		jcbOffenderAiPath.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				jcbOffenderAi.removeAllItems();
				String aiPath= controller.getKiFolderList().get((String)jcbOffenderAiPath.getSelectedItem());
				for (String s: controller.getStrats(aiPath).keySet()) {
					jcbOffenderAi.addItem(s);
				}
				
			}
		});
		
		jcbDefenderAiPath.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				jcbDefenderAi.removeAllItems();
				String aiPath= controller.getKiFolderList().get((String)jcbDefenderAiPath.getSelectedItem());
				for (String s: controller.getStrats(aiPath).keySet()) {
					jcbDefenderAi.addItem(s);
				}
				
			}
		});
	}
	/**
	 * buildGamePanel
	 * 
	 * baut das GamePanel auf (bestehend aus jpnlGameInfo und jpnlBoardDisplay)
	 */

	private void buildGamePanel() {
		jpnlGamePanel= new JPanel();
		
		buildBoardDisplay();
		buildGameInfo();

		
		jpnlGamePanel.setLayout(new BorderLayout());
		jpnlGamePanel.add(jpnlBoardDisplay, BorderLayout.CENTER);
		jpnlGamePanel.add(jpnlGameInfo, BorderLayout.WEST);
		
	}

	/**
	 * buildBoardDisplay
	 * 
	 * legt mit Uebergabe des GuiController Objekts ein JPanelBoardDisplay an
	 */
	private void buildBoardDisplay() {
		jpnlBoardDisplay= new JPanelBoardDisplay(controller);
		jpnlBoardDisplay.setMinimumSize(new Dimension(300,300));
		jpnlBoardDisplay.setOpaque(true);
		jpnlBoardDisplay.setBackground(Color.LIGHT_GRAY);
		jpnlBoardDisplay.setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * buildGameInfo
	 * 
	 * baut die Anzeige des Logs und des aktuellen Spielzustands auf
	 * 
	 */
	private void buildGameInfo() {
		jpnlGameInfo= new JPanel();
		
		JList jlstLog= new JList(logListModel);
		jlstLog.setPreferredSize(new Dimension(250, 0));
		jlstLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log:"));
		TitledBorder tb= (TitledBorder) jlstLog.getBorder();
		//tb.setTitleJustification(TitledBorder.CENTER);
		tb.setTitleFont(font.deriveFont(12F));
		jlstLog.setOpaque(true);
		jlstLog.setBackground(Color.LIGHT_GRAY);
		jlstLog.setFont(font.deriveFont(14F));
		
		jpnlGameInfo.setLayout(new BorderLayout());
		jpnlGameInfo.add(jlstLog, BorderLayout.CENTER);
		
		jtfCurrentPlayer = new JTextField();
		jtfCurrentPlayer.setEditable(false);
		jtfCurrentPlayer.setFont(font.deriveFont(14F));
		jtfCurrentPlayer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Turn:"));
		((TitledBorder) jtfCurrentPlayer.getBorder()).setTitleFont(font.deriveFont(12F));
		jpnlGameInfo.add(jtfCurrentPlayer, BorderLayout.NORTH);
		jtfCurrentPlayer.setOpaque(true);
		jtfCurrentPlayer.setBackground(Color.LIGHT_GRAY);
		
	}
	
	/**
	 * buildMenuBar
	 * 
	 * baut die Menueleiste auf, die das Beenden des Spiels, 
	 * das Starten eines neuen Spiels und die Auswahl
	 * der KI Jar Datei ermoeglicht
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
				controller.resetLogic();
				cardLO.show(cardLOContainer, cardNameStartMenu);
				jpnlStartMenu.requestFocus();
			}
		});
		
		jmGame.add(jmiNewGame);
		
		
		JMenu jmSettings= new JMenu("Optionen");
		//Ki Jar/Zip Auswahl M�glichkeit
		JMenuItem jmiKiJarChooser= new JMenuItem("Ki Jar Datei hinzufuegen");
		final JFileChooser jfcKiJarChooser= new JFileChooser();
		jfcKiJarChooser.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "JAR (*.jar)";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".jar") || f.isDirectory();
			}
		});
		jmiKiJarChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal= jfcKiJarChooser.showOpenDialog(cardLOContainer);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename= jfcKiJarChooser.getSelectedFile().getPath();
					String shortPath= controller.addKiPathName(filename);
					if (shortPath!=null) {
						jcbAiPath.addItem(shortPath);
						jcbOffenderAiPath.addItem(shortPath);
						jcbDefenderAiPath.addItem(shortPath);
					}
				}
			}
		});
		jmSettings.add(jmiKiJarChooser);
		
		/*JMenuItem jmiKiFolderChooser= new JMenuItem("OrdnerPfad hinzuf�gen");
		final JFileChooser jfcKiFolderChooser= new JFileChooser();
		jfcKiFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jmiKiFolderChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal= jfcKiFolderChooser.showOpenDialog(cardLOContainer);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename= jfcKiFolderChooser.getSelectedFile().getPath();
					String shortPath= controller.addKiPathName(filename);
					if (shortPath!=null) {
						jcbAiPath.addItem(shortPath);
						jcbOffenderAiPath.addItem(shortPath);
						jcbDefenderAiPath.addItem(shortPath);
					}
				}
			}
		});
		jmSettings.add(jmiKiFolderChooser);*/
	
		//JMenu jmHelp= new JMenu("Hilfe");
		
		jmbMenuBar.add(jmFile);
		jmbMenuBar.add(jmGame);
		jmbMenuBar.add(jmSettings);
		//jmbMenuBar.add(jmHelp);			
	}



	public GuiController getController() {
		return controller;
	}
	/**
	 * redraw
	 * 
	 * wird von GuiController durch Observer-Methode update() aufgerufen
	 * und fuehrt zum Neuzeichnen des Spielbretts
	 */

	protected void update() {
		String logString= controller.getLastMoveLog();
		if (logString!= null) {
			logListModel.addElement(logString);
		}
		
		String currentPlayer;
		if (controller.isDefendersTurn()) currentPlayer= "Defender ("+playerDefenderName+")";
		else currentPlayer= "Attacker ("+ playerAttackerName+")";
		
		jtfCurrentPlayer.setText(currentPlayer);
		
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

	


	
