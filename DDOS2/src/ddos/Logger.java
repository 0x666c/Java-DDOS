package ddos;

import java.awt.Color;

import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Logger {
	
	public static Logger get;
	
	private final Document log;
	
	private final SimpleAttributeSet def_set;
	private final SimpleAttributeSet warn_set;
	private final SimpleAttributeSet err_set;
	private final SimpleAttributeSet success_set;
	
	private Logger(Document log_doc) {
		this.log = log_doc;
		
		this.def_set = new SimpleAttributeSet();
		this.warn_set = new SimpleAttributeSet();
		this.err_set = new SimpleAttributeSet();
		this.success_set = new SimpleAttributeSet();
		
		StyleConstants.setForeground(def_set, Color.BLACK);
		StyleConstants.setForeground(warn_set, new Color(160, 160, 0));
		StyleConstants.setForeground(err_set, Color.RED);
		StyleConstants.setForeground(success_set, Color.GREEN);
		
		StyleConstants.setBold(err_set, true);
		StyleConstants.setBold(success_set, true);
	}
	
	public static void init(Document doc)
	{
		get = new Logger(doc);
	}
	
	
	public void log_str(String str)
	{
		try
		{
			log.insertString(log.getLength(), "> " + str + "\n", def_set);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void log_str(String str, MSG type)
	{
		try
		{
			switch(type)
			{
			case INFO: 		 log.insertString(log.getLength(), "> " + str + "\n", def_set); break;
			case WARNING:	 log.insertString(log.getLength(), "> " + str + "\n", warn_set); break;
			case ERROR:		 log.insertString(log.getLength(), "> " + str + "\n", err_set); break;
			case SUCCESS:	 log.insertString(log.getLength(), "> " + str + "\n", success_set); break;
			case BLANK:	 	 log.insertString(log.getLength(), "\n"				, def_set); break;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static enum MSG
	{
		INFO,
		WARNING,
		ERROR,
		SUCCESS,
		
		BLANK;
	}
}
