import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

public class VM2M {
  public static void main(String[] args) {
    try{
      VaporProgram vp = parseVapor(System.in, System.out);
      
      VMWrapper wrapper = new VMWrapper();
      RuntimeException r = new RuntimeException();

      ToMipsVisitor<VMWrapper,RuntimeException> bv =  new ToMipsVisitor();
      bv.setWrapper(wrapper);

      //.data + functions
      System.out.println(".data\n");
      for(VDataSegment vds: vp.dataSegments){
        System.out.println(vds.ident + ":");
        for (VOperand v: vds.values){
          System.out.println( "   " + v.toString().substring(1));
        }
        System.out.println("");
      }
      // .text
      String txt = ".text\n" + //
                "\n" + //
                "  jal Main\n" + //
                "  li $v0 10\n" + //
                "  syscall\n";
      System.out.println(txt);

      for(VFunction vf : vp.functions){
        HashMap<Integer,String> labelsByLoc = new HashMap<>();
        wrapper.resetReallocCounts();
        //Stuff for labels
        for(VCodeLabel l: vf.labels){ 
          if (labelsByLoc.get(l.instrIndex) != null){ //in case 2 labels are at the same location
            String prev = labelsByLoc.get(l.instrIndex);
            labelsByLoc.put(l.instrIndex, (prev + "\n   " + l.ident + ":"));
          } else {
            labelsByLoc.put(l.instrIndex, ("" + l.ident + ":"));
        }
      }
        

        //stuff for function declaration
        //TODO fetch in,out and local counts
        wrapper.setStackVars(vf.stack.in,vf.stack.out,vf.stack.local);

        System.out.println(vf.ident + ":");
        System.out.println(wrapper.getPrologue());

        //Stuff for everything else
        int countForlabels = 0;
        if (labelsByLoc.get(countForlabels) != null){ //labels if the first thing
            System.out.println(labelsByLoc.get(countForlabels));
        }
        for(VInstr vi: vf.body){
          vi.accept(wrapper, bv);
          countForlabels++;
          if (labelsByLoc.get(countForlabels) != null){ //labels elsewhere
            System.out.println(labelsByLoc.get(countForlabels));
          }
        }
      }
      System.out.println(wrapper.endOfProgram());
    } catch(Exception e){
      e.printStackTrace();
    }

  }
  

  public static VaporProgram parseVapor(InputStream in, PrintStream err)
  throws IOException
{
  Op[] ops = {
    Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
    Op.PrintIntS, Op.HeapAllocZ, Op.Error,
  };
  boolean allowLocals = false;
  String[] registers = {
    "v0", "v1",
    "a0", "a1", "a2", "a3",
    "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
    "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
    "t8",
  };
  boolean allowStack = true;

  VaporProgram program;
  try {
    program = VaporParser.run(new InputStreamReader(in), 1, 1,
                              java.util.Arrays.asList(ops),
                              allowLocals, registers, allowStack);
  }
  catch (Exception ex) {
    System.out.println("BOO wasn't parsed");
    ex.printStackTrace();
    err.println(ex.getMessage());
    return null;
  }

  return program;
}
}