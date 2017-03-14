/* 
 * ChildThread.java
 */

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ChildThread extends Thread {

    static Vector<ChildThread> handlers = new Vector<ChildThread>(20);
    private Socket serviceSocket;
    private Socket socket1;
    private BufferedReader in;
    private PrintWriter out;
     InetAddress userIP;
   
    String current_user="ANONYMOUS";
     //PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("AddressBook.txt")));
    static Map<Integer, String> map = new LinkedHashMap<>();
     
    static Map<String, String> login1 = new HashMap<>();
    boolean login_state = false;
    
    
    public ChildThread(Socket socket,Map<Integer,String> map) throws IOException {
        this.map=map;
        this.serviceSocket = serviceSocket;
        this.socket1=socket;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()));
       
    }

    public void run() {
       
        String line;
        //INITIALIZE USERS
        login1.put("root", "root01");
        login1.put("john", "john01");
        login1.put("david", "david01");
        login1.put("mary", "mary01");
        
        synchronized (handlers) {
            // add the new client in Vector class
            handlers.addElement(this); 
        }

        try {
            while ((line = in.readLine()) != null) {
                char c;
                String content, cmd = " ";
                int recordid = 0;
                System.out.println("c:" + line);
                //Check the user Input                  
                    c = line.charAt(0);
                //take option and choose appropriate choice
                    String input[] = line.split("\\s+");
                switch (c) {
                    case 'L':
 /////////////////LOGIN
                        if (input[0].equalsIgnoreCase("LOGIN")) {
                            if(!this.login_state){
                            String username = input[1];
                            if (login1.containsKey(username)) {
                                String pwd = (String) login1.get(username);//pwd for username
                                String user_pwd = input[2];
                                if (pwd.equals(user_pwd)) {
                                    login_state = true;
                                    current_user=username;
                                    out.println("200 OK");
                                    out.println("Success Logged in!! Welcome: "+username);
                                    out.println("END");
                                    out.flush();
                                }

                            } else {
                                out.println("401 Invalid username or password ");
                                out.println("END");
                                out.flush();
                            }
                        } 
                            else{out.println("ALREADY LOGGED IN");
                            out.println("END");
                            out.flush();}}
//////////////////////////////////LOGOUT
                        else if (input[0].equalsIgnoreCase("LOGOUT")) {
                            login_state = false;
                            current_user="ANONYMOUS";
                           out.println("200 OK");
                            out.println("END");
                            out.flush();
                        }
////////////////////////////////////LIST COMMAND
                        else if (input[0].equalsIgnoreCase("LIST")) {
                            cmd = line;

                            if (cmd.equals("LIST")) {
                                System.out.println("iNSIDE LIST");
                                if (map.isEmpty()) {
                                    out.println("NO CONTENTS TO SHOW");
                                    out.println("END");
                                    out.flush();
                                } else {
                                    Iterator<Integer> keySetIterator = map.keySet().iterator();
                                    int key;
                                    while (keySetIterator.hasNext()) {
                                        key = keySetIterator.next();
                                        out.println(key + " " + map.get(key));
                                        out.flush();
                                    }
                                    out.println("END");
                                    out.flush();
                                }
                                break;
                            } else {
                                out.println("INVALID COMMAND");
                                out.println("END");
                                out.flush();
                                break;
                            }

                        }//look 1.firstname 2.lastname 3.phone number
 //////////////////////////////////////////////////////////////////////look                       
                        else if (input[0].equalsIgnoreCase("LOOK") && line.length() >=7 && input[1].length()==1) {
                            try {
                                int search_option = Integer.parseInt(input[1]); // get option number

                                switch (search_option) {

                                    case 1://SEARCH WITH FIRSTNAME
                                        Iterator<Integer> lookIterator1 = map.keySet().iterator();
                                        int key1;
                                        int found1 = 0;
                                        while (lookIterator1.hasNext()) {
                                            key1 = lookIterator1.next();
                                            String val = map.get(key1);
                                            String record[] = val.split("\\s+");
                                            if (input[2].equals(record[0])) {
                                                found1 = 1;
                                                out.println("200 OK RECORD FOUND");
                                                out.println(key1 + " " + map.get(key1));
                                                
                                                out.println("END");
                                                out.flush();
                                                
                                            }
                                        }
                                        if (found1 == 0) {
                                            out.println("401 ERROR RECORD NOT FOUND");
                                            out.println("END");
                                            out.flush();
                                        }
                                        break;
                                    case 2://SEARCH WITH LASTNAME
                                        Iterator<Integer> lookIterator2 = map.keySet().iterator();
                                        int key2;
                                        int found2=0;
                                        while (lookIterator2.hasNext()) {
                                            key2 = lookIterator2.next();
                                            String val = map.get(key2);
                                            String record[] = val.split("\\s+");
                                            if (input[2].equals(record[1])) {
                                                out.println("200 OK RECORD FOUND");
                                                out.println(key2 + " " + map.get(key2));
                                                
                                                found2=1;
                                                out.println("END");
                                                out.flush();
                                            }
                                        }
                                        if(found2==0){
                                        out.println("SORRY RECORD NOT FOUND");
                                        out.println("END");
                                        out.flush();
                                        }
                                        break;
                                    case 3://SEARCH WITH PHONE NUMBER
                                        Iterator<Integer> lookIterator3 = map.keySet().iterator();
                                        int key3;
                                        int found3=0;
                                        while (lookIterator3.hasNext()) {
                                            key3 = lookIterator3.next();
                                            String val = map.get(key3);
                                            String record[] = val.split("\\s+");
                                            if (input[2].equals(record[2])) {
                                                found3=1;
                                                out.println("200 OK RECORD FOUND");
                                                out.println(key3 + " " + map.get(key3));
                                                
                                                out.println("END");
                                                  out.flush();                                          }
                                        }
                                        if(found3==0){
                                        out.println("RECORD NOT FOUND");
                                        out.println("END");
                                        out.flush();
                                        }
                                        break;

                                    default:
                                        out.println("Invallid command please choose 1 to search with first 2 with last and 3 with phone number");
                                        out.println("END");
                                        out.flush();
                                        break;
                                }

                            } catch (Exception e) {
                                out.println("TRY ENTERING CORRECT COMMAND");
                                out.println("END");
                                out.flush();
                            }

                        }
                        else{
                        out.println("INVALID INPUT");
                        out.println("END");
                        out.flush();}
                        break;
///////////////////////////////////////////////////////////////////////////////////////////////
                    case 'A': // ADD COMMAND 
                        if (input[0].equalsIgnoreCase("ADD")&& line.length() >=10){
                        if (login_state) {//CHECK IF LOGGED IN
                            cmd = line.substring(0, line.indexOf(' '));
                            content = line.substring(line.indexOf(' ') + 1);//The data after the command is taken
                            String validate[] = line.split("\\s+");
                            if (cmd.equals("ADD") && validate[1].length() >= 1 && validate[2].length() >= 1 && validate[3].length() >= 10) {
                                int recid;//Record id returned after adding
                                String recstring;
                                recid = addtolist(content);//add content to addressbook
                                out.println("200 OK");
                                recstring = "The new Record ID is " + recid;
                                out.println(recstring);
                                out.println("END");
                                out.flush();
                                System.out.println(" ADDED record");
                                
                            } else {
                                out.println("301 message format error ");
                                
                                out.println("END");
                                out.flush();
                            }
                        } else {
                            out.println("Please login to execute this command");
                            out.println("END");
                            out.flush();
                            
                        }
                        }
                        else{out.println("301 INVALID COMMAND FORMAT");
                        out.println("END");
                        out.flush();}
                    break;
 //////////////////////////////////////////////////////////////////////////////////////////////////////////                       
                    case 'D'://DELETE COMMAND
                       if (input[0].equalsIgnoreCase("DELETE")){

                        if (login_state){
                        cmd = line.substring(0, line.indexOf(' '));
                        content = line.substring(line.indexOf(' ') + 1);//The data after the command is taken
                        if (cmd.equals("DELETE") && content.length() >= 4) {
                            System.out.println("DELETING THE RECORD");
                            try {

                                recordid = Integer.parseInt(content);//Getting the record id

                            } catch (NumberFormatException e) {
                                System.out.println("Number format exception");
                            }
                            String result = " ";
                            result = deletelist(recordid);
                            out.println(result);
                            out.println("END");
                            out.flush();
                            break;
                        } else {
                            out.println("WRONG COMMAND OR RECORDID");
                            out.flush();
                            break;
                        }}
                        else{out.println("Please login to execute this command");
                            out.println("END");
                            out.flush();}
                       }
                       else{out.println("INVALID COMMAND");
                        out.println("END");
                       out.println();}
                    break;
//////////////////////////////////////////////////////////////////////////////////////
                    case 'S': // SHUT DOWN
                        if (input[0].equalsIgnoreCase("SHUTDOWN")){
                        if(current_user.equals("root"))
                    {
                        savetofile();
                        try{
                        for (int i = 0; i < handlers.size(); i++) {
                            synchronized (handlers) {
                                ChildThread handler
                                        = (ChildThread) handlers.elementAt(i);
                               
                                    handler.out.println("Server shutdown requested ... closing your program!");
                                    handler.out.println("SHUT");
                                    out.println("END");
                                    handler.out.flush();
                                    handler.in.close();
                                    handler.out.close();
                                    handler.socket1.close();
                            }
                        }
                         System.exit(0);
                        }
                        catch(Exception e){ System.exit(0);}
                              }
                else
                        {
                          out.println("402 User not allowed to execute this command..Please login as root")  ;
                          out.println("END");
                          out.flush();
                        }}
                        else{out.println("INVALID COMMAND");
                        out.println("END");
                        out.flush();}
                        break;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    case 'Q': //QUIT

                        savetofile();
                        out.println("200 OK");
                        out.println("END");
                        out.flush();
                        in.close();
                        out.close();
                        socket1.close();
                        
                        break;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                        
                        case 'W'://WHO
                            if (input[0].equalsIgnoreCase("WHO")){
                        try{
                            out.println("200 OK");
                            out.println("List of active users:");
                        for (int i = 0; i < handlers.size(); i++) {
                          synchronized (handlers) {
                                ChildThread handler
                                        = (ChildThread) handlers.elementAt(i);
                                System.out.println("INSIDE WHO");
                                    
                                out.println(handler.current_user+" "+handler.socket1.getInetAddress());
                                    
                                    out.println("END");
                                    out.flush();
                                handler.out.flush();
                          }
                        }}
                        catch(Exception e){ System.out.println(e);}}
                            else{out.println("INVALID COMMAND");
                        out.println("END");
                        out.flush();}
                        break;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                            
                   
                    default:
                        out.println("300 invalid command");
                        System.out.println("****Invalid command*****");
                        out.println("Try entering the right command agian");
                        out.println("END");
                        out.flush();
                        break;
                }
                savetofile();
                System.out.println("waiting for command");
                
            }
        } catch (IOException ioe) {
           out.println("Client disconnected");
        } finally {
            try {
                out.println("next command");
            } catch (Exception ioe) {
            } finally {
                synchronized (handlers) {
                    handlers.removeElement(this);
                }
            }
        }
    }

    public int addtolist(String x) {
        if (map.isEmpty()) {
            System.out.println("Address book is empty");
            map.put(1001, x);
            return 1001;
        } else {
            Iterator<Integer> keySetIterator = map.keySet().iterator();
            int key = 0;
            while (keySetIterator.hasNext()) {
                key = keySetIterator.next();

            }
            map.put(key + 1, x);
            return key + 1;
        }

    }

    public void savetofile() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("AddressBook.txt")));

            for (Map.Entry<Integer, String> m : map.entrySet()) {
                pw.println(m.getKey() + " " + m.getValue());
            }
            System.out.println("Saving to AddressBook.txt");
            pw.flush();
            pw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadtoMap(File f) {
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));

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

    public String deletelist(int i) {

        if (map.get(i) != null) {
            map.remove(i);
            return "200 ok";
        } else {
            return "401 no record found";
        }
    }

}
