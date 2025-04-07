package myImplementation;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTableNode {

    static int order_of_execuetion = 0;
    SymbolTableNode Parent;
    String identifier;
    String type;
    boolean hasValue;  //all var start uninitialized
    int rank;

    String ParentClass;

    //Think Classes + Methods + Parameters
    HashMap<String, SymbolTableNode> unorderedChildren = new HashMap<>();

    public SymbolTableNode(String id, String type){
        this.identifier = id;
        this.type = type;
        this.hasValue = false;
        rank = order_of_execuetion;
        order_of_execuetion += 1;
    }

    //unordered
    public SymbolTableNode addNode(String id, String type){
        //TODO whose responsability is it to manage parameters
        //TODO prevent nodes that are not main class, class, or method from adding tables
        SymbolTableNode n = new SymbolTableNode(id, type);
        n.Parent = this;
        unorderedChildren.put(n.identifier, n);
        return n;
    }

    public void addExtendsAttribute(String id){
        ParentClass = id;
    }

    public boolean findExisitingSymbol(String id){
        //Looks through list of ordered nodes
        boolean found = false;
        SymbolTableNode n = this;
        while (!found && n != null){
            if(n.unorderedChildren.containsKey(id)){
                return true;
            }
            n = n.Parent;
        }

        if (ParentClass != null){
            findExisitingSymbolTopDown(ParentClass);
        }

        return false;
    }

    public boolean findExisitingSymbolTopDown(String id){
        //Traverse to root node
        SymbolTableNode n;
        for(n = this; n.Parent != null; n = n.Parent)

        return false;
    }
    
    
}
