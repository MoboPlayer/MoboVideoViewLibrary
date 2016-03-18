package com.clov4r.moboplayer.android.videocut;

import android.os.Handler;
import android.os.Message;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 MoboPlayer.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
public class CommenCommandLib {
	public static boolean hasInited = false;
	int commandNum;
	String[] commands;
	
	public CommenCommandLib(){
		
	}
	
	public CommenCommandLib(String command){
		initCommand(command);
	}

	void initCommand(String command) {
		commands = command.split(" ");
		if (commands != null && commands.length > 0)
			commandNum = commands.length;
	}

	public void loadNativeLibs(String ffmpegName, String cutSoName) {
		if (!hasInited) {
			hasInited = true;
			System.loadLibrary(ffmpegName);
			System.loadLibrary(cutSoName);
		}
	}

	public native int excute(int commandNum, Object[] commands);
	public native void stop();
//	public native int excuteFilterAudio(int commandNum, Object[] commands);
//	public native int excuteFilterVideo(int commandNum, Object[] commands);
	
	public int excute() {
		return excute(commandNum, commands);
	}

//	public int excuteFilterAudio() {
//		return excuteFilterAudio(commandNum, commands);
//	}
//
//	public int excuteFilterVideo() {
//		return excuteFilterVideo(commandNum, commands);
//	}
	
}
