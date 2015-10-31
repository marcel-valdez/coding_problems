package structures;

import java.util.*;

public class DirectedNode<T> {
  private final T value;
  private List<DirectedNode> links;

  public DirectedNode(T value) {
    this.value = value;
    this.links = new ArrayList<DirectedNode>();
  }

  public T value() { return this.value; }

  public Iterable<DirectedNode> links() { return this.links; }
  public void linkTo(DirectedNode<T> node) { this.links.add(node); }
}
