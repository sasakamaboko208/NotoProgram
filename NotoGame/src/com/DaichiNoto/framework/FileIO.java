package com.DaichiNoto.framework;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

/**
 * 
 * FileIOクラス
 * 外部ファイルの書き込みと読み込み
 * @author Daichi Noto 2015 03/07
 *
 */

public class FileIO {
	/**
	 * メンバ変数
	 */
	AssetManager m_assets;			//アセット管理するメンバ変数
	String m_externalStoragePath;	//パスを確保するメンバ変数
	
	/**
	 * コンストラクタ
	 * @param assets アセットを取得
	 */
	public FileIO(AssetManager assets){
		this.m_assets = assets;
		//SDカードからパスを取得
		this.m_externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
	}
	
	/**
	 * アセットファイルを開ける関数
	 * @param fileName	ファイル名の取得
	 * @return　送られてきたファイル名ののデータを送る
	 * @throws IOException　ファイル入出力時に起きりうる例外
	 */
	public InputStream readAsset(String fileName) throws IOException {
		return m_assets.open(fileName);
	}
	
	/**
	 * バイナリーファイル読み込む為の関数
	 * @param fileName ファイル名の取得
	 * @return	バイナリ形式で返す
	 * @throws IOException　ファイル入出力時に起きりうる例外
	 */
	public InputStream readFile(String fileName) throws IOException{
		return new FileInputStream(fileName);
		
	}
	
	/**
	 * バイナリーファイル書き込む為の関数
	 * @param fileName ファイル名の取得
	 * @return	バイナリ形式で返す
	 * @throws IOException　ファイル入出力時に起きりうる例外
	 */
	public OutputStream writeFile(String fileName) throws IOException{
		return new FileOutputStream(fileName);
	}
}
