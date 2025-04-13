package myImplementation.SymbolTable;

import java.util.ArrayList;

public class STMethodNode extends SymbolTableNode {
    ArrayList<STVariableNode> params = new ArrayList<>();
    VarType returnType;

    public STMethodNode(String id, SymbolType type){
        super(id,type);
    }

    public void addReturnType(String type){
        switch(type) {
            case "Integer":
                returnType = VarType.INT;
            case "Boolean":
                returnType = VarType.BOOL ;
                break;
            case "Array":
                returnType = VarType.ARRAY;
            default:
              // TODO should throw an error in the future
          }
    }

    public void addParam(String id, String type){
        params.add(new STVariableNode(id, type));
    }

    public void mapParams(){
        //When arguments are given
        //Check that arguments match the types given by params
    }
    
}
