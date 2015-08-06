package com.DaichiNoto.Main;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.DynamicGameObject3D;
import com.DaichiNoto.framework.gl.Animation;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.gl.TextureRegion;
import com.DaichiNoto.framework.math.Vector3;
/**
 * 船(プレイヤー)
 * @author Daichi Noto
 *
 */
public class Ship extends DynamicGameObject3D {
	static float SHIP_VELOCITY = 20f;
	static int SHIP_ALIVE = 0;
	static int SHIP_EXPLODING = 1;
	static float SHIP_EXPLOSION_TIME = 1.6f;
	static float SHIP_RADIUS = 0.5f;
	
	private int m_lives;
	private int m_state;
	float m_stateTime = 0;
	
	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param z
	 */
	public Ship(float x, float y, float z) {
		super(x, y, z, SHIP_RADIUS);
		m_lives = 3;
		m_state = SHIP_ALIVE;
	}
	
	/**
	 * 更新処理
	 * @param deltaTime
	 * @param accelY
	 */
	public void Update(float deltaTime, float accelY) {
		if (m_state == SHIP_ALIVE) {
			getVelocity().set(accelY / 10 * SHIP_VELOCITY, 0, 0);
			getPosition().add(getVelocity().getX() * deltaTime, 0, 0);
			if (getPosition().getX() < GameManager.WORLD_MIN_X)
				getPosition().set(GameManager.WORLD_MIN_X, getPosition().getY(), getPosition().getZ());
			if (getPosition().getX() > GameManager.WORLD_MAX_X)
				getPosition().set(GameManager.WORLD_MAX_X, getPosition().getY(), getPosition().getZ());
			getBound().m_center.set(getPosition());
		} else {
			if (m_stateTime >= SHIP_EXPLOSION_TIME) {
				m_lives--;
				m_stateTime = 0;
				m_state = SHIP_ALIVE;
			}
		}
		m_stateTime += deltaTime;
	}
	
	/**
	 * 描画処理
	 */
	public void Draw(GL10 gl, SpriteBatcher batcher){
		if (m_state == SHIP_EXPLODING) {
			gl.glDisable(GL10.GL_LIGHTING);
			Explosion(gl, getPosition(), m_stateTime, batcher);
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			Assets.m_shipTexture.Bind();
			Assets.m_shipModel.Bind();
			gl.glPushMatrix();
			gl.glTranslatef(getPosition().getX(), getPosition().getY(), getPosition().getZ());
			gl.glRotatef(getVelocity().getX() / Ship.SHIP_VELOCITY * 90, 0, 0, -1);
			Assets.m_shipModel.Draw(GL10.GL_TRIANGLES, 0, Assets.m_shipModel.GetNumVertices());
			gl.glPopMatrix();
			Assets.m_shipModel.Unbind();
		}
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
	 *　死亡処理
	 */
	public void kill() {
		m_state = SHIP_EXPLODING;
		m_stateTime = 0;
		getVelocity().set(0, getPosition().getY(), getPosition().getZ());
	}
	
	/**
	 * 残機取得
	 * @return
	 */
	public int getLife(){
		return m_lives;
	}
	
	/**
	 * 船の状態を取得
	 * @return
	 */
	public int getState(){
		return m_state;
	}
	
	/**
	 * 残機の設定
	 * @param lives
	 */
	public void setLife(int lives){
		m_lives = lives;
	}
	
	/**
	 * 状態を設定
	 * @param state
	 */
	public void setState(int state){
		m_state = state;
	}
}
