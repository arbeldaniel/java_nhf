package tetrisBlocks;

import tetris.TetrisBlock;
import java.awt.Color;

public class ClevelandZ extends TetrisBlock{
	public ClevelandZ() {
		super(new int[][] {{1, 1, 0}, {0, 1, 1}}, Color.red);
	}
}
