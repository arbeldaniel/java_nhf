package tetris;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class GameForm extends JFrame {
	private JPanel gameAreaPlaceholder;
	private JLabel scoreLabel;
	private JLabel levelLabel;
	private GameArea gameArea;
	private GameThread gameThread;
	
	/**
	 * A GameForm main függvénye -
	 * Létrehozza és megjelníti a JFrame -et.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GameForm frame = new GameForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Inicializál és elíndítja egy szálon a játékot.
	 * @see tetris.GameArea#initBackgroundArray()
	 * @see tetris.GameThread#GameThread(GameArea initGameArea, GameForm initGameForm)
	 * @see	tetris.GameThread#run()
	 */
	public void startGame() {
		gameArea.initBackgroundArray();
		gameThread = new GameThread(gameArea, this);
		gameThread.start();
	}

	/**
	 * Inicializálja a billentyűket, amivel a játékot lehet kezelni.
	 * @param ga
	 * @see tetris.GameArea#moveBlockRight()
	 * @see tetris.GameArea#moveBlockLeft()
	 * @see tetris.GameArea#rotateBlock()
	 * @see tetris.GameArea#dropBlock()
	 */
	private void initControls(GameArea ga) {
		InputMap inputMap = this.getRootPane().getInputMap();
		ActionMap actionMap = this.getRootPane().getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
		inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");


		actionMap.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ga.moveBlockRight();
			}
		});

		actionMap.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ga.moveBlockLeft();
			}
		});

		actionMap.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ga.rotateBlock();
			}
		});

		actionMap.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ga.dropBlock();
			}
		});
	}

	/**
	 * A GameForm osztály konstruktora -
	 * A WindowBuilderrel létrehozott elemeket inicializálja.
	 * A végén átadja a placeholder-t a gameArea -nak és meghívja a billentyűparancsokat inicializáló függvényt.
	 * @see #initControls(GameArea ga)
	 */
	public GameForm() {
		JFrame tempFrame = this;
		
		setBackground(Color.DARK_GRAY);
		getContentPane().setSize(new Dimension(500, 620));
		setResizable(false);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(600, 700);
		getContentPane().setLayout(null);
		
		gameAreaPlaceholder = new JPanel();
		gameAreaPlaceholder.setBounds(144, 25, 301, 601);
		getContentPane().add(gameAreaPlaceholder);
		
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setBounds(460, 25, 120, 15);
		getContentPane().add(scoreLabel);
		
		levelLabel = new JLabel("level: 1");
		levelLabel.setBounds(460, 44, 120, 15);
		getContentPane().add(levelLabel);
		
		JButton menuButton = new JButton("main menu");
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameThread.interrupt();
				Tetris.showMenu();
				tempFrame.setVisible(false);
			}
		});
		menuButton.setBounds(471, 601, 117, 25);
		menuButton.setFocusable(false);
		getContentPane().add(menuButton);
		
		gameArea = new GameArea(gameAreaPlaceholder, 10);

		getContentPane().add(gameArea);

		initControls(gameArea);
	}
	
	/**
	 * Frissíti a JLabele -ben a pontszámot.
	 * @param score	Az új pontszám
	 */
	public void updateScore(int score) {
		scoreLabel.setText("Score: "+ score);
	}
	
	/**
	 * Frissíti a JLabele -ben a szintet.
	 * @param level	Az új szint
	 */
	public void updateLevel(int level) {
		levelLabel.setText("Level: " + level);
	}
}
