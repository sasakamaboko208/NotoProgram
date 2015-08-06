package com.DaichiNoto.framework.Input;

import java.util.ArrayList;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.DaichiNoto.framework.Input.Input.KeyInfoEvent;
import com.DaichiNoto.framework.Input.Pool.PoolObjectFactory;
/**
 * KeyboardHandlerクラス
 * キーボードの処理クラス
 * @author Daichi Noto 2015 03/09
 *
 */
public class KeyboardHandler implements OnKeyListener{
	
	/**
	 * メンバ変数
	 */
	private boolean[] m_pressedKeys = new boolean[128];
	private Pool<KeyInfoEvent> m_keyEventPool;												//作成したインスタンスを再利用するための変数
	private ArrayList<KeyInfoEvent> m_keyEventsBuffer = new ArrayList<KeyInfoEvent>();		//使われてない管理する変数
	private ArrayList<KeyInfoEvent> m_keyEvents = new ArrayList<KeyInfoEvent>();
	
	/**
	 * コンストラクタ
	 * @param view
	 */
	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyInfoEvent> factory = new PoolObjectFactory<KeyInfoEvent>(){

			@Override
			public KeyInfoEvent createObject() {
				return new KeyInfoEvent();
			}
		};
		
		m_keyEventPool = new Pool<KeyInfoEvent>(factory, 100);
		
		//キーイベントの設定
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}
	
	/**
	 * Keyが押された時の処理
	 */
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE){
			return false;
		}
		
		synchronized (this) {
			KeyInfoEvent keyEvent = m_keyEventPool.newObject();
			keyEvent.setKeyCode(keyCode);
			keyEvent.setKeyChar((char) event.getUnicodeChar());
			
			//キーが押された時の処理
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.setType(KeyInfoEvent.KEY_DOWN);
				if(keyCode > 0 && keyCode < 127)
					m_pressedKeys[keyCode] = true;
			}
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				keyEvent.setType(KeyInfoEvent.KEY_UP);
				if(keyCode > 0 && keyCode < 127)
					m_pressedKeys[keyCode] = false;
			}
			m_keyEventsBuffer.add(keyEvent);
		}
		return false;
	}
	
	/**
	 * 指定されたキーコードの状態を返す処理
	 * @param keyCode　キーコード
	 * @return　
	 */
	public boolean isKeyPressed(int keyCode){
		if(keyCode < 0 || keyCode > 127){
			return false;
		}
		return m_pressedKeys[keyCode];
	}
	
	/**
	 * 押されたキーのインスタンスを返す関数
	 * @return
	 */
	public ArrayList<KeyInfoEvent> getKeyEvents(){
		synchronized (this) {
			int len = m_keyEvents.size();
			for(int i = 0; i < len; i++){
				m_keyEventPool.free(m_keyEvents.get(i));
			}
			m_keyEvents.clear();
			m_keyEvents.addAll(m_keyEventsBuffer);
			m_keyEventsBuffer.clear();
			return m_keyEvents;
		}
	}
}
