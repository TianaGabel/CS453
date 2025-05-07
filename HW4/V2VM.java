import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.management.RuntimeErrorException;

public class V2VM {
  public static void main(String[] args) {
    try{
      VaporProgram vp = parseVapor(System.in, System.out);
      //System.out.println("boo");
      //Available attributes in vapor program
      //2 bools for stack and locals NOTE we cant use locals
      //String[] registers
      //VDataSegment[] dataSegments (THE CONST DELCARATION)
      //Vfunction[] functions    (BASICALLY EVERYTHING ELSE)

      //vfunction has a body(list of instructions), index/rank for this function
      //code labels, list of params, stack declaration, list of local variables
      //NOTE: apparently runtime exceptions ... osmething
      //NOTE: the visitor doesn't support functions so I guess loop for now and we can dd it later

      
      VMWrapper wrapper = new VMWrapper();
      BasicVisitor<VMWrapper,RuntimeException> bv =  new BasicVisitor();
      RuntimeException r = new RuntimeException();
      for(VFunction vf : vp.functions){
        for(VInstr vi: vf.body){
          vi.accept(wrapper, bv);

        }
        //ALL LISTS MAY BE UNINITIALZIED
        // System.out.println(vf.ident);
        // System.out.println(vf.params);
        // System.out.println(vf.stack);
        // System.out.println(vf.vars[0]);
        // //System.out.println(vf.index);
        // System.out.println(vf.labels);   
        // System.out.println("    " +vf.body[0]);

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