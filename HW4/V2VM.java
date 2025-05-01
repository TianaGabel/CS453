import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;


import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


public class V2VM {
  public static void main(String[] args) {
    try{
      VaporProgram vp = parseVapor(System.in, System.out);
      //System.out.println("boo");
      //Available attributes in vapor program
      //2 bools for stack and locals
      //String[] registers
      //VDataSegment[] dataSegments
      //Vfunction[] functions

      //vfunction has a body(list of instructions), index/rank for this function
      //code labels, list of params, stack declaration, list of local variables

      //index/rank, ismutable, values(data in segment)

    } catch(Exception e){}

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
  catch (ProblemException ex) {
    err.println(ex.getMessage());
    return null;
  }

  return program;
}
}