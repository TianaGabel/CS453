#!/usr/bin/env bash

rm J2V.class
javac J2V.java

# can update later with vars $1 $2


#java J2V < ../Tests/simpleA.java > P.vapor    #pass
# java J2V < ../Tests/simple/inheritance.java > P.vapor  #pass
#java J2V < ../Tests/simple/J2Vexamples.java > P.vapor   #pass
# java J2V < ../Tests/simple/whileandif.java > P.vapor   

# java J2V < ../Tests/mj/MoreThan4.java > P.vapor #pass
# java J2V < ../Tests/mj/BinaryTree.java > P.vapor  #pass bug was reversed params    
# java J2V < ../Tests/mj/BubbleSort.java > P.vapor   #fail looks like array issue
# java J2V < ../Tests/mj/Factorial.java > P.vapor
# java J2V < ../Tests/mj/LinearSearch.java > P.vapor
 java J2V < ../Tests/mj/LinkedList.java > P.vapor
# java J2V < ../Tests/mj/MoreThan4.java > P.vapor     
# java J2V < ../Tests/mj/QuickSort.java > P.vapor
# java J2V < ../Tests/mj/TreeVisitor.java > P.vapor   