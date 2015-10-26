package structures;

public class HashTable {
  /**
   * TODO: Use a binary search tree for collisions instead of a linked list
   */
  private LinkedListMap[] elements;
  private int collisionCount;

  public HashTable() {
   this(5);
  }

  public HashTable(int initialSize) {
    this.elements = new LinkedListMap[initialSize];
    this.collisionCount = 0;
  }


  public int collisionCount() {
    return this.collisionCount;
  }

  public void put(Object key, Object value) {
    if(key == null) {
      throw new RuntimeException("The key should never be null");
    }

    if(elements[hash(key)] == null) {
     elements[hash(key)] = new LinkedListMap();
    }

    LinkedListMap bucket = elements[hash(key)];
    bucket.add(key, value);
    if(bucket.size() > 1) {
      collisionCount++;
    }
  }

  public Object get(Object key) {
    if(key == null) {
      throw new RuntimeException("The key should never be null");
    }

    return elements[hash(key)].get(key);
  }

  private int hash(Object key) {
    int hash = key.hashCode() & 0x7fffffff;
    return  hash % elements.length;
  }
}
