package com.DaichiNoto.Main;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.DynamicGameObject3D;
import com.DaichiNoto.framework.gl.Animation;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.gl.TextureRegion;
import com.DaichiNoto.framework.math.Vector3;

/**
 * インベーダークラス
 * @author Daichi Noto
 *
 */
public class Invader extends DynamicGameObject3D {
	static final int INVADER_ALIVE = 0;
	static final int INVADER_DEAD = 1;
	static final float INVADER_EXPLOSION_TIME = 1.6f;
	static final float INVADER_RADIUS = 0.75f;
	static final float INVADER_VELOCITY = 1;
	static final int MOVE_LEFT = 0;
	static final int MOVE_DOWN = 1;
	static final int MOVE_RIGHT = 2;
		
	int m_state = INVADER_ALIVE;
	float m_stateTime = 0;
	int m_move = MOVE_LEFT;
	boolean m_wasLastStateLeft = true;
	float m_movedDistance = GameManager.WORLD_MAX_X / 2;
	
	private float invaderAngle = 0;
	
	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 */
	public Invader(float x, float y, float z) {
		super(x, y, z, INVADER_RADIUS);
	}
	
	/**
	 * 更新処理
	 * @param deltaTime
	 * @param speedMultiplier
	 */
	public void Update(float deltaTime, float speedMultiplier) {
		if (m_state == INVADER_ALIVE) {
			m_movedDistance += deltaTime * INVADER_VELOCITY * speedMultiplier;
			if (m_move == MOVE_LEFT) {
				getPosition().set(getPosition().getX() - deltaTime * INVADER_VELOCITY * speedMultiplier, getPosition().getY(), getPosition().getZ());
				if (m_movedDistance > GameManager.WORLD_MAX_X) {
					m_move = MOVE_DOWN;
					m_movedDistance = 0;
					m_wasLastStateLeft = true;
				}
			}
			if (m_move == MOVE_RIGHT) {
				getPosition().set(getPosition().getX() + deltaTime * INVADER_VELOCITY * speedMultiplier, getPosition().getY(), getPosition().getZ());

				if (m_movedDistance > GameManager.WORLD_MAX_X) {
					m_move = MOVE_DOWN;
					m_movedDistance = 0;
					m_wasLastStateLeft = false;
				}
			}
			if (m_move == MOVE_DOWN) {
				getPosition().set(getPosition().getX(), getPosition().getY(), getPosition().getZ() + deltaTime * INVADER_VELOCITY * speedMultiplier);

				if (m_movedDistance > 1) {
					if (m_wasLastStateLeft)
						m_move = MOVE_RIGHT;
					else
						m_move = MOVE_LEFT;
					m_movedDistance = 0;
				}
			}
			getBound().m_center.set(getPosition());
		}
		m_stateTime += deltaTime;
	}
	
	/**
	 * 描画処理
	 */
	public void Draw(GL10 gl, SpriteBatcher batcher, float deltaTime){
		invaderAngle += 45 * deltaTime;

		Assets.m_invaderTexture.Bind();
		Assets.m_invaderModel.Bind();
		if(m_state == Invader.INVADER_DEAD){
			gl.glDisable(GL10.GL_LIGHTING);
			Assets.m_invaderModel.Unbind();
			Explosion(gl, getPosition(), m_stateTime, batcher);
			Assets.m_invaderTexture.Bind();
			Assets.m_invaderModel.Bind();
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glPushMatrix();
			gl.glTranslatef(getPosition().getX(), getPosition().getY(), getPosition().getZ());
			gl.glRotatef(invaderAngle, 0, 1, 0);
			Assets.m_invaderModel.Draw(GL10.GL_TRIANGLES, 0,
					Assets.m_invaderModel.GetNumVertices());
			gl.glPopMatrix();
		}

		Assets.m_invaderModel.Unbind();
	}
	
	/**
	 * 爆発エフェクト
	 * @param gl
	 * @param position
	 * @param stateTime
	 */
	private void Explosion(GL10 gl, Vector3 position, float stateTime, SpriteBatcher batcher) {
		TextureRegion frame = Assets.m_explosionAnim.getKeyFrame(stateTime, Animation.ANIMATION_NONLOOPING);
	
		gl.glEnable(GL10.GL_BLEND);
		gl.glPushMatrix();
		gl.glTranslatef(position.getX(), position.getY(), position.getZ());
		batcher.beginBatch(Assets.m_explosionTexture);
		batcher.drawSprite(0, 0, 2, 2, frame);
		batcher.endBatch();
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_BLEND);
	}	
	

	/**
	 * 死亡関数
	 */
	public void kill() {
		m_state = INVADER_DEAD;
		m_stateTime = 0;
	}
}
