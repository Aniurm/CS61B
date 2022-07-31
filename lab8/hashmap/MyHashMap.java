package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Aniurm
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private HashSet<K> keyHolder;
    private int bucketsNum = 16;
    private double loadFactor = 0.75;
    private int nodeNum = 0;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(bucketsNum);
        keyHolder = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        bucketsNum = initialSize;
        buckets = createTable(bucketsNum);
        keyHolder = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        bucketsNum = initialSize;
        loadFactor = maxLoad;
        buckets = createTable(bucketsNum);
        keyHolder = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    @Override
    public void clear() {
        buckets = createTable(bucketsNum);
        keyHolder = new HashSet<>();
        nodeNum = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keyHolder.contains(key);
    }

    private int getIndex(K key) {
        int hc = key.hashCode();
        return Math.floorMod(hc, bucketsNum);
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = getIndex(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        } else {
            for (Node item : bucket) {
                if (item.key.equals(key)) {
                    return item.value;
                }
            }
            return null;
        }
    }

    @Override
    public int size() {
        return nodeNum;
    }

    private boolean needResize() {
        return 1.0 * nodeNum / bucketsNum >= loadFactor;
    }

    private void resize() {
        bucketsNum = bucketsNum * 2;
        Collection<Node>[] newTable = createTable(bucketsNum);
        for (int i = 0; i < bucketsNum / 2; i++) {
            if (buckets[i] == null) {
                continue;
            }

            for (Node item : buckets[i]) {
                int index = getIndex(item.key);
                if (newTable[index] == null) {
                    newTable[index] = createBucket();
                }
                newTable[index].add(item);
            }
        }

        buckets = newTable;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (containsKey(key)) {
            for (Node item : buckets[index]) {
                // replace
                if (item.key.equals(key)) {
                    item.value = value;
                    return;
                }
            }
        } else {    //not exist, add new Node
            if (buckets[index] == null) {
                buckets[index] = new HashSet<>();
            }
            buckets[index].add(createNode(key, value));
            nodeNum++;
            keyHolder.add(key);
        }

        // check N / M
        if (needResize()) {
            resize();
        }
    }

    @Override
    public Set<K> keySet() {
        return keyHolder;
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
        return keyHolder.iterator();
    }
}
