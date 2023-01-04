import java.util.*;
import java.io.*;


class CacheCell{

    public CacheCell(int value){
    
                 this.value = value;             
    }
    
    public Integer value;
    public Integer next;
    public Integer previous;
    

}

class LRUCache {

    private int capacity;
    
    private Integer frontElementKey = null;
    
    private Integer tailElementKey = null;

    private  Map<Integer,CacheCell> storage;
    
    // constructor
    public LRUCache(int capacity) {
        this.capacity = capacity;
        storage = new HashMap<Integer, CacheCell>(capacity);
    }

    // get value of a key
    public int get(Integer key) {

        // check if key existed, otherwise return -1
        if(!storage.containsKey(key)){
            return -1;
        }

        CacheCell cacheCell = storage.get(key);
        // if the cell is tail node, and it's not the only node in storage, change the tail to its next node
        if(tailElementKey == key && cacheCell.next != null )
            tailElementKey = cacheCell.next;

        // put the cell in front of linked list as first node
        cacheCell.previous = frontElementKey;
        cacheCell.next = null;
        storage.get(frontElementKey).next = key;
        frontElementKey = key;

        return cacheCell.value;
    }

    // put a key-value to storage
    public void put(int key, int value) {
    
        // if the capacity is full and the key doesn't exist, remove the LRU cell (tail node )
        if(storage.size()==this.capacity && !storage.containsKey(key)){
            Integer tempTailElement = tailElementKey;
            tailElementKey = (storage.get(tempTailElement).next == null ? key : storage.get(tempTailElement).next);
            storage.remove(tempTailElement); 
        }
        
        
        if(storage.size()!=0)
            storage.get(frontElementKey).next=key; // if storage was not empty, so we have already a front element, it should refrence to new element
        else
            tailElementKey = key; // if the storge is empty, so the only cell is tail element too

        CacheCell cacheCell = new CacheCell(value);
        cacheCell.previous = frontElementKey;
        cacheCell.next = null;
        frontElementKey = key;
        storage.put(key,cacheCell);
        storage.get(tailElementKey).previous = null;

    }
    
}

class Main {

  public static void main (String[] args) {
    int capacity = 2;
    LRUCache cache = new LRUCache(capacity);
    cache.put(1, 1); // storage : {1} 
    cache.put(2, 2); // storage : {1,2} - LRU : 1
    cache.get(1);    // storage : {1,2} - LRU : 2
    cache.put(3, 3); // value 2 is evicted from cache
    System.out.println(cache.get(3));
    System.out.println(cache.get(2));
    System.out.println(cache.get(1));
    
    System.out.println("===============");

    LRUCache lRUCache = new LRUCache(2);
    lRUCache.put(1, 1); // cache is {1=1}
    lRUCache.put(2, 2); // cache is {1=1, 2=2}
    System.out.println(lRUCache.get(1));    // return 1
    lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
    System.out.println(lRUCache.get(2));    // returns -1 (not found)
    lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
    System.out.println(lRUCache.get(1));    // return -1 (not found)
    System.out.println(lRUCache.get(3));    // return 3
    System.out.println(lRUCache.get(4));    // return 4

    System.out.println("===============");
    LRUCache lRUCacheWithOneCap = new LRUCache(1);
    lRUCacheWithOneCap.put(1, 1);
    lRUCacheWithOneCap.put(2, 2);
    System.out.println(lRUCacheWithOneCap.get(1)); // return -1
    System.out.println(lRUCacheWithOneCap.get(2)); // return 2
    lRUCacheWithOneCap.put(3, 3);
    System.out.println(lRUCacheWithOneCap.get(2)); // return -1
    System.out.println(lRUCacheWithOneCap.get(3)); // return 3

    System.out.println("===============");
    LRUCache lRUCacheWithThreeCap = new LRUCache(3);
    lRUCacheWithThreeCap.put(1, 1);
    lRUCacheWithThreeCap.put(2, 2);
    System.out.println(lRUCacheWithThreeCap.get(1)); // return 1
    System.out.println(lRUCacheWithThreeCap.get(2)); // return 2
    lRUCacheWithThreeCap.put(3, 3);
    System.out.println(lRUCacheWithThreeCap.get(2)); // return 2
    System.out.println(lRUCacheWithThreeCap.get(3)); // return 3
    lRUCacheWithThreeCap.put(4, 4);
    System.out.println(lRUCacheWithThreeCap.get(1)); // return -1
    

  }


}