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

public class V2VM {
  public static void main(String[] args) {
    try{
      VaporProgram vp = parseVapor(System.in, System.out);
      //System.out.println("boo");
      //Available attributes in vapor program
      //2 bools for stack and locals NOTE we cant use locals (like variable names)
      //String[] registers
      //VDataSegment[] dataSegments (THE CONST DELCARATION)
      //Vfunction[] functions    (BASICALLY EVERYTHING ELSE)

      //vfunction has a body(list of instructions), index/rank for this function
      //code labels, list of params, stack declaration, list of local variables
      //NOTE: apparently runtime exceptions ... osmething
      //NOTE: the visitor doesn't support functions so I guess loop for now and we can dd it later
      
      VMWrapper wrapper = new VMWrapper();
      RuntimeException r = new RuntimeException();

      RegAllocVisitor<VMWrapper,RuntimeException> regalloc =  new RegAllocVisitor();
      //BasicVisitor<VMWrapper,RuntimeException> bv =  new BasicVisitor();
      ToVaporMVisitor<VMWrapper,RuntimeException> bv =  new ToVaporMVisitor();
      bv.setWrapper(wrapper);

      //       //Field Summary
      //  int	index
      //           All functions in a single program are assigned an index contiguously, starting from zero.
      //  boolean	mutable
      //           Whether the data segment is mutable.
      //  VOperand.Static[]	values
      //           The data in the segment.
      for(VDataSegment vds: vp.dataSegments){
        System.out.println("const " + vds.ident);
        for (VOperand v: vds.values){
          System.out.println( "   " +v);
        }
      }

      for(VFunction vf : vp.functions){
        HashMap<Integer,String> labelsByLoc = new HashMap<>();
        //Stuff for labels
        for(VCodeLabel l: vf.labels){ //no idea what the l.function is for, maybe just the name?
          //System.out.println(l.instrIndex + " " + l.ident);
          if (labelsByLoc.get(l.instrIndex) != null){ //in case 2 labels are at the same location
            String prev = labelsByLoc.get(l.instrIndex);
            labelsByLoc.put(l.instrIndex, (prev + "\n   " + l.ident + ":"));
          } else {
            labelsByLoc.put(l.instrIndex, ("   " + l.ident + ":"));
          }
        }
        

        //stuff for function declaration
        for(VInstr vi: vf.body){
          vi.accept(wrapper, regalloc);
        }
        //System.out.println("----" + vf.vars.length);
        wrapper.incrIn(vf.params.length-1);  //in stuff

        //wrapper.incrLocal(2);
        wrapper.incrLocal(vf.vars.length+1);  //min for backup local
        wrapper.incrLocal((vf.vars.length*2)-12);  //if more are needed
        wrapper.incrLocal(80);

        wrapper.addFunction(vf.ident); //out stuff and local
        System.out.println(wrapper.getFunctionDeclartion(vf.ident));
        wrapper.resetReallocCounts();

        //Stuff for everything else
        int countForlabels = 0;
        bv.addParams(vf.params);
        if (vf.ident.equals("AllocArray")){   //missing a this param, so it is mishandled by the visitor
          wrapper.addArray();
          return;
        }
        if (labelsByLoc.get(countForlabels) != null){
            System.out.println(labelsByLoc.get(countForlabels));
        }
        for(VInstr vi: vf.body){
          vi.accept(wrapper, bv);
          countForlabels++;
          if (labelsByLoc.get(countForlabels) != null){
            System.out.println(labelsByLoc.get(countForlabels));
          }
          //System.out.println("-");
        }
        //wrapper.printMap();
        wrapper.resetRegisters();
        wrapper.resetSRegisters();
        wrapper.resetLocals();
      }


    } catch(Exception e){
      System.out.println("BOO main failure");
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
  boolean allowLocals = true;
  String[] registers = null;
  boolean allowStack = false;

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