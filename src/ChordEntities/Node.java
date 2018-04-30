package ChordEntities;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.Serializable;
import java.math.BigInteger;

public class Node implements Serializable, Comparable<Node>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node successor, predecessor;
	private String ip;
	private boolean isConnected;
	private BigInteger id;
	private boolean isHost;
	private String portNum;
	//private HashMap<Integer, Node> fingerTable;

	public Node(String ip, String port) throws NoSuchAlgorithmException {
		this.ip = ip;
		portNum = port;
		createNodeID();
	}
	
	private void createNodeID() throws NoSuchAlgorithmException {
		String text = this.getIPAddress().toString() + portNum;
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		StringBuffer hex = new StringBuffer();
		
		for(int i = 0; i< hash.length; i++) {
			String s = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hex.append('0');
			hex.append(s);
		}
		
		this.setID(new BigInteger(hex.toString(),16));
		
	}
	
	public void initFingerTable() {
		
	}
	
	public Node getSuccessor() {
		return successor;
	}
	
	public void setSuccessor(Node node) {
		this.successor = node;
	}
	
	public Node getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(Node node) {
		this.predecessor = node;
	}
	public String getIPAddress() {
		return ip;
	}
	
	public boolean getIsConnected() {
		return isConnected;
	}
	
	public void setIsConnected(boolean bool) {
		this.isConnected = bool;
	}
	
	public BigInteger getID() {
		return id;
	}
	
	public void setID(BigInteger id) {
		this.id = id;
	}
	
	public boolean getIsHost() {
		return isHost;
	}
	public void setIsHostTrue() {
		isHost = true;
	}

	public int compareTo(Node arg0) {
		BigInteger id = arg0.getID();
		
		return this.id.subtract(id).intValue();
	}
}