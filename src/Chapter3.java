package chapter3;

import static debug.Debugger.DEBUG;

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


class StackSorter {
  private static int LEFT = 0;
  private static int RIGHT = 1;

  public void sort(LinkedListStack<Integer> stack) {
    if(stack == null) { throw new RuntimeException("Null stack!"); }
    if(stack.size() == 0) { return; }

    LinkedListStack<Integer> aux = new LinkedListStack<>();
    sort(stack, aux, new int[] { -1, -1 }, "ASC");
  }

  public int[] partition(
    Integer pivot,
    LinkedListStack<Integer> left,
    LinkedListStack<Integer> right,
    int leftSize,
    String order) {

    if(isEmpty(left) || leftSize == 0) {
      return new int[] { 0, 0 };
    }

    boolean leftSizeUndetermined = leftSize < 0;
    int[] foundSizes = new int[2];
    int rightSize = 0;
    int i = 0;
    DEBUG.println("\npartition pivot: " + pivot + ", left: " + left + ", leftSize: " + leftSize);
    DEBUG.println("lo: " + left + ", hi: " + right);

    while(
      !isEmpty(left) &&
      comparePivot(pivot, left.peek(), order) &&
      (leftSize > 0 || leftSizeUndetermined)
      ) {
      right.push(left.pop());
      rightSize++;
      leftSize--;
    }
    if(isEmpty(left) || leftSize == 0) {
      // everything was larger than the pivot
      // eventually we will hit this
      foundSizes[LEFT] = 0;
      foundSizes[RIGHT] = rightSize;
    } else {
      DEBUG.println("Stopped at: " + left.peek() + " with leftSize: " + leftSize);
      DEBUG.println("lo: " + left + ", hi: " + right);

      Integer lessThanPivot = left.pop();
      int[] partSizes = partition(pivot, left, right, leftSize - 1, order);
      left.push(lessThanPivot);
      foundSizes[LEFT] = partSizes[LEFT] + 1;
      foundSizes[RIGHT] = partSizes[RIGHT] + rightSize;
    }

    return foundSizes;
  }

  private void sort(LinkedListStack<Integer> left, LinkedListStack<Integer> right, int[] sizes, String order) {
    DEBUG.println("--- sort sizes[LEFT]: "+ sizes[LEFT] + ", sizes[RIGHT]: " + sizes[RIGHT] + " -------");
    if(sizes[LEFT] == 1 || sizes[LEFT] == 0 || isEmpty(left)) {
      return;
    }

    if(sizes[LEFT] == 2) {
      Integer first = left.pop();
      Integer second = left.pop();

      if(areSorted(first, second, order)) {
        left.push(second);  // if they are already sorted, then
        left.push(first); // put them back the way they were
      } else {
        left.push(first);
        left.push(second);
      }

      return;
    }

    Integer pivot = left.pop();
    int[] partitionSizes = partition(pivot, left, right, sizes[LEFT] - 1, order);
    DEBUG.println("--- pivot: " + pivot + ", left: " + left + ", right: " + right);
    // sort left numbers
    sort(left, right, partitionSizes, order);
    // sort right numbers in opposite order so that we can push them back in correct order
    String rightOrder = oppositeOrder(order);
    sort(right, left, new int[] { partitionSizes[RIGHT], partitionSizes[LEFT] }, rightOrder);

    DEBUG.println("merge: " + left + " =>" + pivot + "<= " + right);
    mergePartitions(left, pivot, right, partitionSizes[RIGHT]);
  }

  private void mergePartitions(
    LinkedListStack<Integer> left,
    Integer pivot,
    LinkedListStack<Integer> right,
    int rightSize) {
    // merged results end up in the left stack
    left.push(pivot);
    for(int i = 0; i < rightSize; i++) {
      left.push(right.pop());
    }
  }

  private boolean comparePivot(int pivot, int b, String order) {
    if(order.equals("ASC")) { return b >= pivot; }
    if(order.equals("DESC")) { return b <= pivot; }
    throw new IllegalStateException("Invalid order: " + order);
  }

  private boolean areSorted(int first, int second, String order) {
    boolean ascending = order.equals("ASC");
    boolean firstIsGreater = first > second;
    boolean descending = !ascending;
    boolean firstIsSmaller = !firstIsGreater;
    return (firstIsGreater && ascending) || (firstIsSmaller && descending);
  }

  private static String oppositeOrder(String order) {
    return order.equals("ASC") ? "DESC" : "ASC";
  }
  private boolean isEmpty(LinkedListStack<Integer> stack) {
    return stack.size() == 0;
  }
}


class AnimalShelter {

  private AnimalLink oldest;
  private AnimalLink youngest;

  private AnimalLink oldestCat;
  private AnimalLink oldestDog;
  private AnimalLink youngestCat;
  private AnimalLink youngestDog;

  public void enqueue(Animal animal) {
    AnimalLink animalLink = new AnimalLink(animal);
    if(oldest == null) { youngest = oldest = animalLink; }
    else {
      animalLink.prevAnimal(youngest);
      youngest.nextAnimal(animalLink);
      youngest = animalLink;
    }

    if(animal.type().equals("cat")) {
      if(oldestCat == null) { youngestCat = oldestCat = animalLink; }
      else {
        animalLink.prevOfType(youngestCat);
        youngestCat.nextOfType(animalLink);
        youngestCat = animalLink;
      }
    } else { // assume it is a dog
      if(oldestDog == null) { youngestDog = oldestDog = animalLink; }
      else {
        animalLink.prevOfType(youngestDog);
        youngestDog.nextOfType(animalLink);
        youngestDog = animalLink;
      }
    }
  }

  public Optional<Animal> dequeue() {
    if(oldest == null) { return Optional.<Animal>empty(); }

    if(oldest == oldestDog) {
      return dequeueDog();
    } else if (oldest == oldestCat) {
      return dequeueCat();
    } else {
      throw new RuntimeException(
        "Oldest was neither oldestCat nor oldestDog. \n" +
        "oldest: " + oldest +  "\n" +
        "oldestDog: " + oldestDog + "\n" +
        "oldestCat: " + oldestCat + "\n");
    }
  }

  public Optional<Animal> dequeueDog() {
    DEBUG.println("Removing dog!");
    if(oldestDog == null) { return Optional.<Animal>empty(); }

    Optional<Animal> result = Optional.of(oldestDog.getAnimal());

    dequeueAnimal(oldestDog);

    if(oldestDog == youngestDog) { youngestDog = null; }
    oldestDog = oldestDog.nextOfType();

    return result;
  }

  public Optional<Animal> dequeueCat() {
    DEBUG.println("Removing cat!");
    if(oldestCat == null) { return Optional.<Animal>empty(); }

    Optional<Animal> result = Optional.of(oldestCat.getAnimal());
    dequeueAnimal(oldestCat);

    if(oldestCat == youngestCat) { youngestCat = null; }
    oldestCat = oldestCat.nextOfType();

    return result;
  }

  private void dequeueAnimal(AnimalLink animalLink) {
    if(animalLink.prevAnimal() != null) {
      animalLink.prevAnimal().nextAnimal(animalLink.nextAnimal());
    }

    if(animalLink.nextAnimal() != null) {
      animalLink.nextAnimal().prevAnimal(animalLink.prevAnimal());
    }

    if(animalLink.nextOfType() != null) {
      animalLink.nextOfType().prevOfType(null);
    }

    if(animalLink == oldest) {
      oldest = animalLink.nextAnimal();
    }

    if(animalLink == youngest) {
      youngest = animalLink.prevAnimal();
    }
  }

  static class AnimalLink {
    private final Animal animal;
    private AnimalLink nextOfType;
    private AnimalLink nextAnimal;

    private AnimalLink prevOfType;
    private AnimalLink prevAnimal;

    public AnimalLink(Animal animal) {
      this.animal = animal;
    }

    public Animal getAnimal() { return this.animal; }

    private void nextOfType(AnimalLink next) {
      this.nextOfType = next;
    }

    public AnimalLink nextOfType() {
      return this.nextOfType;
    }

    public void nextAnimal(AnimalLink next) {
      this.nextAnimal = next;
    }

    public AnimalLink nextAnimal() {
      return this.nextAnimal;
    }

    private void prevOfType(AnimalLink prev) {
      this.prevOfType = prev;
    }

    public AnimalLink prevOfType() {
      return this.prevOfType;
    }

    public void prevAnimal(AnimalLink prev) {
      this.prevAnimal = prev;
    }

    public AnimalLink prevAnimal() {
      return this.prevAnimal;
    }

    public String toString() { return this.getAnimal().toString(); }
  }

  static class Animal {
    private static volatile int COUNT = 0;

    private final int age;
    private final String type;

    public Animal(String type) {
      this.type = type;
      this.age = ++COUNT;
    }

    public int age() { return this.age; }
    public String type() { return this.type; }

    public String toString() { return "{ age: " + age + ", type: " + type + " }"; }
  }
}
