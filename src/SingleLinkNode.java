package structures;

public class SingleLinkNode<T> {

  private final T value;
  private SingleLinkNode<T> next;

  public SingleLinkNode(T value) {
    this.value = value;
  }

  public void next(SingleLinkNode<T> node) {
    this.next = node;
  }

  public SingleLinkNode<T> next() {
    return this.next;
  }

  public T value() {
    return this.value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    SingleLinkNode node = this;
    while(node != null) {
      if(node.value == null) {
        sb.append("<null>");
      } else {
        sb.append(node.value.toString());
      }

      if(node.next != null) {
        sb.append("->");
      }

      node = node.next;
    }

    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if(!(other instanceof SingleLinkNode)) {
      return false;
    }
    SingleLinkNode<?> otherNode = (SingleLinkNode<?>)other;
    if(this.value == null) {
      if(otherNode.value != null) {
        return false;
      }
    } else if(!this.value.equals(otherNode.value)) {
      return false;
    }

    if(this.next == null) {
      return otherNode.next == null;
    }

    return this.next.equals(otherNode.next);
  }
}
