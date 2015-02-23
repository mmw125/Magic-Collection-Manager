package interfaceComponents;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import util.Card;
import util.CardWithQuantity;
import util.DataParser;
import util.Set;

/**
 * A Panel that can search for cards and add them to a list
 * that is exported to the local file system
 * @author Mark Wiggans
 */
public class CollectionPanel extends Panel{
	private CardListPanel collectionPanel;
	private DataParser parser;
	private SearchPanel searchPanel;
	public CollectionPanel(){
		super();
		this.parser = DataParser.getInstance();
		
		searchPanel = new SearchPanel();
		panel.add(searchPanel.getPanel(), BorderLayout.WEST);
		
		collectionPanel = new CardListPanel(searchPanel.getList());
		
		try {
			importCollection();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		collectionPanel.getModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent arg0) {
				try {
					exportData();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void intervalAdded(ListDataEvent arg0) {
				try {
					exportData();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void contentsChanged(ListDataEvent arg0) {
				
			}
		});
		collectionPanel.getList().setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		panel.add(collectionPanel.getPanel(), BorderLayout.CENTER);
		panel.revalidate();
	}
	
	/**
	 * Imports the collection into the list
	 * @throws FileNotFoundException
	 */
	public void importCollection() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("collection.txt"));
		while(scanner.hasNext()){
			String s = scanner.nextLine();
			if(s != null && s != ""){
				CardWithQuantity card = parseString(s);
				if(card != null){
					collectionPanel.getModel().addElement(parseString(s));
				}
			}
		}
		collectionPanel.getList().revalidate();
		collectionPanel.updatePrice();
		scanner.close();
	}
	
	/**
	 * Exports the data in the list into a text file
	 * @throws IOException
	 */
	public void exportData() throws IOException{
		File f = new File("collection.txt");
		f.createNewFile();
		PrintWriter writer = new PrintWriter(f);
		for(int i = 0; i < collectionPanel.getList().getModel().getSize(); i++){
			writer.println(collectionPanel.getList().getModel().getElementAt(i).getQuantity()+"~"+collectionPanel.getList().getModel().getElementAt(i).getCard().getName()+"~"+collectionPanel.getList().getModel().getElementAt(i).getCard().getSet().getCode());
		}
		writer.close();
		System.out.println("Exported");
	}
	
	/**
	 * Turns a string with ~ delimiters into a card with quantity
	 * @param input the given text
	 * @return a cardwithquantity object based off 
	 */
	public CardWithQuantity parseString(String input){
		System.out.println(input);
		String[] splitInput = input.split("~");
		for(Set s : parser.getSets()){
			if(splitInput[2].equals(s.getCode())){
				for(Card c : s.getCards()){
					if(c.getName().equals(splitInput[1])){
						return new CardWithQuantity(c, Integer.parseInt(splitInput[0]));
					}
				}
				break;
			}
		}
		return null;
	}
}
