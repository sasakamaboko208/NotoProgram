package com.DaichiNoto.framework.Input;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;

import com.DaichiNoto.framework.Input.Input.TouchEvent;
import com.DaichiNoto.framework.Input.Pool.PoolObjectFactory;

/**
 * MultiTouchHandlerクラス
 * マルチタッチクラス
 * @author Daichi Noto 2015 03/10
 *
 */
public class MultiTouchHandler implements TouchHandler{

	/**
	 * メンバ変数
	 */
	private static final int TOUCH_MAX = 20;					//最大タッチ状態管理数
	private boolean[] m_isTouched = new boolean[TOUCH_MAX];
	private int[] m_touchX = new int[TOUCH_MAX];
	private int[] m_touchY = new int[TOUCH_MAX];
	
	private Pool<TouchEvent> m_touchEventPool;
	private ArrayList<TouchEvent> m_touchEvents = new ArrayList<TouchEvent>();
	private ArrayList<TouchEvent> m_touchEventsBuffer = new ArrayList<TouchEvent>();
	private float m_scaleX;
	private float m_scaleY;
	
	/**
	 * 
	 * @param view
	 * @param scaleX
	 * @param scaleY
	 */
	public MultiTouchHandler(View view, float scaleX, float scaleY) {
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
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			//押されたボタンの種類を取得(マスクビットは必要ないかも)
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			
			//ポインターインデックスの取得
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
				>> MotionEvent.ACTION_POINTER_ID_SHIFT;
				
			//ポインターIDを取得
			int pointerId = event.getPointerId(pointerIndex);
			TouchEvent touchEvent;

			//各アクションの処理
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchEvent = m_touchEventPool.newObject();
				touchEvent.setType(TouchEvent.TOUCH_DOWN);
				touchEvent.setPointer(pointerId);
				
				m_touchX[pointerId] = (int) (event.getX(pointerIndex) * m_scaleX);
				touchEvent.setX(m_touchX[pointerId]);
				
				m_touchY[pointerId] = (int) (event.getY(pointerIndex) * m_scaleY);
				touchEvent.setY(m_touchY[pointerId]);
				m_isTouched[pointerId] = true;
				m_touchEventsBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = m_touchEventPool.newObject();	
				touchEvent.setType(TouchEvent.TOUCH_UP);
				touchEvent.setPointer(pointerId);
				
				m_touchX[pointerId] = (int) (event.getX(pointerIndex) * m_scaleX);
				touchEvent.setX(m_touchX[pointerId]);
				m_touchY[pointerId] = (int) (event.getY(pointerIndex) * m_scaleY);
				touchEvent.setY(m_touchY[pointerId]);
				
				m_isTouched[pointerId] = false;
				m_touchEventsBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_MOVE:
				//複数タッチ処理を同時にする可能性があるので取得
				int pointerCount = event.getPointerCount();
				
				for (int i = 0; i < pointerCount; i++) {
					pointerIndex = i;
					pointerId = event.getPointerId(pointerIndex);
					touchEvent = m_touchEventPool.newObject();
					touchEvent.setType(TouchEvent.TOUCH_DRAGGED);
					touchEvent.setPointer(pointerId);
					m_touchX[pointerId] = (int) (event.getX(pointerIndex) * m_scaleX);
					touchEvent.setX(m_touchX[pointerId]);
					m_touchY[pointerId] = (int) (event.getY(pointerIndex) * m_scaleY);
					touchEvent.setY(m_touchY[pointerId]);
					
					m_touchEventsBuffer.add(touchEvent);
				}
				break;
			}
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if(pointer < 0 || pointer >= 20){
				return false;
			}else{
				return m_isTouched[pointer];
			}
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return m_touchX[pointer];	
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return m_touchY[pointer];
		}
	}

	@Override
	public ArrayList<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = m_touchEvents.size();
			for (int i = 0; i < len; i++)
				m_touchEventPool.free(m_touchEvents.get(i));
			m_touchEvents.clear();
			m_touchEvents.addAll(m_touchEventsBuffer);
			m_touchEventsBuffer.clear();
			return m_touchEvents;
		}
	}

}
