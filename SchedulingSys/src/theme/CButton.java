package theme;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;


public class CButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font defaultFont = new PreparedFont().myCustomFont;
	Font newFontWSize;
	
	public CButton() {
		this(null, null);
		setDefaultProperty();
	}

	public CButton(String text) {
		this(text, null);
		setDefaultProperty();
	}

	public CButton(Icon icon) {
		this(null, icon);
		setDefaultProperty();
	}

	public CButton(String text, Icon icon) {
		setDefaultProperty();
		setModel(new DefaultButtonModel());

		init(text, icon);
	}

	void setDefaultProperty() {
		newFontWSize = defaultFont.deriveFont(25f);
		setFont(newFontWSize);
		setPreferredSize(new Dimension(300, 60));
		setBackground(Colors.DARK);
		setForeground(Colors.LIGHT);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setFocusable(false);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				setBackground(Colors.LIGHT);
				setForeground(Colors.DARK);
				setBorder(BorderFactory.createLineBorder(Colors.DARK));
			}

			public void mouseExited(MouseEvent evt) {
				setBackground(Colors.DARK);
				setForeground(Colors.LIGHT);
			}
		});
	}
	public void setFontSize(float size) {
		newFontWSize = defaultFont.deriveFont(size);
		setFont(newFontWSize);
	}


}
