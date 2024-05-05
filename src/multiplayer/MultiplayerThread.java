package multiplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tetris.GameForm;
import tetris.Tetris;

public class MultiplayerThread extends Thread{
	private MultiplayerForm mf;
	private MultiplayerArea ma;
	
	private int score = 0;
	private int level = 1;
	private int pause = 1000;
	private int speedUp = 50;
	
	private List<Integer> leftBag;
	private List<Integer> rightBag;
	
	private int clearedLines = 0;
	private int totalLines = 0;
	
	/**
	 * A MultiplayerThread osztály konstruktora -
	 * Inicializálja a játékhoz tartozó JFrame -et és alaphelyzetbe állítja a változókat.
	 * @param initGameArea	A konkrét játéktér, egy JPanel
	 * @param initGameForm	A játék JFrame -e
	 */
	public MultiplayerThread(MultiplayerArea initGameArea, MultiplayerForm initGameForm) {
		this.ma = initGameArea;
		this.mf = initGameForm;
		
		mf.updateScore(score);
		mf.updateLevel(level);
		pause = 1000;
		speedUp = 2;
		leftBag = new ArrayList<Integer>();
		rightBag = new ArrayList<Integer>();
		fillBag(leftBag);
		fillBag(rightBag);
	}
	
	/**
	 * Feltölti az alakzatválasztási logikához szükséges változót.
	 * @param bag
	 */
	private void fillBag(List<Integer> bag) {
		for(int i = 0; i < 7; ++i)
			bag.add(i);
	}
	
	/**
	 * Kiválasztja a következő alakzat indexét.
	 * @param bag
	 * @return	 következő alakzat indexe
	 */
	private int getNextBlock(List<Integer> bag) {
		if(bag.isEmpty())
			fillBag(bag);
		Random rand = new Random();
		int i = rand.nextInt(bag.size());
		int nextBlock = bag.get(i);
		bag.remove(i);
		return nextBlock;
	}
	
	/**
	 * Maga a játékciklus.
	 * Lehelyez egy alakzatot, majd addig ejti le, ameddig tudja.
	 * Utána elhelyezni a pálya tömbjében az alakzatot és frissíti
	 * a pontokat, illetve az aktuáli szintet.
	 * @see tetris.Tetris#multiplayerOver(int score)
	 * @see multiplayer.MultiplayerArea#moveBlockToBackground()
	 * @see #calculateScore()
	 * @see multiplayer.MultiplayerForm#updateScore(int score)
	 * @see multiplayer.MultiplayerForm#updateLevel(int level)
	 * @see multiplayer.MultiplayerArea#newCycle()
	 */
	@Override
	public void run() {
		
		while(true) {
			ma.spawnBlock(getNextBlock(leftBag), getNextBlock(rightBag));
			
			while(ma.moveBlockDown()) {
				try {
					Thread.sleep(pause);
				} catch (InterruptedException e) {
					return;
				}
			}
			if(ma.isBlockOutOfBounds()) {
				Tetris.multiplayerOver(score);
				break;
			}
			ma.moveBlockToBackground();
			calculateScore();
			mf.updateScore(score);
			
			if(totalLines > 2) {
				totalLines -= 3;
				if(level < 19)
					mf.updateLevel(++level);
					pause -= speedUp;
			}
			
			ma.newCycle();
		}
	}
	
	/**
	 * Pontszámítási logika
	 */
	private void calculateScore() {
		switch(clearedLines += ma.clearLines()) {
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
}
