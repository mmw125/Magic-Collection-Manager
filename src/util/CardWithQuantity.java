package util;


public class CardWithQuantity{
	private Card card;
	private int quantity;
	private boolean isFoil;
	
	public CardWithQuantity(Card c){
		this(c, 1);
	}
	
	public CardWithQuantity(Card c, int quantity){
		this(c, quantity, false);
	}
	
	public CardWithQuantity(Card c, int quantity, boolean isFoil){
		card = c;
		this.quantity = quantity;
		this.isFoil = isFoil;
	}
	
	public Card getCard(){
		return card;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void increamentQuantity(){
		quantity++;
	}
	
	public void decrementQuantity(){
		quantity--;
	}
	
	public void setQuantity(int quan){
		quantity = quan;
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		output.append(quantity+" ");
		output.append(card.getSet().getCode()+" ");
		output.append(card.getName()+" ");
		if(isFoil){
			output.append("FOIL ");
		}
		output.append(getPrice());
		return output.toString();
	}
	
	public boolean equals(Object o){
		if(o == null){ return false; }
		if(o instanceof CardWithQuantity){
			return ((CardWithQuantity)o).card.equals(card);
		}
		if(o instanceof Card){
			return ((Card)o).equals(card);
		}
		return false;
	}
	
	public double getPrice(){
		if(card.getCurrentPrice() != null) {
			if(isFoil){
				return card.getCurrentPrice().mFoilAverage * quantity;
			}
			return card.getCurrentPrice().mAverage * quantity;
		}
		return 0;
	}
}
