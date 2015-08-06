package com.DaichiNoto.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.DaichiNoto.framework.math.Vector3;

/**
 * 目線カメラクラス
 * @author Daichi Noto
 *
 */
public class LookAtCamera {
	/**
	 * メンバ変数
	 */
	final Vector3 m_position;
	final Vector3 m_up;
	final Vector3 m_lookAt;
	float m_fieldOfView;
	float m_aspectRatio;
	float m_near;
	float m_far;

	/**
	 * コンストラクタ
	 * @param fieldOfView
	 * @param aspectRatio
	 * @param near
	 * @param far
	 */
	public LookAtCamera(float fieldOfView, float aspectRatio, float near, float far) {
		m_fieldOfView = fieldOfView;
		m_aspectRatio = aspectRatio;
		m_near = near;
		m_far = far;

		m_position = new Vector3();
		m_up = new Vector3(0, 1, 0);
		m_lookAt = new Vector3(0,0,-1);
	}

	/**
	 * 位置取得
	 * @return
	 */
	public Vector3 GetPosition() {
		return m_position;
	}

	/**
	 * アップベクトルの取得
	 * @return
	 */
	public Vector3 GetUp() {
		return m_up;
	}

	/**
	 * 方向ベクトルの取得
	 * @return
	 */
	public Vector3 GetLookAt() {
		return m_lookAt;
	}

	/**
	 * カメラの設定（モデルビュー行列と投影行列を設定）
	 * @param gl
	 */
	public void SetMatrices(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, m_fieldOfView, m_aspectRatio, m_near, m_far);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, m_position.getX(), m_position.getY(), m_position.getZ(), m_lookAt.getX(), m_lookAt.getY(), m_lookAt.getZ(), m_up.getX(), m_up.getY(), m_up.getZ());
	}
}
