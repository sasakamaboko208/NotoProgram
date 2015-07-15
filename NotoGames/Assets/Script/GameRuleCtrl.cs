using UnityEngine;
using System.Collections;

//ゲームルールクラス
public class GameRuleCtrl : MonoBehaviour {
	//残り時間
	float m_timeRemaining = 5.0f * 60.0f;

	//ゲームフラグ
	public const int GAMENOW   = 0x0000;
	public const int GAMEOVER  = 0x0001;
	public const int GAMECLEAR = 0x0002;

	int m_flag = GAMENOW;

	//シーン移行時間
	float m_sceneChangeTime = 3.0f;


	// 初期化
	void Start () {
		AudioManager.getInstance.LoadBgm("bgm","bgm_game_field");
		AudioManager.getInstance.PlayBgm("bgm");
		AudioManager.getInstance.LoadSe("gameClear", "se_game_clear");
	}
	
	//更新処理関数
	void Update () {
		//ゲーム終了ならシーン遷移
		if((m_flag == GAMEOVER) || (m_flag == GAMECLEAR)){
			m_sceneChangeTime -= Time.deltaTime;
			if(m_sceneChangeTime <= 0.0f){
				Application.LoadLevel("TitleScene");
			}
		}

		m_timeRemaining -= Time.deltaTime;
		//時間オーバー
		if(m_timeRemaining <= 0.0f){
			GameOver();
		}
	}

	//ゲームオーバー関数
	public void GameOver(){
		Debug.Log("ゲームオーバー");
		m_flag = GAMENOW | GAMEOVER;
	}

	//ゲームクリア関数
	public void GameClear(){
		Debug.Log("ゲームオーバー");
		m_flag = GAMENOW | GAMEOVER;
	}

	//現在の時間を取得
	public float getTimer(){
		return m_timeRemaining;
	}

	//ゲーム状態フラグ渡す関数
	public int getFlag(){
		return m_flag;
	}
}
