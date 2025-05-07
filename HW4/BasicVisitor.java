import cs132.vapor.ast.*; //should cover most bases



public class BasicVisitor<P extends VMWrapperinterface,E extends Throwable> extends VInstr.VisitorP<P,E> {
    //ALL HAVE sourcePos

    // * abstract  void	visit(P p, VAssign a)
    // Dest "where it is being stored"
    // Source "that value"
    // sourcepPos "parent I think"
    // "An assignment instruction."
    // This is only used for assignments of simple operands 
    // to registers and local variables."
    // -> VmemRead
    // -> VmemWrite
    // -> Vbuiltin
    // -> Vcall
    public void visit(P p, VAssign a) {
     System.out.println("assign" + a);   
    }
           
    // abstract  void	visit(P p, VBranch b)
    //"A branch instruction (if and if0)."
    // positive
    // boolean 
    // For if branches, positive is true.
    //
    // target
    // VLabelRef<VCodeLabel>
    // The code label that will be executed next if the branch is taken.
    // 
    //value
    // VOperand
    // The value being branched on.

    public void visit(P p, VBranch b) {
        //System.out.println("branch"); 
        if (b.positive){
            System.out.println("if " + b.value + " goto " + b.target);  
        } else {
            System.out.println("if0 " + b.value + " goto " + b.target);  
        }
    }
            
    // abstract  void	visit(P p, VBuiltIn c)
    //"invocation of a built in op"
    // args " The arguments to the operation"
    // VOperand[]
    //
    // dest "The variable/register in which to store the result of the operation."
    // VVarRef
    //
    // op " The operation being performed."
    // VBuiltIn.Op
    //      name "name of an op"
    //      numParams "either a num or -1 for variable"
    //      lookup(String name) "no description given"
     
    public void visit(P p, VBuiltIn c) {
        String s = VBuiltInArgsToString(c);
        if ((c.dest != null)) {
            if (s.isEmpty()){
                System.out.println(c.dest + " = " + c.op.name + "()");
            } else {
                System.out.println(c.dest + " = " + c.op.name + "(" + s + ")");
            }
        } else {
            if (s.isEmpty()){
                //TODO
                System.out.println(c.op.name + "()");
            } else {
                System.out.println(c.op.name + "(" + s + ")");
            }
        }
    }
               
    // abstract  void	visit(P p, VCall c)
    // "Function call instruction."
    //  addr "The address of the function being called"
    //  VAddr<VFunction>
    //       
    //  args "The list of arguments to pass to the function."
    //  argsVOperand[]
    //           
    // dest "The variable used to store the return value of the function."
    //  VVarRef.Local        
    public void visit(P p, VCall c) {
        String s = VBuiltInArgsToString(c);
        if ((c.dest != null)) {
            if (s.isEmpty()){
                System.out.println(c.dest + " = call "+   c.addr + "()");
            } else {
                System.out.println(c.dest + " = call " + c.addr + "(" + s + ")");
            }
        } else {
            if (s.isEmpty()){
                //TODO
                System.out.println("call "+ c.addr + "()");
            } else {
                System.out.println("call " + c.addr + "(" + s + ")");
            }
        }
    }
               
    // abstract  void	visit(P p, VGoto g)
    // target "The target of the jump."
    // VAddr<VCodeLabel>	target
    public void visit(P p, VGoto g) {
        System.out.println("goto" + g.target); 
    }
    
               
    // abstract  void	visit(P p, VMemRead r)
    // Ex: "v = [a+4]"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemRead r) {
        //System.out.println("--memread--");
        System.out.println(r.dest + " = [" + VMemRefToString(r.source) + "]"); 
    }
               
    // abstract  void   visit(P p, VMemWrite w)
    // Ex: "[a+4] = v"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemWrite w) {
        //System.out.println("--memwrite--");
        System.out.println("[" + VMemRefToString(w.dest) + "] = " + w.source); 

    }
               
    // abstract  void	visit(P p, VReturn r)
    // value
    public void visit(P p, VReturn r) {
        if (r.value != null){
            System.out.println("ret " + r.value); 
        } else {
            System.out.println("ret"); 
        }
    }





    ///MISC HELPER METHODS
    private String VMemRefToString(VMemRef ref){
        if (ref instanceof VMemRef.Global){
            return "" + ((VMemRef.Global)ref).base + "+" + ((VMemRef.Global)ref).byteOffset;
        } else {
            return "BOOOO";
        }

    }

    private String VBuiltInArgsToString(VBuiltIn c){
        String s = "";
        for (int i = 0; i < c.args.length; i++){
            s += "" + c.args[i];
            break;
        }
        for (int i = 1; i < c.args.length; i++){
            s += " " + c.args[i];
        }
        return s;
    }

    private String VBuiltInArgsToString(VCall c){
        String s = "";
        for (int i = 0; i < c.args.length; i++){
            s += "" + c.args[i];
            break;
        }
        for (int i = 1; i < c.args.length; i++){
            s += " " + c.args[i];
        }
        return s;
    }
    
}
