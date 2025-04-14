package myImplementation.SymbolTable;

import java.util.ArrayList;

public class STClassNode extends SymbolTableNode{
    STClassNode ParentClass;
    int size;

    public STClassNode(String id, SymbolType type){
        super(id,type);
        size = 4;
    }

    public void addExtendsAttribute(String id){
        ParentClass = new STClassNode(id, SymbolType.CLASS);
    }

    public int calculateSize(){
        //TODO this will use fields to calculate the size required for an object
        return 4;
    }

    public ArrayList<SymbolTableNode> fields(){
        SymbolTableNode parent = this.Parent;

        ArrayList<SymbolTableNode> children = new ArrayList<>();

        if(ParentClass == null){
            return getChildren();
        }else {
            STClassNode pc = ((STClassNode) parent.findExisitingSymbol(ParentClass.identifier));
            if (pc.identifier.equals(this.identifier)){
                //TODO throw circular inheritance error
            }
            children.addAll(pc.fields());
            children.addAll(getChildren());
        }

        return children;
    }
    
}
