package com.clov4r.moboplayer.android.nil.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtil {
	private static HashMap<String, Bitmap> existedMap = new HashMap<String, Bitmap>();

	public static Bitmap getOriginBitmap(String path) {
		if (existedMap.containsKey(path) && !existedMap.get(path).isRecycled()) {
			return existedMap.get(path);
		} else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			existedMap.put(path, bitmap);
			return bitmap;
		}
	}

	public static Bitmap getScaledBitmap(String path, Bitmap bm, int viewWidth,
			int viewHeight) {
		Bitmap result = existedMap.get(path);
		if (result != null && !result.isRecycled())
			return result;
		if (bm != null) {
			Matrix matrix = new Matrix();
			float sx = (float) viewWidth / bm.getWidth();
			float sy = (float) viewHeight / bm.getHeight();
			matrix.postScale(sx, sy);
			result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
			existedMap.put(path, result);
		}
		return result;
	}

	public static void releaseAllBitmap() {
		Iterator<String> iterator = existedMap.keySet().iterator();
		while (iterator.hasNext()) {
			String path = iterator.next();
			releaseBitmap(path);
		}
	}

	public static void releaseBitmap(String path) {
		if (existedMap.containsKey(path)) {
			Bitmap tmpMap = existedMap.get(path);
			if (tmpMap != null && !tmpMap.isRecycled()) {
				tmpMap.recycle();
			}
			existedMap.remove(path);
		}
	}

	public static void saveMyBitmap(String savePath, Bitmap mBitmap) {
		File f = new File(savePath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
