package myImplementation.SymbolTable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

public interface STVaporArgInterface {

    public void incrIndent();
    public void decrIndent();

    public void resetTemp();
    public Deque<String> pendingLines = new ArrayDeque<>();
    public Stack<Deque> pendingStates = new Stack<>();
    public Stack<String> varStack = new Stack<>();
    public ArrayList<String> locals = new ArrayList<>();

    public void setRoot(SymbolTableNode r);
    public SymbolTableNode getRoot();
    public void setSTClass(SymbolTableNode r);
    public SymbolTableNode getSTClass();
    public void setSTMethod(SymbolTableNode r);
    public SymbolTableNode getSTMethod();
    
    public void addLocal(String id);

    public boolean islocal(String id);
    public void resetLocals();

    public void incrParamCount();
    public int getParamCount();
    public void resetParamCount();

    public String indents();
    public String getTempVar();
    public String newTempVar();
    public String newControlVar(String type);
    // addAfter
    // addbefore
    // print whatever is current stored, probably don't really care about the depth
    public void initStatement();
    public void addbefore(String line);
    public void addAfter(String line);
    public void printStatement(); //Statements are usually self contained
    public String printExpression(); //Expressions usually have some sort of result, so passing along temp encompassing it


    public void programClose();
}