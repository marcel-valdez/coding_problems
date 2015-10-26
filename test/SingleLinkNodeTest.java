package structures;

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

public class SingleLinkNodeTest {
  @Test
  public void testEqualsAreDifferent() {
    checkAreDifferent(node("A"), node("B"));
    checkAreDifferent(node("A"), node(null));
    checkAreDifferent(node(null), node("A"));
    checkAreDifferent(node(null), node("A"));
  }

  @Test
  public void testEqualsAreDifferentLists() {
    checkAreDifferent(node("A"), list("A", "B"));
    checkAreDifferent(list("A", null), list("A", "B"));
    checkAreDifferent(list("A", "C"), list("A", "B"));
    checkAreDifferent(list("A", "C"), list("B", "C"));
  }

  private static void checkAreDifferent(SingleLinkNode<?> node, SingleLinkNode<?> other) {
    // when
    boolean actual = node.equals(other);

    // then
    Assert.assertFalse(actual);
  }

  @Test
  public void testEqualsAreEqual() {
    checkAreEqual(node("A"), node("A"));
    checkAreEqual(node(null), node(null));
  }

  @Test
  public void testEqualsAreEqualLists() {
    checkAreEqual(list("A", "B"), list("A", "B"));
    checkAreEqual(list("A", null), list("A", null));
  }

  private static void checkAreEqual(SingleLinkNode<?> node, SingleLinkNode<?> other) {
    // when
    boolean actual = node.equals(other);

    // then
    Assert.assertTrue(actual);
  }

  private static <T> SingleLinkNode<T> list(T ... values) {
    final SingleLinkNode[] result = new SingleLinkNode[1];
    Stream.of(values)
      .map(value -> node(value))
      .reduce(null,
        (list, node) -> {
          if(list != null) {
            list.next(node);
          } else {
             result[0] = node;
          }

          return node;
        }
      );

    return result[0];
  }

  private static <T> SingleLinkNode<T> node(T value) {
    return new SingleLinkNode<T>(value);
  }
}
