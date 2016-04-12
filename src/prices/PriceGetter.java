package prices;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import util.Card;

public class PriceGetter implements Runnable {
	private static WebClient webClient = null;
	private Card card;
	private PriceReciever rec;
	
	public PriceGetter(Card cardToGet, PriceReciever rec) {
		if(webClient == null) {
			webClient = new WebClient();
		}
		cardToGet = card;
		this.rec = rec;
		new Thread(this).run();
	}

	@Override
	public void run() {
		URL priceUrl = null;
		try {
			priceUrl = new URL("http://partner.tcgplayer.com/x3/phl.asmx/p?pk=MTGFAMILIA&s="
					+ URLEncoder.encode(card.getSet().getName().replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8") + "&p="
					+ URLEncoder.encode(card.getName().replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8")
					+ URLEncoder.encode((card.isBasic() ? " (" + card.getCollectorsNumber() + ")" : ""),
							"UTF-8"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		/* Fetch the information from the web */
		//String result = IOUtils.toString(FamiliarActivity.getHttpInputStream(priceUrl, null));
		HtmlPage page = null;
        try {
                page = webClient.getPage(priceUrl);
        } catch (Exception e) {
            System.out.println("Invalid web address");
            e.printStackTrace();
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
		rec.getPrice(card, pi);
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
