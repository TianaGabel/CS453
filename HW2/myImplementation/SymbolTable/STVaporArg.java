package myImplementation.SymbolTable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

public class STVaporArg implements STVaporArgInterface{

    public final String INDENT = "    ";
    public int indentCount = 0;
    public void incrIndent(){indentCount++;}
    public void decrIndent(){indentCount--;}

    private int tempCounter = 0;
    private int controlCounter = 0;
    private int paramCount = 0;
    public void resetTemp(){tempCounter = 0;}
    //The idea is that I can keep expressions seperate from statement stuff
    public Deque<String> pendingLines = new ArrayDeque<>();
    public Stack<Deque> pendingStates = new Stack<>();
    public Stack<String> varStack = new Stack<>();
    public ArrayList<String> localsl = new ArrayList<>();

    public SymbolTableNode root;
    public SymbolTableNode currentClass;
    public SymbolTableNode currentMethod;

    public STVaporArg(SymbolTableNode r){
        root = r;
    }

    public void setRoot(SymbolTableNode r){
        root = r;
    }
    public SymbolTableNode getRoot(){
        return root;
    }

    public void setSTClass(SymbolTableNode r){
        currentClass = r; //TODO
    }
    public SymbolTableNode getSTClass(){
        return currentClass;
    }

    public void setSTMethod(SymbolTableNode r){
        currentMethod = r; //TODO
    }
    public SymbolTableNode getSTMethod(){
        return currentMethod;
    }

    public void addLocal(String id){
        locals.add(id);
        //System.out.println(locals);
    }

    public void resetLocals(){
        locals.clear();
    }

    public boolean islocal(String id){
        return locals.contains(id) || id.equals("this");
    }

    public void incrParamCount(){
        paramCount++;
    }

    public int getParamCount(){
        return paramCount;
    }

    public void resetParamCount(){
        paramCount = 0;
    }


    public String getTempVar() {
        StringBuilder sb = new StringBuilder();
        sb.append("t.");
        sb.append(tempCounter);
        return sb.toString();
    }
    public String newTempVar() {
        tempCounter++;
        StringBuilder sb = new StringBuilder();
        sb.append("t.");
        sb.append(tempCounter);
        return sb.toString();
    }

    public String newControlVar(String type){
        controlCounter++;
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(controlCounter);
        return sb.toString();
    }
    // addAfter
    // addbefore
    // print whatever is current stored, probably don't really care about the depth
    public void initStatement() {
        pendingStates.add(pendingLines);
        pendingLines = new ArrayDeque<>();
    }
    
    public void addbefore(String line) {
        pendingLines.addFirst(line);
    }

    public void addAfter(String line){
        pendingLines.addLast(line);
    }

    /// At the complete of a group everything currently in the queue printed
    public void printStatement(){
        for (String line: pendingLines){
            System.out.println(line);
        }
        // empty list of print statements
        // load previous pending lines
        pendingLines.clear();
        if (pendingStates.size() != 0){}
            pendingLines = pendingStates.pop();
    }

    public String printExpression(){
        printStatement();
        return varStack.peek();
    }

    //automatic set up for array allocation from lecutre slides
    public void programClose(){
        System.out.println("func AllocArray(size)\n" +
            "    bytes = MulS(size 4)\n" +
            "    bytes = Add(bytes 4)\n" +
            "    v = HeapAllocZ(bytes)\n" +
            "    [v] = size\n" +
            "    ret v");
    }
  
    public  String indents() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentCount; i++) {
            sb.append(INDENT);
        }

        return sb.toString();
    }

}