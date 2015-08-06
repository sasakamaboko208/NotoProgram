package com.DaichiNoto.Main;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.SceneBase;
import com.DaichiNoto.framework.Input.Input.TouchEvent;
import com.DaichiNoto.framework.gl.Camera2D;
import com.DaichiNoto.framework.gl.FPSCounter;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.math.Overlap;
import com.DaichiNoto.framework.math.Rectangle;
import com.DaichiNoto.framework.math.Vector2;

public class GameScene extends SceneBase{
	/**
	 * メンバ変数
	 */
	static final int GAME_RUNNING = 0;
	static final int GAME_PAUSED = 1;
	static final int GAME_OVER = 2;
	
	int m_state;
	Camera2D m_guiCam;
	Vector2 m_touchPoint;
	SpriteBatcher m_batcher;
	GameManager m_gameManager;
	Rectangle m_pauseBounds;
	Rectangle m_resumeBounds;
	Rectangle m_quitBounds;
	Rectangle m_leftBounds;
	Rectangle m_rightBounds;
	Rectangle m_shotBounds;
	int m_lastScore;
	int m_lastLives;
	int m_lastWaves;
	String m_scoreString;
	FPSCounter m_fpsCounter;
	
	/**
	 * コンストラクタ
	 * @param game
	 */
	public GameScene(GameMain game) {
		super(game);
		
		m_state = GAME_RUNNING;
		m_guiCam = new Camera2D(game, 480, 320);
		m_touchPoint = new Vector2();
		m_batcher = new SpriteBatcher(game, 100);
		m_gameManager = new GameManager(game);
		m_pauseBounds = new Rectangle(480 - 64, 320 - 64, 64, 64);
		m_resumeBounds = new Rectangle(240 - 80, 160, 160, 32);
		m_quitBounds = new Rectangle(240 - 80, 160 - 32, 160, 32);
		m_shotBounds = new Rectangle(480 - 64, 0, 64, 64);
		m_leftBounds = new Rectangle(0, 0, 64, 64);
		m_rightBounds = new Rectangle(64, 0, 64, 64);
		m_lastScore = 0;
		m_lastLives = m_gameManager.m_ship.getLife();
		m_lastWaves = m_gameManager.m_waves;
		m_scoreString = "lives:" + m_lastLives + " waves:" + m_lastWaves + " score:" + m_lastScore;
		m_fpsCounter = new FPSCounter();
	}
	
	/**
	 * 更新処理
	 */
	@Override
	public void Update(float deltaTime) {
		switch (m_state) {
		case GAME_PAUSED:
			UpdatePaused();
			break;
		case GAME_RUNNING:
			UpdateRunning(deltaTime);
			break;
		case GAME_OVER:
			UpdateGameOver();
			break;
		}
	}
	
	/**
	 * ポーズ中のの更新処理
	 */
	private void UpdatePaused() {
		List<TouchEvent> events = m_glMain.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			if (event.getType() != TouchEvent.TOUCH_UP)
				continue;

			m_guiCam.touchToWorld(m_touchPoint.set(event.getX(), event.getY()));
			if (Overlap.PointInRectangle(m_resumeBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_state = GAME_RUNNING;
			}
	
			if (Overlap.PointInRectangle(m_quitBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_glMain.setScene(new MainMenuScene(m_glMain));
			}
		}
	}
	
	/**
	 * ゲーム中の更新処理
	 * @param deltaTime
	 */
	private void UpdateRunning(float deltaTime) {
		List<TouchEvent> events = m_glMain.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			if (event.getType() != TouchEvent.TOUCH_DOWN)
				continue;

			m_guiCam.touchToWorld(m_touchPoint.set(event.getX(), event.getY()));

			if (Overlap.PointInRectangle(m_pauseBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_state = GAME_PAUSED;
			}
			if (Overlap.PointInRectangle(m_shotBounds, m_touchPoint)) {
				m_gameManager.Shoot();
			}
		}

		m_gameManager.Update(deltaTime, CalculateInputAcceleration());
		if (m_gameManager.m_ship.getLife() != m_lastLives || m_gameManager.m_score != m_lastScore || m_gameManager.m_waves != m_lastWaves) {
			m_lastLives = m_gameManager.m_ship.getLife();
			m_lastScore = m_gameManager.m_score;
			m_lastWaves = m_gameManager.m_waves;
			m_scoreString = "lives:" + m_lastLives + " waves:" + m_lastWaves + " score:" + m_lastScore;
		}
		if (m_gameManager.IsGameOver()) {
			m_state = GAME_OVER;
		}
	}
	
	private float CalculateInputAcceleration() {
		float accelX = 0;
		if (Settings.m_touchEnabled) {
			for (int i = 0; i < 2; i++) {
				if (m_glMain.getInput().isTouchDown(i)) {
					m_guiCam.touchToWorld(m_touchPoint.set(m_glMain.getInput()
							.getTouchX(i), m_glMain.getInput().getTouchY(i)));
					if (Overlap.PointInRectangle(m_leftBounds, m_touchPoint)) {
						accelX = -Ship.SHIP_VELOCITY / 5;
					}
					if (Overlap.PointInRectangle(m_rightBounds, m_touchPoint)) {
						accelX = Ship.SHIP_VELOCITY / 5;
					}
				}
			}
		} else {
			accelX = m_glMain.getInput().getAccelY();
		}
		return accelX;
	}
	
	/**
	 * ゲームオーバーになった場合の処理
	 */
	private void UpdateGameOver() {
		List<TouchEvent> events = m_glMain.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			if (event.getType() == TouchEvent.TOUCH_UP) {
				Assets.PlaySound(Assets.m_clickSound);
				m_glMain.setScene(new MainMenuScene(m_glMain));
			}
		}
	}
	
	/**
	 * 描画処理
	 * @param deltaTime
	 */
	@Override
	public void Draw(float deltaTime) {
		GL10 gl = m_glMain.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		m_guiCam.setViewportAndMatrices();

		gl.glEnable(GL10.GL_TEXTURE_2D);
		m_batcher.beginBatch(Assets.m_background);
		m_batcher.drawSprite(240, 160, 480, 320, Assets.m_backgroundRegion);
		m_batcher.endBatch();
		gl.glDisable(GL10.GL_TEXTURE_2D);
	
		m_gameManager.Draw(deltaTime);
		
		switch (m_state) {
		case GAME_RUNNING:
			DrawRunning(gl);
			break;
		case GAME_PAUSED:
			DrawPaused(gl);
			break;
		case GAME_OVER:
			DrawGameOver(gl);
		}
	
		m_fpsCounter.logFrame();
	}
	
	/**
	 * ポーズ中の描画処理
	 */
	private void DrawPaused(GL10 gl) {
		GL10 l_gl = gl;
		m_guiCam.setViewportAndMatrices();
		l_gl.glEnable(GL10.GL_BLEND);
		l_gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		l_gl.glEnable(GL10.GL_TEXTURE_2D);
	
		m_batcher.beginBatch(Assets.m_items);
		Assets.m_font.DrawText(m_batcher, m_scoreString, 10, 320-20);
		m_batcher.drawSprite(240, 160, 160, 64, Assets.m_pauseRegion);
		m_batcher.endBatch();
	
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}
	
	/**
	 * ゲーム中の描画処理
	 * @param gl
	 */
	private void DrawRunning(GL10 gl) {
		GL10 l_gl = gl;
		m_guiCam.setViewportAndMatrices();
		l_gl.glEnable(GL10.GL_BLEND);
		l_gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		l_gl.glEnable(GL10.GL_TEXTURE_2D);
	
		m_batcher.beginBatch(Assets.m_items);
		m_batcher.drawSprite(480- 32, 320 - 32, 64, 64, Assets.m_pauseButtonRegion);
		Assets.m_font.DrawText(m_batcher, m_scoreString, 10, 320-20);
		if(Settings.m_touchEnabled) {
			m_batcher.drawSprite(32, 32, 64, 64, Assets.m_leftRegion);
			m_batcher.drawSprite(96, 32, 64, 64, Assets.m_rightRegion);
		}
		m_batcher.drawSprite(480 - 40, 32, 64, 64, Assets.m_fireRegion);
		m_batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}		
	
	/**
	 * ゲームオーバー中の描画処理
	 * @param gl
	 */
	private void DrawGameOver(GL10 gl) {
		GL10 l_gl = gl;
		m_guiCam.setViewportAndMatrices();
		l_gl.glEnable(GL10.GL_BLEND);
		l_gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		l_gl.glEnable(GL10.GL_TEXTURE_2D);

		m_batcher.beginBatch(Assets.m_items);
		m_batcher.drawSprite(240, 160, 128, 64, Assets.m_gameOverRegion);
		Assets.m_font.DrawText(m_batcher, m_scoreString, 10, 320-20);
		m_batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void pause() {
		m_state = GAME_PAUSED;
	}	
	
	@Override
	public void resume() {
	}

	@Override
	public void dispose() {	
	}

}
