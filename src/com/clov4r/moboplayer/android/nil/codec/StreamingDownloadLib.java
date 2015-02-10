package com.clov4r.moboplayer.android.nil.codec;

import android.os.AsyncTask;

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

	public StreamingDownloadLib(StreamingDownloadData downloadData) {
		this.downloadData = downloadData;
	}

	public void setDownloadListener(MoboDownloadListener listener) {
		mMoboDownloadListener = listener;
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
		downloadData.status = StreamingDownloadData.download_status_stoped;
		nativeStopDownload();
	}

	public void onDownloadProgressChanged( long position,// int
																// streamIndex,
			// long pts,
			int currentTime) {
		downloadData.finishSize = position;
		// downloadData.stm_index_pts_map.put(streamIndex, pts);
		downloadData.currentTime = currentTime;
		if (downloadData.duration == 0)
			downloadData.duration = nativeGetDuration();
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onDownloadProgressChanged(downloadData,
					currentTime);
	}

	public void onDownloadFinished(int id) {
		downloadData.status = StreamingDownloadData.download_status_finished;
		if (mMoboDownloadListener != null)
			mMoboDownloadListener.onDownloadFinished(downloadData);
	}

	public void onDownloadFailed(int id) {
		downloadData.status = StreamingDownloadData.download_status_failed;
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
			downloadData.startTime = nativeGetDownloadedLen();
		return downloadData.startTime;
	}

	public native void nativeStartDownload(String streamingUrl,
			String fileSavePath, long finishedSize);// Long[] ptsArray

	public native void nativePauseDownload();

	public native void nativeResumeDownload();

	public native void nativeStopDownload();

	public native int nativeGetDuration();

	public native int nativeGetDownloadedLen();

	public native int nativeGetStartDownloadedTime();

	private class DownloadLib extends AsyncTask<Void, Integer, Void> {
		StreamingDownloadData mStreamingDownloadData = null;

		public DownloadLib(StreamingDownloadData data) {
			mStreamingDownloadData = data;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mStreamingDownloadData.status = StreamingDownloadData.download_status_started;
			nativeStartDownload(mStreamingDownloadData.streamingUrl,
					mStreamingDownloadData.fileSavePath,
					mStreamingDownloadData.finishSize);// mStreamingDownloadData.getPtsArray()
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
