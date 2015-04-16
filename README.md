#MoboVideoView
##MoboVideoView，封装了视频播放相关的一些接口。

##常量如下：

```java
    public static int decode_mode_hard = 1; //硬解码
    public static int decode_mode_mediacodec = 2; //mediacodec解码，介于硬解和软解之间，仅支持4.1以上设备
	public static int decode_mode_soft = 3;//软解码
```
##方法如下：
```java
    public void loadNativeLibs()
    //加载相应的解码包，至少要调用一次

    public void setOnVideoStateChangedListener(OnVideoStateChangedListener listener)
    播放状态改变，回调的接口（初使化视频较耗时，故需在异步执行后回调此接口）

    public void setVideoPath(String path, String params)
    设置需要播放的视频路径及其他参数
    Parameters:
    path  视频路径
    params  内置字幕和音轨的index，格式为：audioTrackIndex+”\n”+subtitleIndex，可不指定

    public void setVideoPath(String path)
    同上

    public void resetDecodeMode(int decodeMode)
    重置解码方式
    Parameters:
    decodeMode 解码方式：软、硬、或mediacodec

    public boolean isPlaying()
    当前是否播放状态

    public int getDecodeMode()
    获取当前解码方式

    public String getCurrentVideoPath()
    获取当前播放视频路径

    public void start()
    开始播放

    public void pause()
    暂停

    public void stop()
    停止：包括当前播放视频、字幕、音频的均停止

    public void seekTo(int position) 
    跳转到，单位秒

    public int getCurrentPosition()
    获取当前播放到的位置，单位：毫秒

    public int getDuration()
    获取总时长，单位：毫秒

    public int getVideoWidth()
    获取当前播放视频的宽度，单位：像素

    public int getVideoHeight()
    获取当前播放视频的高度，单位：像素

    public int changeAudioChannel(int index)
    切换音轨

    public String scanMediaFile(String filePath, String savePath, int time,
			int width, int height, int amount)
    扫描视频并产生缩略图
    Parameters:
        filePath 视频路径
        savePath 图片保存路径（不包含图片后缀名，自动生成png图片）
        time 要生成截图的时间点，单位毫秒
        width 图片宽度（暂不起作用，生成图片宽与视频的宽度等同）
        height 图片高度（暂不起作用，生成图片高与视频的高度等同）
        amount要生成的图片张数
        注：截图或生成缩略图可通过ScreenShotLib类来完成，里边封装了压缩和二次保存图片的方法

    public void setBaseHandler(Handler handler)
    设置handler，openSA方法解析出字幕或视频文件内置字幕流后用以回调，相应message.what==111

    public String getCurrentExtSubtitleSize(String path, int currentTime,
			int sourceIndex, int streamIndex)
    获取图形字幕尺寸
    Parameters:
      path 字幕或视频路径
      currentTime 当前时间
      sourceIndex 详见openSA方法中的解释
      streamIndex 指定字幕流在字幕或视频中的index

    public String getCurrentExtSubtitleInfo(String path, int sourceIndex,
			int streamIndex)
    获取当前字幕信息
    Parameters:
      path 字幕或视频路径
      currentTime 当前时间
      sourceIndex 详见openSA方法中的解释
      streamIndex 指定字幕流在字幕或视频中的index

    public String getCurrentSubtitle(int currentTime)
    获取当前字幕（文字）

    public Bitmap getCurrentImageSubtitle(int currentTime)
    获取当前图形字幕

    public void openSA(String SAPath, int type, int sourceIndex, int streamIndex)
    解析一个视频（包含多个字幕或音轨）或字幕文件，并打开指定的字幕流或音轨流
    Parameters:
      SAPath 字幕或视频路径
      type 解析类型，1：解析文件中指定的音轨；3：解析文件中指定的字幕
      sourceIndex 用以区分对同一个文件的不同打开操作（视频文件中可能同时含有：多个字幕流和多个音频流），开发者需要自行维护一个全局整形变量，每次执行openSA，该变量自行加一后传入即可。
      streamIndex 指定字幕流在字幕或视频中的index
    
    public void closeSA(String SAPath, int type, int sourceIndex,
			int streamIndex)
    关闭对应的字幕或音频
      参数解析详见openSA

    public void setIsLive(boolean live)
    设置是否为直播类型流媒体

    public void setBufferedTime(int time)
    设置缓冲时间，单位秒。

    public int getBufferedTime()
    获取当前缓冲到的时间。单位：秒

    public void setBufferListener(BufferListener listener)
    设置缓冲回调接口

    public void stopBuffering()
    停止缓冲(下载)

    public void deleteDownloadTask(boolean deleteFile)
    删除待播放流媒体地址对应的下载信息
      deleteFile 是否删除下载的视频文件
```

##PlayerActivityBase
    播放器界面的基类，封装了一些播放器常用的常量、变量和方法
```

##常量如下：
```java
	/** 默认比例和大小 **/
	static final protected int PLAY_DISPLAY_MODE_NORMAL = 0;
	/** 按比例缩放至全屏 **/
	static final protected int PLAY_DISPLAY_MODE_FULL_SCREEN = 1;
	/** 铺满屏幕 **/
	static final protected int PLAY_DISPLAY_MODE_FILL = 2;
	/** 16：9 **/
	static final protected int PLAY_DISPLAY_MODE_NINE = 3;
	/** 4：3 **/
	static final protected int PLAY_DISPLAY_MODE_FOUR = 4;
	/** 自定义 **/
	static final protected int PLAY_DISPLAY_MODE_CUSTOM = 5;
	/** 手势调节 **/
	static final protected int PLAY_DISPLAY_MODE_GESTURE = 6;
    
	public static final int state_play = 0;
	public static final int state_pause = 1;
	public static final int state_stop = 2;
```

##变量如下：
```java
	protected int player_state = state_stop;//播放状态
	protected int displayMode = PLAY_DISPLAY_MODE_FULL_SCREEN;//播放比例
	private int videoWidth, videoHeight;//当前播放视频的宽和高
	private boolean isHorizontal = true;//是否横屏
	/** 当前播放地址 **/
	protected String currentVideoPath = null;
	/** 播放组件的view **/
	protected MoboVideoView mMoboVideoView = null;
	/** 当前播放画面的宽和高 **/
	protected int playerWidth, playerHeight;
	/** 浮动窗口状态 **/
	protected boolean isFloatWindowMode = false;
	/** 视频时长，单位：秒 **/
	protected int duration = 0;
	/** 当前播放位置，单位：秒 **/
	protected int currentPosition = 0;
	/** 当前播放的视频在列表中的index **/
	protected int currentVideoIndex = 0;
	/** 播放需要传递的参数，格式为：音轨的index\n字幕的index \n 性能或画质优先 **/
	protected String videoParams = null;
```
##常用方法如下：
```java
    protected void showFloatPlayerByHomeKey(boolean enable)
    设置“是否在按下Home键时”弹出悬浮窗口

    protected void setFloatPlayerListener(FloatPlayerListener listener)
    设置悬浮窗口回调接口

    protected void setVideoPath(String path, String playParams, int decodeMode)
    播放视频
    Parameters:
        path 视频路径
        playParams 参数，详见MoboVideoView中的解释
        decodeMode 解码方式

    protected void setSubtitle(String subPath, int streamIndex)
    解析字幕
    Parameters:
        path 字幕或视频文件路径
        streamIndex 指定需要解析字幕流的index

    protected void playAudioOnly(String videoPath, int audioIndex)
    只播放声音
    Parameters:
        videoPath  视频路径
        audioIndex  待播放声音的index

    protected void changeAudioTrack(int index)
    切换音轨

    protected String getTextSubtitle(int time)
    获取指定时间的字幕（文字）

    protected Bitmap getImageSubtitle(int time) 
    获取指定时间的字幕（图形）

    protected void enableScreenOnSetting()
    屏幕常亮

    protected void releaseScreenOnSetting()
    释放屏幕常亮

    protected void setPlayerScale(int displayMode)
    设置播放画面的播放比例
    protected ArrayList<String> getSubtitleList(String subtitleInfo)
    根据baseHandler中获得的字幕信息，解析出该视频或字幕中各个字幕流的信息

    protected void changeToFloatPlayer()
    打开悬浮窗口

    protected void changeToNormalPlayer()
    悬浮窗口恢复至普通窗口播放
```

#SubtitleJni
##SubtitleJni 字幕相关接口

##方法如下
```java
	/**
	 * You cannot use interface of subtitle if you are not call this method.
	 */
	public void loadFFmpegLibs(String libPath,String libName)

	/**
	 * 预解码方式：一次性解码完所有字幕。优点：取字幕时效率高；缺点：打开字幕时等待时间可能较长（字幕越多时间越长）
	 * open subtitle file
	 * @param filePath
	 * @param index , subtiltle_index : The number of subtitle.
	 * @return <0 then failed
	 */
    public int openSubtitleFile(String filePath,int index, int subtiltle_index);
    
    /**
	 * 预解码方式：一次性解码完所有字幕。优点：取字幕时效率高；缺点：打开字幕时等待时间可能较长（字幕越多时间越长）
	 * @param filePath
	 * @param index
	 * @return
	 */
	public int openSubtitleFile(String filePath, int index);
    

	/**
	 * 按需解码方式：到相应的时间点时再解码相应的字幕。优点：打开较快；缺点：获取字幕效率略低
	 * @param filePath
	 * @param index
	 * subtiltle_index : The number of subtitle.
	 * @return
	 */
	public int openSubtitleFile_2(String filePath, int index,
			int subtiltle_index);

	/**
	 * 按需解码方式：到相应的时间点时再解码相应的字幕。优点：打开较快；缺点：获取字幕效率略低
	 * @param filePath
	 * @param index
	 * @return
	 */
	public int openSubtitleFile_2(String filePath, int index) ;

    /**
     * close subtitle file
     */
    public native void closeSubtitle();
    
    /**
	 * close subtitle file
	 */
	public native void closeSubtitle(int subtiltle_index);
    
    /**
	 * close subtitle file
	 */
	public void closeSubtitle2();
    
	/**
	 * close subtitle file
	 */
	public void closeSubtitle2(int subtiltle_index);
    
    /**
     * 根据时间获取字幕内容
     * @param time
     * @return
     */
    public native String  getSubtitleByTime(int time);
    
    /**
	 * 根据时间获取字幕内容
	 * 
	 * @param time
	 * 单位：毫秒
	 * @return
	 */
	public native String getSubtitleByTime(int time, int subtiltle_index);
    
	/**
	 * 根据时间获取字幕内容
	 * 
	 * @param time
	 * @return
	 */
	public String getSubtitleByTime_2(int time)    
    
    /**
	 * 根据时间获取字幕内容
	 * @param time
	 * 单位：毫秒
	 * @param subtiltle_index
	 * @param time_diff
	 * @return
	 */
	public native String getSubtitleByTime2(int time, int subtiltle_index, int time_diff);
    
  
    /**
     * 字幕文件是否存在
     * @param file
     * @return 字幕个数 只要>0就表示存在
     */
    public native int isSubtitleExits(String file);
```

#ScreenShotLibJni
##ScreenShotLibJni 截图相关接口

##方法如下
```java
        public static ScreenShotLibJni getInstance()
          获取ScreenShotLibJni的实例

        public Bitmap getIDRFrameThumbnail(String videoPath,
			String thumbnailSavePath, int width, int height) 
        获取首个IDR帧的截图
            videoPath            视频路径
            thumbnailSavePath    截图保存路径
            width                截图的宽
            height               截图的高

        public Bitmap getScreenShot(String videoPath, String thumbnailSavePath,
			int position, int width, int height) 
        获取截图
            videoPath            视频路径
            thumbnailSavePath    截图保存路径
            position             截图的时间、单位秒
            width                截图的宽
            height               截图的高
```


