package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import tetris.GameArea;
import tetris.LeaderboardForm;
import tetris.TetrisBlock;
import tetrisBlocks.Hero;

class TetrisTests{
	
	/*
	 * GameArea.spawnBlock
	 * TetrisBlock.spawn
	 */
	@Test
	public void spawnBlockTest() {
		int x = 10;
		JPanel testPanel = new JPanel();
		testPanel.setBounds(144, 25, 301, 601);
		GameArea testArea = new GameArea(testPanel, x);
		testArea.spawnBlock(4);
		TetrisBlock testBlock = new Hero();
		testBlock.spawn(x);
		assertEquals("Good block spawned", testArea.getBlock().getShape(), testBlock.getShape());
		assertEquals("Spawned at the right X", testArea.getBlock().getX(), testBlock.getX());
		assertEquals("Spawned at the right Y", testArea.getBlock().getY(), testBlock.getY());
	}
	
	/*
	 * GameArea.isBlockOutOfBounds
	 */
	@Test
	public void isBlockOutOfBoundsTest() {
		int x = 10;
		JPanel testPanel = new JPanel();
		testPanel.setBounds(144, 25, 301, 601);
		GameArea testArea = new GameArea(testPanel, x);
		testArea.spawnBlock(4);
		testArea.getBlock().setY(-1);
		assertTrue("Block is out of bounds", testArea.isBlockOutOfBounds());
		assertNull("Block is now null", testArea.getBlock());
	}
	
	/*
	 * GameArea.moveBlockDown
	 * GameArea.checkBottom
	 * TetrisBlock.moveDown
	 */
	@Test
	public void checkBottomTest() {
		int x = 10;
		JPanel testPanel = new JPanel();
		testPanel.setBounds(144, 25, 301, 601);
		GameArea testArea = new GameArea(testPanel, x);
		testArea.spawnBlock(4);
		testArea.initBackgroundArray();
		int currentY = testArea.getBlock().getY();
		assertTrue("At spawn", testArea.moveBlockDown());
		assertEquals("After moving down", testArea.getBlock().getY(), currentY + 1);
		testArea.getBlock().setY(20 - testArea.getBlock().getHeight());
		assertFalse("At the bottom", testArea.moveBlockDown());
	}

	/*
	 * TetrisBlock.rotation()
	 */
	@Test
	public void rotationTest() {
		TetrisBlock block1 = new Hero();
		TetrisBlock block2 = new Hero();
		
		assertEquals("Before rotation", block1.getShape(), block2.getShape());
		block1.rotate();
		assertNotEquals("After rotation", block1.getShape(), block2.getShape());
	}
	
	/*
	 * LeaderboardForm.initTableData
	 * LeaderboardForm.addPlayer
	 * LeaderboardForm.saveLeaderboard
	 * LeaderboardForm.initTableSorter
	 */
	@Test
	public void leaderboardTest() {
		String fn = "nothing";
		LeaderboardForm lfTest = new LeaderboardForm(fn);
		assertEquals(lfTest.getTable().getRowCount(), 0);
		lfTest.addPlayer("test player", 69);
		assertEquals(lfTest.getTable().getRowCount(), 1);
		assertEquals(lfTest.getTable().getValueAt(0, 0), "test player");
		assertEquals(lfTest.getTable().getValueAt(0, 1), 69);
		LeaderboardForm newTest = new LeaderboardForm(fn);
		assertEquals(newTest.getTable().getRowCount(), 1);
		assertEquals(newTest.getTable().getValueAt(0, 0), "test player");
		assertEquals(newTest.getTable().getValueAt(0, 1), 69);
		newTest.addPlayer("world record", 999999);
		assertEquals("this", newTest.getTable().getValueAt(0, 0), "world record");
		assertEquals("that", newTest.getTable().getValueAt(0, 1), 999999);
		
		File f = new File(fn);
		f.delete();
	}
 }
