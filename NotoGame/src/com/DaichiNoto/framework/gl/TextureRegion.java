package com.DaichiNoto.framework.gl;

/**
 * テクスチャのクリッピングクラス
 * @author Daichi Noto
 *
 */
public class TextureRegion {
	public final float m_u1, m_v1, m_u2, m_v2;
	public final Texture m_texture;
	
	public TextureRegion(Texture texture, float x, float y, float width, float height){
		m_u1 = x / texture.getWidth();
		m_v1 = y / texture.getHeight();
		m_u2 = m_u1 + width / texture.getWidth();
		m_v2 = m_v1 + height / texture.getHeight();
		
		m_texture = texture;
	}
}
