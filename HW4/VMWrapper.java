import java.util.HashMap;
import java.util.Set;

public class VMWrapper implements VMWrapperinterface{

    //id, register
    HashMap<String,String> varsToLocals = new HashMap<>(); //basically each has a set location in local
    HashMap<String,String> localVarToRegister = new HashMap();
    HashMap<String,String> prevState = new HashMap();
    int registersInUse = 0;   //  $t0 -$t07
    int sRegistersInUse = 0;   //  $s0 -$s07
    //int localArrayInUse = 0;
    boolean rotate = false;
    boolean rotate2 = false;
    int MAXREGISTERS = 8;
    int MAXSREGISTERS = 8;

    public void initLocals(String[] vars){
        int count = 0;
        String r;
        for(String v: vars){
            r = "local[" + count + "]";
            varsToLocals.put(v.toString(),r);
            count++;
        }
        //printMap(varsToLocals);
    }

    public void printMap(HashMap<String,String> m){
        Object[] vars = m.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            System.out.println("--" +(String)vars[i] + " " + m.get((String)vars[i]));
        }
    }

    public void addVarToRegister(String id){
        if (localVarToRegister.keySet().contains(id)){
            return;
        }
        if (registersInUse < MAXREGISTERS){
            String r = "$t" + registersInUse;

            localVarToRegister.put(id,r);
            registersInUse++;
        } 
        else if (sRegistersInUse < MAXSREGISTERS){
            String r = "$s" + sRegistersInUse;

            localVarToRegister.put(id,r);
            sRegistersInUse++;
        } 
        else {
            String newRegister = "$t5";
            if (rotate2){newRegister = "$t4";}
            rotate2 = !rotate2;

            String varAtNewRegister = getVarName(newRegister);
            if (varAtNewRegister == null){
                localVarToRegister.put(id, newRegister);
            } else {
                String varLocalLoc = varsToLocals.get(varAtNewRegister);
                //System.out.println(varLocalLoc + " = " + newRegister);
                print(varLocalLoc + " = " + newRegister);
                localVarToRegister.put(varAtNewRegister,varLocalLoc);
                localVarToRegister.put(id, newRegister);
            }
        }
        //it either gets a spot in one of the t0$ registers
        //or pushes out 5 or 4 to it's spot in local
    }

    public String addVarToLocals(String id){
        String varLocalLoc = varsToLocals.get(id);
        localVarToRegister.put(id,varLocalLoc);
        return varLocalLoc;
    }

    public String getVarName(String r){
        //System.out.println(r + "------BOoo");
        Object[] vars = localVarToRegister.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            //System.out.println(localVarToRegister.get((String)vars[i]) + "------BOoo");
            if (localVarToRegister.get((String)vars[i]).equals(r)){
                return (String)vars[i];
            }
        }
        return null;
    }
    
    public void resetAll(){
        resetLocals();
        resetRegisters();
        resetSRegisters();
        localVarToRegister.clear();
    }

    public void resetRegisters(){
        registersInUse = 0;
        Object[] vars = localVarToRegister.keySet().toArray();  //fix for java's concurrent mod exception
        for(int i=0; i < vars.length; i++){
            if (localVarToRegister.get((String)vars[i]).substring(0,1).equals("$")){
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
        varsToLocals.clear();
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
        //printMap(localVarToRegister);
        String currentLoc =localVarToRegister.get(id);
        if (!checkIsRegisterName(currentLoc)){
            //swap logic TODO if you end up doing any liveness it would pick the newRegister to use
            //Case empty registers
            if (registersInUse < MAXREGISTERS){
                //Case empty registers
                String newRegister = "$t" + registersInUse;
                //System.out.println(newRegister + " = " + currentLoc);
                print(newRegister + " = " + currentLoc);
                localVarToRegister.remove(id);
                localVarToRegister.put(id,newRegister);
                registersInUse++;
            } 
            else if (sRegistersInUse < MAXSREGISTERS){
                //Case empty registers
                String newRegister = "$s" + sRegistersInUse;
                //System.out.println(newRegister + " = " + currentLoc);
                print(newRegister + " = " + currentLoc);
                localVarToRegister.remove(id);
                localVarToRegister.put(id,newRegister);
                sRegistersInUse++;
            } 
            else {
                // backupToLocals();
                // return getVarAsRegister(id);
            //Case no empty registers

            String newRegister = "$t3";
            if (rotate){newRegister = "$t2";}
            rotate = !rotate;
            
            String varAtNewRegister = getVarName(newRegister);
            // if (varAtNewRegister == null){
            //     return "boo";
            // }
            String varLocalLoc = varsToLocals.get(varAtNewRegister);
            print(varLocalLoc + " = " + newRegister);
            print(newRegister + " = " + localVarToRegister.get(id));

            localVarToRegister.put(varAtNewRegister,varLocalLoc);
            localVarToRegister.put(id, newRegister);
            }
        }
        // System.out.println("---------------");
        // printMap();
        return localVarToRegister.get(id);
    }

    private boolean checkIsRegisterName(String id){
        return (id.substring(0, 1).equals("$"));
        //return (id.substring(0, 2).equals("$t")) || id.substring(0, 2).equals("$s");
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
                String varLocalLoc = varsToLocals.get((String)vars[i]);
                print(varLocalLoc + " = " + register);
                localVarToRegister.put((String)vars[i], varLocalLoc);
            }
        }
        resetSRegisters();
        resetRegisters();
    }

    public void restoreLocals(){
        // NOT USED, the only potential use is just post a while loop, but otherwise the registers will be reallocated
        Object[] vars = prevState.keySet().toArray();
        String loc;
        String newRegister;
        for(int i=0; i < vars.length; i++){
            if (checkIsRegisterName(prevState.get((String)vars[i]))){ //look for registers in the prevstate
                loc = localVarToRegister.get((String)vars[i]); //current loc
                localVarToRegister.remove((String)vars[i]); 
                newRegister = prevState.get((String)vars[i]); //past/future loc
                System.out.println(newRegister + " = " + loc);
                localVarToRegister.put((String)vars[i],newRegister);
            }
        }
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

    private void print(String line){
        String INDENT = "   ";
        System.out.println(INDENT + line);
    }
}
