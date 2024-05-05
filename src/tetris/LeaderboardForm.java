package tetris;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.DropMode;

public class LeaderboardForm extends JFrame {

	private JPanel contentPane;
	private JTable leaderboard;
	private DefaultTableModel tableModel;
	private String fileName;
	private TableRowSorter<TableModel> sorter;
	
		
	/**
	 * A LeaderboardForm main függvénye -
	 * Létrehozza és megjelníti a JFrame -et.			
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LeaderboardForm frame = new LeaderboardForm("leaderboard");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Létrehoz egy DefaultTableModel-t, ami majd a JTable konstruktorának paramétere lesz.
	 * De-szerializálás.
	 */
	private void initTableData() {
		Vector columnIdentifiers = new Vector();
		columnIdentifiers.add("Name");
		columnIdentifiers.add("Score");
		
		tableModel = new DefaultTableModel() {
			@Override
			public Class getColumnClass(int col) {
		        if (col == 1)
		            return Integer.class;
		        else return String.class;
		    }
		};
		tableModel.setColumnCount(2);
		
		try {
		FileInputStream is = new FileInputStream(fileName);
		ObjectInputStream os = new ObjectInputStream(is);
		
		tableModel.setDataVector((Vector) os.readObject() ,columnIdentifiers);
		
		is.close();
		os.close();
		}
		catch(Exception e) {
		}
	}
	
	/**
	 * A dicsőséglista rendezésének logikája.
	 */
	private void initTableSorter() {
		if(leaderboard.getRowCount() == 0)
			return;
		sorter = new TableRowSorter<>(tableModel);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING));
		
		sorter.setSortKeys(keys);
	}
	
	/**
	 * Szerializálás
	 */
	private void saveLeaderboard() {
		try {
			FileOutputStream fs = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeObject(tableModel.getDataVector());
			
			fs.close();
			os.close();
		}
		catch(Exception e) {
			}
	}
	
	/**
	 * Felvesz egy rekordot új sorba, majd rendezi a JTable -t.
	 * @param playerName	A játékos neve
	 * @param score			A játékos által elért pontszám
	 */
	public void addPlayer(String playerName, int score) {
		this.setVisible(true);
		tableModel.addRow(new Object[] {playerName, score});
		if(sorter == null)
			initTableSorter();
		sorter.sort();
		saveLeaderboard();
	}

	/**
	 * A LeaderboardForm osztály konstruktora -
	 * A WindowBuilder által létrehozott elemeket inicializálja.
	 */
	public LeaderboardForm(String initFileName) {
		fileName = initFileName;
		JFrame tempFrame = this;
		
		setResizable(false);
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton menuButton = new JButton("main menu");
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tetris.showMenu();
				tempFrame.setVisible(false);
			}
		});
		menuButton.setBounds(311, 233, 117, 25);
		contentPane.add(menuButton);
		
		initTableData();
		
		leaderboard = new JTable(tableModel);
		leaderboard.setBounds(12, 12, 416, 209);
		contentPane.add(leaderboard);
		
		initTableSorter();
	}
	
	public JTable getTable() {
		return leaderboard;
	}
}
