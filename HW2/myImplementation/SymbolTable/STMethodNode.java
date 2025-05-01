package myImplementation.SymbolTable;

import java.util.ArrayList;

public class STMethodNode extends SymbolTableNode {
    public ArrayList<STVariableNode> params = new ArrayList<>();
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

    public void verifyParams(){
        //When arguments are given
        //Check that arguments match the types given by params
    }

    // @Override
    // public void isVar(String id){
    //     for(SymbolTableNode n: getChildren()){
    //         if (n.identifier.equals(id)){
    //             return;
    //         }
    //     }
    //     //this also doesn't consider class field if they exist
    //     //TODO Throw and exception
    // }

    @Override
    public ArrayList<SymbolTableNode> getChildren(){
        ArrayList<SymbolTableNode> children = new ArrayList<>();
        for (String id: unorderedChildren.keySet()){
            children.add(unorderedChildren.get(id));
        }
        children.addAll(params);
        return children;
    }

    //STUFF for VAPOR

    public String vaporFunctionDef(){
        String s = "func " + Parent.identifier + "." + this.identifier + "(this";
        for (SymbolTableNode n: params){
            s += " " + n.identifier;
        }
        s += ")";
        return s;
    }
    
}
