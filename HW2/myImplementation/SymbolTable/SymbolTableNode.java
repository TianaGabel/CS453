package myImplementation.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

// Arg will likely not be used, and these may need to be updated
enum SymbolType {
    GOAL, MAIN, CLASS, METHOD, VARIABLE
}

enum VarType {
    ARRAY, INT, BOOL, INSTANCE
}

public class SymbolTableNode {

    static int order_of_execuetion = 0;
    public SymbolTableNode Parent;
    public String identifier;
    public SymbolType type;
    int rank;

    HashMap<String, SymbolTableNode> unorderedChildren = new HashMap<>();

    public SymbolTableNode(String id, SymbolType type){
        //System.out.println("Node " + id +  " of " + type);
        this.identifier = id;
        this.type = type;
        rank = order_of_execuetion;
        order_of_execuetion += 1;
    }

    //unordered
    public SymbolTableNode addNode(String id, String type){
        SymbolTableNode n;
        switch(type) {
            case "Class":
                n = new STClassNode(id, SymbolType.CLASS);
                break;
            case "Method":
                n = new STMethodNode(id, SymbolType.METHOD);
                break;
            case "Boolean":
            case "Integer":
            case "Array":
            case "Instance":
            case "Variable":
                n = new STVariableNode(id, type);
                break;
            case "Goal":
                n = new SymbolTableNode(id, SymbolType.GOAL);
                break;
            case "Main":
                n = new SymbolTableNode(id, SymbolType.MAIN);
                break;
            default:
                n = new SymbolTableNode(id, null);
              // TODO should throw an error in the future
          }
        //System.out.println("here");
        n.Parent = this;
        // if (n.Parent != null)
        //     System.out.println("      " + n.rank + n.Parent.identifier);
        unorderedChildren.put(n.identifier, n);
        return n;
    }

    public SymbolTableNode findExisitingSymbol(String id){
        //Looks through list of ordered nodes
        boolean found = false;
        SymbolTableNode n = this;
        while (!found && n != null){
            if(n.unorderedChildren.containsKey(id)){
                return n.unorderedChildren.get(id);
            }
            n = n.Parent;
        }

        // for (String classId: ParentClass){
        //     //TODO this will also need to account for order of inheritance
        //     n = findExisitingSymbolTopDown(classId, id);
        //     if (n != null) return n;
        // }

        return null;
    }

    public String getType(){
        return type.toString();
    }

    public SymbolTableNode findExisitingSymbolTopDown(String className, String id){
        //Traverse to root node
        SymbolTableNode n;
        for(n = this; n.Parent != null; n = n.Parent){}
        if (!n.unorderedChildren.containsKey(id)) return null;

        SymbolTableNode target = n.unorderedChildren.get(className);

        return target.unorderedChildren.get(id);
    }
    
    public ArrayList<SymbolTableNode> getChildren(){
        ArrayList<SymbolTableNode> children = new ArrayList<>();
        for (String id: unorderedChildren.keySet()){
            children.add(unorderedChildren.get(id));
        }
        return children;
    }
    
}
