import java.net.*;
import java.io.*;

public class Network {
    public Network()throws IOException{
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        Socket clientSocket = null;

        try {
            System.out.println ("Waiting for Client");
            clientSocket = serverSocket.accept();
        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        ObjectOutputStream out = new ObjectOutputStream(
                clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                clientSocket.getInputStream());

        Point2d pt2 = null;

        try {
            pt2 = (Point2d) in.readObject();
        }
        catch (Exception ex)
        {
            System.out.println (ex.getMessage());
        }
        out.writeObject(pt2);
        out.flush();


        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
