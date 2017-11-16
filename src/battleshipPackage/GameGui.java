package battleshipPackage;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// GameGui will run the GUI and handle user interactions.
// It will contain a local instance of the game itself
public class GameGui extends JFrame {
	private static final long serialVersionUID = 1L;
	//private JButton[][] gameBoard;
	//private JButton[] sideButton;
	private JMenu fileMenu, helpMenu;
	private JMenuBar menuBar;
	//private JPanel board, sideMenu;
	private JLabel help;
	private Container container;
	// TODO: needs private instance of the game variable here
	
	// Constructor
	public GameGui(String name) {
		// takes string and makes it the title of the window
		super(name);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// register the window layout
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		// TODO: initialize local instance of the game (and network)
		//
		//
		
		// Initialize the menu bar items
		//
		// File menu
		fileMenu = new JMenu("File");
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		fileMenu.add(exitItem);
		exitItem.addActionListener(e -> System.exit(-1));
		
		// Help menu
		helpMenu = new JMenu("Help");
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic('a');
		helpMenu.add(aboutItem);
		aboutItem.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Network Battleships written by:\nTian Zhou - tzhou29\nChristopher Lee - clee244",
					"About", JOptionPane.DEFAULT_OPTION);
			});
		
		// Initialize the menu bar and add the items to it
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		// Add the game board to the container
		help = new JLabel("this is help testing");
		help.setHorizontalAlignment(JLabel.CENTER);
		container.add(help, BorderLayout.CENTER);
		//container.add(board, BorderLayout.CENTER);
		//container.add(sideMenu, BorderLayout.EAST);
		
		
		// Set window size and make it visible
		setSize(500, 1000);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		GameGui battleship = new GameGui("Network Battleships by Christopher Lee and Tian Zhou");

	}

}
