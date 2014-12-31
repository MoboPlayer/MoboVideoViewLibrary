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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;

public class SubtitleJni extends BaseJNILib {

	private static SubtitleJni mSubtitleJni = null;

	public static SubtitleJni getInstance() {
		if (mSubtitleJni == null)
			mSubtitleJni = new SubtitleJni();
		return mSubtitleJni;
	}

	/**
	 * open subtitle file
	 * 
	 * @param filePath
	 * @param index
	 * @return <0 then failed
	 */
	public native int openSubtitleFileInJNI(String filePath, int index,
			int subtiltle_index);

	public int openSubtitleFile(String filePath, int index, int subtiltle_index) {
		String charSet = getFilecharset(new File(filePath));
		if (!charSet.equals("UTF-8")) {
			try {
				String tempPath = filePath.substring(0, filePath.length() - 4)
						+ "mobo_temp_utf-8.srt";
				File tempFile = new File(tempPath);
				if (!tempFile.exists()) {
					FileUtils.writeLines(tempFile, "UTF-8",
							FileUtils.readLines(new File(filePath), charSet));
				}
				return openSubtitleFileInJNI(tempPath, index, subtiltle_index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return openSubtitleFileInJNI(filePath, index, subtiltle_index);
	}

	public int openSubtitleFile(String filePath, int index) {
		return openSubtitleFile(filePath, index, 0);
	}

	/**
	 * close subtitle file
	 */
	public native void closeSubtitle(int subtiltle_index);

	/**
	 * close subtitle file
	 */
	public void closeSubtitle() {
		closeSubtitle(0);
	}

	/**
	 * 根据时间获取字幕内容
	 * 
	 * @param time
	 * @return
	 */
	public native String getSubtitleByTime(int time, int subtiltle_index);

	/**
	 * 根据时间获取字幕内容
	 * 
	 * @param time
	 * @return
	 */
	public String getSubtitleByTime(int time) {
		return getSubtitleByTime(time, 0);
	}

	public native int getSubtitleType(int subtiltle_index);

	public native String getSubtitleLanguage(String file);

	/**
	 * Get language info of subtitle
	 * 
	 * @param file
	 * @return
	 */
	public String getSubtitleLanguageInfo(String file) {
		return getSubtitleLanguage(file);
	}

	/**
	 * 字幕文件是否存在
	 * 
	 * @param file
	 * @return 字幕个数 只要>0就表示存在
	 */
	public native int isSubtitleExits(String file);

	private static String getFilecharset(File sourceFile) {
		if (!sourceFile.getAbsolutePath().endsWith("srt")) {
			return "UTF-8";
		}
		String charset = "UTF-8";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; 
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; 
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; 
				checked = true;
			}
//			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0) {
						charset = "GBK";
						break;
					}
					if (0x80 <= read && read <= 0xBF) { 
						charset = "GBK";
						break;
					}
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) 
							continue;
						else {
							charset = "GBK";
							break;
						}
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								charset = "GBK";
								break;
							}
						} else {
							charset = "GBK";
							break;
						}
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

}
