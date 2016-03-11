package chapter18;

import static debug.Debugger.DEBUG;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.core.IsCollectionContaining.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;


import java.lang.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;
import test.*;

public class Chapter18Test extends ChapterTestBase {
  @Test
  public void testCardShuffler() {
    // given
    String[] cards = { "A", "B", "C", "D" };
    Shuffler shuffler = new Shuffler();
    //DEBUG.enable();
    Stream.of(cards).forEach(DEBUG::print);
    DEBUG.println("");
    // when
    String[] shuffledCards = shuffler.shuffle(cards);
    Stream.of(shuffledCards).forEach(DEBUG::print);
    // then
    Assert.assertEquals(cards.length, shuffledCards.length);
    Assert.assertThat(Arrays.asList(shuffledCards), hasItems(shuffledCards));
  }

  @Test
  public void testTwosCounter() {
    // given
    int n = 123;
    int expected = (new int[] { 2, 12, 22, 22, 32, 42, 52, 62, 72, 82, 92, 102, 112, 120, 122, 122  }).length;
    TwosCounter counter = new TwosCounter();
    // when
    int actual = counter.countTwos(n);
    // then
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGeneralWordDistanceFinder() {
    // given
    final String[] words = { "A", "B", "C", "D", "B", "E", "A", "D", "F", "C", "A", "G", "C" };
    final WordDistanceFinder target = new WordDistanceFinder();
    checkWordDistanceFinder((a, b) -> target.generalFind(words, a, b));
  }

  @Test
  public void testOptimizedWordDistanceFinder() {
    // given
    final String[] words = { "A", "B", "C", "D", "B", "E", "A", "D", "F", "C", "A", "G", "C" };
    final WordDistanceFinder target = new WordDistanceFinder();
    checkWordDistanceFinder(target.optimizedFind(words));
  }

  private void checkWordDistanceFinder(BiFunction<String, String, Integer> finder) {
    Assert.assertEquals("AD", 1, finder.apply("A", "D").intValue());
    Assert.assertEquals("DA", 1, finder.apply("D", "A").intValue());

    Assert.assertEquals("BD", 1, finder.apply("B", "D").intValue());
    Assert.assertEquals("DB", 1, finder.apply("D", "B").intValue());

    Assert.assertEquals("EB", 1, finder.apply("E", "B").intValue());
    Assert.assertEquals("BE", 1, finder.apply("B", "E").intValue());

    Assert.assertEquals("ED", 2, finder.apply("E", "D").intValue());
    Assert.assertEquals("DE", 2, finder.apply("D", "E").intValue());

    Assert.assertEquals("BG", 7, finder.apply("B", "G").intValue());
    Assert.assertEquals("GB", 7, finder.apply("G", "B").intValue());

    Assert.assertEquals("BZ", -1, finder.apply("B", "Z").intValue());
    Assert.assertEquals("ZB", -1, finder.apply("Z", "B").intValue());

    Assert.assertEquals(-1, finder.apply("Z", "T").intValue());
  }
  
  @Test
  public void testSentenceChecker() {
    checkSentenceChecker("ilikeicecreamz", true);
    checkSentenceChecker("ilikeicecream", true);
    checkSentenceChecker("ilikeicecreamzz", false);
    checkSentenceChecker("b", false);
    checkSentenceChecker("", false);
  }

  public void checkSentenceChecker(String sentence, boolean expected) {
    HashSet<String> dictionary = new HashSet<>();
    dictionary.add("i");
    dictionary.add("like");
    dictionary.add("ice");
    dictionary.add("cream");
    dictionary.add("icecream");
    dictionary.add("creamz");
    
    SentenceChecker target = new SentenceChecker();
    // When
    boolean actual = target.isValidSentence(sentence, dictionary);
    // Then
    Assert.assertEquals(expected, actual);
  }
}
