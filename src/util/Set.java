package util;

import java.util.Vector;

public class Set extends Object{
	private String setCode;
	private String setName;
	private Boolean onlineOnly = false;
	private Vector<Card> cards;
	
	/**
	 * Creates a new set with the given setCode
	 * @param setCode the 3 letter code for the set
	 */
	public Set(String setCode){
		this.setSetCode(setCode);
		cards = new Vector<Card>();
	}
	public void setOnlineOnly(boolean onlineOnly) { this.onlineOnly = onlineOnly; }
	public boolean getOnlineOnly(){ return onlineOnly; }
	public Vector<Card> getCards() { return cards; }
	public void setName(String setName) { this.setName = setName; }
	public String getName(){ return setName; }
	public String getCode(){ return getSetCode(); }
	public void addCard(Card c){ cards.add(c); }
//	public void addPrice(String cardName, double price){
//		if(cardName.contains("//")){
//			String[] cardNames = cardName.split("//");
//			cardName = cardNames[0].trim();
//			addPrice(cardNames[1].trim(), price);
//		}
//		if(cardName.contains("(")){
//			cardName = cardName.substring(0, cardName.indexOf('(')).trim();
//		}
//		for(Card c : cards){
//			if(c.getName().replace("Æ", "Ae").replace("ö", "o").equals(cardName) && c.getCurrentPrice() == 0){
//				c.setPrice(price);
//				break;
//			}
//		}
//	}
	
//	public void addPrice(String cardName, ArrayList<PriceWithDate> doubleList) {
//		if(cardName.contains("//")){
//			String[] cardNames = cardName.split("//");
//			cardName = cardNames[0].trim();
//			addPrice(cardNames[1].trim(), doubleList);
//		}
//		if(cardName.contains("(")){
//			cardName = cardName.substring(0, cardName.indexOf('(')).trim();
//		}
//		for(Card c : cards){
//			if(c.getName().replace("Æ", "Ae").replace("ö", "o").equals(cardName) && c.getPriceHistory().size() == 0){
//				c.setPriceHistory(doubleList);
//				break;
//			}
//		}
//	}
	
	public String getSetCode() { return setCode; }
	public void setSetCode(String setCode) { this.setCode = setCode; }
	
	@Override
	public boolean equals(Object o){
		if(o == null){return false;}
		if(o instanceof Set){
			return setName.equals(((Set)o).setName) && setCode.equals(((Set)o).setCode);
		}
		return false;
	}
}
