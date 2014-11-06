package com.clov4r.moboplayer.android.nil.library;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class PlayerStateData implements Serializable {
	public ArrayList<LocalVideoData> dataList = null;
	public String currentPath = null;
	public int currentIndex = 0;
	public int playerState = Constant.state_play;
	public String params = null;
	public int decodeAudioSoftType = 0;
	public boolean playBackground = false;
	public boolean playedFromOut;

	public PlayerStateData(ArrayList<LocalVideoData> dataList, String path,
			int index, int playerState, String params) {//
		this.params = params;
		this.dataList = dataList;
		currentPath = path;
		currentIndex = index;
		this.playerState = playerState;
	}

	public PlayerStateData(ArrayList<LocalVideoData> dataList, String path,
			int index, int playerState, String params, int decodeAudioSoftType,
			boolean playBackground, boolean playedFromOut) {//
		this.params = params;
		this.dataList = dataList;
		this.decodeAudioSoftType = decodeAudioSoftType;
		currentPath = path;
		currentIndex = index;
		this.playerState = playerState;
		this.playBackground = playBackground;
		this.playedFromOut = playedFromOut;
	}

}
