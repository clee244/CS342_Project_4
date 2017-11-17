import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Network {
    private ServerSocket serverSocket = null;
    private Socket echoSocket = null;
    private Socket clientSocket = null;
    private Point2d serverInput = null;
    private Point2d clientInput = null;
    private Point2d serverOutput = null;
    private Point2d clientOutput = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private String args[] = null;

    //Function to run the server side of the game
    public void startServer() throws IOException{
        //Open Server at port#
        try {
            serverSocket = new ServerSocket(7777);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 7777.");
            System.exit(1);
        }

        //Accept Client(hopefully)
        while(clientSocket == null){
            try {
                System.out.println("Waiting for Client");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }

        //receive input from client and send output to client
        while(serverSocket.isClosed() == false){
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            try {
                serverInput = (Point2d) in.readObject();
            }
            catch (Exception ex)
            {
                System.out.println (ex.getMessage());
            }
            out.writeObject(serverInput);
            out.flush();
        }

        //out.close();
        //in.close();
        //clientSocket.close();
        //serverSocket.close();
    }

    //Function to run the client side of the game, server needs to be opened for connection first
    public void startClient() throws IOException{
        Scanner scanner = new Scanner(System.in);

        String hostName = scanner.nextLine();
        int portNumber = scanner.nextInt();

        //Open client on hostname and port# (take input)
        try {
            // echoSocket = new Socket("taranis", 7);
            echoSocket = new Socket(hostName, portNumber);

            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: taranis.");
            System.exit(1);
        }

        //send output to server and receive input from server
        while(!echoSocket.isClosed()){
            System.out.println ("Sending point: " + clientInput + " to Server");
            out.writeObject(clientInput);
            out.flush();
            System.out.println ("Send point, waiting for return value");

            try {
                serverOutput = (Point2d) in.readObject();
            }
            catch (Exception ex)
            {
                System.out.println (ex.getMessage());
            }

            System.out.println("Got point: " + serverOutput + " from Server");
        }

        //out.close();
        //in.close();
        //echoSocket.close();
    }

    public Point2d getClientInput() {
        return clientInput;
    }

    public Point2d getClientOutput() {
        return clientOutput;
    }

    public Point2d getServerInput() {
        return serverInput;
    }

    public Point2d getServerOutput() {
        return serverOutput;
    }

    public static void main(String[] args) throws IOException{
        Network network = new Network();

        network.startServer();
        network.startClient();
    }
}
