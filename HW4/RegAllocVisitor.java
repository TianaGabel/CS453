import cs132.vapor.ast.*; //should cover most bases



public class RegAllocVisitor<P extends VMWrapperinterface,E extends Throwable> extends VInstr.VisitorP<P,E> {
    
    public void visit(P p, VAssign a) {}
    public void visit(P p, VBranch b) {}
    public void visit(P p, VBuiltIn c) {}
    public void visit(P p, VCall c) {
        int argsCount = c.args.length;
        p.incrOut(argsCount - 1);
    }
    public void visit(P p, VGoto g) {}
    public void visit(P p, VMemRead r) {}
    public void visit(P p, VMemWrite w) {}
    public void visit(P p, VReturn r) {}

    
    
}
