package gui;

import gui.MainFrame.DELAY_MODE;

public final class InfoPackage {
	public final boolean arePortsEnabled;
	public final boolean arePortsAvailable;
	
	public final boolean isCheckPressed;
	
	public final boolean isHttp_check;
	public final boolean isPing_check;
	
	public final boolean isStartPressed;
	
	public final String ipAdress;
	public final String portString;
	
	public final int clientCount;
	
	public final int junkSize;
	public final int timeoutDelay;
	
	public final DELAY_MODE queryDelayMode;
	public final int queryCustomDelayAmount;
	
	public final boolean doRandomizeJuskContent;
	
	public InfoPackage(boolean http, boolean ping, boolean portsen, boolean startpr, boolean checkpr,
					   String ip, String ports, int clcount, int jSize,
					   int toutDelay, String delayMode, int delayAmtCustom, boolean randomjunk)
	{
		isHttp_check = http;
		isPing_check = ping;
		
		arePortsEnabled = portsen; 
		arePortsAvailable = (!ports.trim().isEmpty() && !ports.matches(".*[a-zA-Z]*.")) && portsen;
		
		isCheckPressed = checkpr;
		isStartPressed = startpr;
		
		ipAdress = ip;
		portString = ports;
		
		clientCount = clcount;
		
		junkSize = jSize;
		timeoutDelay = toutDelay;
		
		queryDelayMode = DELAY_MODE.findByString(delayMode);
		queryCustomDelayAmount = delayAmtCustom;
		
		doRandomizeJuskContent = randomjunk;
	}
}
