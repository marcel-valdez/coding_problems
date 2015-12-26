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
}
