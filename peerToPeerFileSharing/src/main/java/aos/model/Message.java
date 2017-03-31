package main.java.aos.model;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String action;
	private PeerModel model;
	private String fileToSerach;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public PeerModel getModel() {
		return model;
	}

	public void setModel(PeerModel model) {
		this.model = model;
	}

	public String getFileToSerach() {
		return fileToSerach;
	}

	public void setFileToSerach(String fileToSerach) {
		this.fileToSerach = fileToSerach;
	}

}
