package battleshipPackage;

import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// GameGui will run the GUI and handle user interactions.
// It will contain a local instance of the game itself
public class GameGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton[][] gameBoard;
	private JLabel board;
	private JMenu fileMenu, helpMenu;
	private JMenuBar menuBar;
	private JLabel help;
	private Container container;
	private Network network;
	
	// Constructor
	public GameGui(String name) throws IOException{
		// takes string and makes it the title of the window
		super(name);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// register the window layout
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		// Initialize local instance of the game
		gameBoard = new JButton[10][10];
		board = new JLabel();
		board.setLayout(new GridBagLayout());
		container.add(board, BorderLayout.SOUTH);
		
		// Initialize network
        network = new Network();

		// Initialize the menu bar items
		//
		// File menu
		fileMenu = new JMenu("File");
		
        JMenuItem connectServerItem = new JMenuItem("Connect as Server");
        JMenuItem connectClientItem = new JMenuItem("Connect as Client");
        fileMenu.add(connectServerItem);
        fileMenu.add(connectClientItem);

        connectClientItem.addActionListener(e -> {
            JOptionPane.showInputDialog(this,
                    "HostName:"
            );
            JOptionPane.showInputDialog(this,
                    "PortNumber:"
            );
        });
        
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		fileMenu.add(exitItem);
		exitItem.addActionListener(e -> System.exit(-1));

		// Help menu
		helpMenu = new JMenu("Help");
		
		JMenuItem howToPlayItem = new JMenuItem("How to play Battleships");
		helpMenu.add(howToPlayItem);
		howToPlayItem.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, 
					"At the start of the game, players choose where\n"
					+ "to place their fleet of ships. Then the game\n"
					+ "proceeds with each player taking turns declaring\n"
					+ "a cell of the board where they will fire a missile.\n"
					+ "The game is over when a player's entire fleet is\n"
					+ "destroyed.",
					"How to play Battleships", JOptionPane.DEFAULT_OPTION);
			}
		);
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic('a');
		helpMenu.add(aboutItem);
		aboutItem.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, 
					"Network Battleships written by:\n"
					+ "Tian Zhou - tzhou29\n"
					+ "Christopher Lee - clee244",
					"About", JOptionPane.DEFAULT_OPTION);
			}
		);
		
		// Initialize the menu bar and add the items to it
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		// Add the status window to the container
		help = new JLabel("Status Window:");
		help.setHorizontalAlignment(JLabel.CENTER);
		container.add(help, BorderLayout.CENTER);
	    
		// Set window size and make it visible
		setSize(400, 700);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		// counters for statistics
		int numShotsFired = 0;
		double hitsPerMiss = 0.0;
		
		// create a new gui
		GameGui battleship = new GameGui("Network Battleships by Christopher Lee and Tian Zhou");
	}

}
