class simpleA {
    public static void main(String[] args) {
        System.out.println(new Twenty().generate());
    }
}

class Twenty{
    int result;
    public int generate(){
        result = 10 + 20;
        return result;
    }

    public int increaseResult(int x){
        result = result + x;
        return result;
    }

    public int bar() {
    int v;
    v = 0;
    while (v < 10) {
        while (v < 4) {
            v = v + 1;
        }
    v = v + 1;
    }
    return v;
    }

    public int foo(){
    int c;
    if (1 < 20)
     c =9;
    else
    c=1;
    return c;
    }
        
}