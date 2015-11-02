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
