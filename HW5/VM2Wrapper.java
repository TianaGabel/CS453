import java.util.HashMap;
import java.util.Set;

public class VM2Wrapper implements VMWrapperinterface{
    int inCount;
    int outCount;
    int localCount;
    
    public void setStackVars(int in, int out, int locals){
        inCount = in;
        outCount = out;
        localCount = locals;
    }
    public void resetReallocCounts(){
        inCount = 0;
        outCount = 0;
        localCount = 0;
    }

    public String getPrologue(){
        String setFp = "  sw $fp -8($sp)\n";
        String setRa = "  sw $ra -4($sp)\n";
        String moveFp = "  move $fp $sp\n";
        String moveSp = "  subu $sp $sp " + (4*(localCount + outCount + 2));
        return setFp + setRa + moveFp + moveSp;
    }
    public String getEpilogue(){
        String setRa = "  lw $ra -4($fp)\n";
        String setFp = "  lw $fp -8($fp)\n";
        String moveSp = "  addu $sp $sp " + (4*(localCount + outCount + 2));
        return setRa + setFp + moveSp;
    }

    public String endOfProgram(){
        String s =  "_print:\n" + //
                        "  li $v0 1   # syscall: print integer\n" + //
                        "  syscall\n" + //
                        "  la $a0 _newline\n" + //
                        "  li $v0 4   # syscall: print string\n" + //
                        "  syscall\n" + //
                        "  jr $ra\n" + //
                        "\n" + //
                        "_error:\n" + //
                        "  li $v0 4   # syscall: print string\n" + //
                        "  syscall\n" + //
                        "  li $v0 10  # syscall: exit\n" + //
                        "  syscall\n" + //
                        "\n" + //
                        "_heapAlloc:\n" + //
                        "  li $v0 9   # syscall: sbrk\n" + //
                        "  syscall\n" + //
                        "  jr $ra\n" + //
                        "\n" + //
                        ".data\n" + //
                        ".align 0\n" + //
                        "_newline: .asciiz \"\\n" + //
                        "\"\n" + //
                        "_str0: .asciiz \"misc error\"\n";
        return s;
    }
}
