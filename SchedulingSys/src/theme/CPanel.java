package theme;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import system.Login;

public class CPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel n;
	public JPanel e;
	public JPanel w;
	public JPanel contentBasePanel;
	public JPanel s;

	public JPanel backBtnPanel, titleLabelPanel, eastFillerPanel;
	public CLabel backBtn;
	CLabel customPanelTitle;

	int half = 522;

	public CPanel() {
		this(true);

		initComponents();

	}

	public CPanel(int type) {
		this(true);
		if (type == 1) {
			initComponents();
			this.setBounds(0, 0, 500, 750);
		}

	}

	void initComponents() {
		this.setBounds(0, 0, 1100, 750);
		this.setBackground(Colors.LIGHT);
		this.setLayout(new BorderLayout());

		contentBasePanel = new JPanel();
		contentBasePanel.setLayout(new BorderLayout());
		contentBasePanel.setBackground(Colors.LIGHT);
		contentBasePanel.setBorder(new EmptyBorder(20, 0, 0, 0));

		n = new JPanel();
		n.setLayout(new BorderLayout());
		n.setPreferredSize(new Dimension(0, 54));
		n.setBackground(Colors.DARK);
		n.setBorder(new MatteBorder(0, 0, 5, 0, Colors.ORANGE));

		e = new JPanel();
		e.setLayout(new BorderLayout());
		e.setPreferredSize(new Dimension(20, 0));
		e.setBackground(Colors.LIGHT);

		w = new JPanel();
		w.setLayout(new BorderLayout());
		w.setPreferredSize(new Dimension(20, 0));
		w.setBackground(Colors.LIGHT);

		s = new JPanel();
		s.setPreferredSize(new Dimension(0, 15));
		s.setBackground(Colors.LIGHT);

		backBtnPanel = new JPanel();
		backBtnPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		backBtnPanel.setBackground(Colors.DARK);

		customPanelTitle = new CLabel("Title");
		customPanelTitle.setForeground(Colors.LIGHT);
		customPanelTitle.setFontSize(30);
		

		titleLabelPanel = new JPanel();
		titleLabelPanel.setBackground(Colors.DARK);
		titleLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		titleLabelPanel.add(customPanelTitle);

		backBtn = new CLabel();

		backBtn.setIcon(new ImageIcon(getClass().getResource("/image_assets/Rgstr_Imgs/icons8_back_to_50px.png")));
		backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		backBtnPanel.add(backBtn);

		eastFillerPanel = new JPanel();
		eastFillerPanel.setBackground(Colors.DARK);
		eastFillerPanel.setPreferredSize(new Dimension(70, 35));

		n.add(backBtnPanel, BorderLayout.WEST);
		n.add(titleLabelPanel, BorderLayout.CENTER);
		n.add(eastFillerPanel, BorderLayout.EAST);

		this.add(n, BorderLayout.NORTH);
		this.add(e, BorderLayout.EAST);
		this.add(w, BorderLayout.WEST);
		this.add(s, BorderLayout.SOUTH);
		this.add(contentBasePanel, BorderLayout.CENTER);
	}

	public CPanel(LayoutManager layout, boolean isDoubleBuffered) {
		setLayout(layout);
		setDoubleBuffered(isDoubleBuffered);
		updateUI();
	}

	public CPanel(LayoutManager layout) {
		this(layout, true);
	}

	public CPanel(boolean isDoubleBuffered) {
		this(new FlowLayout(), isDoubleBuffered);
	}

	public void setCustomPanelTitle(String text) {
		customPanelTitle.setText(text);
		customPanelTitle.setPreferredSize(new Dimension(customPanelTitle.getPreferredSize().width,
				customPanelTitle.getPreferredSize().height + 2));
	}

	public void customFunc(JPanel accBasePanel, JPanel hcpBasePanel, JPanel leftNavPanel) {
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				accBasePanel.setVisible(false);
				hcpBasePanel.setVisible(true);
				leftNavPanel.setVisible(true);
			}
		});
	}

	public void customFunc(JPanel accBasePanel, JPanel hcpBasePanel, JPanel leftNavPanel, JPanel panel) {
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				accBasePanel.setVisible(false);
				hcpBasePanel.setVisible(true);
				leftNavPanel.setVisible(true);
				panel.setVisible(false);
			}
		});
	}

	public void customFunc(JPanel accBasePanel, JPanel hcpBasePanel, JPanel leftNavPanel, JPanel panel, CLabel label) {
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				accBasePanel.setVisible(false);
				hcpBasePanel.setVisible(true);
				leftNavPanel.setVisible(true);
				panel.setVisible(false);
				label.setText("");
			}
		});
	}

	public void customFunc(JFrame currentFrame) {
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				EventQueue.invokeLater(() -> {
					currentFrame.dispose();
					new Login();
				});
			}
		});

	}
}
