using UnityEngine;
using System.Collections;

//タイマークラス
public class GameTimerGui : MonoBehaviour {
	//メンバ変数設定
	GameRuleCtrl m_gameRuleCtrl;

	float baseWidth = 854f;
	float baseHeight = 480f;

	Texture m_timerIcon;
	GUIStyle m_timerLabelStyle;


	void Awake(){
		m_gameRuleCtrl = GameObject.FindObjectOfType(typeof(GameRuleCtrl)) as GameRuleCtrl;
		//m_gameRuleCtrl = FindObjectOfType<GameRuleCtrl>();
	}

	//初期化
	void Start(){
		m_timerIcon = Resources.Load("2D Assets/TimerIcon") as Texture;
		m_timerLabelStyle = new GUIStyle();
		m_timerLabelStyle.normal.textColor = Color.white;
		m_timerLabelStyle.fontSize = 30;
	}

	//GUIの処理関数
	void OnGUI()
	{
		// 解像度対応.
		GUI.matrix = Matrix4x4.TRS(
			Vector3.zero,
			Quaternion.identity,
			new Vector3(Screen.width / baseWidth, Screen.height / baseHeight, 1f));
		
		// タイマー.
		GUI.Label(
			new Rect(8f, 8f, 128f, 48f),
			new GUIContent(m_gameRuleCtrl.getTimer().ToString("0"), m_timerIcon),
			m_timerLabelStyle);
	}
}
