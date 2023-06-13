package theme;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.LayerUI;

public class Popup {

	protected JFrame popupFrame;
	JPanel popupBasePanel;
	protected JPanel popupContentBasePanel;
	protected JPanel n, e, w, s;
	JPanel nW;
	public static JPanel nE;
	protected JPanel nC;
	protected CLabel title;
	CButton startBtn;

	public Popup() {

		popupFrame = new JFrame();
		popupFrame.setSize(610, 410);
		popupFrame.setLayout(new BorderLayout());
		popupFrame.setUndecorated(true);
		popupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popupFrame.setResizable(false);
		popupFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Colors.DARK));
		popupFrame.setLocationRelativeTo(null);
		popupFrame.setAlwaysOnTop(true);

		popupBasePanel = new JPanel(new BorderLayout());
		popupBasePanel.setBackground(Colors.LIGHT);

		popupContentBasePanel = new JPanel();
		popupContentBasePanel.setBackground(Colors.LIGHT);
		popupContentBasePanel.setBorder(new EmptyBorder(20, 0, 0, 0));

		n = new JPanel();
		n.setLayout(new BorderLayout());
		n.setPreferredSize(new Dimension(0, 54));
		n.setBackground(Colors.DARK);
		n.setBorder(new MatteBorder(0, 0, 5, 0, Colors.ORANGE));

		nW = new JPanel();
		nW.setBackground(Colors.DARK);
		nW.setPreferredSize(new Dimension(35, 0));

		nE = new JPanel();
		nE.setBackground(Colors.LIGHT);
		nE.setPreferredSize(new Dimension(35, 0));

		title = new CLabel("Popup Title");
		title.setForeground(Colors.LIGHT);

		nC = new JPanel();
		nC.setBackground(Colors.DARK);
		nC.add(title);

		e = new JPanel();
		e.setLayout(new BorderLayout());
		e.setPreferredSize(new Dimension(20, 0));
		e.setBackground(Colors.LIGHT);

		w = new JPanel();
		w.setLayout(new BorderLayout());
		w.setPreferredSize(new Dimension(20, 0));
		w.setBackground(Colors.LIGHT);

		s = new JPanel();
		s.setPreferredSize(new Dimension(0, 60));
		s.setBackground(Colors.LIGHT);

		n.add(nW, BorderLayout.WEST);
		n.add(nE, BorderLayout.EAST);
		n.add(nC, BorderLayout.CENTER);

		popupBasePanel.add(n, BorderLayout.NORTH);
		popupBasePanel.add(e, BorderLayout.EAST);
		popupBasePanel.add(w, BorderLayout.WEST);
		popupBasePanel.add(s, BorderLayout.SOUTH);
		popupBasePanel.add(popupContentBasePanel, BorderLayout.CENTER);

		popupFrame.add(popupBasePanel, BorderLayout.CENTER);

		popupFrame.setVisible(true);

	}

}
