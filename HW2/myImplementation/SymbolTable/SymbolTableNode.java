package myImplementation.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTableNode {

    static int order_of_execuetion = 0;
    public SymbolTableNode Parent;
    public String identifier;
    public SymbolType type;
    int rank;

    public HashMap<String, SymbolTableNode> unorderedChildren = new HashMap<>();

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
                n = new STMethodNode(id, SymbolType.MAIN);
                break;
            default:
                n = new SymbolTableNode(id, null);
              // TODO should throw an error in the future
          }
        n.Parent = this;
        unorderedChildren.put(n.identifier, n);
        return n;
    }

    public SymbolTableNode findExistingSymbolFromRoot(String id){
        SymbolTableNode n;
        for(n = this; n.Parent != null; n = n.Parent){} //Traverse to root node

        return n.findExisitingSymbol(id);
    }
    
    public SymbolTableNode findExisitingSymbol(String id){
        //This will be for nodes beneath current node
        if (this.identifier.equals(id)){
            return this;
        }

        if(this.unorderedChildren.containsKey(id)){
            return unorderedChildren.get(id);
        }

        SymbolTableNode result;
        for (SymbolTableNode n : getChildren()){
            result = n.findExisitingSymbol(id);
            if (result != null){
                return result;
            }
        }
        return null;
    }

    public STClassNode findExisitingClass(String id){
        //Should only be called by root
        SymbolTableNode result;
        //classes
        for (SymbolTableNode c : getChildren()){
            if (c.identifier.equals(id)){
                return (STClassNode)c;
            }
        }
        return null;
    }

    public String getType(){
        return type.toString();
    }

    //WOULD BE A GOOD THING FOR LOCAL VARS
    public STVariableNode isVar(String id){
        //Call to method vars
        for(SymbolTableNode n: getChildren()){
            if ((n instanceof STVariableNode) && (n.identifier.equals(id))){
                return (STVariableNode) n;
            }
        }
        //Call to class vars
        for(SymbolTableNode n: Parent.getChildren()){
            if ((n instanceof STVariableNode) && (n.identifier.equals(id))){
                return (STVariableNode) n;
            }
        }
        return null;
        //this also doesn't consider class field if they exist
        //TODO Throw and exception
    }

    public STClassNode isInstance(String id){
        for(SymbolTableNode n: getChildren()){
            if ((n instanceof STVariableNode) && (n.identifier.equals(id))){
                return (STClassNode) n;
            }
        }
        return null;
        //this also doesn't consider class field if they exist
        //TODO Throw and exception
    }
    
    public ArrayList<SymbolTableNode> getChildren(){
        ArrayList<SymbolTableNode> children = new ArrayList<>();
        for (String id: unorderedChildren.keySet()){
            children.add(unorderedChildren.get(id));
        }
        return children;
    }

    public STMethodNode getMain(){
        SymbolTableNode m = this.findExisitingSymbol("Main");
        //System.out.println(m.getClass() + "---");
        if ((m != null)&&(!(m instanceof STClassNode))) {
            return (STMethodNode)m;
        }
        System.out.println("BOOOOOO");
        m = this.getChildren().get(0).unorderedChildren.get("Main");
        return (STMethodNode)m;
    }
    
}
