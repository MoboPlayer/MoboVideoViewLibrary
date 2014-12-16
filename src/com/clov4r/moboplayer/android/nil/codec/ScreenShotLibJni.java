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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ScreenShotLibJni extends BaseJNILib {

	private static ScreenShotLibJni mScreenShotLib = null;

	public static ScreenShotLibJni getInstance() {
		if (mScreenShotLib == null)
			mScreenShotLib = new ScreenShotLibJni();
		return mScreenShotLib;
	}

	public native String getThumbnail(String videoName, int position,
			int width, int height);// ByteBuffer bitmapData,

	OnBitmapCreatedListener mOnBitmapCreatedListener=null;
	public void setOnBitmapCreatedListener(OnBitmapCreatedListener listener){
		mOnBitmapCreatedListener=listener;
	}
	
	public void getScreenShot(String videoName,
			int position, int width, int height) {
		// ByteBuffer bitmapData = null;//ByteBuffer.allocateDirect(3000 *
		// 1024);
		String size = getThumbnail(videoName, position, width, height);
	}

	public void createBitmap(ByteBuffer bitmapData, String size,String fileName) {
		IntBuffer intBuffer = bitmapData.asIntBuffer();
		String[] sizeArray = size.split(",");
		int[] data = new int[intBuffer.limit()];
		intBuffer.get(data);
		Bitmap bitmap = Bitmap.createBitmap(data,
				Integer.parseInt(sizeArray[0]), Integer.parseInt(sizeArray[1]),
				Config.ARGB_8888);
		Log.e("ScreenShotLib", "" + size);
		// return bitmap;
		if(mOnBitmapCreatedListener!=null)
			mOnBitmapCreatedListener.onBitmapCreated(bitmap, fileName);
	}

	public void initByteBuffer(ByteBuffer buffer, int size) {
		buffer = ByteBuffer.allocateDirect(size);
	}
	
	public interface OnBitmapCreatedListener{
		public void onBitmapCreated(Bitmap bitmap,String fileName);
	}
}
