package chapter10;

import static debug.Debugger.DEBUG;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;

import structures.*;

class WordFinder {
  private final Document[] documents;

  public WordFinder(Document ... documents) {
    this.documents = documents;
  }

  // Solution:
  // a) Create a trie with all the words in all the documents and
  // on each 'leaf' node put the id of the document that contains
  // that leaf node. Then it is just a matter of running through
  // the trie to figure out if the document is in the trie.
  // Advantage; Search is O(key) <-- key is small, might as well be O(K)
  // Problem: Memory usage is O(#docs * avg_len(docs))
  //
  // b) Instead of using a Trie you can use a HashMap<String> and
  // and simply put the doucment key on each word in all the documents
  // Advantage: Search is O(1), however there is a high likelyhood that
  // there will be colision since there will be a huge number of entries
  // 1 word for each document.
  // Problem: Memory usage is O(#docs * avg_len(docs))
  //
  // Mitigation: For storage in both cases we can store a system that is
  // MADE to store and retrieve data efficiently, for example:
  // + RDBMS where PK is the word and a column has the associated document
  //   IDs.
  // + Use an in-memory key-value data base, which should have very efficient
  //   retrieval, and a memory could be shared amongst multiple machines using
  //   technology such as memcached (distributed object-caching mechanism).
  //
  // How can we improve the memory usage of this solution?
}

class Document {
  private final String[] content;
  private final Object id;

  public Document(Object id, String ... content) {
    this.id = id;
    this.content = content;
  }

  public String word(int index) {
    return content[index];
  }

  public int wordCount() {
    return this.content.length;
  }

  public Object id() {
    return this.id;
  }
}


class PathFinder {

  public SingleLinkNode<Person> findPath(Person a, Person b) {
    Map<String, SingleLinkNode<Person>> discoverySetB = new HashMap<>();
    Stack<SingleLinkNode<Person>> searchStackB = new Stack<>();
    searchStackB.add(node(b));

    Map<String, SingleLinkNode<Person>> discoverySetA = new HashMap<>();
    Stack<SingleLinkNode<Person>> searchStackA = new Stack<>();
    searchStackA.add(node(a));

    int maxSearch = 3;
    int searchIterations = 0;

    while(searchIterations <= maxSearch) {
      searchIterations++;
      SingleLinkNode<Person> pathAtoB =
        iterateGraph(discoverySetB, discoverySetA, searchStackA);
      if(pathAtoB != null) {
        return pathAtoB;
      }

      SingleLinkNode<Person> pathBtoA =
        iterateGraph(discoverySetA, discoverySetB, searchStackB);
      if(pathBtoA != null) {
        return pathBtoA;
      }
    }

    return null;
  }

  private SingleLinkNode<Person> iterateGraph(
    Map<String, SingleLinkNode<Person>> destConnections,
    Map<String, SingleLinkNode<Person>> srcConnections,
    Stack<SingleLinkNode<Person>> searchStack) {

    while(!searchStack.isEmpty()) {
      SingleLinkNode<Person> connection = searchStack.pop();
      if(destConnections.containsKey(connection.value().username())) {
        // find the path that contains the connection to the node
        SingleLinkNode<Person> bConnection =
          destConnections.get(connection.value().username());
        // join connection paths, iterate one in reverse order
        return joinPaths(connection, bConnection);
      }

      for(Person nextConnection : connection.value().connections()) {
        if(!srcConnections.containsKey(nextConnection.username())) {
          SingleLinkNode<Person> nextConnectionNode = node(nextConnection);
          nextConnectionNode.next(connection);
          srcConnections.put(connection.value().username(), nextConnectionNode);
          // now we have a stack of paths
          searchStack.push(nextConnectionNode);
        }
      }
    }

    return null;
  }

  private SingleLinkNode<Person> joinPaths(
      SingleLinkNode<Person> source, SingleLinkNode<Person> destination) {
    SingleLinkNode<Person> head = destination.next();
    SingleLinkNode<Person> next = source;
    while(head != null) {
      SingleLinkNode<Person> nextDestination = head.next();
      head.next(next);
       next = head;
      head = nextDestination;
    }
    return next;
  }

  SingleLinkNode<Person> node(Person person) {
    return new SingleLinkNode<Person>(person);
  }
}

class Person {
  private List<Person> connections = new ArrayList<>();

  private final String username;
  public Person(String username) {
    this.username = username;
  }

  public void add(Person connection) {
    this.connections.add(connection);
  }

  public Person[] connections() {
    return connections.toArray(new Person[connections.size()]);
  }

  public String username() { return username; }

  public int hashCode() {
    return username.hashCode();
  }

  public final boolean equals(Object other) {
    if(!(other instanceof Person)) {
      return false;
    }

    Person person = (Person) other;
    if(person.username == null || username == null) {
      return username == person.username;
    }

    return username.equals(person.username);
  }
}

// 1 Gb available RAM
// 4,000,000,000 non-negative numbers
// -assuming the file is binary and numbers are stored as 32-bit sequential bits,
//  so we the file would be read 32-bits at a time
//
// File size: 4x10^9 * 4 bytes = 16x10^9 bytes = 16x10^6 kb = 16*10^3 Mb = 16 Gb
//
// No matter what, we need to read all of the numbers, in order to ensure that
// whatever number we generate, it is not one of the 4 billion numbers
//
// Problem: If I process 1Gb worth of numbers, that doesn't help me if I cannot somehow
// store the results of having processed all those numbers.
// Potential #1: Generate ranges with the ranges of numbers, but if the numbers are
// all skipped by 1, then this wouldn't work.
// Questions: Is there a way to store multiple numbers in less memory?
// A trie could be used to store all the numbers where each digit is a character
// for example, to store the first 100 numbers:
// it would require 100-character nodes (1-byte each) + 1 boolean flag to say if
// it was actually picked + 100 memory pointers 64-bit each :(


/*
class Node {
  boolean found = false;
  Node[] nodes = new Node[10]; // 64-bit for Node[] + 64-bit * 10 for each Node[]
}
*/

class FindNewNumber {
  final int bytesTotalSize = 16 * 1000000000;
  final int bytes250Mb = 250 * 1000000;
  final int bytes500Mb = bytes250Mb * 2;

  public int findNumber(String filename) {
    // big file name is 16 Gb
    // requires 500Mb memory
    sort(filename, filename + "sorted", 0, bytesTotalSize);
    IntegerStream numbers = createStream(filename + "sorted", 0, bytesTotalSize);

    int prevNum = numbers.next();
    while(numbers.hasNext()) {
      int number = numbers.next();
      if(number - prevNum > 1) {
        return number - 1;
      }
      prevNum = number;
    }

    throw new RuntimeException("Impossible!");
  }

  private void sort(String srcFile, String dstFile, int start, int size) {
    String part0 = "tmp" + start + "-" + size + "0";
    String part1 = "tmp" + start + "-" + size + "1";
    if(size > 1000000000) {
      sort(srcFile, part0, start, size / 2);
      sort(srcFile, part1, start, size / 2);
      mergeTo(
        createStream(part0, 0, size / 2),
        createStream(part1, 0, size / 2),
        dstFile
      );
    } else {
      sort500MbTo(srcFile, start, part0);
      sort500MbTo(srcFile, start + bytes500Mb, part1);

      // requires no memory
      IntegerStream part0Stream = createStream(part0, 0, bytes500Mb);
      IntegerStream part1Stream = createStream(part1, 0, bytes500Mb);
      mergeTo(part0Stream, part1Stream, dstFile);
    }

    deleteFile(part0);
    deleteFile(part1);
  }

  private void deleteFile(String filename) {}
  private void mergeTo(IntegerStream a, IntegerStream b, String outputFile) {
    FileStream fileStream = openFile(outputFile);
    int aNum = a.next();
    int bNum = b.next();
    while(a.hasNext() || b.hasNext()) {
      if(bNum < 0) {
        writeInt(fileStream, aNum);
        aNum = a.next();
        continue;
      }

      if(aNum < 0) {
        writeInt(fileStream, bNum);
        bNum = b.next();
        continue;
      }

      if(aNum < bNum) {
        writeInt(fileStream, aNum);
        aNum = a.next();
      } else {
        writeInt(fileStream, bNum);
        bNum = b.next();
      }
    }
  }

  private FileStream openFile(String filename) {return null;}
  private void writeInt(FileStream file, int num) {}

  private void sort500MbTo(String srcFilename, int startPoint, String dstFilename) {
    int[] numbers = loadAndSort(srcFilename, 0, bytes500Mb);
    writeToFile(numbers, dstFilename);
    numbers = null;
    System.gc();
  }

  private int[] loadAndSort(String filename, int startPoint, int size) {
    int[] numbers = load(filename, startPoint, size);
    mergesort(numbers);
    return numbers;
  }

  private int[] load(String filename, int startPoint, int size) {
    IntegerStream stream =
      createStream(filename, size, startPoint);
    return stream.toArray();
  }

  public IntegerStream createStream(String filename, int readSize, int startPoint) {
    return new IntegerStream(filename, readSize, startPoint);
  }

  // performs an in-line mergesort
  public void mergesort(int[] numbers) {}
  private void writeToFile(int[] numbers, String filename) {}

  class IntegerStream {
    private final String filename;
    // the amount of data to read from the filename,
    // after that stop reading and claim there are no more numbers.
    private final int readSize;
    // point at which to start reading from the file
    private final int startPoint;

    public IntegerStream(String filename, int readSize, int startPoint) {
      this.filename = filename;
      this.readSize = readSize;
      this.startPoint = startPoint;
    }

    // returns Integer.MIN_VALUE if no more numbers available
    public int next() {
      // loads the next number from the stream
      return 0;
    }

    public boolean hasNext() {
      return false;
    }

    // reads all the remaining numbers and builds an array of size readSize - read
    public int[] toArray() {
      return new int[0];
    }
  }
}

class FileStream {
  public void writeInt(int num) {}
}

// If you are writing a web crawler how do you avoid going into infinite recursion?
// By keeping a Set of all visited paths as you visit them.
class WebCrawler {

  public List<Object> crawl(Url url) {
    List<Object> data = new ArrayList<>();
    Set<Url> visitedUrls = new HashSet<>();
    Deque<Url> toVisitUrls = new ArrayDeque<>();
    toVisitUrls.push(url);
    while(!toVisitUrls.isEmpty()) {
      Url urlToVisit = toVisitUrls.pop();
      crawlPage(urlToVisit, data, visitedUrls);
      crawlUrls(urlToVisit, visitedUrls, toVisitUrls);
    }

    return data;
  }

  private Object extract(Url page) {
    return new Object();
  }

  private void crawlPage(Url url, List<Object> data, Set<Url> visitedUrls) {
    data.add(extract(url));
    visitedUrls.add(url);
  }

  private void crawlUrls(Url url, Set<Url> visitedUrls, Deque<Url> toVisitUrls) {
    for(Url foundUrl : crawlUrls(url)) {
      if(!visitedUrls.contains(foundUrl)) {
        toVisitUrls.push(foundUrl);
      }
    }
  }

  private Url[] crawlUrls(Url url) {
    return new Url[0];
  }
}

class Url {
}

// There *are* 10 billion urls in the internet you are interested
// in crawling, you have a way of reading them all, but now you
// want to detect duplicates within those 10 billion urls.
// How would you do this?
// Problem: Big amount of data, how much?
// 10*10^9 = 10^10 URLS
// 100 chars each = 100 * 2 byte = 200 bytes each (assuming each char is 2 bytes)
// 20^12 bytes = 20^9 Kbytes = 20^6 Mbytes = 20^3 Gb = 2 Tb
