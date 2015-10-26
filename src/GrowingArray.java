package structures;

import java.lang.*;
import java.lang.reflect.*;

public class GrowingArray {
  private Object[] array;
  private int count = 0;
  private Class<?> componentType;

  public GrowingArray(Class<?> arrayType, int size) {
    componentType = arrayType;
    array = createArray(arrayType, size);
  }

  private Object[] createArray(Class<?> type, int size) {
    return (Object[]) Array.newInstance(type, size);
  }

  public void add(Object element) {
    // duplicate array size once we reach 75% of the size
    // O(1) addition amortized analysis
    if(count >= (array.length * 0.75d)) {
      Object[] newArray = createArray(componentType, array.length * 2);
      System.arraycopy(array, 0, newArray, 0, count);
      array = newArray;
    }

    array[count] = element;
    count++;
  }

  public Object[] getData() {
    Object[] copy = createArray(componentType, count);
    System.arraycopy(array, 0, copy, 0, count);
    return copy;
  }
}
