package tetris;
import java.awt.Color;

public class TetrisBlock {
	private int[][] shape;
	private Color color;
	private int x, y;
	private int[][][] shapes;
	private int currentRotation;
	private boolean isDown;
	
	/**
	 * A TetrisBlock osztály konstruktora -
	 * Eltárol egy adott alakzatot és elkészíti annak alakzatnak a változatait.
	 * @param initShape	Eredeti alakzat
	 * @param initColor	Alakzat színe
	 */
	public TetrisBlock(int[][] initShape, Color initColor) {
		this.shape = initShape;
		this.color = initColor;
		this.isDown = false;
		
		initShapes();
	}
	
	
	public void newBlock(TetrisBlock block, int initX) {
		shapes = block.getShapes();
		color = block.getColor();
		isDown = false;
		currentRotation = 0;
		shape = shapes[currentRotation];
		x = initX;
		y = 0 - this.getHeight();
	}
	
	/**
	 * Elkészíti egy alakzat 4 forgatását.
	 */
	private void initShapes() {
		shapes = new int[4][][];
		shapes[0] = shape;
		
		for(int i = 1; i < 4; ++i) {
			int c = shape.length;
			int r = shape[0].length;
			
			shapes[i] = new int[r][c];
			
			for(int y = 0; y < r; ++y) {
				for(int x = 0; x < c; ++x) {
					shapes[i][y][x] = shape[c-x-1][y];
				}
			}
			
			shape = shapes[i];
		}
	}
	
	/**
	 * Előkészíti az alakzatot rajzolásra.
	 * @param gridWidth	A pályán lévő blokkok mérete.
	 */
	public void spawn(int gridWidth) {
		currentRotation = 0;
		shape = shapes[currentRotation];
		
		this.y = 0 - this.getHeight();
		this.x = (gridWidth - this.getWidth()) / 2;
	}
	
	/**
	 * Getter függvény az alakzatra.
	 * @return	Tárolt alakzat
	 */
	public int[][] getShape() {
		return this.shape;
	}
	
	/**
	 * Getter függvény az alakzat színére.
	 * @return	Az alakzat színe
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Getter függvény az alakzat magasságára blokkban.
	 * @return	Az alakzat magassága blokkban
	 */
	public int getHeight() {
		return this.shape.length;
	}
	
	/**
	 * Getter függvény az alakzat szélességére blokkban.
	 * @return	Az alakzat szélessége blokkban
	 */
	public int getWidth() {
		return this.shape[0].length;
	}
	
	/**
	 * Getter függvény az alakzat x koordinátájára.
	 * @return	Az alakzat x koordinátája
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Setter függvény az alakzat x koordinátájára.
	 * @param initX	Az x koordináta új értéke
	 */
	public void setX(int initX) {
		x = initX;
	}
	
	/**
	 * Getter függvény az alakzat y koordinátájára.
	 * @return	Az alakzat y koordinátája
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Setter függvény az alakzat y koordinátájára.
	 * @param initY	Az y koordináta új értéke
	 */
	public void setY(int initY) {
		y = initY;
	}
	
	/**
	 * 1 -el lejjebb tolja az alakaztot.
	 */
	public void moveDown() {
		++y;
	}
	
	/**
	 * 1 -el balra tolja az alakzatot.
	 */
	public void moveLeft() {
		--x;
	}
	
	/**
	 * 1 -el jobbra tolja az alakzatot.
	 */
	public void moveRight() {
		++x;
	}
	
	/**
	 * Visszaadja, hogy az alakzat alja hol helyezkedik el.
	 * @return	Az alakzat aljának y koordinátája blokkban
	 */
	public int getBottomEdge() {
		return y + getHeight();
	}
	
	/**
	 * Visszaadja, hogy az alakzat bel oldala hol helyezkedik el.
	 * @return	Az alakzat bal oldalának x koordinátája blokkban
	 */
	public int getLeftEdge() {
		return x;
	}

	/**
	 * Visszaadja, hogy az alakzat jobb oldala hol helyezkedik el.
	 * @return	Az alakzat jobb oldalának x koordinátája blokkban
	 */
	public int getRightEdge() {
		return x + getWidth();
	}
	
	/**
	 * Előkészíti forgatásra az alakzatot.
	 */
	public void rotate() {
		if(++currentRotation > 3) {
			currentRotation = 0;
		} 
		shape = shapes[currentRotation];
	}
	
	public int[][][] getShapes() {
		return shapes;
	}
	
	public boolean getDown() {
		return isDown;
	}
	
	public void setDown(boolean b) {
		isDown = b;
	}
}
