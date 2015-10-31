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
    array[count] = element;
    count++;
    growIfBig();
  }

  private void growIfBig() {
    if(count >= (array.length * 0.75d)) {
      T[] bigger = createArray(componentType, array.length * 2);
      System.arraycopy(array, 0, bigger, 0, count);
      array = bigger;
    }
  }

  public T getLast() {
    return this.array[count - 1];
  }

  public T remove(int index) {
    if(index < 0 || index >= count) {
      throw new RuntimeException("Index out of bounds. Index: " + index + ", Size: " + count);
    }

    T item = this.array[index];
    System.arraycopy(array, index+1, array, index, count - index - 1);
    count--;
    shrinkIfSmall();

    return item;
  }

  public void insert(int index, T value) {
    count++;
    for(int i = count; i > index; i--) {
      array[i] = array[i-1];
    }

    array[index] = value;
    growIfBig();
  }

  public T removeLast() {
    T last = this.array[count -1];
    this.array[count - 1] = null;
    count--;

    shrinkIfSmall();

    return last;
  }

  private void shrinkIfSmall() {
    if(count <= array.length * 0.25d) {
      T[] smaller = createArray(componentType, array.length / 2);
      System.arraycopy(array, 0, smaller, 0, count);
      array = smaller;
    }
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
