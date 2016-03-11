package chapter18;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import structures.*;

class Shuffler {
  private final Random random = new Random(System.nanoTime());
  
  public <T> T[] shuffle(T ... elements) {
    for(int i = 0; i <= elements.length - 2; i++) {
      int swapIndex = random.nextInt(elements.length - i);
      T temp = elements[i];
      elements[i] = elements[swapIndex];
      elements[swapIndex] = temp;
    }

    return elements;
  }

  public <T> T[] shuffleNaive(T ... elements) {
    //1 Build an array of indices of the same size as elements
    T[] shuffled = (T[]) Array.newInstance(elements[0].getClass(), elements.length);
    List<Integer> indices = new ArrayList<>();
    for(int i = 0; i < elements.length; i++) {
      indices.add(i);
    }

    int shuffledElementsCounter = 0;
    while(indices.size() > 0) {
     int randomIndex = random.nextInt(indices.size()); 
     int shuffledPosition = indices.get(randomIndex);

     shuffled[shuffledPosition] = elements[shuffledElementsCounter];
     
     shuffledElementsCounter++;
     indices.remove(randomIndex);
    }

    return shuffled;
  }
}

class RandomSetGenerator {
  // Assumption: source length must be greater than or equal to output size
  // Assumption: No repeated numbers are desired
  public Set<Integer> generate(int[] source, int outputSize) {
    Random random = new Random(System.nanoTime());
    List<Integer> indices = new ArrayList<>();
    // con: requires O(N) memory
    for(int i = 0; i < source.length; i++) {
      indices.add(i);
    }

    Set<Integer> result = new HashSet<>();
    for(int i = 0; i < outputSize; i++) {
      int indicesIndex = random.nextInt(indices.size());
      int sourceIndex = indices.get(indicesIndex);
      result.add(source[sourceIndex]);
      indices.remove(indicesIndex);
    }

    return result;
  }
}

class TwosCounter {
  public int countTwos(int n) {
    if(n < 2) {
      return 0;
    }

    if(n < 10) {
      return 1;
    }

    int count = 1; // number 2
    double divisionResult = n / 10.0;
    while(divisionResult > 0) {
      count += divisionResult;
      double nextDivisionResult = divisionResult / 10.0;
      count += (int) nextDivisionResult;
      double remainder = nextDivisionResult - ((int) nextDivisionResult);
      if(remainder >= 0.2) {
        count++;
      }

      divisionResult = nextDivisionResult;
    }

    return count;
  }
}

// given a large file with a lot of words, find the shortest distance between
// any two words. If the operation will be repeated many times for the same
// file with other pairs of words, can you optimize your solution?

class WordDistanceFinder {

  public int generalFind(String[] document, String wordA, String wordB) {
    int lastAIdx = -1;
    int lastBIdx = -1;
    int minDistance = Integer.MAX_VALUE;

    for(int i = 0; i < document.length; i++) {
      // A C B D E A
      if(wordA.equals(document[i])) {
        lastAIdx = i;
      }

      if(wordB.equals(document[i])) {
        lastBIdx = i;
      }

      if(lastAIdx >= 0 && lastBIdx >= 0) {
        int distance = Math.abs(lastAIdx - lastBIdx);
        if(distance < minDistance) {
          minDistance = distance;
        }
      }
    }

    if(minDistance == Integer.MAX_VALUE) {
      return -1;
    }

    return minDistance;
  }

  public BiFunction<String, String, Integer> optimizedFind(String[] document) {
    final Map<String, List<Integer>> wordsIndices = new HashMap<>();
    for(int i  = 0; i < document.length; i++) {
      String word = document[i];
      List<Integer> wordIndices = wordsIndices.get(word);
      if(wordIndices == null) {
        wordIndices = new ArrayList<>();
        wordsIndices.put(word, wordIndices);
      }

      wordIndices.add(i);
    }

    return (String wordA, String wordB) -> {
      List<Integer> wordAIndices = wordsIndices.get(wordA);
      List<Integer> wordBIndices = wordsIndices.get(wordB);

      if(wordAIndices == null || wordBIndices == null) {
        return -1;
      }

      int minDistance = Integer.MAX_VALUE;
      int aCounter = 0;
      int bCounter = 0;
      while(aCounter < wordAIndices.size() && bCounter < wordBIndices.size()) {
        int aIndex = wordAIndices.get(aCounter);
        int bIndex = wordBIndices.get(bCounter);
        int distance = Math.abs(aIndex - bIndex);
        if(distance < minDistance) {
          minDistance = distance;
        }

        if(aIndex < bIndex) {
          aCounter++;
        } else {
          bCounter++;
        }
      }

      return minDistance;
    };
  }
}

// Tell me if the sentence is valid according to words available in a dictionary
class SentenceChecker {
  public SentenceChecker() {
  }

  public boolean isValidSentence(String sentence, HashSet<String> dictionary) {
    return isValidSentence(new HashMap<>(), sentence, dictionary, 0);
  }

  private boolean isValidSentence(
    Map<Integer, Boolean> memo,
    String sentence,
    HashSet<String> dictionary,
    int index
  ) {

    if(sentence.length() == 0) {
      return false;
    }

    if(index == sentence.length()) {
      return true;
    }

    if(memo.containsKey(index)) {
      return memo.get(index);
    }

    // I can iterate over the dictionary or iterate over the sentence
    for(int currentIndex = index + 1; currentIndex <= sentence.length(); currentIndex++) {
      String word = sentence.substring(index, currentIndex);
      if(dictionary.contains(word)) {
        if(isValidSentence(memo, sentence, dictionary, currentIndex)) {
          memo.put(index, true);
          return true;
        }
      }
    }

    memo.put(index, false);
    return false;
  }
}