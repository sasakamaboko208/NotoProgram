package com.DaichiNoto.Main;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.DynamicGameObject3D;

public class Shot extends DynamicGameObject3D {
	static float SHOT_VELOCITY = 10f;
	static float SHOT_RADIUS = 0.1f;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 * @param velocityZ
	 */
	public Shot(float x, float y, float z, float velocityZ) {
		super(x, y, z, SHOT_RADIUS);
		getVelocity().set(getVelocity().getX(), getVelocity().getY(), velocityZ);
	}

	/**
	 * 更新処理
	 * @param deltaTime
	 */
	public void Update(float deltaTime) {
		getPosition().add(0, 0,  getVelocity().getZ() * deltaTime);
		getBound().m_center.set(getPosition());
	}
	
	/**
	 * 描画処理
	 * @param gl
	 */
	public void Draw(GL10 gl){
		gl.glColor4f(1, 1, 0, 1);
		Assets.m_shotModel.Bind();

		gl.glPushMatrix();
		gl.glTranslatef(getPosition().getX(), getPosition().getY(), getPosition().getZ());
		Assets.m_shotModel.Draw(GL10.GL_TRIANGLES, 0, Assets.m_shotModel.GetNumVertices());
		gl.glPopMatrix();
			
		Assets.m_shotModel.Unbind();
		gl.glColor4f(1, 1, 1, 1);
	}
}
