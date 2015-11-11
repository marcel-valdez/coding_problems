import org.junit.*;
import org.junit.runners.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RunJunit {
  private static final int ASSERTION_ERROR_TRACE_TRUNCATE = 20;
  private static final int USER_EXCEPTION_TRACE_TRUNCATE = 10;

  private static final int TIMEOUT_SEC = 10;
  private static final String RUN_ALL = "all";
  private static Class<?> classToTest = null;
  // Arg 1: directory where classes live
  // Arg 2: "ClassName" = class to execute
  //        "all" = run all tests
  //        <none> = run self
  public static void main(String[] args) throws Exception {
    if(args.length <= 0) {
      throw new Exception("Usage: java RunJunit <classes directory> [<class name>]");
    }

    final String testClassName;
    if(args.length < 2) {
      testClassName = "RunJunit";
    } else {
      testClassName = args[1];
    }

    if(!testClassName.equals(RUN_ALL)) {
      exit(executeTest(testClassName));
    } else {
      executeAllTests();
    }
  }

  private static void executeAllTests() {
    boolean testsPassed = true;
    String[] tests = getAllClassNames().stream()
      .filter(name -> name.endsWith("Test"))
      .toArray(String[]::new);
    for(String test : tests) {
      try {
        testsPassed = testsPassed && executeTest(test).wasSuccessful();
      } catch(Exception e) {
        testsPassed = false;
        System.out.println(e);
      }
    }

    System.exit(testsPassed ? 0 : 1);
  }

  private static Result executeTest(String testClassName) throws Exception {
    Result result = null;
    try {
      System.out.println("Testing class: " + testClassName);
      result = executeTestClass(findClass(testClassName));
      printFailures(result.getFailures());
      printSummary(result);
    } catch(ClassNotFoundException e) {
      System.out.println("Test class " + testClassName + " does not exist.");
      System.exit(1);
    } catch (TimeoutException e) {
      e.printStackTrace();
      System.exit(1);
    } catch(TestNotFoundException e) {
      executeAllTests();
    }

    return result;
  }

  private static Class<?> findClass(String simpleClassName) throws Exception {
    Optional<String> qClassName = getAllClassNames().stream()
      .filter(className -> className.endsWith(simpleClassName))
      .findFirst();
    if(qClassName.isPresent()) {
      return Class.forName(qClassName.get());
    } else {
      System.out.println("WARNING: No unit test " + simpleClassName);
      throw new TestNotFoundException("No class for test name: " + simpleClassName);
    }
  }

  private static List<String> getAllClassNames() {
    try {
      ClassLoader classLoader = RunJunit.class.getClassLoader();
      Enumeration<URL> roots = classLoader.getResources("");
      List<String> classFiles = new ArrayList<>();
      while(roots.hasMoreElements()) {
        URL url = roots.nextElement();
        File file = new File(url.getPath());
        classFiles.addAll(getAllClassFiles(file, file));
      }

      return classFiles;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> getAllClassFiles(File file, File rootDir) {
    List<String> classFiles = new ArrayList<>();
    if(file.getName().endsWith(".class")) {
      String relativePath = getRelativePath(file, rootDir);
      String qClassName = relativePath.replace(".class", "").replace("/", ".");
      classFiles.add(qClassName);
      return classFiles;
    }

    if(file.isDirectory()) {
      for(File content : file.listFiles()) {
        classFiles.addAll(getAllClassFiles(content, rootDir));
      }
    }

    return classFiles;
  }

  private static String getRelativePath(File file, File rootDir) {
    return rootDir.toURI().relativize(file.toURI()).getPath();
  }

  private static Result executeTestClass(Class<?> testClass) throws Exception {
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      final JUnitCore jUnitCore = new JUnitCore();
      final Future<Result> future =
        executor.submit(() -> jUnitCore.run(testClass));

      return future.get(TIMEOUT_SEC, TimeUnit.SECONDS);
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
      System.out.println(failure.getDescription());
      if(!(failure.getException() instanceof AssertionError)) {

        System.out.println(toShortTrace(failure.getTrace(), ASSERTION_ERROR_TRACE_TRUNCATE));
      } else {
        System.out.println(toShortTrace(failure.getTrace(), USER_EXCEPTION_TRACE_TRUNCATE));
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
     summary += "time: " + (result.getRunTime() / 100) + "ms";
     System.out.println(summary);
  }

  @Test
  @Ignore
  public void failingTest() {
    Assert.assertEquals("This should always fail.", true, false);
    System.out.println("Executed test for RunJunit");
  }

  @Test
  public void passingTest() {
    Assert.assertEquals("This should always pass.", true, true);
  }

  @Test
  @Ignore
  public void testWithException() {
   throw new RuntimeException("How does this look?");
  }

  private static class TestNotFoundException extends RuntimeException {
    public TestNotFoundException(String msg) {
      super(msg);
    }
  }
}
