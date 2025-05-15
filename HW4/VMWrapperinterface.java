import java.util.HashMap;

public interface VMWrapperinterface {

    public void addVarToRegister(String id);
    public String getVarAsRegister(String id);
    public void backupToLocals();
    public void restoreLocals();

    public void addFunction(String id);
    public void resetReallocCounts();

    public void incrIn(int a);
    public void incrOut(int a);
    public void incrLocal(int a);

    public void setIsNotMethod();
    public boolean isNotMethod();

    public String getFunctionDeclartion(String id);
}
