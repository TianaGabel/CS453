import java.util.HashMap;
import java.util.Set;

public class VMWrapper implements VMWrapperinterface{

    HashMap<String,String> localVarToRegister = new HashMap();
    int registersInUse = 0;   //  $t0 -$t08
    int localArrayInUse = 0;

    public void addVarToRegister(String id){
        if (registersInUse < 8){
            String r = "$t0";
            if (registersInUse != 0){r += registersInUse;}

            localVarToRegister.put(id,r);
            registersInUse++;
        } else {
            String r = "local[" + localArrayInUse + "]";
            localVarToRegister.put(id,r);
            localArrayInUse++;
        }
        //it either gets a spot in one of the t0$ registers
        //or gets a spot in the local array
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

        return localVarToRegister.get(id);
    }

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

    public void incrIn(){inCount++;}
    public void incrOut(){outCount++;}
    public void incrLocal(){localCount++;}
    public void incrIn(int a){inCount+= Math.max(a,0);}
    public void incrOut(int a){outCount+=a;}
    public void incrLocal(int a){localCount+=a;}

    public String getFunctionDeclartion(String id){
        int[] temp = reallocMap.get(id);
        String s = "\nfunc " + id + " [in " + temp[0] + ",out " + temp[1] + ",local " + temp[2] + "]";
        return s; 
    }
}
