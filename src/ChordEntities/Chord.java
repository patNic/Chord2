package ChordEntities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Chord extends Thread {
	
	public static ArrayList<Node> chordRing = new ArrayList<Node>();
	private static String myIp, portNum;
	public static void addNewNode(String ip, String port) {
		myIp =ip;
		portNum = port;
	}
	public static void writeNodeAddress(String ip) {
		myIp = ip;
		try(
			FileWriter fw = new FileWriter("./NodeAddresses.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
			{
			    out.println(ip);
			    fw.flush();
			
			} catch (IOException e) {
			  System.out.println("At Chord writeNodeAddress "+e.getMessage());
			}
	}
	
	public static ArrayList<String> readNodeAddresses(){
		ArrayList<String> addresses = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("NodeAddresses.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       addresses.add(line);
		    }
		} catch (Exception e) {
			System.out.println("At Chord readNodeAddress "+e.getMessage());
			e.printStackTrace();
		} 
		
		return addresses;
	}
	
	public static void setStringsToNull() {
		myIp = null;
		portNum = null;
	}
	public void run() {
		while(true) {
			try {
				if(myIp != null && portNum !=null)
				{
					System.out.println("At Chord run ");
					chordRing.add(new Node(myIp, portNum));
				}
				else {
					System.out.print("");
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}