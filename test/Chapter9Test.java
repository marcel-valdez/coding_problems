package chapter9;

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

public class Chapter9Test extends ChapterTestBase {

  private StepsPermutationCounter stepsCounter = new StepsPermutationCounter();
  private PathPermutationCounter pathsCounter = new PathPermutationCounter();

  @After
  public void disableDebug() {
    DEBUG.disable();
  }

  @Test
  public void testStepsPermutationCounter() {
    checkStepsPermutationCounter(0, 0);
    checkStepsPermutationCounter(1, 1);
    checkStepsPermutationCounter(2, 2);
    checkStepsPermutationCounter(3, 6);

    checkStepsPermutationCounter(4, 12);
    checkStepsPermutationCounter(5, 23);
    checkStepsPermutationCounter(6, 44);
  }

  private void checkStepsPermutationCounter(int steps, int expected) {
    areEqual(stepsCounter.count(steps), expected);
  }

  @Test
  public void testPathPermutationCounter() {
    checkPathPermutationCounter(point(0, 0), point(1, 0), 1);
    checkPathPermutationCounter(point(0, 0), point(0, 1), 1);

    checkPathPermutationCounter(point(0, 0), point(1, 1), 2);

    checkPathPermutationCounter(point(0, 0), point(1, 2), 3);
    checkPathPermutationCounter(point(0, 0), point(2, 1), 3);

    checkPathPermutationCounter(point(0, 0), point(2, 2), 6);
    checkPathPermutationCounter(point(0, 0), point(3, 3), 20);
  }

  private void checkPathPermutationCounter(Point src, Point dest, int expected) {
    areEqual(pathsCounter.count(src.x, src.y, dest.x, dest.y), expected);
  }

  @Test
  public void testBlockedPathPermutationCounter_middle() {
    // given
    Integer[][] road = new Integer[3][3];
    road[1][1] = Integer.MIN_VALUE;
    BlockedPathPermutationCounter target = new BlockedPathPermutationCounter();
    // when
    int actual = target.countPaths(road, 0, 0, 2, 2);
    // then
    areEqual(actual, 2);
  }

  @Test
  public void testBlockedPathPermutationCounter_upperRightCorner() {
    // given
    Integer[][] road = new Integer[3][3];
    road[2][0] = Integer.MIN_VALUE;
    BlockedPathPermutationCounter target = new BlockedPathPermutationCounter();
    // when
    int actual = target.countPaths(road, 0, 0, 2, 2);
    // then
    areEqual(actual, 5);
  }

  private Point point(int x, int y) { return new Point(x, y); }

  private static class Point {
    int x; int y;
    public Point(int x, int y) { this.x = x; this.y = y; }
  }

  @Test
  public void testMagicIndexFinder() {
    checkMagicIndexFinder(new int[] { -2, -1, 0, 2, 4, 9, 10 }, 4);
    checkMagicIndexFinder(new int[] { -2, -1, 4, 4, 4, 9, 10 }, 4);
    checkMagicIndexFinder(new int[] { -2, -1, 0, 2, 5, 9, 10 }, null);
    checkMagicIndexFinder(new int[] { -2 }, null);
    checkMagicIndexFinder(new int[] { -1, 1 }, 1);
    checkMagicIndexFinder(new int[] { 0, 2 }, 0);
    checkMagicIndexFinder(new int[] { -3, -4, 2 }, 2);
    checkMagicIndexFinder(new int[] { -3, 1, 4 }, 1);
    checkMagicIndexFinder(new int[] { -3, 1, 4 }, 1);
    checkMagicIndexFinder(new int[] { 2, 2, 2 }, 2);
  }

  private void checkMagicIndexFinder(int[] numbers, Integer expected) {
    // given
    MagicIndexFinder finder = new MagicIndexFinder();
    // when
    Integer magic = finder.findMagicIndex(numbers);
    // then
    areEqual(magic, expected);
  }

  @Test
  public void testSubsetsBuilder() {
    // given
    Integer[] set = { 1, 2, 3 };
    SetSubsetsCreator subsetCreator = new SetSubsetsCreator();
    // when
    Set<SingleLinkNode<Integer>> subsets = subsetCreator.createSubsets(set);
    // then
    isTrue(subsets.contains(nodes(1)));
    isTrue(subsets.contains(nodes(1, 2)));
    isTrue(subsets.contains(nodes(1, 2, 3)));

    isTrue(subsets.contains(nodes(2)));
    isTrue(subsets.contains(nodes(2, 3)));

    isTrue(subsets.contains(nodes(3)));
  }

  private static SingleLinkNode<Integer> nodes(Integer ... values) {
    SingleLinkNode<Integer> tail = null;
    for(Integer value : values) {
      SingleLinkNode<Integer> newTail = new SingleLinkNode<>(value);
      newTail.next(tail);
      tail = newTail;
    }

    return tail;
  }

  @Test
  public void testStringPermutations() {
    checkStringPermutations("");
    checkStringPermutations("a");
    checkStringPermutations("ab", "ba");
    checkStringPermutations("abc", "bac", "bca", "cba", "cab", "acb");
    checkStringPermutations(
      "dabc", "dbac", "dbca", "dcba", "dcab", "dacb",
      "adbc", "bdac", "bdca", "cdba", "cdab", "adcb",
      "abdc", "badc", "bcda", "cbda", "cadb", "acdb",
      "abcd", "bacd", "bcad", "cbad", "cabd", "acbd"
   );
  }

  private void checkStringPermutations(String ... permutations) {
    StringPermutations target = new StringPermutations();
    // when
    List<String> actual = target.permutations(permutations[0]);
    // then
    areEqual(actual.size(), permutations.length);
    checkList(actual, permutations);
  }

  @Test
  public void testParenthesisPrinter() {
    ParenthesisPrinter printer = new ParenthesisPrinter();
    checkList(printer.permutations(1), "()");
    checkList(printer.permutations(2), "(())", "()()");
    checkList(printer.permutations(3), "(())()", "((()))", "()(())", "(()())","()()()");
  }

  private <T> void checkList(List<T> container, T ... items) {
    areEqual(container.size(), items.length);
    for(T item : items) {
      isTrue("Element not found: " + item, container.contains(item));
    }
  }

  @Test
  public void testPaintFill() {
    // given
    byte[][] image = {
      { 0, 1, 1, 0 },
      { 0, 0, 2, 1 },
      { 1, 1, 0, 1 },
      { 0, 0, 1, 0 }
    };

    byte[][] expected = {
      { 2, 1, 1, 0 },
      { 2, 2, 2, 1 },
      { 1, 1, 2, 1 },
      { 2, 2, 1, 2 }
    };

    PaintFill fill = new PaintFill();

    // when
    int pixelsPainted = fill.fill(image, (byte) 2, new PaintFill.Point(0, 0));

    // then
    areEqual(image, expected);
    // then one fill for each pixel painted
    areEqual(pixelsPainted, 7);
  }

  @Test
  public void testChangePermutations() {
    // given
      // 10 pennies
      // 2 nickels
      // 1 nickel + 5 cents
      // 1 dime
    checkChangePermutations(4, 1);
    checkChangePermutations(9, 2);
    checkChangePermutations(10, 4);
    checkChangePermutations(26, 13);

  }

  private void checkChangePermutations(int cents, int expectedCount) {

    ChangePermutations permutations = new ChangePermutations();
    // when
    LinkedListStack<SingleLinkNode<ChangePermutations.Coin>> change =
      permutations.changePermutations(cents);
    // then
    areEqual(change.size(), expectedCount);
    SingleLinkNode<ChangePermutations.Coin> permutation;
    while(!change.isEmpty()) {
      permutation = change.pop();
      areEqual(sum(permutation), cents);
    }
  }

  private int sum(SingleLinkNode<ChangePermutations.Coin> coins) {
    int value = 0;
    DEBUG.println(coins);
    while(coins != null) {
      value += coins.value().value();
      coins = coins.next();
    }
    return value;
  }

  @Test @Ignore
  public void testQueensPosition() {
    QueensBoardSolver.QueensPosition position = new QueensBoardSolver.QueensPosition();

    areEqual(position.place(0,0).positions(), 1L);
    areEqual(position.place(7,7).positions(), (1L << 63));

    isTrue("Same position", position.place(0,0).isOccupied(0,0));
    isTrue("Vertical", position.place(0,0).isOccupied(0,1));
    isTrue("Horizontal", position.place(0,0).isOccupied(1,0));
    isTrue("Diagonal", position.place(0,0).isOccupied(1,1));

    QueensBoardSolver.QueensPosition test = new QueensBoardSolver.QueensPosition(
      position.place(4,0).touchedSquares(),
      0
    );
    DEBUG.enable();
    printBitboard(test.asBitboard());
    DEBUG.disable();
  }

  @Test @Ignore
  public void testQueensBoardSolver() {
    // given
    QueensBoardSolver solver = new QueensBoardSolver();
    // when
    if(true) { return; }
    DEBUG.enable();
    List<boolean[][]> result = solver.solve();
    // then
    for(boolean[][] position : result) {
      printBitboard(position);
      DEBUG.println("-------------");
      break;
    }
    areEqual(result.size(), 92);
    DEBUG.disable();
  }

  private static void printBitboard(boolean[][] position) {
    DEBUG.println("  0 1 2 3 4 5 6 7");
    for(int y = 0; y < position.length; y++) {
      DEBUG.print(y + " ");
      for(int x = 0; x < position.length; x++) {
        DEBUG.print(position[x][y] ? "X " : "- ");
      }
      DEBUG.println("");
    }
  }

  @Test
  public void testBoxStacker() {
    // given
    Box smallest =  box(1, 1, 1);
    Box secondSmallest =  box(2, 2, 2);
    Box middle = box(3, 3, 3);
    Box largest =  box(4, 4, 4);


    List<Box> boxes = boxes(
      smallest,
      secondSmallest,
      box(2, 5, 2),
      box(1, 2, 6),
      largest,
      middle,
      box(3, 2, 3)
    );

    SingleLinkNode<Box> largestNode = new SingleLinkNode<>(largest);
    SingleLinkNode<Box> middleNode = new SingleLinkNode<>(middle);
    middleNode.next(largestNode);
    SingleLinkNode<Box> secondSmallestNode = new SingleLinkNode<>(secondSmallest);
    secondSmallestNode.next(middleNode);
    SingleLinkNode<Box> smallestNode = new SingleLinkNode<>(smallest);
    smallestNode.next(secondSmallestNode);

    BoxStacker stacker = new BoxStacker();

    // when
    SingleLinkNode<Box> stack = stacker.stack(boxes);
    // then
    DEBUG.println(stack);
    areEqual(stack, smallestNode);
  }

  private List<Box> boxes(Box ... boxes) {
    List<Box> result = new ArrayList<>();
    for(Box box : boxes) {
      result.add(box);
    }

    return result;
  }

  private static Box box(int h, int w, int d) {
    return Box.box(h, w, d);
  }

  @Test
  public void testExpressionPermutator() {
    // given
    String expression = "1^0|0|1";
    ExpressionPermutator permutator = new ExpressionPermutator();
    boolean desired = false;
    // when
    List<Expression> permutations = permutator.calculatePermutations(expression, desired);
    // then
    DEBUG.enable();
    DEBUG.println(permutations);
    areEqual(permutations.size(), 2);
    for(Expression permutation : permutations) {
      areEqual(permutation.eval(), desired);
    }
    DEBUG.disable();
  }
}
