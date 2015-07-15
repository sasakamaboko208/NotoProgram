using UnityEngine;
using System.Collections;
using System.Collections.Generic;

//音の管理クラス
public class AudioManager
{
	//チャンネル数の取得
	const int SE_CHANNEL = 4;

	//サウンドの種類
	enum Type{
		Bgm,
		Se,
	}

	//サウンド再生のためのゲームオブジェクト
	GameObject m_soundObj = null;

	//サウンドリリース
	AudioSource m_sourceBgm = null; //BGM
	AudioSource m_sourceSetDefault = null; //SE (デフォルト)
	AudioSource[] m_sourceSetArray; //SE (チャンネル)

	//BGMにアクセスするテーブル
	Dictionary<string, SoundData> m_poolBgm = new Dictionary<string, SoundData>();
	//SEにアクセスするテーブル
	Dictionary<string, SoundData> m_poolSe = new Dictionary<string, SoundData>();

	//シングルトン
	static AudioManager m_instance = null;
	
	private AudioManager(){
			//チャンネルの確保
			m_sourceSetArray = new AudioSource[SE_CHANNEL];
	}

	public static AudioManager getInstance{
		get{
			if(m_instance == null){
				m_instance = new AudioManager();
			}
			return m_instance;
		}
	}

	// AudioSourceを取得する
	AudioSource GetAudioSource(Type type, int channel=-1){

		if(m_soundObj == null){

			//GameObjectがなければ作る
			m_soundObj = new GameObject("Sound");
			//破棄しないようにする
			GameObject.DontDestroyOnLoad(m_soundObj);
			//AudioSourceを作成
			m_sourceBgm = m_soundObj.AddComponent<AudioSource>();
			m_sourceSetDefault = m_soundObj.AddComponent<AudioSource>();

			for(int i = 0; i < SE_CHANNEL; i++){
				m_sourceSetArray[i] = m_soundObj.AddComponent<AudioSource>();
			}
		}

		if(type == Type.Bgm){
			//BGM
			return m_sourceBgm;
		}else{
			if(0 <= channel && channel < SE_CHANNEL){
				//チャンネル指定
				return m_sourceSetArray[channel];
			}else{
				//デフォルト
				return m_sourceSetDefault;
			}
		}
	}

	//BGMをロードする関数
	public void LoadBgm(string key, string resName){
		if(m_poolBgm.ContainsKey(key)){
			//登録済みの場合はいったん消す
			m_poolBgm.Remove(key);
		}

		m_poolBgm.Add(key, new SoundData(key,resName));
	}

	//SEをロードする関数
	public void LoadSe(string key, string resName){
		if(m_poolSe.ContainsKey(key)){
			//登録済みの場合はいったん消す
			m_poolSe.Remove(key);
		}
		
		m_poolSe.Add(key, new SoundData(key,resName));
	}

	//BGMの再生関数
	public bool PlayBgm(string key){
		if(m_poolBgm.ContainsKey(key) == false){
			return false;
		}

		//いったん止める
		StopBgm();

		//リソースの取得
		SoundData data = m_poolBgm[key];

		//再生
		AudioSource source = GetAudioSource(Type.Bgm);
		source.loop = true;
		source.clip = data.getClip();
		source.volume = 0.01f;
		source.Play();

		return true;
	}

	public bool StopBgm(){
		GetAudioSource(Type.Bgm).Stop();
		return true;
	}

	//SEの再生
	public bool PlaySe(string key, int channel=-1){
		if(m_poolSe.ContainsKey(key) == false){
			return false;
		}
		
		//リソースの取得
		SoundData data = m_poolSe[key];

		if(0 <= channel && channel < SE_CHANNEL){
			//再生
			AudioSource source = GetAudioSource(Type.Se, channel);
			source.clip = data.getClip();
			source.Play();
		}else{
			// デフォルトで再生
			AudioSource source = GetAudioSource(Type.Se);
			source.volume = 1.0f;
			source.PlayOneShot(data.getClip());
		}
		
		return true;
	}

	//インナークラス　保存データ
	class SoundData{
		/// アクセス用のキー
		string m_key;
		/// リソース名
		string m_resName;
		/// AudioClip
		AudioClip m_clip;
		
		/// コンストラクタ
		public SoundData(string key, string res) {
			m_key = key;
			m_resName = "SoundAssets/" + res;
			// AudioClipの取得
			m_clip = Resources.Load(m_resName) as AudioClip;
		}

		//AudioClipのインスタンスを取得
		public AudioClip getClip(){
			return m_clip;
		}

		//リソース名取得
		public string getResName(){
			return m_resName;
		}

		//リソース名取得
		public string getKey(){
			return m_key;
		}



	}
}

