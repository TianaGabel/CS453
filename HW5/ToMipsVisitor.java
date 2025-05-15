import cs132.vapor.ast.*; //should cover most bases


//This is basically a pretty printer for basic vapor code, a good reference when it comes to the actual output
public class ToMipsVisitor<P extends VMWrapperinterface,E extends Throwable> extends VInstr.VisitorP<P,E> {
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
        String src =  (a.source.toString());
        String dest =  (a.dest.toString());
        print(loadVar(dest, src)); 
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
        print(loadVar("$t9", b.value.toString()));
        if (b.positive){
            print("bnez $t9 " + b.target);  
        } else {
            print("beqz $t9 " + b.target);  
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
        if (c.op.name.equals("HeapAllocZ") || c.op.name.equals("PrintIntS") || c.op.name.equals("Error")){
            for (int i = 0; i < c.args.length; i++){
                    print(loadVar("$a" + i, c.args[i].toString()));
            }
            switch(c.op.name) {
            case "PrintIntS":
                print("jal _print");
                break;
            case "HeapAllocZ":
                print("jal _heapAlloc");
                break;
            case "Error":
                print("j _error");
                break;
            }
            if (c.dest != null){
                print("move " + c.dest +" $v0");
            }
        } else {
        switch(c.op.name) {
        case "Sub":
            print("subu " + c.dest + " " + c.args[0] + " " + c.args[1]);
            break;
        case "Add":
            print("addu " + c.dest + " " + c.args[0] + " " + c.args[1]);
            break;
        case "MulS":
            print("mul " + c.dest + " " + c.args[0] + " " + c.args[1]);
            break;
        case "Eq":
            //unsure if this needs implemented
            break;
        case "LtS":
            print("slt " + c.dest + " " + c.args[0] + " " + c.args[1]);
            break;
        case "Lt":
            print("slt " + c.dest + " " + c.args[0] + " " + c.args[1]);
            break;
        default:
            // code block
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
        print("jal " + (c.addr.toString()));
    }
               
    // abstract  void	visit(P p, VGoto g)
    // target "The target of the jump."
    // VAddr<VCodeLabel>	target
    public void visit(P p, VGoto g) {
        print("j " + g.target); 
    }
    
               
    // abstract  void	visit(P p, VMemRead r)
    // Ex: "v = [a+4]"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemRead r) {
        //print("--memread--");
        String dest =  (r.dest.toString());
        print("lw " + dest + " " + convertName(r.source)); 
    }
               
    // abstract  void   visit(P p, VMemWrite w)
    // Ex: "[a+4] = v"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemWrite w) {
        //print("--memwrite--");
        String src = (w.source.toString());
        print(loadVar("$t9", src));
        print("sw $t9 "+ convertName(w.dest));  
    }

    public void visit(P p, VReturn r) {
        System.out.println(p.getEpilogue());
        print("jr $ra\n"); 
    }

    ///MISC HELPER METHODS
    /// 
    private String loadVar(String dest, String src){
        //integer
        boolean isValue = src.matches("-?\\d+(\\.\\d+)?");
        boolean isRegister = src.substring(0,1).equals("$");
        boolean isError = src.substring(0,1).equals("\"");
        if (isValue){
            return "li " + dest + " " + src;
        } else if (isRegister){
            return "move " + dest + " " + src;
        }else if (isError){
            return "la " + dest + " _str0";
        } else {
            return "la " + dest + " " + src;
        }
    }

    private String convertName(VMemRef ref){
        if (ref instanceof VMemRef.Global){
            int offset = ((VMemRef.Global)ref).byteOffset;
            return offset + "(" + ((VMemRef.Global)ref).base.toString() + ")";
        } else {
            VMemRef.Stack.Region region = ((VMemRef.Stack)ref).region;
            int index = ((VMemRef.Stack)ref).index;
            if (region == VMemRef.Stack.Region.valueOf("In")){
                return (index*4) + "($fp)"; //caller out is located above the frame
            } else if (region == VMemRef.Stack.Region.valueOf("Out")){
                return (index*4) + "($sp)";
            } else { //local
                return -(index*4 + 12) + "($fp)";
            }
        }
    }

    private VMWrapper wrapper;
    public void setWrapper(VMWrapper w){
        //for easier access
        wrapper = w;
    }
    
    private void print(String line){
        String INDENT = "   ";
        System.out.println(INDENT + line);
    }
}
