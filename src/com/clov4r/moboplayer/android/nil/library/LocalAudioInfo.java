package com.clov4r.moboplayer.android.nil.library;

import java.io.Serializable;

/**
 * 音轨的数据结构
 * 
 * @author lyw
 * 
 */
public class LocalAudioInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -554156939021638607L;
	public String title;
	public String lanuage;
	public String sampleRate;
	public String soundTrack;
	public String codecName;
	public int index = 0;

	public String toString() {
		String msg = title + "--" + lanuage;
		// + "--" + sampleRate + "--"
		// + codecName;//
		// soundTrack +
		return msg;// .toLowerCase().replace("und", "").replace("unknown_codec",
					// "")
		// .replace("unknown", "");
	}
}
