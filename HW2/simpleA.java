class simpleA {
    public static void main(String[] args) {
        System.out.println(new Twenty().increaseResult(2));
    }
}

class Twenty{
    int result;
    public int generate(){
        int a;
        int b;
        int[] d;
        a = 0;
        b = ((2*a)+a)-1;
        d = new int[5];
        result = 10 + 20;
        return result;
    }

    public int increaseResult(int x){
        result = result + x;
        return result;
    }

    public int reset(){
        result = 0;
        return result;
    }
}
