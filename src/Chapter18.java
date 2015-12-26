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