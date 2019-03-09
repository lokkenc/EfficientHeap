package heap;


/*  Author: Chris Lokken
    Date: 3/6/19
    Purpose:
    A hash table modeled after java.util.Map. It uses chaining for collision
    resolution and grows its underlying storage by a factor of 2 when the load
    factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
        size = 0;
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        // TODO 2.1 - do this together with put.
        //throw new UnsupportedOperationException();
        Pair cur = buckets[getHash(key) % getCapacity()];
        if (cur == null)
            return null;
        while (cur.key != key)
        {
            cur = cur.next;
            if (cur == null)
                return null;
        }
        return cur.value;
    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Runtime: average case O(1); worst case O(size^2 + a.length)*/
    public V put(K key, V val) {
        // TODO 2.2
        //   do this together with get. For now, don't worry about growing the
        //   array and rehashing.
        //   Tips:
        //     - Use the key's hashCode method to find which bucket it belongs in.
        //     - It's possible for hashCode to return a negative integer.
        //
        // TODO 2.5 - modify this method to grow and rehash if the load factor
        //            exceeds 0.8.
        //throw new UnsupportedOperationException();
        int bucketIndex = getHash(key) % getCapacity();
        Pair cur = buckets[bucketIndex];
        Pair prev = cur;
        if (cur == null)
        { 
            buckets[bucketIndex] = new Pair(key, val);
            size += 1;
            growIfNeeded();
            return null;
        }
        while (cur != null && cur.key != key)
        {
            prev = cur;
            cur = cur.next;
        }   
        if (cur == null)
        {
            prev.next = new Pair(key, val);
            size += 1;
            growIfNeeded();
            return null;
        }
        V ret = cur.value;
        cur.value = val;
        growIfNeeded();
        return ret;

        
    }

    private int getHash(K k)
    {
        int h = k.hashCode();
        return Math.abs(h);
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        // TODO 2.3
        //throw new UnsupportedOperationException();
        if (get(key) == null)
            return false;
        return true;
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
        // TODO 2.4
        //throw new UnsupportedOperationException();
        if (!(containsKey(key)))
            return null;
        int bucketIndex = getHash(key) % getCapacity();
        Pair cur = buckets[bucketIndex];
        if (cur.key == key)
        {
            buckets[bucketIndex] = cur.next;
        } else
        {
            Pair prev = cur;
            while (cur.key != key)
            {
                prev = cur;
                cur = cur.next;
            }
            prev.next = cur.next;
        }
        size -= 1;
        return cur.value;
        
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the array size
     * (capacity) and rehash values from the old array to the new array */
    private void growIfNeeded() {
        //throw new UnsupportedOperationException();
        double loadFactor = (double) size / (double) buckets.length;
        if (loadFactor <= 0.8)
            return;
        Pair[] temp = buckets;
        buckets = createBucketArray(buckets.length * 2);
        size = 0;
        for (int i = 0; i < temp.length; i++)
        {
            Pair cur = temp[i];
            while (cur != null)
            {  
                put(cur.key, cur.value);
                cur = cur.next;
            }
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
