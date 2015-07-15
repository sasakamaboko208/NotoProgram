using UnityEngine;
using System.Collections;

//ターゲットカーソルクラス
public class TargetCursor : MonoBehaviour {

	//半径
	float m_radius = 1.0f;

	//回転速度
	float m_angularVelocity = 480.0f;

	//目的地
	Vector3 m_destination = new Vector3(0.0f, 0.5f, 0.0f);

	//位置
	Vector3 m_position = new Vector3(0.0f, 0.5f, 0.0f);

	//角度
	float m_angle = 0.0f;

	// 初期化
	void Start () {
		// 初期位置を目的地に設定
		SetPosition( transform.position );
		m_position = m_destination;
	}

	//位置を設定する関数
	public void SetPosition(Vector3 iPosition)
	{
		m_destination = iPosition;
		// 高さは固定
		m_destination.y = 0.5f;
	}
	
	//更新処理関数
	void Update () {
		m_position += ( m_destination - m_position ) / 10.0f;
		// 回転角度
		m_angle += m_angularVelocity * Time.deltaTime;
		// オフセット位置
		Vector3 offset = Quaternion.Euler( 0.0f, m_angle, 0.0f ) * new Vector3( 0.0f, 0.0f, m_radius );
		// エフェクトの位置
		transform.localPosition =  m_position + offset;
	}
}
