package chapter17;

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

public class Chapter17Test extends ChapterTestBase {

  @Test
  public void testInlineNumberSwap() {
    // given
    int[] a = { 1, 2, 5, 8, 17, 32, 65, 128, 255, 512, 1023 };
    int[] b = { 1, 3, 4*3, 9*3, 16*3, 31*3, 64*3, 127*3, 256*3, 511*3, 1022*3 };

    for(int i = 0; i < a.length; i++) {
      PairOfNumbers numbers = new PairOfNumbers(a[i], b[i]);
      InlineNumberSwapper swapper = new InlineNumberSwapper();
      // when
      swapper.swap(numbers);
      // then
      areEqual(numbers.first, b[i]);
      areEqual(numbers.second, a[i]);
    }
  }

  @Test
  public void testTicTacToeResultDetector() {
    checkBoard(new byte[][] {
        { 1, 1, 1 },
        { 0, 2, 0 },
        { 2, 0, 2 }
      },
      true
    );

    checkBoard(new byte[][] {
        { 0, 1, 1 },
        { 2, 1, 0 },
        { 2, 0, 2 }
      },
      false
    );

    checkBoard(new byte[][] {
        { 0, 1, 2 },
        { 2, 1, 0 },
        { 2, 1, 2 }
      },
      true
    );

    checkBoard(new byte[][] {
        { 1, 0, 0 },
        { 2, 1, 0 },
        { 2, 2, 1 }
      },
      true
    );

    checkBoard(new byte[][] {
        { 0, 2, 0 },
        { 2, 1, 0 },
        { 1, 2, 1 }
      },
      false
    );

    checkBoard(new byte[][] {
        { 0, 2, 1 },
        { 2, 1, 0 },
        { 1, 2, 0 }
      },
      true
    );
  }

  private void checkBoard(byte[][] board, boolean expected) {
    TicTacToeResultDetector detector = new TicTacToeResultDetector();

    boolean actual = detector.detectWinner(board);

    areEqual(toString(board), actual, expected);
  }

  private String toString(byte[][] board) {
    StringBuilder sb = new StringBuilder(12);
    sb.append("\n");

    for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
        if(board[i][j] == 0) {
           sb.append(" ");
        } else {
          if(board[i][j] == 1) { sb.append("X"); }
          if(board[i][j] == 2) { sb.append("O"); }
        }
        if(j < 2) { sb.append("|"); }
      }

      sb.append("\n");
      if(i < 2) { sb.append("-----\n"); }
    }

    return sb.toString();
  }

  @Test
  public void testTrailingZerosCounter() {
    NFactorialTrailingZeros target = new NFactorialTrailingZeros();
    DEBUG.enable();
    for(int n : new int[]
        { 1, 4, 5, 6, 9, 10, 11, 14, 15, 16, 19, 20 }) {
      long factorial = factorial(n);
      areEqual(
        format("For number %s (%s)", n, factorial),
        target.calculate(n),
        countTrailingZeros(factorial));
    }
  }

  private int countTrailingZeros(long number) {
    String str = number + "";
    int counter = 0;
    for(int i = str.length() - 1; i >= 0; i--) {
      if(str.charAt(i) == '0') {
        counter++;
      } else {
        break;
      }
    }

    return counter;
  }

  private long factorial(int n) {
    long result = 1;
    for(long i = 1; i <= n; i++) {
      result *= i;
    }

    return result;
  }

  @Test
  public void testLargestSumSequenceFinder() {
    checkLargestSumSequenceFinder(new int[] {3, -4, 9, 0, -8, 5, 2 }, 9);
    checkLargestSumSequenceFinder(new int[] { -1  }, -1);
    checkLargestSumSequenceFinder(new int[] { -1, -2, 0 }, 0);
    checkLargestSumSequenceFinder(new int[] { -4, -2, -1, -3 }, -1);
    checkLargestSumSequenceFinder(new int[] { 1, 2, 3}, 6);
    checkLargestSumSequenceFinder(new int[] { 4, -2, 3}, 5);
    checkLargestSumSequenceFinder(new int[] { 1, -5, 4, -2, 3, -2 }, 5);
  }

  private void checkLargestSumSequenceFinder(int[] numbers, int expectedMaxSum) {
    LargestSumSequenceFinder target = new LargestSumSequenceFinder();
    // when
    int actualMaxSum = target.find(numbers);
    // then
    areEqual(actualMaxSum, expectedMaxSum);
  }

  @Test
  public void testWordCounter() {
    // given
    String text = "My mother lives with her mother at her mother's house, with a " +
                  "motherload of stuff my mother's room, at Mothers City";
    String word = "Mother";
    int expectedCount = 4;
    WordCounter target = new WordCounter();
    // when
    int actualCount = target.count(text, word);
    // then
    areEqual(actualCount, expectedCount);
  }

  @Test
  public void testPairFinder() {
    // given
    int[] numbers = { 0, 1, 2, 3, 4, 5, 6, 1, 13 };
    PairFinder target = new PairFinder();
    // when
    ArrayList<Pair> pairs = target.findPairs(numbers, 6);
    // then
    // DEBUG.enable();
    DEBUG.println(pairs);
    areEqual(pairs.size(), 4);

    // when
    pairs = target.findPairs(numbers, 2);
    // then
    DEBUG.println(pairs);
    areEqual(pairs.size(), 2);

    // when
    pairs = target.findPairs(numbers, 3);
    // then
    DEBUG.println(pairs);
    areEqual(pairs.size(), 3);
  }

  @Test
  public void testBinaryTreeToSortedList() {
    // given
    BiNode five = binode(5);
    BiNode three = binode(3);
    BiNode four = binode(4);
    BiNode two = binode(2);
    BiNode one = binode(1);
    BiNode six = binode(6);
    BiNode seven = binode(7);
    BiNode eight = binode(8);
    BiNode nine = binode(9);

    five.first(three); five.second(seven);

    three.first(two); three.second(four);
    seven.first(six); seven.second(eight);

    two.first(one); eight.second(nine);

    BinaryTreeToSortedList target = new BinaryTreeToSortedList();
    // when
    target.toSortedList(five);

    // then
    areEqual(one.second(), two); areEqual(one.first(), null);
    areEqual(two.second(), three); areEqual(two.first(), one);
    areEqual(three.second(), four); areEqual(three.first(), two);
    areEqual(four.second(), five); areEqual(four.first(), three);
    areEqual(five.second(), six); areEqual(five.first(), four);
    areEqual(six.second(), seven); areEqual(six.first(), five);
    areEqual(seven.second(), eight); areEqual(seven.first(), six);
    areEqual(eight.second(), nine); areEqual(eight.first(), seven);
    areEqual(nine.second(), null); areEqual(nine.first(), eight);
  }

  BiNode binode(int value) {
    return new BiNode(value);
  }

  @Test
  public void testSentenceFinder() {
    Set<String> words = new HashSet<>();
    words.add("i");
    words.add("am");
    words.add("the");
    words.add("greatest");
    words.add("player");
    words.add("in");
    words.add("world");

    SentenceFinder finder = new SentenceFinder(words);
    DEBUG.disable();
    SingleLinkNode<String> sentence = finder.sentencify("iammarcelthegreatestdotaplayerintheworld");

    areEqual(
      sentence,
      link("i", "am", "marcel", "the", "greatest", "dota", "player", "in", "the", "world")
    );
  }

  private static SingleLinkNode<String> link(String ... words) {
    SingleLinkNode<String> head = new SingleLinkNode<>(words[0]);
    SingleLinkNode<String> tail = head;

    for(int i = 1; i < words.length; i++) {
      tail.next(new SingleLinkNode<>(words[i]));
      tail = tail.next();
    }

    return head;
  }
}
