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
      Result result = executeTestClass(findClass(testClassName));
      printFailures(result.getFailures());
      printSummary(result);
      exit(result);
    } catch(ClassNotFoundException e) {
      System.out.println("Test class " + testClassName + " does not exist.");
      System.exit(1);
    } catch (TimeoutException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static Class<?> findClass(String simpleClassName) throws Exception {
    ClassLoader classLoader = RunJunit.class.getClassLoader();
    Enumeration<URL> roots = classLoader.getResources("");
    List<String> classFiles = new ArrayList<>();
    while(roots.hasMoreElements()) {
      URL url = roots.nextElement();
      File file = new File(url.getPath());
      classFiles.addAll(getAllClassFiles(file, file));
    }

    Optional<String> qClassName = classFiles.stream()
      .filter(className -> className.endsWith(simpleClassName))
      .findFirst();
    if(qClassName.isPresent()) {
      return Class.forName(qClassName.get());
    } else {
      throw new RuntimeException("No class for test name: " + simpleClassName);
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
      System.out.println(failure.getDescription());
      if(!(failure.getException() instanceof AssertionError)) {
        System.out.println(toShortTrace(failure.getTrace(), 20));
      } else {
        System.out.println(toShortTrace(failure.getTrace(), 10));
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
