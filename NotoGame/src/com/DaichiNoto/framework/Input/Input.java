package com.DaichiNoto.framework.Input;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

/**
 * Inputクラス
 * @author Daichi Noto 2015 03/09
 *
 */
public class Input {
	/**
	 * メンバ変数
	 */
	AccelerometerHandler m_accelHandler; //加速度ハンドラ
	KeyboardHandler m_keyHandler;		 //キーボードハンドラ
	TouchHandler m_touchHandler;		 //タッチハンドラ
	
	/**
	 * コンストラクタ
	 * @param context
	 * @param view
	 * @param scaleX
	 * @param scaleY
	 */
	public Input(Context context, View view, float scaleX, float scaleY){
		m_accelHandler = new AccelerometerHandler(context);
		m_keyHandler = new KeyboardHandler(view);
		
		//バージョンによって処理を変える
		if(VERSION.SDK_INT < 5){
			//一応この処理も書いておきます
			m_touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		}else{
			m_touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
		}
	}
	
	/**
	 * 指定のキーコードの状態を取得
	 * @param keyCode
	 * @return
	 */
	public boolean isKeyPressed(int keyCode){
		return m_keyHandler.isKeyPressed(keyCode);
	}
	
	/**
	 * 指定のタッチポインターの状態を取得
	 * @param pointer
	 * @return
	 */
	public boolean isTouchDown(int pointer){
		return m_touchHandler.isTouchDown(pointer);
	}
	
	/**
	 * 指定のタッチポインターのX座標を取得
	 * @param pointer
	 * @return
	 */
	public int getTouchX(int pointer){
		return m_touchHandler.getTouchX(pointer);
	}
	
	/**
	 * 指定のタッチポインターのY座標を取得
	 * @param pointer
	 * @return
	 */
	public int getTouchY(int pointer) {
		return m_touchHandler.getTouchY(pointer);
	}
	
	/**
	 * 加速度のX軸の取得
	 * @return
	 */
	public float getAccelX() {
	return m_accelHandler.getAccelX();
	}

	/**
	 * 加速度のY軸の取得
	 * @return
	 */
	public float getAccelY() {
		return m_accelHandler.getAccelY();
	}
	
	/**
	 * 加速度のZ軸の取得
	 * @return
	 */
	public float getAccelZ() {
	return m_accelHandler.getAccelZ();
	}

	/**
	 * タッチイベントの取得
	 * @return
	 */
	public List<TouchEvent> getTouchEvents() {
		return m_touchHandler.getTouchEvents();
	}

	/**
	 * キーイベントの取得
	 * @return
	 */
	public List<KeyInfoEvent> getKeyEvents() {
		return m_keyHandler.getKeyEvents();
	}		
	
	
	
	
//------------------------------------------------
	
	/**
	 * キー処理用のクラス
	 * @author Daichi Noto 2015 03/10
	 */
	public static class KeyInfoEvent{
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;

        private int m_type;
        private int m_keyCode;
        private char m_keyChar;
        
        /**
         * タイプの取得
         * @return type
         */
        public int getType(){
        	return m_type;
        }
        
        /**
         * タイプの設定
         * @param type　取得したタイプ
         */
        public void setType(int type){
        	m_type = type;
        }
        
        /**
         * 押されたキーコードを取得
         * @return　キーコード
         */
        public int getKeyCode(){
        	return m_keyCode;
        }
        
        /**
         * キーコードの設定
         * @param 取得したキーコード
         */
        public void setKeyCode(int keyCode){
        	m_keyCode = keyCode;
        }
        
        /**
         * 押されたキーの文字を取得
         * @return　
         */
        public int getKeyChar(){
        	return m_keyChar;
        }
        
        /**
         * キー文字の設定
         * @param keyChar 取得した文字
         */
        public void setKeyChar(char keyChar){
        	m_keyChar = keyChar;
        }
	}
	
	
//------------------------------------------------------
	/**
	 * タッチ処理用のクラス
	 * @author Daichi Noto 2015 03/10
	 *
	 */
	public static class TouchEvent{
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        private int m_type;
        private int m_x;
        private int m_y;
        private int m_pointer;
        
        /**
         * タイプの取得
         * @return type
         */
        public int getType(){
        	return m_type;
        }
        
        /**
         * タイプの設定
         * @param type　取得したタイプ
         */
        public void setType(int type){
        	m_type = type;
        }
        
        /**
         * ビュー座標系のX座標を取得
         * @return　X座標
         */
        public int getX(){
        	return m_x;
        }
        
        /**
         * ビュー座標系のX座標の設定
         * @param X座標
         */
        public void setX(int x){
        	m_x = x;
        }
        
        /**
         * ビュー座標系のY座標を取得
         * @return　Y座標
         */
        public int getY(){
        	return m_y;
        }
        
        /**
         * ビュー座標系のY座標の設定
         * @param Y座標
         */
        public void setY(int y){
        	m_y = y;
        }
        
        /**
         * ポインタIDを取得
         * @return　
         */
        public int getPointer(){
        	return m_pointer;
        }
        
        /**
         * ポインタIDの設定
         * @param keyChar 取得した文字
         */
        public void setPointer(int pointer){
        	m_pointer = pointer;
        }
	}
}
