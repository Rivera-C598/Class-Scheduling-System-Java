package theme;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SigMethods {

	/**
	 * Adds a click and drag functionality to a JFrame.
	 * 
	 * @param
	 */
	public static void draggable(JFrame frame) {

		Point point = new Point();
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point p = frame.getLocation();
				frame.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
	};

	/**
	 * Initializes the time for a clock object.
	 * 
	 * @param
	 */
	public static void initializeClock(SimpleDateFormat timeFormat, SimpleDateFormat dayFormat,
			SimpleDateFormat dateFormat, JLabel timeLabel, JLabel dayLabel, JLabel dateLabel, String toolTip) {
		
		Calendar cal = Calendar.getInstance();
        
        Date Date = cal.getTime();
        
        String time = timeFormat.format(Date);
        timeLabel.setText(time);
        String day = dayFormat.format(Date);
        dayLabel.setText(day);
        String date = dateFormat.format(Date);
        dateLabel.setText(date);
        
        
        toolTip = day + ", " + date;

		/*
		 * String day, date;
		 * 
		 * Timer timer = new Timer();
		 * 
		 * TimerTask task = new TimerTask() {
		 * 
		 * @Override public void run() { String time =
		 * timeFormat.format(Calendar.getInstance().getTime()); timeLabel.setText(time);
		 * } };
		 * 
		 * timer.scheduleAtFixedRate(task, 0, 1000);
		 * 
		 * day = dayFormat.format(Calendar.getInstance().getTime());
		 * dayLabel.setText(day);
		 * 
		 * date = dateFormat.format(Calendar.getInstance().getTime());
		 * dateLabel.setText(date);
		 * 
		 * toolTip = day + ", " + date;
		 */

	}
	
	
	
	public static void enableGlassPane(JFrame targetFrame) {
		targetFrame.setGlassPane(new BlurLayer(targetFrame));
		targetFrame.setFocusable(false);
		targetFrame.getGlassPane().setVisible(true);
	}
	public static void disableGlassPane(JFrame targetFrame) {
		targetFrame.getGlassPane().setVisible(false);
		targetFrame.setFocusable(true);
	}

	
}
