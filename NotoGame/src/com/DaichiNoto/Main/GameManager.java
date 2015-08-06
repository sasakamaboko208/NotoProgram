package com.DaichiNoto.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.gl.AmbientLight;
import com.DaichiNoto.framework.gl.DirectionalLight;
import com.DaichiNoto.framework.gl.LookAtCamera;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.math.Overlap;

/**
 * ゲーム管理クラス
 * @author Daichi Noto
 *
 */
public class GameManager {

	final static float WORLD_MIN_X = -14;
	final static float WORLD_MAX_X = 14;
	final static float WORLD_MIN_Z = -15;

	int m_waves = 1;
	int m_score = 0;
	float m_speedMultiplier = 1;
	final List<Shot> m_shots = new ArrayList<Shot>();
	final List<Invader> m_invaders = new ArrayList<Invader>();
	final List<Shield> m_shields = new ArrayList<Shield>();
	final Ship m_ship;
	long m_lastShotTime;
	Random m_random;
	
	GameMain m_game;
	LookAtCamera m_camera;
	AmbientLight m_ambientLight;
	DirectionalLight m_directionalLight;
	SpriteBatcher m_batcher;
	float m_invaderAngle = 0;
	
	/**
	 * コンストラクタ
	 */
	public GameManager(GameMain game) {
		m_ship = new Ship(0, 0, 0);
		Invaders();
		Shields();
		m_lastShotTime = System.nanoTime();
		m_random = new Random();
		
		m_game = game;
		m_camera = new LookAtCamera(67, 
				m_game.getGlSurfaceView().getWidth() / (float) m_game.getGlSurfaceView().getHeight(), 0.1f, 100);
		m_camera.GetPosition().set(0, 6, 2);
		m_camera.GetLookAt().set(0, 0, -4);
		m_ambientLight = new AmbientLight();
		m_ambientLight.SetColor(0.2f, 0.2f, 0.2f, 1.0f);
		m_directionalLight = new DirectionalLight();
		m_directionalLight.SetDirection(-1, -0.5f, 0);
		m_batcher = new SpriteBatcher(m_game, 10);
	}
	
	/**
	 * インベーダーの初期化関数
	 */
	private void Invaders() {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 8; column++) {
				Invader invader = new Invader(-WORLD_MAX_X / 2 + column * 2f,
						0, WORLD_MIN_Z + row * 2f);	
				m_invaders.add(invader);
			}
		}
	}
	
	/**
	 * シールドの初期化関数
	 */
	private void Shields() {
		for (int shield = 0; shield < 3; shield++) {
			m_shields.add(new Shield(-10 + shield * 10 - 1, 0, -3));
			m_shields.add(new Shield(-10 + shield * 10 + 0, 0, -3));
			m_shields.add(new Shield(-10 + shield * 10 + 1, 0, -3));
			m_shields.add(new Shield(-10 + shield * 10 - 1, 0, -2));
			m_shields.add(new Shield(-10 + shield * 10 + 1, 0, -2));
		}
	}
	
	/**
	 * 更新処理
	 * @param deltaTime
	 * @param accelX
	 */
	public void Update(float deltaTime, float accelX) {
		m_ship.Update(deltaTime, accelX);
		UpdateInvaders(deltaTime);
		UpdateShots(deltaTime);
	
		CheckShotCollisions();
		CheckInvaderCollisions();

		if (m_invaders.size() == 0) {
			Invaders();
			m_waves++;
			m_speedMultiplier += 0.5f;
		}
	}	
	
	/**
	 * インベーダーの更新処理
	 * @param deltaTime
	 */
	private void UpdateInvaders(float deltaTime) {
		int len = m_invaders.size();
		for (int i = 0; i < len; i++) {
			Invader invader = m_invaders.get(i);
			invader.Update(deltaTime, m_speedMultiplier);

			if (invader.m_state == Invader.INVADER_ALIVE) {
				if (m_random.nextFloat() < 0.001f) {
					Shot shot = new Shot(invader.getPosition().getX(),
							invader.getPosition().getY(),
							invader.getPosition().getZ(),
							Shot.SHOT_VELOCITY);
					m_shots.add(shot);
					Assets.PlaySound(Assets.m_shotSound);
				}
			}

			if (invader.m_state == Invader.INVADER_DEAD &&
					invader.m_stateTime > Invader.INVADER_EXPLOSION_TIME) {
				m_invaders.remove(i);
				i--;
				len--;
			}
		}
	}
	
	/**
	 * シールドの更新処理
	 * @param deltaTime
	 */
	private void UpdateShots(float deltaTime) {
		int len = m_shots.size();
		for (int i = 0; i < len; i++) {
			Shot shot = m_shots.get(i);
			shot.Update(deltaTime);
			if (shot.getPosition().getZ() < WORLD_MIN_Z ||
					shot.getPosition().getZ() > 0) {
				m_shots.remove(i);
				i--;
				len--;
			}
		}
	}
	
	/**
	 * インベーダーの衝突判定
	 */
	private void CheckInvaderCollisions() {
		if (m_ship.getState() == Ship.SHIP_EXPLODING) return;

		int len = m_invaders.size();
		for (int i = 0; i < len; i++) {
			Invader invader = m_invaders.get(i);
			if (Overlap.OverlapSpheres(m_ship.getBound(), invader.getBound())) {
				m_ship.setLife(1);
				m_ship.kill();
				return;
			}
		}
	}
	
	/**
	 * 弾の衝突判定
	 */
	private void CheckShotCollisions() {
		int len = m_shots.size();
		for (int i = 0; i < len; i++) {
			Shot shot = m_shots.get(i);
			boolean shotRemoved = false;

			int len2 = m_shields.size();
			for (int j = 0; j < len2; j++) {
				Shield shield = m_shields.get(j);
				if (Overlap.OverlapSpheres(shield.getBound(), shot.getBound())) {
					m_shields.remove(j);
					m_shots.remove(i);
					i--;
					len--;
					shotRemoved = true;
					break;
				}
			}
			if (shotRemoved)
				continue;
	
			if (shot.getVelocity().getZ() < 0) {
				len2 = m_invaders.size();
				for (int j = 0; j < len2; j++) {
					Invader invader = m_invaders.get(j);
					if (Overlap.OverlapSpheres(invader.getBound(),
							shot.getBound())
							&& invader.m_state == Invader.INVADER_ALIVE) {
						invader.kill();
						Assets.PlaySound(Assets.m_explosionSound);
						m_score += 10;
						m_shots.remove(i);
						len--;
						break;
					}
				}
			} else {
				if (Overlap.OverlapSpheres(shot.getBound(), m_ship.getBound())
						&& m_ship.getState() == Ship.SHIP_ALIVE) {
					m_ship.kill();
					Assets.PlaySound(Assets.m_explosionSound);
					m_shots.remove(i);
					i--;
					len--;
				}
			}
		}
	}
	
	/**
	 * 描画関数
	 * @param deltaTime
	 */
	public void  Draw(float deltaTime){
		GL10 gl = m_game.getGL10();
		m_camera.GetPosition().set(m_ship.getPosition().getX(), m_camera.GetPosition().getY(), m_camera.GetPosition().getZ());
		m_camera.GetLookAt().set(m_ship.getPosition().getX(), m_camera.GetLookAt().getY(), m_camera.GetLookAt().getZ());
		m_camera.SetMatrices(gl);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		m_ambientLight.Enable(gl);
		m_directionalLight.Enable(gl, GL10.GL_LIGHT0);
		
		//船
		m_ship.Draw(gl, m_batcher);
		
		//インベーダの描画
		int len = m_invaders.size();
		for (int i = 0; i < len; i++) {
			Invader invader = m_invaders.get(i);
			invader.Draw(gl, m_batcher, deltaTime);
		}

		gl.glDisable(GL10.GL_TEXTURE_2D);
	
		//シールドの描画
		len = m_shields.size();
		for (int j = 0; j < len; j++) {
			Shield shield = m_shields.get(j);
			shield.Draw(gl);
		}

		//弾の描画
		len = m_shots.size();
		for (int i = 0; i < len; i++) {
			Shot shot = m_shots.get(i);
			shot.Draw(gl);
		}

	
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DEPTH_TEST);	
	}

	/**
	 * ゲームオーバーチェック関数
	 * @return
	 */
	public boolean IsGameOver() {
		return m_ship.getLife() == 0;
	}
	
	/**
	 * ショット関数
	 */
	public void Shoot() {
		if (m_ship.getState() == Ship.SHIP_EXPLODING)
			return;

		int friendlyShots = 0;
		int len = m_shots.size();
		for (int i = 0; i < len; i++) {
			if (m_shots.get(i).getVelocity().getZ() < 0)
				friendlyShots++;
		}

		if (System.nanoTime() - m_lastShotTime > 1000000000 || friendlyShots == 0) {
			m_shots.add(new Shot(m_ship.getPosition().getX(), m_ship.getPosition().getY(),
					m_ship.getPosition().getZ(), -Shot.SHOT_VELOCITY));
			m_lastShotTime = System.nanoTime();
			Assets.PlaySound(Assets.m_shotSound);
		}
	}	
}
