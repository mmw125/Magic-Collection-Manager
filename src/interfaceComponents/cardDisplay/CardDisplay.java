package interfaceComponents.cardDisplay;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.Card;

/**
 * Displays card images downloaded from mtgimage.com
 * 
 * @author Mark Wiggans
 */
public class CardDisplay extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private Card card;
	private Image image;
	private Dimension cardSize = new Dimension(480, 680);
	private static final String CACHE_LOCATION = "cache/imageCache/";

	/**
	 * Creates a new CardDisplay without a card
	 */
	public CardDisplay() {
		super();
		setPreferredSize(new Dimension(480, 680));
	}

	/**
	 * Creates a new CardDisplay with a card
	 * 
	 * @param c
	 */
	public CardDisplay(Card c) {
		this();
		card = c;
	}

	/**
	 * Sets the height of the card display then generates a width
	 * 
	 * @param height
	 */
	public void setCardHeight(int height) {
		double width = (int) ((12 / 17) * height);
		System.out.println(width + ", " + height);
		setCardSize(new Dimension((int) width, height));
	}

	/**
	 * Sets the size of the card to the given size
	 * 
	 * @param d
	 *            the new size of the card
	 */
	public void setCardSize(Dimension d) {
		cardSize = d;
		try {
			if (card != null) {
				getImage(card);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the card to be painted in the window This action is run in a
	 * different thread because fetching the image might take a bit of time
	 * 
	 * @param c
	 *            the card
	 */
	public void setCard(Card c) {
		card = c;
		new Thread(this).run();
	}

	/**
	 * Gets the image and paints it
	 */
	@Override
	public void run() {
		image = null;
		try {
			image = getImage(card);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				repaint();
			}
		});

	}

	/**
	 * Calls paint()
	 */
	public void repaint() {
		paint(getGraphics());
	}

	/**
	 * Paints the image on the canvas
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}

	/**
	 * Acquires the image for the given card. First, it checks if the file
	 * already exists in the image cache. Then, it checks the offical gatherer website
	 * at http://gatherer.wizards.com/. If that fails, it attempts to get it from
	 * http://magiccards.info/. If that fails, it returns null
	 * @param c the card whose image to get
	 * @return the card's image
	 * @throws IOException
	 */
	private Image getImage(Card c) throws IOException {
		File f = new File(CACHE_LOCATION + c.getName() + "-" + c.getSet().getCode() + ".jpg");
		if (f.exists()) {
			return ImageIO.read(f).getScaledInstance((int) cardSize.getWidth(), (int) cardSize.getHeight(), 0);
		} else {
			URL url = new URL("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + c.getMultiverseID() + "&type=card");
			InputStream is = null;
			try {
				is = url.openStream();
			} catch (Exception e) {
				url = new URL("http://magiccards.info/scans/en/"+c.getSet()+"/"+c.getCollectorsNumber()+".jpg");
				try {
					is = url.openStream();
				} catch (Exception e2) {
					System.err.print("Could not access backup image");
					return null;
				}
			}
			OutputStream os = new FileOutputStream(f);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();
			return ImageIO.read(f).getScaledInstance(cardSize.width, cardSize.height, 0);
		}
	}
}
