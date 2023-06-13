package system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import theme.CButton;
import theme.CLabel;
import theme.CPanel;
import theme.Colors;
import theme.PreparedFont;
import theme.SigMethods;
import theme.Utilities;

public class DefaultDashboard implements Runnable, ActionListener {

	static Font myFont = new PreparedFont().myCustomFont;
	static String fontString = new PreparedFont().toString(myFont);

	public static JFrame dashboardFrame;

	public static CPanel viewSchedBasePanel;
	private JPanel viewSchedulePanel, accN, accL, accR;
	protected JPanel accC;
	private JPanel backToLoginPanel;
	private JPanel hcpCSubN;
	protected JPanel lgoutPanel;
	private JPanel hcpNorthBasePanel;
	private JPanel hcpNW;
	private JPanel hcpNE;
	private JPanel hcpN;
	private JPanel hcpL;
	private JPanel hcpR;
	private JPanel hcpC;
	private JPanel leftNavIconsPanel;
	private JPanel accIconPanel;
	private JPanel homeIconPanel;
	private JPanel clockPanel;
	private JPanel hcpBasePanel;
	private JPanel leftNavPanel;
	private JPanel accBasePanel;

	protected CLabel welcomeText, backToLoginBtn, accDetailsLabel, homeIcon, accIcon, lgoutIcon, timeLabel, dayLabel,
			dateLabel;

	JToggleButton menuIcon;
	CButton viewSchedBtn;

	SimpleDateFormat timeFormat, dayFormat, dateFormat;
	String time, day, date, toolTipDate;
	protected JPanel northFillerPanel;
	protected CLabel lgoutLabel;
	protected JPanel accInfoBasePanel;
	private JPanel southFillerPanel;

	public DefaultDashboard() {

		initComponents();

		Thread thread = new Thread(this);
		thread.start();

	}

	public void initComponents() {
		// JFrame
		dashboardFrame = new JFrame();
		
		
		dashboardFrame.setSize(1100, 750);
		dashboardFrame.setLocationRelativeTo(null);
		dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dashboardFrame.setUndecorated(true);
		dashboardFrame.setLayout(new BorderLayout());
		SigMethods.draggable(dashboardFrame);

		// ACC SETTINGS PANEL
		accBasePanel = new JPanel();
		accBasePanel.setBounds(0, 0, 1100, 750);
		accBasePanel.setBackground(Colors.LIGHT);
		accBasePanel.setLayout(new BorderLayout());

		// ADDING OF THE ACC BASE PANEL TO THE JFRAME IS AT THE END
		accN = new JPanel();
		accN.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		accN.setPreferredSize(new Dimension(0, 60));
		accN.setBackground(Colors.DARK);
		accBasePanel.add(accN, BorderLayout.NORTH);

		// SET VISIBLE FALSE TO BE USED LATER
		accBasePanel.setVisible(false);

		backToLoginPanel = new JPanel();
		backToLoginPanel.setBackground(Colors.DARK);

		backToLoginBtn = new CLabel();

		accDetailsLabel = new CLabel("Account details");
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
		accL.setPreferredSize(new Dimension(150, 0));
		accL.setBackground(Colors.LIGHT);
		accBasePanel.add(accL, BorderLayout.WEST);

		accR = new JPanel();
		accR.setPreferredSize(new Dimension(150, 0));
		accR.setBackground(Colors.LIGHT);
		accBasePanel.add(accR, BorderLayout.EAST);

		accC = new JPanel();
		accC.setBackground(Colors.LIGHT);
		accBasePanel.add(accC, BorderLayout.CENTER);

		accInfoBasePanel = new JPanel();
		accInfoBasePanel.setPreferredSize(new Dimension(300, 150));
		accInfoBasePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		accInfoBasePanel.setBackground(Colors.LIGHT);
		accC.add(accInfoBasePanel);

		//

		// HOME PANEL
		hcpBasePanel = new JPanel();
		hcpBasePanel.setPreferredSize(new Dimension(1040, 0));
		hcpBasePanel.setLayout(new BorderLayout());

		welcomeText = new CLabel();
		welcomeText.setText("Welcome, " + Login.userName);
		welcomeText.setForeground(Colors.DARK);
		welcomeText.setFontSize(50);
		welcomeText.fixJLabelBug(welcomeText);
		welcomeText.setOpaque(false);
		welcomeText.setBackground(Colors.ORANGE);

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
		hcpNE.add(new Utilities().darkUtilityPanelForUserDashboard(dashboardFrame));
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

		hcpCSubN = new JPanel();
		hcpCSubN.setPreferredSize(new Dimension(0, 350));
		hcpCSubN.setBackground(Colors.LIGHT);

		hcpC.add(hcpCSubN, BorderLayout.NORTH);

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
		menuIcon.setBorder(BorderFactory.createLineBorder(Colors.DARK));
		menuIcon.setBackground(Colors.DARK);
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

		accIcon = new CLabel("A");
		accIcon.setFontSize(50);
		accIcon.setForeground(Colors.LIGHT);
		accIconPanel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				accIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				accIconPanel.setBackground(Colors.LIGHT);
				accIcon.setForeground(Colors.DARK);
				accIconLabel.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				accIconPanel.setBackground(Colors.DARK);
				accIcon.setForeground(Colors.LIGHT);
				accIconLabel.setForeground(Colors.LIGHT);
			}

			public void mouseClicked(MouseEvent evt) {

				leftNavPanel.setVisible(false);
				hcpBasePanel.setVisible(false);
				accBasePanel.setVisible(true);
			}
		});
		accIconPanel.setVisible(false);

		// LGOUT PANEL
		lgoutPanel = new JPanel();
		lgoutPanel.setPreferredSize(new Dimension(60, 80));
		lgoutPanel.setBackground(Colors.DARK);

		lgoutLabel = new CLabel("Log Out");
		lgoutLabel.setFontSize(10);
		lgoutLabel.setForeground(Colors.LIGHT);

		lgoutIcon = new CLabel("L");
		lgoutIcon.setFontSize(50);
		lgoutIcon.setForeground(Colors.LIGHT);
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

					dispose(dashboardFrame);
					
				}

			}
		});

		lgoutPanel.add(lgoutIcon);
		lgoutPanel.add(lgoutLabel);
		lgoutPanel.setVisible(false);

		// VOTE PANEL

		viewSchedBasePanel = new CPanel();
		viewSchedBasePanel.contentBasePanel.setLayout(new BorderLayout());
		viewSchedBasePanel.contentBasePanel.setBackground(Colors.LIGHT);


		northFillerPanel = new JPanel();
		northFillerPanel.setBackground(Colors.LIGHT);
		northFillerPanel.setPreferredSize(new Dimension(0, 20));
		viewSchedBasePanel.contentBasePanel.add(northFillerPanel, BorderLayout.NORTH);

		southFillerPanel = new JPanel();
		southFillerPanel.setBackground(Colors.LIGHT);
		southFillerPanel.setPreferredSize(new Dimension(0, 5));

		viewSchedBasePanel.contentBasePanel.add(southFillerPanel, BorderLayout.SOUTH);
		
		

		viewSchedulePanel = new JPanel();
		viewSchedulePanel.setBackground(Colors.LIGHT);
		viewSchedulePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		hcpCSubN.add(viewSchedulePanel);

		viewSchedBtn = new CButton("View Schedule");

		viewSchedBtn.setPreferredSize(new Dimension(420, 60));
		viewSchedBtn.addActionListener(this);
		viewSchedBtn.setVisible(true);

		viewSchedulePanel.add(viewSchedBtn);
		// changeThemePanel.add(changeThemeBtn);

		// CLOCK

		timeFormat = new SimpleDateFormat("hh:mm a");
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

		// ADDING OF COMPONENTS IN ORDER
		homeIconPanel.add(homeIcon);
		homeIconPanel.add(homeLabel);

		accIconPanel.add(accIcon);
		accIconPanel.add(accIconLabel);

		leftNavIconsPanel.add(menuIcon);
		leftNavIconsPanel.add(accIconPanel);
		leftNavIconsPanel.add(lgoutPanel);
		leftNavIconsPanel.add(homeIconPanel);

		leftNavPanel.add(leftNavIconsPanel, BorderLayout.CENTER);
		leftNavPanel.add(clockPanel, BorderLayout.SOUTH);

		hcpBasePanel.add(hcpNorthBasePanel, BorderLayout.NORTH);
		hcpBasePanel.add(hcpL, BorderLayout.WEST);
		hcpBasePanel.add(hcpR, BorderLayout.EAST);
		hcpBasePanel.add(hcpC, BorderLayout.CENTER);

		dashboardFrame.add(newPage(viewSchedBasePanel, "Schedule"));

		dashboardFrame.add(accBasePanel);
		dashboardFrame.add(leftNavPanel, BorderLayout.WEST);
		dashboardFrame.add(hcpBasePanel, BorderLayout.EAST);

		dashboardFrame.setVisible(true);
	}

	private CPanel newPage(CPanel panel, String panelTitle) {

		panel.setCustomPanelTitle(panelTitle);
		panel.customFunc(accBasePanel, hcpBasePanel, leftNavPanel, panel);
		panel.setVisible(false);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewSchedBtn) {

			panelSwitch(viewSchedBasePanel);

		}

	}

	public String toString(char arr[]) {
		String string = new String(arr);

		return string;
	}

	public void panelSwitch(JPanel chosenPanel) {
		leftNavPanel.setVisible(false);
		hcpBasePanel.setVisible(false);
		accBasePanel.setVisible(false);
		viewSchedBasePanel.setVisible(false);

		chosenPanel.setVisible(true);
	}

	// REFRESH
	public void refresh() {

		dashboardFrame.dispose();
		new DefaultDashboard();
	}
	
	public void dispose(JFrame frame) {
		EventQueue.invokeLater(() -> {
			
			frame.dispose();
			new Login();
		});
	}

	// THIS SETS THE CLOCK AND RUNS IT ON A DIFFERENT THREAD

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

}
