package com.clov4r.moboplayer.android.nil.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/** 序列化数据 **/
public class DataSaveLib {

	public static final String flag_serializable_data = "flag_serializable_data";
	/** 保存解码方式列表 **/
	public static final String name_of_format_list = "name_of_format_list";
	String saveDirPath = "";
	String saveFilePath = "";
	File saveDir = null;
	Context mContext = null;

	public DataSaveLib(Context context, String fileName) {
		saveDirPath = Global.dataSavePath + flag_serializable_data
				+ File.separator;
		saveFilePath = saveDirPath + fileName;
		saveDir = new File(saveDirPath);
		if (!saveDir.exists())
			saveDir.mkdirs();

		File file = new File(saveFilePath);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				File tempFile1 = new File(Global.rootPath);
				if (!tempFile1.exists())
					tempFile1.mkdir();
				File tempFile2 = new File(Global.dataSavePath);
				if (!tempFile2.exists())
					tempFile2.mkdir();
				File tempFile3 = new File(saveDirPath);
				if (!tempFile3.exists())
					tempFile3.mkdir();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

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
