package multiplayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import tetris.Tetris;

public class MultiplayerForm extends JFrame{
	private MultiplayerArea ma;
	private MultiplayerThread mt;
	
	private JPanel placeholder;
	private JLabel scoreLabel;
	private JLabel levelLabel;
	
	/**
	 * A MultiplayerForm main függvénye -
	 * Létrehozza és megjelníti a JFrame -et.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MultiplayerForm frame = new MultiplayerForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Inicializál és elíndítja egy szálon a játékot.
	 * @see multiplayer.MultiplayerArea#initBackgroundArray()
	 * @see multiplayer.MultiplayerThread#MultiplayerThread(GameArea initGameArea, GameForm initGameForm)
	 * @see	multiplayer.MultiplayerThread#run()
	 */
	public void startGame() {
		ma.initBackgroundArray();
		mt = new MultiplayerThread(ma, this);
		mt.start();
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
	
	/**
	 * A MultiplayerForm osztály konstruktora -
	 * A WindowBuilderrel létrehozott elemeket inicializálja.
	 * A végén átadja a placeholder-t a multiplayerArea -nak és meghívja a billentyűparancsokat inicializáló függvényt.
	 * @see #initControls(GameArea ga)
	 */
	public MultiplayerForm() {
		JFrame tempFrame = this;
		
		setBackground(Color.DARK_GRAY);
		getContentPane().setSize(new Dimension(800, 620));
		setResizable(false);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(900, 700);
		getContentPane().setLayout(null);
		
		placeholder = new JPanel();
		placeholder.setBounds(149, 34, 601, 601);
		getContentPane().add(placeholder);
		
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setBounds(768, 34, 120, 15);
		getContentPane().add(scoreLabel);
		
		levelLabel = new JLabel("level: 1");
		levelLabel.setBounds(768, 61, 120, 15);
		getContentPane().add(levelLabel);
		
		JButton menuButton = new JButton("main menu");
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mt.interrupt();
				Tetris.showMenu();
				tempFrame.setVisible(false);
			}
		});
		menuButton.setBounds(762, 610, 117, 25);
		menuButton.setFocusable(false);
		getContentPane().add(menuButton);
		
		ma = new MultiplayerArea(placeholder, 20);
		getContentPane().add(ma);
		initControls(ma);
	}
	
	/**
	 * Inicializálja a billentyűket, amivel a játékot lehet kezelni.
	 * @param ma
	 * @see multiplayer.MultiplayerArea#moveLeftBlockRight()
	 * @see multiplayer.MultiplayerArea#moveLeftBlockLeft()
	 * @see multiplayer.MultiplayerArea#rotateLeftBlock()
	 * @see multiplayer.MultiplayerArea#dropLeftBlock()
	 * @see multiplayer.MultiplayerArea#moveRightBlockRight()
	 * @see multiplayer.MultiplayerArea#moveRightBlockLeft()
	 * @see multiplayer.MultiplayerArea#rotateRightBlock()
	 * @see multiplayer.MultiplayerArea#dropRightBlock()
	 */
	private void initControls(MultiplayerArea ma) {
		InputMap inputMap = this.getRootPane().getInputMap();
		ActionMap actionMap = this.getRootPane().getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("D"), "d");
		inputMap.put(KeyStroke.getKeyStroke("A"), "a");
		inputMap.put(KeyStroke.getKeyStroke("W"), "w");
		inputMap.put(KeyStroke.getKeyStroke("S"), "s");

		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
		inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");

		actionMap.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.moveRightBlockRight();
			}
		});

		actionMap.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.moveRightBlockLeft();
			}
		});

		actionMap.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.rotateRightBlock();
			}
		});

		actionMap.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.dropRightBlock();
			}
		});
		
		actionMap.put("d", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.moveLeftBlockRight();
			}
		});

		actionMap.put("a", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.moveLeftBlockLeft();
			}
		});

		actionMap.put("w", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.rotateLeftBlock();
			}
		});

		actionMap.put("s", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ma.dropLeftBlock();
			}
		});
	}
}
