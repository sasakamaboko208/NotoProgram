package com.DaichiNoto.Main;

import com.DaichiNoto.framework.Music;
import com.DaichiNoto.framework.Sound;
import com.DaichiNoto.framework.gl.Animation;
import com.DaichiNoto.framework.gl.Font;
import com.DaichiNoto.framework.gl.ObjLoader;
import com.DaichiNoto.framework.gl.Texture;
import com.DaichiNoto.framework.gl.TextureRegion;
import com.DaichiNoto.framework.gl.Vertices3;

/**
 * アセット管理クラス
 * @author Daichi Noto
 *
 */
public class Assets {
	
	/**
	 * 画像アセット変数
	 */
	public static Texture m_background;
	public static TextureRegion m_backgroundRegion;
	public static Texture m_items;
	public static TextureRegion m_logoRegion;
	public static TextureRegion m_menuRegion;
	public static TextureRegion m_gameOverRegion;
	public static TextureRegion m_pauseRegion;
	public static TextureRegion m_settingsRegion;
	public static TextureRegion m_touchRegion;
	public static TextureRegion m_accelRegion;
	public static TextureRegion m_touchEnabledRegion;
	public static TextureRegion m_accelEnabledRegion;
	public static TextureRegion m_soundRegion;
	public static TextureRegion m_soundEnabledRegion;
	public static TextureRegion m_leftRegion;
	public static TextureRegion m_rightRegion;
	public static TextureRegion m_fireRegion;
	public static TextureRegion m_pauseButtonRegion;
	
	public static Font m_font;
	
	public static Texture m_explosionTexture;
	public static Animation m_explosionAnim;
	public static Vertices3 m_shipModel;
	public static Texture m_shipTexture;
	public static Vertices3 m_invaderModel;
	public static Texture m_invaderTexture;
	public static Vertices3 m_shotModel;
	public static Vertices3 m_shieldModel;
	
	/**
	 * 音のアセット変数
	 */
	public static Music m_music;
	public static Sound m_clickSound;
	public static Sound m_explosionSound;
	public static Sound m_shotSound;
	
	public static void Load(GameMain game){
		m_background = new Texture(game, "background.jpg", true);
		m_backgroundRegion = new TextureRegion(m_background, 0, 0, 480, 320);
		
		m_items = new Texture(game, "items.png", true);
		m_logoRegion = new TextureRegion(m_items, 0, 256, 384, 128);
		m_menuRegion = new TextureRegion(m_items, 0, 128, 224, 64);
		m_gameOverRegion = new TextureRegion(m_items, 224, 128, 128, 64);
		m_pauseRegion = new TextureRegion(m_items, 0, 192, 160, 64);
		m_settingsRegion = new TextureRegion(m_items, 0, 160, 224, 32);
		m_touchRegion = new TextureRegion(m_items, 0, 384, 64, 64);
		m_accelRegion = new TextureRegion(m_items, 64, 384, 64, 64);
		m_touchEnabledRegion = new TextureRegion(m_items, 0, 448, 64, 64);
		m_accelEnabledRegion = new TextureRegion(m_items, 64, 448, 64, 64);
		m_soundRegion = new TextureRegion(m_items, 128, 384, 64, 64);
		m_soundEnabledRegion = new TextureRegion(m_items, 190, 384, 64, 64);
		m_leftRegion = new TextureRegion(m_items, 0, 0, 64, 64);
		m_rightRegion = new TextureRegion(m_items, 64, 0, 64, 64);
		m_fireRegion = new TextureRegion(m_items, 128, 0, 64, 64);
		m_pauseButtonRegion = new TextureRegion(m_items, 0, 64, 64, 64);
		m_font = new Font(m_items, 224, 0, 16, 16, 20);
		
		m_explosionTexture = new Texture(game, "explode.png", true);
		TextureRegion[] keyFrames = new TextureRegion[16];
		int frame = 0;
		for (int y = 0; y < 256; y += 64) {
			for (int x = 0; x < 256; x += 64) {
				keyFrames[frame++] = new TextureRegion(m_explosionTexture, x, y, 64, 64);
			}
		}
		m_explosionAnim = new Animation(0.1f, keyFrames);			
		
		m_shipTexture = new Texture(game, "ship.png", true);
		m_shipModel = ObjLoader.Load(game, "ship.obj");
		m_invaderTexture = new Texture(game, "invader.png", true);
		m_invaderModel = ObjLoader.Load(game, "invader.obj");
		m_shieldModel = ObjLoader.Load(game, "shield.obj");
		m_shotModel = ObjLoader.Load(game, "shot.obj");
		
		
		m_music = game.getAudio().newMusic("music.mp3");
		m_music.setLooping(true);
		m_music.setVolume(0.5f);
		
		if(Settings.m_soundEnabled){
			m_music.play();
		}
		m_clickSound = game.getAudio().newSound("click.ogg");
		m_explosionSound = game.getAudio().newSound("explosion.ogg");
		m_shotSound = game.getAudio().newSound("shot.ogg");
	}
	
	/**
	 * ゲームを再開するとき読み直す関数
	 */
	public static void Reload(){
		m_background.Reload();
		m_items.Reload();
		m_explosionTexture.Reload();
		m_shipTexture.Reload();
		m_invaderTexture.Reload();
		
		if(Settings.m_soundEnabled){
			m_music.play();
		}
	}
	
	/**
	 * 音を再生する関数
	 * @param sound
	 */
	public static void PlaySound(Sound sound){
		if(Settings.m_soundEnabled){
			sound.play(1);
		}
		
	}
}
