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
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.CalendarContract.Colors;
import android.util.Log;

public class ScreenShotLibJni extends BaseJNILib {

	private static ScreenShotLibJni mScreenShotLib = null;

	public ScreenShotLibJni() {
		super();
	}

	public static ScreenShotLibJni getInstance() {
		if (mScreenShotLib == null)
			mScreenShotLib = new ScreenShotLibJni();
		return mScreenShotLib;
	}

	private HashMap<String, String> pathMap = new HashMap<String, String>();

	protected native Bitmap getThumbnail(String videoName, String imagePath,
			int position, int width, int height);

	protected native Bitmap stopGetThumbnail();

	protected native Bitmap getKeyFrameThumbnail(String videoName,
			int position, int width, int height);

	protected native Bitmap getKeyFrameThumbnail2(String videoName,
			String imagePath, int position, int width, int height);

	protected native Bitmap getIDRThumbnail(String videoName, String imagePath,
			int width, int height);

	protected native void releaseByteBuffer(ByteBuffer buffer);

	OnBitmapCreatedListener mOnBitmapCreatedListener = null;

	public void setOnBitmapCreatedListener(OnBitmapCreatedListener listener) {
		mOnBitmapCreatedListener = listener;
	}

	/**
	 * Get the first IDR frame,and save it to thumbnailSavePath
	 * @param videoPath
	 * @param thumbnailSavePath
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getIDRFrameThumbnail(String videoPath,
			String thumbnailSavePath, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		return getIDRThumbnail(videoPath, thumbnailSavePath, width, height);
	}
	
	public void stopCreatingThumbnail(){
		stopGetThumbnail();
	}
	
	/**
	 * Get a frame(maybe not key frame)
	 * @param videoPath
	 * @param thumbnailSavePath
	 * @param position
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getScreenShot(String videoPath, String thumbnailSavePath,
			int position, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		return getThumbnail(videoPath, thumbnailSavePath, position, width,
				height);
	}

	/**
	 * Get a key frame and save as ARGB format
	 * @param videoPath
	 * @param thumbnailSavePath
	 * @param position
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getKeyFrameScreenShot(String videoPath,
			String thumbnailSavePath, int position, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		return getKeyFrameThumbnail(videoPath, position, width, height);
	}

	/**
	 * Get a key frame and save as RGB565 format
	 * @param videoPath
	 * @param thumbnailSavePath
	 * @param position
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getKeyFrameScreenShot_2(String videoPath,
			String thumbnailSavePath, int position, int width, int height) {
		pathMap.put(videoPath, thumbnailSavePath);
		return getKeyFrameThumbnail2(videoPath, thumbnailSavePath, position,
				width, height);
	}
	
	/**
	 * @deprecated
	 * @param bitmapData
	 * @param size
	 * @param fileName
	 * @return
	 */
	public Bitmap createBitmap(ByteBuffer bitmapData, String size,
			String fileName) {
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
			// releaseByteBuffer(bitmapData);
			Log.e("", "release finished");
			return bitmap;
		} else if (mOnBitmapCreatedListener != null)
			mOnBitmapCreatedListener.onBitmapCreatedFailed(fileName);
		return null;
	}

	public Bitmap createBitmap(String fileName, String imgPath,
			int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.outHeight = height;
		options.outWidth = width;
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
		if (bitmap == null) {// 截图成功
			if (mOnBitmapCreatedListener != null)
				mOnBitmapCreatedListener.onBitmapCreated(bitmap, fileName,
						imgPath);
		} else {
			if(!new File(imgPath).exists())
			    saveBitmap(imgPath, bitmap);
			if (mOnBitmapCreatedListener != null)
				mOnBitmapCreatedListener.onBitmapCreatedFailed(fileName);
		}
		return bitmap;
	}

	public Bitmap createBitmap(String fileName, String imgPath, byte[] data,
			int width, int height) {
		if (data != null) {
			/** RGB565 **/
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
			bitmap.copyPixelsFromBuffer(buffer);
			saveBitmap(imgPath, bitmap);
			if (mOnBitmapCreatedListener != null) {
				if (bitmap != null)
					mOnBitmapCreatedListener.onBitmapCreated(bitmap, fileName,
							imgPath);
			}
			return bitmap;
		} else {
			if (mOnBitmapCreatedListener != null)
				mOnBitmapCreatedListener.onBitmapCreatedFailed(fileName);
			return null;
		}
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
