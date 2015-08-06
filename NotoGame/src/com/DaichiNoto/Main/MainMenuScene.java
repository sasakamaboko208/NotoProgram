package com.DaichiNoto.Main;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.framework.SceneBase;
import com.DaichiNoto.framework.Input.Input;
import com.DaichiNoto.framework.gl.Camera2D;
import com.DaichiNoto.framework.gl.SpriteBatcher;
import com.DaichiNoto.framework.math.Overlap;
import com.DaichiNoto.framework.math.Rectangle;
import com.DaichiNoto.framework.math.Vector2;

/**
 * メインメニュークラス
 * @author Daichi Noto
 *
 */
public class MainMenuScene extends SceneBase{
	/**
	 * メンバ変数
	 */
	Camera2D m_guiCam;
	SpriteBatcher m_batcher;
	Vector2 m_touchPoint;
	Rectangle m_playBounds;
	Rectangle m_settingsBounds;
	
	/**
	 * コンストラクタ
	 * @param game
	 */
	public MainMenuScene(GameMain game){
		super(game);
		m_guiCam = new Camera2D(game, 480, 320);
		m_batcher = new SpriteBatcher(game, 10);
		m_touchPoint = new Vector2();
		m_playBounds = new Rectangle(240 - 112, 100, 224, 32);
		m_settingsBounds = new Rectangle(240 - 112, 100 - 32, 224, 32);
	}

	/**
	 * 更新処理
	 */
	@Override
	public void Update(float deltaTime) {
		// TODO 自動生成されたメソッド・スタブ
		List<Input.TouchEvent> events = m_glMain.getInput().getTouchEvents();
		int len = events.size();
		for(int i = 0; i < len; i++) {
			Input.TouchEvent event = events.get(i);
			if(event.getType() != Input.TouchEvent.TOUCH_UP)
				continue;

			m_guiCam.touchToWorld(m_touchPoint.set(event.getX(), event.getY()));
			if(Overlap.PointInRectangle(m_playBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_glMain.setScene(new GameScene(m_glMain));
			}
			if(Overlap.PointInRectangle(m_settingsBounds, m_touchPoint)) {
				Assets.PlaySound(Assets.m_clickSound);
				m_glMain.setScene(new SettingsScene(m_glMain));
			}
		}
	}

	/**
	 * 描画関数
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
		m_batcher.drawSprite(240, 240, 384, 128, Assets.m_logoRegion);
		m_batcher.drawSprite(240, 100, 224, 64, Assets.m_menuRegion);
		m_batcher.endBatch();

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
	}

	@Override
	public void pause() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void resume() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
