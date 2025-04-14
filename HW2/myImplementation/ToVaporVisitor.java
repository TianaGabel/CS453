package myImplementation;
import visitor.*;
import java.util.*;

import myImplementation.SymbolTable.STMethodNode;
import myImplementation.SymbolTable.SymbolTableNode;
import syntaxtree.*;

public class ToVaporVisitor<R,A> extends DepthFirstVisitor{
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
   public void visit(NodeList n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeListOptional n) {
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this);
   }

   public void visit(NodeOptional n) {
      if ( n.present() )
         n.node.accept(this);
   }

   public void visit(NodeSequence n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeToken n) { }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public void visit(Goal n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
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
   public void visit(MainClass n) {
      System.out.println("func Main ()");
      indentCount++;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      //Todo prevent handling of args as an array
      //n.f10.accept(this);
      //n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      n.f17.accept(this);
      System.out.println(INDENT + "ret");
      indentCount--;
      System.out.println("");
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public void visit(TypeDeclaration n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public void visit(ClassDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
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
   public void visit(ClassExtendsDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public void visit(VarDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
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
   public void visit(MethodDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      
      String s = ((STMethodNode) root.findExisitingSymbol(varStack.pop())).vaporFunctionDef();
      System.out.println(s);
      tempCount = 0;
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      indentCount++;
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      printStack();
      System.out.println(indents() + "ret " + tempVar());
      indentCount--;
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      System.out.println("");
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public void visit(Type n) {
      //SKIP
      n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public void visit(ArrayType n) {
      //TODO creations of an array
      //Plan on creating a function for doing this and calling
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> "boolean"
    */
   public void visit(BooleanType n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    */
   public void visit(IntegerType n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public void visit(Statement n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public void visit(Block n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public void visit(AssignmentStatement n) {
      n.f0.accept(this);
      currentStack.push(indents() + varStack.peek() + " = " + newTempVar());
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
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
   public void visit(ArrayAssignmentStatement n) {
      // TODO
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
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
   public void visit(IfStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public void visit(PrintStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      String s = indents() + "PrintInts(" + newTempVar() + ")";
      varStack.push(tempVar());
      n.f2.accept(this);
      n.f3.accept(this);
      printStack();
      System.out.println(s);
      n.f4.accept(this);
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
   public void visit(Expression n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public void visit(CompareExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n) {
      //TODO this change needs to be updated in below
      newTempVar();
      n.f0.accept(this);
      n.f1.accept(this);
      newTempVar();
      n.f2.accept(this);
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
   public void visit(MinusExpression n) {
      newTempVar();
      n.f0.accept(this);
      n.f1.accept(this);
      newTempVar();
      n.f2.accept(this);
      printStack();
      System.out.println(indents() + newTempVar() + " = SubS( " + varStack.pop() + " " + varStack.pop() + " )");
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n) {
      newTempVar();
      n.f0.accept(this);
      n.f1.accept(this);
      newTempVar();
      n.f2.accept(this);
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
   public void visit(ArrayLookup n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public void visit(ArrayLength n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public void visit(MessageSend n) {
      //Address of the function table
      String t = newTempVar();
      System.out.println(indents() + t + " = [" + varStack.pop() + "]");
      //TODO Address of the function this may need to be updated via where it is at according to the symbol table
      System.out.println(indents() + newTempVar() + " = [" + t + "]");
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      printStack();
      varStack.push("");
      String s = indents() + varStack.pop() + " = call " + tempVar() + "(" + "TODO" + " ";
      s += varStack.pop();
      s += ")";
      currentStack.push(s);
      n.f5.accept(this);
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n) {
      currentStack.push(indents() + tempVar() + " = " + newTempVar());
      String s =  varStack.pop();
      s += tempVar() + " ";
      varStack.push(s);
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n) {
      n.f0.accept(this);
      n.f1.accept(this);
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
   public void visit(PrimaryExpression n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n) {
      currentStack.push(indents() + tempVar() + " = " + n.f0.toString());
      varStack.push(tempVar());
      n.f0.accept(this);
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n) {
      currentStack.push(indents() + tempVar() + " = 1");
      varStack.push(tempVar());
      n.f0.accept(this);
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n) {
      currentStack.push(indents() + tempVar() + " = 0");
      varStack.push(tempVar());
      n.f0.accept(this);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n) {
      String name = n.f0.toString();
      varStack.push(name);
      n.f0.accept(this);
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public void visit(ArrayAllocationExpression n) {
      hasArrays = true;
      String s = "call :AllocArray(";
      varStack.push("");
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      printStack();
      s += varStack.pop() + ")";
      System.out.println(s);
      n.f4.accept(this);
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n) {
      //TODO size needs to be retrieved from symbol table
      System.out.println(indents() + tempVar() + " = heapAllocZ(4)");
      System.out.println(indents() + "[" + tempVar() + "] = :vmt_" + n.f1.toString());
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public void visit(BracketExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

}

