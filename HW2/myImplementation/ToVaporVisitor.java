package myImplementation;
import visitor.*;
import java.util.*;

import myImplementation.SymbolTable.STMethodNode;
import myImplementation.SymbolTable.STVaporArgInterface;
import myImplementation.SymbolTable.STWinterface;
import myImplementation.SymbolTable.SymbolTableNode;
import syntaxtree.*;

public class ToVaporVisitor<W extends STVaporArgInterface> extends GJVoidDepthFirst<W>{
   String INDENT = "    ";
   boolean hasArrays = false;

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
      w.incrIndent();

      ArrayList<String> s = (w.getRoot().getMain()).mainFunctionCall;
      //TODO replace the 4 with a call to the object size (or not if we do just assume its the first method)
      System.out.println(INDENT + "t.0 = HeapAllocZ(4)");
      System.out.println(INDENT + "[t.0] = :vmt_" + s.get(0));
      System.out.println(INDENT + "t.1 = [t.0]");
      //TODO if it is not the first method needs to be updated
      System.out.println(INDENT + "t.1 = [t.1+0]");

      System.out.print(INDENT + "t.2 = call t.1(t.0");
      for(int i=2; i < s.size(); i++){
         System.out.print(" " + s.get(i));
      }
      System.out.println(")");
      
      System.out.println(INDENT + "PrintIntS(t.2)");
      System.out.println(INDENT + "ret");
      w.decrIndent();
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
      
      STMethodNode m = (STMethodNode) w.getRoot().findExisitingSymbol(w.varStack.pop());
      String s = m.vaporFunctionDef();
      System.out.println(s);
      w.resetTemp();
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
      w.incrIndent();
      n.f7.accept(this, w);
      n.f8.accept(this, w);
      n.f9.accept(this, w);
      n.f10.accept(this, w);
      //w.printStatement(); //ensure nothing remains
      System.out.println(INDENT + "ret " + w.varStack.pop());
      w.decrIndent();
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
      w.initStatement();
      n.f0.accept(this, w);
      //TODO we will operate in blocks as statement should not vary in vars significantly
      w.printStatement();
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
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      String e = w.varStack.pop();
      String i = w.varStack.pop();
      System.out.println(w.indents() + i + " = " + e);
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
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      System.out.println(w.indents() + "PrintInts(" + w.varStack.pop() + ")");
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
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = AddS( " + a + " " + b + " )";
      System.out.println(s);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n,W w) {
      //TODO check syntax
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = SubS( " + a + " " + b + " )";
      System.out.println(s);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = AddS( " + a + " " + b + " )";
      System.out.println(s);
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
      w.varStack.push("MESSAGE SEND");
      //Address of the function table
      // String t = newTempVar();
      // System.out.println(indents() + t + " = [" + varStack.pop() + "]");
      // //TODO Address of the function this may need to be updated via where it is at according to the symbol table
      // System.out.println(indents() + newTempVar() + " = [" + t + "]");
      // n.f0.accept(this, w);
      // n.f1.accept(this, w);
      // n.f2.accept(this, w);
      // n.f3.accept(this, w);
      // n.f4.accept(this, w);
      // printStack();
      // varStack.push("");
      // String s = indents() + varStack.pop() + " = call " + tempVar() + "(" + "TODO" + " ";
      // s += varStack.pop();
      // s += ")";
      // currentStack.push(s);
      // n.f5.accept(this, w);
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n,W w) {
      // currentStack.push(indents() + tempVar() + " = " + newTempVar());
      // String s =  varStack.pop();
      // s += tempVar() + " ";
      // varStack.push(s);
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
      n.f0.accept(this, w);
      w.varStack.add(n.f0.toString());
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n,W w) {
      //Todo
      n.f0.accept(this, w);
      n.f0.accept(this, w);
      w.varStack.add("1");
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n,W w) {
      n.f0.accept(this, w);
      w.varStack.add("0");
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n,W w) {
      String name = n.f0.toString();
      w.varStack.push(name);
      n.f0.accept(this, w);
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n,W w) {
      w.varStack.push("this");
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
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      //TODO fix this to allocate correctly
      String s = "call :AllocArray(" + w.varStack.pop();
      System.out.println(s);
      //TODO push new ref to array to the stack.
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      w.newTempVar();
      w.varStack.push(w.getTempVar());
      //TODO size needs to be retrieved from symbol table
      System.out.println(w.indents() + w.getTempVar() + " = heapAllocZ(4)");
      System.out.println(w.indents() + "[" + w.getTempVar() + "] = :vmt_" + n.f1.toString());
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n,W w) {
      //Todo unsure how this would work
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      w.varStack.pop();
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

