package com.clov4r.moboplayer.android.nil.codec;

import java.io.File;

import android.os.AsyncTask;
import android.util.Log;

import com.clov4r.moboplayer.android.nil.codec.StreamingDownloadManager.MoboDownloadListener;
import com.clov4r.moboplayer.android.nil.codec.StreamingDownloadManager.StreamingDownloadData;

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
public class StreamingDownloadLib {
	MoboDownloadListener mMoboDownloadListener = null;
	StreamingDownloadData downloadData = null;
	boolean isBufferLib = false;

	public StreamingDownloadLib(StreamingDownloadData downloadData) {
		this.downloadData = downloadData;
	}

	public void setDownloadListener(MoboDownloadListener listener) {
		mMoboDownloadListener = listener;
	}

	public void startBuffer(int startPos) {
		nativeStartBuffer(downloadData.streamingUrl, downloadData.packetFile,
				startPos, downloadData.timeout);
		isBufferLib = true;
	}

	public void startDownload() {
		new DownloadLib(downloadData).execute((Void) null);
	}

	public void pauseDownload() {
		downloadData.status = StreamingDownloadData.download_status_paused;
		nativePauseDownload();
	}

	public void resumeDownload() {
		downloadData.status = StreamingDownloadData.download_status_started;
		nativeResumeDownload();
	}

	public void stopDownload() {
		if (isBufferLib) {
			stopBuffer();
			isBufferLib = false;
		}
		downloadData.status = StreamingDownloadData.download_status_stoped;
		// stopBuffer();

//		new Thread() {
//			@Override
//			public void run() {
				nativeStopDownload();
//			}
//		}.start();
	}

	public void pauseBuffer() {
		nativePauseBuffer();
	}

	public void resumeBuffer() {
		nativeResumeBuffer();
	}

	public void stopBuffer() {
//		new Thread() {
//			@Override
//			public void run() {
				nativeStopBuffer();
//			}
//		}.start();
	}

	public void onDownloadProgressChanged(long position, int currentTime,
			long dts, int downloadType) {// int
											// streamIndex,
		downloadData.currentTime = currentTime;
		if (downloadData.last_video_dts < dts)
			downloadData.last_video_dts = dts;
		if (downloadData.finishSize < position)
			downloadData.finishSize = position;
		if (downloadData.duration == 0)
			downloadData.duration = nativeGetDuration();
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onDownloadProgressChanged(downloadData,
					currentTime);
	}

	public void onRewriteFinished() {
		File file = new File(downloadData.fileSavePath + ".tmp");
		boolean deleted = file.delete();
	}

	public void onBuffering() {
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onBuffering();
	}

	public void onDownloadFinished() {
		downloadData.status = StreamingDownloadData.download_status_finished;
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onDownloadFinished(downloadData);
	}

	public void onDownloadFailed(String msg) {
		downloadData.status = StreamingDownloadData.download_status_failed;
		downloadData.failedMsg = msg;
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onDownloadFailed(downloadData);
	}

	public int getDownloadStatus() {
		return downloadData.status;
	}

	/**
	 * Get the duration of video
	 * 
	 * @param id
	 * @return
	 */
	public int getDuration() {
		if (downloadData.duration <= 0)
			downloadData.duration = nativeGetDuration();
		return downloadData.duration;
	}

	/**
	 * Get the time of video has been downloaded to.
	 * 
	 * @return
	 */
	public int getCurrentTimeDownloadedTo() {
		// return nativeGetDownloadedLen();
		return downloadData.currentTime;
	}

	/**
	 * Get the time of video start to download
	 * 
	 * @return
	 */
	public int getStartDownloadedTime() {
		if (downloadData.startTime == -1)
			downloadData.startTime = nativeGetStartDownloadedTime();
		return downloadData.startTime;
	}

	public void setStartDownloadTime(int startBufferTime) {
		downloadData.startTime = startBufferTime;
	}

	public native int nativeStartDownload(String streamingUrl,
			String fileSavePath, String packetFile, long dts,
			long finishedSize, int isLive, int timeout);// ,
	// int
	// timeToDownload

	public native int nativeStartBuffer(String streamingUrl, String packetFile,
			int startPos, int timeout);

	public native void nativeStartDownload3(int intArray[]);

	public native void nativePauseDownload();

	public native void nativeResumeDownload();

	public native void nativeStopDownload();

	public native int nativeGetDuration();

	// public native int nativeGetDownloadedLen();

	public native int nativeGetStartDownloadedTime();

	public native void nativePauseBuffer();

	public native void nativeResumeBuffer();

	public native void nativeStopBuffer();

	private class DownloadLib extends AsyncTask<Void, Integer, Void> {
		StreamingDownloadData mStreamingDownloadData = null;

		public DownloadLib(StreamingDownloadData data) {
			mStreamingDownloadData = data;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (mStreamingDownloadData.currentTime > 0
					&& mStreamingDownloadData.currentTime == mStreamingDownloadData.duration) {
				onDownloadFinished();
				return null;
			}
			if (mStreamingDownloadData.finishSize > 0) {
				File file = new File(mStreamingDownloadData.fileSavePath);
				if (file.exists()) {
					file.renameTo(new File(mStreamingDownloadData.fileSavePath
							+ ".tmp"));
				}
			}
			if (mStreamingDownloadData.failedMsg != null)
				mStreamingDownloadData.failedMsg = null;
			mStreamingDownloadData.status = StreamingDownloadData.download_status_started;
			if (mStreamingDownloadData.currentTime > 0)
				mStreamingDownloadData.startTime = mStreamingDownloadData.currentTime;
			mStreamingDownloadData.startTime = 10;
			nativeStartDownload(mStreamingDownloadData.streamingUrl,
					mStreamingDownloadData.fileSavePath,
					mStreamingDownloadData.packetFile,
					mStreamingDownloadData.last_video_dts,
					mStreamingDownloadData.finishSize,
					mStreamingDownloadData.isLive ? 1 : 0,
					mStreamingDownloadData.timeout);// mStreamingDownloadData.timeStartToDownload
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progresses) {

		}

		@Override
		protected void onPostExecute(Void params) {

		}
	}

}
