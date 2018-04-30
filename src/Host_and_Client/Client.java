package Host_and_Client;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import ChordEntities.ChordRing;
import ChordEntities.Node;
import Threads.Receiver;
import Threads.Sender;

public class Client extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node myNode;
	//private ArrayList<Node> chordRing;
	private String myPort = "9991", hostPort = "7777";
	private DatagramSocket socket;
	private Receiver myReceiver;
	private Sender mySender;
	private ChordRing ring, ring1;
	private int ringSize;
	
	public Client(String host) throws NoSuchAlgorithmException, UnknownHostException{
		myNode = new Node(InetAddress.getLocalHost().getHostAddress(), myPort);
		ring1 = new ChordRing(new ArrayList<Node>());
		try {
			socket = new DatagramSocket(Integer.parseInt(myPort));
			
			String init = new String("INIT_");
			mySender = new Sender(socket, init, InetAddress.getByName(host), Integer.parseInt(hostPort));
			mySender.start();
			
			myReceiver = new Receiver(socket);
			myReceiver.start();
			
			start();
			welcome();
		}
		catch(Exception e) {
			System.out.println("At Client constructor  "+ e.getMessage());
		}
	}
	public void welcome() {
		System.out.println("CHORD!\nTrying to connect to host ...");
	}
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			Object received = myReceiver.getReceived();
			if(received != null) {
				if(received.getClass() == ring1.getClass()) {
					String r = new String("RAND_");
	
					ring = ((ChordRing) received);
					mySender = new Sender(socket, r, myReceiver.getPacketAddress(), myReceiver.getPort());
					mySender.start();
					myReceiver.setReceived();
				}
				else if(received.getClass() == myNode.getClass()) {
					String r = new String("JOIN_");
					
					Node n = (Node) received;
					System.out.println("Node " + n.getID());
					mySender = new Sender(socket, r, myReceiver.getPacketAddress(), myReceiver.getPort());
					mySender.start();
					myReceiver.setReceived();
					
					ring.getRing().add(n);
					updatePredeceSucce_ssor();
				}
				
			}
			else {
				System.out.print("");
			}
		}
	}
	public void updatePredeceSucce_ssor() {
		ArrayList<Node> chordRing = ring.getRing();
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
	public static void main(String[] args) throws NoSuchAlgorithmException, UnknownHostException {
		new Client(InetAddress.getLocalHost().getHostAddress());
	}
}
