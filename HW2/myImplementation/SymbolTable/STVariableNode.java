package myImplementation.SymbolTable;

public class STVariableNode extends SymbolTableNode{
    VarType type;
    boolean hasValue;
    int length; //Only applicable to arrays
    
    
    //I'm going to base this off of how mpi handles variables
    //Just have an array? bool

    public STVariableNode(String id, String t){
        super(id, SymbolType.VARIABLE);
        switch(t) {
            case "Integer":
                type = VarType.INT;
            case "Boolean":
                type = VarType.BOOL ;
                break;
            case "Array":
                type = VarType.ARRAY;
            default:
              // TODO should throw an error in the future
        }
        this.hasValue = false;
    }

}
