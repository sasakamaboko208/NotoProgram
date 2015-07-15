using UnityEngine;
using System.Collections;

//タイトルシーンクラス
public class TitleSceneCtrl : MonoBehaviour {

	//メンバ変数
	Texture2D m_bgTexture;

	//コンストラクタと同じ扱い
	void Awake()
	{
		m_bgTexture = Resources.Load("2D Assets/Title") as Texture2D;
	}

	// Update is called once per frame
	void Update () {
	
	}

	//UI設定関数
	void OnGUI(){
		//スタイルの準備
		GUIStyle buttonStyle = new GUIStyle(GUI.skin.button);

		//対応解像度
		GUI.matrix = Matrix4x4.TRS(Vector3.zero,
		                           Quaternion.identity,
		                           new Vector3(Screen.width/ 854.0f, Screen.height / 480.0f, 1.0f)
			);

		//タイトル画面のテクスチャを表示
		GUI.DrawTexture(new Rect(0.0f, 0.0f, 854.0f, 480.0f), m_bgTexture);

		//スタートボタンを作成します
		if(GUI.Button(new Rect(327, 290, 200, 54), "Start", buttonStyle)){
			//シーンの切り替え
			Application.LoadLevel("GameScene");
		}
	}
}
