class M {
    public static void main(String[] arg){
        System.out.println(new B().bar(2,5));
    }
}
class A {
    int boo;
    public int foo() {return 1;}
    public int bar(int a,int b) {
        int d;
        int c;
        A classy;
        classy = new A();
        System.out.println(classy.foo());
        boo = 5;
        d = a;
        c = b;
        return d*c; 
    }


}

class B extends A{
    public int foo() {
        A a;
        a = new A();
        return 5;
    }
}

class C extends B{
    int d;
    int f;
}

