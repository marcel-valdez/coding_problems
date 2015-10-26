package chapter1;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import java.lang.*;

public class Chapter1Test {
  @Rule
  public ErrorCollector errors = new ErrorCollector();

  UniqueCharsIdentifier uniqueIdentifier = new UniqueCharsIdentifier();
  StringReverser reverser = new StringReverser();
  StringPermutationChecker permutationChecker = new StringPermutationChecker();
  SpaceReplacer spaceReplacer = new SpaceReplacer();
  StringCompressor stringCompressor = new StringCompressor();
  Matrix90DegreeRotator matrixRotator = new Matrix90DegreeRotator();
  MatrixClearer matrixClearer = new MatrixClearer();
  StringRotationChecker rotationChecker = new StringRotationChecker();



  @Test
  public void testReverseString() {
    errors.checkThat(reverser.reverse(null), equalTo(null));
    errors.checkThat(reverser.reverse(""), equalTo(""));
    errors.checkThat(reverser.reverse("A"), equalTo("A"));
    errors.checkThat(reverser.reverse("AB"), equalTo("BA"));
    errors.checkThat(reverser.reverse("ABC"), equalTo("CBA"));
    errors.checkThat(reverser.reverse("ABCD"), equalTo("DCBA"));
  }

  @Test
  public void testStringHasAllUniqueCharacters() {
    checkUniqueChars("");
    checkUniqueChars("A");
    checkUniqueChars("ABC");
    checkUniqueChars("Aa");

    checkNonUniqueChars("ABCA");
    checkNonUniqueChars("AA");
  }

  private void checkUniqueChars(String str) {
    errors.checkThat(uniqueIdentifier.hasUniqueChars(str), is(true));
  }

  private void checkNonUniqueChars(String str) {
    errors.checkThat(uniqueIdentifier.hasUniqueChars(str), is(false));
  }

  @Test
  public void testStringPermutationChecker() {
    errors.checkThat(permutationChecker.check("", ""), is(true));
    errors.checkThat(permutationChecker.check("A", "A"), is(true));
    errors.checkThat(permutationChecker.check("AB", "BA"), is(true));
    errors.checkThat(permutationChecker.check("AB", "AB"), is(true));
    errors.checkThat(permutationChecker.check("ABC", "BCA"), is(true));
    errors.checkThat(permutationChecker.check("ABB", "BBA"), is(true));

    errors.checkThat(permutationChecker.check("ABB", "CBA"), is(false));
    errors.checkThat(permutationChecker.check("A", ""), is(false));
    errors.checkThat(permutationChecker.check("A", "B"), is(false));
    errors.checkThat(permutationChecker.check("A", "BA"), is(false));
    errors.checkThat(permutationChecker.check("AD", "BA"), is(false));
    errors.checkThat(permutationChecker.check("ADB", "CBA"), is(false));
  }

  @Test
  public void testReplaceSpaceWithEscape() {
    checkReplaceSpaceWithEscape("Mr John Smith    ", 13, "Mr%20John%20Smith");
    checkReplaceSpaceWithEscape("   ", 1, "%20");
    checkReplaceSpaceWithEscape("Mr", 2, "Mr");
    checkReplaceSpaceWithEscape("", 0, "");
    checkReplaceSpaceWithEscape("A   ", 2, "A%20");
    checkReplaceSpaceWithEscape(" A  ", 2, "%20A");
    checkReplaceSpaceWithEscape(" A     ", 3, "%20A%20");
    checkReplaceSpaceWithEscape(" A B    ", 4, "%20A%20B");
    checkReplaceSpaceWithEscape(" A B       ", 5, "%20A%20B%20");
  }

  private void checkReplaceSpaceWithEscape(String inputStr, int length, String expectedStr) {
    char[] input = inputStr.toCharArray();
    char[] expected = expectedStr.toCharArray();

    spaceReplacer.replace(input, length);
    errors.checkThat(input, equalTo(expected));
  }

  @Test
  public void testStringCompressor() {
    errors.checkThat(stringCompressor.compress(""), equalTo(""));
    errors.checkThat(stringCompressor.compress("a"), equalTo("a"));
    errors.checkThat(stringCompressor.compress("aa"), equalTo("aa"));
    errors.checkThat(stringCompressor.compress("aaa"), equalTo("a3"));

    errors.checkThat(stringCompressor.compress("aabcccccaaa"), equalTo("a2b1c5a3"));
    errors.checkThat(stringCompressor.compress("abc"), equalTo("abc"));
    errors.checkThat(stringCompressor.compress("aabbcc"), equalTo("aabbcc"));
  }

  @Test
  public void testMatrixRotator3x3() {
    byte[] pixelA = new byte[] { 1, 2, 3, 4 };
    byte[] pixelB = new byte[] { 5, 6, 7, 8 };
    byte[] pixelC = new byte[] { 9, 0, 1, 2 };
    byte[] pixelD = new byte[] { 3, 4, 5, 6 };
    byte[] pixelE = new byte[] { 7, 8, 9, 0 };
    byte[] pixelF = new byte[] { 10, 11, 12, 13 };
    byte[] pixelG = new byte[] { 14, 15, 16, 17 };
    byte[] pixelH = new byte[] { 18, 19, 20, 21 };
    byte[] pixelI = new byte[] { 22, 23, 24, 25 };

    // A B C       G D A
    // D E F  -->  H E B
    // G H I       I F C

    byte[][][] input = new byte[][][] {
      { pixelA, pixelB, pixelC },
      { pixelD, pixelE, pixelF },
      { pixelG, pixelH, pixelI }
    };

    byte[][][] expected = new byte[][][] {
      { copyPixel(pixelG), copyPixel(pixelD), copyPixel(pixelA) },
      { copyPixel(pixelH), copyPixel(pixelE), copyPixel(pixelB) },
      { copyPixel(pixelI), copyPixel(pixelF), copyPixel(pixelC) }
    };

    matrixRotator.rotate(input);

    errors.checkThat(input, equalTo(expected));
  }

  @Test
  public void testMatrixRotator2x2() {
    byte[] pixelA = new byte[] { 1, 2, 3, 4 };
    byte[] pixelB = new byte[] { 5, 6, 7, 8 };
    byte[] pixelD = new byte[] { 3, 4, 5, 6 };
    byte[] pixelE = new byte[] { 7, 8, 9, 0 };

    // A B       D A
    // D E  -->  E B

    byte[][][] input = new byte[][][] {
      { pixelA, pixelB },
      { pixelD, pixelE }
    };

    byte[][][] expected = new byte[][][] {
      { copyPixel(pixelD), copyPixel(pixelA) },
      { copyPixel(pixelE), copyPixel(pixelB) }
    };

    matrixRotator.rotate(input);

    errors.checkThat(input, equalTo(expected));
  }

  private static byte[] copyPixel(byte[] pixel) {
    return new byte[] { pixel[0], pixel[1], pixel[2], pixel[3] };
  }

  @Test
  public void testZeroRowAndColumn3x3() {
    // given
    byte[][] matrix = new byte[][] {
      { 1, 0, 2 },
      { 3, 4, 0 },
      { 5, 6, 7 }
    };

    byte[][] expected = new byte[][] {
      { 0, 0, 0 },
      { 0, 0, 0 },
      { 5, 0, 0 }
    };
    //when
    matrixClearer.clearZeros(matrix);
    //then
    errors.checkThat(matrix, equalTo(expected));
  }

  @Test
  public void testZeroRowAndColumn2x2() {
    // given
    byte[][] matrix = new byte[][] {
      { 1, 0 },
      { 3, 4 }
    };

    byte[][] expected = new byte[][] {
      { 0, 0 },
      { 3, 0 }
    };
    //when
    matrixClearer.clearZeros(matrix);
    //then
    errors.checkThat(matrix, equalTo(expected));
  }

  @Test
  public void testStringRotationChecker() {
    errors.checkThat(rotationChecker.isRotation("", ""), is(true));
    errors.checkThat(rotationChecker.isRotation("a", "a"), is(true));
    errors.checkThat(rotationChecker.isRotation("aa", "aa"), is(true));
    errors.checkThat(rotationChecker.isRotation("ab", "ba"), is(true));
    errors.checkThat(rotationChecker.isRotation("abc", "cab"), is(true));

    errors.checkThat(rotationChecker.isRotation("", "b"), is(false));
    errors.checkThat(rotationChecker.isRotation("a", ""), is(false));
    errors.checkThat(rotationChecker.isRotation("a", "b"), is(false));
    errors.checkThat(rotationChecker.isRotation("a", "ab"), is(false));
    errors.checkThat(rotationChecker.isRotation("aa", "a"), is(false));
    errors.checkThat(rotationChecker.isRotation("ab", "ac"), is(false));
    errors.checkThat(rotationChecker.isRotation("abc", "acb"), is(false));
    errors.checkThat(rotationChecker.isRotation("cab", "bac"), is(false));
    errors.checkThat(rotationChecker.isRotation("abc", "cba"), is(false));


  }
}
