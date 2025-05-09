package myImplementation.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class STClassNode extends SymbolTableNode{
    public HashMap<String, Integer> mapToFunctionTable = new HashMap<>();
    public HashMap<String, Integer> mapToFields = new HashMap<>();
    STClassNode ParentClass;
    int size;

    public STClassNode(String id, SymbolType type){
        super(id,type);
    }

    public void addExtendsAttribute(String id){
        ParentClass = new STClassNode(id, SymbolType.CLASS);
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
            children.addAll(getChildren());
            for (SymbolTableNode n: pc.fields()){
                boolean elderhasattribute = false;
                for (SymbolTableNode child: children){
                    if(child.identifier.equals(n.identifier)){
                        elderhasattribute = true;
                    }
                }
                if (!elderhasattribute){
                    children.add(n);
                }

            }
        }

        return children;
    }
    

    //Stuff for vapor
    public void setupStuff(){
        ArrayList<SymbolTableNode> f = fields();
        int fieldCount = 0;
        int methodCount = 0;
        for (SymbolTableNode node: f){
            if(node instanceof STVariableNode){
                //System.out.println("-v-" + node.identifier);
                fieldCount += 4;
                mapToFields.put(node.identifier,fieldCount);
            }else if (node instanceof STMethodNode){
                //System.out.println("-m-" + node.identifier);
                mapToFunctionTable.put(node.identifier,methodCount);
                methodCount +=4;
            }
        }
        size = fieldCount + 4;
        //System.out.println(size);
    }

    public int mapField(String id){
        if (size == 0){
            setupStuff();
        }
        return mapToFields.get(id);
    }

    public int mapMethod(String id){
        if (size == 0){
            setupStuff();
        }
        return mapToFunctionTable.get(id);
    }
    
    public int getSize(){
        if (size == 0){
            setupStuff();
        }
        return size;
    }

}
