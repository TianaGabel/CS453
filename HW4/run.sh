!# !/usr/bin/env bash

rm V2VM.class

javac -cp vapor-parser.jar:. V2VM.java 

# can update later with vars $1 $2
java V2VM < P.vapor > P.vaporm