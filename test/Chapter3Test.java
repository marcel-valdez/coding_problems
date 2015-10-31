package chapter3;

import static debug.Debugger.DEBUG;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;


import java.lang.*;
import java.util.*;
import java.util.stream.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;
import test.*;

public class Chapter3Test extends ChapterTestBase {

  private CoordinatedStackMaker coordinatedStackMaker = new CoordinatedStackMaker();

  @Test
  public void testThreeStacksWithSingleArray() {
    // given an array that can have 3 stacks of 10 elements in it.
    Object[] data = new Object[3*10];
    // when
    CoordinatedArrayStack[] stacks = coordinatedStackMaker.makeStacks(data, 3);
    // then
    isTrueThat(stacks.length, equalTo(3));
    isFalseThat(stacks[0], nullValue());
    isFalseThat(stacks[1], nullValue());
    isFalseThat(stacks[2], nullValue());

    checkCanUse(stacks, 10);
  }

  private void checkCanUse(CoordinatedArrayStack[] stacks, int times) {
    for(int i = 0; i < times; i++) {
      for(int j = 0; j < stacks.length; j++) {
        stacks[j].push(i + "" + j);
        isTrueThat(stacks[j].peek(), equalTo(i + "" + j));
      }
    }

    for(int i = times - 1; i >= 0; i--) {
      for(int j = stacks.length - 1; j >= 0; j--) {
        int stackIndex = stacks.length - j - 1;
        isTrueThat(stacks[stackIndex].peek(), equalTo(i + "" + stackIndex));
        isTrueThat(stacks[stackIndex].pop(), equalTo(i + "" + stackIndex));
      }
    }

    for(int i = 0; i < stacks.length; i++) {
      isTrueThat(stacks[i].size(), equalTo(0));
    }
  }

  @Test
  public void testTrackStackMin() {
    // given
    MinTrackingStack stack = new MinTrackingStack();
    // when
    stack.push(5);
    isTrueThat(stack.min(), equalTo(5));
    stack.push(4);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(4);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(7);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(4);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(8);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(6);
    isTrueThat(stack.min(), equalTo(4));
    stack.push(2);
    isTrueThat(stack.min(), equalTo(2));
    stack.push(3);
    isTrueThat(stack.min(), equalTo(2));
    stack.push(1);
    isTrueThat(stack.min(), equalTo(1));

    isTrueThat(stack.pop(), equalTo(1));
    isTrueThat(stack.min(), equalTo(2));

    isTrueThat(stack.pop(), equalTo(3));
    isTrueThat(stack.min(), equalTo(2));

    isTrueThat(stack.pop(), equalTo(2));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(6));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(8));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(4));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(7));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(4));
    isTrueThat(stack.min(), equalTo(4));

    isTrueThat(stack.pop(), equalTo(4));
    isTrueThat(stack.min(), equalTo(5));

    isTrueThat(stack.pop(), equalTo(5));
  }

  @Test
  public void testSetOfStacks() {
    // given
    SetOfStacks<Integer> stack = new SetOfStacks<>(3);
    // when [1]
    stack.push(1);
    areEqual(stack.stackCount(), 1);
    areEqual(stack.size(), 1);
    areEqual(stack.get(0), 1);

    // when [1 2]
    stack.push(2);
    // then
    areEqual(stack.get(1), 2);

    // when [1 2 3]
    stack.push(3);
    // then
    areEqual(stack.stackCount(), 1);
    areEqual(stack.size(), 3);
    areEqual(stack.get(1), 2);
    areEqual(stack.get(2), 3);

    // when [1 2 3][4]
    stack.push(4);
    // then
    areEqual(stack.stackCount(), 2);
    areEqual(stack.size(), 4);
    areEqual(stack.get(1), 2);
    areEqual(stack.get(2), 3);
    areEqual(stack.get(3), 4);

    // when [1 2 3] -> 4
    areEqual(stack.pop(), 4);
    // then
    areEqual(stack.stackCount(), 1);
    areEqual(stack.size(), 3);
    areEqual(stack.get(1), 2);
    areEqual(stack.get(2), 3);

    // when [1 2 3][4 5 6][7]
    stack.push(4); stack.push(5); stack.push(6); stack.push(7);
    // then
    areEqual(stack.get(1), 2);
    areEqual(stack.get(2), 3);
    areEqual(stack.get(3), 4);
    areEqual(stack.get(4), 5);
    areEqual(stack.get(5), 6);
    areEqual(stack.get(6), 7);

    // when [] -> 1 2 3 4 5 6 7
    stack.pop();stack.pop();stack.pop();stack.pop();stack.pop();
    stack.pop();stack.pop();
    areEqual(stack.size(), 0);
    areEqual(stack.stackCount(), 0);
  }

  @Test
  public void testTowersOfHanoiSolver() {
    // given
    int[][] expectedMoves = {
      { 0, 2 }, // [2,3] [] [1]
      { 0, 1 }, // [3] [2] [1]
      { 2, 1 }, // [3] [1 2] []
      { 0, 2 }, // [] [1 2] [3]
      { 1, 0 }, // [1] [2] [3]
      { 1, 2 }, // [1] [] [2 3]
      { 0, 2 }  // [] [] [1 2 3]
    };
    TowersOfHanoiListener verifier = new TowersOfHanoiListener() {
      public void onMove(int moveIndex, LinkedListStack<Integer>[] poles, int from, int to) {
        areEqual(expectedMoves[moveIndex][0], from);
        areEqual(expectedMoves[moveIndex][1], to);
      }
    };
    TowersOfHanoiSolver target = new TowersOfHanoiSolverStub(verifier);
    // when
    target.solve(3);
    // then
  }

  private static class TowersOfHanoiSolverStub extends TowersOfHanoiSolver {
    private static int moveIndex = 0;

    private TowersOfHanoiListener listener;

    public TowersOfHanoiSolverStub(TowersOfHanoiListener listener) {
      this.listener = listener;
    }

    @Override
    int moveDisc(LinkedListStack<Integer>[] poles, int from, int to) {
      listener.onMove(moveIndex++, poles, from, to);
      return super.moveDisc(poles, from, to);
    }
  }

  private static interface TowersOfHanoiListener {
    void onMove(int moveIndex, LinkedListStack<Integer>[] poles, int from, int to);
  }

  @Test
  public void testQueueMadeOfStacks() {
    // given
    QueueMadeOfStacks<Integer> queue = new QueueMadeOfStacks();
    // when
    queue.queue(1);
    queue.queue(2);
    queue.queue(3);

    // then
    areEqual(queue.size(), 3);
    areEqual(queue.peek(), 1);
    areEqual(queue.dequeue(), 1);

    // when
    queue.queue(4);
    // then
    areEqual(queue.size(), 3);
    areEqual(queue.peek(), 2);
    areEqual(queue.dequeue(), 2);
    areEqual(queue.size(), 2);
    areEqual(queue.peek(), 3);
    areEqual(queue.dequeue(), 3);
    areEqual(queue.size(), 1);

  }

  @Test
  public void testStackSorter() {
    // given
    StackSorter sorter = new StackSorter();
    LinkedListStack<Integer> stack = new LinkedListStack<>();
    int[] input = { 3, 5, 2, 1, 8, 9, 4 };
    pushAll(stack, input);
    int[] expected = { 9, 8, 5, 4, 3, 2, 1 };

    // when
    sorter.sort(stack);
    // then
    int[] actual = popAll(stack);
    areEqual(actual, expected);
  }

  @Test
  public void testPivot() {
    // given
    LinkedListStack<Integer> lo = new LinkedListStack<>();
    pushAll(lo, 9, 1, 2, 8, 7, 3, 4, 6);
    LinkedListStack<Integer> hi = new LinkedListStack<>();
    pushAll(hi, 19, 18, 11, 12, 17, 16, 13, 14);

    StackSorter sorter = new StackSorter();
    // when
    int[] sizes = sorter.partition(5, lo, hi, -1, "ASC");
    // then
    int[] expectedLo = { 4, 3, 2, 1 };
    int[] expectedHi = { 9, 8, 7, 6, 14, 13, 16, 17, 12, 11, 18, 19 };
    areEqual(popAll(lo), expectedLo);
    areEqual(popAll(hi), expectedHi);
    areEqual(sizes[0], 4);
    areEqual(sizes[1], 4);
  }

  @Test
  public void testPivotSubset() {
    // given
    LinkedListStack<Integer> lo = new LinkedListStack<>();
    pushAll(lo, 9, 1, 2, 8, 7, 3, 4, 6);
    LinkedListStack<Integer> hi = new LinkedListStack<>();

    StackSorter sorter = new StackSorter();
    // when
    int[] sizes = sorter.partition(5, lo, hi, 5, "ASC");
    // then
    int[] expectedLo = { 4, 3, 2, 1, 9 };
    int[] expectedHi = { 8, 7, 6 };
    areEqual(popAll(lo), expectedLo);
    areEqual(popAll(hi), expectedHi);
    areEqual(sizes[0], 2);
    areEqual(sizes[1], 3);
  }

  @Test
  public void testAnimalShelter() {
    // given
    AnimalShelter shelter = new AnimalShelter();
    // when
    shelter.enqueue(cat());
    shelter.enqueue(cat());
    shelter.enqueue(dog());
    shelter.enqueue(cat());
    shelter.enqueue(dog());
    shelter.enqueue(dog());

    // then
    AnimalShelter.Animal animal = shelter.dequeue().get();
    areEqual(animal.age(), 1);
    areEqual(animal.type(), "cat");

    animal = shelter.dequeueDog().get();
    areEqual(animal.age(), 3);
    areEqual(animal.type(), "dog");

    animal = shelter.dequeue().get();
    areEqual(animal.age(), 2);
    areEqual(animal.type(), "cat");

    // when
    shelter.enqueue(cat());

    // then
    animal = shelter.dequeueCat().get();
    areEqual(animal.age(), 4);
    areEqual(animal.type(), "cat");

    animal = shelter.dequeueCat().get();
    areEqual(animal.age(), 7);
    areEqual(animal.type(), "cat");

    animal = shelter.dequeue().get();
    areEqual(animal.age(), 5);
    areEqual(animal.type(), "dog");

    animal = shelter.dequeue().get();
    areEqual(animal.age(), 6);
    areEqual(animal.type(), "dog");
  }

  private static AnimalShelter.Animal cat() {
    return new AnimalShelter.Animal("cat");
  }

  private static AnimalShelter.Animal dog() {
    return new AnimalShelter.Animal("dog");
  }
}
