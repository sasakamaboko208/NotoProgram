using UnityEngine;
using System.Collections;

public class PlayerCtrl : MonoBehaviour
{
	const float RayCastMaxDistance = 100.0f;
	CharacterStatus m_status;
	CharaAnimation m_charaAnimation;
	Transform attackTarget;
	InputManager inputManager;
	public float attackRange = 1.5f;

	//状態の種類
	enum State{
		Walking,
		Attacking,
		Died,
	};

	State m_state = State.Walking;		//現在のステート
	State m_nextState = State.Walking;	//次のステート


	// Use this for initialization
	void Start ()
	{
		m_status = GetComponent<CharacterStatus>();
		m_charaAnimation = GetComponent<CharaAnimation>();
		inputManager  = FindObjectOfType<InputManager>();
	}
	
	// Update is called once per frames
	void Update ()
	{
		switch(m_state){
		case State.Walking:
			Walking();
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
			case State.Attacking:
				AttackStart();
				break;
			case State.Died:
				Died();
				break;
			}
		}
	}

	void StateStartCommon(){
		m_status.attacking = false;
		m_status.died = false;
	}

	//ステートを変更する関数
	void ChangeState(State nextState){
		m_nextState = nextState;
	}

	//攻撃が始まる前に呼び出す
	void AttackStart(){
		StateStartCommon();
		m_status.attacking = true;

		//敵の方に振り向かせる
		Vector3 targetDirection = (attackTarget.position - transform.position).normalized;
		SendMessage("SetDirection",targetDirection);

		//移動を止める
		SendMessage("StopMove");
	}

	void Attacking(){
		if(m_charaAnimation.IsAttacked()){
			ChangeState(State.Walking);
		}
	}

	void Died(){
		m_status.died = true;
	}

	void WalkStart(){
		StateStartCommon();
	}

	void Walking(){
		if(inputManager.Clicked()){
			
			Ray ray = Camera.main.ScreenPointToRay(inputManager.GetCursorPosition());
			
			RaycastHit hitInfo;
			
			if(Physics.Raycast(ray,out hitInfo,RayCastMaxDistance,(1 << LayerMask.NameToLayer("Ground"))
			   | (1<<LayerMask.NameToLayer("EnemyHit")))){

				//地面がクリックされたか
				if(hitInfo.collider.gameObject.layer == LayerMask.NameToLayer("Ground")){
					SendMessage("SetDestination",hitInfo.point);
				}

				//敵がクリックされた
				if(hitInfo.collider.gameObject.layer == LayerMask.NameToLayer("EnemyHit")){
					//水平距離をチェックして攻撃する
					Vector3 hitPoint = hitInfo.point;
					hitPoint.y = transform.position.y;
					float distance = Vector3.Distance(hitPoint,transform.position);

					if(distance < attackRange){
						//攻撃
						attackTarget = hitInfo.collider.transform;
						ChangeState(State.Attacking);
					}else{
						SendMessage("SetDestination",hitInfo.point);
					}
				}
			}
		}
	}

	void Damage(AttackArea.AttackInfo attackInfo){
		m_status.HP -= attackInfo.attackPower;
		if(m_status.HP <= 0){
			m_status.HP = 0;

			ChangeState(State.Died);
		}
	}
}

