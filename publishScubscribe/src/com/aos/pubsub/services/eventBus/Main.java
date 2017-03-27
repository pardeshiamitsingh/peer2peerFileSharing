package com.aos.pubsub.services.eventBus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
public class Main extends Thread{

    int port;
    static ThreadPoolExecutor executor;
    Main(int port)                                      //Main constructor that receive port number
    {
        this.port = port;
    }
    public static void main(String[] args) {

        System.out.println("=======================================================\n");
        System.out.println("Waiting for peers to register files..\n");
        System.out.println("Waiting for peers to search files..\n");
        System.out.println("=======================================================\n");
        /////////////////////////////////////////////////////////////////////////////
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        /////////////////////////////////////////////////////////////////////////////
        Thread t1 = new Thread (new Main(60000));   //Create a thread that Listen for registration requests from peers
        t1.start();                                     //Start thread
        /////////////////////////////////////////////////////////////////////////////
        Thread t2 = new Thread (new Main(60001));   //Create a thread that Listen for registration requests from peers
        t2.start();                                     //Start thread
        /////////////////////////////////////////////////////////////////////////////
        while (true)
        {
            System.out.println("Type the action number as following:");
            System.out.println("1. To exit.");
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine().trim();
            if (userInput.equals("1"))                         //if user entered 6
            {
                System.out.println("Exiting...");
                System.exit(0);                         //exit the program
            }
        }
    }

    /*********************************************************************************************/

    public synchronized void run(){
        try {
            ServerSocket ssock = new ServerSocket(port); // create a socket for both search or register listeners
            while (true) {                                 //keep listening for peers
                Socket sock = null;
                sock = ssock.accept();                     //accept peer connection
                Listener listener = new Listener(sock,port);
                executor.execute(listener);
                //new Listener(sock,port).start();         // create new thread
            }
        }
        catch(UnknownHostException unknownHost){                                           //To Handle Unknown Host Exception
            System.err.println("host not available..!");
        }
        catch (IOException e) {                          //catch the I/O errors
            e.printStackTrace();
        }
        catch (Exception e) {                          //catch the general errors
            e.printStackTrace();
        }
    }
}
