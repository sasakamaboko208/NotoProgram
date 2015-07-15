using UnityEngine;
using System.Collections;

public class CharacterStatus : MonoBehaviour {

	//体力
	public int HP = 100;
	public int MaxHP = 100;

	//攻撃力
	public int Power = 10;

	//最後に攻撃した対象
	public GameObject lastAttackTarget = null;

	//キャラクターの名前
	public string characterName = "Player";

	//状態
	public bool attacking = false;
	public bool died = false;

	//攻撃力強化
	bool m_powerBoost = false;

	//攻撃強化時間
	float m_powerBoostTime = 0.0f;

	//パワーアップエフェクト
	ParticleSystem m_powerUpEffect;

	// Use this for initialization
	void Start () {
		if(gameObject.tag == "Player"){
			m_powerUpEffect = transform.Find("PowerUpEffect").GetComponent<ParticleSystem>();
		}
	}

	//更新処理
	void Update () {
		if(gameObject.tag != "Player"){
			return;
		}

		m_powerBoost = false;
		if(m_powerBoostTime > 0.0f){
			m_powerBoost = true;
			m_powerBoostTime = Mathf.Max (m_powerBoostTime -Time.deltaTime, 0.0f);
		}else{
			m_powerUpEffect.Stop();
		}
	}
	
	//アイテム取得
	public void GetItem(DropItem.ItemKind itemKind){
		switch(itemKind){
		case DropItem.ItemKind.AttackUp:
			m_powerBoostTime = 5.0f;
			m_powerUpEffect.Play();
			break;
		case DropItem.ItemKind.Heal:
			//MaxHPの半分回復
			HP = Mathf.Min(HP + MaxHP / 2, MaxHP);
			break;
		}
	}

	//ブーストフラグ
	public bool getBoostFlg(){
		return m_powerBoost;
	}
}
