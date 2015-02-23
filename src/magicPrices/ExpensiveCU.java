package magicPrices;

import java.io.IOException;

import util.Card;
import util.DataParser;
import util.PriceScraper;

public class ExpensiveCU {
	public static void main(String[] args){
		DataParser parser = DataParser.getInstance();
		System.out.println("Parsing data");
		try {
			parser.parseData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Reading prices");
		PriceScraper scraper = new PriceScraper(parser);
		scraper.run();
		for(Card c : parser.getCards()){
			if(c.isUncommonOrCommon() && c.getCurrentPrice() > .9){
				System.out.println(c.toString());
			}
		}
		System.out.println(parser.getCards().size());
	}
}
