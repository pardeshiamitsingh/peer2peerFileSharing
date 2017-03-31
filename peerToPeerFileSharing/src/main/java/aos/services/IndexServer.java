package main.java.aos.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.aos.model.Message;
import main.java.aos.model.PeerModel;
import main.java.aos.model.ResponseMessage;
import main.java.aos.util.CommonUtil;

/**
 * 
 * @author Amit Index server for keeping track of all the peers along with the
 *         files that are shared by peers
 */
public class IndexServer extends Thread implements IndexServerApi, Runnable {
	/*map where key = peer ID and value = entire PeerModel object*/
	private static Map<String, PeerModel> peersMap = new HashMap<String, PeerModel>();

	private Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Map<String, PeerModel> getPeersMap() {
		return peersMap;
	}

	public void setPeersMap(Map<String, PeerModel> peersMap) {
		this.peersMap = peersMap;
	}

	public IndexServer(Socket socket) {
		this.socket = socket;
	}

	/* to register peer with index Sever */
	@Override
	public boolean register(PeerModel peer) {
		long startTime = new Date().getTime();
		System.out.println("Server is registering peer id  " + peer.getId());
		boolean isRegitered = false;
		PeerModel localPeer = peersMap.get(peer.getId());

		if (localPeer == null) {/* Peer is registering for the first time */

			peersMap.put(peer.getId(), peer);
			isRegitered = true;

		} else { /* peer is registering more files */

			localPeer.getListOfFilesShared().addAll(peer.getListOfFilesShared());
			peersMap.put(peer.getId(), localPeer);
			isRegitered = true;
		}
		long endTime = new Date().getTime();
		long timeTaken = (endTime - startTime);
		System.out.println("Server is done with registering peer id  " + peer.getId()+ " in "+timeTaken+ " milliseconds");
		return isRegitered;
	}

	/* Searches and returns the list of peers having the file */
	@Override
	public List<PeerModel> searchFile(String fileName) {
		long startTime = new Date().getTime();
		System.out.println("Server is searching for file  " + fileName);
		List<PeerModel> returnPeerList = new ArrayList<PeerModel>();
		for (Map.Entry<String, PeerModel> entry : peersMap.entrySet()) {
			PeerModel currentPeerModel = entry.getValue();
			if (currentPeerModel.getListOfFilesShared()
					.contains(fileName)) {/* check for the filename */
				returnPeerList.add(currentPeerModel);
			}
		}
		
		long endTime = new Date().getTime();
		long timeTaken = (endTime - startTime);
		System.out.println("Server is done with searching file " + fileName+ " in "+timeTaken+ " milliseconds");
		return returnPeerList;
	}

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(5555);
			while (true) {
				 System.out.println("Index server started and waiting for peer to connect....");
				 Socket peerSocket = serverSocket.accept();
				 new IndexServer(peerSocket).start();/*start new thread for each request*/
			
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		Message msg = null;
		ObjectInputStream ois;
		ObjectOutputStream oos = null;
		boolean isConnected = true;
		do {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				msg = (Message) ois.readObject();
				System.out.println("Server is being requested  " + msg.getAction() +" operation");
				ResponseMessage resMsg = new ResponseMessage();
				if (CommonUtil.ACTION_REGISTER.equalsIgnoreCase(msg.getAction())) {
					this.register(msg.getModel());
				} else if (CommonUtil.ACTION_SEARCH.equalsIgnoreCase(msg.getAction())) {
					List<PeerModel> returnPeerList = this.searchFile(msg.getFileToSerach());
					resMsg.setPeerModelList(returnPeerList);
				}
				resMsg.setStatus(CommonUtil.SUCCESS);
				oos.writeObject(resMsg);
				oos.flush();
				ois.close();
				isConnected = false;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (isConnected);
	}

}
