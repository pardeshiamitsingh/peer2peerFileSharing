package main.java.aos.services;

import java.util.Date;
import java.util.Scanner;

public class TestPeer2Peer {
	static int peerId=0;
	static Peer p1;
	static Peer2 p2;
	static Peer3 p3;

	public static void main(String[] args) {
		Scanner scanner1= null;
		Scanner scanner2= null;
		Scanner scanner3= null;;
		try{
		while(true){
			System.out.println("========================================================");
			System.out.println("Enter 0 to exit");
			System.out.println("Enter 1 to Register peer 1");
			System.out.println("Enter 2 to Register peer 2");
			System.out.println("Enter 3 to Register peer 3");
			System.out.println("Enter 4 to Use peer 1");
			System.out.println("Enter 5 to Use peer 2");
			System.out.println("Enter 6 to Use peer 3");
			System.out.println("Enter 7 to search a file ");
			System.out.println("Enter 8 to for testing 1000 request ");
			System.out.println("========================================================");
			if(peerId > 0)
			System.out.println("ACTIVE PEER IS PEER-"+peerId);
			Scanner scanner = new Scanner(System. in); 
			String input = scanner. nextLine();
			switch (input) {
			case "0":System.out.println("EXITING..............BYE!!!!!"); System.exit(0);			
				break;
			case "1":  p1 = new Peer(); p1.register();			
			break;
			case "2":  p2 = new Peer2(); p2.register();		
			break;
			case "3":  p3 = new Peer3(); p3.register();			
			break;
			case "4":  System.out.println("Switched to peer 1"); peerId = 1;			
			break;
			case "5":  System.out.println("Switched to peer 2");peerId = 2;			
			break;
			case "6": System.out.println("Switched to peer 3"); peerId = 3;			
			break;
			case "7":
				System.out.println("Search file operation requested by peer "+peerId);
				if(peerId == 1){
				System.out.println("======Enter file name to search a file =======");
				scanner1 = new Scanner(System. in); 
				String fileName = scanner1. nextLine();
				System.out.println("Peer 1 requested for file "+fileName);
				p1 = new Peer();
				      p1.searchFile(fileName);				
			}else if(peerId == 2){
				System.out.println("======Enter file name to search a file =======");
				scanner2 = new Scanner(System. in); 
				String fileName = scanner2. nextLine();
				System.out.println("Peer 2 requested for file "+fileName);
				p2 = new Peer2();
				      p2.searchFile(fileName);	
			}
			else if(peerId == 3){
				System.out.println("======Enter file name to search a file =======");
				scanner3 = new Scanner(System. in); 
				String fileName = scanner3. nextLine();
				System.out.println("Peer 3 requested for file "+fileName);
				p3 = new Peer3();
				      p3.searchFile(fileName);	
			}
			break;
			case "8":	
				long startTime = new Date().getTime();
				if(peerId == 1){
				System.out.println("======Enter file name to search a file =======");
				scanner1 = new Scanner(System. in); 
				String fileName = scanner1. nextLine();
				System.out.println("Peer 1 requested for file "+fileName);
				for(int i = 0 ; i < 1000 ; i++){
					p1 = new Peer();
				      p1.searchFile(fileName);
				}
								
			}else if(peerId == 2){
				System.out.println("======Enter file name to search a file =======");
				scanner2 = new Scanner(System. in); 
				String fileName = scanner2. nextLine();
				System.out.println("Peer 2 requested for file "+fileName);
				for(int i = 0 ; i < 1000 ; i++){
				p2 = new Peer2();
				      p2.searchFile(fileName);	
				}
			}
			else if(peerId == 3){
				System.out.println("======Enter file name to search a file =======");
				scanner3 = new Scanner(System. in); 
				String fileName = scanner3. nextLine();
				System.out.println("Peer 3 requested for file "+fileName);
				{
					p3 = new Peer3();
				      p3.searchFile(fileName);	
				}
			}
				long endTime = new Date().getTime();
				System.out.println("Total time taken to process 1000 request is "+(endTime - startTime));
						
			break;
					default:
				System.out.println("=========Enter valid input===================");
				break;
			}
		}
		}catch(Exception e){
			
		}finally{
			scanner1.close();
			scanner2.close();
			scanner3.close();
		}
	}

}
