#MoboViceoView
##MoboVideoView����װ����Ƶ������ص�һЩ�ӿڡ�

##�������£�

```java
    public static int decode_mode_hard = 1; //Ӳ����
    public static int decode_mode_mediacodec = 2; //mediacodec���룬����Ӳ������֮�䣬��֧��4.1�����豸
	public static int decode_mode_soft = 3;//�����
```
##�������£�
```java
    public void loadNativeLibs()
    //������Ӧ�Ľ����������Ҫ����һ��

    public void setOnVideoStateChangedListener(OnVideoStateChangedListener listener)
    ����״̬�ı䣬�ص��Ľӿڣ���ʹ����Ƶ�Ϻ�ʱ���������첽ִ�к�ص��˽ӿڣ�

    public void setVideoPath(String path, String params)
    ������Ҫ���ŵ���Ƶ·������������
    Parameters:
    path  ��Ƶ·��
    params  ������Ļ�������index����ʽΪ��audioTrackIndex+��\n��+subtitleIndex���ɲ�ָ��

    public void setVideoPath(String path)
    ͬ��

    public void resetDecodeMode(int decodeMode)
    ���ý��뷽ʽ
    Parameters:
    decodeMode ���뷽ʽ����Ӳ����mediacodec

    public boolean isPlaying()
    ��ǰ�Ƿ񲥷�״̬

    public int getDecodeMode()
    ��ȡ��ǰ���뷽ʽ

    public String getCurrentVideoPath()
    ��ȡ��ǰ������Ƶ·��

    public void start()
    ��ʼ����

    public void pause()
    ��ͣ

    public void stop()
    ֹͣ��������ǰ������Ƶ����Ļ����Ƶ�ľ�ֹͣ

    public void seekTo(int position) 
    ��ת������λ��

    public int getCurrentPosition()
    ��ȡ��ǰ���ŵ���λ�ã���λ������

    public int getDuration()
    ��ȡ��ʱ������λ������

    public int getVideoWidth()
    ��ȡ��ǰ������Ƶ�Ŀ�ȣ���λ������

    public int getVideoHeight()
    ��ȡ��ǰ������Ƶ�ĸ߶ȣ���λ������

    public int changeAudioChannel(int index)
    �л�����

    public String scanMediaFile(String filePath, String savePath, int time,
			int width, int height, int amount)
    ɨ����Ƶ����������ͼ
    Parameters:
        filePath ��Ƶ·��
        savePath ͼƬ����·����������ͼƬ��׺�����Զ�����pngͼƬ��
        time Ҫ���ɽ�ͼ��ʱ��㣬��λ����
        width ͼƬ��ȣ��ݲ������ã�����ͼƬ������Ƶ�Ŀ�ȵ�ͬ��
        height ͼƬ�߶ȣ��ݲ������ã�����ͼƬ������Ƶ�ĸ߶ȵ�ͬ��
        amountҪ���ɵ�ͼƬ����
        ע����ͼ����������ͼ��ͨ��ScreenShotLib������ɣ���߷�װ��ѹ���Ͷ��α���ͼƬ�ķ���

    public void setIsLive(boolean live)
    �����Ƿ�Ϊֱ��������ý��

    public void setBaseHandler(Handler handler)
    ����handler��openSA������������Ļ����Ƶ�ļ�������Ļ�������Իص�����Ӧmessage.what==111

    public String getCurrentExtSubtitleSize(String path, int currentTime,
			int sourceIndex, int streamIndex)
    ��ȡͼ����Ļ�ߴ�
    Parameters:
      path ��Ļ����Ƶ·��
      currentTime ��ǰʱ��
      sourceIndex ���openSA�����еĽ���
      streamIndex ָ����Ļ������Ļ����Ƶ�е�index

    public String getCurrentExtSubtitleInfo(String path, int sourceIndex,
			int streamIndex)
    ��ȡ��ǰ��Ļ��Ϣ
    Parameters:
      path ��Ļ����Ƶ·��
      currentTime ��ǰʱ��
      sourceIndex ���openSA�����еĽ���
      streamIndex ָ����Ļ������Ļ����Ƶ�е�index

    public String getCurrentSubtitle(int currentTime)
    ��ȡ��ǰ��Ļ�����֣�

    public Bitmap getCurrentImageSubtitle(int currentTime)
    ��ȡ��ǰͼ����Ļ

    public void openSA(String SAPath, int type, int sourceIndex, int streamIndex)
    ����һ����Ƶ�����������Ļ�����죩����Ļ�ļ�������ָ������Ļ����������
    Parameters:
      SAPath ��Ļ����Ƶ·��
      type �������ͣ�1�������ļ���ָ�������죻3�������ļ���ָ������Ļ
      sourceIndex �������ֶ�ͬһ���ļ��Ĳ�ͬ�򿪲�������Ƶ�ļ��п���ͬʱ���У������Ļ���Ͷ����Ƶ��������������Ҫ����ά��һ��ȫ�����α�����ÿ��ִ��openSA���ñ������м�һ���뼴�ɡ�
      streamIndex ָ����Ļ������Ļ����Ƶ�е�index
    
    public void closeSA(String SAPath, int type, int sourceIndex,
			int streamIndex)
    �رն�Ӧ����Ļ����Ƶ
      �����������openSA

    public void setBufferedTime(int time)
    ���û���ʱ�䣬��λ�롣

    public int getBufferStatus()
    ����-1 û���ڻ��壻����0-100 ����İٷֱȣ��������õ�BufferTime����

    PlayerActivityBase
    ����������Ļ��࣬��װ��һЩ���������õĳ����������ͷ���
```

##�������£�
```java
	/** Ĭ�ϱ����ʹ�С **/
	static final protected int PLAY_DISPLAY_MODE_NORMAL = 0;
	/** ������������ȫ�� **/
	static final protected int PLAY_DISPLAY_MODE_FULL_SCREEN = 1;
	/** ������Ļ **/
	static final protected int PLAY_DISPLAY_MODE_FILL = 2;
	/** 16��9 **/
	static final protected int PLAY_DISPLAY_MODE_NINE = 3;
	/** 4��3 **/
	static final protected int PLAY_DISPLAY_MODE_FOUR = 4;
	/** �Զ��� **/
	static final protected int PLAY_DISPLAY_MODE_CUSTOM = 5;
	/** ���Ƶ��� **/
	static final protected int PLAY_DISPLAY_MODE_GESTURE = 6;
    
	public static final int state_play = 0;
	public static final int state_pause = 1;
	public static final int state_stop = 2;
```

##�������£�
```java
	protected int player_state = state_stop;//����״̬
	protected int displayMode = PLAY_DISPLAY_MODE_FULL_SCREEN;//���ű���
	private int videoWidth, videoHeight;//��ǰ������Ƶ�Ŀ�͸�
	private boolean isHorizontal = true;//�Ƿ����
	/** ��ǰ���ŵ�ַ **/
	protected String currentVideoPath = null;
	/** ���������view **/
	protected MoboVideoView mMoboVideoView = null;
	/** ��ǰ���Ż���Ŀ�͸� **/
	protected int playerWidth, playerHeight;
	/** ��������״̬ **/
	protected boolean isFloatWindowMode = false;
	/** ��Ƶʱ������λ���� **/
	protected int duration = 0;
	/** ��ǰ����λ�ã���λ���� **/
	protected int currentPosition = 0;
	/** ��ǰ���ŵ���Ƶ���б��е�index **/
	protected int currentVideoIndex = 0;
	/** ������Ҫ���ݵĲ�������ʽΪ�������index\n��Ļ��index \n ���ܻ������� **/
	protected String videoParams = null;
```
##���÷������£�
```java
    protected void showFloatPlayerByHomeKey(boolean enable)
    ���á��Ƿ��ڰ���Home��ʱ��������������

    protected void setFloatPlayerListener(FloatPlayerListener listener)
    �����������ڻص��ӿ�

    protected void setVideoPath(String path, String playParams, int decodeMode)
    ������Ƶ
    Parameters:
        path ��Ƶ·��
        playParams ���������MoboVideoView�еĽ���
        decodeMode ���뷽ʽ

    protected void setSubtitle(String subPath, int streamIndex)
    ������Ļ
    Parameters:
        path ��Ļ����Ƶ�ļ�·��
        streamIndex ָ����Ҫ������Ļ����index

    protected void playAudioOnly(String videoPath, int audioIndex)
    ֻ��������
    Parameters:
        videoPath  ��Ƶ·��
        audioIndex  ������������index

    protected void changeAudioTrack(int index)
    �л�����

    protected String getTextSubtitle(int time)
    ��ȡָ��ʱ�����Ļ�����֣�

    protected Bitmap getImageSubtitle(int time) 
    ��ȡָ��ʱ�����Ļ��ͼ�Σ�

    protected void enableScreenOnSetting()
    ��Ļ����

    protected void releaseScreenOnSetting()
    �ͷ���Ļ����

    protected void setPlayerScale(int displayMode)
    ���ò��Ż���Ĳ��ű���
    protected ArrayList<String> getSubtitleList(String subtitleInfo)
    ����baseHandler�л�õ���Ļ��Ϣ������������Ƶ����Ļ�и�����Ļ������Ϣ

    protected void changeToFloatPlayer()
    ����������

    protected void changeToNormalPlayer()
    �������ڻָ�����ͨ���ڲ���
```

#SubtitleJni
##SubtitleJni ��Ļ��ؽӿ�

##��������
```java
	/**
	 * You cannot use interface of subtitle if you are not call this method.
	 */
	public void loadFFmpegLibs(String libPath,String libName)

	/**
	 * open subtitle file
	 * @param filePath
	 * @param index
	 * @return <0 then failed
	 */
    public native int  openSubtitleFileInJNI(String filePath,int index);

    /**
     * close subtitle file
     */
    public native void closeSubtitle();
    
    /**
     * ����ʱ���ȡ��Ļ����
     * @param time
     * @return
     */
    public native String  getSubtitleByTime(int time);
    
    
    /**
     * ��Ļ�ļ��Ƿ����
     * @param file
     * @return ��Ļ���� ֻҪ>0�ͱ�ʾ����
     */
    public native int isSubtitleExits(String file);
```




