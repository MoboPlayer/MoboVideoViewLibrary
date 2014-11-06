package com.clov4r.moboplayer.android.nil.library;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/** �����Ļ������ �����ļ�����Ƶ����Ļ��Ϊ��λ���������Լ�¼���ν�������Ϣ **/
public class SoftDecodeSAData {
	// public static final int type_subtitle_inner = 0;
	// public static final int type_subtitle_ext = 1;
	public static final int type_subtitle = 3;
	public static final int type_audio = 1;

	/** ����������Ƶ����Ļ���ļ�·�� **/
	public String filePath = null;
	public int beanIndex = -1;
	/** ���������ͣ���Ļ0������1 **/
	public int decodeType = -1;
	/** ����Դ����������ڼ�����Ļ **/
	public int sourceIndex = 0;
	/** �����ڲ���������ĳ����Ļ�ڻ��ж����Ļ���������� **/
	public int streamIndex = 0;
	/** 1��ͼƬ��2�����֣�3��ass��Ч��Ļ **/
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
