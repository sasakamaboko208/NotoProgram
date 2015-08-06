package com.DaichiNoto.framework;

import com.DaichiNoto.framework.math.Vector3;

/**
 * ゲームオブジェクト3D(動的)
 * @author Daichi Noto
 *
 */
public class DynamicGameObject3D extends GameObject3D {
	public final Vector3 m_velocity;
	public final Vector3 m_accel;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public DynamicGameObject3D(float x, float y, float z, float radius) {
		super(x, y, z, radius);
		m_velocity = new Vector3();
		m_accel = new Vector3();
	}	
	
	/**
	 * 速さを取得
	 * @return
	 */
	public Vector3 getVelocity(){
		return m_velocity;
	}
	
	/**
	 * 加速度を取得
	 * @return
	 */
	public Vector3 getAccel(){
		return m_accel;
	}
}
