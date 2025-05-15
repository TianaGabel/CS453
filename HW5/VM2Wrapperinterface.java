import java.util.HashMap;

public interface VM2Wrapperinterface {
    public void setStackVars(int in, int out, int locals);
    public void resetReallocCounts();
    public String getPrologue();
    public String getEpilogue();

    public String endOfProgram();
}
