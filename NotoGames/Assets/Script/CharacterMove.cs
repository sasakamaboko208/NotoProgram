using UnityEngine;
using System.Collections;

//キャラクターを移動させるクラス
public class CharacterMove : MonoBehaviour {
	
	//重力値
	const float GravityPower = 9.8f;
	//目的地についたとみなす停止距離
	const float StoppingDistance = 0.6f;
	
	//現在の移動速度
	Vector3 velocity = Vector3.zero;
	
	//キャラクターコントロールのキャッシュ
	CharacterController characterController;
	
	//到着したかどうか
	public bool arrived = false;
	
	bool forceRotate = false;
	
	//向きを強制的に指示する
	Vector3 forceRotateDirection;
	
	//目的地
	public Vector3 destination;
	
	//移動速度
	public float walkSpeed = 6.0f;
	
	//回転速度
	public float rotationSpeed = 360.0f;
	
	// Use this for initialization
	//初期化関数
	void Start () {
		characterController = GetComponent<CharacterController>();
		destination = transform.position;
	}
	
	// Update is called once per frame
	//更新処理関数
	void Update () {
		//移動速度velocityを更新
		if(characterController.isGrounded)
		{
			Vector3 destinationXZ = destination;
			destinationXZ.y = transform.position.y;	//高さを目的地と現在地を同じにしておく
			
			//*************************ここからXZのみで考える**************************************
			//目的地までの距離と方角を求める
			Vector3 direction = (destinationXZ - transform.position).normalized;
			float distance = Vector3.Distance(transform.position,destinationXZ);
			
			//現在の速度を退避
			Vector3 currentVelocity = velocity;
			
			//目的地に近づいたら到着
			if(arrived || distance < StoppingDistance)
			{
				arrived = true;
			}
			
			//移動速度を求める
			if(arrived){
				velocity = Vector3.zero;
			}else{
				velocity = direction * walkSpeed;
			}
			
			//スムーズに補間(二点間の位置を返す)
			velocity = Vector3.Lerp(currentVelocity, velocity,Mathf.Min(Time.deltaTime * 5.0f, 1.0f));
			
			velocity.y = 0;
			
			if(!forceRotate){
				//向きを行きたい方向に向ける
				if(velocity.magnitude > 0.1f && !arrived){
					Quaternion characterTargetRotation = Quaternion.LookRotation(direction);
					transform.rotation = Quaternion.RotateTowards(transform.rotation, 
					                                              characterTargetRotation,
					                                              rotationSpeed * Time.deltaTime);
				}
            }else{
                //強制向き指定
                Quaternion characterTargetRotation = Quaternion.LookRotation(forceRotateDirection);
                transform.rotation = Quaternion.RotateTowards(transform.rotation,
                                                              characterTargetRotation,
                                                              rotationSpeed * Time.deltaTime);
            }
		}

		//重力
		velocity += Vector3.down * GravityPower * Time.deltaTime;
		
		//着地していたら
		Vector3 snapGround = Vector3.zero;
		if(characterController.isGrounded) snapGround = Vector3.down;
		
		characterController.Move(velocity * Time.deltaTime+snapGround);
		if(characterController.velocity.magnitude < 0.1f){
			arrived = true;
		}
		
		if(forceRotate && Vector3.Dot(transform.forward,forceRotateDirection) > 0.99f){
			forceRotate = false;
		}
	}
	
	public void SetDestination(Vector3 destination){
		arrived = false;
		this.destination = destination;
	}
	
	public void SetDirection(Vector3 direction){
		forceRotateDirection = direction;
		forceRotateDirection.y = 0;
		forceRotateDirection.Normalize();
		forceRotate = true;
	}
	
	public void StopMove(){
		destination = transform.position;
	}
	
	public bool Arrived(){
		return arrived;
	}
}
