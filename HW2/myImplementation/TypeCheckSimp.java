package myImplementation;
import visitor.*;
import java.util.*;
import syntaxtree.*;
import myImplementation.SymbolTable.*;

public class TypeCheckSimp<VarType,VarType> extends GJDepthFirst<VarType,VarType>{
    

   SymbolTableNode root;
   SymbolTableNode currentNode;
   String currentID;

   public TypeCheckSimp(SymbolTableNode r){
      root = r;
   }

   //HELPER FUNCTION
   //are all childern's ids unique
   public void checkDistinct(SymbolTableNode parent){
      for(SymbolTableNode child: parent.getChildren()){
         for(SymbolTableNode other: parent.getChildren()){
            if(child.identifier.equals(other.identifier)){
               //TODO throw error
            }
         }
      }
   }

   //TODO unnecessary
   // public VarType checkDistinct(ArrayList<STVariableNode> params){
   //    for(SymbolTableNode child: params){
   //       for(SymbolTableNode other: params){
   //          if(child.identifier.equals(other.identifier)){
   //             //TODO throw error
   //          }
   //       }
   //    }
   // }
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public VarType visit(NodeList n, VarType v) {
      VarType r =null;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this,v);
      
      return r;}

   public VarType visit(NodeListOptional n, VarType v) {
      VarType r =null;
      if ( n.present() ){
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this,v);
      }
      return r;
   }

   public VarType visit(NodeOptional n, VarType v) {
      VarType r =null;
      if ( n.present() )
         n.node.accept(this,v);
      return r;
   }

   public VarType visit(NodeSequence n, VarType v) {
      VarType r =null;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this,v);
      return r;
   }

   public VarType visit(NodeToken n, VarType v) {return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public VarType visit(Goal n, VarType v) {
      VarType r =null;
      //7.2 TYPECHECK
      // are the class names unique? including main
      
      checkDistinct(root);
      checkDistinct(root.findExisitingSymbol("Main"));
      for (SymbolTableNode classNode: root.getChildren()){
         checkDistinct(classNode);
         for (SymbolTableNode methodNode: classNode.getChildren()){
            if (methodNode instanceof STMethodNode){
               checkDistinct(methodNode); //children includes params
            }
         }
      }


      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
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
   public VarType visit(MainClass n, VarType v) {
      VarType r =null;
      //7.3 TYPECHECK
      // are ids unique? (above)
      // do children typecheck?
      // idk maybe more

      n.f0.accept(this,v);
      n.f1.accept(this,v);
      currentNode = root.findExisitingSymbol(currentID);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      n.f7.accept(this,v);
      n.f8.accept(this,v);
      n.f9.accept(this,v);
      n.f10.accept(this,v);
      n.f11.accept(this,v);
      n.f12.accept(this,v);
      n.f13.accept(this,v);
      n.f14.accept(this,v);
      n.f15.accept(this,v);
      n.f16.accept(this,v);
      n.f17.accept(this,v);
      currentNode = root;
      return r;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public VarType visit(TypeDeclaration n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public VarType visit(ClassDeclaration n, VarType v) {
      VarType r =null;
      //7.4 TYPECHECK
      // are ids unique? (above)
      //are method names unique? (also above)
      // do children typecheck?
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      currentNode = root.findExisitingSymbol(currentID);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      currentNode = root;
      return r;
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
   public VarType visit(ClassExtendsDeclaration n, VarType v) {
      VarType r =null;
      //7.4 TYPECHECK
      // are ids unique? (above)
      //are method names unique? (above)
      // do children typecheck?
      // reference the no overloading rule (handled in symbol table)
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      currentNode = root.findExisitingSymbol(currentID);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      n.f7.accept(this,v);
      currentNode = root;
      return r;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public VarType visit(VarDeclaration n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
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
   public VarType visit(MethodDeclaration n, VarType v) {
      VarType r =null;
      //7.5 TYPECHECK
      // are params unique? (above)
      // are ids unique? (above)
      // both of the above?
      // do children typecheck(This and above includes statements)?
      // return matches type? TODO
      // worth looking at doc again

      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      currentNode = currentNode.findExisitingSymbol(currentID);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      n.f7.accept(this,v);
      n.f8.accept(this,v);
      n.f9.accept(this,v);
      n.f10.accept(this,v);
      n.f11.accept(this,v);
      n.f12.accept(this,v);
      currentNode = currentNode.Parent;
      return r;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public VarType visit(FormalParameterList n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public VarType visit(FormalParameter n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public VarType visit(FormalParameterRest n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public VarType visit(Type n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public VarType visit(ArrayType n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> "boolean"
    */
   public VarType visit(BooleanType n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "int"
    */
   public VarType visit(IntegerType n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public VarType visit(Statement n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public VarType visit(Block n, VarType v) {
      VarType r =null;
      // 7.6 22 TYPECHECK
      // do children typecheck?
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public VarType visit(AssignmentStatement n, VarType v) {
      VarType r =null;
      // 7.6 23 TYPECHECK
      // is id a variable? (done)
      // is expression the same type as id?
      // TODO ?? unsure what the last line means
      
      n.f0.accept(this,v);
      currentNode.isVar(currentID);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      return r;
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
   public VarType visit(ArrayAssignmentStatement n, VarType v) {
      VarType r =null;
      // 7.6 TYPECHECK
      // is id an array? (done)
      // is expression in [] an int?
      // is assignment expression an int

      n.f0.accept(this,v);
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      return r;
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
   public VarType visit(IfStatement n, VarType v) {
      VarType r =null;
      // 7.6 TYPECHECK
      // is f2 a bool
      // do the children typecheck?
      // TODO accounting for uninitialized variables
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      return r;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public VarType visit(WhileStatement n, VarType v) {
      VarType r =null;
      // 7.6 TYPECHECK
      // is f2 a bool
      // do the children typecheck?
      // TODO accounting for uninitialized variables if skipped
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      return r;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public VarType visit(PrintStatement n, VarType v) {
      VarType r =null;
      // 7.6 TYPECHECK
      // is f2 an int?
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      return r;
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
   public VarType visit(Expression n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public VarType visit(AndExpression n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // both type bool
      // propagate bool

      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public VarType visit(CompareExpression n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // both type int
      // propagate bool
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public VarType visit(PlusExpression n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public VarType visit(MinusExpression n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public VarType visit(TimesExpression n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public VarType visit(ArrayLookup n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // f0 array (done)
      // f2 int
      // propagate int
      n.f0.accept(this,v);
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public VarType visit(ArrayLength n, VarType v) {
      VarType r =null;
      // 7.7 TYPECHECK
      // f0 array (done)
      // propagate int
      n.f0.accept(this,v);
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public VarType visit(MessageSend n, VarType v) {
      VarType r =null;
      // 7.7 35 TYPECHECK
      // f0 is an instance (done)
      // f2 is a method (done)
      // they go together
      // the arguements match params TODO
      // MAYBE A COLLECT PARAMS BOOL OR JUST HAVE EXPRESSION LIST HANDLE IT
      // the return type matches
      // propagate return type
      // TODO Maybe take another look at this one
      n.f0.accept(this,v);
      STVariableNode i = currentNode.Parent.isVar(currentID);
      i.vtype.equals(VarType.INSTANCE);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!i.MapToInitialized.containsKey(currentID)){
         //TODO throw exception if false
      }
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      return r;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public VarType visit(ExpressionList n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public VarType visit(ExpressionRest n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
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
   public VarType visit(PrimaryExpression n, VarType v) {
      VarType r =null;
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public VarType visit(IntegerLiteral n, VarType v) {
      VarType r =null;
      //TODO propagate
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "true"
    */
   public VarType visit(TrueLiteral n, VarType v) {
      VarType r =null;
      //TODO propagate
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "false"
    */
   public VarType visit(FalseLiteral n, VarType v) {
      VarType r =null;
      //TODO propagate
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public VarType visit(Identifier n, VarType v) {
      VarType r =null;
      //TODO propagate
      //TODO rule 39 unsure if this requires anything
      currentID = n.f0.toString();
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "this"
    */
   public VarType visit(ThisExpression n, VarType v) {
      VarType r =null;
      //TODO rule 40 unsure if this requires anything
      n.f0.accept(this,v);
      return r;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public VarType visit(ArrayAllocationExpression n, VarType v) {
      VarType r =null;
      //7.7 TYPECHECK
      // f3 is an int
      // TODO if not handled by class or method, check existing
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      return r;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public VarType visit(AllocationExpression n, VarType v) {
      VarType r =null;
      //TODO propagate
      //Class existence handled in symboltable
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      return r;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public VarType visit(NotExpression n, VarType v) {
      VarType r =null;
      // bool
      // propagate bool
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      return r;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public VarType visit(BracketExpression n, VarType v) {
      VarType r =null;
      //TODO propagate
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      return r;
   }
    
}

