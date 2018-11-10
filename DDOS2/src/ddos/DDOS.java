package ddos;

import ddos.Logger.MSG;
import gui.InfoPackage;
import useful.Useful;

public class DDOS {
	
	public static volatile boolean allThreadsKeepAlive = true;
	
	//private ArrayList<DDOS_Client_Thread> threads;
	private DDOS_Client_Thread thread;
	
	public DDOS() {
		//threads = new ArrayList<>();
	}
	
	
	public void begin(InfoPackage info)
	{
		if(info.isCheckPressed)
		{
			Logger.get.log_str("Checking...", MSG.WARNING);
			//threads.add(new DDOS_Client_Thread(info.ipAdress, info.portString, 1, 0, true, info.junkSize));
			//threads.forEach(tr -> {tr.run(); Useful.easySleep(5);});
			thread = new DDOS_Client_Thread(info.ipAdress, info.portString, 1, 0, true, info.junkSize, info.timeoutDelay, info.doRandomizeJuskContent, info.queryDelayMode, info.queryCustomDelayAmount);
			thread.run();
		}
		else
		{
			Logger.get.log_str("DDOS-ing...", MSG.ERROR);
			
			//threads.add(new DDOS_Client_Thread(info.ipAdress, info.portString, info.clientCount, 0, false, info.junkSize));
			//threads.forEach(tr -> {tr.run(); Useful.easySleep(5);});
			thread = new DDOS_Client_Thread(info.ipAdress, info.portString, info.clientCount, 0, false, info.junkSize, info.timeoutDelay, info.doRandomizeJuskContent, info.queryDelayMode, info.queryCustomDelayAmount);
			thread.run();
		}
		
	}
	
	// flazzard.gearhostpreview.com
	
	public void end()
	{
		allThreadsKeepAlive = false;
		Useful.easySleep(50);
		//for (DDOS_Client_Thread tr : threads) {
			//tr.stop();
			thread.stop();
		//}
		allThreadsKeepAlive = true;
	}
	
	public void force_end()
	{
		allThreadsKeepAlive = false;
		//for (DDOS_Client_Thread tr : threads) {
			//tr.force_stop();
			thread.force_stop();
		//}
		allThreadsKeepAlive = true;
	}
}