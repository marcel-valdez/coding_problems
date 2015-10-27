package chapter3;

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
  @Rule
  public ErrorCollector errors = new ErrorCollector();

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
}
