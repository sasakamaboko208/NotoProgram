package com.DaichiNoto.framework;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

import com.DaichiNoto.Main.GameMain;

public abstract class SceneBase {
	protected final GLSurfaceView m_glView;
	protected final GL10 m_gl;
	protected final GameMain m_glMain;
	
	/**
	 * コンストラクタ
	 * @param game
	 */
	public SceneBase(GameMain game){
		m_glView = game.getGlSurfaceView();
		m_gl = game.getGL10();
		m_glMain = game;
	}
	
	/**
	 * 更新処理
	 * @param deltaTime
	 */
	public abstract void Update(float deltaTime);
	
	/**
	 * 描画処理
	 * @param deltaTime
	 */
	public abstract void Draw(float deltaTime);
	
	/**
	 * 一時停止処理
	 */
    public abstract void pause();
    
    /**
     * ゲームの再開処理
     */
    public abstract void resume();
    
    /**
     * システムリソースの解放
     */
    public abstract void dispose();
}
