package system;



import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import theme.CButton;
import theme.CLabel;
import theme.CPanel;
import theme.CTextField;
import theme.Colors;
import theme.PreparedFont;
import theme.SigMethods;
import theme.TFLimit;
import theme.Utilities;

public class AdminDashboard implements ItemListener, ActionListener, Runnable {

	static Font myFont = new PreparedFont().myCustomFont;
	static String fontString = new PreparedFont().toString(myFont);
	Connection dbconn = DBConnection.connectDB();

	// Variables Declaration
	// Local Variables
	protected JPanel createSchedulePanel, hcpN, hcpL, hcpR, hcpC, backToLoginPanel, accN, accL, accR, accC,
			leftNavIconsPanel, homeIconPanel, accIconPanel, lgoutPanel, hcpNorthBasePanel, hcpNW, hcpNE, clockPanel,
			acNorth, acSouth, acEast, acWest, acCenter, hcpS;
	protected CLabel backToLoginBtn, accDetailsLabel, homeIcon, accIcon, lgoutIcon, timeLabel, dayLabel,
			createScheduleLabel, dateLabel;
	protected CButton createScheduleBtn, finalizeScheduleBtn, viewCandidatesBtn, viewSchedulesBtn, manageTeachersBtn,
			vPListBtn, cMButton;
	protected JToggleButton menuIcon;
	protected CPanel createScheduleBasePanel, viewScheduleBasePanel, cMBasePanel, manageTeachersBasePanel,
			vPListBasePanel, voteSessionHistoryPanel;
	private CButton addNewTeacherBtn;
	private CButton editTeacherBtn;
	private CButton deleteTeacherBtn;
	private CTextField idTf;
	private CTextField nameTf;
	private JComboBox<String> gradeCB;
	private JComboBox<String> subCB;
	private DefaultTableModel model;
	private JTable teachersTable;
	private JTabbedPane rootTabbedPane;
	private CButton printBtn;
	private JComboBox<String> gradeLevelList;
	private JPanel deck;
	private JTable g10Table;
	private JTable g9Table;
	private JTable g8Table;
	private JTable g7Table;
	private JTable g6Table;
	private JTable g5Table;

	// Global and static variables
	public static JFrame adminDashboardFrame;
	protected static JPanel hcpCSubN, hcpBasePanel, leftNavPanel, accBasePanel;
	protected static CLabel welcomeText;

	// For Clock
	static SimpleDateFormat timeFormat, dayFormat, dateFormat;
	static String time, day, date, toolTipDate;
	static boolean hasLoggedOut, goSignal;

	static int second, minute, hour;

	final static String G5 = "Grade 5";
	final static String G6 = "Grade 6";
	final static String G7 = "Grade 7";
	final static String G8 = "Grade 8";
	final static String G9 = "Grade 9";
	final static String G10 = "Grade 10";

	public AdminDashboard() {

		initComponents();
		// START DIGITAL CLOCK
		Thread thread = new Thread(this);
		thread.start();

	}

	public void initComponents() {
		// CREATE THE BASE FRAME FOR THE ADMIN DASHBOARD
		setAdminDashboardFrame();

		// CREATE THE ACCOUNT PANEL FOR THE LEFT NAVBAR FOR THE ADMIN DASHBOARD
		setAccountPanel();

		// CREATE THE LEFT NAVBAR
		setLeftNavbar();

		// CREATE THE MAIN INTERFACE FOR THE ADMIN DASHBOARD
		setHomePanel();

		// INITIALIZE ADMIN DASHBOARD BUTTONS
		setAdminDashboardButtons();

		// CREATE ADMIN DASHBOARD PANELS
		setCreateSchedulePanel();
		setViewSchedPanel();
		setForCampaignManagersPanel();
		setManageTeachersPanel();
		setViewPartyListsPanel();

		// ADD THE BUTTONS TO THE ADMIN DASHBOARD (ADD ONLY)
		addButtonsToAdminDashboard();

		// ADD THE PANELS TO THE ADMIN DASHBOARD (ADD ONLY)
		addPanelsToAdminDashboard();

		// FINAL TOUCHES
		compileBasePanels();
	}

	public void compileBasePanels() {
		adminDashboardFrame.add(accBasePanel);
		adminDashboardFrame.add(leftNavPanel, BorderLayout.WEST);
		adminDashboardFrame.add(hcpBasePanel, BorderLayout.EAST);

		adminDashboardFrame.setVisible(true);
	}

	public void addPanelsToAdminDashboard() {
		// ADD CPANELS
		adminDashboardFrame.add(newPage(createScheduleBasePanel, "Create Schedule"));
		adminDashboardFrame.add(newPage(viewScheduleBasePanel, "View Schedules"));
		adminDashboardFrame.add(newPage(cMBasePanel, "For Campaign Managers"));
		adminDashboardFrame.add(newPage(manageTeachersBasePanel, "Manage Teachers"));
		adminDashboardFrame.add(newPage(vPListBasePanel, "View Partylists"));
	}

	public void addButtonsToAdminDashboard() {
		// ADDING OF BUTTONS TO THE ADMIN DASHBOARD
		hcpCSubN.add(createSchedulePanel);
		hcpCSubN.add(viewSchedulesBtn);
		hcpCSubN.add(manageTeachersBtn);
	}

	public void setViewPartyListsPanel() {
		vPListBasePanel = new CPanel();
	}

	public void setManageTeachersPanel() {
		manageTeachersBasePanel = new CPanel();
		manageTeachersBasePanel.contentBasePanel.setBackground(Colors.LIGHT);

		// TODO: Set teacher load

		BorderLayout layout = new BorderLayout();

		JPanel manageTeachersPanel = new JPanel(layout);
		manageTeachersPanel.setBackground(Colors.LIGHT);
		manageTeachersPanel.setPreferredSize(new Dimension(300, 0));

		////////////////////////////////////////////////////////////////////////////

		TitledBorder editAddTitle = new TitledBorder(BorderFactory.createLineBorder(Colors.DARK, 2), "Manage teacher");
		editAddTitle.setTitleFont(myFont);
		editAddTitle.setTitleColor(Colors.DARK);
		editAddTitle.setTitleJustification(TitledBorder.TRAILING);

		Dimension northMarginHeight = new Dimension(0, 5);
		Dimension southHeight = new Dimension(0, 40);
		Dimension buttonSize = new Dimension(84, 34);
		Dimension panelSize = new Dimension(300, 55);
		Dimension tfD = new Dimension(260, 30);
		int fSize = 18;
		FlowLayout flow = new FlowLayout(FlowLayout.LEADING, 0, 0);

		JPanel editAddTeachersPanel = new JPanel(new BorderLayout());
		editAddTeachersPanel.setBorder(editAddTitle);
		editAddTeachersPanel.setBackground(Colors.LIGHT);
		editAddTeachersPanel.setPreferredSize(new Dimension(300, 300));

		JPanel editAddPanelTitle = new JPanel();
		editAddPanelTitle.setPreferredSize(northMarginHeight);
		editAddPanelTitle.setBackground(Colors.LIGHT);

		JPanel rootPanel1 = new JPanel(flow);
		rootPanel1.setBackground(Colors.LIGHT);
		rootPanel1.setBorder(new EmptyBorder(2, 15, 0, 10));

		JPanel idPanel = new JPanel(flow);
		idPanel.setPreferredSize(panelSize);
		idPanel.setBackground(Colors.LIGHT);
		CLabel idLabel = new CLabel("Teacher Id");
		idLabel.setFontSize(fSize);
		idTf = new CTextField();
		idTf.setPreferredSize(tfD);
		idTf.setFontSize(fSize);
		idPanel.add(idLabel);
		idPanel.add(idTf);
		idTf.setDocument(new TFLimit(7));

		JPanel namePanel = new JPanel(flow);
		namePanel.setPreferredSize(panelSize);
		namePanel.setBackground(Colors.LIGHT);
		CLabel nameLabel = new CLabel("Name:");
		nameLabel.setFontSize(fSize);
		nameTf = new CTextField();
		nameTf.setPreferredSize(tfD);
		nameTf.setFontSize(fSize);
		namePanel.add(nameLabel);
		namePanel.add(nameTf);

		JPanel gradePanel = new JPanel(flow);
		gradePanel.setPreferredSize(panelSize);
		gradePanel.setBackground(Colors.LIGHT);
		CLabel gradeLabel = new CLabel("Handles Grade level:");
		gradeLabel.setFontSize(fSize);
		gradeCB = new JComboBox<String>();
		gradeCB.setPreferredSize(tfD);
		gradeCB.setFont(new Font(fontString, 0, fSize));
		if (dbconn != null) {
			try {
				String sql = "SELECT grade from gradelevels";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(sql);
				while (res.next()) {
					gradeCB.addItem("Grade " + res.getString("grade"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		gradeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String raw = null;
				if (gradeCB.getSelectedItem() == null) {

				} else {
					raw = gradeCB.getSelectedItem().toString();
				}

				String strArr[] = raw.split(" ", 2);
				String gradeLevel = strArr[1];
				System.out.println(gradeLevel);
				boolean isHighschool = false;
				if (dbconn != null) {
					try {
						String sql = "SELECT isHighschool FROM gradeLevels WHERE grade = '" + gradeLevel + "'";
						Statement st = dbconn.createStatement();
						ResultSet res = st.executeQuery(sql);
						if (res.next()) {
							isHighschool = res.getBoolean("isHighschool");
						}
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}
				if (isHighschool == true) {
					subCB.removeAllItems();
					try {
						String sql = "SELECT * FROM highschoolsubjects";
						Statement st = dbconn.createStatement();
						ResultSet res = st.executeQuery(sql);
						while (res.next()) {
							subCB.addItem(res.getString("subjects"));
						}
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				} else if (isHighschool == false) {
					subCB.removeAllItems();
					try {
						String sql = "SELECT * FROM elementarysubjects";
						Statement st = dbconn.createStatement();
						ResultSet res = st.executeQuery(sql);
						while (res.next()) {

							subCB.addItem(res.getString("subjects"));
						}
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}

			}
		});
		gradePanel.add(gradeLabel);
		gradePanel.add(gradeCB);

		JPanel subPanel = new JPanel(flow);
		subPanel.setPreferredSize(panelSize);
		subPanel.setBackground(Colors.LIGHT);
		CLabel subLabel = new CLabel("Initial Subject:");
		subLabel.setFontSize(fSize);
		subCB = new JComboBox<String>();
		subCB.setPreferredSize(tfD);
		subCB.setFont(new Font(fontString, 0, fSize));

		subPanel.add(subLabel);
		subPanel.add(subCB);

		rootPanel1.add(idPanel);
		rootPanel1.add(namePanel);
		rootPanel1.add(gradePanel);
		rootPanel1.add(subPanel);

		JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		buttonContainer.setBorder(new EmptyBorder(2, 0, 0, 0));
		addNewTeacherBtn = new CButton("Add");
		addNewTeacherBtn.setFontSize(16);
		addNewTeacherBtn.setPreferredSize(buttonSize);
		addNewTeacherBtn.addActionListener(this);

		editTeacherBtn = new CButton("Update");
		editTeacherBtn.setFontSize(16);
		editTeacherBtn.setPreferredSize(buttonSize);
		editTeacherBtn.addActionListener(this);

		deleteTeacherBtn = new CButton("Delete");
		deleteTeacherBtn.setFontSize(16);
		deleteTeacherBtn.setPreferredSize(buttonSize);
		deleteTeacherBtn.addActionListener(this);

		buttonContainer.setBackground(Colors.LIGHT);
		buttonContainer.setPreferredSize(southHeight);

		buttonContainer.add(addNewTeacherBtn);
		buttonContainer.add(editTeacherBtn);
		buttonContainer.add(deleteTeacherBtn);

		editAddTeachersPanel.add(editAddPanelTitle, BorderLayout.NORTH);
		editAddTeachersPanel.add(rootPanel1, BorderLayout.CENTER);
		editAddTeachersPanel.add(buttonContainer, BorderLayout.SOUTH);
		///////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////////////////

		TitledBorder manageTeacherLoadTitle = new TitledBorder(BorderFactory.createLineBorder(Colors.DARK, 2), "-");
		manageTeacherLoadTitle.setTitleFont(myFont);
		manageTeacherLoadTitle.setTitleColor(Colors.DARK);
		manageTeacherLoadTitle.setTitleJustification(TitledBorder.TRAILING);

		JPanel addTeacherLoadPanel = new JPanel(new BorderLayout());
		addTeacherLoadPanel.setBackground(Colors.LIGHT);
		addTeacherLoadPanel.setBorder(manageTeacherLoadTitle);

		JPanel addTeacherLoadPanelTitle = new JPanel();
		addTeacherLoadPanelTitle.setPreferredSize(northMarginHeight);
		addTeacherLoadPanelTitle.setBackground(Colors.LIGHT);

		JPanel rootPanel2 = new JPanel(flow);
		rootPanel2.setBackground(Colors.LIGHT);
		rootPanel2.setBorder(new EmptyBorder(2, 15, 0, 10));

		addTeacherLoadPanel.add(addTeacherLoadPanelTitle, BorderLayout.NORTH);
		//////////////////////////////////////////////////////////////////////////

		manageTeachersPanel.add(editAddTeachersPanel, BorderLayout.NORTH);
		manageTeachersPanel.add(addTeacherLoadPanel, BorderLayout.CENTER);

		manageTeachersBasePanel.contentBasePanel.add(showTeachersTable(), BorderLayout.CENTER);
		manageTeachersBasePanel.contentBasePanel.add(manageTeachersPanel, BorderLayout.EAST);

	}

	private JScrollPane showTeachersTable() {
		teachersTable = new JTable();
		teachersTable.setDefaultEditor(Object.class, null);
		teachersTable.setBackground(Colors.LIGHT);
		teachersTable.setFont(myFont);
		teachersTable.setForeground(Colors.DARK);
		teachersTable.setFillsViewportHeight(true);
		teachersTable.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		teachersTable.setRowSelectionAllowed(true);
		teachersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JTableHeader header = teachersTable.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		model = new DefaultTableModel();
		String[] columnNames = { "Teacher Id", "Name", "Preferred Subject", "Preferred Grade" };
		model.setColumnIdentifiers(columnNames);
		teachersTable.setModel(model);

		JScrollPane scrollPane = new JScrollPane(teachersTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT teachers.teacherId, users.userName, teachers.preferredStartingGradeLevel, teachers.preferredStartingSubject FROM users JOIN teachers WHERE users.id = teachers.teacherId AND users.userType = 'Teacher'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(getTable);
				while (res.next()) {
					String teacherId = res.getString("teacherId");
					String teacherName = res.getString("users.userName");
					String gLevel = res.getString("teachers.preferredStartingGradeLevel");
					String subject = res.getString("teachers.preferredStartingSubject");

					model.addRow(new Object[] { teacherId, teacherName, subject, gLevel });

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		adjustTableSizeAccordingToContent(teachersTable);

		teachersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					int index = teachersTable.getSelectedRow();

					if (teachersTable.getModel().getValueAt(index, 0) != null) {
						String teacherId = (String) teachersTable.getModel().getValueAt(index, 0);
						idTf.setText(teacherId);

						String teacherName = (String) teachersTable.getModel().getValueAt(index, 1);
						nameTf.setText(teacherName);

						String grade = (String) teachersTable.getModel().getValueAt(index, 3);
						gradeCB.setSelectedItem(grade);
					}
				}
			}
		});

		return scrollPane;
	}

	public void setForCampaignManagersPanel() {
		cMBasePanel = new CPanel();
	}

	public void setViewSchedPanel() {
		viewScheduleBasePanel = new CPanel();
		JPanel north = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 0));
		north.setPreferredSize(new Dimension(0, 60));
		north.setBackground(Colors.LIGHT);
		viewScheduleBasePanel.contentBasePanel.add(north, BorderLayout.NORTH);

		gradeLevelList = new JComboBox<String>();
		gradeLevelList.setFont(new Font(fontString, 0, 22));
		gradeLevelList.setPreferredSize(new Dimension(210, 40));

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
		gradeLevelList.addItemListener(this);
		viewScheduleBasePanel.contentBasePanel.add(cardPanel(), BorderLayout.CENTER);
		north.add(gradeLevelList);

		JPanel south = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 0));
		south.setBackground(Colors.LIGHT);
		south.setPreferredSize(new Dimension(0, 60));
		south.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		printBtn = new CButton("Print");
		printBtn.setPreferredSize(new Dimension(250, 50));
		printBtn.addActionListener(this);
		south.add(printBtn);
		viewScheduleBasePanel.contentBasePanel.add(south, BorderLayout.SOUTH);
	}

	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (deck.getLayout());
		cl.show(deck, (String) evt.getItem());
	}

	private JPanel cardPanel() {

		deck = new JPanel(new CardLayout());

		JPanel card5 = new JPanel(new BorderLayout());
		card5.add(showG5SchedTable());

		JPanel card6 = new JPanel(new BorderLayout());
		card6.add(showG6SchedTable());

		JPanel card7 = new JPanel(new BorderLayout());
		card7.add(showG7SchedTable());

		JPanel card8 = new JPanel(new BorderLayout());
		card8.add(showG8SchedTable());

		JPanel card9 = new JPanel(new BorderLayout());
		card9.add(showG9SchedTable());

		JPanel card10 = new JPanel(new BorderLayout());
		card10.add(showG10SchedTable());

		deck.add(card5, G5);
		deck.add(card6, G6);
		deck.add(card7, G7);
		deck.add(card8, G8);
		deck.add(card9, G9);
		deck.add(card10, G10);

		return deck;
	}

	private JScrollPane showG5SchedTable() {
		g5Table = new JTable();
		g5Table.setDefaultEditor(Object.class, null);
		g5Table.setBackground(Colors.LIGHT);
		g5Table.setFont(myFont);
		g5Table.setForeground(Colors.DARK);
		g5Table.setFillsViewportHeight(true);
		g5Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g5Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g5Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g5Table);
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
		adjustTableSizeAccordingToContentSched(g5Table);

		return scrollPane;
	}

	private JScrollPane showG6SchedTable() {
		g6Table = new JTable();
		g6Table.setDefaultEditor(Object.class, null);
		g6Table.setBackground(Colors.LIGHT);
		g6Table.setFont(myFont);
		g6Table.setForeground(Colors.DARK);
		g6Table.setFillsViewportHeight(true);
		g6Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g6Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g6Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g6Table);
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
		adjustTableSizeAccordingToContentSched(g6Table);

		return scrollPane;
	}

	private JScrollPane showG7SchedTable() {
		g7Table = new JTable();
		g7Table.setDefaultEditor(Object.class, null);
		g7Table.setBackground(Colors.LIGHT);
		g7Table.setFont(myFont);
		g7Table.setForeground(Colors.DARK);
		g7Table.setFillsViewportHeight(true);
		g7Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g7Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g7Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g7Table);
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
		adjustTableSizeAccordingToContentSched(g7Table);

		return scrollPane;
	}

	private JScrollPane showG8SchedTable() {
		g8Table = new JTable();
		g8Table.setDefaultEditor(Object.class, null);
		g8Table.setBackground(Colors.LIGHT);
		g8Table.setFont(myFont);
		g8Table.setForeground(Colors.DARK);
		g8Table.setFillsViewportHeight(true);
		g8Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g8Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g8Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g8Table);
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
		adjustTableSizeAccordingToContentSched(g8Table);

		return scrollPane;
	}

	private JScrollPane showG9SchedTable() {
		g9Table = new JTable();
		g9Table.setDefaultEditor(Object.class, null);
		g9Table.setBackground(Colors.LIGHT);
		g9Table.setFont(myFont);
		g9Table.setForeground(Colors.DARK);
		g9Table.setFillsViewportHeight(true);
		g9Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g9Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g9Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g9Table);
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
		adjustTableSizeAccordingToContentSched(g9Table);

		return scrollPane;
	}

	private JScrollPane showG10SchedTable() {
		g10Table = new JTable();
		g10Table.setDefaultEditor(Object.class, null);
		g10Table.setBackground(Colors.LIGHT);
		g10Table.setFont(myFont);
		g10Table.setForeground(Colors.DARK);
		g10Table.setFillsViewportHeight(true);
		g10Table.setBorder(BorderFactory.createLineBorder(Colors.DARK));

		JTableHeader header = g10Table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Colors.DARK);
		header.setForeground(Colors.LIGHT);
		header.setFont(new Font(fontString, 0, 22));

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = { "Time Range", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		model.setColumnIdentifiers(columnNames);
		g10Table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(g10Table);
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
		adjustTableSizeAccordingToContentSched(g10Table);

		return scrollPane;
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

		// load table data
		if (dbconn != null) {
			try {

				String getTable = "SELECT * FROM sched";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(getTable);
				while (res.next()) {
					String timeRange = res.getString("timeRange");
					String mon = res.getString("monday");
					String tue = res.getString("tuesday");
					String wed = res.getString("wednesday");
					String thu = res.getString("thursday");
					String fri = res.getString("friday");
					model.addRow(new Object[] { timeRange, mon, tue, wed, thu, fri });

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		adjustTableSizeAccordingToContentSched(scheduleTable);
		return scrollPane;
	}

	public void setAdminDashboardFrame() {
		adminDashboardFrame = new JFrame();
		SigMethods.draggable(adminDashboardFrame);
		adminDashboardFrame.setSize(1100, 750);
		adminDashboardFrame.setLocationRelativeTo(null);
		adminDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adminDashboardFrame.setUndecorated(true);
		adminDashboardFrame.setLayout(new BorderLayout());
	}

	public void setAdminDashboardButtons() {
		// Admin Dashboard Buttons
		hcpCSubN.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		createSchedulePanel = new JPanel();
		createSchedulePanel.setBackground(Colors.LIGHT);

		createScheduleBtn = new CButton("+");
		createScheduleBtn.setFontSize(50);
		createScheduleBtn.setForeground(Colors.LIGHT);
		createScheduleLabel = new CLabel("Create schedule");
		createScheduleLabel.setFontSize(30);
		createScheduleLabel.setForeground(Colors.DARK);

		createScheduleBtn.setPreferredSize(new Dimension(60, 60));
		createScheduleBtn.addActionListener(this);
		createScheduleBtn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				createScheduleBtn.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				createScheduleBtn.setForeground(Colors.LIGHT);
			}
		});
		createSchedulePanel.add(createScheduleBtn);
		createSchedulePanel.add(createScheduleLabel);

		// FOR VIEW ELECTION STATISTICS BUTTON
		viewSchedulesBtn = new CButton("View Schedules");
		viewSchedulesBtn.setPreferredSize(new Dimension(300, 60));
		viewSchedulesBtn.setFontSize(20);
		viewSchedulesBtn.addActionListener(this);

		// FOR MANAGE USERS
		manageTeachersBtn = new CButton("Manage Teachers");
		manageTeachersBtn.addActionListener(this);

	}

	public void setCreateSchedulePanel() {
		// ADD CANDIDATES BASE PANEL
		createScheduleBasePanel = new CPanel(1);

	}

	public void setLeftNavbar() {
		// LEFT NAVBAR
		leftNavPanel = new JPanel();
		leftNavPanel.setPreferredSize(new Dimension(60, 0));
		leftNavPanel.setBackground(Colors.DARK);
		leftNavPanel.setLayout(new BorderLayout());

		leftNavIconsPanel = new JPanel();
		leftNavIconsPanel.setBackground(Colors.DARK);

		menuIcon = new JToggleButton("\\/");
		menuIcon.setFont(new Font(fontString, 0, 30));
		menuIcon.setForeground(Colors.LIGHT);
		
		menuIcon.setRolloverEnabled(false);
		menuIcon.setFocusable(false);
		menuIcon.setContentAreaFilled(false);
		menuIcon.setOpaque(true);
		menuIcon.setBackground(Colors.DARK);
		menuIcon.setBorder(BorderFactory.createLineBorder(Colors.DARK));
		menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		menuIcon.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					menuIcon.setText("/\\");
					accIconPanel.setVisible(true);
					lgoutPanel.setVisible(true);
				} else {
					menuIcon.setText("\\/");
					accIconPanel.setVisible(false);
					lgoutPanel.setVisible(false);
				}

			}
		});

		homeIconPanel = new JPanel();
		homeIconPanel.setPreferredSize(new Dimension(60, 80));
		homeIconPanel.setBackground(Colors.DARK);
		homeIconPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		homeIconPanel.setBorder(new MatteBorder(2, 0, 0, 0, Colors.LIGHT));

		CLabel homeLabel = new CLabel("home");
		homeLabel.setFontSize(10);
		homeLabel.setForeground(Colors.LIGHT);

		homeIcon = new CLabel("H");
		homeIcon.setFontSize(50);
		homeIcon.setForeground(Colors.LIGHT);
		homeIconPanel.add(homeIcon);
		homeIconPanel.add(homeLabel);

		homeIconPanel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				homeIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				homeIconPanel.setBackground(Colors.LIGHT);
				homeIcon.setForeground(Colors.DARK);
				homeLabel.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				homeIconPanel.setBackground(Colors.DARK);
				homeIcon.setForeground(Colors.LIGHT);
				homeLabel.setForeground(Colors.LIGHT);
			}

			public void mouseClicked(MouseEvent evt) {
				hcpBasePanel.setVisible(true);
			}
		});

		accIconPanel = new JPanel();
		accIconPanel.setBackground(Colors.DARK);
		accIconPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		accIconPanel.setPreferredSize(new Dimension(60, 80));

		CLabel accIconLabel = new CLabel("Account");
		accIconLabel.setFontSize(10);
		accIconLabel.setForeground(Colors.LIGHT);

		accIcon = new CLabel();
		accIcon.setIcon(new ImageIcon(getClass().getResource("/image_assets/usrDbrd_Imgs/icons8_test_account_50px_1.png")));
		accIconPanel.add(accIcon);
		accIconPanel.add(accIconLabel);
		accIconPanel.setVisible(false);
		accIconPanel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				accIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				accIconPanel.setBackground(Colors.LIGHT);
				accIcon.setIcon(new ImageIcon(getClass().getResource("/image_assets/usrDbrd_Imgs/icons8_test_account_50px_2.png")));
				accIconLabel.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				accIconPanel.setBackground(Colors.DARK);
				accIcon.setIcon(new ImageIcon(getClass().getResource("/image_assets/usrDbrd_Imgs/icons8_test_account_50px_1.png")));
				accIconLabel.setForeground(Colors.LIGHT);
			}

			public void mouseClicked(MouseEvent evt) {

				leftNavPanel.setVisible(false);
				hcpBasePanel.setVisible(false);
				accBasePanel.setVisible(true);
			}
		});

		// LGOUT PANEL
		lgoutPanel = new JPanel();
		lgoutPanel.setPreferredSize(new Dimension(60, 80));
		lgoutPanel.setBackground(Colors.DARK);

		CLabel lgoutLabel = new CLabel("Log Out");
		lgoutLabel.setFontSize(10);
		lgoutLabel.setForeground(Colors.LIGHT);

		lgoutIcon = new CLabel("L");
		lgoutIcon.setFontSize(50);
		lgoutIcon.setForeground(Colors.LIGHT);
		lgoutPanel.add(lgoutIcon);
		lgoutPanel.add(lgoutLabel);
		lgoutPanel.setVisible(false);
		lgoutPanel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				lgoutPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				lgoutPanel.setBackground(Colors.LIGHT);
				lgoutIcon.setForeground(Colors.DARK);
				lgoutLabel.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				lgoutPanel.setBackground(Colors.DARK);
				lgoutIcon.setForeground(Colors.LIGHT);
				lgoutLabel.setForeground(Colors.LIGHT);
			}

			public void mouseClicked(MouseEvent evt) {

				int response = 1;

				response = JOptionPane.showConfirmDialog(null, "Confirm Logout?", "Confirm Message",
						JOptionPane.YES_NO_OPTION);
				if (response == 0) {
					response = 1;
					hasLoggedOut = true;
					// Logging out...
					EventQueue.invokeLater(() -> {
						adminDashboardFrame.dispose();
						new Login();
					});
				}

			}
		});

		// CLOCK
		timeFormat = new SimpleDateFormat("hh:mm:ss a");
		dayFormat = new SimpleDateFormat("EEEE");
		dateFormat = new SimpleDateFormat("d MMM yyyy");

		timeLabel = new CLabel();
		timeLabel.setFontSize(10);
		timeLabel.setForeground(Colors.LIGHT);

		dayLabel = new CLabel();
		dayLabel.setFontSize(10);
		dayLabel.setForeground(Colors.LIGHT);

		dateLabel = new CLabel();
		dateLabel.setFontSize(10);
		dateLabel.setForeground(Colors.LIGHT);

		clockPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Point getToolTipLocation(MouseEvent e) {
				return new Point(60, 20);
			}
		};
		clockPanel.setPreferredSize(new Dimension(0, 50));
		clockPanel.setBackground(Colors.DARK);

		clockPanel.add(timeLabel);
		// clockPanel.add(dayLabel);
		clockPanel.add(dateLabel);

		clockPanel.setToolTipText(toolTipDate);

		leftNavIconsPanel.add(menuIcon);
		leftNavIconsPanel.add(accIconPanel);
		leftNavIconsPanel.add(lgoutPanel);
		leftNavIconsPanel.add(homeIconPanel);

		leftNavPanel.add(leftNavIconsPanel, BorderLayout.CENTER);
		leftNavPanel.add(clockPanel, BorderLayout.SOUTH);
	}

	public void setHomePanel() {
		// HOME PANEL
		hcpBasePanel = new JPanel();
		hcpBasePanel.setPreferredSize(new Dimension(1040, 0));
		hcpBasePanel.setLayout(new BorderLayout());

		// CENTER PANEL
		welcomeText = new CLabel("Welcome, " + Login.userName);

		welcomeText.setForeground(Colors.DARK);
		welcomeText.setFontSize(50);

		hcpN = new JPanel();
		hcpN.setBackground(Colors.LIGHT);
		hcpN.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 80));
		hcpN.add(welcomeText);

		hcpNorthBasePanel = new JPanel();
		hcpNorthBasePanel.setPreferredSize(new Dimension(0, 250));
		hcpNorthBasePanel.setBackground(Colors.LIGHT);
		hcpNorthBasePanel.setLayout(new BorderLayout());
		hcpNorthBasePanel.add(hcpN, BorderLayout.CENTER);

		hcpNW = new JPanel();
		hcpNW.setPreferredSize(new Dimension(90, 0));
		hcpNW.setBackground(Colors.LIGHT);
		hcpNorthBasePanel.add(hcpNW, BorderLayout.WEST);

		hcpNE = new JPanel();
		hcpNE.setPreferredSize(new Dimension(90, 0));
		hcpNE.setBackground(Colors.LIGHT);
		hcpNE.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		hcpNE.add(new Utilities().darkUtilityPanel(adminDashboardFrame));
		hcpNorthBasePanel.add(hcpNE, BorderLayout.EAST);

		hcpL = new JPanel();
		hcpL.setPreferredSize(new Dimension(60, 0));
		hcpL.setBackground(Colors.LIGHT);

		hcpR = new JPanel();
		hcpR.setPreferredSize(new Dimension(60, 0));
		hcpR.setBackground(Colors.LIGHT);

		hcpC = new JPanel();
		hcpC.setBackground(Colors.LIGHT);
		hcpC.setBorder(new MatteBorder(4, 0, 0, 0, Colors.DARK));
		hcpC.setLayout(new BorderLayout());

		hcpS = new JPanel();
		hcpS.setPreferredSize(new Dimension(0, 140));
		hcpS.setBackground(Colors.LIGHT);

		hcpCSubN = new JPanel();
		hcpCSubN.setPreferredSize(new Dimension(0, 350));
		hcpCSubN.setBackground(Colors.LIGHT);

		hcpC.add(hcpCSubN, BorderLayout.NORTH);
		hcpC.add(hcpS, BorderLayout.SOUTH);

		hcpBasePanel.add(hcpNorthBasePanel, BorderLayout.NORTH);
		hcpBasePanel.add(hcpL, BorderLayout.WEST);
		hcpBasePanel.add(hcpR, BorderLayout.EAST);
		hcpBasePanel.add(hcpC, BorderLayout.CENTER);
	}

	public void setAccountPanel() {
		// ACC SETTINGS PANEL
		accBasePanel = new JPanel();
		accBasePanel.setBounds(0, 0, 1100, 750);
		accBasePanel.setBackground(Colors.LIGHT);
		accBasePanel.setLayout(new BorderLayout());

		accN = new JPanel();
		accN.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		accN.setPreferredSize(new Dimension(0, 60));
		accN.setBackground(Colors.DARK);
		accBasePanel.add(accN, BorderLayout.NORTH);

		accBasePanel.setVisible(false);

		backToLoginPanel = new JPanel();
		backToLoginPanel.setBackground(Colors.DARK);

		backToLoginBtn = new CLabel();

		accDetailsLabel = new CLabel("Admin account Details");
		accDetailsLabel.setForeground(Colors.LIGHT);
		accDetailsLabel.setFontSize(20);

		backToLoginBtn.setIcon(new ImageIcon(getClass().getResource("/image_assets/Rgstr_Imgs/icons8_back_to_50px.png")));
		backToLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		backToLoginBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				accBasePanel.setVisible(false);
				hcpBasePanel.setVisible(true);
				leftNavPanel.setVisible(true);
			}
		});
		backToLoginPanel.add(backToLoginBtn);
		backToLoginPanel.add(accDetailsLabel);

		accN.add(backToLoginPanel);

		accL = new JPanel();
		accL.setPreferredSize(new Dimension(350, 0));
		accL.setBackground(Colors.LIGHT);
		accBasePanel.add(accL, BorderLayout.WEST);

		accR = new JPanel();
		accR.setPreferredSize(new Dimension(350, 0));
		accR.setBackground(Colors.LIGHT);
		accBasePanel.add(accR, BorderLayout.EAST);

		accC = new JPanel();
		accC.setBackground(Colors.LIGHT);
		accBasePanel.add(accC, BorderLayout.CENTER);

		JPanel accInfoBasePanel = new JPanel();
		accInfoBasePanel.setPreferredSize(new Dimension(500, 500));
		accInfoBasePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		accInfoBasePanel.setBackground(Colors.LIGHT);
		accInfoBasePanel.setBorder(BorderFactory.createLineBorder(Colors.DARK));
		accC.add(accInfoBasePanel);

	}

	private CPanel newPage(CPanel panel, String panelTitle) {

		panel.setCustomPanelTitle(panelTitle);
		panel.customFunc(accBasePanel, hcpBasePanel, leftNavPanel, panel);
		panel.setVisible(false);

		return panel;
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

	private void setTableRowHeights(JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			int rowHeight = table.getRowHeight();

			for (int column = 0; column < table.getColumnCount(); column++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight + 5, comp.getPreferredSize().height);

			}

			table.setRowHeight(row, rowHeight);
		}
	}

	public void adjustTableSizeAccordingToContent(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		TableCellRenderer renderer;
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 10; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
				table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
		setTableRowHeights(table);
	}

	public void panelSwitch(CPanel chosenPanel) {
		leftNavPanel.setVisible(false);
		hcpBasePanel.setVisible(false);
		accBasePanel.setVisible(false);
		createScheduleBasePanel.setVisible(false);
		viewScheduleBasePanel.setVisible(false);

		chosenPanel.setVisible(true);
	}

	@Override
	public void run() {
		while (true) {
			time = timeFormat.format(Calendar.getInstance().getTime());

			timeLabel.setText(time);

			day = dayFormat.format(Calendar.getInstance().getTime());
			dayLabel.setText(day);

			date = dateFormat.format(Calendar.getInstance().getTime());
			dateLabel.setText(date);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createScheduleBtn) {
			adminDashboardFrame.setVisible(false);
			new CreateSchedule();
			// panelSwitch(createScheduleBasePanel);
		} else if (e.getSource() == viewSchedulesBtn) {
			panelSwitch(viewScheduleBasePanel);
		} else if (e.getSource() == manageTeachersBtn) {
			panelSwitch(manageTeachersBasePanel);
		} else if (e.getSource() == addNewTeacherBtn) {
			System.out.println("ADD");
			String id = idTf.getText();
			String name = nameTf.getText();
			String grade = gradeCB.getSelectedItem().toString();

			String sub = null;
			if (subCB.getSelectedItem() != null) {
				sub = subCB.getSelectedItem().toString();
			} else if (subCB.getSelectedItem() == null) {
				sub = "unnasigned";
			}

			System.out.println(grade);
			if (id.isEmpty() || name.isEmpty()) {
				JOptionPane.showMessageDialog(adminDashboardFrame, "Some fields are empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!id.isEmpty() && !name.isEmpty()) {
				if (!teacherIdExists(id)) {
					if (dbconn != null) {
						try {

							String tableName = initializeTeacherTable(id, name);
							System.out.println(sub);
							if (sub.equals("unnasigned")) {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?, ?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, false);
								prepSt.executeUpdate();
							} else {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?,?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, true);
								prepSt.executeUpdate();

							}

							Date date = new Date();
							Timestamp dateCreated = new Timestamp(date.getTime());

							String addToUsersCommand = "INSERT INTO users(id, pass, userType, accCreated, userName)"
									+ "VALUES (?,?,?,?,?)";
							PreparedStatement prepSt2 = (PreparedStatement) dbconn.prepareStatement(addToUsersCommand);
							prepSt2.setString(1, id);
							prepSt2.setString(2, id);
							prepSt2.setString(3, "Teacher");
							prepSt2.setTimestamp(4, dateCreated);
							prepSt2.setString(5, name);

							prepSt2.executeUpdate();

							adminDashboardFrame.dispose();
							new AdminDashboard();
							JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher added succesfully", "Success",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (SQLException e2) {
							e2.printStackTrace();
						}
					}
				} else {
					int userResponse = JOptionPane.showConfirmDialog(adminDashboardFrame,
							"Teacher Id already exists, would you like to \"Update\" this data instead?",
							"This Id already exists", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (userResponse == 0) {
						try {
							Statement st = dbconn.createStatement();

							if (sub.equals("unnasigned")) {
								String updateTeacherTableCommand = "UPDATE teachers SET teacherId = '" + id + "', "
										+ "preferredStartingGradeLevel = '" + grade + "', "
										+ "preferredStartingSubject = '" + sub
										+ "', hasFollowedUp = false WHERE teacherId = '" + id + "'";
								st.executeUpdate(updateTeacherTableCommand);

							} else {
								String updateTeacherTableCommand = "UPDATE teachers SET teacherId = '" + id + "', "
										+ "preferredStartingGradeLevel = '" + grade + "', "
										+ "preferredStartingSubject = '" + sub
										+ "', hasFollowedUp = true WHERE teacherId = '" + id + "'";
								st.executeUpdate(updateTeacherTableCommand);
							}

							String updateUserTableCommand = "UPDATE users SET userName = '" + name + "' WHERE id = '"
									+ id + "'";
							Statement prepSt2 = dbconn.createStatement();

							prepSt2.executeUpdate(updateUserTableCommand);

							adminDashboardFrame.dispose();
							new AdminDashboard();
							JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher data updated succesfully",
									"Update success", JOptionPane.INFORMATION_MESSAGE);

						} catch (SQLException e2) {
							e2.printStackTrace();
						}
					}
				}
			}

		} else if (e.getSource() == editTeacherBtn) {
			System.out.println("EDIT");
			String id = idTf.getText();
			String name = "null";
			if (name.isEmpty()) {
				return;
			} else {
				name = nameTf.getText();
			}

			String grade = gradeCB.getSelectedItem().toString();

			String sub = null;
			if (subCB.getSelectedItem() != null) {
				sub = subCB.getSelectedItem().toString();
			} else if (subCB.getSelectedItem() == null) {
				sub = "unnasigned";
			}

			System.out.println(grade);
			if (id.isEmpty()) {
				JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher Id should not be empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!id.isEmpty()) {
				if (teacherIdExists(id)) {
					if (dbconn != null) {
						try {

							Statement st = dbconn.createStatement();
							System.out.println(sub);
							if (sub.equals("unnasigned")) {
								String updateTeacherTableCommand = "UPDATE teachers SET teacherId = '" + id + "', "
										+ "preferredStartingGradeLevel = '" + grade + "', "
										+ "preferredStartingSubject = '" + sub
										+ "', hasFollowedUp = false WHERE teacherId = '" + id + "'";
								st.executeUpdate(updateTeacherTableCommand);

							} else {
								String updateTeacherTableCommand = "UPDATE teachers SET teacherId = '" + id + "', "
										+ "preferredStartingGradeLevel = '" + grade + "', "
										+ "preferredStartingSubject = '" + sub
										+ "', hasFollowedUp = true WHERE teacherId = '" + id + "'";
								st.executeUpdate(updateTeacherTableCommand);
							}

							String updateUserTableCommand = "UPDATE users SET userName = '" + name + "' WHERE id = '"
									+ id + "'";
							Statement prepSt2 = dbconn.createStatement();

							prepSt2.executeUpdate(updateUserTableCommand);

							adminDashboardFrame.dispose();
							new AdminDashboard();
							JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher data updated succesfully",
									"Update success", JOptionPane.INFORMATION_MESSAGE);

						} catch (SQLException e2) {
							e2.printStackTrace();
						}
					}
				} else {
					int userResponse = JOptionPane.showConfirmDialog(adminDashboardFrame,
							"Teacher Id doesn't exist, would you like to \"Add\" this data instead?",
							"This Id doesn't exists", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (userResponse == 0) {

						try {

							String tableName = initializeTeacherTable(id, name);
							if (sub.equals("unnasigned")) {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?, ?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, false);
								prepSt.executeUpdate();
							} else {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?,?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, true);
								prepSt.executeUpdate();

							}

							Date date = new Date();
							Timestamp dateCreated = new Timestamp(date.getTime());

							String addToUsersCommand = "INSERT INTO users(id, pass, userType, accCreated, userName)"
									+ "VALUES (?,?,?,?,?)";
							PreparedStatement prepSt2 = (PreparedStatement) dbconn.prepareStatement(addToUsersCommand);
							prepSt2.setString(1, id);
							prepSt2.setString(2, id);
							prepSt2.setString(3, "Teacher");
							prepSt2.setTimestamp(4, dateCreated);
							prepSt2.setString(5, name);

							prepSt2.executeUpdate();

							adminDashboardFrame.dispose();
							new AdminDashboard();
							JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher added succesfully", "Success",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (SQLException e2) {
							e2.printStackTrace();
						}

					}
				}
			}

		} else if (e.getSource() == deleteTeacherBtn) {
			System.out.println("DELETE");
			String id = idTf.getText();
			String name = "null";
			if (name.isEmpty()) {
				return;
			} else {
				name = nameTf.getText();
			}

			String grade = gradeCB.getSelectedItem().toString();

			String sub = null;
			if (subCB.getSelectedItem() != null) {
				sub = subCB.getSelectedItem().toString();
			} else if (subCB.getSelectedItem() == null) {
				sub = "unnasigned";
			}

			if (id.isEmpty()) {
				JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher Id should not be empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!id.isEmpty()) {
				if (teacherIdExists(id)) {
					int response = JOptionPane.showConfirmDialog(adminDashboardFrame,
							"Deleting here only removes this teacher from the table, \nand not from the whole database, to delete a user completely, go to the \"Manage users\" button, \n Would you still like to continue?",
							"Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (response == 0) {
						String userAttempt = JOptionPane.showInputDialog(adminDashboardFrame,
								"Please enter an admin key");
						if (userAttempt.equals(Main.ADMIN_KEY)) {
							if (dbconn != null) {
								try {
									Statement st = dbconn.createStatement();

									String tableName = null;
									String getTeacherTable = "SELECT tableName FROM teachers WHERE teacherId = '" + id
											+ "'";
									ResultSet rs = st.executeQuery(getTeacherTable);
									if (rs.next()) {
										tableName = rs.getString("tableName");
									}
									String dropTable = "DROP TABLE " + tableName + "";

									st.executeUpdate(dropTable);

									String removeFromTeachersTable = "DELETE FROM teachers WHERE teacherId = '" + id
											+ "'";

									st.executeUpdate(removeFromTeachersTable);
									// TODO: MAKE COLUROM USER TYPE LOL
									String demoteToStudentLol = "UPDATE users SET userType = 'Student' WHERE id = '"
											+ id + "'";
									Statement prepSt2 = dbconn.createStatement();

									prepSt2.executeUpdate(demoteToStudentLol);

									adminDashboardFrame.dispose();
									new AdminDashboard();
									JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher Removed", "Success",
											JOptionPane.INFORMATION_MESSAGE);

								} catch (SQLException e2) {
									e2.printStackTrace();
								}
							}
						}
					} else if (response == 1) {
						JOptionPane.showMessageDialog(adminDashboardFrame, "Action Cancelled");
					} else
						JOptionPane.showMessageDialog(adminDashboardFrame, "Action Cancelled");
				} else {
					int userResponse = JOptionPane.showConfirmDialog(adminDashboardFrame,
							"Teacher Id doesn't exist, would you like to \"Add\" this data instead?",
							"This Id doesn't exists", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (userResponse == 0) {

						try {
							String tableName = initializeTeacherTable(id, name);
							if (sub.equals("unnasigned")) {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?, ?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, false);
								prepSt.executeUpdate();
							} else {
								String addTeacherCommand = "INSERT INTO teachers(teacherId, preferredStartingGradeLevel, preferredStartingSubject, tableName, hasFollowedUp)"
										+ "VALUES (?,?,?,?,?)";
								PreparedStatement prepSt = (PreparedStatement) dbconn
										.prepareStatement(addTeacherCommand);
								prepSt.setString(1, id);
								prepSt.setString(2, grade);
								prepSt.setString(3, sub);
								prepSt.setString(4, tableName);
								prepSt.setBoolean(5, true);
								prepSt.executeUpdate();
							}

							Date date = new Date();
							Timestamp dateCreated = new Timestamp(date.getTime());

							String addToUsersCommand = "INSERT INTO users(id, pass, userType, accCreated, userName)"
									+ "VALUES (?,?,?,?,?)";
							PreparedStatement prepSt2 = (PreparedStatement) dbconn.prepareStatement(addToUsersCommand);
							prepSt2.setString(1, id);
							prepSt2.setString(2, id);
							prepSt2.setString(3, "Teacher");
							prepSt2.setTimestamp(4, dateCreated);
							prepSt2.setString(5, name);

							prepSt2.executeUpdate();

							adminDashboardFrame.dispose();
							new AdminDashboard();
							JOptionPane.showMessageDialog(adminDashboardFrame, "Teacher added succesfully", "Success",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (SQLException e2) {
							e2.printStackTrace();
						}

					}
				}
			}

		} else if (e.getSource() == printBtn) {
			System.out.println("Print!");

			String selectedTable = null;
			if (gradeLevelList.getSelectedItem() == null) {

			} else {
				selectedTable = gradeLevelList.getSelectedItem().toString();
			}

			if (selectedTable.equals("Grade 5")) {
				MessageFormat header = new MessageFormat("Grade 5 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			} else if (selectedTable.equals("Grade 6")) {
				MessageFormat header = new MessageFormat("Grade 6 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			} else if (selectedTable.equals("Grade 7")) {
				MessageFormat header = new MessageFormat("Grade 7 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			} else if (selectedTable.equals("Grade 8")) {
				MessageFormat header = new MessageFormat("Grade 8 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			} else if (selectedTable.equals("Grade 9")) {
				MessageFormat header = new MessageFormat("Grade 9 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			} else if (selectedTable.equals("Grade 10")) {
				MessageFormat header = new MessageFormat("Grade 10 Schedule");
				MessageFormat footer = new MessageFormat("@Group4");
				try {
					g5Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
				} catch (java.awt.print.PrinterAbortException e1) {
				} catch (PrinterException ex) {
					System.out.println("aaa");
				}
			}

		}

	}

	public boolean teacherIdExists(String teacherId) {
		boolean idExists = false;
		if (dbconn != null) {
			try {
				String checkIfIdExists = "SELECT teacherId FROM teachers WHERE teacherId = '" + teacherId + "'";
				Statement st = dbconn.createStatement();
				ResultSet res = st.executeQuery(checkIfIdExists);
				if (res.next()) {
					idExists = true;
				} else
					return idExists;
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return idExists;

	}

	public String initializeTeacherTable(String id, String name) throws SQLException {
		int max = 500;
		int min = 1;
		int random = (int) (Math.random() * (max - min + 1) + min);
		String randomNum = String.valueOf(random);
		String raw = name;
		String arr[] = raw.split(" ", 2);
		String tableName = arr[0] + randomNum + "schedTable";

		System.out.println("randomNum: " + randomNum);
		System.out.println("tableName: " + tableName);

		if (tableExist(dbconn, tableName)) {
			System.out.println(tableName + " already created");
		} else {

			if (dbconn != null) {
				try {
					String makeTeacherSchedTable = "CREATE TABLE IF NOT EXISTS " + tableName + "(\r\n"
							+ "	teacherId VARCHAR(45),\r\n" + "	timeRange VARCHAR(45),\r\n"
							+ "    mon VARCHAR(100),\r\n" + "    tue VARCHAR(100),\r\n" + "    wed VARCHAR(100),\r\n"
							+ "    thu VARCHAR(100),\r\n" + "    fri VARCHAR(100),\r\n" + "	isMaxed BOOLEAN\r\n" + ")";

					String insertInitialData = "INSERT INTO " + tableName
							+ " (teacherId, timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n" + "VALUES ('" + id
							+ "','7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('" + id + "','9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('" + id
							+ "','9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('" + id
							+ "','11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n" + "('"
							+ id + "','12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('" + id + "','2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('" + id
							+ "','3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = dbconn.createStatement();
					System.out.println(makeTeacherSchedTable);
					st.executeUpdate(makeTeacherSchedTable);
					st.executeUpdate(insertInitialData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return tableName;
	}

	private static boolean tableExist(Connection conn, String tableName) throws SQLException {
		String compareTable = tableName.toUpperCase();
		System.out.println(compareTable);
		boolean tExists = false;
		if (conn != null) {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(
					"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + compareTable + "'");
			if (res.next()) {
				String t = res.getString("TABLE_NAME");
				System.out.println(compareTable + " = " + t);

				tExists = true;
			} else {
				System.out.println(compareTable);
			}
		}
		System.out.println(tExists);
		return tExists;
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
