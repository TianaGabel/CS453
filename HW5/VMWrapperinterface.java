import java.util.HashMap;

public interface VMWrapperinterface {
    public void setStackVars(int in, int out, int locals);
    public void resetReallocCounts();
    public String getPrologue();
    public String getEpilogue();

    public String endOfProgram();
}
