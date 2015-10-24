import org.junit.*;
import org.junit.runners.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RunJunit {
  private static Class<?> classToTest = null;
  // Arg 1: directory where classes live
  // Arg 2: class name to execute (optional, if empty then means to run all, including self)
  public static void main(String[] args) throws Exception {
    if(args.length <= 0) {
      throw new Exception("Usage: java RunJunit <classes directory> [<class name>]");
    }

    final String testName;
    if(args.length < 2) {
      testName = "RunJunit";
    } else {
      testName = args[1];
    }

    executeTest(testName);
  }

  private static void executeTest(String testClassName) throws Exception {
    try {
    System.out.println("Testing class: " + testClassName);
    Result result = executeTestClass(Class.forName(testClassName));
    printFailures(result.getFailures());
    printSummary(result);
    exit(result);
    } catch (TimeoutException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }


  private static Result executeTestClass(Class<?> testClass) throws Exception {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      final JUnitCore jUnitCore = new JUnitCore();

      final Future<Result> future =
        executor.submit(() -> jUnitCore.run(testClass));
      Result result = future.get(5, TimeUnit.SECONDS);
      return result;
    } finally {
      executor.shutdownNow();
    }
  }

  private static void exit(Result result) {
    if(result.wasSuccessful()) {
      System.exit(0);
    } else {
      System.exit(1);
    }
  }

  public static void printFailures(List<Failure> failures) {
    for(Failure failure : failures) {
      System.out.println(failure.toString());
      if(!(failure.getException() instanceof AssertionError)) {
        System.out.println(toShortTrace(failure.getTrace(), 20));
      } else {
        System.out.println(toShortTrace(failure.getTrace(), 8));
      }
    }
  }

  private static String toShortTrace(String fullTrace, int maxTraceLines) {
    String[] shortTrace = Stream
      .of(fullTrace.split("\n"))
      .limit(maxTraceLines)
    .toArray(String[]::new);
    return String.join("\n", shortTrace);
  }

  public static void printSummary(Result result) {
     String summary = "";
     summary += "Tests run: " + result.getRunCount() + " ";
     summary += "ignored: " + result.getIgnoreCount() + " ";
     summary += "failed: " + result.getFailureCount() + " ";
     summary += "time: " + (result.getRunTime() / 1000) + "s";
     System.out.println(summary);
  }

  @Test
  public void failingTest() {
    Assert.assertEquals("This should always fail.", true, false);
    System.out.println("Executed test for RunJunit");
  }

  @Test
  public void passingTest() {
    Assert.assertEquals("This should always pass.", true, true);
  }

  @Test
  public void testWithException() {
   throw new RuntimeException("How does this look?");
  }
}
