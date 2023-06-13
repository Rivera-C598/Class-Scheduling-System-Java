package theme;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.text.Document;


public class CPassField extends JPasswordField{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font defaultFont = new PreparedFont().myCustomFont;

	String fontString = new PreparedFont().toString(defaultFont);
	
	public CPassField() {
		this(null, null, 0);
		setCustomProperty();
	}

	public CPassField(String text) {
		this(null, text, 0);
		setCustomProperty();
	}

	public CPassField(int columns) {
		this(null, null, columns);
		setCustomProperty();
	}

	public CPassField(String text, int columns) {
		this(null, text, columns);
		setCustomProperty();
	}

	public CPassField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		enableInputMethods(false);
		setCustomProperty();
	}

	void setCustomProperty() {
		setFont(new Font(fontString,0,25));
		setForeground(Colors.DARK);
		setBackground(Colors.MILD);
		setEchoChar('*');
		setMargin(new Insets(2, 2, 2, 2));
		setPreferredSize(new Dimension(305, 40));
		setBorder(BorderFactory.createBevelBorder(1));

	}
}
