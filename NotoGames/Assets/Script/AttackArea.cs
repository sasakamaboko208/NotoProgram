using UnityEngine;
using System.Collections;

public class AttackArea : MonoBehaviour {
	CharacterStatus m_status;

	// Use this for initialization
	void Start () {
		m_status = transform.root.GetComponent<CharacterStatus>();
		AudioManager.getInstance.LoadSe("enemyAttackHit","se_enemy_attack_hit");
		AudioManager.getInstance.LoadSe("playerAttackHit","se_player_attack_hit");
	}

	public class AttackInfo{
		public int attackPower; //攻撃力
		public Transform attacker;	//攻撃者
	}

	//攻撃情報の取得
	AttackInfo GetAttackInfo(){
		AttackInfo attackInfo = new AttackInfo();
		//攻撃力の計算
		attackInfo.attackPower = m_status.Power;

		//攻撃力の強化
		if(m_status.getBoostFlg()){
			attackInfo.attackPower += attackInfo.attackPower;
		}

		attackInfo.attacker = transform.root;

		return attackInfo;
	}

	//当たった
	void OnTriggerEnter(Collider other){
		if(other.tag == "Player"){
			AudioManager.getInstance.PlaySe("enemyAttackHit");
		}else{
			AudioManager.getInstance.PlaySe("playerAttackHit");
		}
		//攻撃が当たった相手にDamageメッセージを送る
		other.SendMessage("Damage", GetAttackInfo());

		//攻撃対象を保存
		m_status.lastAttackTarget = other.transform.root.gameObject;
	}

	//攻撃判定を有効にする
	void OnAttack(){
		collider.enabled = true;
	}

	//攻撃判定を無効にする
	void OnAttackTermination(){
		collider.enabled = false;
	}
}
