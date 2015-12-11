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
*
* I could approach this like:
* A)
*  - The dictionary is in a N-Ary tree (access is size(M) where M is length of word)
*  - I try to find a word using the first letter in the dictionary
* This is what I see:
* We try top make a word with 1 letter, if we do make a word with 1 letter,
* then we put that in the results, and continue trying to make a word with
* the second letter, if we dont find a word with the next letter we issue a
* recursion where we skip the next letter and also issue a recursion where
* we try to find a word with the next 2 letters.
*
* It is important to look first for the cases where we do find a letter
*
* and we repeat the same process with N letters until we are out of letters.
*
*
 sentencify(results, letters, skip_count, current_idx, current_word)
 if current_idx == letters.length - 1
   return (results, skip_count)

 next_word = current_word + letters[current_idx]
 current_results = copy(results)
 if exists(next_word)
  current_results.add(next_word)
  return sentencify(current_results, letters, skip_count, current_idx + 1, "")
 else
  continue_sentence, continue_skip_count =
    sentencify(results, letters, skip_count, current_idx + 1, next_word)
  if continue_skip_count <= skip_count + 1
    return (continue_sentence, continue_skip_count)

  skip_sentence, skip_skip_count =
    sentencify(results, letter, skip_count + 1, current_idx + 1, "")

  if continue_skip_count <= skip_skip_count
    return (continue_sentence, continue_skip_count)
  else
    return (skip_sentence, skip_skip_count)
*/

class SentenceFinder {
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
    return sentencify(
      new Memo(),
      null,
      letters,
      0,
      0,
      new StringBuilder(letters.length())
    ).results;
  }

  private static class Memo {
    private final Map<Integer, Recursion> memo = new HashMap<>();

    private boolean has(int idx) {
      return memo.containsKey(idx);
    }

    private Recursion get(int idx) {
      return memo.get(idx);
    }

    private void memo(int idx, Recursion result) {
      memo.put(idx, result);
    }
  }

  private Recursion sentencify(
    Memo memo,
    SingleLinkNode<String> results,
    String letters,
    int skipCount,
    int currentIdx,
    StringBuilder currentWord) {
    // We can memoize bsaed on the current index
    if(currentIdx == letters.length()) {
      if(currentWord.length() > 0) {
        SingleLinkNode<String> skippedWord = new SingleLinkNode<>(currentWord.toString());
        skippedWord.next(results);
        DEBUG.println("skippedWord [%s]", currentWord);
        return results(skippedWord, skipCount + currentWord.length());
      } else {
        return results(results, skipCount);
      }
    }

    //if(memo.has(currentIdx)) {
    //  return memo.get(currentIdx);
    //}

    currentWord.append(letters.charAt(currentIdx));
    String nextWord = currentWord.toString();
    // word exists
    if(exists(nextWord)) {
      SingleLinkNode<String> next = new SingleLinkNode<>(nextWord);
      next.next(results);
      StringBuilder buffer =  new StringBuilder(letters.length() - currentIdx);
      DEBUG.println("exists sentencify(%s, %s, %s)[%s]",
        next, skipCount, currentIdx + 1, "<empty>");
      Recursion restOfSentence =
        sentencify(memo, next, letters, skipCount, currentIdx + 1, buffer);

      Recursion sentence = restOfSentence;
      if(sentence.skipCount > skipCount) {
        // try to make bigger word
        StringBuilder biggerBuffer = new StringBuilder(currentWord.toString());
        Recursion biggerWord = sentencify(
          memo, results, letters, skipCount, currentIdx + 1, biggerBuffer);
        if(biggerWord.skipCount < sentence.skipCount) {
          //DEBUG.print("biggerWord ");
          sentence = biggerWord;
        } else {
          //DEBUG.print("foundWord [%s] ", nextWord);
        }
      }

      //DEBUG.println("memo(%s, %s) [%s]", currentIdx, sentence.results, sentence.skipCount);
      memo.memo(currentIdx, sentence);
      return sentence;
    }

    // word does not exist

    // try making the word bigger
    StringBuilder biggerBuffer = new StringBuilder(currentWord.toString());
    DEBUG.println("biggerBuffer sentencify(%s, %s, %s)[%s]",
      results, skipCount, currentIdx + 1, biggerBuffer);
    Recursion biggerWordSentence = null;
    if(currentWord.length() < words.longestWordSize()) {
      biggerWordSentence = sentencify(memo, results, letters, skipCount, currentIdx + 1, biggerBuffer);
    } else {
      String biggestWord = currentWord.toString() + letters.substring(currentIdx + 1);
      SingleLinkNode<String> biggestWordLnk = new SingleLinkNode<>(biggestWord);
      biggestWordLnk.next(results);
      biggerWordSentence = results(biggestWordLnk, skipCount + biggestWord.length());
    }

    // try cutting off the word right here
    SingleLinkNode<String> cutOffWord = new SingleLinkNode<>(currentWord.toString());
    cutOffWord.next(results);
    StringBuilder cutOffBuffer = new StringBuilder();
    int cutOffSkipCount = skipCount + currentWord.length();
    DEBUG.println("cutOffBuffer sentencify(%s, %s, %s)[<empty>]",
      cutOffWord, cutOffSkipCount, currentIdx + 1);
    Recursion cutOffSentence = sentencify(
      memo, cutOffWord, letters, cutOffSkipCount, currentIdx + 1, cutOffBuffer);

    // pick the sentence with the least skipped chars
    if(cutOffSentence.skipCount < biggerWordSentence.skipCount) {
      //DEBUG.print("cutOffSentence [%s] ", currentWord);
      //DEBUG.println("memo(%s, %s) [%s]",
      //              currentIdx, cutOffSentence.results, cutOffSentence.skipCount);
      memo.memo(currentIdx, cutOffSentence);
      return cutOffSentence;
    } else {
      //DEBUG.print("biggerWordSentence ");
      //DEBUG.println("memo(%s, %s) [%s]",
      //              currentIdx, biggerWordSentence.results, biggerWordSentence.skipCount);
      memo.memo(currentIdx, biggerWordSentence);
      return biggerWordSentence;
    }
  }

  private boolean exists(String word) {
    return words.contains(word);
  }

  private static Recursion results(SingleLinkNode<String> results, int skipCount) {
    Recursion recursion = new Recursion();
    recursion.results = results;
    recursion.skipCount = skipCount;

    return recursion;
  }


  private static class Recursion {
    SingleLinkNode<String> results;
    int skipCount;
  }
}
