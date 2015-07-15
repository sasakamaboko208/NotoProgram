using UnityEngine;
using System.Collections;

//敵の行動クラス
public class EnemyCtrl : MonoBehaviour {
	CharacterStatus m_status;
	CharaAnimation m_charaAnimation;
	Transform m_attackTarget;
	CharacterMove m_charaMove;

	// 待機時間は２秒とする
	public float m_waitBaseTime = 2.0f;

	// 残り待機時間
	float m_waitTime;

	// 移動範囲５メートル
	public float m_walkRange = 5.0f;

	// 初期位置を保存しておく変数
	public Vector3 m_basePosition;

	// 複数のアイテムを入れれるように配列にしましょう。
	public GameObject[] m_dropItemPrefab;

	//攻撃を食らったとき
	GameObject m_hitEffect;
	
	// ステートの種類.
	enum State {
		Walking,	// 探索
		Chasing,	// 追跡
		Attacking,	// 攻撃
		Died,       // 死亡
	};
	
	State m_state = State.Walking;		// 現在のステート.
	State m_nextState = State.Walking;	// 次のステート.

	GameRuleCtrl m_gameRuleCtrl;

	void Awake(){
		m_hitEffect = (GameObject)Resources.Load("Prefabs/HitEffect");
	}
	
	//初期化
	void Start () {
		m_status = GetComponent<CharacterStatus>();
		m_charaAnimation = GetComponent<CharaAnimation>();
		m_charaMove = GetComponent<CharacterMove>();

		//初期位置の保持
		m_basePosition = transform.position;
		//待ち時間
		m_waitTime = m_waitBaseTime;

		m_gameRuleCtrl = FindObjectOfType<GameRuleCtrl>();
		AudioManager.getInstance.LoadSe("enemyAttack","se_enemy_attack");
		AudioManager.getInstance.LoadSe("wargDeath","se_warg_death");
		AudioManager.getInstance.LoadSe("dragonDeath","se_dragon_death");
	}
	
	//更新処理
	void Update () {
		switch(m_state){
		case State.Walking:
			Walking();
			break;
		case State.Chasing:
			Chasing();
			break;
		case State.Attacking:
			Attacking();
			break;
		}

		if(m_state != m_nextState){
			m_state = m_nextState;
			switch(m_state){
			case State.Walking:
				WalkStart();
				break;
			case State.Chasing:
				ChaseStart();
				break;
			case State.Attacking:
				AttackStart();
				break;
			case State.Died:
				Died();
				break;
			}
		}
	}

	//移動開始処理
	void WalkStart(){
		StateStartCommon();
	}

	//移動中の処理
	void Walking()
	{
		//待機中の場合
		if(m_waitTime > 0.0f){
			//待機時間を減らす
			m_waitTime -= Time.deltaTime;
			//待機時間が無くなったら
			if(m_waitTime <= 0.0f){
				//範囲内のどこか
				Vector2 randomValue = Random.insideUnitCircle * m_walkRange;

				//移動先の設定
				Vector3 destinationPos = m_basePosition + new Vector3(randomValue.x, 0.0f, randomValue.y);

				//目的地の設定
				m_charaMove.SetDestination(destinationPos);
			}
		}else{
			//目的地へ到着
			if(m_charaMove.Arrived()){
				//待機状態へ
				m_waitTime = Random.Range(m_waitBaseTime, m_waitTime * 2.0f);
			}

			// ターゲットを発見したら追跡
			if (m_attackTarget)
			{
				ChangeState(State.Chasing);
			}
		}
	}

	//追跡開始
	void ChaseStart(){
		StateStartCommon();
	}

	//追跡中
	void Chasing(){
		//移動先をプレイヤーに設定
		m_charaMove.SetDestination(m_attackTarget.position);
		//２m以内に近づいたら攻撃
		if(Vector3.Distance(m_attackTarget.position, transform.position) <= 2.0f){
			ChangeState(State.Attacking);
		}
	}

	// 攻撃ステートが始まる前に呼び出される.
	void AttackStart()
	{
		StateStartCommon();
		m_status.attacking = true;
		
		// 敵の方向に振り向かせる.
		Vector3 targetDirection = (m_attackTarget.position-transform.position).normalized;

		m_charaMove.SetDirection(targetDirection);
		
		// 移動を止める.
		m_charaMove.StopMove();
		AudioManager.getInstance.PlaySe("enemyAttack");
	}

	//攻撃処理
	void Attacking(){
		if(m_charaAnimation.IsAttacked()){

			ChangeState(State.Walking);
			//待機時間を設定
			m_waitTime = Random.Range(m_waitBaseTime, m_waitTime * 2.0f);

			//ターゲットをリセットする
			m_attackTarget = null;
		}
	}

	//アイテムを生成する関数
	void DropItem(){
		if(m_dropItemPrefab.Length == 0){
			return;
		}

		GameObject dropItem = m_dropItemPrefab[Random.Range(0,
		                       m_dropItemPrefab.Length)];

		Instantiate(dropItem, transform.position, Quaternion.identity);
	}

	//敵の死亡処理関数
	void Died(){
		m_status.died = true;
		DropItem();
		Destroy(gameObject);

		if(gameObject.tag == "Boss"){
			AudioManager.getInstance.PlaySe("wargDeath");
			m_gameRuleCtrl.GameClear();
		}else{
			AudioManager.getInstance.PlaySe("dragonDeath");
		}
	}

	//ダメージ
	void Damage(AttackArea.AttackInfo attackInfo)
	{
		//エフェクト生成と消滅
		GameObject effect = Instantiate(m_hitEffect, transform.position,
		                                Quaternion.identity) as GameObject;

		effect.transform.localPosition = transform.position + new Vector3(0.0f, 0.5f, 0.0f);
		Destroy(effect, 0.3f);

		m_status.HP -= attackInfo.attackPower;

		if (m_status.HP <= 0) {
			m_status.HP = 0;
			// 体力０なので死亡
			ChangeState(State.Died);
		}
	}

	// ステートが始まる前にステータスを初期化する.
	void StateStartCommon()
	{
		m_status.attacking = false;
		m_status.died = false;
	}

	// ステートを変更する.
	void ChangeState(State nextState)
	{
		m_nextState = nextState;
	}

	// 攻撃対象を設定する
	public void SetAttackTarget(Transform target)
	{
		m_attackTarget = target;
	}
}
