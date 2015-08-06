package com.DaichiNoto.framework.gl;

/**
 * フォントクラス
 * @author Daichi Noto
 *
 */
public class Font {
	public final Texture m_texture;
	public final int m_glyphWidth;
	public final int m_glyphHeight;
	public final TextureRegion[] m_glyphs = new TextureRegion[96];
	
	/**
	 * コンストラクタ
	 */
	public Font(Texture texture, int offsetX, int offsetY,
			int glyphsPerRow, int glyphWidth, int glyphHeight){
		m_texture = texture;
		m_glyphWidth = glyphWidth;
		m_glyphHeight = glyphHeight;
		
		int x = offsetX;
		int y = offsetY;
		for(int i = 0; i < 96; i++) {
			m_glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
			x += glyphWidth;
			if(x == offsetX + glyphsPerRow * glyphWidth) {
				x = offsetX;
				y += glyphHeight;
			}
		}
	}
	
	/**
	 * 文字の描画関数
	 * @param batcher
	 * @param text
	 * @param x
	 * @param y
	 */
	public void DrawText(SpriteBatcher batcher, String text, float x, float y) {
		int len = text.length();
		for(int i = 0; i < len; i++) {
			int c = text.charAt(i) - ' ';
			if(c < 0 || c > m_glyphs.length - 1)
				continue;

			TextureRegion glyph = m_glyphs[c];
			batcher.drawSprite(x, y, m_glyphWidth, m_glyphHeight, glyph);
			x += m_glyphWidth;
		}
	}
}
