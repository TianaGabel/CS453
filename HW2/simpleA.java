class simpleA {
    public static void main(String[] args) {
        System.out.println(new Twenty().increaseResult(2));
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

    public int reset(){
        result = 0;
        return result;
    }
}
