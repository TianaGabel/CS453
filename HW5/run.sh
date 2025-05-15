#!/usr/bin/env bash

rm VM2M.class

javac -cp vapor-parser.jar:. VM2M.java 

# java -jar mars.jar nc <input-file>
java VM2M < P.vaporm > P.s


# java VM2M < ../Tests/vaporm/BinaryTree.vaporm > P.s  
# java VM2M < ../Tests/vaporm/BubbleSort.vaporm > P.s   
# java VM2M < ../Tests/vaporm/ShortCircuit.vaporm > P.s  
# java VM2M < ../Tests/vaporm/Factorial.vaporm > P.s  
# java VM2M < ../Tests/vaporm/LinearSearch.vaporm> P.s  
# java VM2M < ../Tests/vaporm/LinkedList.vaporm > P.s  
# java VM2M < ../Tests/vaporm/MoreThan4.vaporm > P.s  
# java VM2M < ../Tests/vaporm/QuickSort.vaporm > P.s  
# java VM2M < ../Tests/vaporm/TreeVisitor.vaporm > P.s 
# java VM2M < ../Tests/vaporm/whileTest.vaporm > P.s