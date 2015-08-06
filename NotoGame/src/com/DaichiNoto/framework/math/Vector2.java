package com.DaichiNoto.framework.math;

import android.util.FloatMath;

/**
 * ２Dベクトル計算
 * @author Daichi Noto
 *
 */
public class Vector2 {
	public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
	public static float TO_DEGREES = (1 / (float) Math.PI) * 180;
	private float m_x, m_y;

	/**
	 * デフォルトコンストラクタ（デフォルト）
	 */
	public Vector2() {
	}
	
	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 */
	public Vector2(float x, float y) {
		m_x = x;
		m_y = y;
	}
	
	/**
	 * コンストラクタ（ベクトルを取得）
	 * @param other
	 */
	public Vector2(Vector2 other) {
		m_x = other.m_x;
		m_y = other.m_y;
	}
	
	/**
	 * ベクトルのコピー関数
	 * @return
	 */
	public Vector2 cpy() {
		return new Vector2(m_x, m_y);
	}
	
	/**
	 * ベクトルの設定
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 set(float x, float y) {
		m_x = x;
		m_y = y;
		return this;
	}

	public Vector2 set(Vector2 other) {
		m_x = other.m_x;
		m_y = other.m_y;
		return this;
	}	

	
	/**
	 * ベクトルの加算
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 add(float x, float y) {
		m_x += x;
		m_y += y;
		return this;
	}
	
	public Vector2 add(Vector2 other) {
		m_x += other.m_x;
		m_y += other.m_y;
		return this;
	}
	
	
	/**
	 * ベクトルの減算
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 sub(float x, float y) {
		m_x -= x;
		m_y -= y;
		return this;
	}

	public Vector2 sub(Vector2 other) {
		m_x -= other.m_x;
		m_y -= other.m_y;
		return this;
	}
	
	
	/**
	 * ベクトルにスカラー値を掛ける関数
	 * @param scalar
	 * @return
	 */
	public Vector2 mul(float scalar) {
		m_x *= scalar;
		m_y *= scalar;
		return this;
	}

	/**
	 * ベクトルの長さを取得する関数
	 * @return
	 */
	public float len() {
		return FloatMath.sqrt(m_x * m_x + m_y * m_y);
	}
	
	/**
	 * ベクトルの正規化関数
	 * @return
	 */
	public Vector2 nor() {
		float len = len();
		if (len != 0) {
			m_x /= len;
			m_y /= len;
		}
		return this;
	}
	
	/**
	 * 角度を取得する関数
	 * @return
	 */
	public float angle() {
		float angle = (float) Math.atan2(m_y, m_x) * TO_DEGREES;
		if (angle < 0)
			angle += 360;
		return angle;
	}

	/**
	 * 指定した角度分ベクトル回転させる関数
	 * @param angle
	 * @return
	 */
	public Vector2 rotate(float angle) {
		float rad = angle * TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);

		float newX = m_x * cos - m_y * sin;
		float newY = m_x * sin + m_y * cos;
		
		m_x = newX;
		m_y = newY;
		
		return this;
	}

	/*:
	 * ベクトル同士の距離を計算するメソッド
	 */
	public float dist(Vector2 other) {
		float distX = m_x - other.m_x;
		float distY = m_y - other.m_y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	public float dist(float x, float y) {
		float distX = m_x - x;
		float distY = m_y - y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	public float distSquared(Vector2 other) {
		float distX = m_x - other.m_x;
		float distY = m_y - other.m_y;
		return distX*distX + distY*distY;
	}

	public float distSquared(float x, float y) {
		float distX = m_x - x;
		float distY = m_y - y;
		return distX * distX + distY * distY;
	}
	
	/**
	 * 位置を取得関数
	 * @return
	 */
	public float getX(){
		return m_x;
	}
	
	public float getY(){
		return m_y;
	}
}
