package com.DaichiNoto.framework;

import android.media.SoundPool;

/**
 * Soundクラス
 * ゲームの効果音を管理するクラス
 * @author Daichi Noto 2015 03/08
 *
 */
public class Sound {
	//メンバ変数
	private int m_soundID;	//効果音のIDを取得するメンバ変数
	private SoundPool m_soundPool;  //効果音を処理するメンバ変数
	
	/**
	 *　コンストラクタ
	 * @param soundPool	効果音のインスタンスを取得
	 * @param soundID	効果音のIDを取得
	 */
	public Sound(SoundPool soundPool, int soundID){
		m_soundPool = soundPool;
		m_soundID = soundID;
	}
	
	/**
	 * 効果音再生メソッド
	 * @param volum	効果音の再生関数
	 */
	public void play(float volum){
		m_soundPool.play(m_soundID, volum, volum, 0, 0, 1);
	}
	
	/**
	 * 効果音を削除
	 */
	public void dispose() {
		m_soundPool.unload(m_soundID);
	}
}
