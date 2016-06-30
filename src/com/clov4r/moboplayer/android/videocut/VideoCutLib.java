package com.clov4r.moboplayer.android.videocut;

import com.clov4r.moboplayer.android.nil.library.Global;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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
public class VideoCutLib {
	public static boolean hasInited = false;
	int commandNum;
	String[] commands;
	boolean hasStoppped = false;
	private CutListener mCutListener = null;
	final String defaultCommand = "ffmpeg -ss %s -i %s -s %s -strict experimental -t %s %s";
	// String[] commandArray = new String[] { "ffmpeg", "-ss", "%s", "-i", "%s",
	// "-s", "%s", "-strict", "experimental", "-t", "%s", "%s" };

	String[] commandArray = new String[] { "ffmpeg", "-ss", "%s", "-i", "%s",
			"-s", "%s", "-strict", "experimental", "-t", "%s", "-c:v",
			"libx264", "-c:a", "aac", "-maxrate", "1m", "-ar", "%s", "%s" };// "-minrate",
																			// "%s",

	final int max_sample_rate = 48000;
	final int max_side_size = 640;
	final int max_rate = 1024;// 单位 kbps

	/**
	 * 务必保证输出路径存在
	 */
	public VideoCutLib(String command) {
		initCommand(command);
	}

	/**
	 * 
	 * @param inputFilePath
	 *            待截取的视频的绝对地址
	 * @param outputFilePath
	 *            截取视频的保存地址，视频所在目录一定要是存在的
	 * @param resolution
	 *            截取分辨率，格式：width*height
	 * @param startTime
	 *            截取开始时间，格式：hh:mm:ss
	 * @param duration
	 *            截取时长，格式：hh:mm:ss
	 * @param samplerate
	 *            采样率
	 */
	public VideoCutLib(String inputFilePath, String outputFilePath,
			String resolution, String startTime, String duration, int samplerate) {
		// initCommand(String.format(defaultCommand, startTime, inputFilePath,
		// resolution, duration, outputFilePath));

		commandArray[2] = startTime;
		commandArray[4] = inputFilePath;// "\"" + + "\"";
		commandArray[6] = checkResolution(resolution);// getFormatResolution(resolution);
		commandArray[10] = duration;
		if (samplerate > 44100)
			samplerate = 44100;
		else if (samplerate <= 32000)
			samplerate = 32000;
		commandArray[18] = samplerate + "";
		commandArray[19] = outputFilePath;// "\"" + + "\"";
		// commandArray[16] = getFormatRate(rate);

		commands = commandArray;
		commandNum = commandArray.length;

		String command = "";
		for (int i = 0; i < commandNum; i++) {
			command += commandArray[i] + " ";
		}
		// Log.e("", command);
	}

	private String getFormatRate(int rate) {
		int dst_rate = rate;
		if (rate > max_rate) {
			dst_rate = max_rate;
		}
		if (dst_rate >= max_rate)
			return dst_rate / 1024 + "m";
		else
			return dst_rate + "k";
	}
	
	private String checkResolution(String resolution){
		if (resolution.contains("*")) {
			int width = Global.parseInt(resolution.substring(0,
					resolution.indexOf("*")));
			int height = Global.parseInt(resolution.substring(resolution
					.indexOf("*") + 1));
			if(width % 2 != 0 || height % 2 != 0){
				if(width % 2 != 0)
					width++;
				if(height % 2 != 0)
					height++;
			}
			return width + "*" + height;
		}
		return resolution;
	}

	private String getFormatResolution(String resolution) {
		if (resolution.contains("*")) {
			int width = Global.parseInt(resolution.substring(0,
					resolution.indexOf("*")));
			int height = Global.parseInt(resolution.substring(resolution
					.indexOf("*") + 1));
			if (width > max_side_size || height > max_side_size) {
				if (width > height) {
					height = height * max_side_size / width;
					width = max_side_size;
				} else {
					width = width * max_side_size / height;
					height = max_side_size;
				}
				if (width % 2 != 0)
					width++;
				if (height % 2 != 0)
					height++;
				return width + "*" + height;
			} else
				return resolution;
		} else
			return resolution;
	}

	private void initCommand(String command) {
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

	public void setCutListener(CutListener listener) {
		mCutListener = listener;
	}

	public int cut() {
		return cutVideo(commandNum, commands);
	}

	public void cutAsynchronous(CutListener listener) {
		setCutListener(listener);
		new CutLib().execute((Void) null);
	}

	private void setProgress(int second, int duration) {
		if (!hasStoppped) {
			Message msg = new Message();
			msg.what = msg_progress_changed;
			msg.arg1 = second;
			msg.arg2 = duration;
			mHandler.sendMessage(msg);
		}
	}

	public native int cutVideo(int commandNum, Object[] commands);

	public void stopCut() {
		hasStoppped = true;
		stopCutVideo();
	}

	public native void stopCutVideo();

	public native int isCutFinished();

	private class CutLib extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			return cutVideo(commandNum, commands);
		}

		@Override
		protected void onProgressUpdate(Integer... progresses) {

		}

		@Override
		protected void onPostExecute(Integer params) {
			if (mCutListener != null && !hasStoppped)// && params == 0
				mCutListener.onFinished(params);
			hasStoppped = true;
		}
	}

	private static final int msg_progress_changed = 111;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case msg_progress_changed:
				if (mCutListener != null)
					mCutListener.onProgressChanged(msg.arg1, msg.arg2);
				break;
			}
		}
	};
}
