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


    


    private void moveToFront(Integer key){

        //if it's already front
        if (frontElementKey == key){
            return;
        }

        // if it's the first and only inserted node, just set the front and tail
        if(frontElementKey == null && tailElementKey == null){
            frontElementKey = key;
            tailElementKey = key;
        }
        else {
            // if it's new node, not already existed, should be put at front
            if (storage.get(key).next == null && storage.get(key).previous == null){
                storage.get(key).previous = frontElementKey;
                storage.get(frontElementKey).next = key;
                frontElementKey = key;
                return;
            }
            // if it's tail
            else if (tailElementKey==key){
                tailElementKey = storage.get(key).next;
                storage.get(tailElementKey).previous= null;
                storage.get(frontElementKey).next = key;
                frontElementKey = key;
                return;
            }
            //if it's middle
            else if (storage.get(key).next != null && storage.get(key).previous != null){
                storage.get(storage.get(key).previous).next = storage.get(key).next;
                storage.get(storage.get(key).next).previous = storage.get(key).previous;
                storage.get(key).next = null;
                storage.get(key).previous = frontElementKey;
                storage.get(frontElementKey).next = key;
                frontElementKey = key;
                
                return;
            }
        }

            
    }





    // get value of a key
    public int get(Integer key) {

        // check if key existed, otherwise return -1
        if(!storage.containsKey(key)){
            return -1;
        }

        CacheCell cacheCell = storage.get(key);

        moveToFront(key);

        return cacheCell.value;
    }

    // put a key-value to storage
    public void put(Integer key, Integer value) {
    
        // if the capacity is full and the key doesn't exist, remove the LRU cell (tail node )
        if(storage.size()==this.capacity && !storage.containsKey(key)){
            evictCell();
        }

        if(storage.containsKey(key)){
            storage.get(key).value = value;
        }
        else{
            CacheCell cacheCell = new CacheCell(value);
            storage.put(key,cacheCell);
        }
        moveToFront(key);

    }



    private void evictCell() {
        if(storage.size()==1){
            storage.remove(tailElementKey);
            frontElementKey = null;
            tailElementKey = null;
            return;
        }

        Integer tempTailElement = tailElementKey;
        storage.get(storage.get(tailElementKey).next).previous = null;
        tailElementKey = storage.get(tempTailElement).next;
        storage.remove(tempTailElement);
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

    System.out.println("===============");
    LRUCache lRUCacheWithTwoCap = new LRUCache(2);
    lRUCacheWithTwoCap.put(2, 1);
    lRUCacheWithTwoCap.put(1, 1);
    lRUCacheWithTwoCap.put(2, 3);
    lRUCacheWithTwoCap.put(4, 1);
    System.out.println(lRUCacheWithTwoCap.get(1));
    System.out.println(lRUCacheWithTwoCap.get(2)); 

    // [[2],[2,1],[3,2],[3],[2],[4,3],[2],[3],[4]]
    System.out.println("===============");
    lRUCacheWithTwoCap = new LRUCache(2);
    lRUCacheWithTwoCap.put(2, 1);
    lRUCacheWithTwoCap.put(3, 2);
    System.out.println(lRUCacheWithTwoCap.get(3)); // return 3
    System.out.println(lRUCacheWithTwoCap.get(2)); // return 1
    lRUCacheWithTwoCap.put(4, 3);
    System.out.println(lRUCacheWithTwoCap.get(2)); // return 1
    System.out.println(lRUCacheWithTwoCap.get(3)); // return -1
    System.out.println(lRUCacheWithTwoCap.get(4)); // return 3
    System.out.println(lRUCacheWithTwoCap.get(2)); // return 1


    //[[3],[1,1],[2,2],[3,3],[4,4],[4],[3],[2],[1],[5,5],[1],[2],[3],[4],[5]]
    lRUCacheWithTwoCap = new LRUCache(3);
    lRUCacheWithTwoCap.put(1, 1);
    lRUCacheWithTwoCap.put(2, 2);
    lRUCacheWithTwoCap.put(3, 3);
    lRUCacheWithTwoCap.put(4, 4);
    System.out.println(lRUCacheWithTwoCap.get(4)); // return 4
    System.out.println(lRUCacheWithTwoCap.get(3)); // return 3
    System.out.println(lRUCacheWithTwoCap.get(2)); // return 2
    System.out.println(lRUCacheWithTwoCap.get(1)); // return -1
    lRUCacheWithTwoCap.put(5, 5);
    System.out.println(lRUCacheWithTwoCap.get(1)); // return -1
    System.out.println(lRUCacheWithTwoCap.get(2)); // return -1
    System.out.println(lRUCacheWithTwoCap.get(3)); // return 3
    System.out.println(lRUCacheWithTwoCap.get(4)); // return 4
    System.out.println(lRUCacheWithTwoCap.get(5)); // return 5

  }


}