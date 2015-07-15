using UnityEngine;
using System.Collections;

//範囲内入ったプレイヤーを攻撃対象にするクラス
public class SeachArea : MonoBehaviour {
	EnemyCtrl m_enemyCtrl;

	//初期化
	void Start () {
		//コンポーネントを検索
		m_enemyCtrl = transform.root.GetComponent<EnemyCtrl>();
	}

	//他のコライダが接触時の処理関数
	void OnTriggerStay(Collider other){
		//Playerタグをターゲットにする
		if(other.tag == "Player"){
			m_enemyCtrl.SetAttackTarget(other.transform);
		}
	}
}
