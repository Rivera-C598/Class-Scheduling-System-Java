package system;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import theme.CButton;
import theme.CLabel;
import theme.CTextField;
import theme.Colors;
import theme.Popup;
import theme.PreparedFont;
import theme.SigMethods;
import theme.TFLimit;
import theme.Utilities;

import java.awt.*;

public class GetTeacherAdditionalInfo extends Popup {

	Connection dbconn = DBConnection.connectDB();
	Font myFont = new PreparedFont().myCustomFont;
	String fontString = new PreparedFont().toString(myFont);

	private CLabel hint;
	private CLabel prompt;
	private JComboBox<String> gradeCB;
	private JComboBox<String> subCB;
	private CButton submitBtn;

	public GetTeacherAdditionalInfo() {

		nE.add(new Utilities().forStudentPrompt(popupFrame, TeacherDashboard.dashboardFrame));
		nE.setBackground(Colors.LIGHT);
		title.setText("Prompt");
		popupContentBasePanel.setLayout(new BorderLayout());
		prompt = new CLabel("Welcome, please do fill up some additional info below");

		int fSize = 20;
		FlowLayout flow = new FlowLayout(FlowLayout.LEADING, 0, 0);
		Dimension panelSize = new Dimension(350, 64);
		Dimension tfD = new Dimension(320, 40);
		JPanel rootPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		rootPanel.setBackground(Colors.LIGHT);

		JPanel gradePanel = new JPanel(flow);
		gradePanel.setPreferredSize(panelSize);
		gradePanel.setBackground(Colors.LIGHT);
		CLabel gradeLabel = new CLabel("You'll be teaching");
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
				String raw = gradeCB.getSelectedItem().toString();
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
		CLabel subLabel = new CLabel("Preferred Subject: ");
		subLabel.setFontSize(fSize);
		subCB = new JComboBox<String>();
		subCB.setPreferredSize(tfD);
		subCB.setFont(new Font(fontString, 0, fSize));

		subPanel.add(subLabel);
		subPanel.add(subCB);

		rootPanel.add(gradePanel);
		rootPanel.add(subPanel);

		submitBtn = new CButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String grade = gradeCB.getSelectedItem().toString();

				String sub = null;
				if (subCB.getSelectedItem() != null) {
					sub = subCB.getSelectedItem().toString();
				} else if (subCB.getSelectedItem() == null) {
					sub = "unnasigned";
				}
				
				if (dbconn != null) {
					try {
						String sql = "UPDATE teachers SET preferredStartingGradeLevel = '" + grade + "', preferredStartingSubject = '"+sub+"' WHERE teacherId = '"
								+ Login.userId + "'";
						Statement st = dbconn.createStatement();
						st.executeUpdate(sql);
						st.executeUpdate("UPDATE teachers SET hasFollowedUp = true WHERE teacherId = '"
								+ Login.userId + "'");
						
						SigMethods.disableGlassPane(TeacherDashboard.dashboardFrame);
						popupFrame.dispose();
						TeacherDashboard.dashboardFrame.dispose();
						new TeacherDashboard();
						JOptionPane.showMessageDialog(TeacherDashboard.dashboardFrame,
								"Thank you!, additional info has been added", "Success",
								JOptionPane.INFORMATION_MESSAGE);

					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}

			}

		});
		hint = new CLabel("You can always edit this later in the account page");

		hint.setFont(new Font(fontString, 2, 14));

		popupContentBasePanel.add(prompt, BorderLayout.NORTH);
		popupContentBasePanel.add(rootPanel, BorderLayout.CENTER);
		s.setPreferredSize(new Dimension(0, 70));
		s.add(submitBtn);
		s.add(hint);
	}

}
