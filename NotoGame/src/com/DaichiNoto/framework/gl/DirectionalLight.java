package com.DaichiNoto.framework.gl;

import javax.microedition.khronos.opengles.GL10;

/**
 * ライトクラス
 * @author Daichi Noto
 *
 */
public class DirectionalLight {
	float[] m_ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
	float[] m_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	float[] m_specular = { 0.0f, 0.0f, 0.0f, 1.0f };
	float[] m_direction = { 0, 0, -1, 0 };
	int m_lastLightId = 0;
	
	/**
	 * 環境光の設定
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void SetAmbient(float r, float g, float b, float a) {
		m_ambient[0] = r;
		m_ambient[1] = g;
		m_ambient[2] = b;
		m_ambient[3] = a;
	}
	
	/**
	 * 拡散光の設定
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void SetDiffuse(float r, float g, float b, float a) {
		m_diffuse[0] = r;
		m_diffuse[1] = g;
		m_diffuse[2] = b;
		m_diffuse[3] = a;
	}
	
	/**
	 * 光沢の設定
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void SetSpecular(float r, float g, float b, float a) {
		m_specular[0] = r;
		m_specular[1] = g;
		m_specular[2] = b;
		m_specular[3] = a;
	}
	
	/**
	 * 光の向き
	 * @param x
	 * @param y
	 * @param z
	 */
	public void SetDirection(float x, float y, float z) {
		m_direction[0] = -x;
		m_direction[1] = -y;
		m_direction[2] = -z;
	}

	/**
	 * 使用可能にする
	 * @param gl
	 * @param lightId
	 */
	public void Enable(GL10 gl, int lightId) {
		gl.glEnable(lightId);
		gl.glLightfv(lightId, GL10.GL_AMBIENT, m_ambient, 0);
		gl.glLightfv(lightId, GL10.GL_DIFFUSE, m_diffuse, 0);
		gl.glLightfv(lightId, GL10.GL_SPECULAR, m_specular, 0);
		gl.glLightfv(lightId, GL10.GL_POSITION, m_direction, 0);
		m_lastLightId = lightId;
	}

	/**
	 * 削除
	 * @param gl
	 */
	public void Disable(GL10 gl) {
		gl.glDisable(m_lastLightId);
	}		
}
