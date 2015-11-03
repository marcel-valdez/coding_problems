package chapter9;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.util.*;

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
