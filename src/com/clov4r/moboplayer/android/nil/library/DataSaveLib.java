package com.clov4r.moboplayer.android.nil.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;

/** 序列化数据 **/
public class DataSaveLib {

	public static final String flag_serializable_data = "flag_serializable_data";
	/** 保存解码方式列表 **/
	public static final String name_of_format_list = "name_of_format_list";
	/** 保存Streaming下载信息 **/
	public static final String name_of_streaming_download_info = "name_of_streaming_download_info";
	String saveDirPath = "";
	String saveFilePath = "";
	File saveDir = null;
	Context mContext = null;

	public DataSaveLib(Context context, String fileName) {
		initPath(context, fileName, false);
	}

	public DataSaveLib(Context context, String fileName,
			boolean saveInnerStorage) {
		initPath(context, fileName, saveInnerStorage);
	}

	private void initPath(Context context, String fileName, boolean isInner) {
		mContext = context;
		String rootPath = null;
		if (isInner) {
			rootPath = context.getFilesDir().getAbsolutePath();// context.getExternalFilesDir(null).toString();
		} else {
			rootPath = Global.getDownloadPath(mContext);//getDataSavePath();
		}
		saveDirPath = rootPath + File.separator + flag_serializable_data
				+ File.separator;
		saveFilePath = saveDirPath + fileName;
		saveDir = new File(saveDirPath);
		boolean created = false;
		if (!saveDir.exists())
			created = saveDir.mkdirs();
	}

	String getPackageName() {
		return mContext.getPackageName();
	}

//	String getDataSavePath() {
//		return Environment.getExternalStorageDirectory().getAbsoluteFile()
//				+ File.separator + getPackageName();
//	}

	/**
	 * 将数据序列化到本地
	 * 
	 * @param data
	 */
	public void saveData(Object data) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(saveFilePath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 ** 读取本地序列化的数据
	 * 
	 * @return
	 */
	public Object readData() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			File file = new File(saveFilePath);
			if (!file.exists() || file.length() <= 0)
				return null;
			fis = new FileInputStream(saveFilePath);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
