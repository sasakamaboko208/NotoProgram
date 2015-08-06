package com.DaichiNoto.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.DaichiNoto.framework.FileIO;

/**
 * 設定クラス
 * @author Daichi Noto
 *
 */
public class Settings {
	public static boolean m_soundEnabled = true;
	public static boolean m_touchEnabled = true;
	public final static String file = ".droidinvaders";
	
	/**
	 * SDカードから設定ロードする関数
	 * @param files
	 */
	public static void Load(FileIO files) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(files.readFile(file)));
			m_soundEnabled = Boolean.parseBoolean(in.readLine());
			m_touchEnabled = Boolean.parseBoolean(in.readLine());
		} catch (IOException e) {
			// デフォルト設定があるのでエラーは無視
		} catch (NumberFormatException e) {
			// 同上
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * SDカードに設定を記録する関数 
	 * @param files
	 */
	public static void Save(FileIO files){
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(file)));
			out.write(Boolean.toString(m_soundEnabled));
			out.write("\n");
			out.write(Boolean.toString(m_touchEnabled));
		} catch (IOException e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}
}
