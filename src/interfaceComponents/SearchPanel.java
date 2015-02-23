package interfaceComponents;

import interfaceComponents.cardDisplay.PicAndPrices;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.Card;
import util.DataParser;

/**
 * This class searches for cards using the database of cards in
 * the parser parameter. This is a fairly simple method and can't do
 * anything with the cards searched for by itself.
 * @author Mark Wiggans
 */
public class SearchPanel extends Panel implements ListSelectionListener{
	
	//Where the search query is entered
	private JTextArea textEnter;
	private DefaultListModel<Card> model;
	
	//The list of cards being displayed
	private JList<Card> list;
	
	//The list of cards to search from
	private ArrayList<Card> associatedList;
	
	/**
	 * Creates a new SearchPanel object with all of
	 * the cards in the DataParser
	 */
	public SearchPanel(){
		this(DataParser.getInstance().getCards());
	}
	
	/**
	 * Creates a new SearchPanel object
	 * @param associatedList the list to search from
	 * @param pAp displays the card images
	 */
	public SearchPanel(ArrayList<Card> associatedList){
		super();
		this.associatedList = associatedList;
		picAndPrices = PicAndPrices.getInstance();
		initPanel();
	}
	
	/**
	 * Initializes the panel
	 */
	public void initPanel(){
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		textEnter = new JTextArea();
		panel.add(textEnter, BorderLayout.NORTH);
		textEnter.addCaretListener(new CaretListener() {
			private String storedText;
			@Override
			public void caretUpdate(CaretEvent e) {
				if(storedText != textEnter.getText()){
					storedText = textEnter.getText();
					model.removeAllElements();
					if(storedText.length() > 0){
						list.removeAll();
						updateList(storedText);
					}
				}
				
			}
		});
		model = new DefaultListModel<Card>();
		list = new JList<Card>(model);
		list.addListSelectionListener(this);
//		list.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
		panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
	}
	
	/**
	 * Updates the list of cards with the given search string
	 * @param search
	 */
	public void updateList(String search){
		model.removeAllElements();
		for(Card c : searchForCards(search)){
			model.addElement(c);
		}
	}
	
	/**
	 * Gets the results from the current query
	 * @return where the results from the query are stored
	 */
	public JList<Card> getList(){
		return list;
	}

	/**
	 * Is run whenever the list selection has changed
	 * @param e basic event information
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		picAndPrices.setCard(list.getSelectedValue());
	}
	
	/**
	 * Given a string, searches for all cards that contain that string
	 * @param searchString the string to search for
	 * @return the first 50 cards that fit
	 */
	public ArrayList<Card> searchForCards(String searchString){
		return searchForCards(searchString, false);
	}
	
	/**
	 * Given a string, searches for all cards that contain that string
	 * @param searchString the string to search for
	 * @return the first 50 cards that fit
	 */
	public ArrayList<Card> searchForCards(String searchString, boolean allowOnline){
		ArrayList<Card> output = new ArrayList<Card>();
		for(Card c : associatedList){
			if(c.getName().toLowerCase().contains(searchString.toLowerCase())){
				if(allowOnline || !c.onlineOnly()){
					output.add(c);
				}
			}
			if(output.size() > 50){
				return output;
			}
		}
		return output;
	}
}
