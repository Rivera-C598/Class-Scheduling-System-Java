package theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class BlurLayer extends JComponent {
	
	private JFrame f;
    public BlurLayer(JFrame f) {
        this.f = f;
        setOpaque(false);
        setFocusable(false);
        addMouseListener(new MouseAdapter(){});
        addMouseMotionListener(new MouseMotionAdapter(){});
    }
    
    public void paintComponent(Graphics g) {
        int w = f.getWidth();
        int h = f.getHeight();
        setLocation(0,0);
        setSize(w,h);
        g.setColor(new Color(0,0,0,.7f));
        g.fillRect(0,0,w,h);
    }
	/*
	 * private BufferedImage mOffscreenImage; private BufferedImageOp mOperation;
	 * 
	 * public BlurLayer() { float ninth = 1.0f / 15.0f; float[] blurKernel = {
	 * ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth,
	 * ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth,
	 * ninth, ninth, ninth, ninth, ninth, ninth, }; mOperation = new ConvolveOp( new
	 * Kernel(0,0, blurKernel), ConvolveOp.EDGE_NO_OP, null); }
	 * 
	 * @Override public void paint (Graphics g, JComponent c) { int w =
	 * c.getWidth(); int h = c.getHeight();
	 * 
	 * if (w == 0 || h == 0) { return; }
	 * 
	 * // Only create the off-screen image if the one we have // is the wrong size.
	 * if (mOffscreenImage == null || mOffscreenImage.getWidth() != w ||
	 * mOffscreenImage.getHeight() != h) { mOffscreenImage = new BufferedImage(w, h,
	 * BufferedImage.TYPE_INT_RGB); }
	 * 
	 * Graphics2D ig2 = mOffscreenImage.createGraphics(); ig2.setClip(g.getClip());
	 * super.paint(ig2, c); ig2.dispose();
	 * 
	 * Graphics2D g2 = (Graphics2D)g; g2.drawImage(mOffscreenImage, mOperation, 0,
	 * 0); }
	 */
		
}
