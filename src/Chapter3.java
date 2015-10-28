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

@SuppressWarnings({"unchecked"})
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

@SuppressWarnings({"unchecked"})
class SetOfStacks<T> {
  private volatile int size;
  private volatile int stackCount;
  private final int threshold;
  private final GrowingArray<LinkedListStack<T>> stacks;

  public SetOfStacks(int threshold) {
    this.threshold = threshold;
    this.stacks = (GrowingArray<LinkedListStack<T>>) (GrowingArray<?>) new GrowingArray<LinkedListStack>(LinkedListStack.class, 10);
    size = 0;
  }

  public void push(T item) {
    if(stackCount() == 0) {
      this.stacks.add(new LinkedListStack<T>());
    }

    LinkedListStack<T> stack = this.stacks.getLast();
    if(stack.size() == threshold) {
      stack = new LinkedListStack<T>();
      this.stacks.add(stack);
    }

    stack.push(item);
    this.size++;
  }

  public T peek() {
    if(this.size() == 0) {
      throw new IllegalStateException("No elements in stack");
    }

    return this.stacks.getLast().peek();
  }

  public T pop() {
    if(this.size() == 0) {
      throw new IllegalStateException("No elements in stack");
    }

    T item = this.stacks.getLast().pop();
    this.size--;

    if(this.stacks.getLast().size() == 0) {
      this.stacks.removeLast();
    }

    return item;
  }

  public T get(int index) {
    if(this.size() <= index) {
      throw new IllegalStateException("Index out of bounds");
    }

    int stackIndex = index / threshold;
    LinkedListStack<T> stack = this.stacks.get(stackIndex);

    int itemIndex = index != 0 ? index % threshold : 0;
    return getItemAtIndex(stack, itemIndex);
  }

  private T getItemAtIndex(LinkedListStack<T> stack, int index) {
    int itemsToPop = stack.size() - index - 1;
    LinkedListStack<T> temp = new LinkedListStack<T>();
    for(int i = 0; i < itemsToPop; i++) {
      temp.push(stack.pop());
    }

    T item = stack.peek();

    for(int i = 0; i < itemsToPop; i++) {
      stack.push(temp.pop());
    }

    return item;
  }

  public int stackCount() {
    return this.stacks.count();
  }

  public int size() {
    return this.size;
  }
}

@SuppressWarnings({"unchecked"})
class TowersOfHanoiSolver {

  public int solve(int numberOfDisks) {
    LinkedListStack<Integer>[] poles = new LinkedListStack[] {
      new LinkedListStack<Integer>(),
      new LinkedListStack<Integer>(),
      new LinkedListStack<Integer>()
    };

    for(int i = 0; i < numberOfDisks; i++) {
      poles[0].push(numberOfDisks - i);
    }

    return moveDiscs(poles, numberOfDisks, 0, 2);
  }

  private int moveDiscs(
    LinkedListStack<Integer>[] poles, int numberOfDisks, int from, int to) {
    if(numberOfDisks == 0) {
      return 0;
    }

    int intermediate = getIntermediatePoleIndex(from, to);

    return moveDiscs(poles, numberOfDisks - 1, from, intermediate)
         + moveDisc(poles, from, to)
         + moveDiscs(poles, numberOfDisks - 1, intermediate, to);
  }

  private static int getIntermediatePoleIndex(int from, int to) {
    return 3 - from - to;
  }

  int moveDisc(LinkedListStack<Integer>[] poles, int from, int to) {
    Integer movingDisc = poles[from].pop();

    if(poles[to].size() > 0 && movingDisc > poles[to].peek()) {
      String msg = String.format(
        "Cannot move a bigger disc (%s) on top of a smaller one (%s).",
        movingDisc,
        poles[to].peek()
      );
      throw new IllegalStateException(msg);
    }

    poles[to].push(movingDisc);

    return 1;
  }
}

/*
* TRUTH: I was distracted, because it was getting late and I wanted to finish!
*
* The error I made here was to rush to an answer, and determine that it was the
* best, without really asking myself, how can I avoid the expensive operation?
* + What I have to do is: identify the expensive operation (popping and pushing everytime)
*   and ask myself:
*   - Do I really need to do it in this expensive manner?
*   - Why do I need to do it in this expensive manner? (Answer: XXX)
*   - Why do I need XXX? (Answer: YYY)
*   - Why do I need YYY? (Answer: ZZZ) Result --> AHA!
*/
class QueueMadeOfStacks<T> {
  LinkedListStack<T> pushStack;
  LinkedListStack<T> popStack;

  public QueueMadeOfStacks() {
    this.pushStack = new LinkedListStack<>();
    this.popStack = new LinkedListStack<>();
  }

  public void queue(T item) {
    this.pushStack.push(item);
  }

  public T dequeue() {
    if(pushStack.size() == 0 && popStack.size() == 0) {
      throw new IllegalStateException("Queue is empty, cannot pop");
    }

    T item = peek();
    popStack.pop();

    return item;
  }

  public T peek() {
    flushPushStack();
    return popStack.peek();
  }

  public int size() {
    return this.pushStack.size() + this.popStack.size();
  }

  private void flushPushStack() {
    if(popStack.size() == 0) {
      while(pushStack.size() > 0) { popStack.push(pushStack.pop()); }
    }
  }
}
