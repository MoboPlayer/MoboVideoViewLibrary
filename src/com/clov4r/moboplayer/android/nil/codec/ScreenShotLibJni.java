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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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
package com.clov4r.moboplayer.android.nil.codec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class ScreenShotLibJni extends BaseJNILib {

	private static ScreenShotLibJni mScreenShotLib = null;

	public static ScreenShotLibJni getInstance() {
		if (mScreenShotLib == null)
			mScreenShotLib = new ScreenShotLibJni();
		return mScreenShotLib;
	}

	private HashMap<String, String> pathMap = new HashMap<String, String>();

	public native String getThumbnail(String videoName, int position,
			int width, int height);

	public native String getIDRThumbnail(String videoName, int width, int height);

	OnBitmapCreatedListener mOnBitmapCreatedListener = null;

	public void setOnBitmapCreatedListener(OnBitmapCreatedListener listener) {
		mOnBitmapCreatedListener = listener;
	}

	public void getIDRFrameThumbnail(String videoPath,
			String thumbnailSavePath, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		getIDRThumbnail(videoPath, width, height);
	}

	public void getScreenShot(String videoPath, String thumbnailSavePath,
			int position, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		getThumbnail(videoPath, position, width, height);
	}

	public void createBitmap(ByteBuffer bitmapData, String size, String fileName) {
		if (bitmapData != null) {
			IntBuffer intBuffer = bitmapData.asIntBuffer();
			String[] sizeArray = size.split(",");
			int[] data = new int[intBuffer.limit()];
			intBuffer.get(data);
			Bitmap bitmap = Bitmap.createBitmap(data,
					Integer.parseInt(sizeArray[0]),
					Integer.parseInt(sizeArray[1]), Config.ARGB_8888);
			Log.e("ScreenShotLib", "" + size);
			// return bitmap;
			if (bitmap != null && pathMap.containsKey(fileName)) {
				saveBitmap(pathMap.get(fileName), bitmap);
			}
			if (mOnBitmapCreatedListener != null)
				mOnBitmapCreatedListener.onBitmapCreated(bitmap, fileName,
						pathMap.get(fileName));
		} else if (mOnBitmapCreatedListener != null)
			mOnBitmapCreatedListener.onBitmapCreatedFailed(fileName);
	}

	public void initByteBuffer(ByteBuffer buffer, int size) {
		buffer = ByteBuffer.allocateDirect(size);
	}

	public interface OnBitmapCreatedListener {
		public void onBitmapCreated(Bitmap bitmap, String videoPath,
				String screenshotSavePath);

		public void onBitmapCreatedFailed(String videoPath);
	}

	private void saveBitmap(String path, Bitmap bitmap) {
		File file = new File(path);
		if (file.exists())
			file.delete();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
			}
		}

	}
}
