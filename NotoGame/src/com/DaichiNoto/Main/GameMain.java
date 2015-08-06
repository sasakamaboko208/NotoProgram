package com.DaichiNoto.Main;

/**
 * GameMainクラス
 * @author Daichi Noto 2015 03/07
 *
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.DaichiNoto.framework.Audio;
import com.DaichiNoto.framework.FileIO;
import com.DaichiNoto.framework.SceneBase;
import com.DaichiNoto.framework.Input.Input;
/*
 * ゲームメインクラス
 */
public class GameMain extends Activity implements Renderer{
	
	/**
	 * ゲーム状態
	 */
	enum GameState{
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	/**
	 * メンバ変数
	 */
	private GLSurfaceView m_glView; //OpenGLのサーフェスビュー変数
	private GL10 		  m_gl;		//OpenGLのAPIにアクセスするためのメンバ変数
	
	private FileIO m_fileIO;			//ファイルの入出力
	private Audio m_audio;				//音の変数
	private Input m_input;				//入力処理
	
	private long m_startTime = System.nanoTime();
	private WakeLock m_wakeLock;
	
	private SceneBase m_scene;
	private GameState m_state = GameState.Initialized;
	private final Object m_lockObj = new Object();				//ロックオブジェクト
	private  boolean m_firstTimeCreate = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//タイトルバーを非表示にする
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		m_glView = new GLSurfaceView(this);
		m_glView.setRenderer(this);
		setContentView(m_glView);
		
		//インスタンス化
		m_fileIO = new FileIO(getAssets());
		m_audio = new Audio(this);
		m_input = new Input(this, m_glView, 1, 1);
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		m_wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	/**
	 * 初期処理
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		m_gl = gl;
		synchronized (m_lockObj) {
			if(m_state == GameState.Initialized){
				m_scene = new MainMenuScene(this);
			}
			m_state = GameState.Running;
			m_scene.resume();
			m_startTime = System.nanoTime();
		}
		
		if(m_firstTimeCreate){
			Settings.Load(getFileIO());
			Assets.Load(this);
			m_firstTimeCreate = false;
		}else{
			Assets.Reload();
		}
	}
	
	/**
	 * 描画関係の処理
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		GameState state = null;
		synchronized (m_lockObj) {
			state = m_state;
		}
		
		if(state == GameState.Running) {
			float deltaTime = (System.nanoTime()-m_startTime) / 1000000000.0f;
			m_startTime = System.nanoTime();

			//更新処理と描画処理
			m_scene.Update(deltaTime);
			m_scene.Draw(deltaTime);
		}

		if(state == GameState.Paused) {
			m_scene.pause();
			synchronized(m_lockObj) {
				m_state = GameState.Idle;
				m_lockObj.notifyAll();
			}
		}

		if(state == GameState.Finished) {
			m_scene.pause();
			m_scene.dispose();
			synchronized(m_lockObj) {
				m_state = GameState.Idle;
				m_lockObj.notifyAll();
			}
		}	
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}
	
	/**
	 * 一時停止する関数
	 */
	@Override
	public void onPause() {
		synchronized (m_lockObj) {
			if(isFinishing()){
				m_state = GameState.Finished;
			}else{
				m_state = GameState.Paused;
			}
			
			while(true){
				try {
					m_lockObj.wait();
					break;
				} catch (InterruptedException e) {
				}
			}
		}
		
		m_wakeLock.release();
		m_glView.onPause();
		super.onPause();
		
		if(Settings.m_soundEnabled){
			Assets.m_music.pause();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		m_glView.onResume();
		m_wakeLock.acquire();
	}
	
	/**
	 * ファイル変数を渡す関数
	 * @return　ファイルクラスのインスタンス
	 */
	public FileIO getFileIO(){
		return m_fileIO;
	}
	
	/**
	 * 音のインスタンスを返す
	 * @return 音クラスのインスタンス
	 */
	public Audio getAudio(){
		return m_audio;
	}
	
	/**
	 * 入力インスタンスを返す
	 * @return　入力インスタンス
	 */
	public Input getInput(){
		return m_input;
	}
	
	/**
	 * GLサーフェスインスタンスを返す
	 * @return
	 */
	public GLSurfaceView getGlSurfaceView(){
		return m_glView;
	}
	
	/**
	 * GL10インスタンスを返す
	 * @return
	 */
	public GL10 getGL10(){
		return m_gl;
	}
	
	/**
	 * 現在のシーンを変更する処理
	 * @param scene
	 */
	public void setScene(SceneBase scene){
		if(scene == null){
			throw new IllegalArgumentException("Screen must not be null");
		}
		m_scene.pause();
		m_scene.dispose();
		scene.resume();
		scene.Update(0);
		m_scene = scene;
	}
}

