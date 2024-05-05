package tetris;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import tetrisBlocks.*;

public class GameArea extends JPanel {
	
	private int gridRows;
	private int gridColumns;
	private int gridCellSize;
	private Color[][] background;
	private TetrisBlock block;
	private TetrisBlock[] blocks;
	
	
	/**
	 * A GameArea osztály konstruktora -
	 * Egy a WindowBuilder -rel létrehozott JPanel egyes attribútumait átveszi.
	 * Szabályos négyzetekre osztja a pályát és elmenti a méreteket.
	 * Egy tömbbe inicializálja a 7 féle lehetséges elemet.
	 * @param placeholder	WindowBuilder -rel léthezott JPanel
	 * @param columns		A pályán lévő kívánt oszlopok száma, vagyis a pálya szélessége blokkban
	 */
	public GameArea(JPanel placeholder, int columns) {
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
	}
	
	/**
	 * Inicializálja az osztály background változót.
	 */
	public void initBackgroundArray() {
		background = new Color[gridRows][gridColumns];
	}
	
	
	/**
	 * A bemeneti paraméter alapján kiválasztja és inicializálja a következő alakzatot.
	 * @param 	nextBlock	A következő alakzat indexe 0-6
	 * @see 	tetris.TetrisBlock#spawn(int gridWidth)
	 */
	public void spawnBlock(int nextBlock) {
		block = blocks[nextBlock];
		block.spawn(gridColumns);
	}
	
	/**
	 * Játék vége feltétel, ha a blokk y értéke negatív, vagyis nincs a pályán, akkor megszünteti az objektumot és visszatér.
	 * @return	Játék vége feltétel
	 */
	public boolean isBlockOutOfBounds() {
		if(block.getY() < 0) {
			block = null;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Vizsgálja, hogy az alakzat tud-e lejjeb esni, ha igen akkor lépteti.
	 * @return	Új blokk következik feltétel
	 * @see 	#checkBottom()
	 */
	public boolean moveBlockDown() {
		if(checkBottom() == false) {
			return false;
		}
		block.moveDown();
		repaint();
		return true;
	}
	
	/**
	 * Vizsgálja, hogy az alakzat leért-e az aljára,
	 * vagy esetleg ráesett egy másik alakzatra.
	 * @return	Megállás feltétel
	 * @see tetris.TetrisBlock#getBottomEdge()
	 */
	private boolean checkBottom() {
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
	 * Megnézi, hogy tolhatjuk-e balra az alakzatot.
	 * @return	balra tolás feltétel
	 * @see #moveBlockLeft()
	 * @see tetris.TetrisBlock#getLeftEdge()
	 */
	private boolean checkLeft() {
		if(block.getLeftEdge() == 0)
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
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * Megnézi, hogy tolhatjuk-e jobbra az alakzatot.
	 * @return	jobbra tolás feltétel
	 * @see 	tetris.TetrisBlock#getRightEdge()
	 */
	private boolean checkRight() {
		if(block.getRightEdge() == gridColumns)
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
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * Ha lehet, akkor jobbra mozgatja az alakzatot és újraszínezi a JPanel -en.
	 * @see #checkRight()
	 * @see tetris.TetrisBlock#moveRight()
	 */
	public void moveBlockRight() {
		if(block == null)
			return;
		if(!checkRight())
			return;
		block.moveRight();
		repaint();
	}
	
	/**
	 * Ha lehet, akkor balra mozgatja az alakzatot és újraszínezi a JPanel -en.
	 * @see #checkLeft()
	 * @see tetris.TetrisBlock#moveLeft()
	 */
	public void moveBlockLeft() {
		if(block == null)
			return;
		if(!checkLeft())
			return;
		block.moveLeft();
		repaint();
	}
	
	/**
	 * Ha lehet, akkor 1 -el lefelé lépteti az alakzatot.
	 * @see #checkBottom()
	 * @see tetris.TetrisBlock#moveDown()
	 */
	public void dropBlock() {
		if(block == null)
			return;
		while(checkBottom()) {
			block.moveDown();
		}
		repaint();
	}
	
	
	/**
	 * 90 fokkal elforgatja az alakzatot.
	 * @see	tetris.TetrisBlock#rotate()
	 */
	public void rotateBlock() {
		if(block == null)
			return;
		block.rotate();
		if(block.getRightEdge() > gridColumns) {
			block.setX(gridColumns - block.getWidth());
		}
		if(block.getLeftEdge() < 0) {
			block.setX(0);
		}
		repaint();
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
	 * Teljesen töröl egy sort.
	 * @param r	A törölni kívánt sor indexe.
	 */
	public void clearLine(int r) {
		for(int i = 0; i < gridColumns; ++i) {
			background[r][i] = null;
		}
	}
	
	/**
	 * Az adott alakzatot betölti a pályát tartalmazó tömbbe.
	 */
	public void moveBlockToBackground() {
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
	 * @see javax.swing.JComponent#paintComponenet(Graphics g)
	 * @see #drawBackground(Graphics g)
	 * @see #drawBlock(Graphics g)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawBackground(g);
		drawBlock(g);
	}

	/**
	 * Berajzol egy alakzatot.
	 * @param g
	 * @see #drawSqare(Graphics g, Color c, int x, int y)
	 */
	private void drawBlock(Graphics g) {
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
	
	public TetrisBlock getBlock() {
		return block;
	}
}
