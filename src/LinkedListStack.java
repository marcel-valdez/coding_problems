package structures;

public class LinkedListStack<T> {
  private SingleLinkNode<T> head;
  private volatile int size;

  public LinkedListStack() {
    size = 0;
    head = null;
  }

  public void push(T value) {
    if(this.head == null) {
      this.head = new SingleLinkNode<>(value);
    } else {
      SingleLinkNode<T> node = new SingleLinkNode<>(value);
      node.next(head);
      this.head = node;
    }

    this.size++;
  }

  public T pop() {
    if(this.head == null) {
      throw new IllegalStateException("Stack is empty, cannot pop");
    }

    T value = this.peek();
    this.head = this.head.next();
    this.size--;
    return value;
  }

  public T peek() {
    return this.head.value();
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size() == 0;
  }

  public String toString() {
    if(this.head == null) { return "<empty>"; }
    else { return this.head.toString(); }
  }
}
