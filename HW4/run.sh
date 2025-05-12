#!/usr/bin/env bash

rm V2VM.class

javac -cp vapor-parser.jar:. V2VM.java 

# can update later with vars $1 $2
#java  V2VM < P.vapor > P.vaporm



# java V2VM < ../Tests/vapor/BinaryTree.vapor > P.vaporm   #error in running
# java V2VM < ../Tests/vapor/BubbleSort.vapor > P.vaporm   #still failed due to a bad addition
# java V2VM < ../Tests/vapor/Factorial.vapor > P.vaporm   #pass
# java V2VM < ../Tests/vapor/LinearSearch.vapor> P.vaporm  #compiles and runs
# java V2VM < ../Tests/vapor/LinkedList.vapor > P.vaporm
 java V2VM < ../Tests/vapor/MoreThan4.vapor > P.vaporm  #pass
# java V2VM < ../Tests/vapor/QuickSort.vapor > P.vaporm
# java V2VM < ../Tests/vapor/TreeVisitor.vapor > P.vaporm