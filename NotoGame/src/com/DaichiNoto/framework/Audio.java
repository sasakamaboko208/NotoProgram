package com.DaichiNoto.framework;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Audioクラス
 * ゲームの音に関するクラス
 * @author Daichi Noto 2015 03/07
 *
 */
public class Audio {
	/**
	 * メンバ変数
	 */
	private AssetManager m_assets;			//アセット管理するメンバ変数
	private SoundPool m_soundPool;			//サウンドエフェクトを簡単に再生できるメンバ変数
	
	/**
	 * コンストラクタ
	 * @param activity　アクティビティクラスを取得
	 */
	public Audio(Activity activity){
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);	//音の調整処理
		m_assets = activity.getAssets();
		m_soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	/**
	 * ファイル名からMusicインスタンスを返す関数
	 * @param filename ファイル名取得
	 * @return　Musicインスタンスを返す
	 */
	public Music newMusic(String filename){
		try{
			AssetFileDescriptor assetDescriptor = m_assets.openFd(filename);
			return new Music(assetDescriptor);
		}catch(IOException e){
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}
	
	/**
	 * ファイル名からSoundインスタンスを返す関数
	 * @param filename ファイル名
	 * @return Soundインスタンスを返す
	 */
	public Sound newSound(String filename){
		try{
			AssetFileDescriptor assetDescriptor = m_assets.openFd(filename);
			int soundID = m_soundPool.load(assetDescriptor, 0);
			return new Sound(m_soundPool, soundID);
		}catch(IOException e){
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}
}
