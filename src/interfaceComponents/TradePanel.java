package interfaceComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Allows you to easily add cards to two CardListPanels and
 * compare the prices between the two
 * @author Mark Wiggans
 */
public class TradePanel extends Panel{
	private JPanel container;
	private SearchPanel searchPanel;
	private CardListPanel list1;
	private CardListPanel list2;
	
	/**
	 * Creates a new TradePanel
	 */
	public TradePanel(){
		super();

		searchPanel = new SearchPanel();
		panel.setLayout(new BorderLayout());
		panel.add(searchPanel.getPanel(), BorderLayout.WEST);
		container = new JPanel();
		panel.add(container, BorderLayout.CENTER);
		container.setLayout(new GridLayout(2, 1));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		container.add(panel1);
		list1 = new CardListPanel();
		JButton button1 = new JButton("+");
		
		panel1.add(button1, BorderLayout.WEST);
		panel1.add(list1.getPanel(), BorderLayout.CENTER);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				list1.addElement(searchPanel.getList().getSelectedValue());
			}
		});
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		container.add(panel2);
		list2 = new CardListPanel();
		JButton button2 = new JButton("+");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				list2.addElement(searchPanel.getList().getSelectedValue());
			}
		});
		panel2.add(button2, BorderLayout.WEST);
		panel2.add(list2.getPanel(), BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar, BorderLayout.NORTH);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
	}
}
