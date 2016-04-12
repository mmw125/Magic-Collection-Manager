package magicPrices;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import prices.PriceInfo;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class extends SpiceRequest for the type PriceInfo, and is used to fetch
 * and cache price info asynchronously
 */
public class PriceFetchRequest {

	private static final int MAX_NUM_RETRIES = 8;
	private final String mCardName;
	private final String mSetCode;
	private int mMultiverseID;
	private String mCardType;
	private String mCardNumber;

	/**
	 * Default constructor
	 *
	 * @param cardName
	 *            The name of the card to look up
	 * @param setCode
	 *            The set code (not TCG name) of this card's set
	 * @param cardNumber
	 *            The collector's number of the card to look up
	 * @param multiverseID
	 *            The multiverse ID of the card to look up
	 */
	public PriceFetchRequest(String cardName, String setCode, String cardNumber, int multiverseID) {
		this.mCardName = cardName;
		this.mSetCode = setCode;
		this.mCardNumber = cardNumber;
		this.mMultiverseID = multiverseID;
	}

	/**
	 * This function takes a string of XML information and parses it into a
	 * Document object in order to extract prices
	 *
	 * @param xml
	 *            The String of XML
	 * @return a Document describing the XML
	 * @throws ParserConfigurationException
	 *             thrown by factory.newDocumentBuilder()
	 * @throws SAXException
	 *             thrown by builder.parse()
	 * @throws IOException
	 *             thrown by builder.parse()
	 */
	private static Document loadXMLFromString(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	/**
	 * This runs as a service, builds the TCGplayer.com URL, fetches the data,
	 * and parses the XML
	 *
	 * @return a PriceInfo object with all the prices
	 * @throws SpiceException
	 *             If anything goes wrong with the database, URL, or connection,
	 *             this will be thrown
	 */
	public PriceInfo loadDataFromNetwork() {
		int retry = MAX_NUM_RETRIES;
		while (retry > 0) {
			try {
				/* If the card number wasn't given, figure it out */
				if (mCardNumber == null || mCardNumber.equals("") || mCardType == null || mCardType.equals("")
						|| mMultiverseID == -1) {
					  //fetchCardByNameAndSet(mCardName, mSetCode, CardDbAdapter.allCardDataKeys,

					if (mCardNumber == null || mCardNumber.equals("")) {
						//mCardNumber = c.getString(c.getColumnIndex(CardDbAdapter.KEY_NUMBER));
					}

					if (mCardType == null || mCardType.equals("")) {
						//mCardType = CardDbAdapter.getTypeLine(c);
					}

					if (mMultiverseID == -1) {
						//getMultiverseIdFromNameAndSet(mCardName, mSetCode, database);
					}
				}

				//String tcgName = CardDbAdapter.getTcgName(mSetCode, database);
				
				/*
				 * Figure out the tcgCardName, which is tricky for split cards
				 */
				String tcgCardName;

				/* Set up retries for multicard ordering */
				if (true) { //multiCardType != CardDbAdapter.MultiCardType.NOPE) {
					/* Next time try the other order */
					switch (retry % (MAX_NUM_RETRIES / 2)) {
					case 0:
						/* Try just the a side */
						//tcgCardName = CardDbAdapter.getNameFromSetAndNumber(mSetCode, mCardNumber.replace("b", "a"), database);
						break;
					case 3:
						/* Try just the b side */
						//tcgCardName = CardDbAdapter.getNameFromSetAndNumber(mSetCode, mCardNumber.replace("a", "b"), database);
						break;
					case 2:
						/* Try the combined name in one direction */
						//tcgCardName = CardDbAdapter.getSplitName(mMultiverseID, true, database);
						break;
					case 1:
						/* Try the combined name in the other direction */
						//tcgCardName = CardDbAdapter.getSplitName(mMultiverseID, false, database);
						break;
					default:
						/* Something went wrong */
						tcgCardName = mCardName;
						break;
					}
				} else {
					/* This isn't a multicard */
					tcgCardName = mCardName;
				}

				/* Retry with accent marks removed */
				if (retry <= MAX_NUM_RETRIES / 2) {
					tcgCardName = PriceFetchRequest.removeAccentMarks(tcgCardName);
				}

				/* Build the URL */
				URL priceUrl = new URL("http://partner.tcgplayer.com/x3/phl.asmx/p?pk=MTGFAMILIA&s="
						+ URLEncoder.encode(tcgName.replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8") + "&p="
						+ URLEncoder.encode(tcgCardName.replace(Character.toChars(0xC6)[0] + "", "Ae"), "UTF-8")
						+ URLEncoder.encode((mCardType.startsWith("Basic Land") ? " (" + mCardNumber + ")" : ""),
								"UTF-8"));

				/* Fetch the information from the web */
				String result = IOUtils.toString(FamiliarActivity.getHttpInputStream(priceUrl, null));

				/* Parse the XML */
				Document document = loadXMLFromString(result);
				Element element = document.getDocumentElement();

				PriceInfo pi = new PriceInfo();
				pi.mLow = Double.parseDouble(getString("lowprice", element));
				pi.mAverage = Double.parseDouble(getString("avgprice", element));
				pi.mHigh = Double.parseDouble(getString("hiprice", element));
				pi.mFoilAverage = Double.parseDouble(getString("foilavgprice", element));
				pi.mUrl = getString("link", element);

				/*
				 * Some cards, like FTV, only have a foil price. This fixed
				 * problems down the road
				 */
				if (pi.mLow == 0 && pi.mAverage == 0 && pi.mHigh == 0 && pi.mFoilAverage != 0) {
					pi.mLow = pi.mFoilAverage;
					pi.mAverage = pi.mFoilAverage;
					pi.mHigh = pi.mFoilAverage;
				}
				return pi;

				/* If this is a single card, skip over a bunch of retry cases */
				if (retry == MAX_NUM_RETRIES && multiCardType == CardDbAdapter.MultiCardType.NOPE) {
					retry = 2;
				}
			} catch (IOException e) {
				exception = new SpiceException(e.getLocalizedMessage());
			}
			retry--;
		}
	}

	/**
	 * Get a string value out of an Element given a tag name
	 *
	 * @param tagName
	 *            The name of the XML tag to extract a string from
	 * @param element
	 *            The Element containing XML information
	 * @return The String in the XML with the corresponding tag
	 */
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
	
	public static String removeAccentMarks(String s) {
        return s.replace(Character.toChars(0xC0)[0] + "", "A")
                .replace(Character.toChars(0xC1)[0] + "", "A")
                .replace(Character.toChars(0xC2)[0] + "", "A")
                .replace(Character.toChars(0xC3)[0] + "", "A")
                .replace(Character.toChars(0xC4)[0] + "", "A")
                .replace(Character.toChars(0xC5)[0] + "", "A")
                .replace(Character.toChars(0xC6)[0] + "", "Ae")
                .replace(Character.toChars(0xC7)[0] + "", "C")
                .replace(Character.toChars(0xC8)[0] + "", "E")
                .replace(Character.toChars(0xC9)[0] + "", "E")
                .replace(Character.toChars(0xCA)[0] + "", "E")
                .replace(Character.toChars(0xCB)[0] + "", "E")
                .replace(Character.toChars(0xCC)[0] + "", "I")
                .replace(Character.toChars(0xCD)[0] + "", "I")
                .replace(Character.toChars(0xCE)[0] + "", "I")
                .replace(Character.toChars(0xCF)[0] + "", "I")
                .replace(Character.toChars(0xD0)[0] + "", "D")
                .replace(Character.toChars(0xD1)[0] + "", "N")
                .replace(Character.toChars(0xD2)[0] + "", "O")
                .replace(Character.toChars(0xD3)[0] + "", "O")
                .replace(Character.toChars(0xD4)[0] + "", "O")
                .replace(Character.toChars(0xD5)[0] + "", "O")
                .replace(Character.toChars(0xD6)[0] + "", "O")
                .replace(Character.toChars(0xD7)[0] + "", "x")
                .replace(Character.toChars(0xD8)[0] + "", "O")
                .replace(Character.toChars(0xD9)[0] + "", "U")
                .replace(Character.toChars(0xDA)[0] + "", "U")
                .replace(Character.toChars(0xDB)[0] + "", "U")
                .replace(Character.toChars(0xDC)[0] + "", "U")
                .replace(Character.toChars(0xDD)[0] + "", "Y")
                .replace(Character.toChars(0xE0)[0] + "", "a")
                .replace(Character.toChars(0xE1)[0] + "", "a")
                .replace(Character.toChars(0xE2)[0] + "", "a")
                .replace(Character.toChars(0xE3)[0] + "", "a")
                .replace(Character.toChars(0xE4)[0] + "", "a")
                .replace(Character.toChars(0xE5)[0] + "", "a")
                .replace(Character.toChars(0xE6)[0] + "", "ae")
                .replace(Character.toChars(0xE7)[0] + "", "c")
                .replace(Character.toChars(0xE8)[0] + "", "e")
                .replace(Character.toChars(0xE9)[0] + "", "e")
                .replace(Character.toChars(0xEA)[0] + "", "e")
                .replace(Character.toChars(0xEB)[0] + "", "e")
                .replace(Character.toChars(0xEC)[0] + "", "i")
                .replace(Character.toChars(0xED)[0] + "", "i")
                .replace(Character.toChars(0xEE)[0] + "", "i")
                .replace(Character.toChars(0xEF)[0] + "", "i")
                .replace(Character.toChars(0xF1)[0] + "", "n")
                .replace(Character.toChars(0xF2)[0] + "", "o")
                .replace(Character.toChars(0xF3)[0] + "", "o")
                .replace(Character.toChars(0xF4)[0] + "", "o")
                .replace(Character.toChars(0xF5)[0] + "", "o")
                .replace(Character.toChars(0xF6)[0] + "", "o")
                .replace(Character.toChars(0xF8)[0] + "", "o")
                .replace(Character.toChars(0xF9)[0] + "", "u")
                .replace(Character.toChars(0xFA)[0] + "", "u")
                .replace(Character.toChars(0xFB)[0] + "", "u")
                .replace(Character.toChars(0xFC)[0] + "", "u")
                .replace(Character.toChars(0xFD)[0] + "", "y")
                .replace(Character.toChars(0xFF)[0] + "", "y");
    }
}