package com.DaichiNoto.framework.math;

/**
 * 四角形の範囲クラス
 * @author Daichi Noto
 *
 */
public class Rectangle {
	public final Vector2 m_lowerLeft;
	public float m_width, m_height;

	public Rectangle(float x, float y, float width, float height) {
		m_lowerLeft = new Vector2(x,y);
		m_width = width;
		m_height = height;
	}
}
