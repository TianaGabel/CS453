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

    ArrayList<String> ParentClass = new ArrayList<>();

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
        ParentClass.add(id);
        //TODO will need future handling for inheritance beyond a single class
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

        for (String classId: ParentClass){
            //TODO this will also need to account for order of inheritance
            n = findExisitingSymbolTopDown(classId, id);
            if (n != null) return n;
        }

        return null;
    }

    public SymbolTableNode findExisitingSymbolTopDown(String className, String id){
        //Traverse to root node
        SymbolTableNode n;
        for(n = this; n.Parent != null; n = n.Parent){}
        if (!n.unorderedChildren.containsKey(id)) return null;

        SymbolTableNode target = n.unorderedChildren.get(className);

        return target.unorderedChildren.get(id);
    }
    
    
}
