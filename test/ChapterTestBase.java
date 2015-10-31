package test;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;


import java.lang.*;
import java.util.*;
import java.util.stream.*;

import org.hamcrest.*;
import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;

public abstract class ChapterTestBase {
  @Rule
  public ErrorCollector errors = new ErrorCollector();

  protected <T> void isTrueThat(String reason, T value, Matcher<T> matcher) {
    errors.checkThat(reason, value, matcher);
  }

  protected <T> void isTrueThat(T value, Matcher<T> matcher) {
    errors.checkThat(value, matcher);
  }

  protected <T> void isFalseThat(String reason, T value, Matcher<T> matcher) {
    errors.checkThat(reason, value, not(matcher));
  }

  protected <T> void isFalseThat(T value, Matcher<T> matcher) {
    errors.checkThat(value, not(matcher));
  }

  protected void areEqual(Object actual, Object expected) {
    errors.checkThat(actual, equalTo(expected));
  }

  protected void areNotEqual(Object actual, Object expected) {
    errors.checkThat(actual, not(equalTo(expected)));
  }

  protected static int[] popAll(LinkedListStack<Integer> stack) {
    int[] result = new int[stack.size()];
    for(int i = 0; i < result.length; i++) {
      result[i] = stack.pop();
    }

    return result;
  }

  protected static void pushAll(LinkedListStack<Integer> stack, int ... input) {
    for(int number : input) {
      stack.push(number);
    }
  }
}
