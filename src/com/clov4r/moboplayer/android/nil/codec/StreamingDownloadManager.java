package com.clov4r.moboplayer.android.nil.codec;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.SparseArray;

import com.clov4r.moboplayer.android.nil.library.DataSaveLib;

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
public class StreamingDownloadManager {
	private static StreamingDownloadManager mStreamingDownloadManager = null;
	private ArrayList<MoboDownloadListener> listenerList = null;
	private HashMap<Integer, StreamingDownloadData> dataMap = null;
	private HashMap<Integer, StreamingDownloadLib> libMap = null;
	private Context mContext = null;
	private DataSaveLib mDataSaveLib = null;

	public StreamingDownloadManager(Context con) {
		mContext = con;
		mDataSaveLib = new DataSaveLib(con,
				DataSaveLib.name_of_streaming_download_info, false);
		dataMap = (HashMap) mDataSaveLib.readData();
		if (dataMap == null)
			dataMap = new HashMap<Integer, StreamingDownloadData>();
		libMap = new HashMap<Integer, StreamingDownloadLib>();
		listenerList = new ArrayList<MoboDownloadListener>();
	}

	public static StreamingDownloadManager getInstance(Context con) {
		if (mStreamingDownloadManager == null)
			mStreamingDownloadManager = new StreamingDownloadManager(con);
		return mStreamingDownloadManager;
	}

	/**
	 * Save the download task info.
	 */
	public void saveDownloadInfo() {
		mDataSaveLib.saveData(dataMap);
	}

	public void stopAll() {
		if (libMap == null)
			return;
		Iterator<Integer> iterator = libMap.keySet().iterator();
		while (iterator.hasNext()) {
			stopDownload(iterator.next());
		}
		saveDownloadInfo();
		dataMap.clear();
		dataMap = null;
		libMap.clear();
		libMap = null;
		mStreamingDownloadManager = null;
	}

	/**
	 * Start a download task.It will be created if not existed.
	 * 
	 * @param streamingUrl
	 * @param fileSavePath
	 * @return
	 */
	public int startDownload(String streamingUrl, String fileSavePath,
			int timeToDownload,boolean isLive) {
		int key = getKeyOf(streamingUrl, fileSavePath);
		StreamingDownloadLib tmpLib = null;
		if (libMap.containsKey(key)) {
			tmpLib = libMap.get(key);
		} else {
			StreamingDownloadData downloadData = null;
			if (dataMap.containsKey(key))
				downloadData = dataMap.get(key);
			else
				downloadData = new StreamingDownloadData();
			downloadData.streamingUrl = streamingUrl;
			downloadData.fileSavePath = fileSavePath;
			downloadData.packetFile = fileSavePath + ".pkts";
			downloadData.timeStartToDownload = timeToDownload;
			downloadData.id = key;
			dataMap.put(key, downloadData);

			tmpLib = new StreamingDownloadLib(downloadData);
			tmpLib.setDownloadListener(mMoboDownloadListener);
			libMap.put(key, tmpLib);
		}
		if (tmpLib.getDownloadStatus() == StreamingDownloadData.download_status_stoped
				|| tmpLib.getDownloadStatus() == StreamingDownloadData.download_status_failed)
			tmpLib.startDownload();
		else if (tmpLib.getDownloadStatus() == StreamingDownloadData.download_status_paused)
			tmpLib.resumeDownload();
		return key;
	}

	/**
	 * Pause download task
	 * 
	 * @param downloadId
	 */
	public void pauseDownload(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			tmpLib.pauseDownload();
		}
	}

	/**
	 * Restart an existed download task
	 * 
	 * @param downloadId
	 */
	public void resumeDownload(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			tmpLib.resumeDownload();
		}
	}

	/**
	 * Stop download task
	 * 
	 * @param downloadId
	 */
	public void stopDownload(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			tmpLib.stopDownload();
			libMap.remove(downloadId);
			saveDownloadInfo();
		}
	}

	public void stopTempBuffer(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			tmpLib.stopTempBuffer();
		}

	}

	public void removeDownload(int downloadId, boolean deleteDownloadedFile) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			int status = tmpLib.getDownloadStatus();
			if (status == StreamingDownloadData.download_status_started
					|| status == StreamingDownloadData.download_status_paused)
				tmpLib.stopDownload();

			libMap.remove(downloadId);
			dataMap.remove(downloadId);
			saveDownloadInfo();
			if (deleteDownloadedFile) {
				File file = new File(tmpLib.downloadData.fileSavePath);
				file.deleteOnExit();
				File pkt_file = new File(tmpLib.downloadData.packetFile);
				pkt_file.deleteOnExit();
				File tmp_pkt_file = new File(tmpLib.downloadData.packetFile+".tmp");
				tmp_pkt_file.deleteOnExit();
			}
		}
	}

	private int getKeyOf(String url, String path) {
		return (url + path).hashCode();
	}

	/**
	 * Get the duration of video
	 * 
	 * @param id
	 * @return
	 */
	public int getDurationOf(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			return tmpLib.getDuration();
		}
		return 0;
	}

	/**
	 * Get the time of video has been downloaded to.
	 * 
	 * @return
	 */
	public int getCurrentTimeDownloadedToOf(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			return tmpLib.getCurrentTimeDownloadedTo();
		}
		return 0;
	}

	/**
	 * Get the time of video start to download
	 * 
	 * @return
	 */
	public int getStartDownloadedTimeOf(int downloadId) {
		if (libMap.containsKey(downloadId)) {
			StreamingDownloadLib tmpLib = libMap.get(downloadId);
			return tmpLib.getStartDownloadedTime();
		}
		return 0;
	}

	public StreamingDownloadData getDownloadDataOfUrl(String url,
			String savePath) {
		Iterator<Integer> iterator = dataMap.keySet().iterator();
		while (iterator.hasNext()) {
			int id = iterator.next();
			StreamingDownloadData tmpData = dataMap.get(id);
			if (tmpData.streamingUrl.equals(url)
					&& tmpData.fileSavePath.equals(savePath)) {
				return tmpData;
			}
		}
		return null;
	}

	public int getDownloadIdOf(String url, String savePath) {
		StreamingDownloadData tmpData = getDownloadDataOfUrl(url, savePath);
		if (tmpData != null)
			return tmpData.id;
		return 0;
	}

	MoboDownloadListener mMoboDownloadListener = null;

	public void setDownloadListener(MoboDownloadListener listener) {
		mMoboDownloadListener = listener;
	}

	public interface MoboDownloadListener {

		public void onDownloadProgressChanged(StreamingDownloadData data,
				int currentTime);

		public void onDownloadFinished(StreamingDownloadData data);

		public void onDownloadFailed(StreamingDownloadData data);
	}

	public static class StreamingDownloadData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7489788052720159408L;
		public static final int download_status_paused = -1;
		public static final int download_status_stoped = 0;
		public static final int download_status_started = 1;
		public static final int download_status_finished = 2;
		public static final int download_status_failed = 3;

		public static final int DOWNLOAD_DEFAULT = 0;
		public static final int DOWNLOAD_PACKET_TEMP = 1;

		public int id;
		public String streamingUrl;
		public String fileSavePath;
		public String packetFile;
		// public int progress;
		/** 已经下载的字节数 **/
		public long finishSize;
		/** 当前下载到的时间 ，单位秒 **/
		public int currentTime;
		public int startTime = -1;
		/** 开始下载的播放时间 ----暂时不用了，因为跳转直接在底层做了 **/
		public int timeStartToDownload = 0;
		public int duration = 0;
		// public boolean isFinished;
		// public boolean isDownloadFailed;
		public int status = download_status_stoped;
		public String failedMsg = null;
		public long last_video_dts;
		/**是否为直播**/
		public boolean isLive;
		// /** 视频中每个stream与pts的对应关系 **/
		// HashMap<Integer, Long> stm_index_pts_map = new HashMap<Integer,
		// Long>();
		//
		// long[] getPtsArray() {
		// // stm_index_pts_map.put(0, 7040l);
		// // stm_index_pts_map.put(1, 6919l);
		// if (stm_index_pts_map.size() == 0)
		// return null;
		// else {
		// long[] resArray = new long[stm_index_pts_map.size()];
		// for (int i = 0; i < stm_index_pts_map.size(); i++)
		// if (stm_index_pts_map.containsKey(i))
		// resArray[i] = stm_index_pts_map.get(i);
		// return resArray;
		// }
		// }
	}

}
