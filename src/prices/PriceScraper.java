package prices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Scanner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import util.Card;
import util.DataParser;
import util.Set;

/**
 * Scrapes the prices for all of the cards from TCGplayer
 * @author Mark Wiggans
 */
public class PriceScraper implements Runnable{
	private DataParser parser;
	private WebClient webClient;
	
	public PriceScraper(DataParser parser){
		this.parser = parser;
		webClient = new WebClient();
	}
	
	public void getAllPrices(){
		for(Set s : parser.getSets()){
			if(!s.getOnlineOnly()){
				getPrices(s, parser);
			}
		}
	}
	
	public void getPrices(Set setIn, DataParser parser){
		String url = "http://magic.tcgplayer.com/db/price_guide.asp?setname="+nameChanges(setIn.getName());
		PageParser pageParser = new PageParser(parser);
		HtmlPage page = null;
        try {
                page = webClient.getPage(url);
        } catch (Exception e) {
            System.out.println("Invalid web address");
            e.printStackTrace();
        }
        pageParser.parsePage(page, setIn);
	}
	
	public class PageParser {
		private DataParser parser;
		
		public PageParser(DataParser parser){
			this.parser = parser;
		}
			
		public void parsePage(HtmlPage page, Set setIn){
			if(page != null){
				String[] storage = page.getPage().asText().split("\n");
	        	for(Set set : parser.getSets()){
	        		if(set.getName().equals(setIn.getName())){
	        			for(int i = 6; i < storage.length; i++){
	        				String[] str = storage[i].split("\t");
	        				double price = 0;
	        				try{
	        					if(!str[6].equals("SOON") && !str[6].equals("Mid")){
	        						price = Double.parseDouble(str[6].trim().replace("$", "").replace(",", ""));
	        					}
	        				}catch(Exception e){
	        					e.printStackTrace();
	        				}
	        				//set.addPrice(str[0].trim(), price);
	        			}
	        		}
	        	}	
			}else{
				System.err.println("The webpage could not be parsed");
			}
		}
	}
	
	/**
	 * Changes the set name in the json file to the ones that TCGplayer uses
	 * @param setName the setname to change
	 * @return the changed setname
	 */
	public static String nameChanges(String setName){
		switch (setName){
			case "Magic 2015 Core Set": return "Magic 2015 (M15)"; 
			case "Magic 2014 Core Set":	return "Magic 2014 (M14)";
			case "Magic 2013": return "Magic 2013 (M13)";
			case "Magic 2012": return "Magic 2012 (M12)";
			case "Magic 2011": return "Magic 2011 (M11)";
			case "Magic 2010": return "Magic 2010 (M10)";
			case "Limited Edition Alpha": return "Alpha Edition";
			case "Limited Edition Beta": return "Beta Edition";
			case "Magic: The Gathering-Commander": return "Commander";
			case "Premium Deck Series: Graveborn": return "PDS: Graveborn";
			case "Duel Decks: Venser vs. Koth": return "Duel Decks: Phyrexia vs the Coalition";
			case "Planechase 2012 Edition": return "Planechase 2012";
			case "Magic: The Gathering—Conspiracy": return "Conspiracy";
			case "Ravnica: City of Guilds": return "Ravnica";
			case "Tenth Edition": return "10th Edition";
			case "Modern Event Deck 2014": return "Magic Modern Event Deck";
			case "Commander 2013 Edition": return "Commander 2013";
			case "Ninth Edition": return "9th Edition";
			case "Eighth Edition": return "8th Edition";
		}
		return setName;
	}
	
	public void wipeCurrentCardData(){
		for(Card c : parser.getCards()){
			c.setPrice(null);
		}
	}
	
	

	/**
	 * Exports all of current pricing data
	 * @throws IOException if it cannot create the file or read it
	 */
	public void exportCurrentData() throws IOException{
		File f = new File("currentPrices.txt");
		f.createNewFile();
		PrintWriter writer = new PrintWriter(f);
		for(Card c : parser.getCards()){
			if(c.getCurrentPrice() != null){
				writer.println(c.getSet().getCode()+"~"+c.getName()+"~"+c.getCurrentPrice());
			}
		}
		writer.close();
	}
	
//	/**
//	 * Exports the card price database
//	 * @throws IOException
//	 */
//	public void exportData() throws IOException{
//		File f = new File("priceDatabase.txt");
//		f.createNewFile();
//		PrintWriter writer = new PrintWriter(f);
//		String s = "DATE";
////		for(PriceWithDate pwd : parser.getCards().get(0).getPriceHistory()){
////			s += "~"+pwd.getDate();
////		}
//		s+= "~"+PriceScraper.getCurrentDate();
//		writer.println(s);
//		for(Card c : parser.getCards()){
//			if(c.getCurrentPrice() != 0){
//				String output = c.getSet().getCode()+"~"+c.getName();
////				for(PriceWithDate priceWithDate : c.getPriceHistory()){
////					output = output + "~" + priceWithDate.getPrice();
////				}
//				output = output + "~" + c.getCurrentPrice();
//				writer.println(output);
//			}else{
//				if(!c.getSet().getOnlineOnly() || c.getSet().getCode() == "VAN"){
////					System.out.println(c.getSet().getCode()+"~"+c.getName()+"~"+c.getCurrentPrice());
//				}
//			}
//			
//		}
//		writer.close();
//	}
	
//	public void importPriceDatabase() throws FileNotFoundException{
//		File f = new File("priceDatabase.txt");
//		if(f.exists()){
//			Scanner scanner = new Scanner(f);
//			String line; ArrayList<String> splitLine;
//			ArrayList<Date> dates = new ArrayList<Date>();
//			Set storedSet = parser.getSets().get(0);
//			while(scanner.hasNextLine()){
//				line = scanner.nextLine();
//				splitLine = new ArrayList<String>(Arrays.asList((line.split("~"))));
//				if(line.startsWith("DATES")){
//					for(int i = 1; i < splitLine.size(); i++){
//						dates.add(Date.parseDate(splitLine.get(i)));
//					}
//				}else{
//					ArrayList<PriceWithDate> priceWithDate = new ArrayList<PriceWithDate>();
//					for(int i = 2; i < splitLine.size(); i++){
//						priceWithDate.add(new PriceWithDate(Double.parseDouble(splitLine.get(i)), dates.get(i-2)));
//					}
//					if(splitLine.get(0).equals(storedSet.getCode())){
//						if(splitLine.get(0).equals(storedSet.getCode())){
//							storedSet.addPrice(splitLine.get(1), priceWithDate);
//						}
//					}else{
//						for(Set s : parser.getSets()){
//							if(splitLine.get(0).equals(s.getCode())){
//								storedSet = s;
//								s.addPrice(splitLine.get(1), priceWithDate);
//								break;
//							}
//						}
//					}
//					
//				}
//				
//			}
//			scanner.close();
//		}else{
//		}
//		
//	}
	
	public void importCurrentPrices() throws FileNotFoundException{
		File f = new File("currentPrices.txt");
		if(f.exists()){
			Scanner scanner = new Scanner(f);
			String line; String[] splitLine;
			while(scanner.hasNextLine()){
				line = scanner.nextLine();
				splitLine = line.split("~");
				for(Set s : parser.getSets()){
					if(splitLine[0].equals(s.getCode())){
						//s.addPrice(splitLine[1], Double.parseDouble(splitLine[2]));
						break;
					}
				}
			}
			scanner.close();
		}
	}
	
	public static void main(String[] args){
		DataParser parser = DataParser.getInstance();
		try {
			parser.parseData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PriceScraper scraper = new PriceScraper(parser);
		scraper.scrapePrices();
	}
	
	public void run(){
		try {
			importCurrentPrices();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void scrapePrices(){
//		try { importPriceDatabase(); } catch (FileNotFoundException e) { e.printStackTrace(); }
		try { importCurrentPrices(); } catch (FileNotFoundException e) { e.printStackTrace(); }
//		try { exportData(); } catch (IOException e) { e.printStackTrace(); }
//		wipeCurrentCardData();
		getAllPrices();
		try { exportCurrentData(); } catch (IOException e) { e.printStackTrace();}
	}
	
	public static String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}
}
