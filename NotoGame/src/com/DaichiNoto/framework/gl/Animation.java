package com.DaichiNoto.framework.gl;

/**
 * アニメーションクラス
 * @author Daichi Noto
 *
 */
public class Animation {
	/**
	 * メンバ変数
	 */
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;

	final TextureRegion[] m_keyFrames;
	final float m_frameDuration;
	
	/**
	 * コンストラクタ
	 * @param frameDuration
	 * @param keyFrames
	 */
	public Animation(float frameDuration, TextureRegion ... keyFrames) {
		m_frameDuration = frameDuration;
		m_keyFrames = keyFrames;
	}

	/**
	 * 現在のアニメーションのキーフレームを取得
	 * @param stateTime
	 * @param mode
	 * @return
	 */
	public TextureRegion getKeyFrame(float stateTime, int mode) {
		int frameNumber = (int)(stateTime / m_frameDuration);

		if(mode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(m_keyFrames.length-1, frameNumber);
		} else {
		frameNumber = frameNumber % m_keyFrames.length;
		}
		return m_keyFrames[frameNumber];
	}
}
