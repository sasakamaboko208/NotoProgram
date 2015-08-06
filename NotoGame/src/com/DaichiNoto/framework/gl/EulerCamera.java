package com.DaichiNoto.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.Matrix;

import com.DaichiNoto.framework.math.Vector3;

/**
 * オイラーカメラクラス
 * @author Daichi Noto
 *
 */
public class EulerCamera {
	final Vector3 m_position = new Vector3();
	float m_yaw;
	float m_pitch;
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
	public EulerCamera(float fieldOfView, float aspectRatio, float near, float far){
		m_fieldOfView = fieldOfView;
		m_aspectRatio = aspectRatio;
		m_near = near;
		m_far = far;
	}
	
	/**
	 * 位置情報の取得
	 * @return
	 */
	public Vector3 GetPosition() {
		return m_position;
	}

	/**
	 * 水平面回転軸の取得
	 * @return
	 */
	public float GetYaw() {
		return m_yaw;
	}

	/**
	 * 左右を軸に回転軸を取得
	 * @return
	 */
	public float GetPitch() {
		return m_pitch;
	}
	
	/**
	 * ヨーとピッチを直接指定すして回転
	 * @param yaw
	 * @param pitch
	 */
	public void SetAngles(float yaw, float pitch) {
		if (pitch < -90)
			pitch = -90;
		if (pitch > 90)
			pitch = 90;
		m_yaw = yaw;
		m_pitch = pitch;
	}

	/**
	 * 角度を設定
	 * @param yawInc
	 * @param pitchInc
	 */
	public void Rotate(float yawInc, float pitchInc) {
		m_yaw += yawInc;
		m_pitch += pitchInc;
		if (m_pitch < -90)
			m_pitch = -90;
		if (m_pitch > 90)
			m_pitch = 90;
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
		gl.glRotatef(-m_pitch, 1, 0, 0);
		gl.glRotatef(-m_yaw, 0, 1, 0);
		gl.glTranslatef(-m_position.getX(), -m_position.getY(), -m_position.getZ());
	}
	
	final float[] m_matrix = new float[16];
	final float[] m_inVec = { 0, 0, -1, 1 };
	final float[] m_outVec = new float[4];
	final Vector3 m_direction = new Vector3();

	/**
	 * カメラの向き関数
	 * @return
	 */
	public Vector3 GetDirection() {
		Matrix.setIdentityM(m_matrix, 0);
		Matrix.rotateM(m_matrix, 0, m_yaw, 0, 1, 0);
		Matrix.rotateM(m_matrix, 0, m_pitch, 1, 0, 0);
		Matrix.multiplyMV(m_outVec, 0, m_matrix, 0, m_inVec, 0);
		m_direction.set(m_outVec[0], m_outVec[1], m_outVec[2]);
		return m_direction;
	}
}
