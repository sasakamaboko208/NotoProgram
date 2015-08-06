package com.DaichiNoto.framework;

import com.DaichiNoto.framework.math.Vector2;

/**
 * ゲームオブジェクト（動的）
 * @author Daichi Noto
 *
 */
public class DynamicGameObject extends GameObject {
	public final Vector2 m_velocity;
	public final Vector2 m_accel;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		m_velocity = new Vector2();
		m_accel = new Vector2();
	}
	
	/**
	 * 速さを取得
	 * @return
	 */
	public Vector2 getVelocity(){
		return m_velocity;
	}
	
	/**
	 * 加速度を取得
	 * @return
	 */
	public Vector2 getAccel(){
		return m_accel;
	}
}
