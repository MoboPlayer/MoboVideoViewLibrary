package com.clov4r.moboplayer.android.videocut;

import org.apache.http.protocol.ExecutionContext;

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
public class CommenCommandLibGif extends CommenCommandLib {
	final String gif_command_1 = "ffmpeg -y -ss %s -t %s -i %s -vf fps=%s,scale=%s:-1:flags=lanczos,palettegen %s";
	final String gif_command_2 = "ffmpeg -ss %s -t %s -i %s -i %s -filter_complex \"fps=%s,scale=%s:-1:flags=lanczos[x];[x][1:v]paletteuse\" %s";
	final String gif_command_3 = "ffmpeg -i %s -ss %s -r %s -s %s -t %s %s";

	String startTime;
	String duration;
	String fps;
	String width;
	String height;
	String videoPath;
	String gifPath;
	String palettePath;

	public CommenCommandLibGif() {

	}

	public int start(boolean highQuality) {
		int ret = -1;
		if (highQuality) {
			String command_1 = String.format(gif_command_1, startTime,
					duration, videoPath, fps, width, palettePath);
			String command_2 = String.format(gif_command_2, startTime,
					duration, videoPath, palettePath, fps, width, gifPath);
			initCommand(command_1);
			ret = excute();
			if (ret >= 0) {
				initCommand(command_2);
				ret = excute();
			}
		} else {
			String command_3 = String.format(gif_command_3, videoPath,
					startTime, fps, width +"x" +height, duration, gifPath);
			initCommand(command_3);
			ret = excute();
		}
		return ret;
	}

	public CommenCommandLibGif setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public CommenCommandLibGif setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public CommenCommandLibGif setFps(String fps) {
		this.fps = fps;
		return this;
	}

	public CommenCommandLibGif setWidth(String width) {
		this.width = width;
		return this;
	}

	public CommenCommandLibGif setHeight(String height) {
		this.height = height;
		return this;
	}

	public CommenCommandLibGif setVideoPath(String videoPath) {
		this.videoPath = videoPath;
		palettePath = videoPath.substring(0, videoPath.lastIndexOf("/") + 1)
				+ System.currentTimeMillis() + ".png";
		return this;
	}

	public CommenCommandLibGif setGifPath(String gifPath) {
		this.gifPath = gifPath;
		return this;
	}

}
