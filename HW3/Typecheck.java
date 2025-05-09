// Helper for HW2/CS453.
import java.io.*;
import visitor.*;
import syntaxtree.*;
import java.util.*;
import myImplementation.*;
// Files are stored in the picojava directory/package.
//import picojava.*;
import myImplementation.SymbolTable.STWrapper;
import myImplementation.TypeCheckSimp;

public class Typecheck {
    public static void main(String[] args) {
	Node root = null;
	try {
	    root = new MiniJavaParser(System.in).Goal();

		// Pretty-print the tree. PPrinter inherits from
	    // GJDepthFirst<R,A>. R=Void, A=String.
	    PPrinter<Void,String> pp = new PPrinter<Void,String>();
	    root.accept(pp);

	    // Build the symbol table. Top-down visitor, inherits from
	    // GJDepthFirst<R,A>. R=Void, A=Integer.
		SymTableVisitor<Void, String> pv = new SymTableVisitor<Void,String>();
		pv.createRoot();
		root.accept(pv);
	    //root.accept(pv);
	    // HashMap<String, String> symt = pv.symt;
		


	    // Do type checking. Bottom-up visitor, also inherits from
	    // GJDepthFirst. Visit functions return MyTpe (=R), and
	    // take a symbol table (HashMap<String,String>) as
	    // argument (=A). You may implement things differently of
	    // course!

	    TypeCheckSimp<STWrapper> ts = new TypeCheckSimp<STWrapper>(pv.getRoot());
	    root.accept(ts, (new STWrapper()));

	    // Ugly code not to be inspired from: "my" way of storing
	    // type info / typecheck property: if some of my internal
	    // structure is empty, then things don't typecheck for
	    // me. This is specific to my own implementation.

	    // if (res != null && res.type_array.size() > 0)
	    // 	System.out.println("Code typechecks");
	    // else
	    // 	System.out.println("Type error");
		System.out.println("-Code typechecks");
	}
	catch (Exception e) {
		System.out.println("-Type error");
	    //System.out.println(e.toString());
	    System.exit(1);
	}
    }
}
