package com.clov4r.moboplayer.android.videocut;

import java.util.ArrayList;

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
public class VideoConcatLib extends CommenCommandLib {
	// String ff_command = "ffmpeg -i F:\Media\aipeike\2015-01-01-00-04-47.MP4
	// -i F:\Media\aipeike\2015-10-10-11-24-12.MP4 " +
	// "-filter_complex \"[0:v:0] [0:a:0] [1:v:0] [1:a:0] concat=n=2:v=1:a=1 [v] [a]\" -map \"[v]\" -map \"[a]\" "
	// +
	// "-scodec copy ConcatVideo\output_video_with_sub.mp4";
	boolean hasStoppped = false;
	ArrayList<String> commandList = new ArrayList<String>();
	ArrayList<String> srcPathList = new ArrayList<String>();
	String dstPath;

	public VideoConcatLib() {

	}

	public VideoConcatLib setDstPath(String dstPath) {
		this.dstPath = dstPath;
		return this;
	}

	public VideoConcatLib addSrcPath(String srcPath) {
		srcPathList.add(srcPath);
		return this;
	}

	public void startConcat() {
		if (srcPathList.size() == 0)
			return ;
		commandList.add("ffmpeg");
		for (int i = 0; i < srcPathList.size(); i++) {
			commandList.add("-i");
			commandList.add(srcPathList.get(i));
		}
		commandList.add("-filter_complex");
		String commandFilter = "[0:v:0] [0:a:0] [1:v:0] [1:a:0] concat=n=2:v=1:a=1 [v] [a]";
		commandList.add(commandFilter);

		commandList.add("-map");
		commandList.add("[v]");
		commandList.add("-map");
		commandList.add("[a]");

		commandList.add("-strict");
		commandList.add("experimental");
		commandList.add(dstPath);

		commands = (String[]) commandList
				.toArray(new String[commandList.size()]);
		commandNum = commands.length;
		new CreateLib().execute(0);
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
	protected VideoProcessListener mVideoProcessListener = null;
	private static final int msg_progress_changed = 111;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case msg_progress_changed:
				if (mVideoProcessListener != null)
					mVideoProcessListener.onProgressChanged(msg.arg1, msg.arg2);
				break;
			}
		}
	};

	public void stopCreate() {
		hasStoppped = true;
		stop();
	}
	
	public boolean hasStopped(){
		return hasStoppped;
	}

	public void setCutListener(VideoProcessListener listener){
		mVideoProcessListener = listener;
	}

	private class CreateLib extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			return excute();
		}

		@Override
		protected void onProgressUpdate(Integer... progresses) {

		}

		@Override
		protected void onPostExecute(Integer params) {
			mVideoProcessListener.onFinished(params);
		}
	}
}
