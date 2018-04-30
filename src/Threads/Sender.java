package Threads;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Sender extends Thread{
	private DatagramSocket socket;
	private Object node;
	private InetAddress address;
	private int port;
	
	public Sender(DatagramSocket socket, Object n, InetAddress address, int port) {
		this.socket = socket;
		node = n;
		this.address = address;
		this.port = port;
	}
	public void run() {
			try {
				 ByteArrayOutputStream bo_stream = new ByteArrayOutputStream();
				 ObjectOutputStream o_stream = new ObjectOutputStream(bo_stream);
				 o_stream.flush();
				 o_stream.writeObject(node);
				 o_stream.flush();
				 byte[] buf = bo_stream.toByteArray();
				 DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
				 socket.send(packet);
				 
				 o_stream.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
	}
}
