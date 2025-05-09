package myImplementation.SymbolTable;

import java.util.HashMap;

public class STVariableNode extends SymbolTableNode{
    public VarType vtype;
    public String classType; //only for instances
    public HashMap<String, SymbolTableNode> MapToInitialized = new HashMap<>();
    boolean hasValue;
    boolean[] childrenHasVals;
    int length; // arrays and instances
    
    
    //I'm going to base this off of how mpi handles variables
    //Just have an array? bool

    public STVariableNode(String id, String t){
        super(id, SymbolType.VARIABLE);
        switch(t) {
            case "Integer":
                vtype = VarType.INT;
                break;
            case "Boolean":
                vtype = VarType.BOOL ;
                break;
            case "Array":
                vtype = VarType.ARRAY;
                break;
            case "Instance":
                vtype = VarType.INSTANCE;
                break;
            default:
              // TODO should throw an error in the future
        }
        this.hasValue = false;
    }

    public STClassNode classType(){
        return (STClassNode)findExistingSymbolFromRoot(classType);
    }

    public void startArray(){
        //set length
        //generate array of size length of all false
    }

    public void startInstance(){
        
        //set length based on a method in class
        //generate array of size length of all false (possibly methods can be considered true)
    }

    @Override
    public String toString() {
        String s = "";
        s += identifier + " ";
        s += vtype + " ";
        return s;
    }

}
