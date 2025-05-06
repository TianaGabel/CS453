package myImplementation.SymbolTable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

public interface STVaporArgInterface {

    public final String INDENT = "    ";
    public int indentCount = 0;
    public void incrIndent();
    public void decrIndent();

    public int tempCounter = 0;
    public void resetTemp();
    public Deque<String> pendingLines = new ArrayDeque<>();
    public Stack<Deque> pendingStates = new Stack<>();
    public Stack<String> varStack = new Stack<>();

    
    SymbolTableNode root = null;
    SymbolTableNode currentNode = null;
    String currentType = "";
    String expressionExitID = "";

    public void setRoot(SymbolTableNode r);

    public String indents();
    public String getTempVar();
    public String newTempVar();
    // addAfter
    // addbefore
    // print whatever is current stored, probably don't really care about the depth
    public void initStatement();
    public void addbefore(String line);
    public void addAfter(String line);
    public void printStatement(); //Statements are usually self contained
    public String printExpression(); //Expressions usually have some sort of result, so passing along temp encompassing it
}