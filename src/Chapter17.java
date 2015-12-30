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

class TicTacToeResultDetector {

  // Assumptions all we want to know is if someone won not WHO won.
  // the input is 1 for player 1, 2 for player 2, 0 for not used
  public boolean detectWinner(byte[][] board) {
    if(board == null || board.length != 3 || board[0].length != 3 ) {
      throw new RuntimeException("Board can only by 3 by 3");
    }

    boolean diagonalA = true, diagonalB = true;
    for(int i = 0; i <= 2; i++) {
      if(board[i][0] != 0 && board[0][i] != 0) {
        if ((board[i][0] == board[i][1] && board[i][1] == board[i][2])
         || (board[0][i] == board[1][i] && board[1][i] == board[2][i])) {
          return true;
        }
      }

      if(i > 0) {
        diagonalA &= board[i - 1][i - 1] == board[i][i];
        diagonalB &= board[i - 1][3 - i] == board[i][2 - i];
      }
    }

    return diagonalB || diagonalA;
  }
}

class NFactorialTrailingZeros {
  public int calculate(int n) {
    if(n < 5) { return 0; }
    if(n == 5) { return 1; }
    if(n < 10) { return 1; }

    int tens = n / 10;
    int total = tens * 2;
    if(n % 10 >= 5) {
      total++;
    }

    return total;
  }
}

class LargestSumSequenceFinder {
  // Assumption: There is at least 1 positive number
  public int find(int[] numbers) {
    if(numbers == null || numbers.length == 0) {
      throw new RuntimeException("Numbers cannot be null or empty");
    }

    if(numbers.length == 1) {
      return numbers[0];
    }

    int maxSum = Integer.MIN_VALUE;
    int currentSum = 0;

    for(int number : numbers) {
      int nextSum = currentSum + number;
      // always check if adding the next number results in a max
      // sequence sum, note that there is no need to check if the
      // next number is negative before comparing against the max sum
      if(nextSum > maxSum) {
        maxSum = nextSum;
      }

      if(nextSum < 0) {
        // restart the sequence sum if adding the next negative number
        // results in a negative sum
        currentSum = 0;
      } else {
        // so long as adding the next number does not result in a negative
        // sum, it is worth considering it in the sequence sum
        currentSum = nextSum;
      }
    }

    return maxSum;
  }
}

class WordCounter {
  private static final int UPPER_LOWER_DELTA = 'a' - 'A';

  public WordCounter() {}

  public int count(String text, String word) {
    String lowerWord = word.toLowerCase();

    int comparedWordIndex = 0;
    int count = 0;
    boolean skipCurrentWord = false;
    for(int i = 0; i < text.length(); i++) {
      if(isNonLetter(text.charAt(i))) {
        skipCurrentWord = false;
        continue;
      }

      if(skipCurrentWord) {
        continue;
      }

      if(isSameLetter(lowerWord.charAt(comparedWordIndex), text.charAt(i))) {
          comparedWordIndex++;
          if(comparedWordIndex == lowerWord.length()) {
            if(i+1 == text.length() || isNonLetter(text.charAt(i+1))) {
              // words matched!
              count++;
            }
            // restart the word index
            comparedWordIndex = 0;
          }
        } else {
          comparedWordIndex = 0;
          skipCurrentWord = true;
        }
    }

    return count;
  }

  private boolean isSameLetter(char lowerCaseLetter, char otherLetter) {
    return lowerCaseLetter == otherLetter ||
           lowerCaseLetter == (otherLetter - UPPER_LOWER_DELTA);
  }

  private boolean isNonLetter(char symbol) {
    return !(symbol >= 'a' && symbol <= 'z') &&
           !(symbol >= 'A' && symbol <= 'Z');
  }
}

class Pair {
  private final int a;
  private final int b;
  public Pair(int a, int b) { this.a = a; this.b = b; }
  public String toString() {
    return String.format("(%s, %s)", a, b);
  }
}

class PairFinder {
  public ArrayList<Pair> findPairs(int[] numbers, int sum) {
    if(numbers == null || numbers.length < 2) {
      throw new RuntimeException("Numbers must be at least of length 2");
    }

    ArrayList<Pair> results = new ArrayList<>();
    HashMap<Integer, Integer> numbersHash = new HashMap<>();

    for(int number : numbers) {
      if(numbersHash.containsKey(sum - number)) {
        for(int i = 0; i < numbersHash.get(sum - number); i++) {
          results.add(pair(number, sum - number));
        }
      }

      if(numbersHash.containsKey(number)) {
        numbersHash.put(number, numbersHash.get(number) + 1);
      } else {
        numbersHash.put(number, 1);
      }
    }

    return results;
  }

  Pair pair(int a, int b) { return new Pair(a, b); }
}

class BinaryTreeToSortedList {
  public void toSortedList(BiNode tree) {
    ArrayList<BiNode> sortedNodes = new ArrayList<>();
    sortedNodes.add(null);
    appendToSortedList(tree, sortedNodes);
    sortedNodes.add(null);

    for(int i = 1; i < sortedNodes.size() - 1; i++) {
      BiNode node = sortedNodes.get(i);
      node.first(sortedNodes.get(i - 1));
      node.second(sortedNodes.get(i + 1));
    }
  }

  public void appendToSortedList(BiNode tree, List<BiNode> sortedNodes) {
    if(tree == null) { return; }
    appendToSortedList(tree.first(), sortedNodes);
    sortedNodes.add(tree);
    appendToSortedList(tree.second(), sortedNodes);
  }
}

class BiNode {
  private final int value;
  private BiNode first;
  private BiNode second;

  public BiNode(int value) {
    this.value = value;
  }

  public void first(BiNode first) { this.first = first; }
  public BiNode first() { return this.first; }

  public void second(BiNode second) { this.second = second; }
  public BiNode second() { return this.second; }

}

/*
* The input is a set of words all clumped up together, such as:
* iammarcelthegreatestdotaplayerintheworld
*
* The expected output is the strinmg with the least unknown characters
* in it, such that you minimize the letters that dont belong to a word:
* i am MARCEL the greatest DOTA player in the world
*
* For this to work we assume:
* - We have access to dictionary of words
* - We assume that there are words not in that dictionary,
*   such as name and proper nouns.
*/

class SentenceFinder {
  private int iterations = 0;

  private static class Dictionary {
    private final Set<String> words;
    private final int longestWordSize;

    public Dictionary(Set<String> words) {
      this.words = words;
      int longestWordSize = 0;
      for(String word : words) {
        if(word.length() > longestWordSize) {
          longestWordSize = word.length();
        }
      }
      this.longestWordSize = longestWordSize;
    }

    public boolean contains(String word) {
      return this.words.contains(word);
    }

    public int longestWordSize() {
      return longestWordSize;
    }
  }

  private final Dictionary words;
  public SentenceFinder(Set<String> words) {
    this.words = new Dictionary(words);
  }

  public SingleLinkNode<String> sentencify(String letters) {
    Recursion result =  sentencify(new Memo(), letters, 0, "");
    DEBUG.println("Input length: %s", letters.length());
    DEBUG.println("Iterations: %s", iterations);
    DEBUG.println("Complexity: N^%2.1f", Math.log(iterations) / Math.log(letters.length()));
    return result.words;
  }

  private Recursion sentencify(
    Memo memo, String letters, int index, String currentWord) {
    iterations++;
    if(memo.has(index, currentWord)) {
      return memo.get(index, currentWord);
    }

    Recursion result = null;
    if(index == letters.length()) {
      if(exists(currentWord)) {
        result = results(node(currentWord), 0);
      } else {
        if(currentWord.equals("")) {
          result = results(null, 0);
        } else {
          result = results(node(currentWord.toUpperCase()), currentWord.length());
        }
      }
    } else {
      String nextWord = currentWord + letters.charAt(index);
      Recursion cutoff = sentencify(memo, letters, index + 1, "");
      Recursion extend = sentencify(memo, letters, index + 1, nextWord);
      SingleLinkNode<String> cutoffRecursionNode = cutoff.words;

      result = extend;
      if(exists(nextWord)) {
        if(cutoff.skipCount <= extend.skipCount) {
          SingleLinkNode<String> cutoffNode = node(nextWord);
          cutoffNode.next(cutoffRecursionNode);
          result = results(cutoffNode, cutoff.skipCount);
        }
      } else if(cutoff.skipCount + nextWord.length() < extend.skipCount) {
        SingleLinkNode<String> cutoffNode = node(nextWord.toUpperCase());
        cutoffNode.next(cutoffRecursionNode);
        result = results(cutoffNode, nextWord.length() + cutoff.skipCount);
      }
    }

    memo.memo(index, currentWord, result);
    return result;
  }

  private SingleLinkNode<String> node(String value) {
    return new SingleLinkNode<String>(value);
  }

  private boolean exists(String word) {
    return words.contains(word);
  }

  private static Recursion results(SingleLinkNode<String> results, int skipCount) {
    return new Recursion(results, skipCount);
  }

  private static class Recursion {
    final SingleLinkNode<String> words;
    final int skipCount;
    public Recursion(SingleLinkNode<String> words, int skipCount) {
      this.skipCount = skipCount;
      this.words = words;
    }
  }

  private static class Memo {
    private Map<String, Recursion> entries = new HashMap<>();
    public void memo(int index, String currentWord, Recursion result) {
      String key = currentWord + index;
      entries.put(key, result);
    }

    public boolean has(int index, String currentWord) {
      String key = currentWord + index;
      return entries.containsKey(key);
    }

    public Recursion get(int index, String currentWord) {
      String key = currentWord + index;
      return entries.get(key);
    }
  }
}
