
/**
 * Created by Jens on 20-Mar-16.
 */
/*
class GenericSearch extends JPanel {
	//This is a generic search tab with button, which will show results in a popup window
	private String[][] searchTable;
	private String[][] table;
	private String[] titles;
	private int x;
	private int y;
	private SQL sql = new SQL();
	public GenericSearch(String query, String[] titles) {
		table = sql.getStringTable(query, false);
		this.titles = titles;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		x = (int) (screen.width * 0.75);
		y = (int) (screen.height * 0.75);
		setLayout(new BorderLayout());
		JTextField search = new JTextField();
		JButton searcher = new JButton("Search");
		searcher.setToolTipText("Search for any entry and display all matches in a separate window.");
		Action searchPress = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> rowAdded = new ArrayList<Integer>();
				searchTable = new String[table.length][table[0].length];
				for (int i = 0; i < table.length; i++) {
					for (int j = 0; j < table[i].length; j++) {
						if (table[i][j] == null) {
							table[i][j] = "";
						}
						if (!(rowAdded.contains(i)) && table[i][j].toLowerCase().contains(search.getText().toLowerCase())) {
							int k = 0;
							boolean added = false;
							for (int l = 0; l < searchTable.length; l++) {
								while (!added && k < searchTable[0].length) {
									if (searchTable[k][0] == null || searchTable[k][0].isEmpty()) {
										searchTable[k] = table[i];
										added = true;
										rowAdded.add(i);
									} else {
										k++;
									}
								}
							}
						}
					}
				}
				searchWindow window = new searchWindow();
			}
		};
		search.addActionListener(searchPress);
		searcher.addActionListener(searchPress);
		add(search, BorderLayout.CENTER);
		add(searcher, BorderLayout.EAST);
	}

	private class searchWindow extends JFrame {
		public searchWindow() {
			setSize((int) (x * 0.4), (int) (y * 0.4));
			setTitle("Search results");
			setAlwaysOnTop(true);
			JTable searchTab = new JTable(searchTable, titles);
			JScrollPane scroll = new JScrollPane(searchTab);
			searchTab.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						String[] selected = table[searchTab.getSelectedRow()];
						for (int i = 0; i < table.length; i++) {
							if (selected.equals(table[i])) {
								if (GenericSearch.this instanceof EmployeeTab.EmployeeSearch) {
									GenericList.searchEditWindow edit = new GenericList.searchEditWindow(table[i], i);
								}
							}
						}
					}
				}
			});
			add(scroll, BorderLayout.CENTER);
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}
}
*/