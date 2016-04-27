package util;

import magicPrices.MainWindow;
import prices.PriceGetter;
import prices.PriceInfo;

public class Card {
	private int cmc;
	private String name;
	private String manaCost;
	private PriceInfo currentPrice = null;
	private Set set;
	private Rarity rarity;
	private int multiverseId;
	private String collectorsNumber = null;
	private boolean fetchingPrice = false;
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){ 
		return name;
	}
	public void setManaCost(String cost){ 
		manaCost = cost; 
	}
	public String getCost(){ return manaCost; }
	public void setCMC(int cmc){ this.cmc = cmc; }
	public int getCMC(){ return cmc; }
	public Set getSet(){ return set; }
	public void setRarity(Rarity r){ rarity = r; }
	public void setMultiverseID(int id){ multiverseId = id; }
	public int getMultiverseID() { return multiverseId; }
	public void setCollectorsNumber(String id){ collectorsNumber = id; }
	public String getCollectorsNumber() { return collectorsNumber; }
	public boolean onlineOnly(){ return set.getOnlineOnly(); }
	public void setSet(Set set){ this.set = set; }
	
	public void setPrice(PriceInfo price) {
		currentPrice = price;
		System.out.println("Trying to update the window");
		MainWindow.getInstance().repaint();
	}
	
	public PriceInfo getCurrentPrice(){ 
		if(currentPrice == null) {
			if(!fetchingPrice) {
				PriceGetter.getCardPrice(this);
				fetchingPrice = true;
			}
			return null;
		}
		return currentPrice; 
	}
	
	public String toString(){
		PriceInfo currentPrice = getCurrentPrice();
		if(currentPrice != null){
			return set.getSetCode()+" "+name + " " + currentPrice.mAverage;
		}else{
			return set.getSetCode()+" "+name; 
		}
	}
	public boolean equals(Object o){
		if(o == null){ return false; }
		if(o instanceof Card){
			Card c = (Card)o;
			return c.name.equals(name) && c.set.equals(set);
		}
		if(o instanceof CardWithQuantity){
			return equals(((CardWithQuantity)o).getCard());
		}
		return false;
	}
	
	public boolean isUncommon(){
		return rarity == Rarity.UNCOMMON;
	}
	
	public boolean isCommon(){
		return rarity == Rarity.COMMON;
	}
	
	public boolean isUncommonOrCommon() {
		return isUncommon() || isCommon();
	}
	public boolean isBasic() {
		return name.equals("Plains") || name.equals("Island") || name.equals("Swamp") || name.equals("Mountain") || name.equals("Forest");
	}
}
