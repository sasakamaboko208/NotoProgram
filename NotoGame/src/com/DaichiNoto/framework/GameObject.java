package com.DaichiNoto.framework;

import com.DaichiNoto.framework.math.Rectangle;
import com.DaichiNoto.framework.math.Vector2;

/**
 * ゲームオブジェクト（静的）
 * @author Daichi Noto
 *
 */
public class GameObject {
	private final Vector2 m_position;
	private final Rectangle m_bounds;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GameObject(float x, float y, float width, float height) {
		m_position = new Vector2(x,y);
		m_bounds = new Rectangle(x-width/2, y-height/2, width, height);
	}
	
	/**
	 * 位置を取得
	 * @return
	 */
	public Vector2 getPosition(){
		return m_position;
	}
	
	/**
	 * 範囲を取得
	 * @return
	 */
	public Rectangle getBound(){
		return m_bounds;
	}
}
