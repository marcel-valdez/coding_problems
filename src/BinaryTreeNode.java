package structures;

public class BinaryTreeNode<T> {
  private final T value;
  private BinaryTreeNode<T> left;
  private BinaryTreeNode<T> right;
  private BinaryTreeNode<T> parent;

  public BinaryTreeNode(T value) { this.value = value; }

  public T value() { return this.value; }

  public void parent(BinaryTreeNode<T> parent) { this.parent = parent; }
  public BinaryTreeNode<T> parent() { return this.parent; }

  public void left(BinaryTreeNode<T> left) {
    if(left != null) {
      left.parent(this);
    }

    this.left = left;
  }

  public BinaryTreeNode<T> removeLeft() {
    BinaryTreeNode<T> node = this.left;

    this.left.parent(null);
    this.left = null;

    return node;
  }

  public BinaryTreeNode<T> left() { return this.left; }

  public void right(BinaryTreeNode<T> right) {
    if(right != null) {
      right.parent(this);
    }

    this.right = right;
  }

  public BinaryTreeNode<T> removeRight() {
    BinaryTreeNode<T> node = this.right;

    this.right.parent(null);
    this.right = null;

    return node;
  }

  public BinaryTreeNode<T> right() { return this.right; }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.valueOf(value));
    if(left() != null) {
      sb.append("\n");
      toString(sb, left(), 0);
    }

    if(right() != null) {
      sb.append("\n");
      toString(sb, right(), 0);
    }

    return sb.toString();
  }

  public boolean equals(Object other) {
    if(!(other instanceof BinaryTreeNode)) {
      return false;
    }

    BinaryTreeNode node = (BinaryTreeNode) other;
    return equal(this.value(), node.value())
        && equal(this.left(), node.left())
        && equal(this.right(), node.right());
  }

  private static boolean equal(Object a, Object b) {
    if(a == null || b == null) { return a == b; }

    return a.equals(b);
  }

  private static void toString(StringBuilder sb, BinaryTreeNode node, int parentLevel) {
    appendLevels(sb, parentLevel);
    if(node != null) {
      sb.append("|-");
      sb.append(String.valueOf(node.value));
      if(node.left() != null) {
        sb.append("\n");
        toString(sb, node.left(), parentLevel + 1);
      }

      if(node.right() != null) {
        sb.append("\n");
        toString(sb, node.right(), parentLevel + 1);
      }

      if(node.right() != null || node.left() != null) {
        sb.append("\n");
        appendLevels(sb, parentLevel + 1);
      }
    }
  }

  private static void appendLevels(StringBuilder sb, int levels) {
    for(int i = 0; i < levels; i++) { sb.append("| "); }
  }
}
