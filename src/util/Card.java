package util;

public class Card{
	private int cmc;
	private String name;
	private String manaCost;
	private double currentPrice = 0;
	private Set set;
	private Rarity rarity;
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
	public void setRarity(Rarity r){
		rarity = r;
	}
	public boolean onlineOnly(){ return set.getOnlineOnly(); }
	public void setSet(Set set){ this.set = set; }
	public void setPrice(Double price){ currentPrice = price; }
	public double getCurrentPrice(){ return currentPrice; }
	public String toString(){
		if(currentPrice != 0){
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
}
