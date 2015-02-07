package com.clov4r.moboplayer.android.nil.library;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;


/**
 * 工具类，包含一些全局的常量和方法
 * 
 * @author lyw
 * 
 */
public class Global {
	public static String root = "/mnt/sdcard/";
	public static String rootPath = root+".mobovideoview";
	public static String dataSavePath = rootPath + "/data/";
	/** width为短的一边，height为长的一边 **/
	public static int screenHeight, screenWidth;
	public static int screenSize = 0;;
	public static boolean isPad = false;

	public static String getNameOf(String path) {
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory())
					return file.getName();
				else {
					String fileName = file.getName();
					if (fileName.contains("."))
						return fileName.substring(0, fileName.indexOf("."));
					else
						return fileName;
				}
			} else {
				if (path.contains("/") && path.contains(".")) {
					int index1 = path.lastIndexOf("/");
					int index2 = path.lastIndexOf(".");
					if (index1 < index2) {
						return path.substring(index1 + 1, index2);
					} else
						return path.hashCode() + "";
				}
			}
		}
		return null;
	}

	/**
	 * 获取文件格式
	 * 
	 * @param absPath
	 * @return
	 */
	public static String getFileFormat(String absPath) {
		if (absPath != null && absPath.contains(".")) {
			String format = absPath.substring(absPath.lastIndexOf(".") + 1);
			if (format == null || "".equals(format) || format.length() > 6) {
				if (format == null)
					format = "mobo";
				else if (absPath.contains("3gp"))
					format = "3gp";
				else if (absPath.contains("mp4"))
					format = "mp4";
				else if (absPath.contains("flv"))
					format = "flv";
				else if (absPath.contains("3gpp"))
					format = "3gpp";
				else
					format = "mobo";
			}
			return format.toLowerCase();
		}
		return absPath;
	}

	public static int parseInt(Object obj, int defaultInt) {
		try {
			return parseInt(obj.toString());
		} catch (Exception e) {
			return defaultInt;
		}
	}

	public static long parseLong(Object obj, long defaultLong) {
		try {
			return parseLong(obj.toString());
		} catch (Exception e) {
			return defaultLong;
		}
	}

	public static float parseFloat(Object obj, float defaultFloat) {
		try {
			return parseFloat(obj.toString());
		} catch (Exception e) {
			return defaultFloat;
		}
	}

	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long parseLong(String s) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static float parseFloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return 0f;
		}
	}

	public static boolean parseBoolean(String s) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean hasPermanentMenuKey(Context con) {
		ViewConfiguration configuration = ViewConfiguration.get(con);
		try {
			Object result = configuration.getClass()
					.getMethod("hasPermanentMenuKey", new Class[] {})
					.invoke(configuration, new Object[] {});

			if (result != null && result instanceof Boolean)
				return Boolean.parseBoolean(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@SuppressLint("NewApi")
	public static int getRealScreenSize(boolean isWidth, Activity act) {
		final DisplayMetrics metrics = new DisplayMetrics();
		Display display = act.getWindowManager().getDefaultDisplay();
		if (hasPermanentMenuKey(act)) {
			act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = metrics.widthPixels; // 屏幕宽度（像素）
			int height = metrics.heightPixels; // 屏幕高度（像素）

			if (isWidth)
				return width;
			else
				return height;
		} else {
			Method mGetRawH = null, mGetRawW = null;

			// Not real dimensions
			display.getMetrics(metrics);
			int width = metrics.heightPixels;
			int height = metrics.widthPixels;

			try {
				// For JellyBeans and onward
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
					display.getRealMetrics(metrics);

					// Real dimensions
					width = metrics.heightPixels;
					height = metrics.widthPixels;
				} else {
					mGetRawH = Display.class.getMethod("getRawHeight");
					mGetRawW = Display.class.getMethod("getRawWidth");

					try {
						width = (Integer) mGetRawW.invoke(display);
						height = (Integer) mGetRawH.invoke(display);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			} catch (NoSuchMethodException e3) {
				e3.printStackTrace();
			}

			if (isWidth) {
				return width;
			} else {
				return height;
			}
		}
	}
	
	/**
	 * 检测sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailability() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
