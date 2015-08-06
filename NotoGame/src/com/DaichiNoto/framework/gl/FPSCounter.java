package com.DaichiNoto.framework.gl;

import android.util.Log;

/**
 * FPSカウンタークラス
 * @author Daichi Noto
 *
 */
public class FPSCounter {
	long m_startTime = System.nanoTime();
	int m_frames = 0;

	/**
	 * カウンターを表示
	 */
	public void logFrame() {
		m_frames++;
		if(System.nanoTime() - m_startTime >= 1000000000) {
			Log.d("FPSCounter", "fps: " + m_frames);
			m_frames = 0;
			m_startTime = System.nanoTime();
		}
	}			
}
