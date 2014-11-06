package com.clov4r.moboplayer.android.nil.library;

import java.io.Serializable;

public class LocalSubtitle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6710231091229647703L;
	// public int type = 0;//
	public String title;
	public String lanuage = "";//
	public String codecName = "";//
	public boolean isInnerSubtitle = true;
	public int index = 0;
	public int streamIndex = 0;
	public boolean isSelected = false;

	public String toString() {
		String msg = title + "--" + lanuage + "--" + codecName;//
		return msg;// .toLowerCase().replace("und", "").replace("unknown_codec",
					// "").replace("unknown", "");
	}

}
