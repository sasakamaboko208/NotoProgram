package com.DaichiNoto.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.DaichiNoto.Main.GameMain;

/**
 * Verticesクラス
 * @author Daichi Noto
 *
 */
public class Vertices {
	final GameMain m_game;
	final boolean m_hasColor;
	final boolean m_hasTexCoords;
	final int m_vertexSize;
	final IntBuffer m_vertices;
    final int[] m_tmpBuffer;
	final ShortBuffer m_indices;
	
	/**
	 * コンストラクタ
	 * @param game
	 * @param maxVertices
	 * @param maxIndices
	 * @param hasColor
	 * @param hasTexCoords
	 */
	public Vertices(GameMain game, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
		m_game = game;
		m_hasColor = hasColor;
		m_hasTexCoords = hasTexCoords;
		m_vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4;
		m_tmpBuffer = new int[maxVertices * m_vertexSize / 4];
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * m_vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		m_vertices = buffer.asIntBuffer();
	
		if(maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			m_indices = buffer.asShortBuffer();
		} else {
			m_indices = null;
		}
	}
	
	/**
	 * バーティクスの設定
	 * @param vertices
	 * @param offset
	 * @param length
	 */
	public void SetVertices(float[] vertices, int offset, int length) {
		m_vertices.clear();
		int len = offset + length;
		for(int i=offset, j=0; i < len; i++, j++)
		m_tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		m_vertices.put(m_tmpBuffer, 0, length);
		m_vertices.flip();
	}

	/**
	 * 
	 * @param indices
	 * @param offset
	 * @param length
	 */
	public void SetIndices(short[] indices, int offset, int length) {
		m_indices.clear();
		m_indices.put(indices, offset, length);	
		m_indices.flip();
	}

	/**
	 * バインド処理関数
	 */
	public void Bind() {
	    GL10 gl = m_game.getGL10();
	    
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    m_vertices.position(0);
	    gl.glVertexPointer(2, GL10.GL_FLOAT, m_vertexSize, m_vertices);
	    
	    if(m_hasColor) {
	        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	        m_vertices.position(2);
	        gl.glColorPointer(4, GL10.GL_FLOAT, m_vertexSize, m_vertices);
	    }
	    
	    if(m_hasTexCoords) {
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        m_vertices.position(m_hasColor?6:2);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, m_vertexSize, m_vertices);
	    }
	}

	/**
	 * 描画処理
	 * @param primitiveType
	 * @param offset
	 * @param numVertices
	 */
	public void Draw(int primitiveType, int offset, int numVertices) {        
	    GL10 gl = m_game.getGL10();
	    
	    if(m_indices!=null) {
	        m_indices.position(offset);
	        gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, m_indices);
	    } else {
	        gl.glDrawArrays(primitiveType, offset, numVertices);
	    }        
	}

	/**
	 * 解放処理
	 */
	public void Unbind() {
	    GL10 gl = m_game.getGL10();
	    if(m_hasTexCoords)
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	    if(m_hasColor)
	        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
