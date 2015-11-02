package structures;

public class DoubleLinkNode<T> {

  private final T value;
  private DoubleLinkNode<T> next;
  private DoubleLinkNode<T> prev;


  public DoubleLinkNode(T value) {
    this.value = value;
  }

  public void next(DoubleLinkNode<T> node) {
    if(this.next() != null) {
      this.next().prev = null;
    }

    if(node != null) {
      if(node.prev() != null) {
        node.prev().next(null);
      }

      node.prev = this;
    }

    this.next = node;
  }

  public DoubleLinkNode<T> next() {
    return this.next;
  }

  public void prev(DoubleLinkNode<T> prev) {
    if(prev != null) {
      prev.next(this);
    } else if(this.prev() != null) {
      this.prev().next(null);
    }
  }

  public DoubleLinkNode<T> prev() {
    return this.prev;
  }

  public void unlink() {
    DoubleLinkNode<T> next = this.next();
    if(this.next() != null) {
      this.next().prev(this.prev());
    } else if(this.prev() != null) {
      this.prev().next(null);
    }
  }

  public void insertNext(DoubleLinkNode<T> next) {
    DoubleLinkNode<T> oldNext = this.next();
    oldNext.prev(null);
    this.next(next);
    oldNext.prev(next);
  }

  public void insertPrev(DoubleLinkNode<T> prev) {
    DoubleLinkNode<T> oldPrev = this.prev();
    oldPrev.next(null);
    this.prev(prev);
    oldPrev.next(prev);
  }

  public T value() {
    return this.value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    DoubleLinkNode node = this;
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

  public int hashCode() {
    int hashCode = 0;
    if(this.value != null) {
      hashCode += 31 * this.value.hashCode();
    }

    if(this.next() != null) {
      hashCode += 17 * this.next.hashCode();
    }

    if(this.prev() != null) {
      hashCode += 12 * this.prev.hashCode();
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object other) {
    if(this == other) {
      return true;
    }

    if(!(other instanceof DoubleLinkNode)) {
      return false;
    }

    DoubleLinkNode<?> otherNode = (DoubleLinkNode<?>)other;

    return equal(this.value, otherNode.value)
        && equal(this.next, otherNode.next)
        && equal(this.prev, otherNode.prev);
  }

  private static boolean equal(Object a, Object b) {
    if(a == null || b == null) { return a == b; }

    return a.equals(b);
  }
}
