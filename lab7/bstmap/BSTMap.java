package bstmap;
import java.util.Iterator;
import java.util.Set;

import afu.org.checkerframework.checker.oigj.qual.O;
import edu.princeton.cs.algs4.BST;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private BSTNode mapRoot;

    public BSTMap() {
        mapRoot = null;
    }

    private class BSTNode {
        K nodeKey;
        V nodeValue;
        BSTNode nodeLeft, nodeRight;
        int treeSize;

        BSTNode(K key, V value, int size) {
            nodeKey = key;
            nodeValue = value;
            treeSize = size;
        }

        private BSTNode getNode(K key) {
            return getNodeHelp(key, mapRoot);
        }

        private BSTNode getNodeHelp(K key, BSTNode x) {
            if (x == null) {
                return null;
            } else if (x.nodeKey.compareTo(key) == 0) {
                return x;
            } else if (x.nodeKey.compareTo(key) > 0) {
                return getNodeHelp(key, x.nodeLeft);
            } else {
                return getNodeHelp(key, x.nodeRight);
            }
        }
    }

    @Override
    public void clear() {
        mapRoot = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey() is null");
        }
        if (mapRoot == null) {
            return false;
        }

        BSTNode lookup = mapRoot.getNode(key);
        return lookup != null;
    }

    @Override
    public V get(K key) {
        if (mapRoot == null) {
            return null;
        }
        BSTNode lookup = mapRoot.getNode(key);
        if (lookup == null) {
            return null;
        } else {
            return lookup.nodeValue;
        }
    }

    @Override
    public int size() {
        return sizeHelp(mapRoot);
    }

    private int sizeHelp(BSTNode x) {
        if (x == null) {
            return 0;
        } else {
            return x.treeSize;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("call put() with a null key");
        }

        mapRoot = putHelp(mapRoot, key, value);
    }

    private BSTNode putHelp(BSTNode x, K key, V value) {
        if (x == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(x.nodeKey);
        if (cmp > 0) {
            x.nodeRight = putHelp(x.nodeRight, key, value);
        } else if (cmp < 0) {
            x.nodeLeft = putHelp(x.nodeLeft, key, value);
        } else {
            x.nodeValue = value;
        }

        x.treeSize = sizeHelp(x.nodeLeft) + sizeHelp(x.nodeRight) + 1;
        return x;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {

    }
}