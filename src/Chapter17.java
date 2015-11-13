package chapter17;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.util.*;
import java.util.stream.*;

import structures.*;

class InlineNumberSwapper {

  public void swap(PairOfNumbers pair) {
    pair.first = pair.first ^ pair.second;
    pair.second = pair.first ^ pair.second;
    pair.first = pair.first ^ pair.second;
  }
}

class PairOfNumbers {
  public int first;
  public int second;
  public PairOfNumbers(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public PairOfNumbers pair(int a, int b) {
    return new PairOfNumbers(a, b);
  }
}
