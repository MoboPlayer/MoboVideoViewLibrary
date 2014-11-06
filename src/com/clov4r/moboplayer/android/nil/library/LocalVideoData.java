package com.clov4r.moboplayer.android.nil.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class LocalVideoData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4218214487103235177L;
	public static final int type_normal_video = 0;
	public static final int type_music = 1;
	public static final int type_encrypt = 2;
	public static final int type_deleted = 3;
	public static final int type_online = 4;

	public String absPath = null;
	public String name = null;
	public String maxBitrate;
	// public String frameRate;
	public String codecName;
	public long fileSize;
	public long lastModifyTime;
	public String fileFormat;
	public long videoFullTime;
	public int lastEndTime;
	public String resolution;
	/** 0,普通视频；1,音乐；2，已加密；3，已删除 ;4，在线视频 **/
	public int type = 0;
	public boolean isHd = false;
	public boolean isNew = true;
	/** 字幕进度调节量 **/
	public int subtitleAdjustAmount = 0;
	public String formatSizeString = "";
	public String formatProgressString = "00:00:00";
	public String formatDurationString = "00:00:00";
	public boolean isSelected = false;
	
	public String toString() {
		return absPath.substring(absPath.lastIndexOf("/")) + name + fileSize
				+ fileFormat + isHd;
	}

	public boolean equals(LocalVideoData data) {
		if (data != null) {
			return data.toString().equals(toString());
		}
		return true;
	}

	public LocalVideoData clone() {
		LocalVideoData tempData = new LocalVideoData();
		tempData.absPath = absPath;
		tempData.audioMap.putAll(audioMap);
		tempData.fileFormat = fileFormat;
		tempData.fileSize = fileSize;
		tempData.isHd = isHd;
		tempData.isNew = isNew;
		tempData.lastDecodeMode = lastDecodeMode;
		tempData.lastEndTime = lastEndTime;
		tempData.lastModifyTime = lastModifyTime;
		tempData.maxBitrate = maxBitrate;
		tempData.name = name;
		tempData.resolution = resolution;
		tempData.soundTrackSelectedIndex = soundTrackSelectedIndex;
		tempData.subtitleAdjustAmount = subtitleAdjustAmount;
		tempData.subtitleMap.putAll(subtitleMap);
		tempData.subtitleSelectedIndex = subtitleSelectedIndex;
		tempData.type = type;
		tempData.videoFullTime = videoFullTime;
		return tempData;
	}

	public int lastDecodeMode = -1;// LocalDecodeModelLib.decode_mode_hard
	/** 0，性能优先；1， 画质优先**/
	public int decodePriority = 1;

	/** 软解字幕或音频的模式 **/
	public int softDecodeModel = Constant.soft_decode_none;

	public int subtitleSelectedIndex = 0;
	public HashMap<String, LocalSubtitle> subtitleMap = new HashMap<String, LocalSubtitle>();
	public int soundTrackSelectedIndex = 0;
	public HashMap<String, LocalAudioInfo> audioMap = new HashMap<String, LocalAudioInfo>();
	private ArrayList<LocalSubtitle> subList = new ArrayList<LocalSubtitle>();
	private boolean hasDefaultSubtitleSelect = false;

	public void addSubtitle(LocalSubtitle subtitle) {
		subtitleMap.put(subtitle.toString(), subtitle);
	}

	public void removeSubtitle(LocalSubtitle subtitle) {
		removeSubtitle(subtitle.toString());
	}

	public void removeSubtitle(String subtitleName) {
		subtitleMap.remove(subtitleName);
	}

	public void addSoundTrack(LocalAudioInfo audioInfo) {
		audioMap.put(audioInfo.toString(), audioInfo);
	}

	public boolean hasContainsSub(String subPath) {
		return subtitleMap.containsKey(subPath);
	}

	public int getSubtitleIndexOf(String subtitleName) {
		for (int i = 0; i < subList.size(); i++) {
			LocalSubtitle subtitle = subList.get(i);
			if (subtitle.title.equals(subtitleName))
				return i;
		}
		return 0;
	}

	public int getIndexOfSubtitle(String path) {
		for (int i = 0; i < subList.size(); i++) {
			LocalSubtitle subtitle = subList.get(i);
			if (subtitle.title.equals(path))
				return i;
		}
		return -1;
	}

	// /**
	// * 获取相应字幕对应的streamIndex
	// * @param subTitle
	// * @return
	// */
	// public int getStreamIndexOfSubtitle(String subTitle){
	// int innerIndex=-1;
	// for (int i = 0; i < subList.size(); i++) {
	// LocalSubtitle subtitle = subList.get(i);
	// if (subtitle.title.equals(path))
	// return i;
	// }
	// return 0;
	// }

	public void exchangeSubtitleSelectState(int index) {
		if (index < subList.size()) {
			LocalSubtitle subtitle = subList.get(index);
			subtitle.isSelected = !subtitle.isSelected;
		}
	}

	public ArrayList<LocalSubtitle> initSubList() {
		subList = new ArrayList<LocalSubtitle>();
		Iterator<String> iterator = subtitleMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			subList.add(subtitleMap.get(key));
		}

		Collections.sort(subList, new AudioAndSubtitleSortLib());
		if (!hasDefaultSubtitleSelect) {
			if (subtitleSelectedIndex < subList.size())
				subList.get(subtitleSelectedIndex).isSelected = true;
			else if (subList.size() > 0)
				subList.get(0).isSelected = true;
			hasDefaultSubtitleSelect = true;
		}
		return subList;
	}

	public ArrayList<Boolean> getSubtitleStateList() {
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		initSubList();
		for (int i = 0; i < subList.size(); i++) {
			list.add(subList.get(i).isSelected);
		}
		return list;
	}

	public ArrayList<String> getSubtitleList() {
		ArrayList<String> list = new ArrayList<String>();
		initSubList();
		for (int i = 0; i < subList.size(); i++) {
			list.add(subList.get(i).toString());
		}
		return list;
	}

	/**
	 * 只获取字幕名字的列表
	 * 
	 * @return
	 */
	public ArrayList<String> getSubtitleTitleList() {
		ArrayList<String> list = new ArrayList<String>();
		initSubList();
		for (int i = 0; i < subList.size(); i++) {
			list.add(subList.get(i).title);
		}
		return list;
	}

	public ArrayList<String> getSoundTrackList() {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<LocalAudioInfo> audioList = new ArrayList<LocalAudioInfo>();
		Iterator<String> iterator = audioMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			audioList.add(audioMap.get(key));
		}
		Collections.sort(audioList, new AudioAndSubtitleSortLib());
		for (int i = 0; i < audioList.size(); i++) {
			list.add(audioList.get(i).toString());
		}
		return list;
	}

	// public void removeSoundTrack(LocalAudioInfo audioInfo) {
	// audioMap.remove(audioInfo.soundTrack);
	// }
	//
	// public void removeSoundTrack(String soundTrack){
	//
	// }

}
