import org.junit.*;
import java.lang.*;

public class HashTableTest {
  @Test
  public void testConstructor() {
    new HashTable();
    new HashTable(10);
  }

  @Test
  public void testPutSingle() {
    //given
    HashTable target = new HashTable();
    String value = "theValue";
    Object key = new Object();
    //when
    target.put(key, value);
    //then
    Assert.assertEquals(value, target.get(key));
  }

  @Test
  public void testPutMultiple() {
    //given
    HashTable target = new HashTable();
    String[] values = { "1", "2", "3", "4", "5" };
    Object[] keys = {
      Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4),
      Integer.valueOf(5)
    };
    //when
    for(int i = 0; i < keys.length; i++) {
      target.put(keys[i], values[i]);
    }
    //then
    for(int i = 0; i < keys.length; i++) {
      Assert.assertEquals(values[i], target.get(keys[i]));
    }
  }

  @Test
  public void testPutMultipleWithCollisions() {
    //given
    HashTable target = new HashTable();
    String[] values = { "1", "2", "3", "4", "5" };
    Object[] keys = {
     "asldkfj", "qoweir", "x.c,vb", "9218347", "/.,123e"
    };
    //when
    for(int i = 0; i < keys.length; i++) {
      target.put(keys[i], values[i]);
    }
    //then
    for(int i = 0; i < keys.length; i++) {
      Assert.assertEquals(values[i], target.get(keys[i]));
    }
  }
}
