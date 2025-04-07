package myImplementation;

import visitor.*;
import java.util.*;
import syntaxtree.*;

public class SymTableVisitor<R,A> extends DepthFirstVisitor{
    
   SymbolTableNode currentNode;
   SymbolTableNode root;
   String currentID;

   public void createRoot(){
      root = new SymbolTableNode("Goal", "Program");
      currentNode = root;
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
      n.f0.accept(this);
      n.f1.accept(this);
      currentNode = currentNode.addNode(currentID, "Main");
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      currentNode.addNode(currentID, "Array");
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      n.f17.accept(this);
      currentNode = root;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public void visit(TypeDeclaration n) {
      System.out.println();
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
      currentNode = currentNode.addNode(currentID, "Class");
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      currentNode = root;
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
      currentNode = currentNode.addNode(currentID, "Class");
      n.f2.accept(this);
      n.f3.accept(this);
      currentNode = currentNode.addExtendsAttribute(currentID);
      //TODO add extending attribute
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      currentNode = root;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public void visit(VarDeclaration n) {
      System.out.println();
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
      pPrintNodeClose(n);
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
      System.out.println();
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      n.f5.accept(this);
      pPrintSymbol(n.f5);
      n.f6.accept(this);
      pPrintSymbol(n.f6);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      pPrintSymbol(n.f9);
      n.f10.accept(this);
      n.f11.accept(this);
      pPrintSymbol(n.f11);
      n.f12.accept(this);
      pPrintSymbol(n.f12);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n) {
    pPrintNodeType(n);
      pPrintNodeTypeAndName(n, n.f0);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public void visit(ArrayType n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
   }

   /**
    * f0 -> "boolean"
    */
   public void visit(BooleanType n) {
      pPrintNodeType(n);
      n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    */
   public void visit(IntegerType n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public void visit(Block n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public void visit(AssignmentStatement n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      pPrintNodeClose(n);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      pPrintSymbol(n.f4);
      n.f5.accept(this);
      n.f6.accept(this);
      pPrintSymbol(n.f6);
      pPrintNodeClose(n);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      n.f5.accept(this);
      pPrintSymbol(n.f5);
      n.f6.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public void visit(PrintStatement n) {
      pPrintNodeType(n);n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      pPrintSymbol(n.f4);
      pPrintNodeClose(n);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public void visit(CompareExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public void visit(ArrayLookup n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public void visit(ArrayLength n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintNodeClose(n);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      n.f4.accept(this);
      n.f5.accept(this);
      pPrintSymbol(n.f5);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n) {
      pPrintNodeTypeAndName(n, n.f0);
      n.f0.accept(this);
      n.f1.accept(this);
      pPrintNodeClose(n);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n) {
      pPrintNodeTypeAndName(n, n.f0);
      n.f0.accept(this);
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n) {
      pPrintNodeTypeAndName(n, n.f0);
      n.f0.accept(this);
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n) {
      pPrintNodeTypeAndName(n, n.f0);
      n.f0.accept(this);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n) {
      n.f0.accept(this);
      currentID = n.f0;
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n) {
      pPrintNodeTypeAndName(n, n.f0);
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
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintSymbol(n.f1);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
      n.f3.accept(this);
      n.f4.accept(this);
      pPrintSymbol(n.f4);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
      n.f3.accept(this);
      pPrintSymbol(n.f3);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      pPrintNodeClose(n);
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public void visit(BracketExpression n) {
      pPrintNodeType(n);
      n.f0.accept(this);
      pPrintSymbol(n.f0);
      n.f1.accept(this);
      n.f2.accept(this);
      pPrintSymbol(n.f2);
      pPrintNodeClose(n);
   }
    
}
