package system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import theme.Utilities;
import theme.CButton;
import theme.CLabel;
import theme.Colors;
import theme.SigMethods;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboard extends DefaultDashboard implements ActionListener {

	Connection dbconn = DBConnection.connectDB();
	CLabel gradeLevelLabel;
	CButton editGLevelBtn;

	public StudentDashboard() {

		if (dbconn != null) {
			try {
				String sql = "SELECT hasAddedGradeLevel FROM students WHERE studentId = '" + Login.userId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				if (res.next()) {
					boolean hasAdded = res.getBoolean("hasAddedGradeLevel");
					if (hasAdded == false) {
						SigMethods.enableGlassPane(dashboardFrame);
						new StudentEnterGradeLevel();
					} else
						System.out.println("has added");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String gradeLevel1 = null;
		if (dbconn != null) {
			try {
				String sql = "SELECT gradeLevel FROM students WHERE studentId = '" + Login.userId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				while (res.next()) {
					gradeLevel1 = res.getString("gradeLevel");

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		northFillerPanel.setPreferredSize(new Dimension(0, 50));
		northFillerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		northFillerPanel.add(new CLabel("Hello, " + Login.userName + " - " + gradeLevel1));
		viewSchedBasePanel.contentBasePanel.add(showScheduleTable());
		String gradeLevel = null;
		if (dbconn != null) {
			try {

				String sql = "SELECT gradeLevel FROM students WHERE studentId = '" + Login.userId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				while (res.next()) {
					gradeLevel = res.getString("gradeLevel");

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		gradeLevelLabel = new CLabel("Grade Level: " + gradeLevel);

		accC.add(gradeLevelLabel);
		editGLevelBtn = new CButton("Edit Grade level");
		editGLevelBtn.addActionListener(this);
		accC.add(editGLevelBtn);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == editGLevelBtn) {

			SigMethods.enableGlassPane(dashboardFrame);
			new EditGradeLevel();
		} else if (e.getSource() == viewSchedBtn) {
			panelSwitch(viewSchedBasePanel);
		}

	}

	private JScrollPane showScheduleTable() {
		JTable scheduleTable = new JTable();
		scheduleTable.setDefaultEditor(Object.class, null);
		scheduleTable.setBackground(Colors.LIGHT);
		scheduleTable.setFont(myFont);
		scheduleTable.setForeground(Colors.DARK);
		scheduleTable.setFillsViewportHeight(true);
		scheduleTable.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = scheduleTable.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		scheduleTable.setModel(model);

		JScrollPane scrollPane = new JScrollPane(scheduleTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		if (dbconn != null) {
			try {
				String gradeLevel = null;
				String gradeLvl = null;
				String sql = "SELECT gradeLevel FROM students WHERE studentId = '" + Login.userId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				if (res.next()) {
					gradeLevel = res.getString("gradeLevel");
					if (gradeLevel==null) {
						System.out.println("a");
					} else {
						String arr[] = gradeLevel.split(" ", 2);
						gradeLvl = arr[1];
						System.out.println(gradeLvl);
						// load table data
						try {

							String getTable = "SELECT * FROM grade" + gradeLvl + "sched";
							System.out.println(getTable);
							Statement st1 = dbconn.createStatement();
							ResultSet res1 = st1.executeQuery(getTable);
							while (res1.next()) {
								String timeRange = res1.getString("timeRange");
								String mon = res1.getString("mon");
								String tue = res1.getString("tue");
								String wed = res1.getString("wed");
								String thu = res1.getString("thu");
								String fri = res1.getString("fri");
								model.addRow(new Object[] { timeRange, mon, tue, wed, thu, fri });

							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				} else {
					System.out.println("Student still hasnt followed up");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		adjustTableSizeAccordingToContentSched(scheduleTable);
		return scrollPane;
	}

	private void setTableRowHeightsSched(JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			int rowHeight = table.getRowHeight();

			for (int column = 0; column < table.getColumnCount(); column++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
				if (row == 0 || row == 2 || row == 4 || row == 6) {
					rowHeight = Math.max(rowHeight + 20, comp.getPreferredSize().height);
				}
				if (row == 1 || row == 5) {
					rowHeight = Math.max(rowHeight + 5, comp.getPreferredSize().height);
				}
				if (row == 3) {
					rowHeight = Math.max(rowHeight + 10, comp.getPreferredSize().height);
				}

			}

			table.setRowHeight(row, rowHeight);
		}
	}

	public void adjustTableSizeAccordingToContentSched(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		TableCellRenderer renderer;
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 10; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				if (column == 0) {
					width = width - 10;
				}
				width = Math.max(comp.getPreferredSize().width + 1, width);
				table.getColumnModel().getColumn(column).setCellRenderer(new WordWrapCellRenderer());
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
		setTableRowHeightsSched(table);
	}

	static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
		WordWrapCellRenderer() {

			setLineWrap(true);
			setWrapStyleWord(true);
			setFont(new Font(fontString, 0, 22));
			setBackground(Color.white);
			setForeground(Colors.DARK);
			setMargin(new Insets(10, 10, 10, 10));

		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value.toString());

			return this;
		}
	}

}
