package tetris;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import multiplayer.*;

public class Tetris {
	private static GameForm gameForm;
	private static MenuForm menuForm;
	private static LeaderboardForm leaderboardForm;
	private static MultiplayerForm multiplayerForm;
	
	/**
	 * Tetris osztály main függvénye -
	 * Elindítja és kezeli a programot.
	 * @param args
	 * @see	tetris.MenuForm#MenuForm()
	 * @see tetris.GameForm#GameForm()
	 * @see tetris.LeaderboardForm#LeaderboardForm()
	 */
	public static void main(String[] args) {
			
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				menuForm = new MenuForm();
				gameForm = new GameForm();
				leaderboardForm = new LeaderboardForm("leaderboard");
				multiplayerForm = new MultiplayerForm();
				menuForm.setVisible(true);
			}
		});
	}
	
	/**
	 * Láthatóvá teszi a játékot kezelő JFrame -et
	 * és elindítja a játékot.
	 * @see tetris.gameForm#startGame()
	 */
	public static void start() {
		gameForm.setVisible(true);
		gameForm.startGame();
	}
	
	/**
	 * Megjeleníti a dicsőséglistát.
	 */
	public static void showLeaderboard() {
		leaderboardForm.setVisible(true);
	}
	
	/**
	 * Megjeleníti a menüt.
	 */
	public static void showMenu() {
		menuForm.setVisible(true);
	}
	
	/**
	 * Megjelíti a többjátékos mód JFrame -ét és elindítja a játékot.
	 */
	public static void startMultiplayer() {
		multiplayerForm.setVisible(true);
		multiplayerForm.startGame();
	}
	
	/**
	 * A játék végeztével bekéri a játékos nevét
	 * és eltánteti a játék JFrame -ét.
	 * @param score	A játékos által szerzett pontszám.
	 * @see tetris.LeaderboardForm#addPlayer(String playerName, int score)
	 */
	public static void gameOver(int score) {
		leaderboardForm.addPlayer(JOptionPane.showInputDialog("GameOver!\nPlease enter your name:\n"), score);
		gameForm.setVisible(false);
	}
	
	public static void multiplayerOver(int score) {
		JOptionPane.showMessageDialog(multiplayerForm, "Game Over!\nScore: "+ score, "Game Over", JOptionPane.ERROR_MESSAGE);
		multiplayerForm.setVisible(false);
	}
}
