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
import java.io.InputStream;

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
	 * @param filePath
	 * @param index
	 * @return <0 then failed
	 */
    public native int  openSubtitleFileInJNI(String filePath,int index, int subtiltle_index);

    public int openSubtitleFile(String filePath,int index, int subtiltle_index) {
    	if(!isUtf8Encode(filePath)) {
    		try {
        		String tempPath = filePath.substring(0, filePath.length()-4)+"mobo_temp_utf-8.srt";
        		File tempFile = new File(tempPath);
        		if(!tempFile.exists()) {
        			FileUtils.writeLines(tempFile, "UTF-8", FileUtils.readLines(new File(filePath), "GBK"));
            	}
				return openSubtitleFileInJNI(tempPath, index, subtiltle_index);
			} catch (Exception e) {
				e.printStackTrace();
			}        
    	}
    	return openSubtitleFileInJNI(filePath, index, subtiltle_index);
    }
    
    public int openSubtitleFile(String filePath,int index) {
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
     * @param time
     * @return
     */
    public native String  getSubtitleByTime(int time, int subtiltle_index);
    
    /**
     * 根据时间获取字幕内容
     * @param time
     * @return
     */
    public String  getSubtitleByTime(int time) {
    	return getSubtitleByTime(time, 0);
    }
    
    /**
     * 字幕文件是否存在
     * @param file
     * @return 字幕个数 只要>0就表示存在
     */
    public native int isSubtitleExits(String file);
    
    /**
     * file is utf-8?
     * @param filePath
     * @return
     */
    private boolean isUtf8Encode(String filePath) {
		if(!filePath.endsWith("srt")) {
			return true;
		}
		InputStream in = null;
		try {
			File file = new File(filePath);  
			in= new java.io.FileInputStream(file);  
			byte[] b = new byte[3];  
			in.read(b);  
			in.close();  
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)  
			    return true;  
			else  
			    return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
