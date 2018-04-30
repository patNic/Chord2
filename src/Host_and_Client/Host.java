package Host_and_Client;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import ChordEntities.ChordRing;
import ChordEntities.Node;
import Threads.Receiver;
import Threads.Sender;

public class Host extends Thread  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String myPort = "7777";
	private Node myNode;
	private Sender mySender;
	private Receiver myReceiver;
	private DatagramSocket socket;
	private ArrayList<Node> chordRing;
	private ChordRing ring;
	
	public Host() throws NoSuchAlgorithmException, UnknownHostException {
		myNode = new Node(InetAddress.getLocalHost().getHostName(), myPort+"");
		myNode.setPredecessor(myNode);
		myNode.setSuccessor(myNode);
		
		chordRing = new ArrayList<Node>();
		chordRing.add(myNode);
		
		ring = new ChordRing(chordRing);
		
		try {
			socket = new DatagramSocket(Integer.parseInt(myPort));
			myReceiver = new Receiver(socket);
			myReceiver.start();
			
			start();
			
		} catch (SocketException e) {
			System.out.println("At Host Constructor "+ e.getMessage());
			e.printStackTrace();
		}
		welcome();
	}
	
	public void welcome() {
		System.out.println("CHORD!\nWaiting for peers ...");
	}
	public void run() {
		while(true) {
			Object object = myReceiver.getReceived();
			if(object != null) {
				if(object.getClass() == myNode.getClass()) {
					
				}
				else if(object.getClass() == "".getClass()) {
					if(object.toString().equals("INIT_")) {
						System.out.println("Somebody wants to join the network");
				
						mySender = new Sender(socket, ring, myReceiver.getPacketAddress(),myReceiver.getPort());
						mySender.start();
						
						myReceiver.setReceived();
					}
					else if(object.toString().equals("RAND_")) {
						System.out.println("Sending random node ... ");
						Collections.shuffle(chordRing);
						
						Node buf = chordRing.get(chordRing.size()/2);
						mySender = new Sender(socket, buf, myReceiver.getPacketAddress(),myReceiver.getPort());
						mySender.start();
						
						myReceiver.setReceived();
					}
					else if(object.toString().equals("JOIN_")) {
						System.out.println("JOINIng the network");
						try {
							Node newNode = new Node(myReceiver.getPacketAddress()+"", ""+myReceiver.getPort());
							
							chordRing.add(newNode);
							System.out.println("ChordRing Size "+ chordRing.size());
							updatePredeceSucce_ssor();
							myReceiver.setReceived();
							
						} catch (NoSuchAlgorithmException e) {
							System.out.print("At host JOIN"+e.getMessage() );
							e.printStackTrace();
						}
					}
				}
			}
			else {
				System.out.print("");
			}
		}
	}
	public void updatePredeceSucce_ssor() {
		for(int i = 0; i < chordRing.size(); i++) {
			if( i == chordRing.size() -1) {
				chordRing.get(i).setSuccessor(chordRing.get(0));
				break;
			}
			chordRing.get(i).setSuccessor(chordRing.get(i+1));
		}
		
		
		for(int i = chordRing.size()-1; i >= 0; i--) {
			if( i == 0) {
				chordRing.get(i).setPredecessor(chordRing.get(chordRing.size() - 1));
				break;
			}
			chordRing.get(i).setPredecessor(chordRing.get(i-1));
		}
		for(int i = 0; i < chordRing.size(); i++) {
			System.out.println("Node "+ chordRing.get(i).getID() +
					" Successor "+ chordRing.get(i).getSuccessor().getID() +
					" Predecessor "+ chordRing.get(i).getPredecessor().getID());
		}
		Collections.sort(chordRing);
		for(int i = 0; i < chordRing.size(); i++) {
			if( i == chordRing.size() -1) {
				chordRing.get(i).setSuccessor(chordRing.get(0));
				break;
			}
			chordRing.get(i).setSuccessor(chordRing.get(i+1));
		}
		
		
		for(int i = chordRing.size()-1; i >= 0; i--) {
			if( i == 0) {
				chordRing.get(i).setPredecessor(chordRing.get(chordRing.size() - 1));
				break;
			}
			chordRing.get(i).setPredecessor(chordRing.get(i-1));
		}
		for(int i = 0; i < chordRing.size(); i++) {
			System.out.println("Node "+ chordRing.get(i).getID() +
					" Successor "+ chordRing.get(i).getSuccessor().getID() +
					" Predecessor "+ chordRing.get(i).getPredecessor().getID());
		}
	}
	
	public Node getNode() {
		return myNode;
	}
	public void addToChordRing(Node n) {
		chordRing.add(n);
	}
	public static void main(String[] args) throws NoSuchAlgorithmException, UnknownHostException{
		new Host();
	}
	
}
