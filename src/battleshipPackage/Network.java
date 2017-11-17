package battleshipPackage;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.String.*;

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
                    {
                        System.out.println ("Waiting for Connection");

                        try {
                            commSocket = serverSocket.accept();
                            System.out.println("Connection Found");
                            out = new PrintWriter(commSocket.getOutputStream(),
                                    true);
                            in = new BufferedReader(
                                    new InputStreamReader( commSocket.getInputStream()));
                        }
                        catch (SocketTimeoutException ste)
                        {
                            System.out.println ("Timeout Occurred");
                        }
                    }
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
            finally
            {
                try {
                    serverSocket.close();
                }
                catch (IOException e)
                {
                    System.err.println("Could not close port: " + portNum);
                    System.exit(1);
                }
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
            } catch (UnknownHostException e) {
            } catch (IOException e) {
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
