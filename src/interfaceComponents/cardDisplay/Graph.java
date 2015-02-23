package interfaceComponents.cardDisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import util.PriceWithDate;

/**
 * Displays the price history from a given card or set of cards
 * @author Mark
 */
public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<ArrayList<PriceWithDate>> data;
	private Point offset = new Point();

	public Graph() {
		data = new ArrayList<ArrayList<PriceWithDate>>();
		setBackground(Color.WHITE);
		offset.x = 0;
		offset.y = 0;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setDoubleBuffered(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		float width = 3.0F;
		g2d.setColor(Color.darkGray);
		g2d.setStroke(new BasicStroke(width));
		if(data != null && data.size() != 0 && data.get(0).size() != 0){
			double highestPrice = data.get(0).get(0).getPrice();
			double lowestPrice = data.get(0).get(0).getPrice();
			for (ArrayList<PriceWithDate> currentData : data) {
				for(PriceWithDate pwd : currentData){
					if(pwd.getPrice() > highestPrice){
						highestPrice = pwd.getPrice();
					}else if(pwd.getPrice() < lowestPrice){
						lowestPrice = pwd.getPrice();
					}
				}
			}
			highestPrice = highestPrice + (highestPrice * .1);
			lowestPrice = lowestPrice - (lowestPrice * .1);
			double spread = highestPrice - lowestPrice;
			for (ArrayList<PriceWithDate> currentData : data) {
				for (int i = 0; i < currentData.size(); i++) {
					int x1 = (int) (i * 5);
					double price = currentData.get(i).getPrice();
					int y1 = getHeight() -(int) ((price - lowestPrice) / spread * getHeight());
					g2d.drawRect(x1 - 2, y1 - 2, 4, 4);
					if (i != 0) {
						int x0 = (int) ((i - 1) * 5);
						int y0 = getHeight() -(int) ((currentData.get(i-1).getPrice() - lowestPrice) / spread * getHeight());
						g2d.drawLine(x0, y0, x1, y1);
					}
				}
			}
		}
	}

	public void paint() {
		paintComponent(getGraphics());
	}

	public void setData(ArrayList<PriceWithDate> newData) {
		this.data.clear();
		this.data.add(newData);
		paint();
	}

	public void addData(ArrayList<PriceWithDate> newData) {
		this.data.add(newData);
		paint();
	}
}