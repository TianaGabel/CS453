import java.io.*;
import visitor.*;
import syntaxtree.*;
import syntaxtree.Node;

import java.util.*;
import myImplementation.*;
import myImplementation.SymbolTable.STVaporArg;
import myImplementation.SymbolTable.STVaporArgInterface;
import myImplementation.SymbolTable.SymbolTableNode;

public class J2V {
    public static void main(String[] args) {
    Node root = null;
    try {
        root = new MiniJavaParser(System.in).Goal();

		SymTableVisitor<Void, String> pv = new SymTableVisitor<Void,String>();
		pv.createRoot();
		root.accept(pv);

        SymbolTableNode STroot = pv.getRoot();
        ArrayList<SymbolTableNode> STClasses = STroot.getChildren();

        // TODO get the file extension from the command line
        //PrintStream fileStream = new PrintStream(vapor_program);
        //System.setOut(fileStream);

        String INDENT = "    ";

        for (SymbolTableNode n: STClasses) {
            if (n.getType().equals("CLASS")){
                System.out.println("const vmt_" + n.identifier);
                for (SymbolTableNode m: n.getChildren()) {
                    if (m.getType().equals("METHOD")) {
                        System.out.println(INDENT + ":" + n.identifier + "." + m.identifier);
                    }
                }
                System.out.println("");
            }
        }

        ToVaporVisitor<STVaporArg> vaporV = new ToVaporVisitor<>();
		root.accept(vaporV, new STVaporArg(STroot));

    }
    catch (ParseException e) {
        System.out.println(e.toString());
        System.exit(1);
    }
    }
}

