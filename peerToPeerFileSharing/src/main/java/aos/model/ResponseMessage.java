package main.java.aos.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Amit Response sent back from index server
 */
public class ResponseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private List<PeerModel> peerModelList;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PeerModel> getPeerModelList() {
		return peerModelList;
	}

	public void setPeerModelList(List<PeerModel> peerModelList) {
		this.peerModelList = peerModelList;
	}

}
