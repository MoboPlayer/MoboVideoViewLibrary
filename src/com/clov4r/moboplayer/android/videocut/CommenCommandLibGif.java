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
public class CommenCommandLibGif extends CommenCommandLib {
	final String gif_command_1 = "ffmpeg -y -ss %s -t %s -i %s -vf fps=%s,scale=%s:-1:flags=lanczos,palettegen %s";
	final String gif_command_2 = "ffmpeg -ss %s -t %s -i %s -i %s -filter_complex \"fps=%s,scale=%s:-1:flags=lanczos[x];[x][1:v]paletteuse\" %s";
	final String gif_command_3 = "ffmpeg -ss %s -i %s -r %s -s %s -t %s %s";

	String command_1, command_2, command_3;

	String startTime;
	String duration;
	String fps;
	String width;
	String height;
	String videoPath;
	String gifPath;
	String palettePath;
	boolean hasStoppped = false;

	public CommenCommandLibGif() {

	}

	private void initCommand(boolean highQuality) {
		if (highQuality) {
			command_1 = String.format(gif_command_1, startTime, duration,
					videoPath, fps, width, palettePath);
			command_2 = String.format(gif_command_2, startTime, duration,
					videoPath, palettePath, fps, width, gifPath);
		} else {
			command_3 = String.format(gif_command_3, startTime, videoPath, fps,
					width + "x" + height, duration, gifPath);
		}
	}

	public int startCreate(boolean highQuality) {
		initCommand(highQuality);
		int ret = -1;
		if (highQuality) {
			command_1 = String.format(gif_command_1, startTime, duration,
					videoPath, fps, width, palettePath);
			command_2 = String.format(gif_command_2, startTime, duration,
					videoPath, palettePath, fps, width, gifPath);
			initCommand(command_1);
			ret = excute();
			if (ret >= 0) {
				initCommand(command_2);
				ret = excute();
			}
		} else {
			command_3 = String.format(gif_command_3, videoPath, startTime, fps,
					width + "x" + height, duration, gifPath);
			initCommand(command_3);
			ret = excute();
		}
		return ret;
	}

	public void startCreateAsynchronous(boolean highQuality,
			CutListener listener) {
		initCommand(highQuality);
		setCutListener(listener);
		new CreateLib().execute(highQuality ? 1 : 0);
	}

	public void stopCreate() {
		hasStoppped = true;
		stop();
	}
	
	public boolean hasStopped(){
		return hasStoppped;
	}

	public CommenCommandLibGif setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public CommenCommandLibGif setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public CommenCommandLibGif setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public CommenCommandLibGif setWidth(String width) {
		this.width = width;
		return this;
	}

	public CommenCommandLibGif setHeight(String height) {
		this.height = height;
		return this;
	}

	public CommenCommandLibGif setVideoPath(String videoPath) {
		this.videoPath = videoPath;
		palettePath = videoPath.substring(0, videoPath.lastIndexOf("/") + 1)
				+ System.currentTimeMillis() + ".png";
		return this;
	}

	public CommenCommandLibGif setGifPath(String gifPath) {
		this.gifPath = gifPath;
		return this;
	}

	public void setCutListener(CutListener listener) {
		mCutListener = listener;
	}

	private void setProgress(int second) {
		Message msg = new Message();
		msg.what = msg_progress_changed;
		msg.arg1 = second;
		mHandler.sendMessage(msg);
	}

	protected CutListener mCutListener = null;
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

	private class CreateLib extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			int ret = -1;
			if (params[0] == 1) {
				initCommand(command_1);
				ret = excute();
				if (ret >= 0) {
					initCommand(command_2);
					ret = excute();
				}
			} else if (params[0] == 0) {
				initCommand(command_3);
				return excute();
			}
			return ret;

		}

		@Override
		protected void onProgressUpdate(Integer... progresses) {

		}

		@Override
		protected void onPostExecute(Integer params) {
			if (mCutListener != null && params == 0 && !hasStoppped)
				mCutListener.onFinished(params);
			hasStoppped = true;
		}
	}
}
