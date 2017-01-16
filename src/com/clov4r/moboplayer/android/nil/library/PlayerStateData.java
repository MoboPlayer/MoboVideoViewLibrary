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
	public float playSpeed = 1f;
	public boolean isPlayAfterScreenOff = false;
	public int playerLoopMode = 0;
	public int recyle_partly_start_time = 0, recyle_partly_end_time = 0;//AB点循环起始时间
	public boolean isSaveSize = false;

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

	public void setSpeed(float speed){
		playSpeed = speed;
	}
	
	public void setPlayAfterScreenOff(boolean isPlayAfterScreenOff){
		this.isPlayAfterScreenOff = isPlayAfterScreenOff;
	}

	public void setPlayerLoopMode(int playerLoopMode, int recyle_partly_start_time, int recyle_partly_end_time){
		this.playerLoopMode = playerLoopMode;
		this.recyle_partly_start_time = recyle_partly_start_time;
		this.recyle_partly_end_time = recyle_partly_end_time;
	}
	
	public void setIsSaveSize(boolean isSaveSize){
		this.isSaveSize = isSaveSize;
	}

}
