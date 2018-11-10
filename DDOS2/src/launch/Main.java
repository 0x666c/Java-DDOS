package launch;

import javax.swing.SwingUtilities;

import gui.MainFrame;

public class Main {
	
	private static MainFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{frame = new MainFrame();
										frame.setVisible(true);});
		
		
	}
	
	public static void enable_btns()
	{
		frame.enableButtons();
	}
	public static void disable_stop_btn()
	{
		frame.disable_stop_btn();
	}
}
