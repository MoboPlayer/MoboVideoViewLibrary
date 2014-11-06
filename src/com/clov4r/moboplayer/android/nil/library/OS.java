package com.clov4r.moboplayer.android.nil.library;

import java.io.DataInputStream;

import android.util.Log;

public class OS {
	public static final String FILE_CPUINFO_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
	public static final String FILE_CPUINFO_SCALING_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static final String FILE_PROC_CPUINFO = "/proc/cpuinfo";
	
	static public void printD(String msg)
	{
		//Log.d("NIL_OS", msg);
	}
	
	
	public static int getSDKVersionNumber() {
	  	 int sdkVersion;
	  	 try {
	    	 sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
	  	 } catch (NumberFormatException e) {
	    	 sdkVersion = 0;
	  	 }
	  	 return sdkVersion;
	}

	public static final String runAsRoot(String cmd)
	{
		Process su;  
		StringBuffer outputs = new StringBuffer(128);
		try {  
			// su to get root privileges  
			String fullCmd = "su root -c "+cmd;
			String[] args = cmd.split(" ");
			su = Runtime.getRuntime().exec(args);   
			// Attempt to write a file to a root-only  
			//DataOutputStream suSender = new DataOutputStream(su.getOutputStream());
			DataInputStream suReciever = new DataInputStream(su.getInputStream());
			//suSender.writeBytes(cmd+"\n");  
			// Close the terminal  
			//suSender.writeBytes("exit\n");  
			//suSender.flush();  

			String line = null;
			do{
			  line = suReciever.readLine();
			  if ( line != null) outputs.append(line);
			  else break;
			}while(true);

			int exitValue = su.waitFor();
			
			if (exitValue != 255) {  
				Log.d("CPU", "root access for CPU hardware identifying is granted.");  
			} else {
				Log.d("CPU", "root access for CPU hardware identifying is denied.");  
			}  
		} catch (Exception e) {
			e.printStackTrace();
			// ignore error and give up
			return null;
		}
		return outputs.toString();
	}

}
