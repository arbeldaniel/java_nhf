package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameThread extends Thread {
	private GameArea gameArea;
	private GameForm gameForm;
	private int score = 0;
	private int clearedLines = 0;
	private int totalLines = 0;
	private int level = 1;
	
	private int pause = 1000;
	private int speedUp = 22;
	
	private List<Integer> bag;
	
	/**
	 * A GameThread osztály konstruktora -
	 * Inicializálja a játékhoz tartozó JFrame -et és alaphelyzetbe állítja a változókat.
	 * @param initGameArea	A konkrét játéktér, egy JPanel
	 * @param initGameForm	A játék JFrame -e
	 */
	public GameThread(GameArea initGameArea, GameForm initGameForm) {
		this.gameArea = initGameArea;
		this.gameForm = initGameForm;
		
		gameForm.updateScore(score);
		gameForm.updateLevel(level);
		pause = 1000;
		speedUp = 2;
		bag = new ArrayList<Integer>();
		fillBag();
	}
	
	/**
	 * Maga a játékciklus.
	 * Lehelyez egy alakzatot, majd addig ejti le, ameddig tudja.
	 * Utána elhelyezni a pálya tömbjében az alakzatot és frissíti
	 * a pontokat, illetve az aktuáli szintet.
	 * @see tetris.Tetris#gameOver(int score)
	 * @see tetris.GameArea#moveBlockToBackground()
	 * @see #calculateScore()
	 * @see tetris.GameForm#updateScore(int score)
	 * @see tetris.GameForm#updateLevel(int level)
	 */
	@Override
	public void run() {
		
		while(true) {
			gameArea.spawnBlock(getNextBlock());
			
			while(gameArea.moveBlockDown()) {
				try {
					Thread.sleep(pause);
				} catch (InterruptedException e) {
					return;
				}
			}
			if(gameArea.isBlockOutOfBounds()) {
				Tetris.gameOver(score);
				break;
			}
			gameArea.moveBlockToBackground();
			calculateScore();
			gameForm.updateScore(score);
			
			if(totalLines > level * 10) {
				totalLines -= level * 10;
				if(level < 30)
					gameForm.updateLevel(++level);
					pause -= speedUp;
			}
		}
	}
	
	/**
	 * Pontszámítási logika
	 */
	private void calculateScore() {
		switch(clearedLines += gameArea.clearLines()) {
		case 0:
			break;
		case 1:
			score += level * 40;
			break;
		case 2:
			score += level * 100;
			break;
		case 3:
			score += level * 300;
			break;
		case 4:
			score += level * 1200;
			break;
		}
		totalLines += clearedLines;
		clearedLines = 0;
	}
	
	/**
	 * Feltölti az alakzatválasztási logikához szükséges változót.
	 */
	private void fillBag() {
			for(int i = 0; i < 7; ++i)
				bag.add(i);
	}

	/**
	 * Kiválasztja a következő alakzat indexét.
	 * @return	A következő alakzat indexe
	 */
	private int getNextBlock() {
		if(bag.isEmpty())
			fillBag();
		Random rand = new Random();
		int i = rand.nextInt(bag.size());
		int nextBlock = bag.get(i);
		bag.remove(i);
		return nextBlock;
	}
}
