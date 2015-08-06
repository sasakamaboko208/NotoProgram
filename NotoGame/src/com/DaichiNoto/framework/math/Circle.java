package com.DaichiNoto.framework.math;

/**
 * 円クラス
 * @author Daichi Noto
 *
 */
public class Circle {
	public final Vector2 m_center = new Vector2();
	public float m_radius;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle(float x, float y, float radius) {
		m_center.set(x,y);
		m_radius = radius;
	}
}
