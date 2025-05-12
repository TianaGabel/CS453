import java.util.HashMap;
import java.util.Set;

public class VMWrapper implements VMWrapperinterface{

    HashMap<String,String> localVarToRegister = new HashMap();
    int registersInUse = 0;   //  $t0 -$t07
    int sRegistersInUse = 0;   //  $s0 -$s07
    int localArrayInUse = 0;

    public void addVarToRegister(String id){
        if (localVarToRegister.keySet().contains(id)){
            return;
        }
        if (registersInUse < 8){
            String r = "$t" + registersInUse;

            localVarToRegister.put(id,r);
            registersInUse++;
        } 
        // else if (sRegistersInUse < 8){
        //     String r = "$s" + sRegistersInUse;

        //     localVarToRegister.put(id,r);
        //     sRegistersInUse++;
        // } 
        else {
            String r = "local[" + localArrayInUse + "]";
            String newRegister = "$t7";
            System.out.println(r + " = " + newRegister);
            localVarToRegister.put(id,r);
            swapMapEntriesByRegister(newRegister, r);
            localArrayInUse++;
        }
        //it either gets a spot in one of the t0$ registers
        //or pushes out t7$
    }

    public String addVarToLocals(String id){
        String r = "local[" + localArrayInUse + "]";
        localVarToRegister.put(id,r);
        localArrayInUse++;
        return r;
    }

    public void resetRegisters(){
        registersInUse = 0;
        Object[] vars = localVarToRegister.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            if (localVarToRegister.get((String)vars[i]).substring(0,2).equals("$t")){
                localVarToRegister.remove((String)vars[i]);
            }
        }
    }

    public void resetSRegisters(){
        sRegistersInUse = 0;
        Object[] vars = localVarToRegister.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            if (localVarToRegister.get((String)vars[i]).substring(0,2).equals("$s")){
                localVarToRegister.remove((String)vars[i]);
            }
        }
    }

    public void resetLocals(){
        localArrayInUse = 0;
        Object[] vars = localVarToRegister.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            if (localVarToRegister.get((String)vars[i]).substring(0,2).equals("lo")){
                localVarToRegister.remove((String)vars[i]);
            }
        }
    }

    public String getVarAsRegister(String id){
        //if not in map add it
        //either return the current register in use for that var
        //or do a swap for a register spot from local spot
        //$swapRegister(look up name maybe?) = $t07
        //$t07 = local[4]
        //local[4] = $swapRegister
        //return "$t07"
        //update hashmpa
        if (!localVarToRegister.keySet().contains(id)){
            addVarToRegister(id);
        }
        String currentLoc =localVarToRegister.get(id);
        if (!checkIsRegisterName(currentLoc)){
            //swap logic TODO if you end up doing any liveness it would pick the newRegister to use
            //Case empty registers
            if (registersInUse < 8){
                //Case empty registers
                String newRegister = "$t" + registersInUse;
                System.out.println(newRegister + " = " + currentLoc);
                localVarToRegister.remove(id);
                localVarToRegister.put(id,newRegister);
                registersInUse++;
            } 
            // else if (sRegistersInUse < 8){
            //     //Case empty registers
            //     String newRegister = "$s" + sRegistersInUse;
            //     System.out.println(newRegister + " = " + currentLoc);
            //     localVarToRegister.remove(id);
            //     localVarToRegister.put(id,newRegister);
            //     sRegistersInUse++;
            // } 
            else {
            //Case no empty registers
            String newRegister = "$t7";
            System.out.println("$v1 = " + newRegister);
            System.out.println(newRegister + " = " + currentLoc);
            System.out.println(currentLoc + " = $v1");
            swapMapEntriesByRegister(newRegister, currentLoc);
            }
        }

        return localVarToRegister.get(id);
    }

    private boolean checkIsRegisterName(String id){
        return (id.substring(0, 2).equals("$t")) || id.substring(0, 2).equals("$s");
    }

    private void swapMapEntriesByRegister(String reg1, String reg2){
        String varName1 = "";
        String varName2 = "";

        Object[] vars = localVarToRegister.keySet().toArray();
        for(int i=0; i < vars.length; i++){ //TODO break early
            if (localVarToRegister.get((String)vars[i]).equals(reg1)){
                varName1 = (String)vars[i];
            }
            if (localVarToRegister.get((String)vars[i]).equals(reg2)){
                varName2 = (String)vars[i];
            }
        }

        localVarToRegister.put(varName1,reg2);
        localVarToRegister.put(varName2,reg1);
    }

    public void backupToLocals(){
        // remove the variables in registers and readd to locals
        // also print that this is being done
        // reset registers afterwards
        Object[] vars = localVarToRegister.keySet().toArray();
        String register;
        String newLoc;
        for(int i=0; i < vars.length; i++){
            if (checkIsRegisterName(localVarToRegister.get((String)vars[i]))){
                register = localVarToRegister.get((String)vars[i]);
                localVarToRegister.remove((String)vars[i]);
                newLoc = addVarToLocals((String)vars[i]);
                System.out.println(newLoc + " = " + register);
            }
        }
        resetSRegisters();
        resetRegisters();
    }


    //Allocation before stuff
    HashMap<String,int[]> reallocMap = new HashMap();
    int inCount;
    int outCount;
    int localCount;

    public void addFunction(String id){
        int[] temp = new int[3];
        temp[0] = inCount;
        temp[1] = outCount;
        temp[2] = localCount;
        reallocMap.put(id,temp);
        resetReallocCounts();
    }

    public void resetReallocCounts(){
        inCount = 0;
        outCount = 0;
        localCount = 0;
    }

    public void incrIn(int a){inCount+= Math.max(a,0);}
    public void incrOut(int a){outCount=Math.max(a, outCount);}
    public void incrLocal(int a){localCount=Math.max(a,localCount);}

    public String getFunctionDeclartion(String id){
        int[] temp = reallocMap.get(id);
        String s = "func " + id + " [in " + temp[0] + ",out " + temp[1] + ",local " + temp[2] + "]";
        return s; 
    }

    public void addArray(){
        System.out.println("  $t0 = $a1\r\n" + 
                        "  $t1 = MulS($t0 4)\r\n" + 
                        "  $t1 = Add($t1 4)\r\n" + //
                        "  $t1 = HeapAllocZ($t1)\r\n" + //
                        "  [$t1] = $t0\r\n" + //
                        "  $v0 = $t1\r\n" + //
                        "  ret");
    }
}
