package com.clov4r.moboplayer.android.videocut;

import android.os.AsyncTask;
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
	private CutListener mCutListener = null;
	final String defaultCommand = "ffmpeg -ss %s -i %s -s %s -strict experimental -t %s %s";
	String[] commandArray = new String[] { "ffmpeg", "-ss", "%s", "-i", "%s",
			"-s", "%s", "-strict", "experimental", "-t", "%s", "%s" };

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
	 */
	public VideoCutLib(String inputFilePath, String outputFilePath,
			String resolution, String startTime, String duration) {
		// initCommand(String.format(defaultCommand, startTime, inputFilePath,
		// resolution, duration, outputFilePath));

		commandArray[2] = startTime;
		commandArray[4] = inputFilePath;// "\"" + + "\"";
		commandArray[6] = resolution;
		commandArray[10] = duration;
		commandArray[11] = outputFilePath;// "\"" + + "\"";

		commands = commandArray;
		commandNum = commandArray.length;
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

	private void setProgress(int second) {
		Message msg = new Message();
		msg.what = msg_progress_changed;
		msg.arg1 = second;
		mHandler.sendMessage(msg);
	}

	public native int cutVideo(int commandNum, Object[] commands);

	public native void stopCut();

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
			if (mCutListener != null)
				mCutListener.onFinished(params);
		}
	}

	private static final int msg_progress_changed = 111;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case msg_progress_changed:
				if (mCutListener != null)
					mCutListener.onProgressChanged(msg.arg1);
				break;
			}
		}
	};
}
