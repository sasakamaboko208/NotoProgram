using UnityEngine;
using System.Collections;

//キャラクターの攻撃判定クラス
public class AttackAreaActivator : MonoBehaviour {
	private Collider[] m_attackAreaColliders; //攻撃判定コライダの配列

	//初期化
	void Start () {
		//子供のGameObjectからAttackAreaスクリプトがついているのでGameObjectを
		//探す
		AttackArea[] attackAreas = GetComponentsInChildren<AttackArea>();
		m_attackAreaColliders = new Collider[attackAreas.Length];

		for(int i = 0; i < attackAreas.Length; i++)
		{
			//AttackAreaスクリプトがついているのGameObjectのコライダを配列に格納する
			m_attackAreaColliders[i] = attackAreas[i].collider;
			m_attackAreaColliders[i].enabled = false;
		}
	}

	//アニメーションイベントのStartAttackHitを受け取ってコライダを有効にする
	void StartAttackHit(){
		foreach(Collider attackAreaCollider in m_attackAreaColliders){
			attackAreaCollider.enabled = true;
		}
	}

	//アニメーションイベントのEndAttackHitを受け取ってコライダを無効にする
	void EndAttackHit(){
		foreach(Collider attackAreaCollider in m_attackAreaColliders){
			attackAreaCollider.enabled = false;
		}
	}
}
