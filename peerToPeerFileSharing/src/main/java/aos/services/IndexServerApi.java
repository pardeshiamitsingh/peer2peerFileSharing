package main.java.aos.services;

import java.util.List;

import main.java.aos.model.PeerModel;

/**
 * 
 * @author Amit Interface exposed to all the peers
 */
public interface IndexServerApi {

	boolean register(PeerModel peer);

	List<PeerModel> searchFile(String fileName);
}
