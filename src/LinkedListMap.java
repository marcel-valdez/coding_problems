package structures;
/**
* TODO: Write a test class for the linked list structure
*/
public class LinkedListMap {

  private Node head = null;
  private int size = 0;

  public void add(Object key, Object value) {
    if(key == null) {
      throw new RuntimeException("Key should never be null");
    }

    Node node = new Node(key, value);
    if(head == null) {
      head = node;
    } else {
      head.next(node);
    }

    this.size++;
  }

  public Object get(Object key) {
    if(key == null) {
      return null;
    }

    Node current = head;
    while(current != null) {
      if(current.key.equals(key)) {
        return current.value;
      }
      current = head.next();
    }

   return null;
  }

  public int size() {
    return size;
  }

  public static class Node {
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
}
