package system;

import javax.swing.*;
import theme.Utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import theme.CButton;
import theme.CLabel;
import theme.CPassField;
import theme.CTextField;
import theme.Colors;
import theme.PreparedFont;
import theme.SigMethods;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;;

public class Login implements ActionListener {

	Connection dbconn = DBConnection.connectDB();
	
	Font myFont = new PreparedFont().myCustomFont;
	String fontString = new PreparedFont().toString(myFont);

	JFrame loginFrame;
	CButton loginBtn;

	private CTextField idTextField;

	private CPassField passField;

	public Login() {
		initComponents();
	}

	private void initComponents() {

		loginFrame = new JFrame("Login");
		loginFrame.setSize(700, 500);
		loginFrame.setLayout(new BorderLayout());
		loginFrame.setResizable(false);
		loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setUndecorated(true);
		SigMethods.draggable(loginFrame);
		// loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		
		

		loginFrame.add(contentPanel(), BorderLayout.CENTER);

		loginFrame.setVisible(true);

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
		
		nE.add(new Utilities().darkUtilityPanel(loginFrame));
		
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
		southPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		southPanel.setPreferredSize(new Dimension(0, 60));
		basePanel.add(southPanel, BorderLayout.SOUTH);

		CLabel label = new CLabel("Log in");
		label.setFontSize(36);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Colors.LIGHT);
		titlePanel.setPreferredSize(new Dimension(0, 75));
		titlePanel.add(label);

		CLabel idLabel = new CLabel("Id");
		idTextField = new CTextField();
		idTextField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				idIfKeyTyped(evt);
				if (idTextField.getText().length() >= 7
						&& !(evt.getKeyChar() == KeyEvent.VK_DELETE || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
					loginFrame.getToolkit().beep();
					evt.consume();
				}
			}
		});

		JPanel idFormPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		idFormPanel.setPreferredSize(new Dimension(320, 75));
		idFormPanel.setBackground(Colors.LIGHT);
		idFormPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		idFormPanel.add(idLabel);
		idFormPanel.add(idTextField);

		CLabel passLabel = new CLabel("Password");
		passField = new CPassField();

		JPanel passFormPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		passFormPanel.setPreferredSize(new Dimension(320, 95));
		passFormPanel.setBackground(Colors.LIGHT);
		passFormPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		passFormPanel.add(passLabel);
		passFormPanel.add(passField);
		
		JPanel additionalUtilityPanel = new JPanel();
		additionalUtilityPanel.setBackground(Colors.LIGHT);
		additionalUtilityPanel.setLayout(new BorderLayout());
		additionalUtilityPanel.setPreferredSize(new Dimension(305, 60));
		
		
		CLabel forgotPassLabel = new CLabel("Forgot Password?");
		forgotPassLabel.setFont(new Font(fontString, 2, 14));
		forgotPassLabel.fixJLabelBug(forgotPassLabel);
		forgotPassLabel.setForeground(Colors.DARK);
		forgotPassLabel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				forgotPassLabel.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				forgotPassLabel.setForeground(Colors.DARK);
			}

			public void mouseClicked(MouseEvent evt) {
				
				JOptionPane.showMessageDialog(loginFrame, "sorry, this feature is not available at the moment","Schedule creator v1.1", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});

		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 8));
		labelContainer.setPreferredSize(new Dimension(155, 25));
		labelContainer.setBackground(Colors.LIGHT);
		labelContainer.add(forgotPassLabel);

		JPanel showPassPanel = new JPanel();
		showPassPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 0));
		showPassPanel.setPreferredSize(new Dimension(155, 0));
		showPassPanel.setBackground(Colors.LIGHT);

		JCheckBox showPassIcon = new JCheckBox();
		showPassIcon.setIcon(new ImageIcon(getClass().getResource("/image_assets/Rgstr_Imgs/hidePassIcon.png")));
		showPassIcon.setRolloverEnabled(false);
		showPassIcon.setSelectedIcon(new ImageIcon(getClass().getResource("/image_assets/Rgstr_Imgs/showPassIcon.png")));
		showPassIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		showPassIcon.setFocusable(false);
		showPassIcon.setBackground(Colors.LIGHT);
		showPassIcon.setFont(new Font(fontString, 0, 16));
		showPassIcon.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					passField.setEchoChar((char) 0);
				} else {
					passField.setEchoChar('*');
				}
			}
		});

		showPassPanel.add(showPassIcon);
		additionalUtilityPanel.add(labelContainer, BorderLayout.WEST);
		additionalUtilityPanel.add(showPassPanel, BorderLayout.EAST);

		passFormPanel.add(additionalUtilityPanel);

		loginBtn = new CButton("Log in");
		loginBtn.addActionListener(this);
		loginBtn.setPreferredSize(new Dimension(300, 55));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(new Dimension(0, 75));
		buttonsPanel.setBorder(new MatteBorder(0, 0, 2, 0, Colors.DARK));
		buttonsPanel.setBackground(Colors.LIGHT);
		buttonsPanel.add(loginBtn);

		CLabel registerBtn = new CLabel("Register");
		registerBtn.setForeground(Colors.DARK);
		registerBtn.setFontSize(18);

		registerBtn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				registerBtn.setForeground(Colors.DARK);
			}

			public void mouseExited(MouseEvent evt) {
				registerBtn.setForeground(Colors.DARK);
			}

			public void mouseClicked(MouseEvent evt) {
				loginFrame.dispose();
				new Register();
			}
		});
		southPanel.add(registerBtn);

		JPanel formsBasePanel = new JPanel();
		formsBasePanel.setBackground(Colors.LIGHT);
		formsBasePanel.add(idFormPanel);
		formsBasePanel.add(passFormPanel);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Colors.LIGHT);

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

	public boolean verifyUser(String id) {

		boolean userMatch = false;
		if (dbconn != null) {
			try {
				PreparedStatement st = (PreparedStatement) dbconn
						.prepareStatement("SELECT * FROM users WHERE id = ?");

				st.setString(1, id);

				ResultSet res = st.executeQuery();

				if (res.next()) {
					// if there is a match
					userMatch = true;
				}

			} catch (SQLException ex) {
				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return userMatch;
	}

	public boolean verifyPass(String password) {
		boolean passMatch = false;

		if (dbconn != null) {
			try {

				PreparedStatement st = (PreparedStatement) dbconn
						.prepareStatement("SELECT * FROM users WHERE pass = ?");

				st.setString(1, password);

				ResultSet res = st.executeQuery();

				if (res.next()) {
					// if there is a match
					passMatch = true;
				}
			} catch (SQLException ex) {
				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return passMatch;
	}

	public static int userAdminId = 0;
	public static String userId, userName, userType;

	private void userLogin(String id, String password) {
		if (dbconn != null) {
			try {

				PreparedStatement st = (PreparedStatement) dbconn
						.prepareStatement("SELECT * FROM users WHERE id = ? AND pass = ?");

				st.setString(1, id);
				st.setString(2, password);

				ResultSet res = st.executeQuery();

				if (res.next()) {

					// METHOD TO GET LOGGED IN USER INFO TO BE USED BY THE ENTIRE SYSTEM
					extractUserInfo(res);

					// CHECKING USER STATUS
					
					String userType = res.getString("userType");

					if (userType.equals("Student")) {


						//userAdminId = extractAdminId();

						EventQueue.invokeLater(() -> {
							new StudentDashboard();
						});

					} else if(userType.equals("Teacher")){

						
						EventQueue.invokeLater(() -> {
							new TeacherDashboard();
						});

					}else if (userType.equals("Admin")) {
						userAdminId = extractAdminId(id);
						
						EventQueue.invokeLater(() -> {
							new AdminDashboard();
						});
					}

				} else {
					JOptionPane.showMessageDialog(null, "Id / password not found", "Error",
							JOptionPane.ERROR_MESSAGE);
					EventQueue.invokeLater(() -> {
						new Login();
					});

				}
			} catch (SQLException ex) {
				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			System.out.println("The connection not available.");
		}
	}

	private void extractUserInfo(ResultSet result) throws SQLException {
		userId = result.getString("id");
		userName = result.getString("userName");
		userType = result.getString("userType");
	}
	
	private int extractAdminId(String id) {
		int adminId = 0;
		if(dbconn!=null) {
			try {
				Statement st = dbconn.createStatement();
				String getAdminIdCommand = "SELECT adminId from admins WHERE userId = '"+id+"'";
				ResultSet res = st.executeQuery(getAdminIdCommand);
				if(res.next()) {
					adminId = res.getInt("adminId");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return adminId;
	}
	
	String id, password;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginBtn) {
			// DATABASE
			// VERIFICATION
			// GETTING/GATHERING INPUTS
		
			id = idTextField.getText();
			password = String.valueOf(passField.getPassword());

			if (id.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(loginFrame, "Fields should not be empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (verifyUser(id)) {
				if (verifyPass(password)) {

					userLogin(id, password);

					loginFrame.dispose();

				} else {
					JOptionPane.showMessageDialog(null, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Id not found", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
