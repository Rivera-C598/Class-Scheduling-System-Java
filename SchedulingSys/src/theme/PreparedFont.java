package theme;

import java.awt.Font;


public class PreparedFont {
	
public Font myCustomFont = new Font("Consolas", 0, 20);
	
	
	public Font setFontModifications(Font font, float fontSize) {
		Font f = font.deriveFont(fontSize);
		return f;
	}
	public String toString(Font myFont) {
		return myFont.getFontName();
	}

}
