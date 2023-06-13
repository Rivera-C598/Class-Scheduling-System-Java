package theme;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;


public class CLabel extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font defaultFont = new PreparedFont().myCustomFont;
	
	public CLabel() {
		setFont(defaultFont);
		setForeground(Colors.DARK);
		
	}

	public CLabel(String text) {
		setText(text);
		setFont(defaultFont);
		setForeground(Colors.DARK);
	}
	
	public void fixJLabelBug(CLabel label) {
		label.setPreferredSize(new Dimension(label.getPreferredSize().width, label.getPreferredSize().height + 3));
	}
	
	
	public void setFontSize(float fontSize) {
		Font newFontSize = defaultFont.deriveFont(fontSize);
		setFont(newFontSize);
	}
	public void setNewFont(Font fontName) {
		setFont(fontName);
	}
	public void setNewFont(Font fontName, float fontSize) {
		Font newFontSize = fontName.deriveFont(fontSize);
		setFont(newFontSize);
	}
}
