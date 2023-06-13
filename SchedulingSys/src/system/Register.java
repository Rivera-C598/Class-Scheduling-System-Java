package system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import theme.CButton;
import theme.CLabel;
import theme.CPassField;
import theme.CTextField;
import theme.Colors;
import theme.PreparedFont;
import theme.SigMethods;
import theme.TFLimit;
import theme.Utilities;

public class Register implements ActionListener {

	Connection dbconn = DBConnection.connectDB();

	Font myFont = new PreparedFont().myCustomFont;
	String fontString = new PreparedFont().toString(myFont);

	JFrame registerFrame;
	private CTextField idTextField, nameTf;
	private CPassField passField, confrmPassFld;
	private CButton registerBtn;
	private JCheckBox showPassIcon;
	private CLabel confrmPassErrPromptLabel;
	private JComboBox<String> userTypeComboBox;

	public Register() {
		initComponents();
	}

	private void initComponents() {

		registerFrame = new JFrame("Register");
		registerFrame.setSize(700, 750);
		registerFrame.setLayout(new BorderLayout());
		registerFrame.setResizable(false);
		registerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		registerFrame.setLocationRelativeTo(null);
		registerFrame.setUndecorated(true);
		SigMethods.draggable(registerFrame);
		// registerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		

		registerFrame.add(contentPanel(), BorderLayout.CENTER);

		registerFrame.setVisible(true);

	}

	private JPanel contentPanel() {
		JPanel basePanel = new JPanel(new BorderLayout());
		basePanel.setBackground(Colors.LIGHT);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Colors.LIGHT);
		northPanel.setPreferredSize(new Dimension(0, 45));
		northPanel.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Colors.DARK));
		basePanel.add(northPanel, BorderLayout.NORTH);

		JPanel nW = new JPanel();
		nW.setBackground(Colors.LIGHT);

		northPanel.add(nW, BorderLayout.CENTER);

		JPanel nE = new JPanel();
		nE.setBackground(Colors.LIGHT);

		nE.add(new Utilities().darkUtilityPanel(registerFrame));

		northPanel.add(nE, BorderLayout.EAST);

		JPanel westPanel = new JPanel();
		westPanel.setBackground(Colors.LIGHT);
		westPanel.setPreferredSize(new Dimension(180, 0));
		basePanel.add(westPanel, BorderLayout.WEST);

		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(Colors.LIGHT);
		eastPanel.setPreferredSize(new Dimension(180, 0));
		basePanel.add(eastPanel, BorderLayout.EAST);

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Colors.LIGHT);
		southPanel.setPreferredSize(new Dimension(0, 50));
		southPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		basePanel.add(southPanel, BorderLayout.SOUTH);

		////////// TITLE PANEL ///////////////////////////////

		CLabel label = new CLabel("Register");
		label.setFontSize(36);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Colors.LIGHT);
		titlePanel.setPreferredSize(new Dimension(0, 65));
		titlePanel.add(label);

		//////////////////////////////////////////////////////

		//////////////////////// ID PANEL ////////////////////////////////

		CLabel idLabel = new CLabel("Id");
		idTextField = new CTextField();
		idTextField.setDocument(new TFLimit(7));

		JPanel idFormPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		idFormPanel.setPreferredSize(new Dimension(320, 75));
		idFormPanel.setBackground(Colors.LIGHT);
		idFormPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		idFormPanel.add(idLabel);
		idFormPanel.add(idTextField);

		/////////////////////////////////////////////////////

		/////////// NAME PANEL //////////////////
		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(320, 75));
		namePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		namePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		namePanel.setBackground(Colors.LIGHT);

		CLabel nameLabel = new CLabel("Name");
		namePanel.add(nameLabel);

		nameTf = new CTextField();

		namePanel.add(nameTf);

		//////////////////////////////////////////

		//////////// PASSWORD PANEL //////////////////

		CLabel passLabel = new CLabel("Password");
		passField = new CPassField();

		JPanel passFormPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		passFormPanel.setPreferredSize(new Dimension(320, 75));
		passFormPanel.setBackground(Colors.LIGHT);
		passFormPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		passFormPanel.add(passLabel);
		passFormPanel.add(passField);

		//////////////////////////////////////////////////////

		//////////// CONFIRM PASSWORD PANEL //////////////////
		JPanel confrmPassPanel = new JPanel();
		confrmPassPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		confrmPassPanel.setPreferredSize(new Dimension(320, 95));
		confrmPassPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		confrmPassPanel.setBackground(Colors.LIGHT);

		CLabel confrmPassLabel = new CLabel("Confirm Password");
		confrmPassFld = new CPassField();

		confrmPassPanel.add(confrmPassLabel);
		confrmPassPanel.add(confrmPassFld);

		JPanel confrmPassValidatorPanel = new JPanel();
		confrmPassValidatorPanel.setBackground(Colors.LIGHT);
		confrmPassValidatorPanel.setLayout(new BorderLayout());

		confrmPassErrPromptLabel = new CLabel();
		confrmPassErrPromptLabel.setFont(new Font(fontString, 2, 12));
		confrmPassErrPromptLabel.setForeground(Color.red);

		JPanel confrmEP = new JPanel();
		confrmEP.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		confrmEP.setPreferredSize(new Dimension(155, 25));
		confrmEP.setBackground(Colors.LIGHT);
		confrmEP.add(confrmPassErrPromptLabel);

		JPanel showAllPassP = new JPanel();
		showAllPassP.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 0));
		showAllPassP.setPreferredSize(new Dimension(155, 0));
		showAllPassP.setBackground(Colors.LIGHT);

		showPassIcon = new JCheckBox("Show Password");
		showPassIcon.setIcon(new ImageIcon("image_assets/Rgstr_Imgs/hidePassIcon.png"));
		showPassIcon.setRolloverEnabled(false);
		showPassIcon.setSelectedIcon(new ImageIcon("image_assets/Rgstr_Imgs/showPassIcon.png"));
		showPassIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		showPassIcon.setFocusable(false);
		showPassIcon.setBackground(Colors.LIGHT);
		showPassIcon.setFont(new Font(fontString, 0, 12));
		showPassIcon.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					passField.setEchoChar((char) 0);
					confrmPassFld.setEchoChar((char) 0);
				} else {
					passField.setEchoChar('*');
					confrmPassFld.setEchoChar('*');
				}
			}
		});

		showAllPassP.add(showPassIcon);
		confrmPassValidatorPanel.add(confrmEP, BorderLayout.WEST);
		confrmPassValidatorPanel.add(showAllPassP, BorderLayout.EAST);

		confrmPassPanel.add(confrmPassValidatorPanel);

		JPanel userTypePanel = new JPanel();
		userTypePanel.setPreferredSize(new Dimension(320, 75));
		userTypePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		userTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		userTypePanel.setBackground(Colors.LIGHT);
		CLabel userTypeLabel = new CLabel("Select User Type");
		userTypePanel.add(userTypeLabel);

		String userTypeChoices[] = { "Student", "Teacher", "Admin" };

		userTypeComboBox = new JComboBox<String>(userTypeChoices);
		userTypeComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
		userTypeComboBox.setFont(myFont);

		userTypeComboBox.setPreferredSize(new Dimension(305, 40));
		userTypePanel.add(userTypeComboBox);

		JPanel formsBasePanel = new JPanel();
		formsBasePanel.setBackground(Colors.LIGHT);

		formsBasePanel.add(userTypePanel);
		formsBasePanel.add(idFormPanel);
		formsBasePanel.add(namePanel);
		formsBasePanel.add(passFormPanel);
		formsBasePanel.add(confrmPassPanel);

		registerBtn = new CButton("Register");
		registerBtn.addActionListener(this);
		registerBtn.setPreferredSize(new Dimension(300, 55));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(new Dimension(0, 75));
		buttonsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Colors.DARK));
		buttonsPanel.setBackground(Colors.LIGHT);
		buttonsPanel.add(registerBtn);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Colors.LIGHT);

		CLabel backToLoginBtn = new CLabel("Back to login");
		backToLoginBtn.setForeground(Colors.DARK);
		backToLoginBtn.setFontSize(18);

		backToLoginBtn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				backToLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				backToLoginBtn.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				backToLoginBtn.setForeground(Colors.DARK);
			}

			public void mouseClicked(MouseEvent evt) {
				registerFrame.dispose();
				new Login();
			}
		});
		southPanel.add(backToLoginBtn);

		centerPanel.add(titlePanel, BorderLayout.NORTH);
		centerPanel.add(formsBasePanel, BorderLayout.CENTER);
		centerPanel.add(buttonsPanel, BorderLayout.SOUTH);

		basePanel.add(centerPanel, BorderLayout.CENTER);

		return basePanel;
	}

	// method to only allow digits in the school id input
	private void idIfKeyTyped(KeyEvent evt) {
		if (!Character.isDigit(evt.getKeyChar())) {
			evt.consume();
		}
	}

	String id, name, password, confirmPassword, userType;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == registerBtn) {

			// ENTER REGISTRATION DETAILS TO DATABASE HERE

			userType = userTypeComboBox.getSelectedItem().toString();
			id = idTextField.getText();
			name = nameTf.getText();
			password = String.valueOf(passField.getPassword());
			confirmPassword = String.valueOf(confrmPassFld.getPassword());

			Date date = new Date();
			Timestamp dateCreated = new Timestamp(date.getTime());

			// VERIFICATION

			if (id.trim().isBlank() || name.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
				JOptionPane.showMessageDialog(registerFrame, "Some fields are empty", "Error",
						JOptionPane.ERROR_MESSAGE);

			} else if (name.matches(".*\\d.*")) {
				JOptionPane.showMessageDialog(registerFrame, "Invalid Name", "Error", JOptionPane.ERROR_MESSAGE);

			} else if (password.length() < 4 || confirmPassword.length() < 4) {
				confrmPassErrPromptLabel.setText("Password should be atleast 4 characters long");
			} else if (password == null ? confirmPassword != null : !password.equals(confirmPassword)) {
				confrmPassErrPromptLabel.setText("");
				confrmPassErrPromptLabel.setText("Passwords do not match");
			} else if (!checkSchoolIDExists(id)) {
				confrmPassErrPromptLabel.setText("");
				userRegisterToDatabase(id, name, password, userType, dateCreated);
			}

		}

	}

	public boolean checkSchoolIDExists(String schoolId) {
		boolean schoolIDExists = false;

		if (dbconn != null) {
			try {

				PreparedStatement st = (PreparedStatement) dbconn.prepareStatement("SELECT * FROM users WHERE id = ?");

				st.setString(1, schoolId);

				ResultSet res = st.executeQuery();
				if (res.next()) {
					schoolIDExists = true;
					JOptionPane.showMessageDialog(null, "This ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException ex) {
				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return schoolIDExists;
	}

	private void userRegisterToDatabase(String id, String name, String password, String userType,
			Timestamp dateCreated) {
		if (dbconn != null) {
			try {
				PreparedStatement ps = (PreparedStatement) dbconn.prepareStatement(
						"INSERT INTO users (id, userName, pass, accCreated, userType)VALUES(?,?,?,?,?)");

				ps.setString(1, id);
				ps.setString(2, name);
				ps.setString(3, password);
				ps.setTimestamp(4, dateCreated);
				ps.setString(5, userType);

				ps.executeUpdate();

				if (userType.equals("Student")) {
					try {
						PreparedStatement pS = (PreparedStatement) dbconn
								.prepareStatement("INSERT INTO students (studentId, hasAddedGradeLevel)VALUES(?,?)");

						pS.setString(1, id);
						pS.setBoolean(2, false);

						pS.executeUpdate();

						JOptionPane.showMessageDialog(registerFrame, "Student Account Registered", "Success",
								JOptionPane.INFORMATION_MESSAGE);

						idTextField.setText("");
						nameTf.setText("");
						passField.setText("");
						confrmPassFld.setText("");

					} catch (SQLException e) {
						e.printStackTrace();
					}

				} else if (userType.equals("Teacher")) {
					try {
						
						
						String tableName = initializeTeacherTable(id, name);
						
						
						PreparedStatement pSt = (PreparedStatement) dbconn
								.prepareStatement("INSERT INTO teachers (teacherId, hasFollowedUp, tableName)VALUES(?,?,?)");

						pSt.setString(1, id);
						pSt.setBoolean(2, false);
						pSt.setString(3, tableName);

						pSt.executeUpdate();

						

						JOptionPane.showMessageDialog(registerFrame, "Teacher Account Registered", "Success",
								JOptionPane.INFORMATION_MESSAGE);

						idTextField.setText("");
						nameTf.setText("");
						passField.setText("");
						confrmPassFld.setText("");

					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (userType.equals("Admin")) {
					String adminKey = "root";
					String keyEntered = JOptionPane.showInputDialog(null, "Please Enter an Admin Key");
					if (keyEntered.equals(adminKey)) {
						JOptionPane.showMessageDialog(null, "Admin Key Confirmed", "Admin Key Confirmation Message",
								JOptionPane.INFORMATION_MESSAGE);

						try {
							PreparedStatement pStmt = (PreparedStatement) dbconn
									.prepareStatement("INSERT INTO admins (userId)VALUES(?)");

							pStmt.setString(1, id);

							pStmt.executeUpdate();

							JOptionPane.showMessageDialog(registerFrame, "Admin Account Registered", "Success",
									JOptionPane.INFORMATION_MESSAGE);

							idTextField.setText("");
							nameTf.setText("");
							passField.setText("");
							confrmPassFld.setText("");

						} catch (SQLException e) {
							e.printStackTrace();
						}

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public String initializeTeacherTable(String id, String name) throws SQLException {
		int max = 500;
		int min = 1;
		int random = (int) (Math.random() * (max - min + 1) + min);
		String randomNum = String.valueOf(random);
		String raw = name;
		String arr[] = raw.split(" ", 2);
		String tableName = arr[0] + randomNum + "schedTable";

		if(tableExist(dbconn, tableName)) {System.out.println(tableName + " already created");}else {
			if (dbconn != null) {
				try {
					String makeTeacherSchedTable = "CREATE TABLE IF NOT EXISTS " + tableName + "(\r\n"
				+ "	teacherId VARCHAR(45),\r\n" + "	timeRange VARCHAR(45),\r\n"
				+ "    mon VARCHAR(100),\r\n" + "    tue VARCHAR(100),\r\n"
				+ "    wed VARCHAR(100),\r\n" + "    thu VARCHAR(100),\r\n"
				+ "    fri VARCHAR(100),\r\n" + "	isMaxed BOOLEAN\r\n" + ")";
					
					String insertInitialData = "INSERT INTO "+tableName+" (teacherId, timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('"+id+"','7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('"+id+"','9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('"+id+"','9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('"+id+"','11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('"+id+"','12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('"+id+"','2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('"+id+"','3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = dbconn.createStatement();
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
		if(conn!=null) {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '"+compareTable+"'");
			if(res.next()) {
				String t = res.getString("TABLE_NAME");
				System.out.println(compareTable + " = " + t);
				
				tExists = true;
			}else {
				System.out.println(compareTable);
			}
		}
		System.out.println(tExists);
		return tExists;
	}

	

}
