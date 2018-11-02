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
## Recommended Study Plan

### MVP

You should solve all of these problems at the minimum, to even have a chance at success.

- [ ] Chapter 1: Arrays and Strings
- [ ] Chapter 2: Linked Lists
- [ ] Chapter 3: Stacks and Queues
- [ ] Chapter 4: Trees and Graphs
- [ ] Chapter 9: Recursion and Dynamic Programming
- [ ] Chapter 11: Sorting and Searching
- [ ] Chapter 17: Moderate

### HIGH VALUE

Solving these significatively increases your chances of success.

- [ ] Chapter 10: Scalability and Memory Limits
- [ ] Chapter 18: Hard
- [ ] Chapter 5: Bit Manipulation
- [ ] Chapter 12: Testing

### RELATED TO THINGS RARELY ASKED

Solving these will increase your chances if you don't already know these subjects, but if you've worked with these before, they won't help a lot.

- [ ] Chapter 7: Mathematics and Probability
- [ ] Chapter 16: Threads and Locks
- [ ] Chapter 15: Databases
- [ ] Chapter 13: C and C++

### NOT VERY USEFUL

I found these to be of low value and having little to no useful information that one could benefit from in order to pass the interviews.

- [ ] Chapter 13: Java
  - Too basic to be of worth.
- [ ] Chapter 6: Brain Teasers
  - Nobody does brain teasers anymore.
- [ ] Chapter 8: Object-oriented design
  - The reason this was useless in my case, is that the information it has is very generic. I did my master's thesis on this subject.
