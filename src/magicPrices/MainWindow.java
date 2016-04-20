package magicPrices;

import interfaceComponents.CollectionPanel;
import interfaceComponents.Panel;
import interfaceComponents.TradePanel;
import interfaceComponents.cardDisplay.PicAndPrices;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import util.DataParser;

public class MainWindow{
	private JTabbedPane tabbedPane;
	private JFrame frame;
	private DataParser parser;
	private PicAndPrices picAndPrices;
	
	private static MainWindow instance; 
	
	public static MainWindow getInstance(){
		if(instance == null){
			instance = new MainWindow();
		}
		return instance;
	}
	
	/**
	 * Revalidates the frame
	 */
	public static void refresh(){
		getInstance().frame.revalidate();
	}
	
	/**
	 * Creates a new MainWindow object
	 */
	private MainWindow(){
		frame = new JFrame("Magic Collection Manager");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(500, 500));
		
		tabbedPane = new JTabbedPane();
		frame.add(tabbedPane, BorderLayout.CENTER);
		
		parser = DataParser.getInstance();
		try {
			parser.parseData();
		} catch (IOException e) {
			System.out.println("Could not find the AllSets.json file");
			e.printStackTrace();
		}
		
		MenuBar bar = new MenuBar();
		frame.getContentPane().add(bar, BorderLayout.NORTH);
		
		picAndPrices = PicAndPrices.getInstance();
		frame.add(picAndPrices.getPanel(), BorderLayout.EAST);
		addPanel(new CollectionPanel(), "Collection");
		addPanel(new TradePanel(), "Trades");
		frame.revalidate();
		frame.setVisible(true);
	}
	
	public void addPanel(Panel pan, String panelName){
		tabbedPane.add(panelName, pan.getPanel());
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
	
	public void repaint() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				tabbedPane.repaint();
			}
		});
	}
	
	/**
	 * The menu bar along the top of the window
	 * @author Mark
	 */
	class MenuBar extends JMenuBar {
		private static final long serialVersionUID = 1L;

		public MenuBar(){
			init();
		}
		
		private void init(){
			JMenu file = new JMenu("File");
			this.add(file);
			
//			JMenuItem updatePrices = new JMenuItem("Update Prices");
//			updatePrices.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					Thread t = new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							PriceScraper scraper = new PriceScraper(parser);
//							scraper.getAllPrices();
//							try {
//								scraper.exportCurrentData();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//					t.run();
//				}
//			});
			//file.add(updatePrices);
		}
	}
}
