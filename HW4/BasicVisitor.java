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
        System.out.println("branch" + b);   
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
        System.out.println("builtin" + c);   
    }
               
    // abstract  void	visit(P p, VCall c)
    // "Function call instruction."
    //  addr "The address of the function being called"
    //  VAddr<VFunction>
    //       
    //  arg "The list of arguments to pass to the function."
    //  argsVOperand[]
    //           
    // dest "The variable used to store the return value of the function."
    //  VVarRef.Local        
    public void visit(P p, VCall c) {
        System.out.println("call" + c); 
    }
               
    // abstract  void	visit(P p, VGoto g)
    // target "The target of the jump."
    // VAddr<VCodeLabel>	target
    public void visit(P p, VGoto g) {
        System.out.println("goto" + g); 
    }
    
               
    // abstract  void	visit(P p, VMemRead r)
    // Ex: "v = [a+4]"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemRead r) {
        System.out.println("memread" + r); 
    }
               
    // abstract  void   visit(P p, VMemWrite w)
    // Ex: "[a+4] = v"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemWrite w) {
        System.out.println("memwrite " + w); 
    }
               
    // abstract  void	visit(P p, VReturn r)
    // value
    public void visit(P p, VReturn r) {
        System.out.println("ret" + r); 
    }
    
}
