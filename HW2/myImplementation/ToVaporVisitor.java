package myImplementation;
import visitor.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import myImplementation.SymbolTable.STClassNode;
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
      w.programClose();
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

      ArrayList<String> s = w.getRoot().getMain().mainFunctionCall;
      //ArrayList<SymbolTableNode> s = w.getRoot().getMain().getChildren();
      //System.out.println(s + "----");
      int size = ((STClassNode)w.getRoot().findExisitingClass(s.get(0))).getSize();
      System.out.println(INDENT + "t.0 = HeapAllocZ("+size+")");
      System.out.println(INDENT + "[t.0] = :vmt_" + s.get(0));
      System.out.println(INDENT + "t.1 = [t.0]");
      int targetMethod = ((STClassNode)w.getRoot().findExisitingClass(s.get(0))).mapMethod(s.get(1));
      System.out.println(INDENT + "t.1 = [t.1+"+ targetMethod + "]");

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
      w.setSTClass(w.getRoot().findExisitingClass(w.varStack.peek()));
      ((STClassNode)w.getSTClass()).setupStuff();
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
      w.setSTClass(w.getRoot().findExisitingClass(w.varStack.peek()));
      ((STClassNode)w.getSTClass()).setupStuff();
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
      w.addLocal(w.varStack.peek());
      //System.out.println(w.varStack.peek()+ "--");
      //System.out.println(w.locals);
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
      w.resetLocals();
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      
      STMethodNode m = (STMethodNode) w.getSTClass().findExisitingSymbol(w.varStack.pop());
      w.setSTMethod(m);
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
      w.addLocal(w.varStack.peek());
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
      w.addLocal(w.varStack.peek());
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
      if (w.islocal(i)){
         System.out.println(w.indents() + i + " = " + e);
      } else {
         int field = ((STClassNode)w.getSTClass()).mapField(i);
         System.out.println(w.indents() + "[this+" + field + "]" + " = " + e);
      }
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
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);
      n.f6.accept(this, w);
      String exToAssign = w.varStack.pop();
      String index = w.varStack.pop();
      String i = w.varStack.pop();
      //TODO check this is correct syntax
      System.out.println(w.indents() +i + "[" + index + "] = " + exToAssign);
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
      w.varStack.push(w.newControlVar("If"));
      String name = w.varStack.peek();
      //condition stuff should be outside the labels
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      System.out.println( w.indents() + "if0 " + w.varStack.pop() + " goto :"+ name +"_else");
      w.incrIndent();
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      System.out.println( w.indents()+ "goto :" + name +"_end");
      w.decrIndent();
      System.out.println(w.indents() + name +"_else:");
      w.incrIndent();
      n.f5.accept(this, w);
      n.f6.accept(this, w);
      w.decrIndent();
      System.out.println(w.indents() + name +"_end:");
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n,W w) {
      w.varStack.push(w.newControlVar("While"));
      String name = w.varStack.peek();
      System.out.println(w.indents() + name +"_test:");
      w.incrIndent();
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      System.out.println( w.indents() + "if0 " + w.varStack.pop() + " goto :"+ name +"_end");
      w.decrIndent();
      System.out.println(w.indents() + name +"_body:");
      w.incrIndent();
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      System.out.println(w.indents() + "goto :"+ name +"_test");
      w.decrIndent();
      System.out.println(w.indents() + name +"_end:");
      w.varStack.pop();
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
      System.out.println(w.indents() + "PrintIntS(" + w.varStack.pop() + ")");
   }

   /**
    * f0 -> AndExpression()   //num
    *       | CompareExpression()   //num
    *       | PlusExpression()  //num
    *       | MinusExpression() //num
    *       | TimesExpression()  //num
    *       | ArrayLookup()  //temp
    *       | ArrayLength() //temp
    *       | MessageSend() //always temp
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
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = MulS(" + a + " " + b + ")";
      System.out.println(s);
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
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = LtS(" + b + " " + a + ")";
      System.out.println(s);
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
      String s = w.indents() + w.varStack.peek() + " = Add(" + a + " " + b + ")";
      System.out.println(s);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      String a = w.varStack.pop();
      String b = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = Sub(" + b + " " + a + ")";
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
      String s = w.indents() + w.varStack.peek() + " = MulS(" + a + " " + b + ")";
      System.out.println(s);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public void visit(ArrayLookup n,W w) {
      w.varStack.push(w.newTempVar());
      n.f0.accept(this, w);
      //pop array var from stack
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      //pop inter var
      //use current stack with the other 2 in order to create array

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
      String s = "[" + w.varStack.pop() + "]";
      w.varStack.add(w.newTempVar());
      System.out.println(w.indents() + w.getTempVar() + "=" + s);
      //pop array var, sound be first access
      //set to current stack var
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
      //Ex:
      //ft = [instance]
      //ft = [ft + method]
      //t.2 = call ft(instance a)
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      n.f2.accept(this, w);
      n.f3.accept(this, w);
      n.f4.accept(this, w);
      n.f5.accept(this, w);

      String params = "";
      for (int i =0; i < w.getParamCount();i++){
         params = " " + w.varStack.pop() + params; 
      }

      String methodName = w.varStack.pop();
      String instanceName = w.varStack.pop();
      int methodLoc;
      methodLoc =((STClassNode)w.getRoot().findExisitingMethod(methodName).Parent).mapMethod(methodName);

      String instanceLoc= instanceName; //t.0
      String t1 = w.newTempVar();
      String t2 = w.newTempVar();
      w.varStack.push(t2);
      
      System.out.println(w.indents() + t1 + " = [" + instanceLoc + "]");
      System.out.println(w.indents() + t1 + " = [" + t1 +  " + " + methodLoc + "]");
      System.out.println(w.indents() + t2 + " =  call " + t1 + "(" + instanceLoc + params +")");
      w.resetParamCount();
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      w.incrParamCount();
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n,W w) {
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      w.incrParamCount();
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
      boolean isTemp = false;
      try{
         isTemp = w.varStack.peek().substring(0, 2).equals("t."); //style t.1
      } catch (Exception e){}
      boolean isValue = w.varStack.peek().matches("-?\\d+(\\.\\d+)?"); //credit to for the regex https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
      if((!isTemp)&&(!isValue)){
         //System.out.println(w.varStack.peek());
         if(!w.islocal(w.varStack.peek())){
            //It's probably a field
            int temp = ((STClassNode)w.getSTClass()).mapField(w.varStack.pop());
            System.out.println(w.indents() + w.newTempVar() + " = [this+" + temp + "]");
            w.varStack.push(w.getTempVar());
         }
      }
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
      String s = "call :AllocArray(" + w.varStack.pop() + ")";
      w.varStack.push(w.newTempVar()); //reference to newly created array
      System.out.println(w.indents() + w.getTempVar() + " = " + s);
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
      String classID = w.varStack.pop();
      w.varStack.push(w.newTempVar());

      int size = ((STClassNode)w.getRoot().findExisitingClass(classID)).getSize();
      System.out.println(w.indents() + w.getTempVar() + " = HeapAllocZ("+ size +")");
      System.out.println(w.indents() + "[" + w.getTempVar() + "] = :vmt_" + classID);
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n,W w) {
      //referenced from example code
      n.f0.accept(this, w);
      n.f1.accept(this, w);
      String temp = w.varStack.pop();
      w.varStack.add(w.newTempVar());
      String s = w.indents() + w.varStack.peek() + " = Sub(1 " + temp + ")";
      System.out.println(s);
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

