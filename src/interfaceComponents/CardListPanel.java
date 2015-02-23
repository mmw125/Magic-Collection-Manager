package interfaceComponents;

import interfaceComponents.cardDisplay.PicAndPrices;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.Card;
import util.CardWithQuantity;

/**
 * Shows a list of card with quantity objects
 * This has a associatedList normally from a SearchPanel that it pulls cards from along with setting
 * the picAndPrices to display that card. This also has a price display
 * @author Mark Wiggans
 */
public class CardListPanel extends Panel implements MouseListener, ListSelectionListener{
	private JList<Card> associatedList;
	private JList<CardWithQuantity> list;
	private DefaultListModel<CardWithQuantity> model;
	private JLabel totalPrice;
	
	/**
	 * Creates a new CardListPanel object with the given pAp
	 * @param pAp the associated pAp
	 */
	public CardListPanel(){
		this(null);
	}
	
	/**
	 * Creates a new CardListPanel object
	 * @param jList where to pull the cards from
	 * @param pAp where to display the cards when they are selected
	 */
	public CardListPanel(JList<Card> jList){
		super();
		this.associatedList = jList; 
		init();
	}
	
	/**
	 * Loads the panel
	 */
	private void init(){
		totalPrice = new JLabel("$0");
		panel.setLayout(new BorderLayout());
		model = new DefaultListModel<CardWithQuantity>();
		list = new JList<CardWithQuantity>(model);
		panel.add(list, BorderLayout.CENTER);
		panel.add(totalPrice, BorderLayout.NORTH);
		list.addListSelectionListener(this);
		addAssociatedListListener();
		list.addMouseListener(this);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
	}

	private void addAssociatedListListener(){
		if(associatedList != null){
			associatedList.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) { }
				
				@Override
				public void mousePressed(MouseEvent arg0) { }
				
				@Override
				public void mouseExited(MouseEvent arg0) { }
				
				@Override
				public void mouseEntered(MouseEvent arg0) { }
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(arg0.getClickCount() == 2){
						addElement(associatedList.getSelectedValue());
					}
				}
			});
		}
	}
	
	/**
	 * Sets the list that this list pulls from
	 * Most likely this will be a search
	 * @param list
	 */
	public void setAssociatedJList(JList<Card> list){
		if(associatedList != null){
			associatedList.removeMouseListener(this);
		}
		associatedList = list;
		if(associatedList != null){
			associatedList.addMouseListener(this);
		}
	}
	
	/**
	 * Sets the price associated with the panel
	 */
	public void updatePrice(){
		double price = 0;
		for(int i = 0; i < model.getSize(); i++){
			price += model.getElementAt(i).getPrice();
		}
		price = Math.round(price);
		totalPrice.setText("$"+price);
	}
		
	/**
	 * Gets the cards in the list
	 * @return the list containing the cards in the list
	 */
	public JList<CardWithQuantity> getList(){
		return list;
	}
	
	/**
	 * Gets the list model
	 * @return
	 */
	public DefaultListModel<CardWithQuantity> getModel(){
		return model;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount() == 2){
			if(picAndPrices != null){
				picAndPrices.setCard(list.getSelectedValue());
			}
			list.getSelectedValue().decrementQuantity();
			if(list.getSelectedValue().getQuantity() <= 0){
				model.removeElement(list.getSelectedValue());
			}
			updatePrice();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(picAndPrices != null){
			picAndPrices.setCard(list.getSelectedValue());
		}
	}

	/**
	 * Adds a card element to the internal array
	 * @param card
	 */
	public void addElement(Card card) {
		if(card != null){
			boolean alreadyExists = false;
			for(int i = 0; i < model.getSize(); i++){
				if(model.getElementAt(i).getCard().equals(card)){
					model.getElementAt(i).increamentQuantity();
					alreadyExists = true;
				}
			}
			if(!alreadyExists){
				model.addElement(new CardWithQuantity(card));
			}
			list.revalidate();
			updatePrice();
		}
	}
}
