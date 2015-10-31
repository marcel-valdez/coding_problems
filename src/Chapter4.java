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
