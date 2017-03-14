/*
 * Server.java
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiThreadServer {

    public static final int SERVER_PORT = 7652;
    static Map<Integer, String> map = new LinkedHashMap<>();
    public static void main(String args[]) 
    {
	ServerSocket myServerice = null;
	Socket serviceSocket = null;
       try {
            File file = new File("AddressBook.txt");//Create instance of a file and check if it exists
            if (!file.exists()) {
                file.createNewFile();
            } else {

                try {

            BufferedReader in = new BufferedReader(new FileReader(file));

            String line = "";
            while ((line = in.readLine()) != null) {
                String first = line.substring(0, line.indexOf(' '));

                Integer id = Integer.valueOf(first);

                String value = line.substring(line.indexOf(' ') + 1);
                map.put(id, value);
            }

            in.close();
            //System.out.println(map.toString());
        } catch (Exception e) {
            System.out.println(e);

        }
            }
        } catch (Exception e) {
            System.out.println("caught exception");
        }
        
	// Try to open a server socket 
	try {
           
	    myServerice = new ServerSocket(SERVER_PORT);
	}
	catch (IOException e) {
	    System.out.println(e);
	}   
 
                
	// Create a socket object from the ServerSocket to listen and accept connections.
	while (true)
	{
	    try 
	    {
		// Received a connection
		serviceSocket = myServerice.accept();
		System.out.println("MultiThreadServer: new connection from " + serviceSocket.getInetAddress());

		// Create and start the client handler thread
		ChildThread cThread = new ChildThread(serviceSocket,map);
		cThread.start();
                
	    }   
	    catch (IOException e) 
	    {
		System.out.println(e);
	    }
	}
    }

}