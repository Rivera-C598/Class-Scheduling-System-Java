package system;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import theme.CButton;
import theme.CLabel;
import theme.CTextField;
import theme.Colors;
import theme.Popup;
import theme.PreparedFont;
import theme.SigMethods;
import theme.Utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditGradeLevel extends Popup implements ActionListener {
	Connection dbconn = DBConnection.connectDB();
	Font myFont = new PreparedFont().myCustomFont;
	String fontString = new PreparedFont().toString(myFont);

	CTextField gradeLevelTf = new CTextField();
	CLabel promptLabel = new CLabel("Enter new grade level: ");
	CButton submitNewGradeLevel = new CButton("Submit");
	JComboBox<String> gradeList;

	public EditGradeLevel() {

		nE.add(new Utilities().forStudentPrompt(popupFrame, StudentDashboard.dashboardFrame));
		nE.setBackground(Colors.LIGHT);
		title.setText("Edit Grade Level");
		popupContentBasePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		submitNewGradeLevel.addActionListener(this);
		popupContentBasePanel.add(promptLabel);

		gradeList = new JComboBox<String>();
		gradeList.setPreferredSize(new Dimension(305, 40));
		gradeList.setFont(new Font(fontString, 0, 20));
		gradeList.setCursor(new Cursor(Cursor.HAND_CURSOR));
		try {
			String getGradeListChoices = "SELECT grade from gradeLevels";

			Statement st = dbconn.createStatement();
			ResultSet res = st.executeQuery(getGradeListChoices);
			while (res.next()) {
				gradeList.addItem("Grade " + res.getString("grade"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		popupContentBasePanel.add(gradeList);
		popupContentBasePanel.add(submitNewGradeLevel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String gradeLvl;
		gradeLvl = gradeList.getSelectedItem().toString();

		if (dbconn != null) {
			try {
				String sql = "UPDATE students SET gradeLevel = '" + gradeLvl + "' WHERE studentId = '" + Login.userId
						+ "'";
				Statement st = dbconn.createStatement();
				st.executeUpdate(sql);
				st.executeUpdate(
						"UPDATE students SET hasAddedGradeLevel = true WHERE studentId = '" + Login.userId + "'");
				SigMethods.disableGlassPane(StudentDashboard.dashboardFrame);
				popupFrame.dispose();
				
				StudentDashboard.dashboardFrame.dispose();
				new StudentDashboard();
				JOptionPane.showMessageDialog(null, "Grade level updated", "Success", JOptionPane.INFORMATION_MESSAGE);

			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

	}

}
