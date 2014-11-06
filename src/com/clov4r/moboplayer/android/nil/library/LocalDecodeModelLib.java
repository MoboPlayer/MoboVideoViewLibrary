package com.clov4r.moboplayer.android.nil.library;

import java.util.HashMap;
import java.util.Iterator;

import com.clov4r.moboplayer.android.nil.library.R;

import android.content.Context;

/**
 * 工具类：检测是否为媒体文件；添加删除和关闭媒体文件；保存媒体文件类型的列表；判断解码模式；etc
 * 
 * @author lyw
 * 
 */
public class LocalDecodeModelLib {
	public static final String encryptedFileFormat = "mbo";
	public static int decode_mode_hard = 1;
	public static int decode_mode_mediacodec = 2;
	public static int decode_mode_soft = 3;
	/* 禁用标志 */
	public static int decode_mode_closed = 4;
	/* 假删除标志 */
	public static int DECODE_MODE_DELETE = 5;

	String defaultFileType[] = { "avi", "mp3", "rmvb", "rm", "3gp", "wmv",
			"mp4", "mov", "mkv", "flv", "3gpp", "mpg", "mlv", "mpeg", "m2v",
			"vob", "tp", "ts", "asf", "ra", "ram", "hlv", "ogg", "f4v", "m4v",
			"divx", "ape", "acc", "webm", "m2ts", "mobo", "flac", "wav", "iso",
			"wma", "cd", "wave", "aiff", "au", "amr", "midi", "realrudio",
			"vqf", "oggvorbis", "aac", encryptedFileFormat };

	String streamType[] = { "file", "FTP", "Gopher", "HLS", "HTTP", "HTTPS",
			"Icecast", "MMSH", "MMST", "pipe", "RTMP", "RTMPE", "RTMPS",
			"RTMPT", "RTMPTE", "RTMPTS", "RTP", "SAMBA", "SCTP", "SFTP", "TCP",
			"TLS", "UDP" };
	private String extVideoFormat[] = null;
	private static String extAudioFormat[] = null;

	private HashMap<String, Integer> formatMap = new HashMap<String, Integer>();
	// private HashMap<String, Object> streamMap = new HashMap<String,
	// Object>();

	private static LocalDecodeModelLib mLocalDecodeModelLib = null;

	public static LocalDecodeModelLib getInstance(Context con) {
		if (mLocalDecodeModelLib == null)
			mLocalDecodeModelLib = new LocalDecodeModelLib(con);
		return mLocalDecodeModelLib;
	}

	public LocalDecodeModelLib(Context con) {
		extVideoFormat = con.getResources().getStringArray(
				R.array.expend_video_format);
		extAudioFormat = con.getResources().getStringArray(
				R.array.ext_audio_format);

		init(con);
	}

	/**
	 * 保存本地设置项
	 * 
	 * @param con
	 */
	public void saveData(Context con) {
		DataSaveLib lib = new DataSaveLib(con, DataSaveLib.name_of_format_list);
		lib.saveData(formatMap);
	}

	private void init(Context con) {
		if (formatMap == null || formatMap.size() == 0) {
			DataSaveLib lib = new DataSaveLib(con,
					DataSaveLib.name_of_format_list);
			formatMap = (HashMap<String, Integer>) lib.readData();
			if (formatMap == null) {
				formatMap = new HashMap<String, Integer>();
				for (int i = 0; i < defaultFileType.length; i++) {
					formatMap.put(defaultFileType[i], decode_mode_hard);
				}
				lib.saveData(formatMap);
			} else {
				boolean hasChecked = Global.parseBoolean(SharedPreverenceLib
						.getByKey("set_format_to_lowercase", false).toString());
				if (!hasChecked) {
					Iterator<String> iterator = formatMap.keySet().iterator();
					HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
					while (iterator.hasNext()) {
						String key = iterator.next();
						tmpMap.put(key.toLowerCase(), formatMap.get(key));
					}
					formatMap.clear();
					formatMap.putAll(tmpMap);
					SharedPreverenceLib.saveSetting(con,
							"set_format_to_lowercase", true);
				}
			}
			if (extVideoFormat != null) {
				for (int i = 0; i < extVideoFormat.length; i++) {
					if (!formatMap.containsKey(extVideoFormat[i]))
						formatMap.put(extVideoFormat[i], decode_mode_hard);
				}
			}
			if (extAudioFormat != null) {
				for (int i = 0; i < extAudioFormat.length; i++) {
					if (!formatMap.containsKey(extAudioFormat[i]))
						formatMap.put(extAudioFormat[i], decode_mode_hard);
				}
			}
		}

		// for (int i = 0; i < streamType.length; i++)
		// streamMap.put(streamType[i].toLowerCase(), true);
	}

	public void resetAllFormat(Context con) {
		DataSaveLib lib = new DataSaveLib(con, DataSaveLib.name_of_format_list);
		formatMap = new HashMap<String, Integer>();
		for (int i = 0; i < defaultFileType.length; i++) {
			formatMap.put(defaultFileType[i], decode_mode_hard);
		}
		lib.saveData(formatMap);
	}

	/**
	 * 添加新视频格式
	 * 
	 * @param format
	 */
	public void addFormat(String format) {
		if (format != null && !formatMap.containsKey(format.toLowerCase())) {
			formatMap.put(format.toLowerCase(), decode_mode_hard);
		}
	}

	/**
	 * 清空库
	 */
	public void removeAllFromat() {
		formatMap.clear();
	}

	/**
	 * 删除视频格式
	 * 
	 * @param format
	 */
	public void removeFormat(String format) {
		if (format != null && formatMap.containsKey(format.toLowerCase())) {
			formatMap.remove(format.toLowerCase());
		}
	}

	/**
	 * 改变相应格式的解码方式
	 * 
	 * @param format
	 * @param decodeMode
	 */
	public void changeDecodeMode(String format, int decodeMode) {
		if (format != null) {
			format = format.toLowerCase();
			if (!formatMap.containsKey(format)) {
				addFormat(format);
			}
			formatMap.put(format, decodeMode);
		}
	}

	/**
	 * 获取视频文件的解码方式
	 * 
	 * @param absPath
	 * @return
	 */
	public int getDecodeModel(String absPath) {
		if (absPath != null) {
			String format = getVideoFormat(absPath);
			if (format != null) {
				format = format.toLowerCase();
				if (formatMap.containsKey(format))
					return formatMap.get(format);
			}
		}
		return decode_mode_hard;
	}

	/**
	 * 检测该文件是否为视频文件
	 * 
	 * @param absPath
	 * @return
	 */
	public boolean checkIsMedia(String absPath) {
		String format = getVideoFormat(absPath);
		if (format != null) {
			format = format.toLowerCase();
			if (format.endsWith(encryptedFileFormat))
				format = format.replace(encryptedFileFormat, "");
			if (formatMap.containsKey(format)
					&& formatMap.get(format) != decode_mode_closed)
				return true;
		}

		return false;
	}

	static String musicEndings[] = { "wav", "mp3", "wma", "ogg", "ape", "acc",
			"cd", "wave", "aiff", "au", "amr", "midi", "realaudio", "vqf",
			"oggvorbis", "aac", "ra", };

	/**
	 * 判断是不是音乐文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkIsMusic(String path) {
		if (path == null || "".equals(path) || !path.contains("/")
				|| !path.contains("."))
			return false;
		else {
			// MediaStore.Audio.Media.IS_MUSIC
			int end = path.lastIndexOf(".") + 1;
			if (end < 0)
				end = path.length() - 1;
			String ending = path.substring(end);
			if (ending != null) {
				if (ending.endsWith(encryptedFileFormat))
					ending = ending.replace(encryptedFileFormat, "");
				for (int i = 0; i < musicEndings.length; i++) {
					String tempEnding = musicEndings[i];
					if (tempEnding.equalsIgnoreCase(ending))
						return true;
				}
				if (extAudioFormat != null)
					for (int i = 0; i < extAudioFormat.length; i++) {
						String tempEnding = extAudioFormat[i];
						if (tempEnding.equalsIgnoreCase(ending))
							return true;
					}
			}
		}
		return false;
	}

	public boolean checkIsOnlineVideo(String path) {
		if (path != null) {
			path = path.toLowerCase();
			// if (path.startsWith("http") || path.startsWith("mms")
			// || path.startsWith("rtsp") || path.startsWith("rtp")
			// || path.startsWith("rsvp") || path.startsWith("rtpc")) {
			// return true;
			// }
			// return streamMap.containsKey(path);
			for (int i = 0; i < streamType.length; i++)
				if (path.startsWith(streamType[i]))
					return true;
		}
		return false;
	}

	// public static boolean isInvalidate(String path) {
	// if (MainInterface.mSettingItem != null
	// && MainInterface.mSettingItem.isScanVideoOnly()) {
	// if (checkIsMusic(path))
	// return true;
	// }
	// return false;
	// }

	/**
	 * 根据视频路径，获取视频格式
	 * 
	 * @param absPath
	 * @return
	 */
	public String getVideoFormat(String absPath) {
		if (absPath != null && absPath.contains(".")) {
			String format = absPath.substring(absPath.lastIndexOf(".") + 1);
			return format.toLowerCase();
		}
		return absPath;
	}

	/**
	 * 获得解码类型列表
	 * 
	 * @return KV类型的解码类型列表
	 */
	public HashMap<String, Integer> getVideoFormatList() {
		return formatMap;
	}

	/**
	 * 检测数据库是否需要升级
	 */
	public void checkUpgradeDecodeModeData(Context pContext) {
		for (String newFileType : defaultFileType) {
			if (formatMap.get(newFileType) == null) {
				formatMap.put(newFileType, decode_mode_hard);
			}
		}
		saveData(pContext);
	}
}
