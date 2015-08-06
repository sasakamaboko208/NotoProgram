package com.DaichiNoto.framework.gl;

import javax.microedition.khronos.opengles.GL10;

/**
 * アンビエントライトクラス（均一にライトを照らす）
 * @author Daichi Noto
 *
 */
public class AmbientLight {
	float[] m_color = {0.2f, 0.2f, 0.2f, 1};

	/**
	 * 色の設定
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void SetColor(float r, float g, float b, float a) {
		m_color[0] = r;
		m_color[1] = g;
		m_color[2] = b;
		m_color[3] = a;
	}
	
	/**
	 * ライトの使用
	 * @param gl
	 */
	public void Enable(GL10 gl) {
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, m_color, 0);
	}
}
