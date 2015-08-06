package com.DaichiNoto.framework;
import java.io.IOException;



import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
/**
 * Musicクラス
 * ゲームの音楽（BGM）を管理するクラス
 * @author Daichi Noto 2015 03/08
 *
 */
public class Music implements OnCompletionListener{
	
	/**
	 * メンバ変数
	 */
	private MediaPlayer m_mediaPlayer;		//音を管理するメンバ変数
	private boolean m_isPrepared = false;	//現在の状態（同期）の管理するメンバ変数
	
	/**
	 * コンストラクタ
	 * @param assetDescriptor
	 */
	public Music(AssetFileDescriptor assetDescriptor) {
		m_mediaPlayer = new MediaPlayer();
		try {
			
			//データ読み取り
			m_mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), 
										assetDescriptor.getStartOffset(), 
										assetDescriptor.getLength());
			//同期
			m_mediaPlayer.prepare();
			
			//状態をtrueに変更
			m_isPrepared = true;
			
			//BGM登録
			m_mediaPlayer.setOnCompletionListener(this);
			
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	/**
	 * 終了する際に呼ばれる関数
	 */
	public void dispose(){
		//BGMが流れていたら止めます
		if(m_mediaPlayer.isPlaying())
		{
			m_mediaPlayer.stop();
		}
		
		m_mediaPlayer.release();	//解放
	}
	
	/**
	 * 一時停止状態にする関数
	 */
	public void pause(){
		//BGMが流れている場合止める
		if(m_mediaPlayer.isPlaying()){
			m_mediaPlayer.pause();
		}
	}
	
	/**
	 * BGMがループするかどうかの状態を返す関数
	 * @return ループ中かどうかを返す
	 */
	public boolean isLooping(){
		return m_mediaPlayer.isLooping();
	}
	
	/**
	 * BGMの再生状態を返す関数
	 * @return 再生状態を返す
	 */
	public boolean isPlaying(){
		return m_mediaPlayer.isPlaying();
	}
	
	/**
	 * 現在の再生状態を停止する関数
	 * @return	停止状態フラグを返す
	 */
	public boolean isStopped(){
		return !m_isPrepared;
	}
	
	/**
	 * BGMの再生
	 */
	public void play(){
		//すでに再生中なら抜ける
		if(m_mediaPlayer.isPlaying()){
			return;
		}
		
		try{
			synchronized (this) {
				//再生の準備を整える必要があるかないか
				if(!m_isPrepared){
					m_mediaPlayer.prepare();
				}
				m_mediaPlayer.start();
			}
		}catch(IllegalStateException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ループの状態を設定関数
	 * @param isLooping ループ状態フラグ
	 */
	public void setLooping(boolean isLooping) {
		m_mediaPlayer.setLooping(isLooping);
	}
	
	/**
	 * ボリュームの設定関数
	 * @param volume ボリューム
	 */
	public void setVolume(float volume) {
		m_mediaPlayer.setVolume(volume, volume);
	}
	
	/**
	 * BGMを停止する関数
	 */
	public void stop() {
		m_mediaPlayer.stop();
		synchronized (this) {
			m_isPrepared = false;
		}
	}
	
	/**
	 * 終了時の処理関数
	 */
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		synchronized (this) {
			m_isPrepared = false;
		}
	}

}
