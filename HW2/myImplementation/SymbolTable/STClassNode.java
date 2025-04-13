package myImplementation.SymbolTable;

import java.util.ArrayList;

public class STClassNode extends SymbolTableNode{
    ArrayList<STClassNode> ParentClass = new ArrayList<>();

    public STClassNode(String id, SymbolType type){
        super(id,type);
    }

    public void addExtendsAttribute(String id){
        ParentClass.add(new STClassNode(id, SymbolType.CLASS));
        //Creates a dummy point of reference for each
        //TODO will need future handling for inheritance beyond a single class
    }
    
}
