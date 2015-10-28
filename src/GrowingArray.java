package structures;

import java.lang.*;
import java.lang.reflect.*;

@SuppressWarnings({"unchecked"})
public class GrowingArray<T> {
  private T[] array;
  private int count = 0;
  private Class<T> componentType;

  public GrowingArray(Class<T> arrayType, int size) {
    componentType = arrayType;
    array = createArray(arrayType, size);
  }

  private T[] createArray(Class<T> type, int size) {
    return (T[]) Array.newInstance(type, size);
  }

  public void add(T element) {
    // duplicate array size once we reach 75% of the size
    // O(1) addition amortized analysis
    if(count >= (array.length * 0.75d)) {
      T[] bigger = createArray(componentType, array.length * 2);
      System.arraycopy(array, 0, bigger, 0, count);
      array = bigger;
    }

    array[count] = element;
    count++;
  }

  public T getLast() {
    return this.array[count - 1];
  }

  public T remove(int index) {
    throw new RuntimeException("TODO: Implement this");
  }

  public T removeLast() {
    T last = this.array[count -1];
    this.array[count - 1] = null;
    count--;

    if(count <= array.length * 0.25d) {
      T[] smaller = createArray(componentType, array.length / 2);
      System.arraycopy(array, 0, smaller, 0, count);
      array = smaller;
    }

    return last;
  }

  public T get(int index) {
    return array[index];
  }

  public T[] getShallowCopy() {
    T[] copy = createArray(componentType, count);
    System.arraycopy(array, 0, copy, 0, count);
    return copy;
  }

  // Gets the number of elemetns in the array
  public int count() {
    return this.count;
  }

  // Gets the size of the buffer
  public int size() {
    return this.array.length;
  }
}
