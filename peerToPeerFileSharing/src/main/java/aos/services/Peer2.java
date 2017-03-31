package main.java.aos.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import main.java.aos.model.Message;
import main.java.aos.model.PeerModel;
import main.java.aos.model.ResponseMessage;
import main.java.aos.util.CommonUtil;

/**
 * 
 * @author Amit Peer class which can act as server and client
 */
public class Peer2 extends Thread {
	
	static String indexServerHostName = null;
	private Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Peer2(Socket socket) {
		this.socket = socket;
	}

	public Peer2() {
	}/*default constructor*/



	public static void main(String[] args) {
		Scanner scanner1 = null;
		try{
		System.out.println("Enter Index Server host Name e.g localhost");
		indexServerHostName = new Scanner(System.in).nextLine();
		new Peer2().start();
		while (true) {
			System.out.println("========================================================");
			System.out.println("Enter 0 to exit");
			System.out.println("Enter 1 to Register peer 1");
			System.out.println("Enter 2 to search a file");
			System.out.println("Enter 3 for 1000 sequential request");
			scanner1 = new Scanner(System.in);
			String input = scanner1.nextLine();
			switch (input) {
			case "0":
				System.out.println("EXITING..............BYE!!!!!");
				System.exit(0);
				break;
			case "1":
				Peer2 p2 = new Peer2();
				p2.register();
				break;
			case "2":
				System.out.println("Search file operation requested by peer 2");
				System.out.println("======Enter file name to search a file =======");
				scanner1 = new Scanner(System.in);
				String fileName = scanner1.nextLine();
				System.out.println("Peer 2 requested for file " + fileName);
				p2 = new Peer2();
				p2.searchFile(fileName);
				break;
			case "3":
				long startTime = new Date().getTime();
				System.out.println("======Enter file name to search a file =======");
				scanner1 = new Scanner(System.in);
				String fileNameToSearch = scanner1.nextLine();
				System.out.println("Peer 2 requested for file " + fileNameToSearch);
				for (int i = 0; i < 1000; i++) {
					p2 = new Peer2();
					p2.searchFile(fileNameToSearch);
				}
				long endTime = new Date().getTime();
				System.out.println("Time taken to process 1000 request "+(endTime -startTime)); 
				break;
			default:
				System.out.println("=========Enter valid input===================");
				break;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			scanner1.close();
		}

	}

	public void register() {
		 Socket socket = null;
		 ObjectInputStream inputStream = null;
		 ObjectOutputStream outputStream = null;
		
			try {
				/*create message object to send it to index server*/
				Message msg = new Message();
				msg.setAction(CommonUtil.ACTION_REGISTER);				
				List<String> fileList_1 = new ArrayList<String>();
				for(int i = 1 ; i <= 10 ; i ++){
					fileList_1.add(i+"kb_p2.txt");
				}
				fileList_1.add("common_p1_p2.txt");
				fileList_1.add("common_p2_p3.txt");
				
				/*create socket connection */
				socket = new Socket(indexServerHostName, 5555);
				System.out.println("Registering peer 2");
				outputStream = new ObjectOutputStream(socket.getOutputStream());				
				PeerModel p1 = new PeerModel(InetAddress.getLocalHost().getHostName(),"2222", fileList_1, "");
				msg.setModel(p1);
				System.out.println( "peer 2 have been registered at Index server successfully at "+msg.getModel().getId());
				outputStream.writeObject(msg);

			} catch (SocketException se) {
				se.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/*search file*/
	public void searchFile(String fileName) {
		 Socket socket = null;
		 ObjectInputStream inputStream = null;
		 ObjectOutputStream outputStream = null;
		
			try {
				/*create message object to send it to index server*/
				Message msg = new Message();
				msg.setAction(CommonUtil.ACTION_SEARCH);
				msg.setFileToSerach(fileName);
				socket = new Socket(indexServerHostName, 5555);/*Index server port*/
				outputStream = new ObjectOutputStream(socket.getOutputStream());				
				System.out.println("Peer request file = " + msg.getFileToSerach());				
				outputStream.writeObject(msg);		
				/*input stream for receiving the response sent back from Index server*/
				inputStream =  new ObjectInputStream(socket.getInputStream());
				ResponseMessage resMsg = (ResponseMessage) inputStream.readObject();		
				
				if(resMsg.getPeerModelList().size() > 0){ /*check if the file is found at any of the peer*/
					
					System.out.println(msg.getFileToSerach()+" file found at below peers ");
					
					for(PeerModel currentPeerName : resMsg.getPeerModelList()){
						
						System.out.println(currentPeerName.getId());
						
					}
					/*Obtain the file from the some other peer*/
					obtain(msg.getFileToSerach(),resMsg.getPeerModelList().get(0));
				}else{ /* file not found*/
					
					System.out.println(msg.getFileToSerach()+" file NOT found");
					
				}
				

			} catch (SocketException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/*peer transfers the requested file to other peers*/
	@Override
	public void run() {
		
		ServerSocket serverSocket = null;
		try {
		
			
			serverSocket = new ServerSocket(2222);
			while (true) {

				System.out.println("Peer 2 started at port 2222....");
				Socket otherPeerSocket = serverSocket.accept();
				/* start new thread for each request */
				new Peer2(otherPeerSocket).start();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		Message msg = null;
		ObjectInputStream ois;
		ObjectOutputStream oos = null;
		
		boolean isConnected = true;
		do {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				msg = (Message) ois.readObject();
				System.out.println("Peer 2 is being requested  file" + msg.getFileToSerach());
				String fileRequested = getAbsPathOfTheFile(msg.getFileToSerach(),CommonUtil.SEND);//CommonUtil.RCV+msg.getFileToSerach();
				oos = new ObjectOutputStream(socket.getOutputStream());
				String content = new String(Files.readAllBytes(Paths.get(fileRequested)));
				oos.writeObject(content);
		        oos.flush();
		        isConnected = false;

			} catch (Exception e) {
				//e.printStackTrace();
			}
		} while (isConnected);
	}
	
	/*obtain a file from peer*/
	public void obtain(String fileName, PeerModel peerToGetTheFile){
		Socket socket = null;
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			
			System.out.println("Downloading file "+fileName +" from peer "+peerToGetTheFile.getId());
			/* create message object to send it to peer server */
			Message msg = new Message();
			/* to indicate its a file obtain request */
			msg.setAction(CommonUtil.ACTION_OBTAIN_FILE);
			msg.setFileToSerach(fileName);
			socket = new Socket(peerToGetTheFile.getIpAddress(), Integer.parseInt(peerToGetTheFile.getPort()));
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			/* write to output stream */
			outputStream.writeObject(msg);
			outputStream.flush();
			/* input stream for the response received from peer server */			
			inputStream = new ObjectInputStream(socket.getInputStream()); //initiate reader
			String fileContent = inputStream.readObject().toString();  			
			String filePath = getAbsPathOfTheFile(fileName, CommonUtil.RCV);
			filePath = filePath+"\\"+fileName;
			FileWriter writer = new FileWriter(filePath,true);//initiate writer
			writer.write(fileContent);                                
			writer.close();       


		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*get absolute path of the file */
	private String getAbsPathOfTheFile(String fileName, String folderPath){
		final File f = new File(Peer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		//File f2 = new File(f.getParent());
		File f1 = new File(f.getParent().concat("\\") + folderPath);
		File[] listOfFiles = f1.listFiles();                      //store files into file array
		String filePath = f.getParent().concat("\\") + folderPath;
		if("rcv".equalsIgnoreCase(folderPath)){
			filePath = f.getParent().concat("\\")+folderPath;
			return filePath;
		}
		for(File fName : listOfFiles){
			if(fName.getName().equalsIgnoreCase(fileName)){
				filePath = fName.getAbsolutePath();
			}
		}
		return filePath;
		
	}

}
