public class HashTable {
  private NodeList[] elements;

  public HashTable() {
   this(5);
  }

  public HashTable(int initialSize) {
    this.elements = new NodeList[initialSize];
  }

  public void put(Object key, Object value) {
    if(elements[hash(key)] == null) {
     elements[hash(key)] = new NodeList();
    }

    elements[hash(key)].add(key, value);
  }

  public Object get(Object key) {
    return elements[hash(key)].get(key);
  }

  private int hash(Object key) {
    int hash = key.hashCode() & 0x7fffffff;
    return  hash % elements.length;
  }
}

class NodeList {

  private Node head = null;
  private Node tail = null;

  public void add(Object key, Object value) {
    Node node = new Node(key, value);
    if(head == null) {
      head = node;
    } else {
      head.next(node);
   }
  }

  public Object get(Object key) {
    Node current = head;
    while(current != null) {
      if(current.key.equals(key)) {
        return current.value;
      }
    }

   return null;
  }
}

class Node {
  final Object key;
  final Object value;
  private Node next;

  public Node(Object key, Object value) {
    this.key = key;
    this.value = value;
  }

  public void next(Node next) {
    this.next = next;
  }

  public Node next() {
    return this.next;
  }
}
