package com.DaichiNoto.framework.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Poolクラス
 * インスタンスプール
 * @author Daichi Noto 2015 03/09
 *
 */
public class Pool<T> {
	/**
	 * 新しいオブジェクトを返す
	 * @param <T>
	 */
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    /**
     * メンバ変数
     */
    private final ArrayList<T> freeObjects;		//
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject() {
        T object = null;

        if (freeObjects.size() == 0)
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);

        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
