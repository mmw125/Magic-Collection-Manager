package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {
	private ArrayList<Card> mainBoard;
	private ArrayList<Card> sideBoard;
	private String deckName;
	public Deck(String name){
		this(new ArrayList<Card>(), name);
	}
	
	public Deck(ArrayList<Card> cards, String name) {
		this(cards, new ArrayList<Card>(), name);
	}
	
	public Deck(ArrayList<Card> main, ArrayList<Card> side, String name) {
		mainBoard = main;
		sideBoard = side;
		deckName = name;
	}
	
	/**
	 * Imports the deck from the given file
	 * @param file the file to import
	 * @throws IOException 
	 */
	public Deck(File file) throws IOException{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("Unable to read the file");
			e.printStackTrace();
		}
		if(reader != null){
			String str;
			while((str = reader.readLine()) != null){
				
			}
			
		}
	}
	
	public ArrayList<Card> getMainBoard(){
		return mainBoard;
	}
	
	public ArrayList<Card> sideBoard(){
		return sideBoard;
	}
	
	public String getName(){
		return deckName;
	}
}
