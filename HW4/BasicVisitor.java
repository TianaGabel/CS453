/*

Gonna use insrvisitor.p
 * abstract  void	visit(P p, VAssign a)
           
abstract  void	visit(P p, VBranch b)
           
abstract  void	visit(P p, VBuiltIn c)
           
abstract  void	visit(P p, VCall c)
           
abstract  void	visit(P p, VGoto g)
           
abstract  void	visit(P p, VMemRead r)
           
abstract  void	visit(P p, VMemWrite w)
           
abstract  void	visit(P p, VReturn r)
           
 */
import cs132.vapor.ast.*; //should cover most bases
public class BasicVisitor extends VisitorP<P,E> {
    //ALL HAVE sourcePos

    // * abstract  void	visit(P p, VAssign a)
//     Dest
// Source
// sourcepPos
    @Override
    public void visit(Void p, VAssign a) {
        
    }
           
    // abstract  void	visit(P p, VBranch b)
    //positive
    // boolean
    // For if branches, positive is true.
    //
    // target
    // VLabelRef<VCodeLabel>
    //          The code label that will be executed next if the branch is taken.
    // value
    // VOperand
    //          The value being branched on.
    @Override
    public void visit(Void p, VBranch b) {}
               
    //isError() only
    // abstract  void	visit(P p, VBuiltIn c)
    @Override
    public void visit(Void p, VBuiltIn c) {}
               
    // abstract  void	visit(P p, VCall c)
    //     - VClass
    // addr
    // VAddr<VFunction>
    //       The address of the function being called.
    //  arg
    //  argsVOperand[]
    //           The list of arguments to pass to the function.
    //dest
    //  VVarRef.Local
    //           The variable used to store the return value of the function.
    @Override
    public void visit(Void p, VCall c) {}
               
    // abstract  void	visit(P p, VGoto g)
    //target
    //VAddr<VCodeLabel>	target
    //The target of the jump.
    @Override
    public void visit(Void p, VGoto g) {}
    
               
    // abstract  void	visit(P p, VMemRead r)
    //     Dest
    // Source
    // sourcepPos
    @Override
    public void visit(Void p, VMemRead r) {}
               
    // abstract  void	visit(P p, VMemWrite w)
    //     Dest
    // Source
    // sourcepPos
    @Override
    public void visit(Void p, VMemWrite w) {}
               
    // abstract  void	visit(P p, VReturn r)
    //value
    @Override
    public void visit(Void p, VReturn r) {}
    
}
