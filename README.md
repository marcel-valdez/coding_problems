# What is this?

It is a set of tests and solutions to the coding problems in the book
"Cracking the Coding Interview".

## Requirements

- Java 8
- ruby 1.9+


## Structure

### Solutions are under the src directory

```
/src/Chapter1.java
/src/Chapter2.java
...
/src/ChapterN.java
```

### Tests are under the test directory

```
/test/Chapter1Test.java
/test/Chapter2Test.java
...
/test/ChapterNTest.java
```

## How To

### Work on solutions

To start working on solutions you need the continuous
runner to execute. It will execute the test of the
corresponding solution that you modified, or if the
test is modified, it will execute the test that was
modified, there is currently no way to execute all tests
or to execute tests that correspond to utility files,
this can be done later.


```bash
# install all required gems
bundle install
# execute the continuous runner (Guard)
bundle exec guard
```

## TODO Notes

