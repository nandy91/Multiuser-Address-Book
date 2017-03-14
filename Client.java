/* 
 * Client.java
 */

import java.io.*;
import java.net.*;

public class Client 
{
    public static final int SERVER_PORT = 7652;

    public static void main(String[] args) 
    {
	Socket clientSocket = null;  
	PrintStream os = null;
	String userInput = null;
	BufferedReader stdInput = null;

	//Check the number of command line parameters
	if (args.length < 1)
	{
	    System.out.println("Usage: client <Server IP Address>");
	    System.exit(1);
	}

	// Try to open a socket on SERVER_PORT
	// Try to open input and output streams
	try 
	{
	    clientSocket = new Socket(args[0], SERVER_PORT);
	    os = new PrintStream(clientSocket.getOutputStream());
	    stdInput = new BufferedReader(new InputStreamReader(System.in));
	} 
	catch (UnknownHostException e) 
	{
	    System.err.println("Don't know about host: hostname");
	} 
	catch (IOException e) 
	{
	    System.err.println("Couldn't get I/O for the connection to: hostname");
	}

	// If everything has been initialized then we want to write some data
	// to the socket we have opened a connection to on port 25

	if (clientSocket != null && os != null) 
	{
	    try 
	    {
		//Start a child thread to handle the server's messages
		SThread sThread = new SThread(clientSocket);
		sThread.start();

		//handle the user input
		while ((userInput = stdInput.readLine())!= null)
		{
		   if (userInput.equals("QUIT")) {
                        
                        os.println(userInput);
                        os.close();
                        clientSocket.close();
                        System.exit(0);
                    } else {
                        os.println(userInput);//SEND DATA TO SERVER
		}

		
	    } 
            }
	    catch (IOException e) 
	    {
	    }
	}
    }           
}

/*
 * SThread Class, which handle the server's messages
 */
class SThread extends Thread 
{
    Socket socket;
    BufferedReader is = null;
    String serverInput = null;

    /**
     * Constructor
     */
    SThread(Socket socket)
    {
        
	this.socket = socket;
    }

    /*
     * Child thread of execution, handle the server's messages
     */
    public void run()
    {
	try 
	{
	    is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(is!=null)
            {
	
                while (true) {// Recieve Data from Server till an END string is provided
                    serverInput = is.readLine();
                    if (serverInput.equalsIgnoreCase("END")) {
                        break;
                    }
                    if(serverInput.equalsIgnoreCase("SHUT")){
                     
                        System.exit(0);
                    }
                    System.out.println("S:" + serverInput);//Print data from server on console
                    
                }
            } 
        }
	catch (Exception e) 
	{
            System.exit(0);    
	}
    }           
}
