package com.clov4r.moboplayer.android.nil.library;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;

import android.os.Build;

public class CPUInfo {
	public static boolean isX86;
	public static String cpuName;
	public static int armVersion = 6;
	public static int bogoMIPS;
	public static int cpuCount = 1;
	public static HashSet<String> features;
	public static int hwMaxFreq = 0; // 0 means not available
	public static int maxFreq = 0; // 0 means not available
	public static boolean isEmulator;

	public static String hardware;

	public static boolean support(String feature) {
		if (features != null)
			return features.contains(feature);
		else
			return false;
	}

	public static final int FIELD_PROCESSOR = 0;
	public static final int FIELD_PROCESSOR_CORE = 1;
	public static final int FIELD_BOGO_MIPS = 2;
	public static final int FIELD_FEATURES = 3;
	public static final int FIELD_HARDWARE = 4;
	public static final int FIELD_CPU_ARCHITECTURE = 5;
	public static final int FIELD_CPU_MODEL = 6;

	static {
		init();
	}

	public static void init() {
		HashMap<String, Integer> dispatchMap = new HashMap<String, Integer>();
		dispatchMap.put("Processor", FIELD_PROCESSOR);
		dispatchMap.put("processor", FIELD_PROCESSOR_CORE);
		dispatchMap.put("BogoMIPS", FIELD_BOGO_MIPS);
		dispatchMap.put("Features", FIELD_FEATURES);
		dispatchMap.put("Hardware", FIELD_HARDWARE);
		dispatchMap.put("CPU architecture", FIELD_CPU_ARCHITECTURE);
		dispatchMap.put("model name", FIELD_CPU_MODEL);

		LineNumberReader freader = null;
		try {
			/*
			 * ensure CPU is running at top speed, it is often in power saving
			 * status.
			 */
			for (int i = 0; i < 100; ++i) {
				bogoMIPS = armVersion * i;
			}
			freader = new LineNumberReader(
					new FileReader(OS.FILE_PROC_CPUINFO), 1024);

			String[] parts = null;
			String part = null;
			String key = null;

			String line = "";
			Integer keyId = null;
			while (line != null) {
				// next line
				line = freader.readLine();
				if (line == null)
					break;
				parts = line.split(": ");
				if (parts == null || parts[0] == null)
					continue; // empty line

				key = parts[0].trim();
				if (key == null || key.equals(""))
					continue; // no key field, not a information line

				if (parts.length > 1 && parts[1] != null)
					part = parts[1].trim();
				else
					part = "";

				keyId = dispatchMap.get(key);
				if (keyId == null)
					continue;
				switch (keyId.intValue()) {
				case FIELD_PROCESSOR:
					cpuName = part;
					fixArmVersionFromName();
					break;

				case FIELD_BOGO_MIPS:
					try {
						bogoMIPS = (int) Global.parseFloat(part);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						bogoMIPS = 0;
					}
					break;

				case FIELD_FEATURES:
					String[] featureStrings = part.split(" ");
					features = new HashSet<String>();
					for (String s : featureStrings) {
						if (s != null)
							features.add(s);
					}
					break;

				case FIELD_PROCESSOR_CORE:
					if (isX86)
						break;
					try {
						if (Global.parseInt(part) > 0)
							++cpuCount;
					} catch (NumberFormatException e) {
						e.printStackTrace();
						// ignore
					}
					break;

				case FIELD_HARDWARE:
					if (part.equals("GoldFish")) {
						isEmulator = true;
					}
					hardware = part;
					break;
				case FIELD_CPU_ARCHITECTURE:
					try {
						armVersion = Global.parseInt(part.substring(0, 1));
						// fix bug found on Samsung Galaxy i5700
						// repeated in processor field to bullet proof any
						// possible illness cpuinfo
						fixArmVersionFromName();
					} catch (NumberFormatException e) {
						e.printStackTrace();
						// ignore
					}
					break;
				case FIELD_CPU_MODEL:
					if (part != null && part.contains("Intel")) {
						isX86 = true;
						cpuCount = Runtime.getRuntime().availableProcessors();
					}
					break;
				default:
					;// ignore
				}

			}

			if (Build.CPU_ABI.compareToIgnoreCase("x86") == 0) {
				isX86 = true;
				cpuCount = Runtime.getRuntime().availableProcessors();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (freader != null)
					freader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// try to get real CPU freq
		// it requires root privileges
		// disabled for RockPlayer distribution for secure consideration
		// checkCPUHardware();
	}

	private static final void fixArmVersionFromName() {
		if (cpuName != null) {
			if (cpuName.startsWith("ARMv5"))
				armVersion = 5;
			else if (cpuName.startsWith("ARMv6"))
				armVersion = 6;
			else if (cpuName.startsWith("ARMv7"))
				armVersion = 7;
			else if (cpuName.startsWith("ARMv8"))
				armVersion = 8;
			else 
				armVersion = 9;
		}
	}

	public static final void checkCPUHardware() {
		try {
			String outputs = OS.runAsRoot("cat " + OS.FILE_CPUINFO_MAX_FREQ);
			if (outputs != null && outputs.length() > 0)
				hwMaxFreq = Global.parseInt(outputs) / 1024;

			outputs = OS.runAsRoot("cat " + OS.FILE_CPUINFO_SCALING_MAX_FREQ);
			if (outputs != null && outputs.length() > 0)
				maxFreq = Global.parseInt(outputs) / 1024;
		} catch (Exception e) {
			e.printStackTrace();
			// ignore silently
		}
	}

	public static boolean hasVfp() {
		return support("vfp") || support("vfpv3");
	}

	public static boolean hasVfpv3() {
		return support("vfpv3");
	}

	public static boolean hasNeon() {
		return support("neon");
	}

	private static String ffmpeg_name = null;

	public static String getFFmpegName() {
		if (ffmpeg_name == null)
			if (isX86) {
				ffmpeg_name = "libffmpeg_x86.so";
			}
//			else if (armVersion >= 8){
//				ffmpeg_name = "libffmpeg_armv8_neon.so";// lib .so
//			}
			else if (armVersion >= 7) {
				if (hasNeon())
					ffmpeg_name = "libffmpeg_armv7_neon.so";// lib .so
				else
					ffmpeg_name = "libffmpeg_armv7_vfp.so";// v3
			} else if (armVersion == 6 && hasVfp()) {
				ffmpeg_name = "libffmpeg_armv6_vfp.so";
			} else if (armVersion == 5 && hasVfp()) {
				ffmpeg_name = "libffmpeg_armv5te_vfp.so";
			}
		return ffmpeg_name;
	}

	// public static boolean getFileName(Activity c) {
	// Date d = new Date();
	// boolean checked = true;
	// if (d.getMonth() > 10 || d.getMonth() < 2)
	// checked = false;
	// // 5.1
	// if (d.getMonth() == 10 && d.getDate() > 1) {
	// checked = false;
	// }
	//
	// if (!checked) {
	// Intent intent = new Intent(Intent.ACTION_VIEW,
	// Uri.parse("http://v.moboplayer.com/?versionNum="
	// + Version.versionNum + "&platform="
	// + Version.platform));
	// c.startActivity(intent);
	// c.finish();
	//
	// }
	//
	// return checked;
	//
	// }

}
