import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import java.lang.String.*;

/* Network handles the Server and Client connections
 * Uses example code EchoGui.java from lecture notes 11/16/17
 */


public class Network {
    // Network Items
    private boolean running;
    private boolean connected;
    private boolean serverContinue;
    private ServerSocket serverSocket;
    private Socket commSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String machineInfo = "";
    private int portInfo = 0;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    //Close Socket when exit is clicked/Game is over
    public void doServerClose()
    {
        try {
            serverSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Could not close port: " + portInfo);
            System.exit(1);
        }
    }

    public void doManageServer()
    {
        if (running == false)
        {
            new ConnectionThread();
        }
        else
        {
            serverContinue = false;
        }
    }

    //Function to run the server side of the game
    class ConnectionThread extends Thread
    {
        public ConnectionThread ()
        {
            start();
        }

        public void run()
        {
            int portNum = 0;
            //serverContinue = true;

            try
            {
                serverSocket = new ServerSocket(portNum);
                running = true;

                // get machine's address
                InetAddress addr = InetAddress.getLocalHost();
                machineInfo = addr.getHostAddress();

                // get port number
                portInfo = serverSocket.getLocalPort();

                JOptionPane.showMessageDialog( null,
                        "Host: " + machineInfo + "\nPort: " + portInfo);

                System.out.println ("Connection Socket Created");
                serverContinue = true;
                try {
                    System.out.println ("Waiting for Connection");
                    try {
                        commSocket = serverSocket.accept();
                        out = new PrintWriter(commSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader( commSocket.getInputStream()));
                    }
                    catch (SocketTimeoutException ste) {
                                System.out.println("Timeout Occurred");
                    }
                    System.out.println("Connection Found");
                    commSocket = null;
                    running = false;
                }

                catch (IOException e)
                {
                    JOptionPane.showMessageDialog( null, "Accept failed." );
                    System.err.println("Accept failed.");
                    //System.exit(1);
                }
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog( null, "Port Number must be an integer" );
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog( null,
                        "Could not listen on port: " + portNum);
                System.err.println("Could not listen on port: " + portNum);
                //System.exit(1);
            }
        }
    }

    //Function to run the client side of the game, server needs to be opened for connection first
    public void doManageConnection(String host, int port)
    {
        machineInfo = host;
        portInfo = port;

        if (connected == false)
        {
            String machineName = null;
            int portNum = -1;
            try {
                machineName = machineInfo;
                portNum = portInfo;
                commSocket = new Socket(machineName, portNum );
                out = new PrintWriter(commSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(
                        commSocket.getInputStream()));
                connected = true;
            } catch (NumberFormatException e) {
                System.out.println("Server Port must be an integer\n");
            } catch (UnknownHostException e) {
                System.out.println("Don't know about host: " + machineName);
            } catch (IOException e) {
                System.out.println("Couldn't get I/O for "
                        + "the connection to: " + machineName);
            }

        }
        else
        {
            try
            {
                out.close();
                in.close();
                commSocket.close();
                connected = false;
            }
            catch (IOException e)
            {
            }
        }
    }
}
