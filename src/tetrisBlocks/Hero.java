package tetrisBlocks;

import tetris.TetrisBlock;
import java.awt.Color;

public class Hero extends TetrisBlock{
	public Hero() {
		super(new int[][] {{1}, {1}, {1}, {1}}, Color.cyan);
	}

	@Override
	public void rotate() {
		super.rotate();
		
		if(this.getWidth() == 1) {
			this.setX(this.getX() + 1);
			this.setY(this.getY() - 1);
		}
		else {
			this.setX(this.getX() - 1);
			this.setY(this.getY() + 1);
		}
	}


}
