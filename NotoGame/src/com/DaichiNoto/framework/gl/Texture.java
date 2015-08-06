package com.DaichiNoto.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLUtils;

import com.DaichiNoto.Main.GameMain;
import com.DaichiNoto.framework.FileIO;

public class Texture {
	/**
	 * メンバ変数の宣言
	 */
	GameMain m_gameMain;
	FileIO m_fileIO;
	String m_fileName;
	int m_textureId;
	int m_minFilter;
	int m_magFilter;
	int m_width;
	int m_height;
	boolean m_mipmapped;
	
	/**
	 * コンストラクタ
	 * @param game
	 * @param fileName
	 * @param m_mipmapped
	 */
	public Texture(GameMain game, String fileName, boolean mipmapped){
		m_gameMain = game;
		m_fileIO = game.getFileIO();
		m_fileName = fileName;
		m_mipmapped = mipmapped;
		Load();
	}
	
	/**
	 * テクスチャファイルの読み込み関数
	 */
	private void Load(){
		GL10 gl = m_gameMain.getGL10();
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		m_textureId = textureIds[0];
		
		InputStream in = null;
		try{
			in = m_fileIO.readAsset(m_fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			if(m_mipmapped){
				CreateMipmaps(gl, bitmap);
			}else{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_textureId);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
				setFilter(GL10.GL_NEAREST, GL10.GL_NEAREST);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
				m_width = bitmap.getWidth();
				m_height = bitmap.getHeight();
				bitmap.recycle();
			}
		}catch(IOException e){
			throw new RuntimeException("Couldn't load texture '" + m_fileName + "'", e);
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	/**
	 * テクスチャを作成する関数
	 * @param gl
	 * @param bitmap
	 */
	private void CreateMipmaps(GL10 gl, Bitmap bitmap){
		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_textureId);
		m_width = bitmap.getWidth();
		m_height = bitmap.getHeight();
		setFilter(GL10.GL_LINEAR_MIPMAP_NEAREST, GL10.GL_LINEAR);
		
		int level = 0;
		int newWidth = m_width;
		int newHeight = m_height;
		
		while(true){
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			newWidth = newWidth / 2;
			newHeight = newHeight / 2;
			
			if(newWidth <= 0) break;
			
			Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());
			
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(bitmap,
					new Rect(0,0, bitmap.getWidth(),bitmap.getHeight()),
					new Rect(0, 0, newWidth, newHeight), null);
			bitmap.recycle();
			bitmap = newBitmap;
			level++;
		}
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		bitmap.recycle();
	}
	
	/**
	 * 再読み込み関数
	 */
	public void Reload(){
		Load();
		Bind();
		setFilter(m_minFilter, m_magFilter);
		m_gameMain.getGL10().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * テクスチャのバインド処理
	 */
	public void Bind(){
		GL10 gl = m_gameMain.getGL10();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_textureId);
	}
	
	/**
	 * フィルターをセットする関数
	 */
	public void setFilter(int minFilter, int magFilter){
		m_minFilter = minFilter;
		m_magFilter = magFilter;
		GL10 gl = m_gameMain.getGL10();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	/**
	 * 削除処理
	 */
	public void Dispose(){
		GL10 gl = m_gameMain.getGL10();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_textureId);
		int[] textureIds = { m_textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}
	
	/**
	 * 画像の幅を取得
	 * @return
	 */
	public int getWidth(){
		return m_width;
	}
	
	/**
	 * 画像の高さを取得
	 * @return
	 */
	public int getHeight(){
		return m_height;
	}
}
