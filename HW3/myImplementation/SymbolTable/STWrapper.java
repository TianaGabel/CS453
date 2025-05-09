package myImplementation.SymbolTable;

//VarType won't compile as a type for the visitor so heres a bad comprimise
// TODO
public class STWrapper implements STWinterface{
    public VarType propagatedType = null;

    public STWrapper(){}

    public void setPT(VarType v){
        propagatedType = v;
    }

    public VarType getPT(){
        return propagatedType;
    }

    public boolean is(VarType v){
        return propagatedType == v;
    }
}
