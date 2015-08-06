package com.DaichiNoto.framework.math;

import android.opengl.Matrix;
import android.util.FloatMath;

/**
 * 3Dベクトルクラス
 * @author Daichi Noto
 *
 */
public class Vector3 {
	/**
	 * メンバ変数
	 */
	private static final float[] m_matrix = new float[16];
	private static final float[] m_inVec = new float[4];
	private static final float[] m_outVec = new float[4];
	private float m_x, m_y, m_z;
	
	/**
	 * デフォルトコンストラクタ
	 */
	public Vector3() {
	}

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(float x, float y, float z) {
		m_x = x;
		m_y = y;
		m_z = z;
	}
	
	public Vector3(Vector3 other) {
		m_x = other.m_x;
		m_y = other.m_y;
		m_z = other.m_z;
	}

	/**
	 * コピー関数
	 * @return
	 */
	public Vector3 cpy() {
		return new Vector3(m_x, m_y, m_z);
	}
	
	/**
	 * 位置設定
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Vector3 set(float x, float y, float z) {
		m_x = x;
		m_y = y;
		m_z = z;
		return this;
	}
	
	public Vector3 set(Vector3 other) {
		m_x = other.m_x;
		m_y = other.m_y;
		m_z = other.m_z;
		return this;
	}
	
	/**
	 * 加算関数
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Vector3 add(float x, float y, float z) {
		m_x += x;
		m_y += y;
		m_z += z;
		return this;
	}

	public Vector3 add(Vector3 other) {
		m_x += other.m_x;
		m_y += other.m_y;
		m_z += other.m_z;
		return this;
	}
	
	/**
	 * 減算関数
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Vector3 sub(float x, float y, float z) {
		m_x -= x;
		m_y -= y;
		m_z -= z;
		return this;
	}

	public Vector3 sub(Vector3 other) {
		m_x -= other.m_x;
		m_y -= other.m_y;
		m_z -= other.m_z;
		return this;
	}

	/**
	 * スカラーを掛ける関数
	 * @param scalar
	 * @return
	 */
	public Vector3 mul(float scalar) {
		m_x *= scalar;
		m_y *= scalar;
		m_z *= scalar;
		return this;
	}

	/**
	 * 距離を取得する関数
	 * @return
	 */
	public float len() {
		return FloatMath.sqrt(m_x * m_x + m_y * m_y + m_z * m_z);
	}

	/**
	 * 正規表現関数
	 * @return
	 */
	public Vector3 nor() {
		float len = len();
		if (len != 0) {
			m_x /= len;
			m_y /= len;
			m_z /= len;
		}
		return this;
	}

	/**
	 * 指定した角度分ベクトル回転させる関数
	 * @param angle
	 * @param axisX
	 * @param axisY
	 * @param axisZ
	 * @return
	 */
	public Vector3 rotate(float angle, float axisX, float axisY, float axisZ) {
		m_inVec[0] = m_x;
		m_inVec[1] = m_y;
		m_inVec[2] = m_z;
		m_inVec[3] = 1;
		Matrix.setIdentityM(m_matrix, 0);
		Matrix.rotateM(m_matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(m_outVec, 0, m_matrix, 0, m_inVec, 0);
		m_x = m_outVec[0];
		m_y = m_outVec[1];
		m_z = m_outVec[2];
		return this;
	}
	
	/*:
	 * ベクトル同士の距離を計算するメソッド
	 */
	public float dist(Vector3 other) {
		float distX = m_x - other.m_x;
		float distY = m_y - other.m_y;
		float distZ = m_z - other.m_z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}
	
	public float dist(float x, float y, float z) {
		float distX = m_x - x;
		float distY = m_y - y;
		float distZ = m_z - z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public float distSquared(Vector3 other) {
		float distX = m_x - other.m_x;
		float distY = m_y - other.m_y;
		float distZ = m_z - other.m_z;
		return distX * distX + distY * distY + distZ * distZ;
	}

	public float distSquared(float x, float y, float z) {
		float distX = m_x - x;
		float distY = m_y - y;
		float distZ = m_z - z;
		return distX * distX + distY * distY + distZ * distZ;
	}
	
	/**
	 * 位置取得関数
	 * @return
	 */
	public float getX(){
		return m_x;
	}
	
	public float getY(){
		return m_y;
	}
	
	public float getZ(){
		return m_z;
	}
}
