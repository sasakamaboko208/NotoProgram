using UnityEngine;
using System.Collections;

//リソース管理クラス
public class AssetManager
{
	//シングルトン
	static AssetManager m_instance = null;
	
	private AssetManager(){
	}
	
	public static AssetManager getInstance{
		get{
			if(m_instance == null){
				m_instance = new AssetManager();
			}
			return m_instance;
		}
	}



}

