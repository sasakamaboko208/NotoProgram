package com.DaichiNoto.Main;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.SceneBase;
import com.DaichiNoto.framework.Input.Input.TouchEvent;
import com.DaichiNoto.framework.gl.Camera2D;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.math.Overlap;
import com.DaichiNoto.framework.math.Rectangle;
import com.DaichiNoto.framework.math.Vector2;

/**
 * 設定画面クラス
 * @author Daichi Noto
 *
 */
public class SettingsScene extends SceneBase {
	Camera2D m_guiCam;
	SpriteBatcher m_batcher;
	Vector2 m_touchPoint;
	Rectangle m_touchBounds;
	Rectangle m_accelBounds;
	Rectangle m_soundBounds;
	Rectangle m_backBounds;
	
	/**
	 * コンストラクタ
	 * @param game
	 */
	public SettingsScene(GameMain game) {
		super(game);
		m_guiCam = new Camera2D(game, 480, 320);
		m_batcher = new SpriteBatcher(game, 10);
		m_touchPoint = new Vector2();

		m_touchBounds = new Rectangle(120 - 32, 160 - 32, 64, 64);
		m_accelBounds = new Rectangle(240 - 32, 160 - 32, 64, 64);
		m_soundBounds = new Rectangle(360 - 32, 160 - 32, 64, 64);
		m_backBounds = new Rectangle(32, 32, 64, 64);
	}
	
	/**
	 * 更新処理
	 */
	@Override
	public void Update(float deltaTime) {
		List<TouchEvent> events = m_glMain.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			if (event.getType() != TouchEvent.TOUCH_UP)
				continue;

			m_guiCam.touchToWorld(m_touchPoint.set(event.getX(), event.getY()));
			if (Overlap.PointInRectangle(m_touchBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				Settings.m_touchEnabled = true;
				Settings.Save(m_glMain.getFileIO());
			}
			if (Overlap.PointInRectangle(m_accelBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				Settings.m_touchEnabled = false;
				Settings.Save(m_glMain.getFileIO());
			}
			if (Overlap.PointInRectangle(m_soundBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				Settings.m_soundEnabled = !Settings.m_soundEnabled;
				if (Settings.m_soundEnabled) {
					Assets.m_music.play();
				} else {
					Assets.m_music.pause();
				}
				Settings.Save(m_glMain.getFileIO());
			}
			if (Overlap.PointInRectangle(m_backBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_glMain.setScene(new MainMenuScene(m_glMain));
			}
		}
	}
	
	/**
	 * 描画処理
	 */
	@Override
	public void Draw(float deltaTime) {
		GL10 gl = m_glMain.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		m_guiCam.setViewportAndMatrices();

		gl.glEnable(GL10.GL_TEXTURE_2D);

		m_batcher.beginBatch(Assets.m_background);
		m_batcher.drawSprite(240, 160, 480, 320, Assets.m_backgroundRegion);
		m_batcher.endBatch();
	
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	
		m_batcher.beginBatch(Assets.m_items);
		m_batcher.drawSprite(240, 280, 224, 32, Assets.m_settingsRegion);
		m_batcher.drawSprite(120, 160, 64, 64,
				Settings.m_touchEnabled ? Assets.m_touchEnabledRegion : Assets.m_touchRegion);
		m_batcher.drawSprite(240, 160, 64, 64,
				Settings.m_touchEnabled ? Assets.m_accelRegion
						: Assets.m_accelEnabledRegion);
		m_batcher.drawSprite(360, 160, 64, 64,
				Settings.m_soundEnabled ? Assets.m_soundEnabledRegion : Assets.m_soundRegion);
		m_batcher.drawSprite(32, 32, 64, 64, Assets.m_leftRegion);
		m_batcher.endBatch();

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public void dispose() {
	}	
}
