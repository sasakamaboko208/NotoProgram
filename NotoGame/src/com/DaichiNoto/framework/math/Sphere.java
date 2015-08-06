package com.DaichiNoto.framework.math;

/**
 * 球クラス
 * @author Daichi Noto
 *
 */
public class Sphere {
	public final Vector3 m_center = new Vector3();
	public float m_radius;

	public Sphere(float x, float y, float z, float radius) {
		m_center.set(x,y,z);
		m_radius = radius;
	}
}
