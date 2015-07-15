using UnityEngine;
using System.Collections;

//アイテムの設定
public class DropItem : MonoBehaviour {

	//アイテムの種類
	public enum ItemKind
	{
		AttackUp,
		Heal,
	};

	public ItemKind m_kind;
	
	//初期化
	void Start () {
		Vector3 velocity = Random.insideUnitSphere * 2.0f + Vector3.up * 8.0f;
		rigidbody.velocity = velocity;
	}

	void OnTriggerEnter(Collider other){
		//Playerか判定
		if(other.tag == "Player"){
			CharacterStatus status = other.GetComponent<CharacterStatus>();
			status.GetItem(m_kind);
			// 取得したらアイテムを消す
			Destroy(gameObject);
		}
	}
}
