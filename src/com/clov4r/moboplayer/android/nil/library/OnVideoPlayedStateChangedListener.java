package com.clov4r.moboplayer.android.nil.library;


public interface OnVideoPlayedStateChangedListener {
	public void onStart();

	public void onProgressChanged(int progress);

	public void onSizeChanged(int width, int height);

	public void onFinished();
}
