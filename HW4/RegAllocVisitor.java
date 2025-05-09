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
        //no considerations for registers
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
        //probably also not important for the first pass
        //honestly doens't end up getting pciked up in any of the example even when a register is used
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
        //set out variables
        //backup local registers
        int argsCount = c.args.length;
        //restore local registers
    }
               
    // abstract  void	visit(P p, VGoto g)
    // target "The target of the jump."
    // VAddr<VCodeLabel>	target
    public void visit(P p, VGoto g) {
    }
    
               
    // abstract  void	visit(P p, VMemRead r)
    // Ex: "v = [a+4]"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemRead r) {
        //we may be making more of these I guess

     }
               
    // abstract  void   visit(P p, VMemWrite w)
    // Ex: "[a+4] = v"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemWrite w) {
        //TODO maybe something idk honestly
    }
               
    // abstract  void	visit(P p, VReturn r)
    // value
    public void visit(P p, VReturn r) {
        //in future translations throw the out arrays here for non-main functions
        //also handling for v0
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
