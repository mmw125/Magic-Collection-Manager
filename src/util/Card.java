package util;

import prices.PriceInfo;

public class Card{
	private int cmc;
	private String name;
	private String manaCost;
	private PriceInfo currentPrice = null;
	private Set set;
	private Rarity rarity;
	private int multiverseId;
	private String collectorsNumber = null;
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
	public void setPrice(PriceInfo price){ currentPrice = price; }
	public PriceInfo getCurrentPrice(){ return currentPrice; }
	public String toString(){
		if(currentPrice != null){
			return set.getSetCode()+" "+name + " " + currentPrice;
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
