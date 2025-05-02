#!/usr/bin/env bash

# rm J2V.class
javac Typecheck.java

# can update later with vars $1 $2
java Typecheck < ../Tests/simpleA.java


# Testing Suite
# print should be good
#java Typecheck < ../Tests/mj/BinaryTree.java
java Typecheck < ../Tests/mj/BubbleSort.java
java Typecheck < ../Tests/mj/Factorial.java
java Typecheck < ../Tests/mj/LinearSearch.java
 java Typecheck < ../Tests/mj/LinkedList.java
 java Typecheck < ../Tests/mj/MoreThan4.java
 java Typecheck < ../Tests/mj/QuickSort.java
# java Typecheck < ../Tests/mj/TreeVisitor.java

# print should not type check

# java Typecheck < ../Tests/mj-error/BinaryTree.java
# java Typecheck < ../Tests/mj-error/BubbleSort.java
# java Typecheck < ../Tests/mj-error/Factorial.java
# java Typecheck < ../Tests/mj-error/LinearSearch.java
#  java Typecheck < ../Tests/mj-error/LinkedList.java
#  java Typecheck < ../Tests/mj-error/MoreThan4.java
#  java Typecheck < ../Tests/mj-error/QuickSort.java
#  java Typecheck < ../Tests/mj-error/TreeVisitor.java