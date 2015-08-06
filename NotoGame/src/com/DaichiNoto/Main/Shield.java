package com.DaichiNoto.Main;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.GameObject3D;

/*:
 * シールドクラス
 */
public class Shield extends GameObject3D {
	static float SHIELD_RADIUS = 0.5f;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 */
	public Shield(float x, float y, float z) {
		super(x, y, z, SHIELD_RADIUS);
	}
	
	/**
	 * 描画関数
	 * @param gl
	 */
	public void Draw(GL10 gl) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(0, 0, 1, 0.4f);
		Assets.m_shieldModel.Bind();
		
		gl.glPushMatrix();
		gl.glTranslatef(getPosition().getX(), getPosition().getY(), getPosition().getZ());
		Assets.m_shieldModel.Draw(GL10.GL_TRIANGLES, 0, Assets.m_shieldModel.GetNumVertices());
		gl.glPopMatrix();
		
		Assets.m_shieldModel.Unbind();
		gl.glColor4f(1, 1, 1, 1f);
		gl.glDisable(GL10.GL_BLEND);
	}
}
