package chapter2;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;

import java.lang.*;
import java.util.*;
import java.util.stream.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;

public class Chapter2Test {
  @Rule
  public ErrorCollector errors = new ErrorCollector();

  private DuplicatesRemover duplicatesRemover = new DuplicatesRemover();
  private KthElementGetter kthGetter = new KthElementGetter();
  private MiddleRemover middleRemover = new MiddleRemover();
  private PivotArranger pivotArranger = new PivotArranger();
  private LinkListNumberCalculator linkListNumberCalculator = new LinkListNumberCalculator();
  private CircularListHeadFinder headFinder = new CircularListHeadFinder();
  private PalindromeIdentifier palindromeIdentifier = new PalindromeIdentifier();

  @Test
  public void testRemoveDuplicatesFromSingleLinkedList() {
    checkRemoveDuplicates(
      buildList("F"),
      buildList("F")
    );

    checkRemoveDuplicates(
      buildList("F", "F", "F"),
      buildList("F")
    );

    checkRemoveDuplicates(
      buildList("F", "O"),
      buildList("F", "O")
    );

    checkRemoveDuplicates(
      buildList("F", "O", "F"),
      buildList("F", "O")
    );

    checkRemoveDuplicates(
      buildList("F", "F", "O"),
      buildList("F", "O")
    );

    checkRemoveDuplicates(
      buildList("F", "O", "O"),
      buildList("F", "O")
    );

    checkRemoveDuplicates(
      buildList("F", "O", "L", "L", "O", "W", " ", "U", "P"),
      buildList("F", "O", "L", "W", " ", "U", "P")
    );
  }

  private void checkRemoveDuplicates(SingleLinkNode input, SingleLinkNode expected) {
    // when
    duplicatesRemover.removeDuplicates(input);
    // then
    errors.checkThat(input, equalTo(expected));
  }

  @Test
  public void testGetKthElement() {
    // given
    SingleLinkNode<String> head = buildList("0", "1", "2", "3");
    checkKthElementGetter(head, 0, "3");
    checkKthElementGetter(head, 1, "2");
    checkKthElementGetter(head, 2, "1");
    checkKthElementGetter(head, 3, "0");
  }

  private <T> void checkKthElementGetter(SingleLinkNode<T> head, int index, T expected) {
    // when
    T value = kthGetter.get(head, index).value();
    // then
    errors.checkThat(value, equalTo(expected));
  }

  @Test
  public void testRemoveMiddleElement() {
    // given
    SingleLinkNode<String> head = buildList("A", "B", "C", "D", "E");
    SingleLinkNode<String> middle = head.next().next();
    // turn it into a circular single link list
    SingleLinkNode<String> tail = middle.next().next();
    tail.next(head);

    // when
    middleRemover.remove(middle);

    // then
    errors.checkThat(head.value(), equalTo("A"));
    errors.checkThat(head.next().value(), equalTo("B"));
    errors.checkThat(head.next().next().value(), equalTo("D"));
    errors.checkThat(head.next().next().next().value(), equalTo("E"));

  }

  @Test
  public void testArrangeLinkedList() {
    // given
    SingleLinkNode<Integer> list = buildList(1, 9, 2, 8, 3, 7, 4, 6, 5);
    SingleLinkNode<Integer> expected = buildList(1, 2, 3, 4, 5, 9, 8, 7, 6);
    // when
    pivotArranger.partition(list, 5);
    // then
    errors.checkThat(list, equalTo(expected));
  }


  @Test
  public void testAddLinkListNumbers() {
    checkAddLinkListNumbers(buildList(1), buildList(2), buildList(3));

    checkAddLinkListNumbers(
      buildList(5),
      buildList(7),
      buildList(2, 1)); //12

    checkAddLinkListNumbers(
      buildList(5),
      buildList(7, 1), // 17
      buildList(2, 2)); //22

    checkAddLinkListNumbers(
      buildList(5),
      buildList(7, 9), // 97
      buildList(2, 0, 1)); // 102

    checkAddLinkListNumbers(
      buildList(0, 5), //50
      buildList(2, 7), //72
      buildList(2, 2, 1)); //122

    checkAddLinkListNumbers(
      buildList(7, 1, 6), // 617
      buildList(5, 9, 2), // 295
      buildList(2, 1, 9)); // 912
  }

  public void checkAddLinkListNumbers(
    SingleLinkNode<Integer> inputA,
    SingleLinkNode<Integer> inputB,
    SingleLinkNode<Integer> expected
  ) {
    // when
    SingleLinkNode<Integer> actual = linkListNumberCalculator.sum(inputA, inputB);

    // then
    errors.checkThat(actual, equalTo(expected));
  }

  @Test
  public void testAddLinkListNumbersReversed() {
    checkAddLinkListNumbers(buildList(1), buildList(2), buildList(3));

    checkAddLinkListNumbersReversed(
      buildList(5),
      buildList(7),
      buildList(1, 2)); //12

    checkAddLinkListNumbersReversed(
      buildList(5),
      buildList(1, 7), // 17
      buildList(2, 2)); //22

    checkAddLinkListNumbersReversed(
      buildList(5),
      buildList(9, 7), // 97
      buildList(1, 0, 2)); // 102

    checkAddLinkListNumbersReversed(
      buildList(5, 0), //50
      buildList(7, 2), //72
      buildList(1, 2, 2)); //122

    checkAddLinkListNumbersReversed(
      buildList(6, 1, 7), // 617
      buildList(2, 9, 5), // 295
      buildList(9, 1, 2)); // 912
  }

  public void checkAddLinkListNumbersReversed(
    SingleLinkNode<Integer> inputA,
    SingleLinkNode<Integer> inputB,
    SingleLinkNode<Integer> expected
  ) {
    // when
    SingleLinkNode<Integer> actual = linkListNumberCalculator.sumReverse(inputA, inputB);

    // then
    errors.checkThat(actual, equalTo(expected));
  }

  @Test
  public void testFindMiddleOfCircularList() {
    // given
    SingleLinkNode<Integer> list = buildList(1, 2, 3);
    list.next().next().next(list); // make it circular
    // when
    SingleLinkNode<Integer> head = headFinder.findHead(list);
    // then
    errors.checkThat(head.value(), equalTo(1));
  }

  @Test
  public void testFindMiddleOfCircularListCorrupted() {
    // given
    SingleLinkNode<Integer> list = buildList(1, 2, 3, 4, 5, 6);
    list.next().next().next().next().next().next(list.next().next()); // make it circular
    // when
    SingleLinkNode<Integer> head = headFinder.findHead(list);
    // then
    errors.checkThat(head.value(), equalTo(3));
  }

  @Test
  public void testIdentifyPalindrome() {
    checkIdentifyPalindrome(buildList("A"), true);
    checkIdentifyPalindrome(buildList("A", "A"), true);
    checkIdentifyPalindrome(buildList("A", "B", "A"), true);
    checkIdentifyPalindrome(buildList("A", "B", "A", "B", "A"), true);

    checkIdentifyPalindrome(buildList("A", "B", "A", "B"), false);
    checkIdentifyPalindrome(buildList("A", "B"), false);
  }

  private void checkIdentifyPalindrome(SingleLinkNode<String> head, boolean expected) {
    errors.checkThat(palindromeIdentifier.isPalindrome(head), is(expected));
  }

  private <T> SingleLinkNode<T> buildList(T ... values) {
    final SingleLinkNode[] head = new SingleLinkNode[1];
    Stream.of(values)
      .map(value -> new SingleLinkNode<T>(value))
      .reduce(
        null,
        (list, node) -> {
          if(list == null) {
            head[0] = node;
          } else {
            list.next(node);
          }

          return node;
        }
      );
    return head[0];
  }
}
