package interfaceComponents;

import interfaceComponents.cardDisplay.PicAndPrices;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * A generic panel that provides some basic utility
 * @author Mark Wiggans
 */
public abstract class Panel {
	
	protected JPanel panel;
	protected PicAndPrices picAndPrices;
	
	/**
	 * Creates a new panel object
	 * @param pAp the correlating picture and prices
	 */
	public Panel(){
		this(false);
	}
	
	public Panel(boolean isPAP){
		if(!isPAP){
			picAndPrices = PicAndPrices.getInstance();
		}
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
	}
	
	/**
	 * Gets the panel that should be displayed
	 * @return the panel that should be displayed
	 */
	public JPanel getPanel() {
		return panel;
	}
	
	public void setPicAndPrices(PicAndPrices pAp){
		picAndPrices = pAp;
	}
	
	public PicAndPrices getPicAndPrices(){
		return picAndPrices;
	}
}
