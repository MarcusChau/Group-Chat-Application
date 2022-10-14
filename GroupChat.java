import java.net.*;
import java.io.*;
import java.util.*;


public class GroupChat {
    
    private static final String TERMINATE = "Exit"; // code to terminate the program
    static String name;
    static volatile boolean finished = false;


    // main method
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Two arguments required: <multicast-host> <port number>");
        } else {

            try {

                InetAddress group = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);
                Scanner scn = new Scanner(System.in);
                System.out.println("Enter your name: ");
                name = scn.nextLine();
                MulticastSocket socket = new MulticastSocket(port);

                // Since we are deploying
                socket.setTimeToLive(0); // This on localhost only (For a subnet set it as 1)

                socket.joinGroup(group);
                Thread t = new Thread(new ReadThread(socket, group, port));

                // Spawn Thread
                t.start();

                // Sent to the current group
                System.out.println("Start typing message...\n");

                while(true) {

                    String message;
                    message = scn.nextLine();

                    if (message.equalsIgnoreCase(GroupChat.TERMINATE)) {
                        finished = true;
                        socket.leaveGroup(group);
                        socket.close();
                        scn.close();
                        break;
                    }

                    
                    // Display message
                    message = name + " : " + message;
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
                    socket.send(datagram);
                }
            } catch(SocketException se) {
                System.out.println("Error creating sockt");
                se.printStackTrace();
            }

            catch (IOException ie) {
                System.out.println("Error reading/writing from/to socket");
                ie.printStackTrace();
            }
        }
    }   
}

// class ReadThread implements Runnable
class ReadThread implements Runnable {

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;

    ReadThread(MulticastSocket socket, InetAddress group, int port) {

        this.socket = socket;
        this.group = group;
        this.port = port;
    }


    // Method run that is inherited from class Runnable
    @Override
    public void run() {
        // Running only if GroupChat is not closed
        while(!GroupChat.finished) {

            // Byte array
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            /*
             * Datagram socket is the sending or recieving point for a packet delivery service. 
             * Each packet sent or recieved on a datagram socket is individualy addressed and routed.
             * Multiple packets sent from one machine to another may be routed differently, and may arrive in any order.
             */
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            String message;

            try {

                socket.receive(datagram); 
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!message.startsWith(GroupChat.name)) { System.out.println(message); }
            } catch (IOException e) {
                System.out.println("Socket Closed!");
            }
        }

    }
}
