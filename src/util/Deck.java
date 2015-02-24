package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {
	private ArrayList<CardWithQuantity> mainBoard;
	private ArrayList<CardWithQuantity> sideBoard;
	private String deckName;
	
	/**
	 * Creates a new deck with just a name
	 * @param name
	 */
	public Deck(String name){
		this(new ArrayList<CardWithQuantity>(), name);
	}
	
	public Deck(ArrayList<CardWithQuantity> cards, String name) {
		this(cards, new ArrayList<CardWithQuantity>(), name);
	}
	
	public Deck(ArrayList<CardWithQuantity> main, ArrayList<CardWithQuantity> side, String name) {
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
				str = str.trim();
				String[] split = str.split(" ");
				String cardName;
				if(containsNumber(split[0])){
					cardName = str.substring(split[0].length()+1);
				}else{
					cardName = str;
				}
//				DataParser.getInstance()
				
			}
		}
	}
	
	public boolean containsNumber(String s){
		return s.contains("0") || s.contains("1") || s.contains("2") || s.contains("3") || s.contains("4") ||
				s.contains("5") || s.contains("6") || s.contains("7") || s.contains("8") || s.contains("9");
	}
	
	public void addToMain(Card card){
		add(card, mainBoard);
	}
	
	public void addToSide(Card card){
		add(card, sideBoard);
	}
	
	private void add(Card card, ArrayList<CardWithQuantity> location){
		for(CardWithQuantity cwq : location){
			if(card.equals(cwq.getCard())){
				cwq.increamentQuantity();
				return;
			}
		}
	}
	
	public ArrayList<CardWithQuantity> getMainBoard(){
		return mainBoard;
	}
	
	public ArrayList<CardWithQuantity> sideBoard(){
		return sideBoard;
	}
	
	public String getName(){
		return deckName;
	}
}
