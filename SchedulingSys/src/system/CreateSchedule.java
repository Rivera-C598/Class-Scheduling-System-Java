package system;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import theme.CButton;
import theme.CLabel;
import theme.CPanel;
import theme.CTextField;
import theme.Colors;
import theme.PreparedFont;
import theme.SigMethods;

public class CreateSchedule implements ActionListener {

	Connection dbconn = DBConnection.connectDB();

	static Font myFont = new PreparedFont().myCustomFont;
	static String fontString = new PreparedFont().toString(myFont);

	Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	JFrame createScheduleFrame;
	private CPanel createSchedPanel;
	JPanel manageTeachersBasePanel, manageRoomsBasePanel, scheduleBasePanel, selectSchedPanel;

	JComboBox<String> teacherList;
	JComboBox<String> gradeLevelList;

	private JTabbedPane rootTabbedPane;

	private CTextField selectedDayDisp;

	private CTextField selectedTimeDisp;

	private CTextField selectedSlotDisp;

	private CButton insertSchedBtn;

	private JComboBox<String> subjectList;

	private CButton removeBtn;

	public CreateSchedule() {
		initComponents();
	}

	private void initComponents() {

		createScheduleFrame = new JFrame();
		SigMethods.draggable(createScheduleFrame);
		ImageIcon JFrameLogo = new ImageIcon("icons/ctuLogo.png");
		createScheduleFrame.setIconImage(JFrameLogo.getImage());
		createScheduleFrame.setSize(1300, 750);
		createScheduleFrame.setLocationRelativeTo(null);
		createScheduleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createScheduleFrame.setUndecorated(true);
		createScheduleFrame.setLayout(new BorderLayout());
		createScheduleFrame.add(contentPanel());
		createScheduleFrame.setVisible(true);

	}

	private JPanel contentPanel() {
		createSchedPanel = new CPanel(1);
		createSchedPanel.backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				AdminDashboard.adminDashboardFrame.setVisible(true);
				createScheduleFrame.setVisible(false);
			}
		});
		createSchedPanel.setCustomPanelTitle("Build a Schedule");

		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(Colors.LIGHT);

		JPanel westPanel = new JPanel();
		westPanel.setBackground(Colors.LIGHT);

		westPanel.add(scheduleCreatorPanel());
		root.add(westPanel, BorderLayout.WEST);

		JPanel centerPanel = new JPanel(new BorderLayout());

		JPanel centerNorthPanel = new JPanel();
		centerNorthPanel.setBackground(Colors.LIGHT);
		centerNorthPanel.setPreferredSize(new Dimension(0, 10));
		centerPanel.add(centerNorthPanel, BorderLayout.CENTER);

		centerPanel.setBackground(Colors.LIGHT);
		centerPanel.add(schedulePanel(), BorderLayout.CENTER);
		root.add(centerPanel, BorderLayout.CENTER);

		createSchedPanel.contentBasePanel.add(root);

		return createSchedPanel;

	}

	private JPanel scheduleCreatorPanel() {
		TitledBorder title = new TitledBorder(BorderFactory.createLineBorder(Colors.DARK, 2), "Schedule Creator v1.0");
		title.setTitleFont(myFont);
		title.setTitleColor(Colors.DARK);

		JPanel root = new JPanel(new BorderLayout());
		root.setPreferredSize(new Dimension(400, 650));
		root.setMinimumSize(new Dimension(400, 0));
		root.setBackground(Colors.LIGHT);
		root.setBorder(title);
		JPanel topMargin = new JPanel();
		topMargin.setBackground(Colors.LIGHT);
		topMargin.setPreferredSize(new Dimension(0, 0));

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBackground(Colors.LIGHT);

		JPanel selectGradeLevelPanel = new JPanel();
		selectGradeLevelPanel.setPreferredSize(new Dimension(400, 90));
		selectGradeLevelPanel.setBackground(Colors.LIGHT);

		JPanel holder1 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder1.setPreferredSize(new Dimension(350, 36));
		holder1.setBackground(Colors.DARK);
		CLabel selectGradeLevelLabel = new CLabel("Select a grade level");
		holder1.add(selectGradeLevelLabel);
		selectGradeLevelLabel.setForeground(Colors.LIGHT);
		selectGradeLevelLabel.setFontSize(24);
		selectGradeLevelLabel.fixJLabelBug(selectGradeLevelLabel);

		gradeLevelList = new JComboBox<String>();
		gradeLevelList.setFont(new Font(fontString, 0, 22));
		gradeLevelList.setPreferredSize(new Dimension(350, 40));

		if (dbconn != null) {
			try {
				String sql = "SELECT grade from gradelevels";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				while (res.next()) {
					gradeLevelList.addItem("Grade " + res.getString("grade"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		gradeLevelList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int comboSelectedIndex = 0;
				if (gradeLevelList.getSelectedIndex() == -1) {

				} else {
					comboSelectedIndex = gradeLevelList.getSelectedIndex();
				}
				rootTabbedPane.setSelectedIndex(comboSelectedIndex);

				String grade = null;
				if (gradeLevelList.getSelectedItem() == null) {

				} else {
					grade = gradeLevelList.getSelectedItem().toString();
				}
				String arr[] = grade.split(" ", 2);
				String g = arr[1];
				String targetGrade = "Grade " + g;
				teacherList.removeAllItems();
				try {

					String sql = "SELECT users.userName FROM users JOIN teachers WHERE users.id = teachers.teacherId AND users.userType = 'Teacher' AND teachers.preferredStartingGradeLevel = '"
							+ targetGrade + "'";
					Statement st = dbconn.createStatement();
					ResultSet res = st.executeQuery(sql);
					while (res.next()) {
						teacherList.addItem(res.getString("users.userName"));
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}

			}

		});
		selectGradeLevelPanel.add(holder1);
		selectGradeLevelPanel.add(gradeLevelList);

		// SELECT TEACHER PANEL
		JPanel selectTeacherPanel = new JPanel();
		selectTeacherPanel.setPreferredSize(new Dimension(400, 90));
		selectTeacherPanel.setBackground(Colors.LIGHT);

		JPanel holder2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder2.setPreferredSize(new Dimension(350, 36));
		holder2.setBackground(Colors.DARK);
		CLabel selectTeacherLabel = new CLabel("Select a teacher");
		holder2.add(selectTeacherLabel);
		selectTeacherLabel.setForeground(Colors.LIGHT);
		selectTeacherLabel.setFontSize(24);
		selectTeacherLabel.fixJLabelBug(selectTeacherLabel);

		teacherList = new JComboBox<String>();
		teacherList.setFont(new Font(fontString, 0, 22));
		teacherList.setPreferredSize(new Dimension(350, 40));
		teacherList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String teacherName = null;
				if (teacherList.getSelectedItem() == null) {

				} else {
					teacherName = teacherList.getSelectedItem().toString();
				}
				subjectList.removeAllItems();
				try {

					String sql = "SELECT teachers.preferredStartingSubject FROM teachers JOIN users WHERE teachers.teacherId = users.id AND users.userType = 'Teacher' AND users.userName = '"
							+ teacherName + "'";
					Statement st = dbconn.createStatement();
					ResultSet res = st.executeQuery(sql);
					while (res.next()) {
						subjectList.addItem(res.getString("teachers.preferredStartingSubject"));
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}

			}
		});

		selectTeacherPanel.add(holder2);
		selectTeacherPanel.add(teacherList);

		// SELECT SUBJECT PANEL
		JPanel selectSubjectPanel = new JPanel();
		selectSubjectPanel.setPreferredSize(new Dimension(400, 90));
		selectSubjectPanel.setBackground(Colors.LIGHT);

		JPanel holder3 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder3.setPreferredSize(new Dimension(350, 36));
		holder3.setBackground(Colors.DARK);
		CLabel selectSubjectLabel = new CLabel("Select subject");
		holder3.add(selectSubjectLabel);
		selectSubjectLabel.setForeground(Colors.LIGHT);
		selectSubjectLabel.setFontSize(24);
		selectSubjectLabel.fixJLabelBug(selectSubjectLabel);

		subjectList = new JComboBox<String>();
		subjectList.setFont(new Font(fontString, 0, 22));
		subjectList.setPreferredSize(new Dimension(350, 40));

		selectSubjectPanel.add(holder3);
		selectSubjectPanel.add(subjectList);

		JPanel createSchedPanel = new JPanel(new BorderLayout());
		createSchedPanel.setPreferredSize(new Dimension(370, 280));
		createSchedPanel.setBackground(Colors.LIGHT);
		createSchedPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Colors.DARK));

		JPanel cNorth = new JPanel(new BorderLayout());
		cNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		cNorth.setPreferredSize(new Dimension(360, 90));
		cNorth.setBackground(Colors.LIGHT);

		FlowLayout flow = new FlowLayout(FlowLayout.LEADING, 0, 0);
		Dimension panelSize = new Dimension(115, 90);
		Dimension cSZ = new Dimension(115, 38);

		JPanel selectTRPanel = new JPanel(flow);
		selectTRPanel.setPreferredSize(new Dimension(230, 90));
		selectTRPanel.setBackground(Colors.LIGHT);

		JPanel holder4 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder4.setPreferredSize(new Dimension(230, 36));
		holder4.setBackground(Colors.DARK);

		CLabel trLabel = new CLabel("Selected time range");
		trLabel.setFontSize(22);
		trLabel.setForeground(Colors.LIGHT);
		holder4.add(trLabel);

		selectedTimeDisp = new CTextField();
		selectedTimeDisp.setFont(new Font(fontString, 0, 20));
		selectedTimeDisp.setPreferredSize(new Dimension(230, 38));
		selectedTimeDisp.setEditable(false);

		selectTRPanel.add(holder4);
		selectTRPanel.add(selectedTimeDisp);

		JPanel selectDayPanel = new JPanel(flow);
		selectDayPanel.setPreferredSize(panelSize);
		selectDayPanel.setBackground(Colors.LIGHT);

		JPanel holder5 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder5.setPreferredSize(new Dimension(115, 36));
		holder5.setBackground(Colors.DARK);

		CLabel dLabel = new CLabel("Day");
		dLabel.setFontSize(22);
		dLabel.setForeground(Colors.LIGHT);
		holder5.add(dLabel);
		selectedDayDisp = new CTextField();
		selectedDayDisp.setFont(new Font(fontString, 0, 20));
		selectedDayDisp.setPreferredSize(cSZ);
		selectedDayDisp.setEditable(false);
		selectDayPanel.add(holder5);
		selectDayPanel.add(selectedDayDisp);

		JPanel selectAvailableCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		selectAvailableCell.setBackground(Colors.LIGHT);

		JPanel holder6 = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		holder6.setPreferredSize(new Dimension(350, 36));
		holder6.setBackground(Colors.DARK);

		CLabel selectAvaiLabel = new CLabel("Is slot available?");
		selectAvaiLabel.setFontSize(25);
		selectAvaiLabel.setForeground(Colors.LIGHT);
		holder6.add(selectAvaiLabel);
		selectedSlotDisp = new CTextField();
		selectedSlotDisp.setFont(new Font(fontString, 0, 20));
		selectedSlotDisp.setPreferredSize(new Dimension(350, 40));
		selectedSlotDisp.setEditable(false);
		selectAvailableCell.add(holder6);
		selectAvailableCell.add(selectedSlotDisp);

		JPanel south = new JPanel();
		south.setPreferredSize(new Dimension(370, 100));
		south.setBackground(Colors.LIGHT);
		
		Dimension buttonSizes = new Dimension(190, 45);
		
		insertSchedBtn = new CButton("Insert");
		insertSchedBtn.setPreferredSize(buttonSizes);
		insertSchedBtn.addActionListener(this);
		south.add(insertSchedBtn);

		removeBtn = new CButton("Remove");
		removeBtn.setPreferredSize(buttonSizes);
		removeBtn.addActionListener(this);
		removeBtn.setVisible(false);

		String showBtn = selectedSlotDisp.getText();
		if (showBtn.equals("Yes")) {
			removeBtn.setVisible(false);
		} else if (showBtn.equals("No")) {
			removeBtn.setVisible(true);
		}

		south.add(removeBtn);

		cNorth.add(selectTRPanel, BorderLayout.WEST);
		cNorth.add(selectDayPanel, BorderLayout.EAST);
		createSchedPanel.add(cNorth, BorderLayout.NORTH);
		createSchedPanel.add(selectAvailableCell, BorderLayout.CENTER);
		createSchedPanel.add(south, BorderLayout.SOUTH);

		centerPanel.add(selectGradeLevelPanel);
		centerPanel.add(selectTeacherPanel);
		centerPanel.add(selectSubjectPanel);
		centerPanel.add(createSchedPanel);

		root.add(centerPanel, BorderLayout.CENTER);

		return root;
	}

	private JTabbedPane schedulePanel() {

		UIManager.put("TabbedPane.selected", Colors.DARK);
		rootTabbedPane = new JTabbedPane() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Color getForegroundAt(int index) {
				if (getSelectedIndex() == index)
					return Colors.LIGHT;
				return Colors.DARK;
			}
		};
		rootTabbedPane.setBackground(Colors.LIGHT);
		rootTabbedPane.setFont(new Font(fontString, 0, 22));
		rootTabbedPane.addTab("Grade 5", showGrade5ScheduleTable());
		rootTabbedPane.addTab("Grade 6", showGrade6ScheduleTable());
		rootTabbedPane.addTab("Grade 7", showGrade7ScheduleTable());
		rootTabbedPane.addTab("Grade 8", showGrade8ScheduleTable());
		rootTabbedPane.addTab("Grade 9", showGrade9ScheduleTable());
		rootTabbedPane.addTab("Grade 10", showGrade10ScheduleTable());
		rootTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				gradeLevelList.setSelectedIndex(rootTabbedPane.getSelectedIndex());

			}
		});

		return rootTabbedPane;

	}

	private JScrollPane showGrade5ScheduleTable() {
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade5sched";
				Statement st = dbconn.createStatement();
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
		adjustTableSizeAccordingToContentSched(scheduleTable);

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});

		return scrollPane;
	}

	private JScrollPane showGrade6ScheduleTable() {
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade6sched";
				Statement st = dbconn.createStatement();
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

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});
		adjustTableSizeAccordingToContentSched(scheduleTable);
		return scrollPane;
	}

	private JScrollPane showGrade7ScheduleTable() {
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade7sched";
				Statement st = dbconn.createStatement();
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
		adjustTableSizeAccordingToContentSched(scheduleTable);

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});

		return scrollPane;
	}

	private JScrollPane showGrade8ScheduleTable() {
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade8sched";
				Statement st = dbconn.createStatement();
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
		adjustTableSizeAccordingToContentSched(scheduleTable);

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});

		return scrollPane;
	}

	private JScrollPane showGrade9ScheduleTable() {
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade9sched";
				Statement st = dbconn.createStatement();
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
		adjustTableSizeAccordingToContentSched(scheduleTable);

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});

		return scrollPane;
	}

	private JScrollPane showGrade10ScheduleTable() {
		JTable scheduleTable = new JTable();
		scheduleTable.setDefaultEditor(Object.class, null);
		scheduleTable.setBackground(Colors.MILD);
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM grade10sched";
				Statement st = dbconn.createStatement();
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
		adjustTableSizeAccordingToContentSched(scheduleTable);

		scheduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = scheduleTable.getSelectedRow();
					int selectedCol = scheduleTable.getSelectedColumn();

					if (selectedRow == 0 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {

							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);

						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 0 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 2 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 4 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 1) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 2) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 3) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 4) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("No");
							removeBtn.setVisible(true);
						}

					} else if (selectedRow == 6 && selectedCol == 5) {
						String slot = (String) scheduleTable.getModel().getValueAt(selectedRow, selectedCol);
						String day = (String) scheduleTable.getModel().getColumnName(selectedCol);
						String time = (String) scheduleTable.getModel().getValueAt(selectedRow, 0);
						if (slot.equals("vacant")) {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							selectedSlotDisp.setText("Yes");
							removeBtn.setVisible(false);
						} else {
							selectedTimeDisp.setText(time);
							selectedDayDisp.setText(day);
							removeBtn.setVisible(true);
							selectedSlotDisp.setText("No");
						}

					}

				}
			}
		});

		return scrollPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertSchedBtn) {

			String slotAvailability = selectedSlotDisp.getText();

			String rawGrade = null;
			if (gradeLevelList.getSelectedItem() == null) {

			} else {
				rawGrade = gradeLevelList.getSelectedItem().toString();
			}
			String teacherName = null;
			if (teacherList.getSelectedItem() == null) {

			} else {
				teacherName = teacherList.getSelectedItem().toString();
			}
			String subject = null;
			if (subjectList.getSelectedItem() == null) {

			} else {
				subject = subjectList.getSelectedItem().toString();
			}

			String timeRange = selectedTimeDisp.getText();
			String day = selectedDayDisp.getText();

			String arr[] = rawGrade.split(" ", 2);
			String grade = arr[1];

			if (subject != null) {

				if (day.equals("Monday")) {
					day = "mon";
				} else if (day.equals("Tuesday")) {
					day = "tue";
				} else if (day.equals("Wednesday")) {
					day = "wed";
				} else if (day.equals("Thursday")) {
					day = "thu";
				} else if (day.equals("Friday")) {
					day = "fri";
				}

				boolean goSignal = false;
				if (slotAvailability.equals("Yes")) {
					goSignal = true;
				} else
					goSignal = false;

				String teacherTableName = null;
				try {
					String getTeacherTableName = "SELECT teachers.tableName FROM teachers JOIN users WHERE users.id = teachers.teacherId\r\n"
							+ "AND users.userName = '" + teacherName + "';";
					Statement st = dbconn.createStatement();
					ResultSet res = st.executeQuery(getTeacherTableName);
					if (res.next()) {
						teacherTableName = res.getString("tableName");
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}

				System.out.println(
						grade + " " + teacherTableName + " " + subject + " " + timeRange + " " + day + " " + goSignal);

				if (goSignal == true) {
					String assignation = teacherName + ", " + subject.toUpperCase() + " "+ grade;
					// INSERT TO SCHED TABLE
					try {
						String currentAssgnation = null;
						
						String getCurrentTeacherSchedStatus = "SELECT "+day+" FROM "+teacherTableName+" WHERE timeRange = '"+timeRange+"'";
						Statement st1 = dbconn.createStatement();
						ResultSet rs = st1.executeQuery(getCurrentTeacherSchedStatus);
						if(rs.next()) {
							currentAssgnation = rs.getString(day);
						}
						if(currentAssgnation.equals("vacant")) {
							String insertSched = "UPDATE grade" + grade + "sched SET " + day + " = '" + assignation
								+ "' WHERE timeRange = '" + timeRange + "'";
						
						String insertSchedToTeacher = "UPDATE " + teacherTableName + " SET " + day + " = '"
								+ assignation + "' WHERE timeRange = '" + timeRange + "' ";

						Statement st = dbconn.createStatement();
						st.executeUpdate(insertSched);
						st.executeUpdate(insertSchedToTeacher);

						createScheduleFrame.dispose();
						new CreateSchedule();

						JOptionPane.showMessageDialog(createScheduleFrame, "Schedule Inserted!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						}else {
							JOptionPane.showMessageDialog(createScheduleFrame, "Error, conflict for "+teacherName+"'s schedule ", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						
						
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(createScheduleFrame, "Slot occupied", "Error",
							JOptionPane.ERROR_MESSAGE);

				}
			} else {
				JOptionPane.showMessageDialog(createScheduleFrame, "please select a teacher", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource() == removeBtn) {

			String response = JOptionPane.showInputDialog("Please enter the admin key to continue: ");
			if (response.equals(Main.ADMIN_KEY)) {

				String rawGrade = null;
				if (gradeLevelList.getSelectedItem() == null) {

				} else {
					rawGrade = gradeLevelList.getSelectedItem().toString();
				}
				String teacherName = null;
				if (teacherList.getSelectedItem() == null) {

				} else {
					teacherName = teacherList.getSelectedItem().toString();
				}
				String subject = null;
				if (subjectList.getSelectedItem() == null) {

				} else {
					subject = subjectList.getSelectedItem().toString();
				}

				String timeRange = selectedTimeDisp.getText();
				String day = selectedDayDisp.getText();

				String arr[] = rawGrade.split(" ", 2);
				String grade = arr[1];

				if (day.equals("Monday")) {
					day = "mon";
				} else if (day.equals("Tuesday")) {
					day = "tue";
				} else if (day.equals("Wednesday")) {
					day = "wed";
				} else if (day.equals("Thursday")) {
					day = "thu";
				} else if (day.equals("Friday")) {
					day = "fri";
				}

				String teacherTableName = null;
				try {
					String getTeacherTableName = "SELECT teachers.tableName FROM teachers JOIN users WHERE users.id = teachers.teacherId\r\n"
							+ "AND users.userName = '" + teacherName + "';";
					Statement st = dbconn.createStatement();
					ResultSet res = st.executeQuery(getTeacherTableName);
					if (res.next()) {
						teacherTableName = res.getString("tableName");
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}

				System.out.println(grade + " " + teacherTableName + " " + subject + " " + timeRange + " " + day);

				String assignation = "vacant";
				// INSERT TO SCHED TABLE
				try {
					String removeSched = "UPDATE grade" + grade + "sched SET " + day + " = '" + assignation
							+ "' WHERE timeRange = '" + timeRange + "'";
					String removeSchedFromTeachers = "UPDATE " + teacherTableName + " SET " + day + " = '" + assignation
							+ "' WHERE timeRange = '" + timeRange + "' ";

					Statement st = dbconn.createStatement();
					st.executeUpdate(removeSched);
					st.executeUpdate(removeSchedFromTeachers);

					createScheduleFrame.dispose();
					new CreateSchedule();

					JOptionPane.showMessageDialog(createScheduleFrame, "Schedule Removed!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}

			} else {
				JOptionPane.showMessageDialog(createScheduleFrame, "Incorrect Key, please try again", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		}

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
				width = Math.max(comp.getPreferredSize().width, width);

				table.getColumnModel().getColumn(column).setCellRenderer(new WordWrapCellRenderer());
			}

			columnModel.getColumn(0).setPreferredWidth(150);
			columnModel.getColumn(1).setPreferredWidth(210);
			columnModel.getColumn(2).setPreferredWidth(210);
			columnModel.getColumn(3).setPreferredWidth(210);
			columnModel.getColumn(4).setPreferredWidth(210);
			columnModel.getColumn(5).setPreferredWidth(210);

		}
		setTableRowHeightsSched(table);
	}

	static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
		protected static Border noFocusBorder;

		private Color unselectedForeground;
		private Color unselectedBackground;

		public WordWrapCellRenderer() {
			super();
			noFocusBorder = new EmptyBorder(12, 12, 12, 12);
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
			setBorder(noFocusBorder);
			setBackground(Color.white);

		}

		public void setForeground(Color c) {
			super.setForeground(c);
			unselectedForeground = c;
		}

		public void setBackground(Color c) {
			
			super.setBackground(c);
			unselectedBackground = c;
		}

		public void updateUI() {
			super.updateUI();
			setForeground(null);
			setBackground(null);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			if (isSelected) {
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
				super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
			}

			setFont(table.getFont());

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					super.setForeground(UIManager.getColor("Table.focusCellForeground"));
					super.setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(noFocusBorder);
			}

			setValue(value);

			return this;
		}

		protected void setValue(Object value) {
			setText((value == null) ? "" : value.toString());
		}
	}
}
