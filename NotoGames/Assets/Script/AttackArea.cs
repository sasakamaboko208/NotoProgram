using UnityEngine;
using System.Collections;

public class AttackArea : MonoBehaviour {
	CharacterStatus status;

	// Use this for initialization
	void Start () {
		status = transform.root.GetComponent<CharacterStatus>();
	}

	public class AttackInfo{
		public int attackPower; //攻撃力
		public Transform attacker;	//攻撃者
	}

	//攻撃情報の取得
	AttackInfo GetAttackInfo(){
		AttackInfo attackInfo = new AttackInfo();
		//攻撃力の計算
		attackInfo.attackPower = status.Power;
		attackInfo.attacker = transform.root;

		return attackInfo;
	}

	//当たった
	void OnTriggerEnter(Collider other){
		//攻撃が当たった相手にDamageメッセージを送る
		other.SendMessage("Damage", GetAttackInfo());

		//攻撃対象を保存
		status.lastAttackTarget = other.transform.root.gameObject;
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
