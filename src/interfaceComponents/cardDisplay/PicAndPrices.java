package interfaceComponents.cardDisplay;

import interfaceComponents.Panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import util.Card;
import util.CardWithQuantity;

/**
 * A panel that displays card images
 * @author Mark Wiggans
 */
public class PicAndPrices extends Panel{
	private CardDisplay cardDisplay;
	private Card currentCard;
	private static PicAndPrices picAndPrices;
	
	private PicAndPrices(){
		super(true);
		panel = new JPanel();
		cardDisplay = new CardDisplay();
		panel.setLayout(new BorderLayout());
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
			cardDisplay.setCard(c);
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
