package prices;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import util.Card;

public class PriceGetter implements Runnable {
	private static PriceGetter instance;
	private Thread t;
	private WebClient webClient = null;
	private Vector<Card> cardsToGet;
	private boolean shutdown;
	
	private PriceGetter() {
		webClient = new WebClient();
		cardsToGet = new Vector<Card>();
		shutdown = false;
		t = new Thread(this);
		t.start();
	}
	
	private static PriceGetter getInstance() {
		if(instance == null) {
			instance = new PriceGetter();
		}
		return instance;
	}
	
	public static void shutdown() {
		getInstance().cardsToGet.clear();
		getInstance().shutdown = true;
	}
	
	/**
	 * Gets the card price 
	 * @param card the card to update with its price
	 */
	public static void getCardPrice(Card card) {
		getInstance().cardsToGet.addElement(card);
	}
	
	private static String nameChanges(String setName){
		switch (setName){
			case "Modern Masters 2015 Edition": return "Modern Masters 2015"; 
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

	@Override
	public void run() {
		while(!shutdown) {
			if(cardsToGet.size() == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			} else {
				Card c = cardsToGet.get(0);
				cardsToGet.remove(0);
				updateCardPrice(c);
			}
		}
	}
	
	
	private void updateCardPrice(Card card) {
		URL priceUrl = null;
		try {
			priceUrl = new URL("http://partner.tcgplayer.com/x3/phl.asmx/p?pk=MTGFAMILIA&s="
					+ URLEncoder.encode(PriceGetter.nameChanges(card.getSet().getName()).replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8") + "&p="
					+ URLEncoder.encode(card.getName().replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8")
					+ URLEncoder.encode((card.isBasic() ? " (" + card.getCollectorsNumber() + ")" : ""), "UTF-8"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		XmlPage page = null;
        try {
            page = webClient.getPage(priceUrl);
        } catch (Exception e) {
            System.out.println("Invalid web address");
            e.printStackTrace();
            card.setPrice(new PriceInfo());
            return;
        }

		/* Parse the XML */
		Document document = page.getOwnerDocument();
		Element element = document.getDocumentElement();

		PriceInfo pi = new PriceInfo();
		pi.mLow = Double.parseDouble(getString("lowprice", element));
		pi.mAverage = Double.parseDouble(getString("avgprice", element));
		pi.mHigh = Double.parseDouble(getString("hiprice", element));
		pi.mFoilAverage = Double.parseDouble(getString("foilavgprice", element));
		pi.mUrl = getString("link", element);
		card.setPrice(pi);
	}
	
	private String getString(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null) {
				String returnValue = "";
				for (int i = 0; i < subList.getLength(); i++) {
					returnValue += subList.item(i).getNodeValue();
				}
				return returnValue;
			}
		}
		return null;
	}
}
