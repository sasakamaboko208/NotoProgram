package com.DaichiNoto.framework.Input;

import java.util.ArrayList;

import android.view.View.OnTouchListener;

import com.DaichiNoto.framework.Input.Input.TouchEvent;

/**
 * TouchHandlerクラス
 * 共通のタッチ処理のインターフェイス
 * @author Daichi Noto 2015 03/10
 *
 */
public interface TouchHandler extends OnTouchListener{
	
	/**
	 * タッチしたときの処理
	 * @param pointer タッチスクリーンドライバが割り当てたIDを取得
	 * @return
	 */
	public boolean isTouchDown(int pointer);
	
	/**
	 * XとYの座標を取得
	 * @param pointer　タッチスクリーンドライバが割り当てたIDを取得
	 * @return
	 */
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	
	/**
	 * タッチイベントのインスタンスを取得
	 * @return
	 */
	public ArrayList<TouchEvent> getTouchEvents();
}
