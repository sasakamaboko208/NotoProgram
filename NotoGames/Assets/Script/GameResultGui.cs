using UnityEngine;
using System.Collections;

public class GameResultGui : MonoBehaviour
{
	GameRuleCtrl m_gameRuleCtrl;

	float m_baseWidth = 854f;
	float m_baseHeight = 480f;

	Texture2D m_gameOverTexture;
	Texture2D m_gameClearTexture;
	
	void Awake()
	{
		m_gameRuleCtrl = GameObject.FindObjectOfType(typeof(GameRuleCtrl)) as GameRuleCtrl;

		m_gameOverTexture = Resources.Load("2D Assets/FontGameOver") as Texture2D;
		m_gameClearTexture = Resources.Load("2D Assets/FontCongratulation") as Texture2D;

	}
	
	void OnGUI()
	{
		Texture2D m_aTexture;
		if( m_gameRuleCtrl.getFlag().Equals(GameRuleCtrl.GAMECLEAR ))
		{
			m_aTexture = m_gameClearTexture;
		}
		else if( m_gameRuleCtrl.getFlag().Equals(GameRuleCtrl.GAMEOVER) )
		{
			m_aTexture = m_gameOverTexture;
		}
		else
		{
			return;
		}

		// 解像度対応.
		GUI.matrix = Matrix4x4.TRS(
			Vector3.zero,
			Quaternion.identity,
			new Vector3(Screen.width / m_baseWidth, Screen.height / m_baseHeight, 1f));
		
		// リザルト.
		GUI.DrawTexture(new Rect(0.0f, 208.0f, 854.0f, 64.0f), m_aTexture);
	}
}
