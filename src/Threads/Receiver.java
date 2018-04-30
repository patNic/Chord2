package Threads;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Receiver extends Thread{
	private DatagramSocket mySocket;
	private byte[] buf = new byte[1024];
	private InetAddress packetAddress;
	private int portNum;
	private Object receivedNode;
	
	public Receiver(DatagramSocket  socket) {
		mySocket = socket;
	}
	public void run() {
		while(true){
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				mySocket.receive(packet);
				ByteArrayInputStream byteStream = new
			                                  ByteArrayInputStream(buf);
			      ObjectInputStream is = new
			           ObjectInputStream(new BufferedInputStream(byteStream));
			     receivedNode =is.readObject();
			     is.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			System.out.println("\n\t\t["+packet.getAddress());
			packetAddress = packet.getAddress();
			portNum = packet.getPort();
		}
	}
	
	public InetAddress getPacketAddress() {
		return packetAddress;
	}
	public int getPort() {
		return portNum;
	}
	public Object getReceived() {
		return receivedNode;
	}
	public void setReceived() {
		receivedNode = null;
	}
}
