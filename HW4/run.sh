#!/usr/bin/env bash

rm V2VM.class

javac -cp vapor-parser.jar:. V2VM.java 

# can update later with vars $1 $2
java  V2VM < P.vapor > P.vaporm

#java V2VM < ../Tests/simpleA.java > P.vaporm
# java V2VM < ../Tests/simple/inheritance.java > P.vaporm
#java V2VM < ../Tests/simple/V2VMexamples.java > P.vaporm
# java V2VM < ../Tests/simple/whileandif.java > P.vaporm   


# java V2VM < ../Tests/mj/BinaryTree.java > P.vaporm   
# java V2VM < ../Tests/mj/BubbleSort.java > P.vaporm
# java V2VM < ../Tests/mj/Factorial.java > P.vaporm
# java V2VM < ../Tests/mj/LinearSearch.java > P.vaporm
# java V2VM < ../Tests/mj/LinkedList.java > P.vaporm
# java V2VM < ../Tests/mj/MoreThan4.java > P.vaporm  
# java V2VM < ../Tests/mj/QuickSort.java > P.vaporm
# java V2VM < ../Tests/mj/TreeVisitor.java > P.vaporm