package system;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import theme.CButton;
import theme.CLabel;
import theme.Colors;
import theme.SigMethods;
import java.awt.BorderLayout;
import java.awt.Color;

public class TeacherDashboard extends DefaultDashboard {

	Connection dbconn = DBConnection.connectDB();
	private CButton editButton;
	private JTable teachersScheduleTable;
	private Object gradeLevel;

	public TeacherDashboard() {

		if (dbconn != null) {
			try {
				String sql = "SELECT hasFollowedUp FROM teachers WHERE teacherId = '" + Login.userId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				if (res.next()) {
					boolean hasFollowedUp = res.getBoolean("hasFollowedUp");
					if (hasFollowedUp == false) {
						SigMethods.enableGlassPane(dashboardFrame);
						new GetTeacherAdditionalInfo();
					} else
						System.out.println("continue");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		northFillerPanel.setPreferredSize(new Dimension(0, 50));
		northFillerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		gradeLevel = null;
		String subject = null;
		try {
			Statement st = dbconn.createStatement();

			ResultSet res = st.executeQuery(
					"SELECT preferredStartingGradeLevel, preferredStartingSubject FROM teachers WHERE teacherId = '"
							+ Login.userId + "'");
			if (res.next()) {
				gradeLevel = res.getString("preferredStartingGradeLevel");
				subject = res.getString("preferredStartingSubject");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		northFillerPanel.add(new CLabel("Showing chedule for "+Login.userName));
		viewSchedBasePanel.contentBasePanel.add(showScheduleTable());
		
		JPanel root = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 10));
		root.setBackground(Colors.LIGHT);

		CLabel preferredSub = new CLabel("Subject: " + subject);
		preferredSub.fixJLabelBug(preferredSub);
		CLabel preferredGrade = new CLabel("Advisory: " + gradeLevel);
		preferredGrade.fixJLabelBug(preferredSub);

		JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
		south.setPreferredSize(new Dimension(500, 70));
		south.setBackground(Colors.LIGHT);

		editButton = new CButton("Update Details");
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

					SigMethods.enableGlassPane(dashboardFrame);
					new GetTeacherAdditionalInfo();

				

			}

		});
		south.add(editButton);
		root.add(preferredSub);
		root.add(preferredGrade);
		accInfoBasePanel.setLayout(new BorderLayout());
		accInfoBasePanel.setBorder(BorderFactory.createLineBorder(Colors.DARK));
		accInfoBasePanel.add(root, BorderLayout.CENTER);
		accInfoBasePanel.add(south, BorderLayout.SOUTH);
		
		CButton printBtn = new CButton("Print");
		printBtn.setPreferredSize(new Dimension(250, 50));
		printBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Print!");
				MessageFormat header = new MessageFormat(Login.userName + " schedule for "+ gradeLevel);
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					teachersScheduleTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("noo");
				}
				
			}
			
		});
		viewSchedBasePanel.s.setPreferredSize(new Dimension(0, 60));
		viewSchedBasePanel.s.setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 5));
		viewSchedBasePanel.s.add(printBtn);

	}

	private JScrollPane showScheduleTable() {
		teachersScheduleTable = new JTable();
		teachersScheduleTable.setDefaultEditor(Object.class, null);
		teachersScheduleTable.setBackground(Colors.LIGHT);
		teachersScheduleTable.setFont(myFont);
		teachersScheduleTable.setForeground(Colors.DARK);
		teachersScheduleTable.setFillsViewportHeight(true);
		teachersScheduleTable.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = teachersScheduleTable.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		teachersScheduleTable.setModel(model);

		JScrollPane scrollPane = new JScrollPane(teachersScheduleTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		// load table data
		if (dbconn != null) {
			try {
				String tableName = null;
				Statement st = dbconn.createStatement();
				String getTeacherTable = "SELECT tableName FROM teachers WHERE teacherId = '" + Login.userId + "'";
				ResultSet rs = st.executeQuery(getTeacherTable);
				if (rs.next()) {
					tableName = rs.getString("tableName");
				}
				String getTable = "SELECT * FROM " + tableName + "";

				ResultSet res = st.executeQuery(getTable);
				while (res.next()) {
					String timeRange = res.getString("timeRange");
					String mon = res.getString("mon");
					String tue = res.getString("tue");
					String wed = res.getString("wed");
					String thu = res.getString("thu");
					String fri = res.getString("fri");
					model.addRow(new Object[] { timeRange, mon, tue, wed, thu, fri });

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		adjustTableSizeAccordingToContentSched(teachersScheduleTable);
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
