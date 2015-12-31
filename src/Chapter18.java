package chapter18;

import static debug.Debugger.DEBUG;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
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
}