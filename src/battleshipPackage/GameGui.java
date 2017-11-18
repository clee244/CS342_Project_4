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
	private JButton[][] gameBoard;    // array of buttons representing our 10x10 grid
	private JButton[][] attackBoard;  // array of buttons representing opponent's 10x10 grid
	private JButton[] ships;          // array of buttons representing our fleet of ships
	private int numShips;             // counter of how many of our ships remain, begins at 0
	private int numShotsFired;        // counter of total shots we fired at opponent
	private int numHits;              // counter of total hits we landed on opponent
	private double accuracy;          // percentage of hits per shot
	private ImageIcon water;          // image of water, default at start of game
	private ImageIcon ship;           // image of ship
	private JMenuBar menuBar;         // menu bar of the gui
	private JMenu fileMenu, helpMenu; // drop down lists in the menu bar
	private JLabel help;              // status message at the middle of the gui, used to inform the player
	private JLabel stats;             // statistics window
	private Container container;      // the frame pane of the gui, add components to this
	private Network network;          // the network that will connect the players together

	// Constructor
	public GameGui(String name) throws IOException{
		// takes string and makes it the title of the window
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Register the window layout
		container = getContentPane();
		container.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Initialize the images
		water = new ImageIcon(getClass().getResource("/images/batt100.gif"));
		ship  = new ImageIcon(getClass().getResource("/images/batt2.gif"));

		// Initialize local instance of the game
		gameBoard   = new JButton[10][10];
		attackBoard = new JButton[10][10];
		
		// Initialize our fleet of ships
		ships = new JButton[17];
		numShips = 0;
		
		// Initialize our ocean
		int i, j;
		for(i = 0; i < 10; ++i) {
			for (j = 0; j < 10; ++j) {
				gameBoard[i][j] = new JButton(water);
				gameBoard[i][j].setPreferredSize(new Dimension(18, 18));

				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j;

				container.add(gameBoard[i][j], c);
				
				JButton temp = gameBoard[i][j];
				
				gameBoard[i][j].addActionListener(e -> placeShip(temp));
			}
		}

		// Add the status window to the container
		help = new JLabel("Welcome to BattleShips.\n");
		gbc.gridy = 11;
		gbc.gridwidth = 11;
		container.add(help, gbc);

		// Initialize opponent's ocean
		for(i = 0; i < 10; ++i) {
			for (j = 0; j < 10; ++j) {
				attackBoard[i][j] = new JButton(water);
				attackBoard[i][j].setPreferredSize(new Dimension(18, 18));

				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = j+12;

				container.add(attackBoard[i][j], c);
			}
		}
		
		// Add the statistics window to bottom of container
		numShotsFired = 0;
		numHits       = 0;
		accuracy      = 0.0;
		stats = new JLabel();
		updateStats();
		gbc.gridy = 22;
		container.add(stats, gbc);

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

		connectServerItem.addActionListener(e -> {
		    network.doManageServer();
        });

		connectClientItem.addActionListener(e -> {
                    if (network.getServerSocket().isClosed()) {
                        System.out.println("Server not started, become Server instead.");
                        network.doManageServer();
                    } else {

                        String host = JOptionPane.showInputDialog(this,
                                "HostName:");
                        String port = JOptionPane.showInputDialog(this,
                                "PortNumber:");
                        int parsedPort = Integer.parseInt(port);

                        network.doManageConnection(host, parsedPort);
                    }
                }
		);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		fileMenu.add(exitItem);
		exitItem.addActionListener(e -> {
		    network.doServerClose();
            System.exit(-1);
        });

		// Help menu
		helpMenu = new JMenu("Help");

		JMenuItem howToPlayBattleshipItem = new JMenuItem("How to play Battleships");
		helpMenu.add(howToPlayBattleshipItem);
		howToPlayBattleshipItem.addActionListener(e -> {
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

		JMenuItem howToPlayItem = new JMenuItem("How to use the UI");
		helpMenu.add(howToPlayItem);
		howToPlayItem.addActionListener(e -> {
					JOptionPane.showMessageDialog(this,
							"The 10x10 grid on top is your fleet's ocean.\n"
									+ "The grid on the bottom is your opponent's.\n"
									+ "Begin by selecting cells on your grid to place\n"
									+ "your battleships.\n"
									+ "The status message in the middle will tell you\n"
									+ "which player's turn it is and the events of\n"
									+ "the game.",
							"How to use the UI", JOptionPane.DEFAULT_OPTION);
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


		// Set window size and make it visible
		setSize(200, 450);
		setResizable(false);
		setVisible(true);
	}

	// Places a ship on our ocean if we have any ships left to deploy, and if the cell is empty
	private void placeShip(JButton cell) {
		if (numShips < 17) {
			if (!contains(ships, cell)) {
				// change the button's image to a ship
				cell.setIcon(ship);
				
				// display the changes on status message
				GridBagLayout layout = (GridBagLayout) container.getLayout();
				GridBagConstraints gbc = layout.getConstraints(cell);
				int x = gbc.gridx;
				int y = gbc.gridy;
				help.setText("Ship placed at (" + x + "," + y + ").");
				
				// add the button to our ship array
				ships[numShips] = cell;
				numShips++;
			}
		}
	}
	
	// Returns true if button is in the array, false if not
	private boolean contains(JButton[] array, JButton toFind) {
		int i;
		for (i = 0; i < array.length; ++i) {
			if (array[i] == toFind) {
				return true;
			}
		}
		
		return false;
	}
	
	// Updates the statistics window with the latest information
	private void updateStats() {
		accuracy = (double)numHits/numShotsFired;
		stats.setText("Shots: " + numShotsFired + ". Hits: " + numHits + ". Accuracy: " + accuracy);
	}
	
	public static void main(String[] args) throws IOException{

		// create a new gui
		GameGui battleship = new GameGui("Battleships");
	}

}
