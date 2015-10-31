package chapter4;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.util.*;

import structures.*;

class BinaryTreeBalanceChecker {

  public boolean check(BinaryTreeNode root) {
    return checkBalance(root, 0).isBalanced();
  }

  private Balance checkBalance(BinaryTreeNode node, int parentHeight) {
    if(node == null) { return new Balance(parentHeight, true); }

    Balance leftBalance = checkBalance(node.left(), parentHeight + 1);
    if(!leftBalance.isBalanced()) {
      return leftBalance;
    }

    Balance rightBalance = checkBalance(node.right(), parentHeight + 1);
    if(!rightBalance.isBalanced()) {
      return rightBalance;
    }

    boolean isBalanced = Math.abs(rightBalance.height() - leftBalance.height()) <= 1;
    int height = Math.max(rightBalance.height(), leftBalance.height());

    return new Balance(height, isBalanced);
  }

  public static class Balance {
    private final int height;
    private final boolean isBalanced;

    public Balance(int height, boolean isBalanced) {
      this.height = height;
      this.isBalanced = isBalanced;
    }

    public int height() { return this.height; }
    public boolean isBalanced() { return this.isBalanced; }
  }
}


class PathChecker {

  // Time Complexity: O(N), Space Complexity: O(N)
  public boolean pathExists(DirectedNode nodeA, DirectedNode nodeB) {
    if(nodeA == nodeB) { return true; }
    if(nodeA == null || nodeB == null) { return false; }

    LinkedListStack<DirectedNode> visitingA = new LinkedListStack<>();
    HashSet<DirectedNode> visitedA = new HashSet<>();
    LinkedListStack<DirectedNode> visitingB = new LinkedListStack<>();
    HashSet<DirectedNode> visitedB = new HashSet<>();

    visitingA.push(nodeA);
    visitedA.add(nodeA);
    visitingB.push(nodeB);
    visitedB.add(nodeB);

    while(visitingA.size() != 0 && visitingB.size() != 0) {
      if(visitLinks(visitingA, visitedA, visitedB)) {
        return true;
      }

      if(visitLinks(visitingB, visitedB, visitedA)) {
        return true;
      }
    }

    return false;
  }

  @SuppressWarnings({"unchecked"})
  public boolean visitLinks(
    LinkedListStack<DirectedNode> visiting,
    HashSet<DirectedNode> visited,
    HashSet<DirectedNode> destinations) {

    DirectedNode node = visiting.pop();
    for(DirectedNode link : (Iterable<DirectedNode>) node.links()) {
      if(destinations.contains(link)) {
        return true;
      }

      if(visited.contains(link)) {
        continue;
      }

      visiting.push(link);
    }

    return false;
  }
}


// 1,2,3,4,5,6,7,8,9, length = 9
// Basically conver this into it binary search array.
// Should create:
//     5 index = lenght/2 = 9/2 = 4
//  3     7 { 3 = 9 - 1 = 8 / 2 = 2
//2  4  6   8
//            9
class TreeFromSortedNumbersCreator {
  public BinaryTreeNode<Integer> create(int ... numbers) {
    if(numbers == null || numbers.length == 0) {
      return null;
    }

    return create(numbers, 0, numbers.length);
  }

  public BinaryTreeNode<Integer> create(int[] numbers, int start, int length) {
    if(length == 0) { return null; }

    int rootIndex = start + (length / 2);
    int remainingLength = length - 1;
    BinaryTreeNode<Integer> root = new BinaryTreeNode<Integer>(numbers[rootIndex]);

    int leftStart = start;
    int leftLength = rootIndex - start;
    root.left(create(numbers, leftStart, leftLength));

    int rightStart = rootIndex + 1;
    int rightLength = remainingLength - leftLength;
    root.right(create(numbers, rightStart, rightLength));

    return root;
  }
}

class ListsFromTreeLevelsCreator {
  public structures.LinkedList<structures.LinkedList> create(BinaryTreeNode tree) {
    structures.LinkedList<structures.LinkedList> lists = new structures.LinkedList<>();
    LinkedListStack<BinaryTreeNode> currentLevel = new LinkedListStack<>();
    LinkedListStack<BinaryTreeNode> nextLevel = new LinkedListStack<>();

    nextLevel.push(tree);

    do {
      LinkedListStack<BinaryTreeNode> temp = currentLevel;
      currentLevel = nextLevel;
      nextLevel = temp;

      lists.add(new structures.LinkedList<BinaryTreeNode>());
      while(!currentLevel.isEmpty()) {
        BinaryTreeNode node = currentLevel.pop();
        lists.last().add(node.value());
        if(node.left() != null) { nextLevel.push(node.left()); }
        if(node.right() != null) { nextLevel.push(node.right()); }
      }
    } while(!nextLevel.isEmpty());

    return lists;
  }
}

class BinarySearchTreeChecker {
  public boolean check(BinaryTreeNode<Integer> tree) {
    if(tree == null) { throw new RuntimeException("tree cannot be null"); }
    return check(tree, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private boolean check(BinaryTreeNode<Integer> tree, int min, int max) {
    if(tree == null) { return true; }

    if(tree.value() <= min || tree.value() >= max) { return false; }

    return check(tree.left(), min, tree.value()) &&
           check(tree.right(), tree.value(), max);
  }
}

class NextInOrderNodeFinder {
  public <T> BinaryTreeNode<T> find(BinaryTreeNode<T> node) {
    if(node == null) { throw new RuntimeException("node can't be null"); }

    if(node.right() == null) {
      return firstLeftAncestor(node);
    }

    return leftMostLeaf(node.right());
  }

  private <T> BinaryTreeNode<T> firstLeftAncestor(BinaryTreeNode<T> node) {
    if(node.parent() == null) { return null; }

    if(node.parent().left() == node) { return node.parent(); }

    return firstLeftAncestor(node.parent());
  }

  private <T> BinaryTreeNode<T> leftMostLeaf(BinaryTreeNode<T> node) {
    if(node.left() == null) {
      if(node.right() == null) {
        return node;
      } else {
        return leftMostLeaf(node.right());
      }
    }

    return leftMostLeaf(node.left());
  }
}

class HashedCommonAncestorFinder implements CommonAncestorFinder {
  public <T> BinaryTreeNode<T> find(BinaryTreeNode<T> node, BinaryTreeNode<T> other) {
    if(node == null || other == null) {
      throw new RuntimeException("node and other cannot be null.");
    }

    if(node == other) { return node; }

    HashSet<BinaryTreeNode> nodeAncestors = new HashSet<BinaryTreeNode>();
    nodeAncestors.add(node);
    HashSet<BinaryTreeNode> otherAncestors = new HashSet<BinaryTreeNode>();
    otherAncestors.add(other);

    while(node != null || other != null) {
      if(node != null) {
        if(node.parent() != null) {
          if(otherAncestors.contains(node.parent())) {
            return node.parent();
          }

          nodeAncestors.add(node.parent());
        }

        node = node.parent();
      }

      if(other != null) {
        if(other.parent() != null) {
          if(nodeAncestors.contains(other.parent())) {
            return other.parent();
          }

          otherAncestors.add(other.parent());
        }

        other = other.parent();
      }
    }

    // no common ancestor found
    return null;
  }
}

class TraversedCommonAncestorFinder implements CommonAncestorFinder {
  public <T> BinaryTreeNode<T> find(BinaryTreeNode<T> node, BinaryTreeNode<T> other) {
    while(node != null) {
      BinaryTreeNode<T> current = other;
      while(current != null) {
        if(node == current) {
          return node;
        }
        current = current.parent();
      }

      node = node.parent();
    }

    return null;
  }
}

interface CommonAncestorFinder {
  public <T> BinaryTreeNode<T> find(BinaryTreeNode<T> node, BinaryTreeNode<T> other);
}

class SubtreeChecker {
  public boolean check(BinaryTreeNode tree, BinaryTreeNode subtree) {
    return check(tree, subtree, height(subtree)) == -1;
  }

  private int height(BinaryTreeNode tree) {
    if(tree == null) { return 0; }

    return Math.max(height(tree.left()), height(tree.right())) + 1;
  }

  private int check(BinaryTreeNode tree, BinaryTreeNode subtree, int subtreeHeight) {
    if(tree == null) { return 0; }

    int leftHeight = check(tree.left(), subtree, subtreeHeight);
    int rightHeight = check(tree.right(), subtree, subtreeHeight);
    if(leftHeight == -1 || rightHeight == -1) {
      return -1;
    }

    int height = Math.max(leftHeight, rightHeight) + 1;
    if(height == subtreeHeight && equals(tree, subtree)) {
      return -1;
    }

    return height;
  }

  private boolean isLeaf(BinaryTreeNode tree) {
    return tree.left() == null && tree.right() == null;
  }

  // O(n) -> number of nodes in tree
  private boolean equals(BinaryTreeNode tree, BinaryTreeNode other) {
    if(tree == null || other == null) { return other == null; }
    return tree.equals(other);
  }
}


class BinaryTreeSumFinder {

  public List<String> find(BinaryTreeNode<Integer> tree, int sum) {
    List<String> results = new ArrayList<>();
    find(results, null, tree, sum);

    return results;
  }

  private void find(
    List<String> results,
    SingleLinkNode<Integer> path,
    BinaryTreeNode<Integer> tree,
    int sum) {
    if(tree == null) { return; }

    SingleLinkNode<Integer> newPath = new SingleLinkNode<>(tree.value());
    newPath.next(path);

    SingleLinkNode<Integer> pathNode = newPath;
    SingleLinkNode<Integer> sumPath = new SingleLinkNode(pathNode.value());
    SingleLinkNode<Integer> sumPathHead = sumPath;

    int pathSum = 0;
    while(pathNode != null) {
      pathSum += pathNode.value();
      if(pathSum == sum) {
        results.add(sumPathHead.toString());
      }
      pathNode = pathNode.next();
      if(pathNode != null) {
        sumPath.next(new SingleLinkNode(pathNode.value()));
        sumPath = sumPath.next();
      }
    }

    find(results, newPath, tree.left(), sum);
    find(results, newPath, tree.right(), sum);
  }
}
