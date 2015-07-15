using UnityEngine;
using System.Collections;

//キャラクターのステータスを表示クラス
public class CharacterStatusGui : MonoBehaviour {

	float m_baseWidth = 854f;
	float m_baseHeight = 480f;

	//ステータス
	CharacterStatus m_playerStatus;
	Vector2 m_playerStatusOffset = new Vector2(8.0f, 80.0f);

	//名前
	Rect m_nameRect = new Rect(0.0f, 0.0f, 120.0f, 24.0f);
	GUIStyle m_nameLabelStyle;

	//ライフバー
	Texture m_backLifeBarTexture;
	Texture m_frontLifeBarTexture;

	float m_frontLifeBarOffsetX = 2.0f;
	float m_lifeBarTextureWidth = 128.0f;
	
	Rect m_playerLifeBarRect = new Rect(0.0f, 0.0f, 128.0f, 16.0f);
	Color m_playerFrontLifeBarColor = Color.green;
	Rect m_enemyLifeBarRect = new Rect(0.0f, 0.0f, 128.0f, 24.0f);
	Color m_enemyFrontLifeBarColor = Color.red;

	//Awakeでの初期化
	void Awake(){
		//画像の読み込み
		m_backLifeBarTexture = Resources.Load("2D Assets/BackLifeBar") as Texture;
		m_frontLifeBarTexture = Resources.Load("2D Assets/FrontLifeBar") as Texture;

		PlayerCtrl playerCtrl = GameObject.FindObjectOfType(typeof(PlayerCtrl)) as PlayerCtrl;
		m_playerStatus = playerCtrl.GetComponent<CharacterStatus>();

		//名前関連の初期化
		m_nameLabelStyle = new GUIStyle();

	}

	//GUI処理関数
	void OnGUI(){
		//解像度対応
		GUI.matrix = Matrix4x4.TRS(
			Vector3.zero,
			Quaternion.identity,
			new Vector3(Screen.width / m_baseWidth, Screen.height / m_baseHeight, 1f));

		//ステータス
		DrawPlayerStatus();
		DrawEnemyStatus();

	}


	//初期化
	void Start(){
		m_nameLabelStyle.normal.textColor = Color.white;
		m_nameLabelStyle.fontSize = 20;
		m_nameLabelStyle.alignment = TextAnchor.UpperRight;
	}

	//

	//キャラクターステータスの描画関数
	void DrawCharacterStatus(float x, float y, CharacterStatus status,
	                         Rect bar_rect, Color front_color){

		//名前
		GUI.Label(new Rect(x, y, m_nameRect.width, m_nameRect.height),
		          status.characterName,
		          m_nameLabelStyle);

		float lifeValue = (float)status.HP / status.MaxHP;

		//背面バー
		if(m_backLifeBarTexture != null){
			y += m_nameRect.height;
			GUI.DrawTexture(new Rect(x, y, bar_rect.width, bar_rect.height), 
			                m_backLifeBarTexture);
		}

		//前面バー
		if(m_frontLifeBarTexture != null){
			float resizeFrontBarOffsetX = m_frontLifeBarOffsetX *
				bar_rect.width / m_lifeBarTextureWidth;
			float front_bar_width = bar_rect.width - resizeFrontBarOffsetX * 2;
			var guiColor = GUI.color;

			GUI.color = front_color;
			GUI.DrawTexture(new Rect(x + resizeFrontBarOffsetX, y, front_bar_width * lifeValue, bar_rect.height), m_frontLifeBarTexture);
			GUI.color = guiColor;
		}

	}

	//プレイヤーステータスの描画関数
	void DrawPlayerStatus(){
		float x = m_baseWidth - m_playerLifeBarRect.width - 
			m_playerStatusOffset.x;
		float y = m_playerStatusOffset.y;
		DrawCharacterStatus(x, y, m_playerStatus, m_playerLifeBarRect,
		                    m_playerFrontLifeBarColor);


	}

	//敵ステータスの描画
	void DrawEnemyStatus(){
		if (m_playerStatus.lastAttackTarget != null)
		{
			CharacterStatus targetStatus = m_playerStatus.lastAttackTarget.GetComponent<CharacterStatus>();
			DrawCharacterStatus(
				(m_baseWidth - m_enemyLifeBarRect.width) / 2.0f, 0f,
				targetStatus,
				m_enemyLifeBarRect,
				m_enemyFrontLifeBarColor);
		}
	}
}
