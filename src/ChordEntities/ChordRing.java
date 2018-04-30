package ChordEntities;

import java.io.Serializable;
import java.util.ArrayList;

public class ChordRing implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Node> chordRing;
	public ChordRing(ArrayList<Node> ring) {
		chordRing = ring;
	}
	public ArrayList<Node> getRing(){
		return chordRing;
	}
}
