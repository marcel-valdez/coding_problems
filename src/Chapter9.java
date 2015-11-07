package chapter9;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.util.*;
import java.util.stream.*;

import structures.*;

class StepsPermutationCounter {
  public int count(int steps) {
    if(steps < 0) { throw new RuntimeException("Steps must be a positive number"); }

    return count(new int[steps], steps);
  }

  private int count(int[] cache, int steps) {
    if(steps <= 2) {
      return steps;
    }

    if(cache[steps - 1] == 0) {
      cache[steps - 1] =
        1 + count(cache, steps - 1)
      + 1 + count(cache, steps - 2)
      + 1 + count(cache, steps - 3);
    }

    return cache[steps -1];
  }
}

class PathPermutationCounter {

  public int count(int srcX, int srcY, int destX, int destY) {
    // main route + alternatives
    return count(new Integer[destX + 1][destY + 1], srcX, srcY, destX, destY);
  }

  Integer count(Integer[][] cache, int srcX, int srcY, int destX, int destY) {
    if(srcX == destX && srcY == destY) { return 1; }

    Integer count = get(cache, srcX, srcY, destY + 1);
    if(count != null) {
      DEBUG.println("Using cache for: " + srcX + ", " + srcY);
      return count;
    } else {
      count = 0;
    }

    if(srcX != destX) {
     count += count(cache, srcX + 1, srcY, destX, destY);
    }

    if(srcY != destY) {
      count +=  count(cache, srcX, srcY + 1, destX, destY);
    }

    set(cache, srcX, srcY, count);

    return count;
  }

  Integer get(Integer[][] cache, int x, int y, int height) {
    if(cache[x] == null) {
      cache[x] = new Integer[height];
    }

    return cache[x][y];
  }

  private void set(Integer[][] cache, int x, int y, int value) {
    assert cache[x] != null;

    cache[x][y] = value;
  }
}

class BlockedPathPermutationCounter extends PathPermutationCounter {
  private static Integer BLOCKED = Integer.MIN_VALUE;
  public int countPaths(Integer[][] road, int srcX, int srcY, int destX, int destY) {
    if(destX < 0 || destX < 0) { throw new RuntimeException("Invalid destination."); }
    if(road[destX][destY] == BLOCKED) {
      throw new RuntimeException("Destination is blocked");
    }

    if(destX == destY && destX == 0) { return 1; }

    return count(road, srcX, srcY, destX, destY);
  }

  Integer count(Integer[][] road, int srcX, int srcY, int destX, int destY) {
    Integer count = get(road, srcX, srcY, destY + 1);
    if(BLOCKED.equals(count)) {
      return 0;
    }

    return super.count(road, srcX, srcY, destX, destY);
  }
}

class MagicIndexFinder {

  private static final Integer NOT_FOUND = Integer.MIN_VALUE;

  public Integer findMagicIndex(int[] array) {
    if(array == null || array.length == 0) {
      throw new RuntimeException("array cannot be null or empty");
    }
    Integer result = find(
      array,
      array.length / 2,
      0,
      array.length
    );

    if(result == NOT_FOUND) { return null; }

    return result;
  }

  // array: [ -2, -1, 0, 2, 4, 9, 10 ] (size: 7)
  // pivot: 3 | 5 | 4
  // start: 0 | 4 | 4
  // lengt: 7 | 3 | 1
  Integer find(int[] array, int pivot, int start, int length) {
    if(length <= 0) {
      return NOT_FOUND;
    }

    DEBUG.println("Range: [" + start + ".." + (start + length) + "]");

    if(pivot == array[pivot]) {
      return pivot;
    }

    // if a[i] is less than the start, then there is no way that
    // there could be a magic index in between a[start]..a[pivot]
    if(array[pivot] >= start) {
      Integer left = find(
        array,
        start + ((pivot - start) / 2),
        start,
        pivot - start);

      if(left != NOT_FOUND) {
        return left;
      }
    }

    // if the number at a[pivot] is greater than the maximum number a
    // number could have in the array, then not even duplicated
    // values could make it so that there is a magic index in the
    // range a[pivot]..a[n]
    if(array[pivot] <= pivot + length) {
      Integer right = find(
        array,
        pivot + (length - pivot + 1)/2,
        pivot + 1,
        length - pivot - 1);

      return right;
    }

    return NOT_FOUND;
  }
}

class SetSubsetsCreator {

  // A, B, C
  public <T> Set<SingleLinkNode<T>> createSubsets(T[] set) {
    if(set == null || set.length == 0) { return new HashSet<>(); }
    Set<SingleLinkNode<T>> subsets = new HashSet<>();
    createSubsets(subsets, set, null, -1);
    return subsets;
  }

  private <T> void createSubsets(
    Set<SingleLinkNode<T>> subsets,
    T[] set,
    SingleLinkNode<T> root,
    int rootIndex) {
    for(int i = rootIndex + 1; i < set.length; i++) {
      SingleLinkNode<T> subsetRoot = new SingleLinkNode<>(set[i]);
      subsetRoot.next(root);
      DEBUG.println(subsetRoot);
      subsets.add(subsetRoot);
      createSubsets(subsets, set, subsetRoot, i);
    }
  }
}

class StringPermutations {

  // a: { a }
  // ab: {
  //   ba
  //   ab
  // }
  // abc: {
  //   bac, bca
  //   cba, cab
  //   acb, abc
  // }
  public List<String> permutations(String str) {
    if(str == null || str.length() <= 1) {
      List<String> result = new ArrayList<>();
      result.add(str);
      return result;
    }

    return permute(str);
  }

  private List<String> permute(String str) {

    List<String> result = new ArrayList<>();
    LinkedListStack<List<Character>> permutations = new LinkedListStack<>();

    List<Character> first= new ArrayList<>();
    first.add(str.charAt(0));

    permutations.push(first);

    for(int j = 1; j < str.length(); j++) {
      LinkedListStack<List<Character>> nextPermutations = new LinkedListStack<>();
      while(!permutations.isEmpty()) {
        List<Character> chars = permutations.pop();
        for(int i = 0; i <= chars.size(); i++) {
          List<Character> permutation = copyWithInsertion(chars, str.charAt(j), i);
          if(j == str.length() - 1) {
            result.add(toString(permutation));
          } else {
            nextPermutations.push(permutation);
          }
        }
      }

      permutations = nextPermutations;
    }

    return result;
  }

  private static <T> List<T> copyWithInsertion(List<T> list, T inserted, int index) {
    List<T> copy = new ArrayList<>(list.size());
    int i = 0;
    for(T item : list) {
      if(i++ == index) { copy.add(inserted); }
      copy.add(item);
    }

    if(i == index) { copy.add(inserted); }

    return copy;
  }

  private static String toString(List<Character> chars) {
    char[] arr = new char[chars.size()];
    int i = 0;
    for(Character aChar : chars) {
      arr[i++] = aChar;
    }

    return new String(arr);
  }
}

class ParenthesisPrinter {
  // TODO: Write tests
  public List<String> permutations(int pairs) {
    if(pairs <= 0) { return new ArrayList<>(); }
    Set<Parens> content = new HashSet<>();
    content.add(Parens.empty());
    content = doPermutations(pairs, content, 1);
    return toStrings(content);
  }

  private List<String> toStrings(Set<Parens> parens) {
    List<String> result = new ArrayList<>();
    for(Parens pair : parens) {
      result.add(pair.toString());
    }

    return result;
  }

  private Set<Parens> doPermutations(int pairs, Set<Parens> content, int pairCount) {
    Set<Parens> nextValues = new HashSet<>();
    for(int i = pairCount+1; i <= pairs; i++) {
      for(Parens parens : content) {
        nextValues.add(DEBUG.println(parens.wrap()));
        nextValues.add(DEBUG.println(parens.before()));
        nextValues.add(DEBUG.println(parens.after()));
      }
      Set<Parens> tmp = content;
      content = nextValues;
      tmp.clear(); // assuming this is for free, and does not go delete 1 by 1
      nextValues = tmp;
    }

    return content;
  }

  private static class Parens {
    private final ContentPosition position;
    private final Parens content;
    private String toString;
    private int hashCode;

    private Parens(Parens content, ContentPosition position) {
      if(position == null) { throw new RuntimeException("position cannot be null."); }
      this.content = content;
      this.position = position;
      this.hashCode = Integer.MIN_VALUE;
    }

    private ContentPosition position() { return this.position; }

    private Parens content() { return this.content; }

    public static Parens empty() {
      return new Parens(null, ContentPosition.NONE);
    }

    public Parens wrap() {
      return new Parens(this, ContentPosition.WRAPPED);
    }

    public Parens before() {
      return new Parens(this, ContentPosition.BEFORE);
    }

    public Parens after() {
      return new Parens(this, ContentPosition.AFTER);
    }

    public String toString() {
      if(this.toString == null) {
        StringBuilder sb = new StringBuilder();
        appendString(sb);
        this.toString = sb.toString();
      }

      return this.toString;
    }

    private void appendString(StringBuilder sb) {
      if(this.toString != null) {
        sb.append(this.toString);
      } else {
        switch(this.position) {
          case NONE:
            sb.append("()");
            break;
          case BEFORE:
            sb.append("()");content.appendString(sb);
            break;
          case AFTER:
            content.appendString(sb);sb.append("()");
            break;
          case WRAPPED:
            sb.append("(");content.appendString(sb);sb.append(")");
            break;
        }
      }
    }

    public int hashCode() {
      if(this.hashCode == Integer.MIN_VALUE) {
        this.hashCode = this.toString().hashCode();
      }

      return this.hashCode;
    }

    public boolean equals(Object other) {
      if(!(other instanceof Parens)) { return false; }
      return this.toString().equals(other.toString());
    }

    private boolean equal(Object a, Object b) {
      if(a == null || b == null) { return b == a; }

      return a.equals(b);
    }
  }

  enum ContentPosition {
    NONE(7), WRAPPED(13), BEFORE(23), AFTER(41);

    private int hashCode;

    ContentPosition(int hashCode) {
      this.hashCode = hashCode;
    }
  }
}

class PaintFill {
  public int fill(byte[][] image, byte color, Point start) {
    if(image == null) { throw new RuntimeException("image cannot be null."); }
    if(image.length == 0) { throw new RuntimeException("Image cannot be of width 0"); }
    if(image[0].length == 0) { throw new RuntimeException("Image cannot be of height 0"); }

    int height = image[0].length;
    int width = image.length;
    byte filledColor = image[start.x][start.y];

    Stack<Point> moves = new Stack<>();
    moves.push(new Point(0, 0));

    int pixelsPainted = 0;
    while(!moves.isEmpty()) {
      Point move = moves.pop();
      if(move.fill(image, filledColor, color)) {
        pixelsPainted++;
        moves.push(move.left());
        moves.push(move.leftDown());
        moves.push(move.leftUp());

        moves.push(move.right());
        moves.push(move.rightUp());
        moves.push(move.rightDown());

        moves.push(move.up());
        moves.push(move.down());
      }
    }

    return pixelsPainted;
  }

  public static class Point {
    int x; int y;
    public Point(int x, int y) { this.x = x; this.y = y; }

    public boolean isWithinImage(byte[][] image) {
      return (x >= 0 && y >= 0) && x < image.length && y < image[0].length;
    }

    public boolean isOfColor(byte[][] image, byte color) {
      return isWithinImage(image) && image[x][y] == color;
    }

    public void paint(byte[][] image, byte color) {
      image[x][y] = color;
    }

    public Point left() { return new Point(x - 1, y); }
    public Point right() { return new Point(x + 1, y); }
    public Point up() { return new Point(x, y + 1); }
    public Point down() { return new Point(x, y - 1); }
    public Point leftUp() { return left().up(); }
    public Point leftDown() { return left().down(); }
    public Point rightUp() { return right().up(); }
    public Point rightDown() { return right().down(); }

    public boolean fill(byte[][] image, byte filledColor, byte color) {
      if(!isOfColor(image, color) &&  isOfColor(image, filledColor)) {
        paint(image, color);
        return true;
      }

      return false;
    }
  }
}

class ChangePermutations {
  // Problemas:
  // 1. No me fui directo al pintarron primero, intente empezar con codigo
  // 2. Me fui por la solucion mas complicada primero, implementar
  //    iterativamente un problema recursivo
  // 3. Lei el problema mal, la tarea simplemente era dar el numero de permutaciones
  //    no crear las formas de cambio de monedas.
  public LinkedListStack<SingleLinkNode<Coin>> changePermutations(int cents) {
    LinkedListStack<Permutation> currentPermutations = new LinkedListStack<>();
    LinkedListStack<Permutation> nextPermutations = new LinkedListStack<>();

    currentPermutations.push(new Permutation(cents, null));

    Map<Integer, LinkedListStack<Permutation>> memo = new HashMap<>();
    for(Coin coin : Coin.values()) {
      while(!currentPermutations.isEmpty()) {
        Permutation permutation = currentPermutations.pop();
        int denominationCount = permutation.remainder / coin.value();
        DEBUG.println("denominationCount: " + denominationCount + ", coin: " + coin);
        for(int i = 0; i <= denominationCount; i++) {
          int remainder = permutation.remainder - (i * coin.value());
          if(coin == Coin.PENNY && remainder != 0) {
            continue;
          }

          SingleLinkNode[] coins = produceCoins(coin, i);
          nextPermutations.push(
            permutation(
              coins[0], // head
              coins[1], // tail
              permutation,
              remainder
            )
          );
        }
      }

      LinkedListStack<Permutation> tmp = currentPermutations;
      currentPermutations = nextPermutations;
      nextPermutations = tmp;
    }

    return toResults(currentPermutations);
  }

  private SingleLinkNode[] produceCoins(Coin coin, int count) {
    SingleLinkNode<Coin> tail = null;
    SingleLinkNode<Coin> head = null;
    for(int i = 0; i < count; i++) {
      if(head == null) {
        tail = head = new SingleLinkNode<>(coin);
      } else {
        tail.next(new SingleLinkNode<>(coin));
        tail = tail.next();
      }
    }

    return new SingleLinkNode[] { head, tail };
  }

  private LinkedListStack<SingleLinkNode<Coin>> toResults(
    LinkedListStack<Permutation> permutations) {
    LinkedListStack<SingleLinkNode<Coin>> result = new LinkedListStack<>();

    while(!permutations.isEmpty()) {
      result.push(permutations.pop().coins);
    }

    return result;
  }

  private Permutation permutation(
    SingleLinkNode<Coin> head,
    SingleLinkNode<Coin> tail,
    Permutation other,
    int remainder) {
    if(head != null) {
      tail.next(other.coins);
    } else {
      head = other.coins;
    }

    DEBUG.println("Permutation(" + remainder +  "): " + head);
    return new Permutation(remainder, head);
  }

  private static class Permutation {
    public final int remainder;
    public final SingleLinkNode<Coin> coins;
    public Permutation(int remainder, SingleLinkNode<Coin> coins) {
      this.remainder = remainder; this.coins = coins;
    }
  }

  enum Coin {
    QUARTER(25), DIME(10), NICKEL(5), PENNY(1);

    private final int value;

    Coin(int value) { this.value = value; }

    public int value() { return this.value; }
  }
}

/**
 * Lesson learned: Do not to solve the problems using
 * technically optimized solutions (i.e. bit shifting,
 * iterative solutions to recursive problems, etc)
 * That complicates the problem's solution by a truck-load
 * always go for the solution that is 'algorithmically'
 * optimal, even if it does not perform the best
 * possible due to computer constraints.a
 */
class QueensBoardSolver {

  public List<boolean[][]> solve() {

    LinkedListStack<QueensPosition> positions = new LinkedListStack<>();
    LinkedListStack<QueensPosition> nextPositions = new LinkedListStack<>();

    Set<Long> positionsConsidered = new HashSet<>();
    positions.push(new QueensPosition());
    positionsConsidered.add(positions.peek().positions);
    // 8^3
    for(int queen = 0; queen < 8; queen++) {
      int combos = 0;
      int memoAccess = 0;
      while(!positions.isEmpty()) {
        QueensPosition position = positions.pop();
        for(int x = 0; x < 8; x++) {
          for(int y = 0; y < 8; y++) {
            if(!position.isOccupied(x, y)) {
              QueensPosition newPosition = position.place(x, y);
              if(!positionsConsidered.contains(newPosition.positions)) {
                positionsConsidered.add(newPosition.positions);
                combos++;
                nextPositions.push(newPosition);
              } else {
                memoAccess++;
              }
            }
          }
        }
      }

      DEBUG.println("Q: " + queen + " Combos: " + combos +  " Memos: " + memoAccess);
      LinkedListStack<QueensPosition> tmp = positions;
      positions = nextPositions;
      nextPositions = tmp;
    }

    List<boolean[][]> results = new ArrayList<>();
    while(!positions.isEmpty()) {
      results.add(positions.pop().asBitboard());
    }

    return results;
  }

  public static class QueensPosition {
    private static final long ROW = 0b11111111l;
    private static final long COLUMN =
      0b0000000100000001000000010000000100000001000000010000000100000001l;
    private static final long LEFT_DIAGONAL = // '\'
      0b1000000001000000001000000001000000001000000001000000001000000001l;
    private static final long RIGHT_DIAGONAL = // '/'
      0b0000000100000010000001000000100000010000001000000100000010000000l;

    private long positions;
    private long touchedSquares;
    public QueensPosition() {
      this(0, 0);
    }

    public QueensPosition(long positions, long touchedSquares) {
      this.positions = positions;
      this.touchedSquares = touchedSquares;
    }

    // assumes x from 0 to 7, y from 0 to 7
    public QueensPosition place(int x, int y) {
      long newTouchedSquares = touchedSquares | touchedSquaresByPosition(x, y);
      long newPositions = positions | position(x, y);
      return new QueensPosition(newPositions, newTouchedSquares);
    }

    private long touchedSquaresByPosition(int x, int y) {
      // produce the touched rows
      long leftDiagonal = LEFT_DIAGONAL; // '\'
      long rightDiagonal = RIGHT_DIAGONAL; // '/'
      int moveVerticallyBy = y - x;
      int moveLeftDiagonalHorizontallyBy = x - y - 1;
      int moveRightDiagonalHorizontallyBy = x - y;
// TODO: Figure out how to mark the diagonals as 'touched' by a Queen
// in a given position
      if(moveLeftDiagonalHorizontallyBy < 0) {
        // move to the right
        leftDiagonal <<= Math.abs(moveLeftDiagonalHorizontallyBy);
      } else if(moveLeftDiagonalHorizontallyBy > 0) {
        // move to the left
        leftDiagonal >>= Math.abs(moveLeftDiagonalHorizontallyBy);
      }

      if(moveRightDiagonalHorizontallyBy < 0) {
        // move to the right
        rightDiagonal <<= Math.abs(moveRightDiagonalHorizontallyBy);
      } else if(moveRightDiagonalHorizontallyBy > 0) {
        // move to the left
        rightDiagonal >>= Math.abs(moveRightDiagonalHorizontallyBy);
      }

      long occupiedRow = ROW << (y*8);
      long occupiedColumn = COLUMN << x;
      long result = occupiedRow | occupiedColumn | leftDiagonal | rightDiagonal;

      return result;
    }

    public boolean isOccupied(int x, int y) {
      return (touchedSquares & position(x, y)) != 0;
    }

    public long positions() {
      return this.positions;
    }

    public long touchedSquares() {
      return this.touchedSquares;
    }

    public int hashCode() {
      return (int) this.positions;
    }

    public boolean equals(Object other) {
      if(!(other instanceof QueensPosition)) {
        return false;
      }

      return positions == ((QueensPosition) other).positions;
    }

    public boolean[][] asBitboard() {
      boolean[][] bitboard = new boolean[8][8];
      for(int y = 0; y < 8; y++) {
        for(int x = 0; x < 8; x++) {
          bitboard[x][y] = (positions & position(x, y)) != 0;
        }
      }

      return bitboard;
    }

    private static long position(int x, int y) {
      return (1l << (y * 8)) << x;
    }
  }
}

class BoxStacker {
  private static final int HEAD = 0;
  private static final int TAIL = 1;
  private static final RecursionResult NONE = new RecursionResult(-1, null);

  public SingleLinkNode<Box> stack(List<Box> boxes) {
    if(boxes == null) { throw new RuntimeException("boxes cant be null"); }
    if(boxes.size() == 0) { return null; }

    Map<Box, RecursionResult> memo = new HashMap<>();
    RecursionResult tallest = null;
    for(Box box : boxes) {
      SingleLinkNode<Box> head = new SingleLinkNode<>(box);
      if(tallest == null) {
        tallest = result(head, box.h());
      }

      RecursionResult boxResult = maxStack(memo, head, boxesThatFit(box, boxes), box.h(), tallest);
      if(boxResult.h() > tallest.h()) {
        tallest = boxResult;
      }
    }

    return tallest.stack();
  }

  // returns the talles stack when bottom is the box at the bottom of the stack
  private RecursionResult maxStack(
    Map<Box, RecursionResult> memo,
    SingleLinkNode<Box> bottom,
    List<Box> remainingBoxes, // remaining boxes
    int currentHeight,
    RecursionResult tallest
  ) {

    if(memo.containsKey(bottom.value())) {
      RecursionResult memoValue = getMemo(memo, bottom, currentHeight);
      if(memoValue == NONE) {
        return NONE;
      } else {
        return memoValue;
      }
    }

    // can we cache the results based on the bottom box?
    // that is a good question.
    if(remainingBoxes.size() == 0) {
      return result(bottom, currentHeight);
    }

    RecursionResult localTallest = null;
    for(Box potentialNext : remainingBoxes) {
      // optimization: do not even try to recurse further
      // if stack can't be taller than tallest even if
      // all boxes taht fit in it were stacked.
      List<Box> potentialStack = boxesThatFit(potentialNext, remainingBoxes);
      int potentialHeight = sumOfHeights(potentialStack);
      int potentialTotalHeight = currentHeight + potentialNext.h() + potentialHeight;
      if(potentialTotalHeight > tallest.h()) {
        SingleLinkNode<Box> newBottom = new SingleLinkNode<>(potentialNext);
        newBottom.next(bottom);
        RecursionResult stackResult = maxStack(
          memo,
          newBottom,
          potentialStack,
          currentHeight + potentialNext.h(),
          tallest
        );

        if(localTallest == null || stackResult.h() > localTallest.h()) {
          localTallest = stackResult;
        }

        if(stackResult.h() > tallest.h()) {
          DEBUG.println("New tallest: " + stackResult);
          tallest = stackResult;
        }
      }
    }

    memoize(memo, bottom, currentHeight, localTallest);
    // if no stack better than the one passed in, then stop execution.
    return tallest;
  }

  private RecursionResult getMemo(
    Map<Box, RecursionResult> memo,
    SingleLinkNode<Box> bottom,
    int bottomHeight) {

    RecursionResult memoized =  memo.get(bottom.value());
    SingleLinkNode<Box>[] headTail = copyTopStack(null, memoized.stack());
    headTail[TAIL].next(bottom);
    DEBUG.println("Found memo for: " + bottom.value() + ", requested by: " + bottom.next());
    return result(headTail[HEAD], memoized.h() + bottomHeight);
  }

  private void memoize(
    Map<Box, RecursionResult> memo,
    SingleLinkNode<Box> bottom,
    int bottomHeight,
    RecursionResult result) {

    RecursionResult memoEntry = NONE;
    if(result != null) {
      SingleLinkNode<Box>[] headTail = copyTopStack(bottom, result.stack());
      memoEntry = result(headTail[HEAD], result.h() - bottomHeight);
    }

    memo.put(
      bottom.value(),
      memoEntry
    );
  }

  private SingleLinkNode<Box>[] copyTopStack(
    SingleLinkNode<Box> bottom, SingleLinkNode<Box> stack) {

    if(stack == bottom) {
      return null; // no boxes on top of the bottom
    }

    SingleLinkNode<Box> tail = copyNode(stack);
    SingleLinkNode<Box> head = tail;
    if(stack != null) {
      SingleLinkNode<Box> next = stack.next();
      while(next != null && next != bottom) {
        tail.next(copyNode(next));
        tail = tail.next();
        next = next.next();
      }
    }

    return new SingleLinkNode[] { head, tail };
  }

  private SingleLinkNode<Box> copyNode(SingleLinkNode<Box> node) {
    if(node == null) { return null; }
    return new SingleLinkNode<Box>(node.value());
  }

  private List<Box> boxesThatFit(Box bottom, List<Box> boxes) {
    return boxes.stream().filter(b -> b.fitsIn(bottom)).collect(Collectors.toList());
  }

  public int sumOfHeights(List<Box> boxes) {
    return boxes.stream().mapToInt(b -> b.h()).sum();
  }

  private static RecursionResult result(SingleLinkNode<Box> stack, int height) {
    return new RecursionResult(height, stack);
  }

  private static class RecursionResult {
    private final SingleLinkNode<Box> stack;
    private final int height;
    public RecursionResult(int height, SingleLinkNode<Box> stack) {
      this.stack = stack;
      this.height = height;
    }

    public SingleLinkNode<Box> stack() { return stack; }
    public int h() { return height; }


    public String toString() {
      return "{stack: " + stack + ", h: " + height + "}";
    }
  }
}


class Box {
  private int height, width, depth;
  private Box(int height, int width, int depth) {
    this.height = height;
    this.width = width;
    this.depth = depth;
  }

  public static Box box(int height, int width, int depth) {
    return new Box(height, width, depth);
  }

  public int h() { return height; }
  public int w() { return width; }
  public int d() { return depth; }

  public boolean fitsIn(Box box) {
    return h() < box.h() && w() < box.w() && d() < box.d();
  }

  @Override
  public boolean equals(Object other) {
    if(!(other instanceof Box)) { return false; }
    Box otherBox = (Box) other;
    return h() == otherBox.h() && w() == otherBox.w() && d() == otherBox.d();
  }

  @Override
  public int hashCode() {
    return 71 + h() * 31 + w() * 13 + d() * 23;
  }
  @Override
  public String toString() {
    return "[h:" + h() + " w:" + w() + " d:" + d() + "]";
  }
}

class ExpressionPermutator {
  public List<Expression> calculatePermutations(String expression, boolean desired) {
    List[][] memos = new List[expression.length()][expression.length() + 1];
    List<Expression> permutations =
      calculateAllPermutations(memos, expression, 0, expression.length());
    filterUndesiredPermutations(permutations, desired);

    return permutations;
  }

  private List<Expression> calculateAllPermutations(
    List[][] memos,
    String expression,
    int startIndex,
    int length) {
    List<Expression> permutations = new ArrayList<>();

    if(length == 0) {
      return permutations;
    }

    if(getMemo(memos, startIndex, length) != null) {
      return getMemo(memos, startIndex, length);
    }

    //DEBUG.println("permutations(" + startIndex + ", " + length + "): " + expression.substring(startIndex, startIndex + length));

    if(length == 1) {
      permutations.add(expr(expression.charAt(startIndex)));
      memo(memos, permutations, startIndex, length);
      return permutations;
    }

    if(length == 3) {
      permutations.add(expr(expression, startIndex));
      memo(memos, permutations, startIndex, length);
      return permutations;
    }

    if(startIndex == 0) {
      if(expression.length() < 3) {
        throw new RuntimeException("At least 3 chars are required, found: <" + expression + ">.");
      }
    }

    for(int i = 1; i < length; i+=2) {
      List<Expression> lefts =
        calculateAllPermutations(memos, expression, startIndex, i);

      Operator operator = oper(expression.charAt(startIndex + i));

      List<Expression> rights =
      calculateAllPermutations(memos, expression, startIndex + i + 1, length - i - 1);
      for(Expression left : lefts) {
        for(Expression right : rights) {
          permutations.add(new Expression(left, right, operator));
        }
      }
    }

    memo(memos, permutations, startIndex, length);
    return permutations;
  }

  private List<Expression> getMemo(List[][] memos, int startIndex, int length) {
    return memos[startIndex][length];
  }

  private void memo(List[][] memos, List<Expression> result, int startIndex, int length) {
    //DEBUG.println("memo(" + startIndex + "," + length + "): " + result);
    memos[startIndex][length] = result;
  }

  private Expression expr(String expr, int index) {
    return new Expression(
      new Expression(oper(expr.charAt(index))),
      new Expression(oper(expr.charAt(index + 2))),
      oper(expr.charAt(index + 1))
    );
  }

  private Expression expr(char op) {
    return new Expression(oper(op));
  }

  private Operator oper(char op) {
    return Operator.value(op);
  }

  private void filterUndesiredPermutations(List<Expression> permutations, boolean desired) {
    for(int i = 0; i < permutations.size(); i++) {
      if(permutations.get(i).eval() != desired) {
        permutations.remove(i);
        i--;
      }
    }
  }
}

class Expression {
  private final Expression left;
  private final Expression right;
  private final Operator operator;
  private Boolean memo;
  private String asString;

  public Expression(Expression left, Expression right, Operator operator) {
    if(left == null || right == null || operator == null) {
      throw new RuntimeException("All parameters must be set to non-null");
    }

    if(operator == Operator.TRUE || operator == Operator.FALSE) {
      throw new RuntimeException("Composed operator cannot be a constant. (" + operator + ")");
    }

    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  public Expression(Operator operator) {
    if(operator != Operator.FALSE && operator != Operator.TRUE) {
      throw new RuntimeException(
        "Only true and false can have no left and right ops, but found: " + operator);
    }

    this.left = null;
    this.right = null;
    this.operator = operator;
  }

  public boolean eval() {
    if(this.memo == null) {
      this.memo = doEval();
    }

    return this.memo;
  }

  private boolean doEval() {
    switch(operator) {
      case OR:
        return left.eval() || right.eval();
      case AND:
        return left.eval() && right.eval();
      case XOR:
        return left.eval() ^ right.eval();
      case FALSE:
        return false;
      case TRUE:
        return true;
    }

    throw new RuntimeException("Invalid operator: " + operator);
  }

  @Override
  public String toString() {
    if(asString == null) {
      StringBuilder sb = new StringBuilder();
      appendString(sb);
      asString = sb.toString();
    }

    return asString;
  }

  private void appendString(StringBuilder sb) {
    if(operator == Operator.FALSE || operator == Operator.TRUE) {
      sb.append(operator.op());
    } else {
      sb.append('(');
      left.appendString(sb);
      sb.append(operator.op());
      right.appendString(sb);
      sb.append(')');
    }
  }
}

enum Operator {
  OR('|'), AND('&'), XOR('^'), FALSE('0'), TRUE('1');

  private final char op;

  Operator(char op) {
    this.op = op;
  }

  public static Operator value(char op) {
    switch(op) {
      case '|': return OR;
      case '&': return AND;
      case '^': return XOR;
      case '0': return FALSE;
      case '1': return TRUE;
    }

    throw new RuntimeException("Operator: <" + op + "> unkown.");
  }

  public char op() { return this.op; }
  public boolean matches(char op) {
    return this.op == op;
  }
}
