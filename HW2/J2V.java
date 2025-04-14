import java.io.*;
import visitor.*;
import syntaxtree.*;
import java.util.*;
import myImplementation.*;

public class J2V {
    public static void main(String[] args) {
    Node root = null;
    try {
        root = new MiniJavaParser(System.in).Goal();

        // Probably should not overwrite System.out
        // Create a "symbol table" with the strings for each and concatenate the output
        
    }
    catch (ParseException e) {
        System.out.println(e.toString());
        System.exit(1);
    }
    }
}

