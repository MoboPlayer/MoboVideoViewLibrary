package com.clov4r.moboplayer.android.nil.library;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SoftDecodeSAManager {
	private static SoftDecodeSAManager mSoftDecodeSAManager = null;
	private ArrayList<SoftDecodeSAData> decodeDataList = new ArrayList<SoftDecodeSAData>();
	private int flag = 0;

	// private int currentSubBeanIndex = 0;
	// private int currentAudioBeanIndex = 0;

	public SoftDecodeSAManager() {

	}

	public static SoftDecodeSAManager getInstanceOf() {
		if (mSoftDecodeSAManager == null)
			mSoftDecodeSAManager = new SoftDecodeSAManager();
		return mSoftDecodeSAManager;
	}

	public int getBeanIndexOf(String path, int decodeType, int sourceIndex,
			int streamIndex) {
		SoftDecodeSAData data = getBeanOf(path, decodeType, sourceIndex,
				streamIndex);
		if (data != null)
			return data.beanIndex;
		return -1;
	}

	public void removeBeanOf(String path, int decodeType, int sourceIndex,
			int streamIndex) {
		SoftDecodeSAData data = getBeanOf(path, decodeType, sourceIndex,
				streamIndex);
		if (data != null)
			decodeDataList.remove(data);
	}

	public void removeBeanOf(int beanIndex) {
		SoftDecodeSAData data = getBeanOf(beanIndex);
		if (data != null)
			decodeDataList.remove(data);
	}

	public void removeAll() {
		decodeDataList.clear();
		mSoftDecodeSAManager = null;
	}

	public int addBeanOf(String path, int decodeType, int sourceIndex,
			int streamIndex) {
		SoftDecodeSAData data = new SoftDecodeSAData();
		data.filePath = path;
		data.decodeType = decodeType;
		data.beanIndex = flag;
		data.sourceIndex = sourceIndex;
		data.streamIndex = streamIndex;
		flag++;
		decodeDataList.add(data);
		if (decodeType == SoftDecodeSAData.type_subtitle)
			data.byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
		return data.beanIndex;
	}

	// public int getCurrentSubtitleBeanIndex() {
	// return currentSubBeanIndex;
	// }
	//
	// public int getCurrentAudioBeanIndex() {
	// return currentAudioBeanIndex;
	// }

	public ArrayList<Integer> getAllBeanIndexList() {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; i < decodeDataList.size(); i++) {
			idList.add(decodeDataList.get(i).beanIndex);
		}
		return idList;
	}

	public ArrayList<SoftDecodeSAData> getAllSubtitleData() {
		ArrayList<SoftDecodeSAData> result = new ArrayList<SoftDecodeSAData>();
		for (int i = 0; i < decodeDataList.size(); i++) {
			SoftDecodeSAData tmpData = decodeDataList.get(i);
			if (tmpData.decodeType == SoftDecodeSAData.type_subtitle)
				result.add(tmpData);
		}
		return result;
	}

	public ArrayList<SoftDecodeSAData> getAllTextSubtitleData() {
		ArrayList<SoftDecodeSAData> result = new ArrayList<SoftDecodeSAData>();
		for (int i = 0; i < decodeDataList.size(); i++) {
			SoftDecodeSAData tmpData = decodeDataList.get(i);
			if (tmpData.decodeType == SoftDecodeSAData.type_subtitle
					&& tmpData.subtitleType > 1)
				result.add(tmpData);
		}
		return result;
	}

	public String getTextSubtitle(String character) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < decodeDataList.size(); i++) {
			SoftDecodeSAData tmpData = decodeDataList.get(i);
			if (tmpData.decodeType == SoftDecodeSAData.type_subtitle
					&& tmpData.subtitleType > 1)
				sb.append(tmpData.getSubtitleText(character));
		}
		return sb.toString();
	}

	public SoftDecodeSAData getImageSubtitleData() {
		for (int i = 0; i < decodeDataList.size(); i++) {
			SoftDecodeSAData tmpData = decodeDataList.get(i);
			if (tmpData.decodeType == SoftDecodeSAData.type_subtitle
					&& tmpData.subtitleType == 1) {
				return tmpData;
			}
		}
		return null;
	}

	public boolean isDecodingBySoft() {
		return decodeDataList.size() > 0;
	}

	private SoftDecodeSAData getBeanOf(String path, int decodeType,
			int sourceIndex, int streamIndex) {
		if (path != null)
			if (decodeType == SoftDecodeSAData.type_subtitle)
				for (int i = 0; i < decodeDataList.size(); i++) {
					SoftDecodeSAData data = decodeDataList.get(i);
					if (data.subtitleType == 1 || path.endsWith("idx")){//idx、图形字幕只用一个源
						if (path.equals(data.filePath)
								&& decodeType == data.decodeType
								&& sourceIndex == data.sourceIndex) {
							return data;
						}
					}else{
						if (path.equals(data.filePath)
								&& decodeType == data.decodeType
								&& sourceIndex == data.sourceIndex
								&& streamIndex == data.streamIndex) {
							return data;
						}
					}
				}
			else {
				for (int i = 0; i < decodeDataList.size(); i++) {
					SoftDecodeSAData data = decodeDataList.get(i);
					if (data.filePath.equals(path)
							&& decodeType == data.decodeType)
						return data;
				}
			}
		return null;
	}

	public SoftDecodeSAData getBeanOf(int beanIndex) {
		for (int i = 0; i < decodeDataList.size(); i++) {
			SoftDecodeSAData data = decodeDataList.get(i);
			if (beanIndex == data.beanIndex) {
				return data;
			}
		}
		return null;
	}
}
