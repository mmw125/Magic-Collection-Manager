package interfaceComponents.cardDisplay;

import interfaceComponents.Panel;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import util.Card;
import util.CardWithQuantity;

/**
 * Shows a picture of a selected card and 
 * @author Mark Wiggans
 *
 */
public class PicAndPrices extends Panel{
	private CardDisplay cardDisplay;
	private Graph calc;
	private Card currentCard;
	private static PicAndPrices picAndPrices;
	
	private PicAndPrices(){
		super(true);
		panel = new JPanel();
		calc = new Graph();
		cardDisplay = new CardDisplay();
		panel.setLayout(new BorderLayout());
		panel.add(calc, BorderLayout.CENTER);
		panel.add(cardDisplay, BorderLayout.NORTH);
	}
	
	public static PicAndPrices getInstance(){
		if (picAndPrices == null){
			picAndPrices = new PicAndPrices();
		}
		return picAndPrices;
	}
	
	public void setCard(Card c) {
		if(c != null){
			if(currentCard != null && currentCard.equals(c)){
				return;
			}
			currentCard = c;
			calc.setData(c.getPriceHistory());
			cardDisplay.setCard(c);
		}
	}
	
	public void setCards(ArrayList<Card> cards){
		if(cards != null){
			for(int i = 0; i < cards.size(); i++){
				if(i == 0){
					calc.setData(cards.get(0).getPriceHistory());
				}else{
					calc.addData(cards.get(i).getPriceHistory());
				}
			}
		}
	}
	
	public void setCard(CardWithQuantity c){
		if(c != null){
			setCard(c.getCard());
		}else{
			System.err.println("CardWithQuantity is null");
		}
	}
}
