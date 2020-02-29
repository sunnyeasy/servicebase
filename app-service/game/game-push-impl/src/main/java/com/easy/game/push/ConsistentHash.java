package com.easy.game.push;

import java.util.ArrayList;
import java.util.List;

public class ConsistentHash<T> {
    private List<T> consistentHashReferers;
    private List<T> referers;

    /**
     * 默认的consistent的hash的数量
     */
    public static final int DEFAULT_CONSISTENT_HASH_BASE_LOOP = 1000;

    public ConsistentHash(List<T> referers, int baseLoop) {
        // 只能引用替换，不能进行referers update。
        this.referers = referers;

        List<T> copyReferers = new ArrayList<>();
        List<T> tempReferers = new ArrayList<>();
        for (int i = 0; i < baseLoop; i++) {
            for (T ref : copyReferers) {
                tempReferers.add(ref);
            }
        }
        consistentHashReferers = tempReferers;
    }

    public ConsistentHash(List<T> referers) {
        this(referers, DEFAULT_CONSISTENT_HASH_BASE_LOOP);
    }

    public T select(Object key) {
        int hash = hashCode(key);
        T ref = null;
        if (referers.size() == 1) {
            ref = referers.get(0);
        } else {
            for (int i = 0; i < referers.size(); i++) {
                ref = consistentHashReferers.get(((hash + i) % consistentHashReferers.size()));
            }
        }
        return ref;
    }

    public int hashCode(Object key) {
        String data = key.toString();

        //FNV1hash
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
}
