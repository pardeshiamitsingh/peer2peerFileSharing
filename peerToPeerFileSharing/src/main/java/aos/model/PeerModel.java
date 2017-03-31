package main.java.aos.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Amit Data Structure of the peer
 */
public class PeerModel implements Serializable {

	/**
	 * 
	 */
	private String id;
	private static final long serialVersionUID = 1L;
	private String ipAddress;
	private String port;
	private List<String> listOfFilesShared;
	private String dirPath;
	
	public PeerModel(String ipAddress,String port,List fileList,String dirPath) {
		this.id = ipAddress+":"+port;
		this.ipAddress = ipAddress;
		this.listOfFilesShared = fileList;
		this.dirPath = dirPath;
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public List<String> getListOfFilesShared() {
		return listOfFilesShared;
	}

	public void setListOfFilesShared(List<String> listOfFilesShared) {
		this.listOfFilesShared = listOfFilesShared;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

}
