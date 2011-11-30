package de.gruppe12.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import javax.imageio.ImageIO;


public class GameGui extends JFrame {
	private static final long serialVersionUID = 1L;
	
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

	private KeyListener enterListener;
	
	public GameGui () throws IOException {
		super();
		
		controller= new GuiController();
		controller.setGameGui(this);
		cardLO= new CardLayout();
		cardLOContainer= getContentPane();
		
		initGUI();
		
	}
	
	private void initGUI() throws IOException {
		setPreferredSize(new Dimension(800, 600));
		setIconImage(ImageIO.read(new File("images/viking icon.gif")));
		setTitle("Hnefatafl");
		
		enterListener= new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar()=='\n') {
					((JButton)e.getSource()).doClick();
				}
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
		add(jpnlStartMenu, "Start Menu");
		add(jpnlHumanVsHuman, "Human vs Human");
		add(jpnlHumanVsAi, "Human vs AI");
		add(jpnlAiVsAi, "AI vs AI");
		add(jpnlGamePanel, "Game Panel");
		
		pack();
	}

	private void buildStartMenu() {
		jpnlStartMenu= new JPanel();
		jpnlStartMenu.setLayout(new GridBagLayout());
		final JButton jbtnHumanVsHuman = new JButton("Mensch vs Mensch");
		final JButton jbtnHumanVsAi = new JButton("Mensch vs KI");
		final JButton jbtnAiVsAi = new JButton("KI vs KI");
		
		addToGBPanel(0, 0, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlStartMenu);
		addToGBPanel(1, 1, 1, 1, 0.5, 0.5, jbtnHumanVsHuman, jpnlStartMenu);
		addToGBPanel(1, 2, 1, 1, 0.5, 0.5, jbtnHumanVsAi, jpnlStartMenu);
		addToGBPanel(1, 3, 1, 1, 0.5, 0.5, jbtnAiVsAi, jpnlStartMenu);
		addToGBPanel(2, 4, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlStartMenu);
		
		jbtnHumanVsHuman.addKeyListener(enterListener);
		jbtnHumanVsAi.addKeyListener(enterListener);
		jbtnAiVsAi.addKeyListener(enterListener);
		
		jpnlStartMenu.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jbtnHumanVsHuman.getHeight()/3;
				jbtnHumanVsHuman.setFont(new Font("Arial", Font.BOLD, fontSize));
				jbtnHumanVsAi.setFont(new Font("Arial", Font.BOLD, fontSize));
				jbtnAiVsAi.setFont(new Font("Arial", Font.BOLD, fontSize));
			}
		});
		
		jbtnHumanVsHuman.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, "Human vs Human");
				jpnlHumanVsHuman.requestFocus();
			}
		});
		
	}

	private void buildHumanVsHuman() {
		jpnlHumanVsHuman= new JPanel();
		
		final JLabel jlbPlayer1= new JLabel("Angreifer:");
		final JLabel jlbPlayer2= new JLabel("Verteidiger:");
		final JTextField jtfPlayer1= new JTextField("   Spieler 1   ");
		final JTextField jtfPlayer2= new JTextField("   Spieler 2   ");
		final JButton jbtnStart= new JButton("Spiel starten");
		jpnlHumanVsHuman.setLayout(new GridBagLayout());
		addToGBPanel(0, 0, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 1, 1, 1, 0.5, 0.5, jlbPlayer1, jpnlHumanVsHuman);
		addToGBPanel(2, 1, 1, 1, 0.5, 0.5, jtfPlayer1, jpnlHumanVsHuman);
		addToGBPanel(0, 2, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 3, 1, 1, 0.5, 0.5, jlbPlayer2, jpnlHumanVsHuman);
		addToGBPanel(2, 3, 1, 1, 0.5, 0.5, jtfPlayer2, jpnlHumanVsHuman);
		addToGBPanel(0, 4, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		addToGBPanel(1, 5, 2, 1, 0.5, 0.5, jbtnStart, jpnlHumanVsHuman);
		addToGBPanel(3, 6, 1, 1, 0.5, 0.5, Box.createGlue(), jpnlHumanVsHuman);
		
		jpnlHumanVsHuman.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				jtfPlayer1.setText("Spieler 1");
				jtfPlayer2.setText("Spieler 2");
				jtfPlayer1.requestFocus();
				
			}
		});
		
		FocusListener markOnFocus= new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {}
			
			@Override
			public void focusGained(FocusEvent fe) {
				JTextField field= (JTextField)fe.getSource();
				field.setSelectionStart(0);
				field.setSelectionEnd(field.getText().length());
			}
		};
		
		jtfPlayer1.addFocusListener(markOnFocus);
		jtfPlayer2.addFocusListener(markOnFocus);
		
		KeyAdapter enterToTabListener= new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				((JTextField)e.getSource()).transferFocus();
			}
		};
		
		jtfPlayer1.addKeyListener(enterToTabListener);
		jtfPlayer2.addKeyListener(enterToTabListener);
		jbtnStart.addKeyListener(enterListener);
		jbtnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: Namen speichern
				cardLO.show(cardLOContainer, "Game Panel");
			}
		});

		jpnlStartMenu.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				int fontSize= jtfPlayer1.getHeight()/3;
				jtfPlayer1.setFont(new Font("Arial", Font.BOLD, fontSize));
				jtfPlayer2.setFont(new Font("Arial", Font.BOLD, fontSize));
				jlbPlayer1.setFont(new Font("Arial", Font.PLAIN, fontSize));
				jlbPlayer2.setFont(new Font("Arial", Font.PLAIN, fontSize));
				jbtnStart.setFont(new Font("Arial", Font.BOLD, fontSize));
			
			}
		});
		
	}
	
	private void buildHumanVsAi() {
		jpnlHumanVsAi= new JPanel();
		
	}
	
	private void buildAiVsAi() {
		jpnlAiVsAi= new JPanel();
		
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
		ActionListener devListener= new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLO.show(cardLOContainer, ((JMenuItem)e.getSource()).getActionCommand());
			}
		};
		
		JMenu jmDevelopment= new JMenu("DevButton");
		JMenuItem jmiStartMenu= new JMenuItem("Start Menu");
		JMenuItem jmiHvH= new JMenuItem("Human vs Human");
		JMenuItem jmiHvA= new JMenuItem("Human vs AI");
		JMenuItem jmiAvA= new JMenuItem("AI vs AI");
		JMenuItem jmiGamePanel= new JMenuItem("Game Panel");
		
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

	protected void redraw() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				jpnlBoardDisplay.repaint();
			}
		});
		
	}
	
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

	


	
