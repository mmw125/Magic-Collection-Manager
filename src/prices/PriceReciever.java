package prices;

import util.Card;

public interface PriceReciever {
	public void getPrice(Card card, PriceInfo info);
}
