class simpleA {
    public static void main(String[] args) {
        A a;
        a = new A();
    }
}

class A{
    int result;
    public int doSomething(){
        return 0;
    }
}

class B extends A{
    int otherVar;
}