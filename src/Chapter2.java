package chapter2;

import java.lang.*;
import java.util.*;

import structures.*;

class DuplicatesRemover {
  public <T> void removeDuplicates(SingleLinkNode<T> head) {
    if(head == null) {
      return;
    }

    if(head.next() == null) {
      return;
    }
    /*
    * By using semantically meaningful names, I was able to find the bug
    * in this algorithm, by renaming current to lastUnique, I figured out
    * that it was being assigned a non-unique node.
    */
    SingleLinkNode<T> lastUnique = head;
    while(lastUnique != null) {
      SingleLinkNode<T> next = lastUnique.next();
      if(lastUnique.next() != null && isLastDuplicate(head, next)) {
        lastUnique.next(next.next());
      } else {
        lastUnique = next;
      }
    }
  }

  public <T> boolean isLastDuplicate(SingleLinkNode<T> start, SingleLinkNode<T> last) {
    SingleLinkNode<T> iterator = start;
    do {
      if(iterator.value() == null) {
        if(last.value() == null) {
          return true;
        }
      } else if(iterator.value().equals(last.value())) {
        return true;
      }

      iterator = iterator.next();
    } while(iterator != last);

    return false;
  }
}


class KthElementGetter {

   public <T> SingleLinkNode<T> get(SingleLinkNode<T> head, int index) {
     return get(head, index, new Index(-1));
   }

   public <T> SingleLinkNode<T> get(SingleLinkNode<T> current, int index, Index movingIndex) {
     if(current != null) {
       SingleLinkNode<T> found = get(current.next(), index, movingIndex);
       if(index != movingIndex.get()) {
         current = found;
       }
     }

     movingIndex.inc();
     return current;
   }

   private static class Index {
     private int index;
     public Index(int initial) { index = initial; }
     public int get() { return index; }
     public void inc() { index++; }
   }
}

class MiddleRemover {
  public <T> void remove(SingleLinkNode<T> middle) {
    SingleLinkNode<T> iterator = middle.next();
    while(iterator.next() != middle) {
      iterator = iterator.next();
    }

    iterator.next(middle.next());
  }
}

class PivotArranger {
  public void partition(SingleLinkNode<Integer> node, Integer pivot) {
    if(node == null || pivot == null || node.next() == null) {
      return;
    }

    SingleLinkNode<Integer> lessTail = null;
    SingleLinkNode<Integer> greaterHead = null;
    SingleLinkNode<Integer> greaterTail = null;
    SingleLinkNode<Integer> prev = null;
    while(node != null) {
      SingleLinkNode<Integer> next = node.next();
      if(node.value().intValue() < pivot.intValue()) {
        if(lessTail == null) {
          lessTail = node;
          prev = node;
        } else {
          lessTail.next(node);
          node.next(null);
          if(prev != lessTail) {
            prev.next(next);
          } else {
            prev = node;
          }

          lessTail = node;
        }
      } else if(node.value().intValue() > pivot.intValue()) {
        if(greaterHead == null) {
          greaterHead = node;
          greaterTail = node;
          prev = node;
        } else {
          greaterTail.next(node);
          node.next(null);
          if(prev != greaterTail) {
            prev.next(next);
          } else {
            prev = node;
          }

          greaterTail = node;
        }
      } else {
        prev.next(next);
        node.next(greaterHead);
        greaterHead = node;
      }

      node = next;
    }

    if(lessTail != null) {
      lessTail.next(greaterHead);
    }
  }
}

class LinkListNumberCalculator {
  public SingleLinkNode<Integer> sum(
    SingleLinkNode<Integer> first,
    SingleLinkNode<Integer> second) {
    if(first == null && second == null) { return new SingleLinkNode<Integer>(0); }
    if(first == null && second != null) { return first; }
    if(first == null && second != null) { return second; }

    SingleLinkNode<Integer> head = null;
    SingleLinkNode<Integer> tail = null;
    int add = 0;
    while(first != null || second != null) {
      int firstInt = first != null ? first.value() : 0;
      int secondInt = second != null ? second.value() : 0;

      int resultInt = firstInt + secondInt + add;
      if(resultInt >= 10) {
        add = 1;
        resultInt = resultInt - 10;
      } else {
        add = 0;
      }

      SingleLinkNode<Integer> result = new SingleLinkNode<>(resultInt);
      if(head == null) {
        tail = head = result;
      } else {
        tail.next(result);
        tail = result;
      }

      first = first != null ? first.next() : null;
      second = second != null ? second.next() : null;
    }

    if(add > 0) {
      tail.next(new SingleLinkNode<>(1));
    }

    return head;
  }

  public SingleLinkNode<Integer> sumReverse(
    SingleLinkNode<Integer> first,
    SingleLinkNode<Integer> second) {
    if(first == null && second == null) { return new SingleLinkNode<Integer>(0); }
    if(first == null && second != null) { return first; }
    if(first == null && second != null) { return second; }

    return reverse(sum(reverse(first), reverse(second)));
  }

  private <T> SingleLinkNode<T> reverse(SingleLinkNode<T> head) {
    // 1 -> 2 -> 3 -> N
    // 2 -> 1 -> 3 -> N
    // 3 -> 2 -> 1 -> N
    SingleLinkNode<T> iter = head;
    while(iter.next() != null) {
      SingleLinkNode<T> next = iter.next();
      iter.next(next.next());
      next.next(head);
      head = next;
    }

    return head;
  }
}

class CircularListHeadFinder {
  // I solved this one without really understanding WHY we needed
  // a fast runner and a slow runner
  public <T> SingleLinkNode<T> findHead(SingleLinkNode<T> node) {
    if(node == null || node.next() == null || node.next() == node) {
      return node;
    }

    SingleLinkNode<T> head = node;
    SingleLinkNode<T> runner = node;
    do {
      if(node.next() == null || runner.next() == null || runner.next().next() == null) {
        throw new RuntimeException("No nulls allowed");
      }
      node = node.next();
      runner = runner.next().next();
    } while(runner != node);

    while(head != runner) {
      head = head.next();
      runner = runner.next();
    }

    return runner;
  }
}

class PalindromeIdentifier {
  // Assume it is not circular and is single link
  // Asssume spaces are important, meaning they are treated like
  //         any other character, it must match no matter what.
  // null: true
  // A : true
  // A -> A : true
  // A -> B : false
  // A -> B -> A : true
  // A -> B -> A -> B : false
  // A -> B -> A -> B -> A : true

  public boolean isPalindrome(SingleLinkNode<String> head) {
    if(head == null || head.next() == null) { return true; }
    return isPalindrome(head, head).matched;
  }

  private RecursionResult isPalindrome(
    SingleLinkNode<String> head, SingleLinkNode<String> current) {
    if(current.next() == null) {
      return new RecursionResult(
        equals(current.value(), head.value()),
        head.next()
      );
    } else {
      RecursionResult recursion = isPalindrome(head, current.next());
      recursion.matched = recursion.matched && equals(current.value(), recursion.node.value());
      recursion.node = recursion.node.next();
      return recursion;
    }
  }

  private static boolean equals(Object a, Object b) {
    if(a == null || b == null) {
      return a == b;
    }

    return a.equals(b);
  }

  private static class RecursionResult {
    private boolean matched;
    private SingleLinkNode<String> node;
    public RecursionResult(boolean matched, SingleLinkNode<String> node) {
      this.matched = matched; this.node = node;
    }
  }
}
