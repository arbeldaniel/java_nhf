package tetris;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuForm extends JFrame {
	
	private JPanel contentPane;

	/**
	 * A MenuForm main függvénye -
	 * Létrehozza és megjelníti a JFrame -et.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuForm frame = new MenuForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * A MenuForm osztály konstruktora -
	 * A WindowBuilder által összeállított kód
	 * A startButton megnyomására elrejti a menüt és meghívja Tetris start() függvényét.
	 * A leaderboardButton megnyomásra elrejti a menöt és meghívja a Tetris showLeaderboard() függvényét.
	 * @see tetris.Tetris#start()
	 * @see tetris.Tetris#showLeaderboard()
	 */
	public MenuForm() {
		setResizable(false);
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFrame tempFrame = this;
		
		JButton startButton = new JButton("play");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tetris.start();
				tempFrame.setVisible(false);
			}
		});
		
		startButton.setBounds(139, 62, 164, 25);
		contentPane.add(startButton);
		
		JButton leaderboardButton = new JButton("leaderboard");
		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Tetris.showLeaderboard();
				tempFrame.setVisible(false);
			}
		});
		leaderboardButton.setBounds(139, 136, 164, 25);
		contentPane.add(leaderboardButton);
		
		JButton quitButton = new JButton("quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		quitButton.setBounds(139, 173, 164, 25);
		contentPane.add(quitButton);
		
		JButton btnMultiplayer = new JButton("multiplayer");
		btnMultiplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tetris.startMultiplayer();
				tempFrame.setVisible(false);
			}
		});
		btnMultiplayer.setBounds(139, 99, 164, 25);
		contentPane.add(btnMultiplayer);
	}
}
