package ddos;

import java.util.ArrayList;
import ddos.Logger.MSG;
import gui.MainFrame.DELAY_MODE;
import useful.Useful;

public class DDOS_Client_Thread {
	
	private final int thread_number;
	
	public volatile int flood_count = 0;
	
	private ArrayList<DDOS_Client> clients;
	{
		clients = new ArrayList<>();
	}
	
	public DDOS_Client_Thread(String ip, String port, int client_amt, int ind, boolean check, int junkSize, int timeoutDelay, boolean isJunkRandom, DELAY_MODE queryMode, int customDelayAmt) {
		thread_number = ind;
		for(int i=0;i<client_amt;i++)
		{
			clients.add(new DDOS_Client(ip, port, i, check, junkSize, this, timeoutDelay, isJunkRandom, queryMode, customDelayAmt));
		}
	}
	
	public void run() {
		Logger.get.log_str("Started DDOS_VECTOR " + thread_number);
		
		for(DDOS_Client cl : clients)
		{
			cl.start();
			Useful.easySleep(1);
		}
	}
	
	public void stop()
	{
		for(int i=0;i<5;i++)
			for (DDOS_Client cl : clients) {
				cl.keepAlive = false;
				/*try {
					cl.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
	}
	
	@SuppressWarnings("deprecation")
	public void force_stop()
	{
		for (DDOS_Client cl : clients) {
			Logger.get.log_str("DDOS_THREAD " + cl.thread_number + " was force-stopped", MSG.WARNING);
			cl.keepAlive = false;
			cl.stop();
		}
	}
}
