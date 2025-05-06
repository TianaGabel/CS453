package myImplementation;
import visitor.*;
import java.util.*;

import myImplementation.SymbolTable.STMethodNode;
import myImplementation.SymbolTable.STWinterface;
import myImplementation.SymbolTable.SymbolTableNode;
import syntaxtree.*;

public class ToVaporVisitor<W> extends GJVoidDepthFirst<W>{
   String INDENT = "    ";
   // String name = "";
   int indentCount = 0;
   int tempCount = 0;
   Stack<String> varStack = new Stack<String>();
   Stack<String> currentStack = new Stack<String>();
   SymbolTableNode root;

   boolean hasArrays = false;

   public void programClose(){
      if (hasArrays){
         System.out.println("func AllocArray(size)\n" +
            "    bytes = MulS(size 4)\n" +
            "    bytes = Add(bytes 4)\n" +
            "    v = HeapAllocZ(bytes)\n" +
            "    [v] = size\n" +
            "    ret v)");
      }
   }

   public void setRoot(SymbolTableNode r){
      root = r;
   }

   private void printStack() {
      while (!currentStack.isEmpty()) {
         String s = currentStack.pop();
         System.out.println(s);
      }
   }

   private String indents() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < indentCount; i++) {
         sb.append(INDENT);
      }
      return sb.toString();
   }

   private String tempVar() {
      StringBuilder sb = new StringBuilder();
      sb.append("t.");
      sb.append(tempCount);
      return sb.toString();
   }

   private String newTempVar() {
      tempCount++;
      StringBuilder sb = new StringBuilder();
      sb.append("t.");
      sb.append(tempCount);
      return sb.toString();
   }
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public void visit(NodeList n,W w) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this, w);
   }

   public void visit(NodeListOptional n,W w) {
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this, w);
   }

   public void visit(NodeOptional n,W w) {
      if ( n.present() )
         n.node.accept(this, w);
   }

   public void visit(NodeSequence n,W w) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this, w);
   }

   public void visit(NodeToken n,W w) { }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public void visit(Goal n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public void visit(MainClass n,W w) {
      System.out.println("func Main ()");
      indentCount++;
      // n.f0.accept(this, w);
      // n.f1.accept(this, w);
      // n.f2.accept(this, w);
      // n.f3.accept(this, w);
      // n.f4.accept(this, w);
      // n.f5.accept(this, w);
      // n.f6.accept(this, w);
      // n.f7.accept(this, w);
      // n.f8.accept(this, w);
      // n.f9.accept(this, w);
      //Todo prevent handling of args as an array
      //n.f10.accept(this, w);
      //n.f11.accept(this, w);
      //end array
      // n.f12.accept(this, w);
      // n.f13.accept(this, w);
      // n.f14.accept(this, w);
      //n.f15.accept(this, w);
      // n.f16.accept(this, w);
      // n.f17.accept(this, w);

      ArrayList<String> s = ((STMethodNode)root.getChildren().get(0).unorderedChildren.get("Main")).mainFunctionCall;
      //TODO replace the 4 will a call to the object size
      System.out.println(INDENT + "t.0 = HeapAllocZ(4)");
      System.out.println(INDENT + "[t.0] = :vmt_" + s.get(0));
      System.out.println(INDENT + "t.1 = [t.0]");
      //TODO if it is not the first method needs to be updated
      System.out.println(INDENT + "t.1 = [t.1+0]");
      //TODO are there any instances with multiple args
      if (s.size() > 2) {System.out.println(INDENT + "t.2 = call t.1(t.0 " +  s.get(2) + ")");}
      else {System.out.println(INDENT + "t.2 = call t.1(t.0)");}
      System.out.println(INDENT + "PrintIntS(t.2)");
      System.out.println(INDENT + "ret");
      indentCount--;
      System.out.println("");
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public void visit(TypeDeclaration n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public void visit(ClassDeclaration n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public void visit(ClassExtendsDeclaration n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
      n.f7.accept(this, w);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public void visit(VarDeclaration n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public void visit(MethodDeclaration n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      
      String s = ((STMethodNode) root.findExisitingSymbol(varStack.pop())).vaporFunctionDef();
      System.out.println(s);
      tempCount = 0;
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
      indentCount++;
      n.f7.accept(this, w);
      n.f8.accept(this, w);
      n.f9.accept(this, w);
      printStack();
      System.out.println(indents() + "ret " + tempVar());
      indentCount--;
      n.f10.accept(this, w);
      n.f11.accept(this, w);
      n.f12.accept(this, w);
      System.out.println("");
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public void visit(Type n,W w) {
      //SKIP
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public void visit(ArrayType n,W w) {
      //TODO creations of an array
      //Plan on creating a function for doing this and calling
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> "boolean"
    */
   public void visit(BooleanType n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "int"
    */
   public void visit(IntegerType n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public void visit(Statement n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public void visit(Block n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public void visit(AssignmentStatement n,W w) {
      n.f0.accept(this, w);
      currentStack.push(indents() + varStack.peek() + " = " + newTempVar());
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      printStack();
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public void visit(ArrayAssignmentStatement n,W w) {
      // TODO
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public void visit(IfStatement n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public void visit(PrintStatement n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      String s = indents() + "PrintInts(" + newTempVar() + ")";
      varStack.push(tempVar());
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      printStack();
      System.out.println(s);
      n.f4.accept(this, w);
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public void visit(Expression n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public void visit(CompareExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n,W w) {
      //TODO this change needs to be updated in below
      newTempVar();
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      newTempVar();
      n.f2.accept(this, w);
      String a = varStack.pop();
      String b = varStack.pop();
      String s = indents() + varStack.peek() + " = AddS( " + a + " " + b + " )";
      printStack();
      System.out.println(s);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n,W w) {
      newTempVar();
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      newTempVar();
      n.f2.accept(this, w);
      printStack();
      System.out.println(indents() + newTempVar() + " = SubS( " + varStack.pop() + " " + varStack.pop() + " )");
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n,W w) {
      newTempVar();
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      newTempVar();
      n.f2.accept(this, w);
      printStack();
      //TODO update to a proper multiply
      System.out.println(indents() + newTempVar() + " = AddS( " + varStack.pop() + " " + varStack.pop() + " )");
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public void visit(ArrayLookup n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public void visit(ArrayLength n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public void visit(MessageSend n,W w) {
      //Address of the function table
      String t = newTempVar();
      System.out.println(indents() + t + " = [" + varStack.pop() + "]");
      //TODO Address of the function this may need to be updated via where it is at according to the symbol table
      System.out.println(indents() + newTempVar() + " = [" + t + "]");
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      printStack();
      varStack.push("");
      String s = indents() + varStack.pop() + " = call " + tempVar() + "(" + "TODO" + " ";
      s += varStack.pop();
      s += ")";
      currentStack.push(s);
      n.f5.accept(this, w);
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n,W w) {
      currentStack.push(indents() + tempVar() + " = " + newTempVar());
      String s =  varStack.pop();
      s += tempVar() + " ";
      varStack.push(s);
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public void visit(PrimaryExpression n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n,W w) {
      currentStack.push(indents() + tempVar() + " = " + n.f0.toString());
      varStack.push(tempVar());
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n,W w) {
      currentStack.push(indents() + tempVar() + " = 1");
      varStack.push(tempVar());
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n,W w) {
      currentStack.push(indents() + tempVar() + " = 0");
      varStack.push(tempVar());
      n.f0.accept(this, w);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n,W w) {
      String name = n.f0.toString();
      varStack.push(name);
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n,W w) {
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public void visit(ArrayAllocationExpression n,W w) {
      hasArrays = true;
      String s = "call :AllocArray(";
      varStack.push("");
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      printStack();
      s += varStack.pop() + ")";
      System.out.println(s);
      n.f4.accept(this, w);
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n,W w) {
      //TODO size needs to be retrieved from symbol table
      System.out.println(indents() + tempVar() + " = heapAllocZ(4)");
      System.out.println(indents() + "[" + tempVar() + "] = :vmt_" + n.f1.toString());
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public void visit(BracketExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
   }

}

