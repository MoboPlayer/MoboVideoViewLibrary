package com.clov4r.moboplayer.android.nil.library;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/** 软解字幕或音轨 ，以文件（视频或字幕）为单位，此类用以记录本次解析的信息 **/
public class SoftDecodeSAData {
	// public static final int type_subtitle_inner = 0;
	// public static final int type_subtitle_ext = 1;
	public static final int type_subtitle = 3;
	public static final int type_audio = 1;

	/** 待解析的视频或字幕的文件路径 **/
	public String filePath = null;
	public int beanIndex = -1;
	/** 解析的类型：字幕0、音轨1 **/
	public int decodeType = -1;
	/** 数据源的索引：如第几个字幕 **/
	public int sourceIndex = 0;
	/** 数据内部索引：如某个字幕内还有多个字幕流，其索引 **/
	public int streamIndex = 0;
	/** 1、图片；2、文字；3、ass特效字幕 **/
	public int subtitleType;
	public boolean initFinished = false;

	public boolean isSubtitle() {
		return beanIndex == type_subtitle;
	}

	public ByteBuffer byteBuffer = null;// ByteBuffer.allocateDirect(1024 *
										// 1024);

	public String getSubtitleText(String character) {
		if (decodeType == type_subtitle && byteBuffer != null) {
			try {
				return new String(byteBuffer.array(), character);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String toString() {
		return filePath + decodeType + sourceIndex + streamIndex;
	}

	public boolean equals(SoftDecodeSAData data) {
		return toString().equals(data.toString());
	}
}
