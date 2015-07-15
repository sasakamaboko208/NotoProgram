using UnityEngine;
using System.Collections;

//敵の管理クラス
public class EnemyManagerCtrl : MonoBehaviour {
	//敵のプレハブ
	public GameObject m_enemyPrefab;

	//敵を格納
	GameObject[] m_exisyEnemy;

	//生成される最大数
	public int MAX_ENEMY = 2;

	//初期化
	void Start () {
		//配列確保
		m_exisyEnemy = new GameObject[MAX_ENEMY];
		//周期的に実行したい
		StartCoroutine(Exec());
	
	}

	IEnumerator Exec(){
		while(true){
			Generate();
			yield return new WaitForSeconds(3.0f);
		}
	}

	void Generate(){
		for(int i = 0; i < m_exisyEnemy.Length; i++){
			if(m_exisyEnemy[i] == null){
				m_exisyEnemy[i] = Instantiate(m_enemyPrefab,
				                              transform.position,
				                              transform.rotation) as GameObject;
				return;
			}
		}
	}
}
