package chapter3;

import java.lang.*;
import java.util.*;

import structures.*;


class CoordinatedStackMaker {

  public CoordinatedArrayStack[] makeStacks(Object[] data, int numberOfStacks) {
    CoordinatedArrayStack[] stacks = new CoordinatedArrayStack[numberOfStacks];
    for(int i = 0; i < numberOfStacks; i++) {
      stacks[i] = new CoordinatedArrayStack(data, i, numberOfStacks);
    }

    return stacks;
  }
}


class CoordinatedArrayStack {
  private final Object[] storage;
  private final int base;
  private final int multiplier;
  private int size;

  public CoordinatedArrayStack(Object[] storage, int base, int multiplier) {
    this.storage = storage;
    this.base = base;
    this.multiplier = multiplier;
    this.size = 0;
  }
  public void push(Object item) {
    int index = base + (size * multiplier);
    storage[index] = item;
    size++;
  }

  public Object pop() {
    if(size == 0) { return null; }
    Object data = peek();

    int index = base + ((size-1) * multiplier);
    storage[index] = null;
    size--;

    return data;
  }

  public Object peek() {
    if(size == 0) { return null; }
    return storage[base + ((size -1) * multiplier)];
  }

  public int size() {
    return size;
  }

  private static class Item {
    final int index;
    final int prevIndex;
    final Object data;
    public Item(int prevIndex, Object data, int index) {
      this.prevIndex = prevIndex;
      this.data = data;
      this.index = index;
    }
  }
}


// push:4 - min=4 | 4             | 4
// pusn:6 - min=4 | 4->6          | 4->6
// push:5 - min=4 | 4->5->6       | 4->5->6 // THIS could cost O(N)
// push:3 - min=3 | 4->5->6->3    | 3->4->5->6
// push:2 - min=2 | 4->5->6->3->2 | 2->3->4->5->6

// What can I do to push() the elements in O(1) in such
// a way that I can keep track of min() when pop() in O(1) ?

// pop(2) - min=3 | 4->5->6->3
// pop(3) - min=4 | 4->5->6
// pop(6) - min=4 | 4->5
// pop(5) - min=4 | 4

class MinTrackingStack {
  private SingleLinkNode<Integer> head;
  private SingleLinkNode<SingleLinkNode<Integer>> min;

  public void push(int value) {
    SingleLinkNode<Integer> node = new SingleLinkNode<>(value);
    if(head == null) {
      this.head = node;
      this.min = new SingleLinkNode<>(head);
    } else {
      if (value < min.value().value().intValue()) {
        SingleLinkNode<SingleLinkNode<Integer>> newMin = new SingleLinkNode<>(node);
        newMin.next(min);
        min = newMin;
      }

      node.next(head);
      head = node;
    }
  }

  public int pop() {
    if(head == null) { throw new RuntimeException("No more values!"); }

    if(min.value() == head) {
      min = min.next();
    }

    SingleLinkNode<Integer> popped = head;
    head = head.next();
    return popped.value().intValue();
  }

  public int min() {
    if(min == null) { throw new RuntimeException("No more values!"); }

    return min.value().value().intValue();
  }
}
