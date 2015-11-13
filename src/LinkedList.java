package structures;

public class LinkedList<T> {
  private SingleLinkNode<T> head;
  private SingleLinkNode<T> tail;
  private int size = 0;

  public void add(T value) {
    if(this.isEmpty()) {
      this.head = this.tail = new SingleLinkNode<T>(value);
    } else {
      SingleLinkNode<T> node = new SingleLinkNode<T>(value);
      tail.next(node);
      tail = node;
    }

    this.size++;
  }

  public void appendAll(LinkedList<T> other) {
    this.tail.next(other.head);
    this.tail = other.tail;
    this.size += other.size;
  }

  public T removeFirst() {
    if(this.isEmpty()) { throw new RuntimeException("List is empty"); }
    T value = this.head.value();
    if(this.tail == this.head) {
      this.tail = this.head = null;
    } else {
      this.head = this.head.next();
    }

    this.size--;

    return value;
  }

  public T get(int index) {
    if(index < 0 || index >= size()) {
      throw new RuntimeException("Index out of bounds: " + index + ", size: " + size() + ".");
    }

    if(this.isEmpty()) { throw new RuntimeException("List is empty"); }

    SingleLinkNode<T> node = this.head;
    while(index-- > 0) { node = node.next(); }

    return node.value();
  }

  public T first() {
    if(this.isEmpty()) { throw new RuntimeException("List is empty"); }

    return this.head.value();
  }

  public T last() {
    if(this.tail == null) { throw new RuntimeException("List is empty"); }

    return this.tail.value();
  }

  public boolean contains(T value) {
    SingleLinkNode<T> node = head;
    while(node != null) {
      if(equals(node.value(), value)) {
        return true;
      }

      node = node.next();
    }

    return false;
  }

  public int size() { return this.size; }
  public boolean isEmpty() { return this.size() == 0; }

  public <K> K[] toArray(Class<K> itemClass) {
    K[] array = (K[]) java.lang.reflect.Array.newInstance(itemClass, size());
    SingleLinkNode<T> node = head;
    int i = 0;
    while(head != null) {
      array[i] = (K) head.value();
      head = head.next();
      i++;
    }

    return array;
  }

  public String toString() {
    if(isEmpty()) { return "<empty>"; }
    StringBuilder sb = new StringBuilder();
    sb.append(" {");
    sb.append(head);
    sb.append("} ");

    return sb.toString();
  }

  public boolean equals(Object otherObj) {
    if(!(otherObj instanceof LinkedList)) {
      return false;
    }

    LinkedList<T> other = (LinkedList<T>) otherObj;

    return this.size == other.size && equals(this.head, other.head);
  }

  private boolean equals(Object value, Object other) {
    if(value == null || other == null) { return value == other; }

    return value.equals(other);
  }
}
