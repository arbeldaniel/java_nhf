package multiplayer;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import tetris.TetrisBlock;
import tetrisBlocks.*;

public class MultiplayerArea extends JPanel{
	private int gridRows;
	private int gridColumns;
	private int gridCellSize;
	private Color[][] background;
	private TetrisBlock leftBlock;
	private TetrisBlock rightBlock;
	private TetrisBlock[] blocks;
	
	/**
	 * A MultiplayerArea osztály konstruktora -
	 * Egy a WindowBuilder -rel létrehozott JPanel egyes attribútumait átveszi.
	 * Szabályos négyzetekre osztja a pályát és elmenti a méreteket.
	 * Egy tömbbe inicializálja a 7 féle lehetséges elemet.
	 * @param placeholder
	 * @param columns
	 */
	public MultiplayerArea(JPanel placeholder, int columns) {
		this.setBounds(placeholder.getBounds());
		this.setBackground(placeholder.getBackground());
		this.setBorder(placeholder.getBorder());
		
		gridColumns = columns;
		gridCellSize = this.getBounds().width / gridColumns;
		gridRows = this.getBounds().height / gridCellSize;
		
		blocks = new TetrisBlock[] {new BlueRicky(),
									new OrangeRicky(),
									new ClevelandZ(),
									new RhodeIslandZ(),
									new Hero(),
									new Smashboy(),
									new Teewee()};
		leftBlock = new Hero();
		rightBlock = new Hero();
	}
	
	/**
	 * Inicializálja az osztály background változót.
	 */
	public void initBackgroundArray() {
		background = new Color[gridRows][gridColumns];
	}
	
	/**
	 * A bemeneti paraméter alapján kiválasztja és inicializálja a következő alakzatokat.
	 * @param 	nextBlock	A következő alakzat indexe 0-6
	 * @see 	tetris.TetrisBlock#newBlock(TetrisBlock block, int initX)
	 */
	public void spawnBlock(int left, int right) {
		leftBlock.newBlock(blocks[left], (gridColumns - leftBlock.getWidth())/4);
		rightBlock.newBlock(blocks[right], (gridColumns - leftBlock.getWidth())/4 + (gridColumns / 2));

	}
	
	/**
	 * Vizsgálja, hogy az alakzat tud-e lejjeb esni, ha igen akkor lépteti.
	 * @return	Új blokkok következnek feltétel
	 * @see 	#checkBottom(TetrisBlock block)
	 */
	public boolean moveBlockDown() {
		if(checkBottom(leftBlock) == false) {
			if(checkBottom(rightBlock) == false)
				return false;
			rightBlock.moveDown();
			repaint();
			return true;
		}
		if(checkBottom(rightBlock) == false) {
			if(checkBottom(leftBlock) == false)
				return false;
			leftBlock.moveDown();
			repaint();
			return true;
		}
		leftBlock.moveDown();
		rightBlock.moveDown();
		repaint();
		return true;
	}
	
	/**
	 * Vizsgálja, hogy az alakzat leért-e az aljára,
	 * vagy esetleg ráesett egy másik alakzatra.
	 * @param block A vizsgált blokk
	 * @return		Megállás feltétel
	 * @see tetris.TetrisBlock#getBottomEdge()
	 */
	private boolean checkBottom(TetrisBlock block) {
		if(block.getBottomEdge() == gridRows)
			return false;
		
		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();
		
		for(int c = 0; c < w; ++c) {
			for(int r = h - 1; r >= 0; --r) {
				if(shape[r][c] != 0) {	
					int x = c + block.getX();
					int y = r + block.getY() + 1;
					if(y < 0)
						break;
					if(background[y][x] != null)
						return false;
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * Játék vége feltétel, ha mindkét blokk y értéke negatív, vagyis nincsenek a pályán, akkor megszünteti az objektumokat és visszatér.
	 * @return	Játék vége feltétel
	 */
	public boolean isBlockOutOfBounds() {
		if(leftBlock.getY() < 0 || rightBlock.getY() < 0) {
			leftBlock = null;
			rightBlock = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Betölti a két blokot a pályát tartalmazó tömbbe.
	 * @see #blockToBackground(TetrisBlock block)
	 */
	public void moveBlockToBackground() {
		blockToBackground(leftBlock);
		blockToBackground(rightBlock);
	}
	
	/**
	 * Az adott alakzatot betölti a pályát tartalmazó tömbbe.
	 * @param block	A block, amit betöltünk
	 */
	private void blockToBackground(TetrisBlock block) {
		int height = block.getHeight(),
			width  = block.getWidth();
		Color color = block.getColor();
		int[][] shape = block.getShape();
		int xPos = block.getX(),
			yPos = block.getY();
		
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				if(shape[y][x] == 1) {
					background[y + yPos][x + xPos] = color;
				}
			}
		}
	}
	
	/**
	 * Alulról felfelé vizsgálja a sorokat, ahol megtelt egy sor, azt törli,
	 * lejjeb léptet minden sort 1 -el és növeli az ebben a körben elpusztított sorok számát.
	 * @return	Az ebben a körben elpusztított sorok száma
	 * @see #clearLine(int r)
	 * @see #shiftDown(int x)
	 */
	public int clearLines() {
		boolean lineFilled = true;
		int linesCleared = 0;
		
		for(int r = gridRows - 1; r >= 0; --r) {
			for(int c = 0; c < gridColumns; ++c) {
				if(background[r][c] == null) {
					lineFilled = false;
					break;
				}
			}
			if(lineFilled) {
				clearLine(r);
				shiftDown(r);
				clearLine(0);
				repaint();
				++r;
				++linesCleared;
			}
			lineFilled = true;
		}
		return linesCleared;
	}
	
	/**
	 * Teljesen töröl egy sort.
	 * @param r	A törölni kívánt sor indexe.
	 */
	public void clearLine(int r) {
		for(int i = 0; i < gridColumns; ++i) {
			background[r][i] = null;
		}
	}
	
	/**
	 * Egy egész sort 1 -el lejjebb tol.
	 * @param x	A lejebb tolni kívánt sor indexe.
	 */
	public void shiftDown(int x) {
		for(int r = x; r > 0; --r) {
			for(int c = 0; c < gridColumns; ++c) {
				background[r][c] = background[r-1][c];
			}
		}
	}
	
	/**
	 * A baloldali játékos blokkját jobbra mozgatja.
	 */
	public void moveLeftBlockRight() {
		moveBlockRight(leftBlock, rightBlock);
	}
	
	/**
	 * A jobboldali játékos blokkját jobbra mozgatja.
	 */
	public void moveRightBlockRight() {
		moveBlockRight(rightBlock, leftBlock);
	}
	
	/**
	 * Ha lehet, akkor jobbra mozgatja az alakzatot és újraszínezi a JPanel -en.
	 * @param block	A mozgatandó blokk
	 * @param other A másik, nem mozgatandó alakzat.
	 * @see #checkRight(TetrisBlock block, TetrisBLock other)
	 * @see tetris.TetrisBlock#moveRight()
	 */
	private void moveBlockRight(TetrisBlock block, TetrisBlock other) {
		if(block == null)
			return;
		if(!checkRight(block, other))
			return;
		block.moveRight();
		repaint();
	}
	
	/**
	 * Megnézi, hogy tolhatjuk-e jobbra az alakzatot.
	 * @param block	A mozgatandó blokk
	 * @param other A másik, nem mozgatandó blokk
	 * @return	jobbra tolás feltétel
	 * @see 	tetris.TetrisBlock#getRightEdge()
	 */
	private boolean checkRight(TetrisBlock block, TetrisBlock other) {
		if(block.getRightEdge() == gridColumns || block.getDown())
			return false;
		
		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();
		
		for(int r = 0; r < h; ++r) {
			for(int c = w - 1; c >= 0; --c) {
				if(shape[r][c] != 0) {	
					int x = c + block.getX() + 1;
					int y = r + block.getY();
					if(y < 0)
						break;
					if(background[y][x] != null)
						return false;
					if(!other.getDown() && r <= other.getHeight() - 1)
						for(int i = 0; i < other.getWidth(); ++i)
							if(other.getShape()[r][i] == 1 && x == other.getX() + i)
								return false;
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * A baloldali játékos blokkját balra mozgatja.
	 */
	public void moveLeftBlockLeft() {
		moveBlockLeft(leftBlock, rightBlock);
	}
	
	/**
	 * A jobboldali játékos blokkját balra mozgatja.
	 */
	public void moveRightBlockLeft() {
		moveBlockLeft(rightBlock, leftBlock);
	}
	
	/**
	 * Ha lehet, akkor balra mozgatja az alakzatot és újraszínezi a JPanel -en.
	 * @see #checkLeft()
	 * @see tetris.TetrisBlock#moveLeft()
	 */
	private void moveBlockLeft(TetrisBlock block, TetrisBlock other) {
		if(block == null)
			return;
		if(!checkLeft(block, other))
			return;
		block.moveLeft();
		repaint();
	}
	
	/**
	 * Megnézi, hogy tolhatjuk-e balra az alakzatot.
	 * @param block	A mozgatandó blokk
	 * @param other A másik, nem mozgatandó blokk
	 * @return	balra tolás feltétel
	 * @see #moveBlockLeft()
	 * @see tetris.TetrisBlock#getLeftEdge()
	 */
	private boolean checkLeft(TetrisBlock block, TetrisBlock other) {
		if(block.getLeftEdge() == 0 || block.getDown())
			return false;
		
		int[][] shape = block.getShape();
		int w = block.getWidth();
		int h = block.getHeight();
		
		for(int r = 0; r < h; ++r) {
			for(int c = 0; c < w; ++c) {
				if(shape[r][c] != 0) {	
					int x = c + block.getX() - 1;
					int y = r + block.getY();
					if(y < 0)
						break;
					if(background[y][x] != null)
						return false;
					if(!other.getDown() && r <= other.getHeight() - 1)
						for(int i = other.getWidth() - 1; i >= 0; --i)
							if(other.getShape()[r][i] == 1 && x == other.getX() + i)
								return false;
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * A baloldali játékos blokkját tolja le.
	 */
	public void dropLeftBlock() {
		dropBlock(leftBlock);
	}
	
	/**
	 * A jobboldali játékos blokkját tolja le.
	 */
	public void dropRightBlock() {
		dropBlock(rightBlock);
	}
	
	/**
	 * Ha lehet, akkor 1 -el lefelé lépteti az alakzatot.
	 * @param block	A letolandó blokk
	 * @see #checkBottom()
	 * @see tetris.TetrisBlock#moveDown()
	 */
	private void dropBlock(TetrisBlock block) {
		if(block == null)
			return;
		while(checkBottom(block)) {
			block.moveDown();
		}
		repaint();
		blockToBackground(block);
		block.setDown(true);
	}
	
	/**
	 * A baloldali játékos blokkját forgatja.
	 */
	public void rotateLeftBlock() {
		rotateBlock(leftBlock);
	}
	
	/**
	 * A jobboldali játékos blokkját forgatja.
	 */
	public void rotateRightBlock() {
		rotateBlock(rightBlock);
	}
	
	/**
	 * 90 fokkal elforgatja az alakzatot
	 * @param block	Az elforgatandó alakzat
	 * @see	tetris.TetrisBlock#rotate()
	 * @see tetris.TetrisBlock#getRightEdge()
	 */
	public void rotateBlock(TetrisBlock block) {
		if(block == null)
			return;
		block.rotate();
		if(block.getRightEdge() > gridColumns) {
			block.setX(gridColumns - block.getWidth());
		}
		else if(block.getLeftEdge() < 0) {
			block.setX(0);
		}
		else {
			if(block.getRightEdge() != gridColumns)
				block.setX(block.getX() + 1);
			block.setY(block.getY() - 1);
		}
		repaint();
	}
	
	/**
	 * @see javax.swing.JComponent#paintComponenet(Graphics g)
	 * @see #drawBackground(Graphics g)
	 * @see #drawBlock(Graphics g)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawBackground(g);
		drawBlock(g, leftBlock);
		drawBlock(g, rightBlock);
	}
	
	/**
	 * Végigmegy a pályán és berajzolja az elmentett elemeket.
	 * @param g
	 */
	public void drawBackground(Graphics g) {
		Color color;
		
		for(int y = 0; y < gridRows; ++y) {
			for(int x = 0; x < gridColumns; ++x) {
				color = background[y][x];
				if(color != null) {
					int xOffset = x * gridCellSize;
					int yOffset = y * gridCellSize;
					
					drawSquare(g, color, xOffset, yOffset);
				}
			}
		}
	}
	
	/**
	 * Berajzol egy négyzetet egy adott helyre.
	 * @param g
	 * @param c	A négyzet színe
	 * @param x	A négyzet x koordinátája blokkban
	 * @param y A négyzet y koordinátája blokkban
	 */
	public void drawSquare(Graphics g, Color c, int x, int y) {
		g.setColor(c);
		g.fillRect(x, y, gridCellSize, gridCellSize);
		g.setColor(Color.black);
		g.drawRect(x, y, gridCellSize, gridCellSize);
	}
	
	/**
	 * Berajzol egy alakzatot.
	 * @param g
	 * @param block	A berajzolandó alakzat
	 * @see #drawSqare(Graphics g, Color c, int x, int y)
	 */
	private void drawBlock(Graphics g, TetrisBlock block) {
		int height = block.getHeight(),
			width = block.getWidth();
		Color color = block.getColor();
		int[][] shape = block.getShape();
		
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				if(shape[y][x] == 1) {
					int xOffset = (block.getX() + x) * gridCellSize,
						yOffset = (block.getY() + y) * gridCellSize;
					
					drawSquare(g, color, xOffset, yOffset);
				}
			}
		}
	}
	
	/**
	 * Állítja a blokkok isDown flag-jét
	 */
	public void newCycle() {
		leftBlock.setDown(false);
		rightBlock.setDown(false);
	}
}
