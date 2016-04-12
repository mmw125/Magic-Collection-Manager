package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;

/**
 * This imports a json file containing all of the card data into card and set classes
 * This is relatively efficient and should take a second or two depending on the speed of the computer
 * @author Mark Wiggans
 */
public class DataParser {
	private ArrayList<Card> cards;
	private ArrayList<Set> sets;
	private static DataParser parser;
	
	public static DataParser getInstance(){
		if(parser == null){
			parser = new DataParser();
		}
		return parser;
	}

	public static void main(String[] args) throws IOException {
		DataParser p = new DataParser();
		JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File("AllSets.json")), "UTF-8"));
		try{
			p.readArray(reader);
		}finally{
			reader.close();
		}
	}
	
	private DataParser(){
		cards = new ArrayList<Card>(30000);
		sets = new ArrayList<Set>();
	}
	
	/**
	 * Parses a Json file with the magic set and card information
	 * @throws IOException
	 */
	public void parseData() throws IOException{
		JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File("AllSets.json")), "UTF-8"));
		try{
			readArray(reader);
		}finally{
			reader.close();
		}
	}
	
	public Card cardFromName(String name){
		for(Card c : cards){
			if(name.startsWith(c.getName())){
				return c;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param reader
	 * @throws IOException
	 */
	public void readArray(JsonReader reader) throws IOException{
		reader.beginObject();
		while(reader.hasNext()){
			sets.add(parseSet(reader));
		}
	}
	
	/**
	 * Reads and imports a set from the reader
	 * @param reader the reader to read from
	 * @return a set with all of the cards properly added
	 * @throws IOException
	 */
	public Set parseSet(JsonReader reader) throws IOException{
		String setName = reader.nextName();
		reader.beginObject();
		Set set = new Set(setName);
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("name")){
				set.setName(reader.nextString());
			}else if(name.equals("cards")){
				reader.beginArray();
				while(reader.hasNext()){
					Card card = parseCard(reader);
					set.addCard(card);
					card.setSet(set);
					cards.add(card);
				}
				reader.endArray();
//			}else if(name.equals("gathererCode")){
//
//			}else if(name.equals("oldCode")){
//				
//			}else if(name.equals("releaseDate")){
//				
//			}else if(name.equals("border")){
//				
//			}else if(name.equals("type")){
//				
//			}else if(name.equals("block")){
//				
			}else if(name.equals("onlineOnly")){
				try{
					set.setOnlineOnly(reader.nextBoolean());
				}catch(IllegalStateException e){
					e.printStackTrace();
				}
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return set;
	}
	
	/**
	 * Creates a card given a reader that is starting an object
	 * @param reader the reader to read from
	 * @return a card with the given quantities
	 * @throws IOException
	 */
	public Card parseCard(JsonReader reader) throws IOException{
		reader.beginObject();
		Card c = new Card();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("name")){
				c.setName(reader.nextString());
			}else if(name.equals("manaCost")){
				c.setManaCost(reader.nextString());
			}else if(name.equals("cmc")){
				try{
					c.setCMC(reader.nextInt());
				}catch(NumberFormatException e){
					reader.skipValue();
				}
			}else if(name.equals("number")){
				c.setCollectorsNumber(reader.nextString());
			}else if(name.equals("multiverseid")){
				c.setMultiverseID(reader.nextInt());
			}else if(name.equals("rarity")){
				String rarityString = reader.nextString();
				Rarity rarity = Rarity.stringToRarity(rarityString);
				c.setRarity(rarity);
//				c.setRarity(Rarity.valueOf(reader.nextString()));
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return c;
		
	}
	
	/**
	 * Turns a set name into the code for that set
	 * @param setName the given set name
	 * @return the code for the given set. null if it doesn't exist
	 */
	public String setNameToCode(String setName){
		for(Set set : sets){
			if(set.getName().equals(setName)){
				return set.getSetCode();
			}
		}
		return null;
	}
	
	/**
	 * Gets all of the cards in the file that it parsed
	 * @return
	 */
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public ArrayList<Set> getSets(){
		return sets;
	}
}
