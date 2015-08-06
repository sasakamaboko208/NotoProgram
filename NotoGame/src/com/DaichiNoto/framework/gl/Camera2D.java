package com.DaichiNoto.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.Main.GameMain;
import com.DaichiNoto.framework.math.Vector2;
/**
 * カメラクラス(２D)
 * @author Daichi Noto
 *
 */
public class Camera2D {
	/**
	 * メンバ変数
	 */
	public final Vector2 m_position;
	public float m_zoom;
	public final float m_frustumWidth;
	public final float m_frustumHeight;
	final GameMain m_game;
	
	/**
	 * コンストラクタ
	 * @param game
	 * @param frustumWidth
	 * @param frustumHeight
	 */
	public Camera2D(GameMain game, float frustumWidth, float frustumHeight) {
		m_game = game;
		m_frustumWidth = frustumWidth;
		m_frustumHeight = frustumHeight;
		m_position = new Vector2(frustumWidth / 2, frustumHeight / 2);
		m_zoom = 1.0f;
	}

	/**
	 * 
	 */
	public void setViewportAndMatrices() {
		GL10 gl = m_game.getGL10();
		gl.glViewport(0, 0, m_game.getGlSurfaceView().getWidth(), m_game.getGlSurfaceView().getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(m_position.getX()- m_frustumWidth * m_zoom / 2,
				m_position.getX() + m_frustumWidth * m_zoom/ 2,
				m_position.getY() - m_frustumHeight * m_zoom / 2,
				m_position.getY() + m_frustumHeight * m_zoom/ 2,
				1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	/**
	 * タッチできる範囲（2D）
	 * @param touch
	 */
	public void touchToWorld(Vector2 touch) {
		touch.set( (touch.getX() / (float) m_game.getGlSurfaceView().getWidth()) * m_frustumWidth * m_zoom,
				(1 - touch.getY() / (float) m_game.getGlSurfaceView().getHeight()) * m_frustumHeight * m_zoom);
		touch.add(m_position).sub(m_frustumWidth * m_zoom / 2, m_frustumHeight * m_zoom / 2);
	}
}
