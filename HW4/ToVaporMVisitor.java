import cs132.vapor.ast.*; //should cover most bases


//This is basically a pretty printer for basic vapor code, a good reference when it comes to the actual output
public class ToVaporMVisitor<P extends VMWrapperinterface,E extends Throwable> extends VInstr.VisitorP<P,E> {
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
        String src = convertName(a.source.toString());
        String dest = convertName(a.dest.toString());
        print(dest + " = " + src ); 
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
        //print("branch"); 
        p.backupToLocals();
        if (b.positive){
            print("if " + convertName(b.value.toString()) + " goto " + b.target);  
        } else {
            print("if0 " + convertName(b.value.toString()) + " goto " + b.target);  
        }
        //p.backupToLocals();
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
        if (c.dest != null) { //everything else
            String s = " = " + c.op.name + "(" ;
            for (int i = 0; i < c.args.length; i++){
                s += convertName(c.args[i].toString()) + " ";
            }
            s = convertName(c.dest.toString()) + s;
            print(s + ")");
        }else { //case print
            String s = c.op.name + "(" ;
            for (int i = 0; i < c.args.length; i++){
                s += convertName(c.args[i].toString()) + " ";
            }
            print(s + ")");
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
        if (!c.addr.toString().contains(".")){ //function not a method
                for (int i = 0; i < Math.min(4,c.args.length); i++){ //other params
                    print("$a" + (i+1) + " = " + convertName(c.args[i].toString()));
                }
            if ((c.dest != null)) {
                print("call "+  convertName(c.addr.toString()));
                print(convertName(c.dest.toString()) + " = $v0");
            } else {
                print("call " + convertName(c.addr.toString()));
            }
            return;
        } else {
            if (c.args.length != 0){
                for (int i = 0; i < Math.min(4,c.args.length); i++){ //other params
                    print("$a" + (i) + " = " + convertName(c.args[i].toString()));
                }
                for (int i = 4; i < c.args.length; i++){ //other params
                    print("out[" + (i-4) + "] = " + convertName(c.args[i].toString()));
                }
            }
            String callVar = convertName(c.addr.toString());
            p.backupToLocals(); //backup values
            if ((c.dest != null)) {
                print("call "+  callVar);
                print(convertName(c.dest.toString()) + " = $v0");
            } else {
                print("call " + callVar);
            }
            //p.restoreLocals(); //backup values
    }
    }
               
    // abstract  void	visit(P p, VGoto g)
    // target "The target of the jump."
    // VAddr<VCodeLabel>	target
    public void visit(P p, VGoto g) {
        p.backupToLocals();
        print("goto " + g.target); 
    }
    
               
    // abstract  void	visit(P p, VMemRead r)
    // Ex: "v = [a+4]"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemRead r) {
        //print("--memread--");
        String dest = convertName(r.dest.toString());
        String src = convertName(r.source);
        print(dest + " = [" + src + "]"); 
    }
               
    // abstract  void   visit(P p, VMemWrite w)
    // Ex: "[a+4] = v"
    // "dest = source"
    // Dest
    // Source
    // sourcepPos
    public void visit(P p, VMemWrite w) {
        //print("--memwrite--");
        
        String dest = convertName(w.dest);
        String src = convertName(w.source.toString());
        print("[" + dest + "] = " + src); 

    }
               
    // abstract  void	visit(P p, VReturn r)
    // value
    public void visit(P p, VReturn r) {
        if (r.value != null){
            print("$v0 = " + convertName(r.value.toString()));
            print("ret\n"); 
        } else {
            print("ret\n"); 
        }
    }





    ///MISC HELPER METHODS
    /// 
    private VMWrapper wrapper;
    public void setWrapper(VMWrapper w){
        //for easier access
        wrapper = w;
    }

    public void addParams(VVarRef[] refs){
        String s;
        if (wrapper.isNotMethod()){
            for (int i = 0; i < Math.min(4,refs.length); i++){ //other params
                s = convertName(refs[i].toString()) +" = $a" + (i+1);
                print(s);
            }
            for (int i = 4; i < refs.length; i++){
                s = convertName(refs[i].toString()) +" = in[" + (i-4) + "]";
                print(s);
            }
        } else {
            for (int i = 1; i < Math.min(4,refs.length); i++){ //other params
                    s = convertName(refs[i].toString()) +" = $a" + i;
                    print(s);
            }
            for (int i = 4; i < refs.length; i++){
                    s = convertName(refs[i].toString()) +" = in[" + (i-4) + "]";
                    print(s);
            }
        }

    }

    private String convertName(String id){
        if (id.equals("this")){
            return "$a0";
        }
        boolean isValue = id.matches("-?\\d+(\\.\\d+)?");
        boolean isft =  id.substring(0, 1).equals(":");
        boolean isError = id.substring(0, 1).equals("\"");
        if (isValue || isft || isError){
            return id;
        }
        return wrapper.getVarAsRegister(id);
    }

    private String convertName(VMemRef ref){
        if (ref instanceof VMemRef.Global){
            if (((VMemRef.Global)ref).byteOffset == 0){
                return "" + convertName(((VMemRef.Global)ref).base.toString());
            } else {
                return "" + convertName(((VMemRef.Global)ref).base.toString()) + "+" + ((VMemRef.Global)ref).byteOffset;
            }
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
        //handle the this outside
        // for (int i = 0; i < c.args.length; i++){
        //     s += "" + c.args[i];
        //     break;
        // }
        for (int i = 1; i < c.args.length; i++){
            s += " " + c.args[i];
        }
        return s;
    }
    
    private void print(String line){
        String INDENT = "   ";
        System.out.println(INDENT + line);
    }
}
