using UnityEngine;
using System.Collections;

//キャラクター入力クラス
public class InputManager : MonoBehaviour {
	Vector2 slideStartPosition;
	Vector2 prevPosition;
	Vector2 delta = Vector2.zero;
	bool moved = false;

	// Update is called once per frame
	void Update () {
		//スライド開始時点
		if(Input.GetButtonDown("Fire1")){
			slideStartPosition = Input.mousePosition;
		}

		//画面の一割以上移動させたらスライド開始と判断する
		if(Input.GetButton("Fire1")){
			if(Vector2.Distance(slideStartPosition, Input.mousePosition) >= (Screen.width * 0.1f)){
				moved = true;
			}
		}

		//スライド操作が終了したか
		if(!Input.GetButtonUp("Fire1") && !Input.GetButton("Fire1")){
			moved = false;
		}

		//移動量を求める
		if(moved){
			delta = (Vector2)(Input.mousePosition) - prevPosition;
		}else{
			delta = Vector2.zero;
		}

		prevPosition = Input.mousePosition;
	}

	public bool Clicked(){
		if(!moved && Input.GetButtonUp("Fire1")){
			return true;
		}else{
			return false;
		}
	}

	public Vector2 GetDeltaPosition(){
		return delta;
	}

	public bool Moved(){
		return moved;
	}

	public Vector2 GetCursorPosition(){
		return Input.mousePosition;
	}
}
