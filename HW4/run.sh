#!/usr/bin/env bash

rm V2VM.class

javac -cp vapor-parser.jar:. V2VM.java 

# can update later with vars $1 $2
#java  V2VM < P.vapor > P.vaporm


# java V2VM < ../Tests/vapor/BinaryTree.vapor > P.vaporm   #incorrect and missing a significant amount of output
 java V2VM < ../Tests/vapor/BubbleSort.vapor > P.vaporm   #pass
# java V2VM < ../Tests/vapor/Factorial.vapor > P.vaporm   #pass
# java V2VM < ../Tests/vapor/LinearSearch.vapor> P.vaporm  #pass
# java V2VM < ../Tests/vapor/LinkedList.vapor > P.vaporm   #fails near the end after the 2220000 and just before the 3rd 1000000
# java V2VM < ../Tests/vapor/MoreThan4.vapor > P.vaporm  #pass
# java V2VM < ../Tests/vapor/QuickSort.vapor > P.vaporm   #lc pass
# java V2VM < ../Tests/vapor/TreeVisitor.vapor > P.vaporm  #out-of-bounds memory read
# java V2VM < ../Tests/vapor/whileTest.vapor > P.vaporm   #lc pass