package com.DaichiNoto.framework;

import com.DaichiNoto.framework.math.Sphere;
import com.DaichiNoto.framework.math.Vector3;

/**
 * ゲームオブジェクト3D（静的）
 * @author Daichi Noto
 *
 */
public class GameObject3D {
	private final Vector3 m_position;
	private final Sphere m_bounds;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public GameObject3D(float x, float y, float z, float radius) {
		m_position = new Vector3(x,y,z);
		m_bounds = new Sphere(x, y, z, radius);
	}
	
	/**
	 * 位置を取得
	 * @return
	 */
	public Vector3 getPosition(){
		return m_position;
	}
	
	/**
	 * 範囲を取得
	 * @return
	 */
	public Sphere getBound(){
		return m_bounds;
	}
}
