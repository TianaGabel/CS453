package myImplementation;
import visitor.*;
import java.util.*;
import syntaxtree.*;
import myImplementation.SymbolTable.*;
import myImplementation.SymbolTable.VarType;

public class TypeCheckSimp<W extends STWinterface> extends GJVoidDepthFirst<W>{
   
   SymbolTableNode root;
   SymbolTableNode currentNode;
   String currentID;

   public TypeCheckSimp(SymbolTableNode r){
      root = r;
   }

   public void checkDistinct(){
      checkDistinct(root);
      for (SymbolTableNode classNode: root.getChildren()){
         checkDistinct(classNode);
         for (SymbolTableNode methodNode: classNode.getChildren()){
            if (methodNode instanceof STMethodNode){
               checkDistinct(methodNode); //children includes params
            }
         }
      }
   }

   //HELPER FUNCTION
   //are all childern's ids unique
   private void checkDistinct(SymbolTableNode parent){
      for(SymbolTableNode child: parent.getChildren()){
         for(SymbolTableNode other: parent.getChildren()){
            if(child.identifier.equals(other.identifier)){
               //TODO throw error
            }
         }
      }
   }

   //TODO unnecessary
   // public void checkDistinct(ArrayList<STVariableNode> params){
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
   public void visit(NodeList n, W  v) {
      /**/
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this,v);
      
      /*r*/
      }

   public void visit(NodeListOptional n, W  v) {
      /**/
      if ( n.present() ){
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this,v);
      }
      /*r*/
   }

   public void visit(NodeOptional n, W  v) {
      /**/
      if ( n.present() )
         n.node.accept(this,v);
      /*r*/
   }

   public void visit(NodeSequence n, W  v) {
      /**/
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this,v);
      /*r*/
   }

   public void visit(NodeToken n, W  v) { }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public void visit(Goal n, W  v) {
      /**/
      //7.2 TYPECHECK
      // are the class names unique? including main

      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      /*r*/
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
   public void visit(MainClass n, W  v) {
      /**/
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
      /*r*/
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public void visit(TypeDeclaration n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public void visit(ClassDeclaration n, W  v) {
      /**/
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
      /*r*/
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
   public void visit(ClassExtendsDeclaration n, W  v) {
      /**/
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
      /*r*/
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public void visit(VarDeclaration n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      /*r*/
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
   public void visit(MethodDeclaration n, W  v) {
      /**/
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
      /*r*/
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public void visit(Type n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public void visit(ArrayType n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "boolean"
    */
   public void visit(BooleanType n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "int"
    */
   public void visit(IntegerType n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public void visit(Statement n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public void visit(Block n, W  v) {
      /**/
      // 7.6 22 TYPECHECK
      // do children typecheck?
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public void visit(AssignmentStatement n, W  v) {
      /**/
      // 7.6 23 TYPECHECK
      // is id a variable? (done)
      // is expression the same type as id? (done)
      // TODO ?? unsure what the last line means
      
      n.f0.accept(this,v);
      currentNode.isVar(currentID);
      VarType typeOfID = v.getPT();
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(typeOfID)){
         //TODO tne
      }
      n.f3.accept(this,v);

      /*r*/
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
   public void visit(ArrayAssignmentStatement n, W  v) {
      /**/
      // 7.6 TYPECHECK
      // is id an array? (done)
      // is expression in [] an int? (done)
      // is assignment expression an int (done)

      n.f0.accept(this,v);
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f6.accept(this,v);
      /*r*/
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
   public void visit(IfStatement n, W  v) {
      /**/
      // 7.6 TYPECHECK
      // is f2 a bool
      // do the children typecheck?
      // TODO accounting for uninitialized variables
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.BOOL)){
         //TODO tne
      }
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      n.f5.accept(this,v);
      n.f6.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n, W  v) {
      /**/
      // 7.6 TYPECHECK
      // is f2 a bool
      // do the children typecheck?
      // TODO accounting for uninitialized variables if skipped
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.BOOL)){
         //TODO tne
      }
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public void visit(PrintStatement n, W  v) {
      /**/
      // 7.6 TYPECHECK
      // is f2 an int?
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f3.accept(this,v);
      n.f4.accept(this,v);
      /*r*/
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
   public void visit(Expression n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // both type bool
      // propagate bool

      n.f0.accept(this,v);
      if (!v.is(VarType.BOOL)){
         //TODO tne
      }
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.BOOL)){
         //TODO tne
      }
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public void visit(CompareExpression n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // both type int
      // propagate bool
      n.f0.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      v.setPT(VarType.BOOL);
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // both type int
      // propagate int
      n.f0.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      //Already int
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public void visit(ArrayLookup n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // f0 array (done)
      // f2 int
      // propagate int
      n.f0.accept(this,v);
      if (!v.is(VarType.ARRAY)){
         //TODO tne
      }
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      if (!v.is(VarType.INT)){
         //TODO tne
      }
      n.f3.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public void visit(ArrayLength n, W  v) {
      /**/
      // 7.7 TYPECHECK
      // f0 array (done)
      // propagate int
      n.f0.accept(this,v);
      if (!v.is(VarType.ARRAY)){
         //TODO tne
      }
      currentNode.isVar(currentID).vtype.equals(VarType.ARRAY);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      v.setPT(VarType.INT);
      /*r*/
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public void visit(MessageSend n, W  v) {
      /**/
      // 7.7 35 TYPECHECK
      // f0 is an instance (done)
      // f2 is a method (done)
      // they go together
      // the arguements match params TODO
      // MAYBE A COLLECT PARAMS BOOL OR JUST HAVE EXPRESSION LIST HANDLE IT
      // the return type matches
      // propagate return type (done)
      // TODO Maybe take another look at this one
      n.f0.accept(this,v);
      if (v.getPT() == VarType.INSTANCE){
         //Case instance was just created
         STClassNode i = (STClassNode)root.findExisitingSymbol(currentID);
         n.f1.accept(this,v);
         n.f2.accept(this,v);
         String m = currentID;
         if (i.findExisitingSymbol(m) == null){
            //TODO tne
         }
         n.f3.accept(this,v);
         n.f4.accept(this,v);
         n.f5.accept(this,v);
         v.setPT(((STMethodNode)i.findExisitingSymbol(m)).returnType);
      }else {
         //Case instance was preexisitng
         STVariableNode i = (STVariableNode)root.findExisitingSymbol(currentID);
         i.vtype.equals(VarType.INSTANCE);
         n.f1.accept(this,v);
         n.f2.accept(this,v);
         String m = currentID;
         if (!i.MapToInitialized.containsKey(m)){
            //TODO throw exception if false
         }
         n.f3.accept(this,v);
         n.f4.accept(this,v);
         n.f5.accept(this,v);
         v.setPT(((STMethodNode)i.classType.findExisitingSymbol(m)).returnType);
      }
      /*r*/
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n, W  v) {
      /**/
      //TODO probably handled by st but check
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      /*r*/
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
   public void visit(PrimaryExpression n, W  v) {
      /**/
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n, W  v) {
      /**/

      v.setPT(VarType.INT);
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n, W  v) {
      /**/
      n.f0.accept(this,v);
      v.setPT(VarType.BOOL);
      /*r*/
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n, W  v) {
      /**/
      n.f0.accept(this,v);
      v.setPT(VarType.BOOL);
      /*r*/
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n, W  v) {
      /**/
      //TODO propagate ???? do we want to handle existence here
      //TODO rule 39 unsure if this requires anything
      currentID = n.f0.toString();
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n, W  v) {
      /**/
      //TODO rule 40 unsure if this requires anything
      n.f0.accept(this,v);
      /*r*/
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public void visit(ArrayAllocationExpression n, W  v) {
      /**/
      //7.7 TYPECHECK
      // f3 is an int
      // TODO if not handled by class or method, check existing
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      if (v.is(VarType.INT)){
         //TODO tne
      }
      n.f4.accept(this,v);
      v.setPT(VarType.ARRAY);
      /*r*/
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n, W  v) {
      /**/
      //propagate
      //TODO Class existence handled in symboltable
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      n.f3.accept(this,v);
      v.setPT(VarType.INSTANCE);
      /*r*/
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n, W  v) {
      /**/
      // bool
      // propagate bool
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      if (!v.is(VarType.BOOL)){
         //TODO throw exception
      }
      /*r*/
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public void visit(BracketExpression n, W  v) {
      /**/
      n.f0.accept(this,v);
      n.f1.accept(this,v);
      n.f2.accept(this,v);
      /*r*/
   }
    
}

