
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
	private ImageIcon water;          // image of water, default at start of game
	private ImageIcon ship;           // image of ship
	private JMenuBar menuBar;         // menu bar of the gui
	private JMenu fileMenu, helpMenu; // drop down lists in the menu bar
	private JLabel help;              // status message at the middle of the gui, used to inform the player
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
		water = new ImageIcon(getClass().getResource("images/batt100.gif"));
		ship  = new ImageIcon(getClass().getResource("images/batt2.gif"));

		// Initialize local instance of the game
		gameBoard   = new JButton[10][10];
		attackBoard = new JButton[10][10];

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

	public static void main(String[] args) throws IOException{
		// counters for statistics
		int numShotsFired = 0;
		double hitsPerMiss = 0.0;

		// create a new gui
		GameGui battleship = new GameGui("Battleships");
	}

}
