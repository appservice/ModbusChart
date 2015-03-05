package eu.luckyApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServerRunningChecker {

	
	private boolean isConnectedToServer;
	private long serverId;
	private String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isConnectedToServer() {
		return isConnectedToServer;
	}

	public void setConnectedToServer(boolean isConnectedToServer) {
		this.isConnectedToServer = isConnectedToServer;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}
	

}
