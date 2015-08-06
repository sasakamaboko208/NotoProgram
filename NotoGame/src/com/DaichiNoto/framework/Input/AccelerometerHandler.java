package com.DaichiNoto.framework.Input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 加速度センサーの処理クラス
 * @author Daichi Noto 2015 03/09
 *
 */
public class AccelerometerHandler implements SensorEventListener{
	/**
	 * メンバ変数
	 */
	
	private float m_accelX;
	private float m_accelY;
	private float m_accelZ;
	
	/**
	 * コンストラクタ
	 * @param context
	 */
	public AccelerometerHandler(Context context)
	{
		//加速度センサーが実際に使える状態にする
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
		//加速度センサーが使えるかチェック
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor accelerometer = manager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/**
	 * 加速度センサーの値を取得
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		m_accelX = event.values[0];
		m_accelY = event.values[1];
		m_accelZ = event.values[2];
		
	}
	
	/**
	 * 加速度X軸を返す
	 * @return
	 */
	public float getAccelX() {
		return m_accelX;
	}
	
	/**
	 * 加速度Y軸を返す
	 * @return
	 */
	public float getAccelY() {
		return m_accelY;
	}
	
	/**
	 * 加速度Z軸を返す
	 * @return
	 */
	public float getAccelZ() {
		return m_accelZ;
	}
}
