package com.DaichiNoto.framework.Input;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;

import com.DaichiNoto.framework.Input.Input.TouchEvent;
import com.DaichiNoto.framework.Input.Pool.PoolObjectFactory;

/**
 * SingleTouchHandlerクラス
 * シングルタッチクラス
 * @author Daichi Noto 2015 03/10
 *
 */
public class SingleTouchHandler implements TouchHandler{

	/**
	 * メンバ変数
	 */
	private boolean m_isTouched;		//状態変数
	private int m_touchX;				//タッチされたX座標
	private int m_touchY;				//タッチされたY座標
	private Pool<TouchEvent> m_touchEventPool;									//作成されたインスタンスを再利用するための変数
	private ArrayList<TouchEvent> m_touchEvents = new ArrayList<TouchEvent>();			//初めて作成されたタッチイベントインスタンスを格納するための変数
	
	private ArrayList<TouchEvent> m_touchEventsBuffer = new ArrayList<TouchEvent>();
	private float m_scaleX;
	private float m_scaleY;
	
	/**
	 * コンストラクタ
	 * @param view
	 * @param scaleX
	 * @param scaleY
	 */
	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		m_touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);

		m_scaleX = scaleX;
		m_scaleY = scaleY;
	}
	
	/**
	 * タッチ処理
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized(this) {
			TouchEvent touchEvent = m_touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.setType(TouchEvent.TOUCH_DOWN);
				m_isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.setType(TouchEvent.TOUCH_DRAGGED);
				m_isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.setType(TouchEvent.TOUCH_UP);
				m_isTouched = false;
				break;
			}
			
			touchEvent.setX(m_touchX = (int)(event.getX() * m_scaleX));
			touchEvent.setY(m_touchY = (int)(event.getY() * m_scaleY));
			m_touchEventsBuffer.add(touchEvent);
			return true;
		}
	}

	/**
	 * 押された時の処理
	 */
	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this) {
			if(pointer == 0)
				return m_isTouched;
			else
				return false;
		}
	}
	/**
	 * タッチX座標の取得
	 */
	@Override
	public int getTouchX(int pointer) {
		synchronized(this) {
			return m_touchX;
		}
	}

	/**
	 * タッチY座標の取得
	 */
	@Override
	public int getTouchY(int pointer) {
		synchronized(this) {
			return m_touchY;
		}
	}

	/**
	 * タッチイベントの取得
	 */
	@Override
	public ArrayList<TouchEvent> getTouchEvents() {
		synchronized(this) {
			int len = m_touchEvents.size();
			for( int i = 0; i < len; i++ ){
				m_touchEventPool.free(m_touchEvents.get(i));
			}
			m_touchEvents.clear();
			m_touchEvents.addAll(m_touchEventsBuffer);
			m_touchEventsBuffer.clear();
			return m_touchEvents;
		}
	}

}
