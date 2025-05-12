#!/usr/bin/env bash

rm V2VM.class

javac -cp vapor-parser.jar:. V2VM.java 

# can update later with vars $1 $2
#java  V2VM < P.vapor > P.vaporm

# java V2VM < ../Tests/vapor/BinaryTree.vapor > P.vaporm   #error too many calls so locals is too small
# java V2VM < ../Tests/vapor/BubbleSort.vapor > P.vaporm   #loops get 
# java V2VM < ../Tests/vapor/Factorial.vapor > P.vaporm   #pass
# java V2VM < ../Tests/vapor/LinearSearch.vapor> P.vaporm  #pass
# java V2VM < ../Tests/vapor/LinkedList.vapor > P.vaporm   #fails mem and idk
# java V2VM < ../Tests/vapor/MoreThan4.vapor > P.vaporm  #pass
# java V2VM < ../Tests/vapor/QuickSort.vapor > P.vaporm
# java V2VM < ../Tests/vapor/TreeVisitor.vapor > P.vaporm
# java V2VM < ../Tests/vapor/whileTest.vapor > P.vaporm   #pass