package com.clov4r.moboplayer.android.nil.library;

import java.util.Comparator;

public class AudioAndSubtitleSortLib implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 instanceof LocalSubtitle) {
			return compareSubtitle((LocalSubtitle) arg0, (LocalSubtitle) arg1);
		} else if (arg0 instanceof LocalAudioInfo) {
			return compareAudioTrack((LocalAudioInfo) arg0,
					(LocalAudioInfo) arg1);
		}

		return 0;
	}

	private int compareSubtitle(LocalSubtitle sub1, LocalSubtitle sub2) {
		return sub1.index - sub2.index;
	}

	private int compareAudioTrack(LocalAudioInfo audio1, LocalAudioInfo audio2) {
		return audio1.index - audio2.index;
	}
}
